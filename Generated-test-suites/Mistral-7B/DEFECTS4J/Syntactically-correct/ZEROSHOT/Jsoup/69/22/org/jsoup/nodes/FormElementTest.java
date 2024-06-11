package org.jsoup.nodes.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Elements elements = formElement.elements();
 assertNotNull(elements);
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement = new Elements(new Tag("input")).first();
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodThrowsIllegalArgumentExceptionWhenBaseUriCannotBeDetermined() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormDataMethodEmptyList() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormDataMethodWithSingleInput() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement = new Elements(new Tag("input"))
 .attr("name", "test")
 .attr("value", "testValue")
 .first();
 formElement.addElement(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("test", keyVal.key);
 assertEquals("testValue", keyVal.value);
 }

 @Test
 public void testFormDataMethodWithMultipleInputs() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement1 = new Elements(new Tag("input"))
 .attr("name", "test1")
 .attr("value", "testValue1")
 .first();
 formElement.addElement(inputElement1);
 Element inputElement2 = new Elements(new Tag("input"))
 .attr("name", "test2")
 .attr("value", "testValue2")
 .first();
 formElement.addElement(inputElement2);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 }

 @Test
 public void testFormDataMethodWithSelect() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element selectElement = new Elements(new Tag("select"))
 .attr("name", "test")
 .first();
 Element optionElement1 = new Elements(new Tag("option"))
 .attr("value", "option1")
 .attr("selected", "selected")
 .first();
 Element optionElement2 = new Elements(new Tag("option"))
 .attr("value", "option2")
 .first();
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("test", keyVal.key);
 assertEquals("option1", keyVal.value);
 }

 @Test
 public void testFormDataMethodWithCheckboxOrRadio() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element checkboxElement = new Elements(new Tag("input"))
 .attr("name", "test")
 .attr("type", "checkbox")
 .first();
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNull(formData);
 checkboxElement.attr("checked", "checked");
 formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("test", keyVal.key);
 assertEquals("on", keyVal.value);
 }
}