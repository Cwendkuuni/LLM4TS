import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<html><body><form action='/submit' method='post'><input name='username' type='text'/><input name='password' type='password'/><select name='country'><option value='USA'>USA</option><option value='Canada' selected>Canada</option></select></form></body></html>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement form = document.selectFirst("form");
 assertNotNull(form);
 }

 @Test
 public void testEmptyElementsList() {
 FormElement form = document.selectFirst("form");
 Elements elements = form.elements();
 assertNotNull(elements);
 assertEquals(0, elements.size());
 }

 @Test
 public void testFormElementAddElement() {
 FormElement form = document.selectFirst("form");
 Element input = Jsoup.parse("<input name='newElement' type='text'/>").body();
 form.addElement(input);
 Elements elements = form.elements();
 assertEquals(1, elements.size());
 assertEquals(input.tagName(), elements.get(0).tagName());
 }

 @Test
 public void testFormElementSubmitEmptyForm() {
 FormElement form = document.selectFirst("form");
 Connection connection = form.submit();
 assertNull(connection);
 }

 @Test
 public void testFormElementSubmitWithData() {
 FormElement form = document.selectFirst("form");
 // set up some data
 form.select("input[name='username']").val("testUser");
 form.select("select[name='country'] option[value='USA']").attr("selected", "selected");
 // submit the form
 Connection connection = form.submit();
 List<Connection.KeyVal> formData = connection.data();
 // validate the data
 assertEquals(3, formData.size());
 assertEquals("username", formData.get(0).key);
 assertEquals("testUser", formData.get(0).val);
 assertEquals("country", formData.get(1).key);
 assertEquals("USA", formData.get(1).val);
 assertNull(formData.get(2).key);
 assertNull(formData.get(2).val);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitWithoutBaseUri() {
 FormElement form = Jsoup.parse("<form method='post'>").body();
 form.submit();
 }
}