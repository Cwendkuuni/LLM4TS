import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setup() {
 String html = "<html><body><form action=\"/submit\" method=\"post\"><input name=\"name\" type=\"text\"/><input name=\"age\" type=\"number\"><select name=\"gender\"><option value=\"male\">Male</option><option value=\"female\" selected>Female</option></select><input name=\"agree\" type=\"checkbox\" checked/><button type=\"submit\"></button></form></body></html>";
 formElement = Jsoup.parseBodyFragment(html).getElementsByTag("form").first().as(FormElement.class);
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 List<Element> expectedElements = Arrays.asList(
 formElement.getElementsByTag("input"),
 formElement.getElementsByTag("select"),
 formElement.getElementsByTag("button")
 );
 assertEquals(expectedElements, elements);
 }

 @Test
 public void testAddElement() {
 Element inputElement = new Element("input");
 inputElement.attr("type", "hidden");
 inputElement.attr("name", "newElement");
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 assertEquals(inputElement, elements.last());
 }

 @Test
 public void testSubmit() {
 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("name", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("age", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("gender", "female"));
 expectedFormData.add(HttpConnection.KeyVal.create("agree", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("newElement", ""));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataWithEmptyInput() {
 formElement.getElementsByTag("input").first().remove();
 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataWithDisabledInput() {
 formElement.getElementsByTag("input").first().attr("disabled", "disabled");
 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataWithMissingAction() {
 formElement.removeAttr("action");
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test
 public void testFormDataWithInvalidMethod() {
 formElement.attr("method", "invalid");
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test
 public void testFormDataWithEmptyNameInput() {
 formElement.getElementsByTag("input").first().attr("name", "");
 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataWithNullNameInput() {
 formElement.getElementsByTag("input").first().attr("name", null);
 assertNull(formElement.submit().data());
 }

 @Test
 public void testFormDataWithNullValueInput() {
 formElement.getElementsByTag("input").first().val(null);
 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), null, new Attributes());
 FormElement formElement2 = formElement;
 assertTrue(formElement1.equals(formElement2));
 }

 @Test
 public void testNotEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), null, new Attributes());
 FormElement formElement2 = new FormElement(new Tag("div"), null, new Attributes());
 assertFalse(formElement1.equals(formElement2));
 }
}