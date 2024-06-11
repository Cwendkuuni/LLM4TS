import org.jsoup.Jsoup;
import org.jsoup.helper.Elements;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 formElement.addElement(textInput);
 Element passwordInput = new Element("input", new Attributes().put("type", "password").put("name", "password"));
 formElement.addElement(passwordInput);

 List<Element> elements = formElement.elements();
 assertEquals(2, elements.size());
 assertNotNull(elements.get(0));
 assertNotNull(elements.get(1));
 assertEquals(textInput.tagName(), elements.get(0).tagName());
 assertEquals(passwordInput.tagName(), elements.get(1).tagName());
 }

 @Test
 public void testSubmitWithNoActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 try {
 formElement.submit();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithEmptyActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form", new Attribute("action", "")), "http://test.com", new Attributes());
 try {
 formElement.submit();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithAbsoluteActionURL() {
 FormElement formElement = new FormElement(new Tag("form", new Attribute("action", "http://test.com/submit")), "http://test.com", new Attributes());
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testSubmitWithRelativeActionURL() {
 FormElement formElement = new FormElement(new Tag("form", new Attribute("action", "/submit")), "http://test.com", new Attributes());
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataWithNoFormControls() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataWithTextInput() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username").put("value", "test"));
 formElement.addElement(textInput);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("username", keyVal.key());
 assertEquals("test", keyVal.value());
 }

 @Test
 public void testFormDataWithCheckboxInput() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element checkboxInput = new Element("input", new Attributes().put("type", "checkbox").put("name", "agree").put("value", "yes"));
 formElement.addElement(checkboxInput);
 formElement.addElement(checkboxInput); // add another checkbox with the same name
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 // checkboxes are sent as arrays, not key-value pairs
 assertEquals(2, ((List<String>) formData.get(0).value()).size());
 assertEquals("yes", ((List<String>) formData.get(0).value()).get(0));
 assertEquals("yes", ((List<String>) formData.get(0).value()).get(1));
 }

 @Test
 public void testFormDataWithMultipleInputs() {
 FormElement formElement = new Element("form", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 Element passwordInput = new Element("input", new Attributes().put("type", "password").put("name", "password"));
 formElement.addElement(textInput);
 formElement.addElement(passwordInput);

 List<Element> elements = formElement.elements();
 assertEquals(2, elements.size());

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());

 Connection.KeyVal usernameKeyVal = formData.get(0);
 assertEquals("username", usernameKeyVal.key());
 assertEquals(Arrays.asList(""), usernameKeyVal.value()); // empty string since no value provided

 Connection.KeyVal passwordKeyVal = formData.get(1);
 assertEquals("password", passwordKeyVal.key());
 assertNull(passwordKeyVal.value());
 }
}