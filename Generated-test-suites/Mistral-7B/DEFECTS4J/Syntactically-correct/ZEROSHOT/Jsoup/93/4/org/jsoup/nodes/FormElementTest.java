package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Element formElement;
 private Element inputElement;
 private Element selectElement;
 private Element checkboxElement;

 @Before
 public void setUp() {
 ParseSettings parseSettings = new ParseSettings();
 parseSettings.setOmitEmptyTags(true);
 Parser parser = new Parser(parseSettings);

 Tag formTag = new Tag("form");
 formTag.putMap("action", "http://www.example.com");
 formTag.putMap("method", "post");
 formElement = parser.parse(formTag.outerHtml()).first();

 Tag inputTag = new Tag("input");
 inputTag.putMap("name", "inputName");
 inputTag.putMap("type", "text");
 inputElement = parser.parse(inputTag.outerHtml()).first();

 Tag selectTag = new Tag("select");
 selectTag.putMap("name", "selectName");
 Elements optionElements = new Elements();
 Element opt1 = new Tag("option").text("Option 1");
 Element opt2 = new Tag("option").text("Option 2");
 optionElements.add(opt1);
 optionElements.add(opt2);
 selectTag.children().addAll(optionElements);
 selectElement = parser.parse(selectTag.outerHtml()).first();

 Tag checkboxTag = new Tag("input");
 checkboxTag.putMap("name", "checkboxName");
 checkboxTag.putMap("type", "checkbox");
 checkboxElement = parser.parse(checkboxTag.outerHtml()).first();
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethodReturnsEmptyListByDefault() {
 assertEquals(0, formElement.elements().size());
 }

 @Test
 public void testAddElementAndRemoveElement() {
 formElement.addElement(inputElement);
 formElement.addElement(selectElement);
 formElement.addElement(checkboxElement);
 assertEquals(3, formElement.elements().size());
 formElement.removeChild(inputElement);
 assertEquals(2, formElement.elements().size());
 }

 @Test
 public void testSubmitMethod() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataMethodEmptyForm() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodWithInputElement() {
 formElement.addElement(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("inputName", keyVal.key());
 assertEquals(inputElement.val(), keyVal.value());
 }

 @Test
 public void testFormDataMethodWithSelectElement() {
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("selectName", keyVal.key());
 assertEquals(selectElement.children().get(selectElement.children().size() - 1).text(), keyVal.value());
 }

 @Test
 public void testFormDataMethodWithCheckboxElement() {
 checkboxElement.putMap("checked", "checked");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("checkboxName", keyVal.key());
 assertEquals("on", keyVal.value());
 }
}