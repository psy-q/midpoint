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
package com.evolveum.midpoint.web.session;

import com.evolveum.midpoint.prism.query.ObjectPaging;
import com.evolveum.midpoint.web.page.admin.users.dto.OrgUnitSearchDto;

import java.io.Serializable;

/**
 *  @author shood
 * */
public class OrgUnitStorage implements Serializable{

    /**
     *  DTO used for search purposes in {@link com.evolveum.midpoint.web.page.admin.users in OrgUnitBrowser}
     * */
    private OrgUnitSearchDto orgUnitSearch;

    /**
     *  Paging DTO used in table on page {@link com.evolveum.midpoint.web.page.admin.users in OrgUnitBrowser}
     * */
    private ObjectPaging orgUnitPaging;

    public OrgUnitSearchDto getOrgUnitSearch() {
        return orgUnitSearch;
    }

    public void setOrgUnitSearch(OrgUnitSearchDto orgUnitSearch) {
        this.orgUnitSearch = orgUnitSearch;
    }

    public ObjectPaging getOrgUnitPaging() {
        return orgUnitPaging;
    }

    public void setOrgUnitPaging(ObjectPaging orgUnitPaging) {
        this.orgUnitPaging = orgUnitPaging;
    }
}