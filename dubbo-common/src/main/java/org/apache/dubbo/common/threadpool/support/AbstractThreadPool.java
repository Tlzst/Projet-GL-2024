package org.apache.dubbo.common.threadpool.support;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.threadlocal.NamedInternalThreadFactory;
import org.apache.dubbo.common.threadpool.MemorySafeLinkedBlockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.dubbo.common.constants.CommonConstants.CORE_THREADS_KEY;
import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_CORE_THREADS;
import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_QUEUES;
import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_THREADS;
import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_THREAD_NAME;
import static org.apache.dubbo.common.constants.CommonConstants.QUEUES_KEY;
import static org.apache.dubbo.common.constants.CommonConstants.THREADS_KEY;
import static org.apache.dubbo.common.constants.CommonConstants.THREAD_NAME_KEY;

public abstract class AbstractThreadPool {

    protected Executor createExecutor(URL url){
        String name =
                url.getParameter(THREAD_NAME_KEY, (String) url.getAttribute(THREAD_NAME_KEY, DEFAULT_THREAD_NAME));
        int threads = url.getParameter(THREADS_KEY, DEFAULT_THREADS);
        int queues = url.getParameter(QUEUES_KEY, DEFAULT_QUEUES);

        BlockingQueue<Runnable> blockingQueue;

            if (queues == 0) {
            blockingQueue = new SynchronousQueue<>();
        } else if (queues < 0) {
            blockingQueue = new MemorySafeLinkedBlockingQueue<>();
        } else {
            blockingQueue = new LinkedBlockingQueue<>(queues);
        }

            return new ThreadPoolExecutor(
                getFirstParameter(url),
                threads,
                Long.MAX_VALUE,
                TimeUnit.MILLISECONDS,
                blockingQueue,
                    new NamedInternalThreadFactory(name, true),
                    new AbortPolicyWithReport(name, url));
    }

    protected abstract int getFirstParameter(URL url);

}
