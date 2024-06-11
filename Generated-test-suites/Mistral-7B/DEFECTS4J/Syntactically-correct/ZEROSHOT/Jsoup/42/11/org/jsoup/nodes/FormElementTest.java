import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setup() {
 String html = "<html><body>" +
 " <form action=\"https://www.example.com/submit\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" value=\"testuser\">" +
 " <input type=\"hidden\" name=\"password\" value=\"testpassword\">" +
 " <select name=\"options\">" +
 " <option value=\"option1\" selected>Option 1</option>" +
 " <option value=\"option2\">Option 2</option>" +
 " </select>" +
 " <input type=\"checkbox\" name=\"check1\" value=\"check1\">Checkbox 1" +
 " <input type=\"checkbox\" name=\"check2\" value=\"check2\">Checkbox 2" +
 " </form>" +
 " </body></html>";

 Parser parser = Jsoup.parser();
 formElement = parser.parseBodyFragment(html);
 }

 @Test
 public void testFormElementConstructor() {
 Tag tag = formElement.tag();
 String baseUri = "https://www.example.com";
 Attributes attributes = new Attributes();
 attributes.put("method", "post");
 FormElement formElement = new FormElement(tag, baseUri, attributes);

 assertEquals("form", formElement.tagName());
 assertEquals(baseUri, formElement.baseUri());
 assertEquals(attributes, formElement.attributes());
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input");
 formElement.addElement(newElement);
 Elements elements = formElement.elements();
 assertEquals(6, elements.size());
 }

 @Test
 public void testSubmit() throws Exception {
 connection = formElement.submit();
 assertNotNull(connection);
 assertEquals("https://www.example.com/submit", connection.url());
 assertEquals(Connection.Method.POST, connection.method());
 }

 @Test
 public void testFormDataEmptyForm() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataTextInput() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("username=testuser", formData.get(0).toString());
 }

 @Test
 public void testFormDataHiddenInput() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("password=testpassword", formData.get(1).toString());
 }

 @Test
 public void testFormDataSelectInput() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("options=option1", formData.get(2).toString());
 }

 @Test
 public void testFormDataCheckboxInputUnchecked() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertFalse(formData.stream().anyMatch(kv -> "check1".equals(kv.key())));
 assertFalse(formData.stream().anyMatch(kv -> "check2".equals(kv.key())));
 }

 @Test
 public void testFormDataCheckboxInputChecked() {
 Element checkbox1 = (Element) formElement.select("input[name=\"check1\"]").first();
 checkbox1.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.stream().anyMatch(kv -> "check1".equals(kv.key())));
 }
}