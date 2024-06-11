import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private static final String HTML_FORM = "<html><head></head><body>" +
         "<form action=\"/submit\" method=\"post\">" +
         "<input name=\"input1\" type=\"text\">" +
         "<input name=\"input2\" type=\"hidden\" value=\"hiddenValue\">" +
         "<select name=\"select1\">" +
         "<option value=\"option1\">Option 1</option>" +
         "<option value=\"option2\" selected>Option 2</option>" +
         "</select>" +
         "<input name=\"input3\" type=\"checkbox\" checked>" +
         "<input name=\"input4\" type=\"checkbox\">" +
         "</form>" +
         "</body></html>";

 private Element rootElement;

 @Before
 public void setUp() throws Exception {
 rootElement = Jsoup.parse(HTML_FORM);
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = (FormElement) rootElement.select("form").first();
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = (FormElement) rootElement.select("form").first();
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertTrue(elements.size() > 0);
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = (FormElement) rootElement.select("form").first();
 Element inputElement = new Element("input");
 inputElement.attr("name", "newInput");
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 assertTrue(elements.contains(inputElement));
 }

 @Test
 public void testSubmitMethod() {
 FormElement formElement = (FormElement) rootElement.select("form").first();
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithNoActionUrl() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 connection = formElement.submit();
 }

 @Test
 public void testFormDataMethod() {
 FormElement formElement = (FormElement) rootElement.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(4, formData.size());
 }

 @Test
 public void testFormDataMethodWithSelectElement() {
 FormElement formElement = (FormElement) rootElement.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("option2", formData.get(0).getKey());
 assertEquals("Option 2", formData.get(0).getValue());
 }

 @Test
 public void testFormDataMethodWithCheckboxElementChecked() {
 FormElement formElement = (FormElement) rootElement.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.get(3).getKey().startsWith("input4"));
 }

 @Test
 public void testFormDataMethodWithCheckboxElementNotChecked() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element checkboxElement = new Element("input");
 checkboxElement.attr("name", "input4");
 checkboxElement.attr("type", "checkbox");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertFalse(formData.get(3).getKey().startsWith("input4"));
 }

 @Test
 public void testEqualsMethod() {
 FormElement formElement1 = (FormElement) rootElement.select("form").first();
 FormElement formElement2 = (FormElement) rootElement.select("form").first();
 assertTrue(formElement1.equals(formElement2));
 }

 @Test
 public void testEqualsMethodWithDifferentElements() {
 FormElement formElement1 = (FormElement) rootElement.select("form").first();FormElement formElement2 = new FormElement(new Tag("form"), "", new Attributes());
 assertFalse(formElement1.equals(formElement2));
 }
}