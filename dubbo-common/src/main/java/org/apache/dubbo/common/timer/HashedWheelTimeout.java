package org.apache.dubbo.common.timer;

import org.apache.dubbo.common.logger.ErrorTypeAwareLogger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ClassUtils;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import static org.apache.dubbo.common.constants.LoggerCodeConstants.COMMON_ERROR_RUN_THREAD_TASK;

public final class HashedWheelTimeout implements Timeout {

    private static final ErrorTypeAwareLogger logger = LoggerFactory.getErrorTypeAwareLogger(HashedWheelTimeout.class);
    private static final int ST_INIT = 0;
    private static final int ST_CANCELLED = 1;
    private static final int ST_EXPIRED = 2;
    private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");

    private final HashedWheelTimer timer;
    private final TimerTask task;



    private final long deadline;

    @SuppressWarnings({"unused", "FieldMayBeFinal", "RedundantFieldInitialization"})
    private volatile int state = ST_INIT;

    /**
     * RemainingRounds will be calculated and set by Worker.transferTimeoutsToBuckets() before the
     * HashedWheelTimeout will be added to the correct HashedWheelBucket.
     */
    long remainingRounds;

    /**
     * This will be used to chain timeouts in HashedWheelTimerBucket via a double-linked-list.
     * As only the workerThread will act on it there is no need for synchronization / volatile.
     */
    HashedWheelTimeout next;
    HashedWheelTimeout prev;

    /**
     * The bucket to which the timeout was added
     */
    HashedWheelBucket bucket;

    HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
        this.timer = timer;
        this.task = task;
        this.deadline = deadline;
    }

    public HashedWheelTimer getTimer() {
        return timer;
    }

    public int getStCancelled(){
        return ST_CANCELLED;
    }

    public long getDeadline() {
        return deadline;
    }

    @Override
    public Timer timer() {
        return timer;
    }

    @Override
    public TimerTask task() {
        return task;
    }

    @Override
    public boolean cancel() {
        // only update the state it will be removed from HashedWheelBucket on next tick.
        if (!compareAndSetState(ST_INIT, ST_CANCELLED)) {
            return false;
        }
        // If a task should be canceled we put this to another queue which will be processed on each tick.
        // So this means that we will have a GC latency of max. 1 tick duration which is good enough. This way we
        // can make again use of our LinkedBlockingQueue and so minimize the locking / overhead as much as possible.
        timer.getCancelledTimeouts().add(this);
        return true;
    }

    void remove() {
        HashedWheelBucket bucket = this.bucket;
        if (bucket != null) {
            bucket.remove(this);
        } else {
            timer.getPendingTimeouts().decrementAndGet();
        }
    }

    public boolean compareAndSetState(int expected, int state) {
        return STATE_UPDATER.compareAndSet(this, expected, state);
    }

    public int state() {
        return state;
    }

    @Override
    public boolean isCancelled() {
        return state() == ST_CANCELLED;
    }

    @Override
    public boolean isExpired() {
        return state() == ST_EXPIRED;
    }

    public void expire() {
        if (!compareAndSetState(ST_INIT, ST_EXPIRED)) {
            return;
        }

        try {
            task.run(this);
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn(COMMON_ERROR_RUN_THREAD_TASK, "", "", "An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
            }
        }
    }

    @Override
    public String toString() {
        final long currentTime = System.nanoTime();
        long remaining = deadline - currentTime + timer.startTime;
        String simpleClassName = ClassUtils.simpleClassName(this.getClass());

        StringBuilder buf = new StringBuilder(192)
                .append(simpleClassName)
                .append('(')
                .append("deadline: ");
        if (remaining > 0) {
            buf.append(remaining)
                    .append(" ns later");
        } else if (remaining < 0) {
            buf.append(-remaining)
                    .append(" ns ago");
        } else {
            buf.append("now");
        }

        if (isCancelled()) {
            buf.append(", cancelled");
        }

        return buf.append(", task: ")
                .append(task())
                .append(')')
                .toString();
    }
}
