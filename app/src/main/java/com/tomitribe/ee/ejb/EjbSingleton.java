/*
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
/**
 * EJB Step 1 - The @javax.ejb.Singleton
 */
package com.tomitribe.ee.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@Startup
public class EjbSingleton {

    private static final AtomicInteger id = new AtomicInteger(0);

    private Timer timer;

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void postConstruct() {
        createTimer();
    }

    @PreDestroy
    public void preDestroy() {
        if (null != timer) {
            try {
                timer.cancel();
            } catch (final Exception e) {
                //Ignore
            }
        }
    }

    private void createTimer() {
        try {
            timer = this.timerService.createSingleActionTimer(15000, new TimerConfig("EjbSingleton.Timer", false));
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    @Timeout
    @Asynchronous
    @Lock(LockType.WRITE)
    public void programmaticTimeout(final Timer timer) {
        System.out.println(list[random.nextInt(list.length)]);
        try {
            Thread.sleep(15000);
        } catch (final InterruptedException e) {
            return;
        }
        createTimer();
    }

    private final static Random random = new Random();

    private static final String[] list = new String[]{
            "Wonder if I can print to the console?",
            "Come and find me, haha!",
            "Boo! that scared you",
            "This bug could spoil your day...",
            "Hmm, this looks broken...",
            "Wake up and find me!",
            "This is annoying I bet!",
    };

    public int getNextId() {
        return id.incrementAndGet();
    }
}
