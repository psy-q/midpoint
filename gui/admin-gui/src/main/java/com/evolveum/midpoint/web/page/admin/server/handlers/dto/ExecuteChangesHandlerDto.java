/*
 * Copyright (c) 2010-2016 Evolveum
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

package com.evolveum.midpoint.web.page.admin.server.handlers.dto;

import com.evolveum.midpoint.prism.PrismContext;
import com.evolveum.midpoint.schema.constants.SchemaConstants;
import com.evolveum.midpoint.util.exception.SchemaException;
import com.evolveum.midpoint.util.exception.SystemException;
import com.evolveum.midpoint.web.page.admin.server.dto.TaskDto;
import com.evolveum.midpoint.web.security.MidPointApplication;
import com.evolveum.midpoint.web.util.WebXmlUtil;
import com.evolveum.prism.xml.ns._public.types_3.ObjectDeltaType;
import org.apache.wicket.Application;

/**
 * @author mederly
 */
public class ExecuteChangesHandlerDto extends QueryBasedHandlerDto {

	public static final String F_OBJECT_DELTA_XML = "objectDeltaXml";

	public ExecuteChangesHandlerDto(TaskDto taskDto) {
		super(taskDto);
	}

	public String getObjectDeltaXml() {
		ObjectDeltaType objectDeltaType = taskDto.getExtensionPropertyRealValue(SchemaConstants.MODEL_EXTENSION_OBJECT_DELTA, ObjectDeltaType.class);
		if (objectDeltaType == null) {
			return null;
		}
		PrismContext prismContext = ((MidPointApplication) Application.get()).getPrismContext();
		try {
			return WebXmlUtil.stripNamespaceDeclarations(
					prismContext.serializeAnyData(objectDeltaType, SchemaConstants.MODEL_EXTENSION_OBJECT_DELTA, PrismContext.LANG_XML));
		} catch (SchemaException e) {
			throw new SystemException("Couldn't serialize object delta: " + e.getMessage(), e);
		}
	}

}
