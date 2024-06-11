import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<form action=\"http://example.com\" method=\"post\">" +
 "  <input type=\"text\" name=\"username\" />" +
 "  <input type=\"hidden\" name=\"password\" value=\"secret\" />" +
 "  <select name=\"color\">" +
 "    <option value=\"red\" selected></option>" +
 "    <option value=\"green\"></option>" +
 "  </select>" +
 "  <input type=\"checkbox\" name=\"agree\" value=\"true\" checked />" +
 "  <input type=\"submit\" value=\"Submit\" />" +
 "</form>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = doc.selectFirst("form");
 assertNotNull(formElement);
 assertEquals("form", formElement.tagName());
 }

 @Test
 public void testGetElements() {
 FormElement formElement = doc.selectFirst("form");
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = doc.selectFirst("form");
 Element textInput = new Element("input");
 textInput.attr("type", "text");
 textInput.attr("name", "newInput");
 formElement.addElement(textInput);
 Elements elements = formElement.elements();
 assertEquals(6, elements.size());
 assertEquals("input", elements.get(5).tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutAction() {
 FormElement formElement = doc.selectFirst("form:has(action:absent)");
 formElement.submit();
 }

 @Test
 public void testSubmit() {
 FormElement formElement = doc.selectFirst("form");
 Connection connection = formElement.submit();
 assertEquals("POST", connection.method().name());
 assertEquals("http://example.com", connection.url().toString());
 }

 @Test
 public void testFormData() {
 FormElement formElement = doc.selectFirst("form");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());

 assertEquals("username", formData.get(0).key());
 assertNull(formData.get(0).value());

 assertEquals("red", formData.get(1).key());
 assertEquals("", formData.get(1).value());

 assertEquals("password", formData.get(2).key());
 assertEquals("secret", formData.get(2).value());

 assertEquals("color", formData.get(3).key());
 assertEquals("red", formData.get(3).value());

 assertEquals("agree", formData.get(4).key());
 assertEquals("true", formData.get(4).value());
 }
}