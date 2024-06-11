import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private Element document;

 @Before
 public void setup() {
 String html = "<html>\n" +
 "  <body>\n" +
 "    <form>\n" +
 "      <input type=\"text\" name=\"username\" value=\"John Doe\">\n" +
 "      <input type=\"email\" name=\"email\" value=\"john.doe@example.com\">\n" +
 "      <input type=\"submit\" name=\"submit\">\n" +
 "      <select name=\"options\">" +
 "        <option value=\"option1\">Option 1</option>" +
 "        <option value=\"option2\" selected>Option 2</option>" +
 "      </select>\n" +
 "      <input type=\"checkbox\" name=\"checkbox\" value=\"checked\">\n" +
 "      <input type=\"radio\" name=\"radio\" value=\"value1\">\n" +
 "      <input type=\"radio\" name=\"radio\" value=\"value2\" checked>\n" +
 "    </form>\n" +
 "  </body>\n" +
 "</html>";
 document = Jsoup.parse(html);
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = (FormElement) document.select("form").first();
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Element> elements = formElement.elements();
 assertEquals(6, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = (FormElement) document.select("form").first();
 Element newElement = document.createElement("input");
 newElement.attr("type", "hidden");
 newElement.attr("name", "newElement");
 formElement.addElement(newElement);
 List<Element> elements = formElement.elements();
 assertEquals(7, elements.size());
 }

 @Test
 public void testSubmitMethodFormWithActionURL() {
 FormElement formElement = (FormElement) document.select("form").first();
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testSubmitMethodWithoutActionURL() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test
 public void testFormDataMethod() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(8, formData.size());
 for (Connection.KeyVal keyVal : formData) {
 System.out.println("Name: " + keyVal.key);
 System.out.println("Value: " + keyVal.value);
 }
 }

 @Test
 public void testFormDataMethodWithEmptyValues() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.addElement(new Element("input", new Attributes().put("name", "name")));
 formElement.addElement(new Element("input", new Attributes().put("name", "emptyValue")));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertNull(formData.get(1).value);
 }

 @Test
 public void testFormDataMethodWithMultipleSelectOptions() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("option2", formData.get(2).value);
 }

 @Test
 public void testFormDataMethodWithMultipleCheckboxes() {
 FormElement formElement = (FormElement) document.select("form").first();
 formElement.select("input[type=checkbox]").first().attr("checked", "checked");
 formElement.select("input[type=checkbox]").get(1).attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(10, formData.size());
 assertNotNull(formData.get(8).value);
 assertNotNull(formData.get(9).value);
 }

 @Test
 public void testFormDataMethodWithMultipleRadios() {
 FormElement formElement = (FormElement) document.select("form").first();
 formElement.select("input[type=radio]").first().attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("value1", formData.get(6).value);
 }
}