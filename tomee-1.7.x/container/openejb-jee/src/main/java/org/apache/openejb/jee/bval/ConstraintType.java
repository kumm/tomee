/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openejb.jee.bval;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for constraintType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="constraintType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groups" type="{http://jboss.org/xml/ns/javax/validation/mapping}groupsType" minOccurs="0"/>
 *         &lt;element name="payload" type="{http://jboss.org/xml/ns/javax/validation/mapping}payloadType" minOccurs="0"/>
 *         &lt;element name="element" type="{http://jboss.org/xml/ns/javax/validation/mapping}elementType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="annotation" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "constraintType", propOrder = {
    "message",
    "groups",
    "payload",
    "element"
})
public class ConstraintType {

    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String message;
    protected GroupsType groups;
    protected PayloadType payload;
    protected List<ElementType> element;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String annotation;

    /**
     * Gets the value of the message property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessage(final String value) {
        this.message = value;
    }

    /**
     * Gets the value of the groups property.
     *
     * @return possible object is
     * {@link GroupsType }
     */
    public GroupsType getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     *
     * @param value allowed object is
     *              {@link GroupsType }
     */
    public void setGroups(final GroupsType value) {
        this.groups = value;
    }

    /**
     * Gets the value of the payload property.
     *
     * @return possible object is
     * {@link PayloadType }
     */
    public PayloadType getPayload() {
        return payload;
    }

    /**
     * Sets the value of the payload property.
     *
     * @param value allowed object is
     *              {@link PayloadType }
     */
    public void setPayload(final PayloadType value) {
        this.payload = value;
    }

    /**
     * Gets the value of the element property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the element property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElement().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ElementType }
     */
    public List<ElementType> getElement() {
        if (element == null) {
            element = new ArrayList<ElementType>();
        }
        return this.element;
    }

    /**
     * Gets the value of the annotation property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAnnotation() {
        return annotation;
    }

    /**
     * Sets the value of the annotation property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAnnotation(final String value) {
        this.annotation = value;
    }

}
