import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setup() {
 String html = "<html><body><form action='https://example.com' method='post'><input type='text' name='username' /><input type='submit' name='submit' value='Submit' /></form></body></html>";
 formElement = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testFormElementIsCreatedWithCorrectTagAndBaseUri() {
 Tag tag = formElement.tag();
 String baseUri = formElement.baseUri();
 assertEquals("form", tag.name());
 assertEquals("", baseUri);
 }

 @Test
 public void testFormElementHasElementsMethodWhichReturnsEmptyListByDefault() {
 Elements elements = formElement.elements();
 assertTrue(elements.isEmpty());
 }

 @Test
 public void testFormElementCanAddAnElement() {
 Element input = new Element("input", new Attributes().put("type", "text").put("name", "password"));
 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(input, elements.first());
 }

 @Test
 public void testFormElementCanRemoveAnElement() {
 Element input = new Element("input", new Attributes().put("type", "text").put("name", "password"));
 formElement.addElement(input);
 formElement.removeChild(input);
 Elements elements = formElement.elements();
 assertTrue(elements.isEmpty());
 }

 @Test
 public void testFormElementThrowsIllegalArgumentExceptionWhenFormActionUriCannotBeDetermined() {
 formElement = new Element("form");
 try {
 formElement.submit();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testFormElementSubmitsFormWithCorrectDataWhenAllInputsAreText() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("", formData.get(0).value()); // empty inputs have empty values
 assertEquals("submit", formData.get(1).key());
 assertEquals("Submit", formData.get(1).value());
 }

 @Test
 public void testFormElementSubmitsFormWithCorrectDataWhenInputIsDisabled() {
 Element input = new Element("input", new Attributes().put("type", "text").put("name", "password").put("disabled", "disabled"));
 formElement.addElement(input);
 formElement.removeChild(0); // remove the first text input which is not form control
 formElement.addElement(input);
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormElementSubmitsFormWithCorrectDataWhenInputIsSelect() {
 Element select = new Element("select");
 Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
 Element option2 = new Element("option", new Attributes().put("value", "option2"));
 select.addElement(option1);
 select.addElement(option2);
 formElement.addElement(select);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("", formData.get(0).key()); // select elements have empty keys
 assertEquals("option1", formData.get(0).value());
 }

 @Test
 public void testFormElementSubmitsFormWithCorrectDataWhenInputIsCheckboxOrRadio() {
 Element checkbox = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkbox").put("checked", "checked"));
 formElement.addElement(checkbox);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("checkbox", formData.get(0).key());
 assertEquals("on", formData.get(0).value()); // checkbox inputs have "on" values when checked
 }

 @Test
 public void testFormElementSubmitsFormWithCorrectDataWhenInputIsASelectWithMultipleOptionsSelected() {
 Element select = new Element("select");
 Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
 Element option2 = new Element("option", new Attributes().put("value", "option2").put("selected", "selected"));
 select.addElement(option1);
 select.addElement(option2);
 formElement.addElement(select);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("", formData.get(0).key()); // select elements have empty keys
 assertEquals("option1", formData.get(1).value());
 assertEquals("option2", formData.get(2).value());
 }
}