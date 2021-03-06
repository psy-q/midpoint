package com.evolveum.midpoint.web.component.assignment;

import com.evolveum.midpoint.gui.api.component.BasePanel;
import com.evolveum.midpoint.web.component.dialog.Popupable;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created by honchar
 */
public class AssignmentDetailsPanel extends BasePanel<AssignmentEditorDto> {
    private static final String ID_DETAILS_PANEL = "detailsPanel";

    public AssignmentDetailsPanel(String id) {
        super(id);
    }

    public AssignmentDetailsPanel(String id, IModel<AssignmentEditorDto> model) {
        super(id, model);
        initLayout();
    }

    private void initLayout() {
        setOutputMarkupId(true);
        AssignmentEditorPanel assignmentDetailsPanel = new AssignmentEditorPanel(ID_DETAILS_PANEL, getModel());
        assignmentDetailsPanel.setOutputMarkupId(true);
        add(assignmentDetailsPanel);

    }
}
