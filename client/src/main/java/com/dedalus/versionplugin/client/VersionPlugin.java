/*
 * Copyright 2021 Kaur Palang
 * Copyright 2023 Julian Pufler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dedalus.versionplugin.client;

import com.kaurpalang.mirth.annotationsplugin.annotation.MirthClientClass;
import com.mirth.connect.plugins.ClientPlugin;
import com.mirth.connect.client.ui.Frame;
import com.mirth.connect.client.ui.PlatformUI;
import com.dedalus.versionplugin.shared.Constants;
import com.dedalus.versionplugin.shared.interfaces.VersionServletInterface;
import com.dedalus.versionplugin.shared.models.VersionModel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@MirthClientClass
public class VersionPlugin extends ClientPlugin {

    protected Frame frame = PlatformUI.MIRTH_FRAME;

    private static final Logger logger = LogManager.getLogger(VersionPlugin.class);

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static VersionModel versionData;

    public VersionPlugin(String name) {
        super(name);
    }

    @Override
    public String getPluginPointName() {
        return Constants.POINT_NAME;
    }

    @Override
    public void start() {
        System.out.println("[" + Constants.POINT_NAME + "] Starting...");

        try {
            VersionServletInterface servlet = frame.mirthClient.getServlet(VersionServletInterface.class);
            versionData = servlet.getVersionData();
        } catch (Exception e) {
            versionData = new VersionModel("N/A");
        }

        System.out.println("[" + Constants.POINT_NAME + "] Retrieved version " + versionData.getVersion());

        Runnable updateTask = () -> {
            if (frame.statusBar != null) {
                String currentServerText = frame.statusBar.getServerText();
                if (currentServerText.indexOf(versionData.getVersion()) == -1) {
                    System.out.println("[" + Constants.POINT_NAME + "] Updating StatusBar ServerText");
                    frame.statusBar
                            .setServerText(
                                    currentServerText + " | Communication Platform Version: "
                                            + versionData.getVersion());
                }
            } else {
                System.out.println("[" + Constants.POINT_NAME + "] StatusBar is null");
            }
        };

        // 3 seconds initialDelay for the window to open
        executor.scheduleAtFixedRate(updateTask, 3, 15, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        System.out.println("[" + Constants.POINT_NAME + "] Stopping...");
        executor.shutdownNow();
    }

    @Override
    public void reset() {
    }
}
