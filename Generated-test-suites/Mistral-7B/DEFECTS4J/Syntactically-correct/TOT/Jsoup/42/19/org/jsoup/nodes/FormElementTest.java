import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<html><body><form action='/submit' method='post'><input type='text' name='username' value='test'><input type='submit' name='submit' value='Submit'></form></body></html>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = document.selectFirst("form");
 assertNotNull(formElement);
 }

 @Test
 public void testElements() {
 FormElement formElement = document.selectFirst("form");
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(2, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = document.selectFirst("form");
 Element inputElement = Jsoup.parse("<input type='text' name='password' value='password'>").body();
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testSubmit() {
 FormElement formElement = document.selectFirst("form");
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormData() {
 FormElement formElement = document.selectFirst("form");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 }

 @Test
 public void testEmptyFormData() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testSubmitWithEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithNoAction() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes().put("name", "myForm"));
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithInvalidMethod() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes().put("name", "myForm").put("method", "invalidMethod"));
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}