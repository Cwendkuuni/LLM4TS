package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Assert.assertEquals("form", formElement.tagName());
 }

 @Test
 public void testElements() {
 FormElement formElement = createFormElement();
 Element inputElement = createInputElement("input");
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testRemoveElement() {
 FormElement formElement = createFormElement();
 Element inputElement = createInputElement("input");
 formElement.addElement(inputElement);
 formElement.removeChild(inputElement);
 Elements elements = formElement.elements();
 Assert.assertEquals(0, elements.size());
 }

 @Test
 public void testSubmit() {
 FormElement formElement = createFormElement();
 String url = "http://example.com";
 formElement.attr("action", url);
 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 Assert.assertEquals(url, connection.url());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 FormElement formElement = createFormElement();
 formElement.attr("action", "");
 formElement.submit();
 }

 @Test
 public void testFormData() {
 FormElement formElement = createFormElement();
 Element inputElement1 = createInputElement("input", "name1", "value1");
 Element inputElement2 = createInputElement("input", "name2", "value2");
 formElement.addElement(inputElement1);
 formElement.addElement(inputElement2);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(2, formData.size());
 for (Connection.KeyVal keyVal : formData) {
 System.out.println(keyVal.key + "=" + keyVal.val);
 }
 }

 @Test
 public void testFormDataWithSelectTag() {
 FormElement formElement = createFormElement();
 Element selectElement = createSelectElement("select", "name", "Option 1", "Option 2");
 formElement.addElement(selectElement);
 Element optionElement1 = createOptionElement("option", "Option 1");
 Element optionElement2 = createOptionElement("option", "Option 2");
 selectElement.addElement(optionElement1);
 selectElement.addElement(optionElement2);
 selectElement.elementSiblings().get(0).attr("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Assert.assertEquals("name", formData.get(0).key);
 Assert.assertEquals("Option 1", formData.get(0).val);
 }

 @Test
 public void testFormDataWithCheckboxAndRadio() {
 FormElement formElement = createFormElement();
 Element checkboxElement1 = createInputElement("input", "name", null, "value1", "checkbox");
 Element checkboxElement2 = createInputElement("input", "name", null, "value2", "checkbox");
 formElement.addElement(checkboxElement1);
 formElement.addElement(checkboxElement2);
 checkboxElement1.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Assert.assertEquals("name", formData.get(0).key);
 Assert.assertEquals("value1", formData.get(0).val);

 FormElement formElementWithRadio = createFormElement();
 Element radioElement1 = createInputElement("input", "name", null, "value1", "radio");
 Element radioElement2 = createInputElement("input", "name", null, "value2", "radio");
 formElementWithRadio.addElement(radioElement1);
 formElementWithRadio.addElement(radioElement2);
 radioElement1.attr("checked", "checked");
 List<Connection.KeyVal> formDataWithRadio = formElementWithRadio.formData();
 Assert.assertEquals(1, formDataWithRadio.size());
 Assert.assertEquals("name", formDataWithRadio.get(0).key);
 Assert.assertEquals("value1", formDataWithRadio.get(0).val);
 }

 private FormElement createFormElement() {
 return new FormElement(new Tag("form"), "", new Attributes());
 }

 private Element createInputElement(String tag, String name, String value, String... attributes) {
 Element element = new Element(new Tag(tag), "", new Attributes());
 element.attr("name", name);
 if (value != null) {
 element.attr("value", value);
 }
 for (String attribute : attributes) {
 String[] pair = attribute.split("=");
 element.attr(pair[0], pair[1]);
 }
 return element;
 }

 private Element createSelectElement(String tag, String name, String... options) {
 Element selectElement = new Element(new Tag(tag), "", new Attributes());
 selectElement.attr("name", name);
 for (String option : options) {
 Element optionElement = new Element(new Tag("option"), "", new Attributes());
 optionElement.val(option);
 selectElement.addElement(optionElement);
 }
 return selectElement;
 }

 private Element createOptionElement(String tag, String value, String... attributes) {
 Element optionElement = new Element(new Tag(tag), "", new Attributes());
 optionElement.val(value);
 for (String attribute : attributes) {
 String[] pair = attribute.split("=");
 optionElement.attr(pair[0], pair[1]);
 }
 return optionElement;
 }
}