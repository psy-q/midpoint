/*
 * Copyright (c) 2010-2013 Evolveum
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
package com.evolveum.midpoint.web.page.admin.configuration;

import com.evolveum.midpoint.security.api.AuthorizationConstants;
import com.evolveum.midpoint.web.application.AuthorizationAction;
import com.evolveum.midpoint.web.application.PageDescriptor;

/**
 *  @author shood
 * */

@PageDescriptor(url = {"/admin/config", "/admin/config/logging"}, action = {
        @AuthorizationAction(actionUri = PageAdminConfiguration.AUTH_CONFIGURATION_ALL,
                label = PageAdminConfiguration.AUTH_CONFIGURATION_ALL_LABEL,
                description = PageAdminConfiguration.AUTH_CONFIGURATION_ALL_DESCRIPTION),
        @AuthorizationAction(actionUri = AuthorizationConstants.NS_AUTHORIZATION + "#configSystemConfiguration",
                label = "PageSystemConfiguration.auth.configSystemConfiguration.label",
                description = "PageSystemConfiguration.auth.configSystemConfiguration.description")})
public class PageSystemConfigurationLogging extends PageAdminConfiguration{

    public PageSystemConfigurationLogging(){
        setResponsePage(new PageSystemConfiguration(){

            @Override
            public int getTabIndex(){
                return CONFIGURATION_TAB_LOGGING;
            }

        });
    }

}
