package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class FormElementTest {

 private Element formElement;
 private Parser parser;

 @Before
 public void setUp() {
 parser = Jsoup.parser();
 String html = "<form action='/' method='post'>\n" +
 "    <input name='field1' type='text' value='value1'>\n" +
 "    <select name='field2'>\n" +
 "        <option value='option1'>Option 1</option>\n" +
 "        <option value='option2' selected>Option 2</option>\n" +
 "    </select>\n" +
 "    <input name='field3' type='checkbox' value='value3' checked>\n" +
 "    <input name='field4' type='hidden' value='value4'>\n" +
 "    <input type='submit' name='submit'>\n" +
 "    <input type='hidden' name='_method' value='put'>\n" +
 "    <input type='hidden' name='_csrf' value='csrfToken'>\n" +
 "    <input name='field5' type='text'>\n" +
 "</form>";
 formElement = parser.parse(html);
 }

 @Test
 public void testFormElementConstruction() {
 Tag tag = formElement.tag();
 assertSame(Tag.FORM, tag);
 assertEquals("form", tag.tagName());
 assertNotNull(formElement.baseUri());
 Attributes attributes = formElement.attributes();
 assertEquals(8, attributes.size());
 assertEquals("post", attributes.get("method"));
 assertEquals("/", attributes.get("action"));
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 Element input = parser.parse("<input name='field6' type='text' value='value6' />");
 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertEquals(6, elements.size());
 assertSame(formElement, elements.get(5).parent());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 assertEquals(Connection.Method.POST, connection.method());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(6, formData.size());

 assertEquals("field1", formData.get(0).key());
 assertEquals("value1", formData.get(0).val());
 assertEquals("field2", formData.get(1).key());
 assertEquals("option2", formData.get(1).val());
 assertEquals("field3", formData.get(2).key());
 assertEquals("value3", formData.get(2).val());
 assertEquals("field5", formData.get(3).key());
 assertEquals(null, formData.get(3).val());
 assertEquals("submit", formData.get(4).key());
 assertEquals(null, formData.get(4).val());
 assertEquals("_method", formData.get(5).key());
 assertEquals("put", formData.get(5).val());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 FormElement formElementNoAction = parser.parse("<form method='post'>").elements().get(0);
 try {
 formElementNoAction.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testFormDataForSelect() {
 Element selectElement = formElement.select("select[name='field2']").first();
 assertEquals("field2", selectElement.attr("name"));
 Elements options = selectElement.select("option[selected]");
 assertEquals(1, options.size());
 assertEquals("Option 2", options.get(0).text());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("field2", formData.get(1).key());
 assertEquals("option2", formData.get(1).val());
 }

 @Test
 public void testFormDataForCheckbox() {
 Element checkboxElement = formElement.select("input[name='field3'][checked]").first();
 assertEquals("field3", checkboxElement.attr("name"));
 assertEquals("value3", checkboxElement.val());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("field3", formData.get(2).key());
 assertEquals("value3", formData.get(2).val());
 }
}