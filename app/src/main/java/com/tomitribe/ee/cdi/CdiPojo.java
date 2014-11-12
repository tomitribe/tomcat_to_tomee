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
 * CDI STEP 2 - Create a pojo and optionally give it a name.
 */
package com.tomitribe.ee.cdi;

import java.io.Serializable;

@javax.inject.Named("cdipojo") //This allows use in Unified Expression Language (EL)
public class CdiPojo implements Serializable {

    private String absolutelyAnything = "Something";

    public String getAbsolutelyAnything() {
        return absolutelyAnything;
    }

    public void setAbsolutelyAnything(final String absolutelyAnything) {
        this.absolutelyAnything = absolutelyAnything;
    }

    public String fixMePlease(final String s) {

        if("fishh".equalsIgnoreCase(s)){
            return "fish";
        }

        return s;
    }
}
