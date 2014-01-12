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

package com.evolveum.midpoint.web.page.admin.reports;

import com.evolveum.midpoint.util.logging.Trace;
import com.evolveum.midpoint.util.logging.TraceManager;
import com.evolveum.midpoint.web.component.AjaxDownloadBehaviorFromStream;
import com.evolveum.midpoint.web.component.data.TablePanel;
import com.evolveum.midpoint.web.component.util.ListDataProvider;
import com.evolveum.midpoint.web.component.util.LoadableModel;
import com.evolveum.midpoint.web.page.admin.reports.component.SingleButtonRowPanel;
import com.evolveum.midpoint.web.page.admin.reports.dto.ReportDto;
import com.evolveum.midpoint.xml.ns._public.common.common_2a.MetadataType;
import com.evolveum.midpoint.xml.ns._public.common.common_2a.ReportType;
import com.evolveum.prism.xml.ns._public.types_2.PolyStringType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lazyman
 */
public class PageCreatedReports extends PageAdminReports {

    private static final Trace LOGGER = TraceManager.getTrace(PageCreatedReports.class);

    private static final String DOT_CLASS = PageCreatedReports.class.getName() + ".";

    private static final String BUTTON_DOWNLOAD_TEXT = "Download";

    private static final String ID_MAIN_FORM = "mainForm";
    private static final String ID_CREATED_REPORTS_TABLE = "table";

    private IModel<List<ReportDto>> model;


    public PageCreatedReports(){

        model = new LoadableModel<List<ReportDto>>() {

            @Override
            protected List<ReportDto> load() {
                return loadModel();
            }
        };
        initLayout();
    }

    @Override
    protected IModel<String> createPageSubTitleModel(){
        return new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return createStringResource("page.subTitle").getString();
            }
        };
    }

    //TODO - load real ReportType objects from repository
    private List<ReportDto> loadModel(){
        List<ReportDto> reportList = new ArrayList<ReportDto>();

        ReportDto reportDto;
        for(int i = 1; i <= 5; i++){
            reportDto = new ReportDto(ReportDto.Type.USERS, "ReportName" + i, "Report description " + i,
                    "Author" + i, "January " + i + "., 2014");
            reportDto.setFileType("PDF");
            reportList.add(reportDto);
        }

        return reportList;
    }

    private void initLayout(){
        Form mainForm = new Form(ID_MAIN_FORM);
        add(mainForm);

        final AjaxDownloadBehaviorFromStream ajaxDownloadBehavior = new AjaxDownloadBehaviorFromStream() {

            @Override
            protected byte[] initStream() {
                return createReport();
            }
        };
        ajaxDownloadBehavior.setContentType("application/pdf; charset=UTF-8");
        mainForm.add(ajaxDownloadBehavior);

        TablePanel table = new TablePanel<ReportDto>(ID_CREATED_REPORTS_TABLE,
                new ListDataProvider<ReportDto>(this, model), initColumns(ajaxDownloadBehavior));
        table.setShowPaging(true);
        table.setOutputMarkupId(true);
        mainForm.add(table);
    }

    private List<IColumn<ReportDto, String>> initColumns(final AjaxDownloadBehaviorFromStream ajaxDownloadBehavior){
        List<IColumn<ReportDto, String>> columns = new ArrayList<IColumn<ReportDto, String>>();

        IColumn column;
        column = new PropertyColumn<ReportDto, String>(createStringResource("pageCreatedReports.table.name"), null){

            @Override
            public IModel<Object> getDataModel(IModel<ReportDto> rowModel){
                ReportDto dto = rowModel.getObject();

                return (IModel)createStringResource(dto.getName());
            }
        };
        columns.add(column);

        column = new PropertyColumn<ReportDto, String>(createStringResource("pageCreatedReports.table.description"), null){

            @Override
            public IModel<Object> getDataModel(IModel<ReportDto> rowModel){
                ReportDto dto = rowModel.getObject();

                return (IModel)createStringResource(dto.getDescription());
            }
        };
        columns.add(column);

        column = new PropertyColumn<ReportDto, String>(createStringResource("pageCreatedReports.table.author"), null){

            @Override
            public IModel<Object> getDataModel(IModel<ReportDto> rowModel){
                ReportDto dto = rowModel.getObject();

                return (IModel)createStringResource(dto.getAuthor());
            }
        };
        columns.add(column);

        column = new PropertyColumn<ReportDto, String>(createStringResource("pageCreatedReports.table.time"), null){

            @Override
            public IModel<Object> getDataModel(IModel<ReportDto> rowModel){
                ReportDto dto = rowModel.getObject();

                return (IModel)createStringResource(dto.getTimeString());
            }
        };
        columns.add(column);

        column = new PropertyColumn<ReportDto, String>(createStringResource("pageCreatedReports.table.filetype"), null){

            @Override
            public IModel<Object> getDataModel(IModel<ReportDto> rowModel){
                ReportDto dto = rowModel.getObject();

                return (IModel)createStringResource(dto.getFileType());
            }
        };
        columns.add(column);

        column = new AbstractColumn<ReportDto, String>(new Model(), null) {

            @Override
            public void populateItem(Item<ICellPopulator<ReportDto>> cellItem,
                                     String componentId, IModel<ReportDto> rowModel) {

                cellItem.add(new SingleButtonRowPanel<ReportDto>(componentId, rowModel, BUTTON_DOWNLOAD_TEXT){

                    @Override
                    public void executePerformed(AjaxRequestTarget target, IModel<ReportDto> model){
                        downloadPerformed(target, model.getObject(), ajaxDownloadBehavior);
                    }
                });
            }
        };
        columns.add(column);

        return columns;
    }

    private void downloadPerformed(AjaxRequestTarget target, ReportDto report,
                                   AjaxDownloadBehaviorFromStream ajaxDownloadBehavior){
        //TODO - run download from file
    }

    private byte[] createReport(){
        //TODO - create report from ReportType
        return null;
    }



}
