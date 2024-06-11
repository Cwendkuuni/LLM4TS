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
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html lang=\"en\"><body>" +
 "  <form action=\"/submit\" method=\"post\">" +
 "    <input name=\"username\" type=\"text\">" +
 "    <input name=\"password\" type=\"password\">" +
 "    <select name=\"country\">" +
 "      <option value=\"USA\">United States</option>" +
 "      <option value=\"CAN\" selected>Canada</option>" +
 "    </select>" +
 "    <input type=\"submit\" value=\"Submit\">" +
 "  </form>" +
 "</body></html>";
 formElement = Jsoup.parse(html).selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 assertEquals(Tag.FORM, formElement.tag());
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement newFormElement = new FormElement(new Tag("input"), "", new Attributes());
 formElement.addElement(newFormElement);
 Elements elementsAfterAddition = formElement.elements();
 assertEquals(4, elementsAfterAddition.size());
 }

 @Test
 public void testRemoveChild() {
 FormElement childToRemove = formElement.getChild(1);
 formElement.removeChild(childToRemove);
 Elements elementsAfterRemoval = formElement.elements();
 assertEquals(2, elementsAfterRemoval.size());
 }

 @Test
 public void testSubmit() {
 List<Connection.KeyVal> formData = formElement.submit().data();
 assertEquals(3, formData.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 FormElement formWithoutAction = new FormElement(new Tag("form"), "", new Attributes());
 formWithoutAction.submit();
 }

 @Test
 public void testFormDataForTextInput() {
 List<Connection.KeyVal> formData = formElement.formData();
 Element textInput = formElement.getElementsByTag("input").first();
 String expectedInputName = textInput.attr("name");
 String expectedInputValue = textInput.val();
 Connection.KeyVal inputData = null;
 for (Connection.KeyVal data : formData) {
 if (data.key.equals(expectedInputName)) {
 inputData = data;
 break;
 }
 }
 assertNotNull(inputData);
 assertEquals(expectedInputValue, inputData.value);
 }

 @Test
 public void testFormDataForSelectInput() {
 List<Connection.KeyVal> formData = formElement.formData();
 Element selectInput = formElement.getElementsByTag("select").first();
 String expectedSelectName = selectInput.attr("name");
 String expectedSelectValue = selectInput.select("option[selected]").first().val();
 Connection.KeyVal selectData = null;
 for (Connection.KeyVal data : formData) {
 if (data.key.equals(expectedSelectName)) {
 selectData = data;
 break;
 }
 }
 assertNotNull(selectData);
 assertEquals(expectedSelectValue, selectData.value);
 }

 @Test
 public void testFormDataForCheckboxInput() {
 FormElement checkboxElement = formElement.getElementsByTag("input").last();
 checkboxElement.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 String expectedCheckboxName = checkboxElement.attr("name");
 String expectedCheckboxValue = "on";
 Connection.KeyVal checkboxData = null;
 for (Connection.KeyVal data : formData) {
 if (data.key.equals(expectedCheckboxName)) {
 checkboxData = data;
 break;
 }
 }
 assertNotNull(checkboxData);
 assertEquals(expectedCheckboxValue, checkboxData.value);
 }

 @Test
 public void testFormDataForDisabledInput() {
 FormElement disabledInput = formElement.getElementsByTag("input").get(0);
 disabledInput.attr("disabled", "disabled");
 List<Connection.KeyVal> formData = formElement.formData();
 String disabledInputName = disabledInput.attr("name");
 Connection.KeyVal disabledInputData = null;
 for (Connection.KeyVal data : formData) {
 if (data.key.equals(disabledInputName)) {
 disabledInputData = data;
 break;
 }
 }
 assertNull(disabledInputData);
 }
}