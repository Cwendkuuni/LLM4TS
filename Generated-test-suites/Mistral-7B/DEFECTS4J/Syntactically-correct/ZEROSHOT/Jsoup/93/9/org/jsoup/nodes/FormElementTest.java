package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private static final String BASE_URI = "http://test.com";
 private static final Tag FORM_TAG = new Tag("form");

 @Test
 public void testFormElementWithEmptyAttributes() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes());
 assertNotNull(formElement);
 assertNull(formElement.elements());
 }

 @Test
 public void testFormElementWithElements() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes());
 Element inputElement = Mockito.mock(Element.class);
 Element textAreaElement = Mockito.mock(Element.class);

 formElement.addElement(inputElement);
 formElement.addElement(textAreaElement);

 List<Element> elements = formElement.elements();
 assertEquals(2, elements.size());
 assertEquals(inputElement, elements.get(0));
 assertEquals(textAreaElement, elements.get(1));
 }

 @Test
 public void testSubmitWithNullAction() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes().put("action", ""));
 try {
 formElement.submit();
 FailureHandler.fail("IllegalArgumentException expected but not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithValidAction() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes().put("action", "http://test.com/submit"));
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataWithNoElements() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes());
 Element selectElement = new Element("select");
 selectElement.attr("name", "color");
 Element optionRed = new Element("option").attr("value", "red").attr("selected", "selected");
 Element optionBlue = new Element("option").attr("value", "blue");
 selectElement.appendElement(optionRed);
 selectElement.appendElement(optionBlue);
 formElement.addElement(selectElement);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("color", formData.get(0).key);
 assertEquals("red", formData.get(0).val);
 }

 @Test
 public void testFormDataWithCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes());
 Element checkboxElement = new Element("input").attr("type", "checkbox").attr("name", "checkbox1");
 formElement.addElement(checkboxElement);
 checkboxElement.attr("checked", "checked");

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("checkbox1", formData.get(0).key);
 assertEquals("on", formData.get(0).val);

 formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes());
 Element radioElement = new Element("input").attr("type", "radio").attr("name", "choice");
 formElement.addElement(radioElement);
 radioElement.attr("checked", "checked");

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("choice", formData.get(0).key);
 assertEquals("on", formData.get(0).val);
 }

 @Test
 public void testFormDataWithTextInputElement() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URI, new Attributes());
 Element textInputElement = new Element("input").attr("type", "text").attr("name", "username");
 textInputElement.val("test");
 formElement.addElement(textInputElement);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("username", formData.get(0).key);
 assertEquals("test", formData.get(0).val);
 }
}