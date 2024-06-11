import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setUp() {
 Parser parser = new Parser.Builder().build();
 Setting setting = new Setting.Builder().build();
 String html = "<html><head></head><body>" +
 " <form action='/submit' method='post'>" +
 " <input type='text' name='username' value='test'>" +
 " <input type='text' name='password' value='password'>" +
 " <select name='country'>" +
 " <option value='USA'>USA</option>" +
 " <option selected value='Canada'>Canada</option>" +
 " </select>" +
 " <input type='radio' name='gender' value='male' checked>" +
 " <input type='radio' name='gender' value='female'>" +
 " <input type='submit'>" +
 " </form>" +
 "</body></html>";
 formElement = parser.parse(html, setting);
 }

 @Test
 public void testFormElementConstruction() {
 Tag formTag = (Tag) formElement.tag();
 assertNotNull(formTag);
 assertEquals("form", formTag.tagName());
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(6, elements.size());
 }

 @Test
 public void testAddElement() {
 Element input = new Element("input");
 input.attr("type", "hidden");
 input.attr("name", "newElement");
 FormElement form = (FormElement) formElement;
 form.addElement(input);
 Elements elements = form.elements();
 assertNotNull(elements);
 assertEquals(7, elements.size());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithoutBaseUri() {
 Element formElementWithoutBaseUri = new FormElement(new Tag("form"), null, new Attributes());
 assertThrows(IllegalStateException.class, () -> formElementWithoutBaseUri.submit());
 }

 @Test
 public void testFormData() {
 FormElement form = (FormElement) formElement;
 List<Connection.KeyVal> formData = form.formData();
 assertNotNull(formData);
 assertEquals(6, formData.size());
 }
}