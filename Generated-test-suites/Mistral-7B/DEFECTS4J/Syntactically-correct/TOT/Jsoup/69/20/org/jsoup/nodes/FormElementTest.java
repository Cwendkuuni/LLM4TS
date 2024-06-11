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

import static org.junit.Assert.*;

public class FormElementTest {

 private Element form;

 @Before
 public void setUp() {
 String html = "<html><head></head><body><form action=\"/submit\" method=\"post\">" +
 " <input type=\"text\" name=\"input1\" value=\"value1\"/>" +
 " <select name=\"select1\"><option value=\"option1\">Option 1</option>" +
 " <option value=\"option2\">Option 2</option>" +
 " <option selected value=\"option3\">Option 3</option></select>" +
 " <input type=\"checkbox\" name=\"checkbox1\" value=\"value1\" checked/>" +
 " <input type=\"checkbox\" name=\"checkbox2\" value=\"value2\"/>" +
 " <input type=\"submit\"/>" +
 "</form></body></html>";
 form = Jsoup.parse(html).body().getFirstElement();
 }

 @Test
 public void testElements() {
 FormElement formElement = (FormElement) form;
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 assertTrue(elements.get(0) instanceof Element);
 assertTrue(elements.get(1) instanceof InputElement);
 assertTrue(elements.get(2) instanceof SelectElement);
 assertTrue(elements.get(3) instanceof InputElement);
 assertTrue(elements.get(4) instanceof InputElement);
 }

 @Test
 public void testSubmitWithValidFormData() {
 FormElement formElement = (FormElement) form;
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("POST", connection.method().name());
 assertEquals("/submit", connection.url());
 assertEquals(5, formData.size());
 assertEquals("input1", formData.get(0).key());
 assertEquals("value1", formData.get(0).val());
 assertEquals("select1", formData.get(1).key());
 assertEquals("option3", formData.get(1).val());
 assertEquals("checkbox1", formData.get(2).key());
 assertEquals("value1", formData.get(2).val());
 assertEquals("checkbox2", formData.get(3).key());
 assertEquals("", formData.get(3).val());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoActionUrl() {
 FormElement formElement = (FormElement) Jsoup.parse("<html><body><form>" +
 " <input type=\"text\" name=\"input1\" value=\"value1\"/>" +
 " </form></body></html>").body().getFirstElement();
 formElement.submit();
 }

 @Test
 public void testSubmitWithInvalidMethod() {
 FormElement formElement = (FormElement) form;
 Connection connection = null;
 try {
 connection = formElement.submit();
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertEquals(Connection.Method.GET, connection.method());
 }
}