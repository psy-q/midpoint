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

package com.evolveum.midpoint.wf.processors.primary;

import com.evolveum.midpoint.model.api.context.ModelContext;
import com.evolveum.midpoint.prism.Objectable;
import com.evolveum.midpoint.prism.PrismObject;
import com.evolveum.midpoint.prism.delta.ObjectDelta;
import com.evolveum.midpoint.schema.result.OperationResult;
import com.evolveum.midpoint.task.api.Task;
import com.evolveum.midpoint.util.exception.ObjectNotFoundException;
import com.evolveum.midpoint.util.exception.SchemaException;
import com.evolveum.midpoint.wf.jobs.JobCreationInstruction;
import com.evolveum.midpoint.wf.messages.ProcessEvent;
import com.evolveum.midpoint.xml.ns._public.common.common_2a.ObjectReferenceType;
import com.evolveum.midpoint.xml.ns._public.common.common_2a.ObjectType;
import com.evolveum.midpoint.xml.ns._public.common.common_2a.WfProcessInstanceType;

import java.util.List;
import java.util.Map;

/**
 *
 * PrimaryApprovalProcessWrapper is an interface to (or a wrapper of) a specific kind of workflow process related
 * to primary-stage change approval. Examples of process wrappers:
 *  - AddRoleProcessWrapper
 *  - CreateUserProcessWrapper
 *  - ChangeAttributeXProcessWrapper (X is an attribute of a user)
 *  - ...
 *
 * Process wrapper plays a role on these occasions:
 *  1) when a change arrives - process wrapper tries to recognize whether the change contains relevant
 *     delta(s); if so, it prepares instruction(s) to start related workflow approval process(es)
 *  2) when a process instance finishes - process wrapper modifies the delta(s) related to particular
 *     process instance and passes them along, to be executed
 *  3) when a user wants to approve the item or asks about the state of process instance(s) -
 *     it prepares the data that is specific to individual process.
 *
 * @author mederly
 */
public interface PrimaryApprovalProcessWrapper {

    /**
     * Examines the change and determines whether there are pieces that require (change type specific)
     * approval, for example, if there are roles added.
     *
     * If yes, it takes these deltas out of the original change and prepares instruction(s) to start wf process(es).
     *
     * @param modelContext Original model context (e.g. to be able to get information about whole context of the operation)
     * @param change Change to be examined and modified by implementation of this method
     * @param taskFromModel General context of the operation - the method should not modify the task.
     * @param result Operation result - the method should report any errors here (TODO what about creating subresults?)
     * @return list of start process instructions
     * @see com.evolveum.midpoint.wf.jobs.JobCreationInstruction
     */
    List<JobCreationInstruction> prepareJobCreationInstructions(ModelContext<?> modelContext, ObjectDelta<? extends ObjectType> change, Task taskFromModel, OperationResult result) throws SchemaException;

    /**
     * On process instance end, prepares deltaOut based in deltaIn and information gathered during approval process.
     *
     * @param event Current ProcessEvent providing information on what happened within wf process instance.
     * @param job Reference to a job (pair of process instance and a task) in which the event happened.
     * @param result Operation result - the method should report any errors here.
     * @return List of resulting object deltas. Typically, when approved, resulting delta is the same as delta that had to be approved,
     * and when rejected, the resulting delta list is empty. However, approver might requested a change in the delta, so the processing
     * here may be more complex.
     * @throws SchemaException if there is any problem with the schema.
     */
    List<ObjectDelta<Objectable>> prepareDeltaOut(ProcessEvent event, PrimaryChangeProcessorJob job, OperationResult result) throws SchemaException;

    /**
     * Returns a list of users who have approved the particular request. This information is then stored in the task by the wf module,
     * and eventually fetched from there and put into metadata (createApproverRef/modifyApproverRef) by the model ChangeExecutor.
     *
     * However, information about the approvers is process-specific. Default implementation of this method in BaseWrapper corresponds
     * to behavior of general ItemApproval process.
     *
     * @param event Current ProcessEvent providing information on what happened within wf process instance.
     * @return List of references to approvers that approved this request.
     */
    List<ObjectReferenceType> getApprovedBy(ProcessEvent event);

    /**
     * Returns a PrismObject containing information about a work item to be processed by the user. For example, for 'approve role addition' process
     * here is the RoleApprovalFormType prism object, having the following items:
     * - user: to whom is a role being requested,
     * - role: which role was requested to be added,
     * - timeInterval: what is the validity time of the assignment that was requested,
     * - requesterComment: a text that the requester entered when he requested the operation to be carried out,
     * - comment - here the approver writes his comments on approving or rejecting the work item.
     *
     * @param task activiti task corresponding to the work item that is being displayed
     * @param variables process instance variables at the point of invoking the work item (activiti task)
     * @param result operation result where the operation status should be reported
     * @return PrismObject containing the specific information about work item
     * @throws SchemaException if any of key objects cannot be retrieved because of schema exception
     * @throws ObjectNotFoundException if any of key objects cannot be found
     */
    PrismObject<? extends ObjectType> getRequestSpecificData(org.activiti.engine.task.Task task, Map<String, Object> variables, OperationResult result) throws SchemaException, ObjectNotFoundException;

    /**
     * Returns a object related to the work item at hand. E.g. for 'approve role addition' process this method returns corresponding role object.
     *
     * @param task activiti task corresponding to the work item that is being displayed
     * @param variables process instance variables at the point of invoking the work item (activiti task)
     * @param result operation result where the operation status should be reported
     * @return PrismObject containing the object related to the work item
     * @throws SchemaException if the object cannot be retrieved because of schema exception
     * @throws ObjectNotFoundException if the object cannot be found
     */
    PrismObject<? extends ObjectType> getRelatedObject(org.activiti.engine.task.Task task, Map<String, Object> variables, OperationResult result) throws SchemaException, ObjectNotFoundException;

    /**
     * Returns the name of process instance details GUI panel class name.
     *
     * @return Fully qualified class name used to implement process-specific instance state panel.
     *
     * @see com.evolveum.midpoint.web.component.wf.processes.itemApproval.ItemApprovalPanel
     */
    String getProcessInstanceDetailsPanelName(WfProcessInstanceType processInstance);
}
