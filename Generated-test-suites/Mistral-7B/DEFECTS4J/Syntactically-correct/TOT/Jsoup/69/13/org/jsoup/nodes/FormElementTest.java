package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.helper.Elements;
import org.jsoup.helper.FormSubmitter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<html><head></head><body><form action='/' method='post'><input name='test' type='text'></form></body></html>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElements() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.add(new InputElement("input", "", new Attributes().put("type", "text").put("name", "test")));
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals("input", elements.get(0).tagName());
 }

 @Test
 public void testSubmitWithEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 try {
 Connection connection = formElement.submit();
 connection.execute();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithValidForm() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 formElement.add(new InputElement("input", "", new Attributes().put("type", "text").put("name", "test").put("value", "testValue")));
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("testValue", formData.get(0).val());
 }

 @Test
 public void testSubmitWithMultipleControls() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 formElement.add(new InputElement("input1", "", new Attributes().put("type", "text").put("name", "test1").put("value", "testValue1")));
 formElement.add(new InputElement("input2", "", new Attributes().put("type", "text").put("name", "test2").put("value", "testValue2")));
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals(2, formData.size());
 assertEquals("test1", formData.get(0).key());
 assertEquals("testValue1", formData.get(0).val());
 assertEquals("test2", formData.get(1).key());
 assertEquals("testValue2", formData.get(1).val());
 }

 @Test
 public void testSubmitWithSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 formElement.add(new SelectElement("select", new Attributes().put("name", "test")));
 formElement.add(new OptionElement("option1", "value1", new Attributes()));
 formElement.add(new OptionElement("option2", "value2", new Attributes()));
 formElement.child(0).setAttribute("selected", "selected");
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("value1", formData.get(0).val());
 }

 @Test
 public void testSubmitWithCheckboxOrRadio() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 formElement.add(new InputElement("input", "", new Attributes().put("type", "checkbox").put("name", "test").put("value", "testValue")));
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals(0, formData.size());
 formElement.child(0).setAttribute("checked", "checked");
 connection = formElement.submit();
 formData = connection.requestBody();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("testValue", formData.get(0).val());
 }

 @Test
 public void testFormDataWithEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataWithSingleControl() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 formElement.add(new InputElement("input", "", new Attributes().put("type", "text").put("name", "test").put("value", "testValue")));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("testValue", formData.get(0).val());
 }

 @Test
 public void testFormDataWithMultipleControls() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 formElement.add(new InputElement("input1", "", new Attributes().put("type", "text").put("name", "test1").put("value", "testValue1")));
 formElement.add(new InputElement("input2", "", new Attributes().put("type", "text").put("name", "test2").put("value", "testValue2")));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("test1", formData.get(0).key());
 assertEquals("testValue1", formData.get(0).val());
 assertEquals("test2", formData.get(1).key());
 assertEquals("testValue2", formData.get(1).val());
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 formElement.add(new SelectElement("select", new Attributes().put("name", "test")));
 formElement.add(new OptionElement("option1", "value1", new Attributes()));
 formElement.child(0).setAttribute("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("value1", formData.get(0).val());
 }

 @Test
 public void testFormDataWithCheckboxOrRadio() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 formElement.add(new InputElement("input", "", new Attributes().put("type", "checkbox").put("name", "test").put("value", "testValue")));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 formElement.child(0).setAttribute("checked", "checked");
 formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("testValue", formData.get(0).val());
 }

 @Test
 public void testSubmitWithFormWithBaseUri() {
 FormElement formElement = new FormElement(new Tag("form"), "/test", new Attributes());
 formElement.add(new InputElement("input", "", new Attributes().put("type", "text").put("name", "test").put("value", "testValue")));
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals("test", formData.get(0).key());
 assertEquals("testValue", formData.get(0).val());
 assertEquals("/test", connection.url());
 }
}