import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setup() {
 String html = "<html><body><form action='/test' method='post'>" +
         "<input type='text' name='username'/>" +
         "<select name='selectElement'>" +
         "<option value='option1'>Option 1</option>" +
         "<option value='option2' selected>Option 2</option>" +
         "</select>" +
         "<input type='checkbox' name='checkbox' checked/>" +
         "<input type='submit'/>" +
         "</form></body></html>";
 formElement = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testFormElementConstructor() {
 Tag tag = formElement.tag();
 String baseUri = formElement.baseUri();
 Attributes attributes = formElement.attributes();
 FormElement formElement = new FormElement(tag, baseUri, attributes);
 assertSame(formElement.tag(), tag);
 assertSame(formElement.baseUri(), baseUri);
 assertSame(formElement.attributes(), attributes);
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertEquals(4, elements.size());
 assertNotNull(elements.get(0));
 assertSame(elements.get(0).tagName(), "input");
 assertSame(elements.get(0).attr("type"), "text");
 assertSame(elements.get(0).attr("name"), "username");
 assertNotNull(elements.get(1));
 assertSame(elements.get(1).tagName(), "select");
 assertNotNull(elements.get(2));
 assertSame(elements.get(2).tagName(), "option");
 assertSame(elements.get(2).val(), "Option 2");
 assertNotNull(elements.get(3));
 assertSame(elements.get(3).tagName(), "input");
 assertSame(elements.get(3).attr("type"), "submit");
 }

 @Test
 public void testAddElement() {
 Element newElement = Jsoup.parse("<input type='text' name='newElement'/>").body().first();
 FormElement formElement = this.formElement;
 formElement.addElement(newElement);
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 assertSame(elements.get(4).tagName(), "input");
 assertSame(elements.get(4).attr("name"), "newElement");
 }

 @Test
 public void testSubmit() {
 List<Connection.KeyVal> formData = formElement.submit().request().cookies();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("username", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("selectElement", "option2"));
 expectedFormData.add(HttpConnection.KeyVal.create("checkbox", "on"));
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyActionUrl() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("username", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("selectElement", "option2"));
 expectedFormData.add(HttpConnection.KeyVal.create("checkbox", "on"));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "", new Attributes());
 FormElement formElement2 = this.formElement;
 assertFalse(formElement1.equals(formElement2));

 Element newElement = Jsoup.parse("<input type='text' name='newElement'/>").body().first();
 FormElement formElement3 = new FormElement(formElement.tag(), formElement.baseUri(), new Attributes().put("name", "newName"));
 FormElement formElement4 = new FormElement(formElement.tag(), formElement.baseUri(), new Attributes().put("name", "newName").put("class", "newClass"));
 assertFalse(formElement3.equals(formElement4));
 assertFalse(formElement1.equals(formElement3));
 assertFalse(formElement1.equals(newElement));

 FormElement formElement5 = new FormElement(formElement.tag(), formElement.baseUri(), formElement.attributes());
 assertTrue(formElement5.equals(formElement));
 }
}