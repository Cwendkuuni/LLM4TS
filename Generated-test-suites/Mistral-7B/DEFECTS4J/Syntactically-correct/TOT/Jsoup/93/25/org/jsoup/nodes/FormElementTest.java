import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 assertEquals("form", formElement.tagName());
 assertNotNull(formElement.elements());
 }

 @Test
 public void testGetFormControls() {
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "userName"));
 Element passwordInput = new Element("input", new Attributes().put("type", "password").put("name", "password"));
 formElement.addElement(textInput);
 formElement.addElement(passwordInput);

 List<Element> formControls = formElement.elements();
 assertEquals(2, formControls.size());
 assertEquals(textInput, formControls.get(0));
 assertEquals(passwordInput, formControls.get(1));
 }

 @Test
 public void testSubmitFormWithEmptyFormData() {
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "userName"));
 formElement.addElement(textInput);

 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertTrue(formData.isEmpty());
 }

 @Test
 public void testSubmitFormWithData() {
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "userName").put("value", "John Doe"));
 Element passwordInput = new Element("input", new Attributes().put("type", "password").put("name", "password").put("value", "password123"));
 formElement.addElement(textInput);
 formElement.addElement(passwordInput);

 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals(2, formData.size());
 assertEquals("userName", formData.get(0).key());
 assertEquals("John Doe", formData.get(0).value());
 assertEquals("password", formData.get(1).key());
 assertEquals("password123", formData.get(1).value());
 }

 @Test
 public void testSubmitFormWithDisabledInput() {
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "userName").put("disabled", "disabled"));
 formElement.addElement(textInput);

 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertFalse(formData.isEmpty());
 assertNull(formData.get(0));
 }

 @Test
 public void testSubmitFormWithMissingNameAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text"));
 formElement.addElement(textInput);

 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitFormWithMissingActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "userName").put("value", "John Doe"));
 formElement.addElement(textInput);

 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 // expected
 }
 }
}