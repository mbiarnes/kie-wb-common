/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.backend.converters.fromstunner.properties;

import java.util.Optional;
import java.util.Set;

import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.DataObjectReference;
import org.eclipse.bpmn2.ItemDefinition;
import org.kie.workbench.common.stunner.core.util.StringUtils;

import static org.kie.workbench.common.stunner.bpmn.backend.converters.fromstunner.Factories.bpmn2;

public class DataObjectPropertyWriter extends PropertyWriter {

    private final Set<DataObject> dataObjects;

    private final DataObject dataObject;

    public DataObjectPropertyWriter(DataObjectReference element,
                                    VariableScope variableScope,
                                    Set<DataObject> dataObjects) {
        super(element, variableScope);
        this.dataObjects = dataObjects;
        dataObject = bpmn2.createDataObject();
        element.setDataObjectRef(dataObject);
    }

    @Override
    public void setName(String value) {
        final String escaped = StringUtils.replaceIllegalCharsAttribute(value.trim());
        dataObject.setName(escaped);
        dataObject.setId(escaped);
    }

    private void addDataObjectToProcess(DataObject dataObject) {
        final Optional<DataObject> any = dataObjects.stream().filter(elm -> elm.getId()
                .equals(dataObject.getId()))
                .findAny();
        if (any.isPresent()) {
            if (!any.get().getItemSubjectRef().getStructureRef().equals(dataObject.getItemSubjectRef().getStructureRef())) {
                any.get().getItemSubjectRef().setStructureRef("Object");
            }
        } else {
            dataObjects.add(dataObject);
        }
    }

    public void setType(String type) {
        ItemDefinition itemDefinition = bpmn2.createItemDefinition();
        itemDefinition.setStructureRef(type);
        dataObject.setItemSubjectRef(itemDefinition);
        addDataObjectToProcess(dataObject);
    }

    @Override
    public DataObjectReference getElement() {
        return (DataObjectReference) super.getElement();
    }

    public Set<DataObject> getDataObjects() {
        return dataObjects;
    }
}

