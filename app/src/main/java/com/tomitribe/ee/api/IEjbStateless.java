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
package com.tomitribe.ee.api;

import javax.ejb.Local;
import javax.ejb.Remote;

@Remote
@Local
public interface IEjbStateless {

    String sayHello();

    void setState();
}
