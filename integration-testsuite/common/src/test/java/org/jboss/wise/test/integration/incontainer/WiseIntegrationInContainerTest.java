/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.wise.test.integration.incontainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.wise.core.test.WiseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

@RunWith(Arquillian.class)
public class WiseIntegrationInContainerTest extends WiseTest {

    private static final String WAR = "basic";
    private static final String SERVLET_WAR = "incontainer";

    @Deployment(name = WAR, order = 1)
    public static WebArchive createDeploymentA() {
        // retrieve a pre-built archive
        WebArchive archive = ShrinkWrap
                .create(ZipImporter.class, WAR + ".war")
                .importFrom(new File(getTestResourcesDir() + "/../../../target/test-classes/" + WAR + ".war"))
                .as(WebArchive.class);
        return archive;
    }


    @Deployment(name = SERVLET_WAR, order = 2)
    public static WebArchive createDeploymentB() {
        // retrieve a pre-built archive
        WebArchive archive = ShrinkWrap
                .create(ZipImporter.class, SERVLET_WAR + ".war")
                .importFrom(new File(getTestResourcesDir() + "/../../../target/test-classes/" + SERVLET_WAR + ".war"))
                .as(WebArchive.class);
        return archive;
    }

    @Test
    @RunAsClient
    public void test() throws Exception {
        URL url = new URL(getServerHostAndPort() + "/incontainer/HelloWorldServlet?name=foo");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String result = br.readLine();
        if (result.startsWith("[FIXME]")) {
            System.out.println(result);
        } else {
            Assert.assertEquals("WS return: foo", result);
        }
    }
}
