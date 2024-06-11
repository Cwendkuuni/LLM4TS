import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setup() {
 String html = "<html><body>" +
 " <form action=\"/submit\" method=\"post\">" +
 " <input type=\"text\" name=\"input1\" value=\"value1\">" +
 " <input type=\"text\" name=\"input2\" value=\"value2\">" +
 " <select name=\"select\">" +
 " <option value=\"option1\">Option 1</option>" +
 " <option selected value=\"option2\">Option 2</option>" +
 " </select>" +
 " <input type=\"submit\" value=\"Submit\">" +
 " </form>" +
 "</body></html>";
 formElement = Jsoup.parse(html).body().getFirstFormElements().get(0);
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 Element input = new Element("input");
 input.attr("type", "text");
 input.attr("name", "newInput");
 formElement.addElement(input);
 Elements elementsAfterAdd = formElement.elements();
 assertEquals(4, elementsAfterAdd.size());
 }

 @Test
 public void testSubmitEmptyForm() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 Connection connection = formElement.submit();
 assertNull(connection);
 }

 @Test
 public void testSubmitFormWithData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithoutBaseUri() {
 formElement = new FormElement(new Tag("form"), null, new Attributes());
 assertThrows(IllegalArgumentException.class, formElement::submit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithInvalidMethod() {
 formElement.attr("method", "invalidMethod");
 List<Connection.KeyVal> formData = formElement.formData();
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test
 public void testFormDataWithDisabledInput() {
 formElement.getElementsByAttribute("disabled").first().attr("disabled", "disabled");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertNull(formData.get(1)); // disabled input value is not added to form data
 }

 @Test
 public void testFormDataWithSelectElement() {
 formElement.getElementsByTagName("select").first().getElementsByTag("option").get(1).attr("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 assertEquals("option2", formData.get(1).getKey());
 }

 @Test
 public void testFormDataWithCheckboxOrRadioButton() {
 Element checkbox = formElement.getElementsByTagName("input").get(2);
 checkbox.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 assertEquals("input2", formData.get(2).getKey());
 }
}