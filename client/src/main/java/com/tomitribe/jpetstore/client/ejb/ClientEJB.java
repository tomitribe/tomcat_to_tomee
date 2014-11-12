/**
 * Tomitribe Confidential
 * <p/>
 * Copyright(c) Tomitribe Corporation. 2014
 * <p/>
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 * <p/>
 */
package com.tomitribe.jpetstore.client.ejb;

import com.tomitribe.ee.ejb.IEjbStateful;
import com.tomitribe.jpetstore.client.ui.FormMain;

import javax.naming.InitialContext;
import java.util.Properties;

public class ClientEJB {

    public ClientEJB() {
    }

    public void callEJB(final FormMain formMain) {
        final Thread t = new Thread() {
            public void run() {

                int i = -1;

                try {
                    final Properties p = new Properties();
                    p.put("java.naming.factory.initial", org.apache.openejb.client.RemoteInitialContextFactory.class.getName());
                    p.put("java.naming.provider.url", "http://127.0.0.1:8080/tomee/ejb");

                    final InitialContext ctx = new InitialContext(p);

                    final IEjbStateful myBean = (IEjbStateful) ctx.lookup("EjbStatefulRemote");
                    i = myBean.getId();
                } catch (final Exception e) {
                    e.printStackTrace();
                } finally {
                    formMain.setEjbResult(i);
                }
            }
        };

        t.start();
    }
}
