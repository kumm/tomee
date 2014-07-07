/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.openejb.tool.junit;

import org.apache.openejb.util.Pipe;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OpenEJBJUnitDebugListener extends RunListener {
    private static final String OS = System.getProperty("os.name", "unknown");
    private static final boolean UNIX = !OS.toLowerCase(Locale.ENGLISH).startsWith("windows");
    static {
        System.out.println(">>> will debug - unix? " + UNIX + " (" + OS + ")");
    }

    private MonitoringThread thread;

    @Override
    public void testRunStarted(final Description description) throws Exception {
        if (!UNIX) {
            return;
        }
        thread = new MonitoringThread();
        thread.start();
    }

    @Override
    public void testRunFinished(final Result result) throws Exception {
        doStop();
    }

    @Override
    public void testFailure(final Failure failure) throws Exception {
        doStop();
    }

    @Override
    public void testAssumptionFailure(final Failure failure) {
        try {
            doStop();
        } catch (final InterruptedException e) {
            Thread.interrupted();
        }
    }

    private void doStop() throws InterruptedException {
        if (!UNIX) {
            return;
        }

        if (thread != null) {
            thread.done = true;
            thread.join();
            thread = null;
        }
    }

    public static class MonitoringThread extends Thread {
        private static final long TIMEOUT = TimeUnit.MINUTES.toMillis(5);
        private static final long PAUSE = 50; // ms
        private static final long ITERATIONS = TIMEOUT / PAUSE;

        private volatile boolean done = false;

        @Override
        public void run() {
            long i = ITERATIONS;
            while (!done) {
                try {
                    sleep(PAUSE);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    break;
                }
                i--;
                if (i == 0) {
                    makeSpace();
                    kill3UNIX();
                    makeSpace();
                    i = ITERATIONS;
                }
            }
        }

        private static void makeSpace() {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.flush();
        }

        public static void kill3UNIX() { // debug purpose only
            try {
                final int pid = getPid();
                Pipe.pipe(Runtime.getRuntime().exec("kill -3 " + pid));
            } catch (final Exception e1) {
                e1.printStackTrace();
            }
        }

        public static int getPid() throws Exception {
            final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            final Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            final Object mgmt = jvm.get(runtime);
            final Method pid_method = mgmt.getClass().getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);
            return Number.class.cast(pid_method.invoke(mgmt)).intValue();
        }
    }

}
