import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.ElementFactory;
import org.jsoup.select.Elements;
import org.jsoup.helper.HttpConnection;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<html><body><form action=\"/submit\" method=\"post\"><input name=\"input1\" type=\"text\"><input name=\"input2\" type=\"text\"><input name=\"input3\" type=\"hidden\"><select name=\"select1\"><option value=\"option1\">Option 1</option><option value=\"option2\" selected>Option 2</option></select></form></body></html>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = document.select("form").first();
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = document.select("form").first();
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = document.select("form").first();
 Element inputElement = ElementFactory.parseHtml("<input name=\"input4\" type=\"text\">");
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(4, elements.size());
 }

 @Test
 public void testSubmitMethod() {
 FormElement formElement = document.select("form").first();
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataMethodTextInput() {
 FormElement formElement = document.select("form").first();
 List<HttpConnection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 HttpConnection.KeyVal keyVal = formData.get(0);
 assertEquals("input1", keyVal.key());
 assertEquals("", keyVal.value());
 }

 @Test
 public void testFormDataMethodSelectInput() {
 FormElement formElement = document.select("form").first();
 List<HttpConnection.KeyVal> formData = formElement.formData();
 HttpConnection.KeyVal keyVal = formData.get(2);
 assertEquals("select1", keyVal.key());
 assertEquals("option2", keyVal.value());
 }

 @Test
 public void testFormDataMethodHiddenInput() {
 FormElement formElement = document.select("form").first();
 List<HttpConnection.KeyVal> formData = formElement.formData();
 HttpConnection.KeyVal keyVal = formData.get(3);
 assertEquals("input3", keyVal.key());
 assertEquals("", keyVal.value());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodNoActionAttribute() {
 FormElement formElement = document.parse("<html><body><form><input name=\"input1\"></form></body></html>").select("form").first();
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodNoMethodAttribute() {
 FormElement formElement = document.parse("<html><body><form action=\"/submit\"><input name=\"input1\" type=\"text\"></form></body></html>").select("form").first();
 formElement.submit();
 }
}