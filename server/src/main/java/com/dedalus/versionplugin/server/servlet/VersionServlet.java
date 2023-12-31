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

package com.dedalus.versionplugin.server.servlet;

import com.kaurpalang.mirth.annotationsplugin.annotation.MirthApiProvider;
import com.kaurpalang.mirth.annotationsplugin.type.ApiProviderType;
import com.dedalus.versionplugin.shared.Constants;
import com.dedalus.versionplugin.shared.interfaces.VersionServletInterface;
import com.mirth.connect.server.api.MirthServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@MirthApiProvider(type = ApiProviderType.SERVER_CLASS)
public class VersionServlet extends MirthServlet implements VersionServletInterface {

    public VersionServlet(@Context HttpServletRequest request, @Context SecurityContext sc) {
        super(request, sc, Constants.POINT_NAME);
    }

    @Override
    public String getVersionData() {
        String version = System.getenv("COMPLATFORM_VERSION");
        return version;
    }
}