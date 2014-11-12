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
 * EJB
 */
package com.tomitribe.ee.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

@Stateful
public class EjbStateful implements IEjbStateful{

    private final AtomicInteger id = new AtomicInteger(0);
    private final AtomicInteger count = new AtomicInteger(0);

    @EJB
    private EjbSingleton singleton;

    @PostConstruct
    public void postConstruct() {
        id.set(singleton.getNextId());
    }

    @Override
    public int getId() {
        return id.get();
    }

    @Override
    public int get() {
        return count.get();
    }

    public boolean compareAndSet(final int expect, final int update) {
        return count.compareAndSet(expect, update);
    }

    public double doubleValue() {
        return count.doubleValue();
    }

    public int intValue() {
        return count.intValue();
    }

    public float floatValue() {
        return count.floatValue();
    }

    public int addAndGet(final int delta) {
        return count.addAndGet(delta);
    }

    public int getAndDecrement() {
        return count.getAndDecrement();
    }

    public byte byteValue() {
        return count.byteValue();
    }

    public int getAndSet(final int newValue) {
        return count.getAndSet(newValue);
    }

    public short shortValue() {
        return count.shortValue();
    }

    @Override
    public int incrementAndGet() {
        return count.incrementAndGet();
    }

    @Override
    public int decrementAndGet() {
        return count.decrementAndGet();
    }

    public int accumulateAndGet(final int x, final IntBinaryOperator accumulatorFunction) {
        return count.accumulateAndGet(x, accumulatorFunction);
    }

    public boolean weakCompareAndSet(final int expect, final int update) {
        return count.weakCompareAndSet(expect, update);
    }

    public int getAndAdd(final int delta) {
        return count.getAndAdd(delta);
    }

    public void set(final int newValue) {
        count.set(newValue);
    }

    public int getAndUpdate(final IntUnaryOperator updateFunction) {
        return count.getAndUpdate(updateFunction);
    }

    public void lazySet(final int newValue) {
        count.lazySet(newValue);
    }

    public int getAndAccumulate(final int x, final IntBinaryOperator accumulatorFunction) {
        return count.getAndAccumulate(x, accumulatorFunction);
    }

    public int updateAndGet(final IntUnaryOperator updateFunction) {
        return count.updateAndGet(updateFunction);
    }

    public long longValue() {
        return count.longValue();
    }

    @Override
    public int getAndIncrement() {
        return count.getAndIncrement();
    }
}
