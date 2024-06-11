package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private static final String HTML_FORM_TEST = "" +
         "<html>\n" +
         "<body>\n" +
         "<form method=\"get\" action=\"http://localhost:8080\">\n" +
         "<input type=\"text\" name=\"username\" value=\"John\">\n" +
         "<input type=\"text\" name=\"password\" value=\"password123\">\n" +
         "<select name=\"country\">\n" +
         "<option value=\"US\">United States</option>\n" +
         "<option value=\"CA\" selected=\"selected\">Canada</option>\n" +
         "</select>\n" +
         "<input type=\"submit\" value=\"Submit\">\n" +
         "</form>\n" +
         "</body>\n" +
         "</html>";

 private Element formElement;

 @Before
 public void setUp() throws Exception {
 Document doc = Jsoup.parse(HTML_FORM_TEST);
 formElement = doc.getElementsByTag("form").first();
 }

 @After
 public void tearDown() throws Exception {
 formElement = null;
 }

 @Test
 public void testFormElementConstructor() {
 Tag tag = formElement.tag();
 String baseUri = formElement.baseUri();
 Attributes attributes = formElement.attributes();

 FormElement formElement = new FormElement(tag, baseUri, attributes);

 assertEquals(tag, formElement.tag());
 assertEquals(baseUri, formElement.baseUri());
 assertEquals(attributes, formElement.attributes());
 }

 @Test
 public void testFormElementElements() {
 Elements elements = formElement.elements();
 assertEquals(elements.size(), 5);

 Element usernameInput = formElement.getElementsByTag("input").get(0);
 Element countrySelect = formElement.getElementsByTag("select").get(0);
 Element submitButton = formElement.getElementsByTag("input").get(3);

 assertEquals(elements.get(0), usernameInput);
 assertEquals(elements.get(1), usernameInput.nextElementSibling());
 assertEquals(elements.get(2), countrySelect);
 assertEquals(elements.get(3), submitButton);
 }

 @Test
 public void testFormElementAddElement() {
 Element newElement = Jsoup.parse("<input type=\"hidden\" name=\"test\">").get(0);
 FormElement formElement = new FormElement(formElement.tag(), formElement.baseUri(), formElement.attributes());

 formElement.addElement(newElement);
 Elements elements = formElement.elements();

 assertEquals(elements.size(), 6);
 assertEquals(elements.get(5), newElement);
 }

 @Test
 public void testFormElementRemoveChild() {
 Node nodeToRemove = formElement.getElementsByTag("select").get(0);
 formElement.removeChild(nodeToRemove);

 Elements elements = formElement.elements();
 assertEquals(elements.size(), 3);
 assertNull(formElement.getElementsByTag("select").get(0));
 }

 @Test
 public void testFormElementSubmit() throws Exception {
 Connection connection = formElement.submit();
 assertTrue(connection.response().isSuccess());
 assertEquals("GET", connection.method().name());

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(formData.size(), 3);

 assertEquals(formData.get(0).key(), "username");
 assertEquals(formData.get(0).val(), "John");
 assertEquals(formData.get(1).key(), "password");
 assertEquals(formData.get(1).val(), "password123");
 assertEquals(formData.get(2).key(), "country");
 assertEquals(formData.get(2).val(), "CA");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitWithNoAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 throw e;
 }
 }

}