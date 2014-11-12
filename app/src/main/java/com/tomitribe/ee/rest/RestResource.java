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
/**
 * REST Step 1 - Representational state transfer Service.
 * Just add some rest annotations to a Pojo, the @Inject is just icing on the cake.
 */
package com.tomitribe.ee.rest;

import com.tomitribe.ee.cdi.CdiPojo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/myrest")
public class RestResource {

    /**
     * CDI STEP 7 - Inject to your hearts content.
     */
    @Inject
    private CdiPojo cdiPojo;

    @GET
    public String hello() {
        return cdiPojo.getAbsolutelyAnything();
    }

    @POST
    public String lowerCase(final String message) {
        return "Hi REST: " + message.toLowerCase();
    }

    /**
     * REST STEP 4 - Expose something complex, because we are not only going to need
     * native types. Look at the class for some detail of what we are returning.
     */
    @Path("complex")
    @GET
    @Produces({APPLICATION_JSON})
    public ComplexType getComplexType() {
        final ComplexType ct = new ComplexType();

        ct.setName("Just a simple example");
        ct.setDescription("But you get the picture of how complex it could be right?");

        return ct;
    }
}
