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
 * ARQUILLIAN STEP 10 - Our second, and very complex, Arquillian test.
 *
 *
 */
package com.tomitribe.ee;

import com.tomitribe.ee.cdi.CdiPojo;
import com.tomitribe.ee.ejb.EjbSingleton;
import com.tomitribe.ee.ejb.EjbStateful;
import com.tomitribe.ee.api.IEjbStateful;
import com.tomitribe.ee.rest.ComplexType;
import com.tomitribe.ee.rest.RestResource;
import com.tomitribe.ee.ws.WebServiceDemo;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.openejb.testing.EnableServices;
import org.apache.ziplock.IO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webcommon30.WebAppVersionType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@RunWith(Arquillian.class)
@EnableServices({"jax-ws"})
public class TestArquillianHolyMoly {

    @ArquillianResource
    public URL url;

    @Deployment(testable = false)
    public static WebArchive deploy() {

        /**
         * This looks nasty at first glance, but what we are doing here
         * is just creating the common web.xml file.
         */

        final String xml = Descriptors.create(WebAppDescriptor.class)
                .version(WebAppVersionType._3_0)
                .getOrCreateServlet()
                .servletName("jaxrs")
                .servletClass(Application.class.getName())
                .createInitParam()
                .paramName(Application.class.getName())
                .paramValue(Application.class.getName())
                .up()
                .up()
                .getOrCreateServletMapping()
                .servletName("jaxrs")
                .urlPattern("/api")
                .up()
                .exportAsString();

        return ShrinkWrap.create(WebArchive.class, TestArquillianHolyMoly.class.getName()
                + ".war") //Name is just convenient

                .addClasses(
                        WebServiceDemo.class,
                        RestResource.class,
                        ComplexType.class,
                        CdiPojo.class,
                        EjbStateful.class,
                        IEjbStateful.class,
                        EjbSingleton.class) //Add our classes to test

                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") //Turn on CDI

                .addAsWebInfResource(EmptyAsset.INSTANCE, "ejb-jar.xml") //Turn on scanning

                .setWebXML(new StringAsset(xml));
    }

    @Test
    @RunAsClient
    public void invokeWebService() throws Exception {

        //final URL url = new URL("http://localhost:" + System.getProperty("server.http.port"));
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {

            final URI uri = URI.create(url.toExternalForm() + "WebServiceDemoService");
            final HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity("" +
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <ns1:getMessage xmlns:ns1=\"http://ws.ee.tomitribe.com/\"/>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>"));

            final HttpResponse response = httpClient.execute(post);
            final String body = asString(response);

            final String expected = "" +
                    "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<soap:Body>" +
                    "<ns:getMessageResponse xmlns:ns=\"http://ws.ee.tomitribe.com/\">" +
                    "<return>Halo, cool game</return>" +
                    "</ns:getMessageResponse>" +
                    "</soap:Body>" +
                    "</soap:Envelope>";

            Assert.assertEquals(expected, body.replaceAll("ns[0-9]*", "ns"));

        } finally {
            httpClient.close();
        }
    }

    @Test
    @RunAsClient
    public void invokeRest() throws Exception {

        //final URL url = new URL("http://localhost:" + System.getProperty("server.http.port"));
        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {

            final HttpResponse response = httpClient.execute(
                    new HttpGet(new URI(url.toExternalForm() + "api/myrest")));

            final String body = asString(response);
            Assert.assertEquals("Something", body);

        } finally {
            httpClient.close();
        }
    }

    public static String asString(final HttpResponse execute) throws IOException {
        final InputStream in = execute.getEntity().getContent();
        try {
            return IO.slurp(in);
        } finally {
            in.close();
        }
    }
}
