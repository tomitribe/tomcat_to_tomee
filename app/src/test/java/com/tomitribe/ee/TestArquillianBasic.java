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
package com.tomitribe.ee;

import com.tomitribe.ee.ejb.EjbSingleton;
import com.tomitribe.ee.ejb.EjbStateful;
import com.tomitribe.ee.ejb.IEjbStateful;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.io.IOException;

/**
 * ARQUILLIAN STEP 9 - Our first, and very basic, Arquillian test
 */
@RunWith(Arquillian.class)
public class TestArquillianBasic {


    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class, TestArquillianBasic.class.getName() + ".war") //Name is just convenient
                .addClasses(TestArquillianBasic.class, EjbStateful.class, IEjbStateful.class, EjbSingleton.class) //Add our classes to test
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); //Turn on CDI
    }

    @EJB
    private IEjbStateful stateful;

    @Test
    @InSequence(1)
    public void test1() throws IOException {
        //This stateful instance...
        Assert.assertEquals("Value is not 1", 1, stateful.getId());
        Assert.assertEquals("Value is not 1", 1, stateful.incrementAndGet());
        Assert.assertEquals("Value is not 2", 2, stateful.incrementAndGet());
        Assert.assertEquals("Value is not 1", 1, stateful.decrementAndGet());
    }

    @Test
    @InSequence(2)
    public void test2() throws IOException {
        //Is not the same as that stateful instance.
        Assert.assertEquals("Value is not 1", 2, stateful.getId());
        Assert.assertEquals("Value is not 1", 1, stateful.incrementAndGet());
        Assert.assertEquals("Value is not 2", 2, stateful.incrementAndGet());
        Assert.assertEquals("Value is not 1", 1, stateful.decrementAndGet());
    }

    @Test
    @InSequence(3)
    public void test3() throws IOException {
        //Assert.assertEquals("Value is not 1", 1, stateful.decrementAndGet());
    }
}
