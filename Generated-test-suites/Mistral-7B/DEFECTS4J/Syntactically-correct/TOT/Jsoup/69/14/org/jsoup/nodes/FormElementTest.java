import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
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
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetFormControls() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("name", "inputName"));
 formElement.add(inputElement);
 Elements formControls = formElement.elements();
 assertFalse(formControls.isEmpty());
 assertEquals(1, formControls.size());
 assertTrue(formControls.get(0) instanceof Element);
 assertEquals("inputName", formControls.get(0).attr("name"));
 }

 @Test
 public void testFormSubmissionWithGETMethod() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes().put("action", "/submit"));
 formElement.add(new Element("input", new Attributes().put("name", "inputName").put("value", "inputValue")));
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertFalse(formData.isEmpty());
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("inputName", keyVal.key());
 assertEquals("inputValue", keyVal.value());
 }

 @Test
 public void testFormSubmissionWithPOSTMethod() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes().put("action", "/submit"));
 formElement.add(new Element("input", new Attributes().put("name", "inputName").put("value", "inputValue")));
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertFalse(formData.isEmpty());
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("inputName", keyVal.key());
 assertEquals("inputValue", keyVal.value());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormSubmissionWithoutActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri");
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormSubmissionWithEmptyFormData() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes().put("action", "/submit"));
 formElement.add(new Element("input", new Attributes().put("name", "inputName")));
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertTrue(formData.isEmpty());
 }
}