/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.common.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liaochuntao@live.com">liaochuntao</a>
 */
public final class ThreadUtils {

    public static void objectWait(Object object) {
        try {
            object.wait();
        } catch (InterruptedException ignore) {
            Thread.interrupted();
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void latchAwait(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void latchAwait(CountDownLatch latch, long time, TimeUnit unit) {
        try {
            latch.await(time, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Through the number of cores, calculate the appropriate number of threads;
     * 1.5-2 times the number of CPU cores
     *
     * @return thread count
     */
    public static int getSuitableThreadCount() {
        final int coreCount = Runtime.getRuntime().availableProcessors();
        int workerCount = 1;
        while (workerCount < coreCount * THREAD_MULTIPLER) {
            workerCount <<= 1;
        }
        return workerCount;
    }

    private final static int THREAD_MULTIPLER = 2;

    /**
     * Provide a number between 0(inclusive) and {@code upperLimit}(exclusive) for the given {@code string},
     * the number will be nearly uniform distribution.
     * <p>
     * <p>
     *
     * e.g. Assume there's an array which contains some IP of the servers provide the same service,
     * the caller name can be used to choose the server to achieve load balance.
     * <blockquote><pre>
     *     String[] serverIps = new String[10];
     *     int index = shakeUp("callerName", serverIps.length);
     *     String targetServerIp = serverIps[index];
     * </pre></blockquote>
     *
     * @param string     a string. the number 0 will be returned if it's null
     * @param upperLimit the upper limit of the returned number, must be a positive integer, which means > 0
     * @return a number between 0(inclusive) and upperLimit(exclusive)
     * @throws IllegalArgumentException if the upper limit equals or less than 0
     * @since 1.0.0
     * @author jifengnan
     */
    public static int shakeUp(String string, int upperLimit) {
        if (upperLimit < 1) {
            throw new IllegalArgumentException("upper limit must be greater than 0");
        }
        if (string == null) {
            return 0;
        }
        return (string.hashCode() & Integer.MAX_VALUE) % upperLimit;
    }
}
