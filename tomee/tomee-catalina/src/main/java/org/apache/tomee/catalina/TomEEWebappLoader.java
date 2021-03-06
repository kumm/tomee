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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tomee.catalina;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.WebappLoader;
import org.apache.openejb.ClassLoaderUtil;
import org.apache.openejb.classloader.ClassLoaderConfigurer;
import org.apache.openejb.classloader.CompositeClassLoaderConfigurer;
import org.apache.openejb.config.QuickJarsTxtParser;
import org.apache.openejb.loader.SystemInstance;

import java.io.File;

/**
 * Usage example in META-INF/context.xml
 * <p/>
 * <p/>
 * <Context antiJARLocking="true" >
 * <Loader
 * className="org.apache.tomee.catalina.ProvisioningWebappLoader"
 * searchExternalFirst="true"
 * virtualClasspath="mvn:commons-el:commons-el:1.0;mvn:commons-el:commons-el:1.0"
 * searchVirtualFirst="true"
 * />
 * </Context>
 */
public class TomEEWebappLoader extends WebappLoader {
    public static final boolean SKIP_BACKGROUND_PROCESS = "true".equals(SystemInstance.get().getProperty("tomee.classloader.skip-background-process", "false"));

    @Override
    public void backgroundProcess() {
        if (SKIP_BACKGROUND_PROCESS) {
            return;
        }

        final ClassLoader classloader = super.getClassLoader();
        if (classloader instanceof TomEEWebappClassLoader) {
            final TomEEWebappClassLoader tomEEWebappClassLoader = (TomEEWebappClassLoader) classloader;
            tomEEWebappClassLoader.restarting();
            try {
                super.backgroundProcess();
            } finally {
                tomEEWebappClassLoader.restarted();
            }
        } else {
            super.backgroundProcess();
        }
    }

    @Override
    public boolean modified() {
        if (SKIP_BACKGROUND_PROCESS) {
            return false;
        }
        return super.modified();
    }

    @Override
    protected void startInternal() throws LifecycleException {
        final Context context = getContext();

        ClassLoaderConfigurer configurer = ClassLoaderUtil.configurer(context.getName());

        // WEB-INF/jars.xml
        final File war = Contexts.warPath(Context.class.cast(context));
        final File jarsXml = new File(war, "WEB-INF/" + QuickJarsTxtParser.FILE_NAME);
        final ClassLoaderConfigurer configurerTxt = QuickJarsTxtParser.parse(jarsXml);
        if (configurerTxt != null) {
            configurer = new CompositeClassLoaderConfigurer(configurer, configurerTxt);
        }

        TomEEWebappClassLoader.initContext(configurer);
        TomEEWebappClassLoader.initContext(context);
        try {
            super.startInternal();
        } finally {
            TomEEWebappClassLoader.cleanContext();
        }
    }

    @Override
    public String toString() {
        return "TomEE" + super.toString();
    }
}
