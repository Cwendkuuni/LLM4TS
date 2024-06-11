import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 "   <form action=\"http://example.com\" method=\"post\">" +
 "      <input type=\"text\" name=\"username\" />" +
 "      <input type=\"password\" name=\"password\" />" +
 "      <select name=\"select_element\">" +
 "         <option value=\"option1\">Option 1</option>" +
 "         <option value=\"option2\" selected>Option 2</option>" +
 "         <option value=\"option3\">Option 3</option>" +
 "      </select>" +
 "      <input type=\"checkbox\" name=\"checkbox1\" checked />" +
 "      <input type=\"checkbox\" name=\"checkbox2\" />" +
 "      <input type=\"radio\" name=\"radio_element\" value=\"value1\" checked />" +
 "      <input type=\"radio\" name=\"radio_element\" value=\"value2\" />" +
 "      <button type=\"submit\">Submit</button>" +
 "   </form>" +
 " </body></html>";

 Document doc = Jsoup.parse(html);
 formElement = doc.select("form").first();
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = (FormElement) formElement;
 assertNotNull(formElement);
 assertNotNull(formElement.elements());
 }

 @Test
 public void testGetFormControls() {
 List<Element> formControls = formElement.elements();
 assertEquals(11, formControls.size());

 for (Element control : formControls) {
 assertInstanceOf(Element.class, control);
 }
 }

 @Test
 public void testGetFormDataEmpty() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testGetFormDataWithTextInput() {
 FormElement formElement = (FormElement) formElement;
 List<Connection.KeyVal> formData = formElement.formData();

 Element textInput = formElement.select("input[name=username]").first();
 assertEquals(1, formData.size());
 assertEquals(textInput.attr("name"), formData.get(0).key);
 assertEquals(textInput.val(), formData.get(0).value);
 }

 @Test
 public void testGetFormDataWithSelectElement() {
 FormElement formElement = (FormElement) formElement;
 List<Connection.KeyVal> formData = formElement.formData();

 Element selectElement = formElement.select("select[name=select_element]").first();
 assertEquals(1, formData.size());
 assertEquals(selectElement.attr("name"), formData.get(0).key);
 assertEquals(selectElement.select("option[selected]").val(), formData.get(0).value);
 }

 @Test
 public void testGetFormDataWithCheckbox() {
 FormElement formElement = (FormElement) formElement;
 List<Connection.KeyVal> formData = formElement.formData();

 Element checkbox1 = formElement.select("input[name=checkbox1][checked]").first();
 assertEquals(1, formData.size());
 assertEquals(checkbox1.attr("name"), formData.get(0).key);
 assertEquals(checkbox1.val(), formData.get(0).value);
 }

 @Test
 public void testGetFormDataWithRadioButton() {
 FormElement formElement = (FormElement) formElement;
 List<Connection.KeyVal> formData = formElement.formData();

 Element radioButton1 = formElement.select("input[name=radio_element][value=value1][checked]").first();
 assertEquals(1, formData.size());
 assertEquals(radioButton1.attr("name"), formData.get(0).key);
 assertEquals(radioButton1.val(), formData.get(0).value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutAction() {
 FormElement formElement = (FormElement) formElement;
 formElement.submit();
 // This line should throw an IllegalArgumentException
 }

 @Test
 public void testSubmitWithAction() {
 FormElement formElement = (FormElement) formElement;
 String action = formElement.attr("action");
 Connection connection = formElement.submit();
 assertEquals(action, connection.url());
 }
}