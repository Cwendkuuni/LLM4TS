import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private ParseSetup setup;
 private Document document;

 @Before
 public void setUp() {
 setup = new ParseSetup.Builder().build();
 Parser parser = setup.parser();
 String html = "<html><body>" +
 "  <form action='/submit' method='post'>" +
 "    <input type='text' name='username' value='John'>" +
 "    <input type='text' name='password' value='password'>" +
 "    <select name='colors'>" +
 "      <option value='red'>Red</option>" +
 "      <option value='green' selected>Green</option>" +
 "      <option value='blue'>Blue</option>" +
 "    </select>" +
 "    <input type='checkbox' name='hobbies' value='swim'>Swim" +
 "    <input type='checkbox' name='hobbies' value='run'>Run" +
 "  </form>" +
 "  <input type='text' name='other' value='Other'>" +
 "</body></html>";
 document = parser.parse(html);
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = (FormElement) document.selectFirst("form");
 assertEquals("form", formElement.tagName());
 assertEquals("/submit", formElement.baseUri());
 assertFalse(formElement.hasAttr("action"));
 }

 @Test
 public void testElementsMethod() {
 Elements expectedElements = document.select("input[type='text'], select, input[type='checkbox']");
 Elements actualElements = formElement.elements();
 assertEquals(expectedElements.size(), actualElements.size());
 for (Element element : expectedElements) {
 assertTrue(formElement.elements().contains(element));
 }
 }

 @Test
 public void testAddElementMethod() {
 Element textElement = document.selectFirst("input[type='text']");
 FormElement formElement = (FormElement) document.selectFirst("form");
 formElement.addElement(textElement);
 Elements actualElements = formElement.elements();
 Element expectedElement = textElement;
 assertTrue(formElement.equals(formElement.addElement(expectedElement)));
 assertEquals(1 + expectedElements.size(), actualElements.size());
 assertEquals(expectedElement, actualElements.get(0));
 }

 @Test
 public void testSubmitMethod() {
 Connection connection = formElement.submit();
 assertEquals(Connection.Method.POST, connection.method());
 assertEquals("/submit", connection.url());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(7, formData.size());
 List<String> expectedKeys = new ArrayList<String>() {{
 add("username");
 add("password");
 add("colors");
 add("hobbies");
 add("hobbies");
 add("other");
 add(""); // empty key for select option
 }};
 List<String> expectedValues = new ArrayList<String>() {{
 add("John");
 add("password");
 add("green");
 add("Swim");
 add("Run");
 add("Other");
 add("Red"); // value of selected option
 }};
 for (int i = 0; i < formData.size(); i++) {
 Connection.KeyVal keyVal = formData.get(i);
 assertEquals(expectedKeys.get(i), keyVal.key());
 assertEquals(expectedValues.get(i), keyVal.val());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithoutBaseUri() {
 FormElement formElement = document.selectFirst("form");
 formElement.submit();
 }
}