import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private static final String HTML_FORM_STRING = "<html><body>" +
         "<form action=\"http://example.com\" method=\"post\">" +
         "<input type=\"text\" name=\"username\" value=\"John\">" +
         "<input type=\"password\" name=\"password\" value=\"Password123\">" +
         "<select name=\"color\">" +
         "<option value=\"red\" selected>" +
         "Red" +
         "</option>" +
         "<option value=\"blue\">" +
         "Blue" +
         "</option>" +
         "</select>" +
         "<input type=\"checkbox\" name=\"agree\" value=\"agree\">" +
         "<input type=\"submit\" value=\"Submit\">" +
         "</form>" +
         "</body></html>";

 private Element formElement;

 @Before
 public void setUp() {
 ParseSetting.setDefaultParseTime(0);
 Parser parser = new Parser.Builder().parseTime(0).build();
 formElement = parser.parse(HTML_FORM_STRING).body().selectFirst("form");
 }

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(Tag.FORM, null, new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetFormControls() {
 Elements formControls = formElement.elements();
 assertEquals(5, formControls.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(Tag.FORM, null, new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "textfield")
         .put("value", "test"));
 formElement.addElement(inputElement);
 assertEquals(1, formElement.elements().size());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(5, formData.size());

 // Test username
 Connection.KeyVal username = null;
 for (Connection.KeyVal keyVal : formData) {
 if (keyVal.key.equals("username")) {
 username = keyVal;
 break;
 }
 }
 assertNotNull(username);
 assertEquals("John", username.value);
 // Test password
 Connection.KeyVal password = null;
 for (Connection.KeyVal keyVal : formData) {
 if (keyVal.key.equals("password")) {
 password = keyVal;
 break;
 }
 }
 assertNotNull(password);
 assertEquals("Password123", password.value);
 // Test select
 Connection.KeyVal color = null;
 for (Connection.KeyVal keyVal : formData) {
 if (keyVal.key.equals("color")) {
 color = keyVal;
 break;
 }
 }
 assertNotNull(color);
 assertEquals("red", color.value);
 // Test checkbox
 Connection.KeyVal agree = null;
 for (Connection.KeyVal keyVal : formData) {
 if (keyVal.key.equals("agree")) {
 agree = keyVal;
 break;
 }
 }
 assertNotNull(agree);
 assertEquals("agree", agree.value);
 }
}