package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private static final String HTML = "" +
         "<html lang=\"en\">" +
         "<head></head>" +
         "<body>" +
         "<form action=\"/form\" method=\"post\">" +
         "<input name=\"input1\" type=\"text\">" +
         "<input name=\"input2\" type=\"text\" value=\"value2\">" +
         "<select name=\"select1\">" +
         "<option value=\"option1\">Option 1</option>" +
         "<option selected value=\"option2\">Option 2</option>" +
         "</select>" +
         "<input name=\"input3\" type=\"checkbox\">" +
         "<input name=\"input4\" type=\"checkbox\" checked>" +
         "<input name=\"input5\" type=\"submit\" value=\"Submit\">" +
         "</form>" +
         "</body>" +
         "</html>";

 private Element formElement;

 @Before
 public void setup() {
 Document doc = Jsoup.parse(HTML);
 formElement = doc.selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElements() {
 Elements expected = new Elements();
 expected.add(new ElementTag("input", new Attributes().put("name", "input1").put("type", "text")));
 expected.add(new ElementTag("input", new Attributes().put("name", "input2").put("type", "text").put("value", "value2")));
 expected.add(new ElementTag("select", new Attributes().put("name", "select1")));
 expected.add(new ElementTag("input", new Attributes().put("name", "input3").put("type", "checkbox")));
 expected.add(new ElementTag("input", new Attributes().put("name", "input4").put("type", "checkbox").put("checked", "checked")));

 Elements actual = formElement.elements();
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 Element e = expected.get(i);
 Element a = actual.get(i);
 assertEquals(e.tag(), a.tag());
 assertEquals(e.attributes(), a.attributes());
 }
 }

 @Test
 public void testAddElement() {
 Element newElement = new ElementTag("input", new Attributes().put("name", "newInput").put("type", "hidden"));
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 formElement.addElement(newElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(newElement, elements.get(0));
 }

 @Test
 public void testSubmit() throws Exception {
 Connection connection = formElement.submit();
 assertEquals(Connection.Method.POST, connection.method());
 assertEquals("/form", connection.url().toString());
 List<Connection.KeyVal> formData = connection.data();
 assertEquals(5, formData.size());
 assertEquals("input1", formData.get(0).key());
 assertEquals("", formData.get(0).val());
 assertEquals("input2", formData.get(1).key());
 assertEquals("value2", formData.get(1).val());
 assertEquals("select1", formData.get(2).key());
 assertEquals("option2", formData.get(2).val());
 assertEquals("input3", formData.get(3).key());
 assertEquals("", formData.get(3).val());
 assertEquals("input4", formData.get(4).key());
 assertEquals("on", formData.get(4).val());
 }

 @Test
 public void testFormDataEmptyForm() throws Exception {
 FormElement emptyFormElement = new FormElement(new Tag("form"), null, new Attributes());
 List<Connection.KeyVal> formData = emptyFormElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataDisabledInput() throws Exception {
 // Add disabled input to FormElement
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 Element disabledInput = new ElementTag("input", new Attributes().put("name", "disabledInput").put("type", "text").put("disabled", "disabled"));
 formElement.addElement(disabledInput);
 // Expect empty form data list
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataSelectElement() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 Element selectElement = new ElementTag("select", new Attributes().put("name", "select1"));
 Element option1 = new ElementTag("option", new Attributes().put("value", "option1"));
 Element option2 = new ElementTag("option", new Attributes().put("selected", "selected").put("value", "option2"));
 selectElement.add(option1);
 selectElement.add(option2);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("select1", formData.get(0).key());
 assertEquals("option2", formData.get(0).val());
 }

 @Test
 public void testFormDataCheckboxElement() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 Element checkboxElement1 = new ElementTag("input", new Attributes().put("name", "checkbox1").put("type", "checkbox"));
 Element checkboxElement2 = new ElementTag("input", new Attributes().put("name", "checkbox2").put("type", "checkbox").put("checked", "checked"));
 formElement.addElement(checkboxElement1);
 formElement.addElement(checkboxElement2);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("checkbox1", formData.get(0).key());
 assertEquals("", formData.get(0).val());
 assertEquals("checkbox2", formData.get(1).key());
 assertEquals("on", formData.get(1).val());
 }
}