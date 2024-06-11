package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 " <form action=\"https://www.example.com/\">" +
 " <input type=\"text\" name=\"username\" value=\"John\"/>" +
 " <input type=\"text\" name=\"password\" value=\"Password\"/>" +
 " <select name=\"select\">" +
 " <option value=\"Option1\">Option 1</option>" +
 " <option value=\"Option2\" selected>Option 2</option>" +
 " <option value=\"Option3\">Option 3</option>" +
 " </select>" +
 " <input type=\"checkbox\" name=\"checkBox\" value=\"CheckBoxValue\">Check Box" +
 " </form>" +
 " </body></html>";

 Parser parser = Jsoup.parser();
 Document document = parser.parse(html);
 formElement = document.select("form").first();
 connection = formElement.submit();
 }

 @Test
 public void testFormElementCreation() {
 // Test FormElement constructor
 Tag tag = new Tag("form");
 FormElement formElement = new FormElement(tag, "http://test.com", new Attributes());
 assertNotNull(formElement);
 assertSame(tag, formElement.tag());
 }

 @Test
 public void testElements() {
 // Test elements() method
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertSame(formElement, elements.parent());
 Equals verifier = new EqualsVerifier();
 verifier.addExpectedDifferences("element classes");
 verifier.addSuppressedDifferences("element classes");
 verifier.assertEquals(formElement.children(), elements);
 }

 @Test
 public void testAddElement() {
 // Test addElement() method
 Element input = new Element("input");
 input.attr("type", "text");
 input.attr("name", "newElement");
 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertTrue(elements.size() > formElement.elements().size());
 assertEquals(input, elements.get(elements.size() - 1));
 }

 @Test
 public void testRemoveChild() {
 // Test removeChild() method
 Element inputToRemove = formElement.select("input[name=password]").first();
 formElement.removeChild(inputToRemove);
 Elements elements = formElement.elements();
 assertTrue(elements.size() < formElement.elements().size());
 assertNull(formElement.select("input[name=password]").first());
 }

 @Test
 public void testSubmit() {
 // Test submit() method
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("John", formData.get(0).value());
 assertEquals("password", formData.get(1).key());
 assertEquals("Password", formData.get(1).value());
 assertEquals("select", formData.get(2).key());
 assertEquals("Option2", formData.get(2).value());
 assertEquals("checkBox", formData.get(3).key());
 assertTrue(formData.get(3).value().equalsIgnoreCase("CheckBoxValue") || formData.get(3).value().isEmpty());
 // Test submit() method throws IllegalArgumentException
 Element formWithoutAction = new Element("form", new Attributes().put("action", ""));
 assertThrows(IllegalArgumentException.class, formWithoutAction::submit);
 }

 @Test
 public void testFormDataForCheckbox() {
 // Test formData() method for checkbox
 Element checkboxElement = formElement.select("input[name=checkBox]").first();
 assertNull(checkboxElement.val());
 checkboxElement.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());
 assertEquals("username", formData.get(0).key());
 //...
 assertEquals("checkBox", formData.get(3).key());
 assertTrue(formData.get(3).value().equalsIgnoreCase("CheckBoxValue"));
 }

 @Test
 public void testFormDataForSelect() {
 // Test formData() method for select
 Element selectElement = formElement.select("select").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());
 assertEquals("username", formData.get(0).key());
 //...
 assertEquals("select", formData.get(2).key());
 assertEquals("Option2", formData.get(2).value());
 }
}