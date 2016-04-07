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

package com.evolveum.midpoint.web.page.admin.server;

import com.evolveum.midpoint.model.api.ModelInteractionService;
import com.evolveum.midpoint.model.api.context.ModelContext;
import com.evolveum.midpoint.model.api.context.ModelProjectionContext;
import com.evolveum.midpoint.model.api.context.ModelState;
import com.evolveum.midpoint.model.api.visualizer.Scene;
import com.evolveum.midpoint.prism.PrismObject;
import com.evolveum.midpoint.prism.delta.ObjectDelta;
import com.evolveum.midpoint.schema.result.OperationResult;
import com.evolveum.midpoint.task.api.Task;
import com.evolveum.midpoint.util.exception.SchemaException;
import com.evolveum.midpoint.util.exception.SystemException;
import com.evolveum.midpoint.web.component.prism.show.SceneDto;
import com.evolveum.midpoint.web.component.prism.show.WrapperScene;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.addIgnoreNull;

/**
 * @author mederly
 */
public class TaskOtherChangesDto implements Serializable {

    public static final String F_STATE = "state";
    public static final String F_PRIMARY_DELTAS = "primaryDeltas";

    private ModelState state;
    private SceneDto primarySceneDto;

    public TaskOtherChangesDto(ModelContext<?> modelContext, ModelInteractionService modelInteractionService, Task opTask, OperationResult result) {

        state = modelContext.getState();

        if (modelContext.getFocusContext() != null) {

            // focusType & focusName
            PrismObject object = modelContext.getFocusContext().getObjectNew();
            if (object == null) {
                object = modelContext.getFocusContext().getObjectOld();
            }

            // primaryDelta
			final List<ObjectDelta<? extends ObjectType>> primaryDeltas = new ArrayList<>();
			final List<? extends Scene> primaryScenes;
			try {
				addIgnoreNull(primaryDeltas, modelContext.getFocusContext().getPrimaryDelta());
				for (ModelProjectionContext projCtx : modelContext.getProjectionContexts()) {
					addIgnoreNull(primaryDeltas, projCtx.getPrimaryDelta());
				}
				primaryScenes = modelInteractionService.visualizeDeltas(primaryDeltas, opTask, result);
			} catch (SchemaException e) {
				throw new SystemException(e);		// TODO
			}
			final WrapperScene primaryWrapperScene = new WrapperScene(primaryScenes, primaryDeltas.size() != 1 ? "PagePreviewChanges.primaryChangesMore" : "PagePreviewChanges.primaryChangesOne", primaryDeltas.size());
			primarySceneDto = new SceneDto(primaryWrapperScene);
        }
    }

    public ModelState getState() {
        return state;
    }

    public SceneDto getPrimaryDeltas() {
        return primarySceneDto;
    }
}
