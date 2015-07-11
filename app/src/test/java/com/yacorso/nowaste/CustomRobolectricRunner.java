/*
 *
 *  * Copyright (c) 2015.
 *  *
 *  * "THE BEER-WARE LICENSE" (Revision 42):
 *  * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 *  * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 *  * As long as you retain this notice you can do whatever you want with this stuff.
 *  * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *  *
 *  * NoWaste team
 *
 */

package com.yacorso.nowaste;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.SdkConfig;
import org.robolectric.manifest.AndroidManifest;

public class CustomRobolectricRunner extends RobolectricTestRunner {
    public CustomRobolectricRunner(Class<?> testClass)
            throws InitializationError {
        super(testClass);
        String buildVariant = (BuildConfig.FLAVOR.isEmpty()
                ? "" : BuildConfig.FLAVOR+ "/") + BuildConfig.BUILD_TYPE;
        String intermediatesPath = BuildConfig.class.getResource("")
                .toString().replace("file:", "");
        intermediatesPath = intermediatesPath
                .substring(0, intermediatesPath.indexOf("/classes"));

        System.setProperty("android.package",
                BuildConfig.APPLICATION_ID);
        System.setProperty("android.manifest",
                intermediatesPath + "/manifests/full/"
                        + buildVariant + "/AndroidManifest.xml");
        System.setProperty("android.resources",
                intermediatesPath + "/res/" + buildVariant);
        System.setProperty("android.assets",
                intermediatesPath + "/assets/" + buildVariant);
    }
}
