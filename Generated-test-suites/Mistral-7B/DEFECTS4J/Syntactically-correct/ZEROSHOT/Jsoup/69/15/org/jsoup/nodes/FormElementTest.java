import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setUp() {
 Parser parser = Jsoup.parse("", new Document.OutputSettings().parser(new FormElementParser()));
 Tag formTag = new Tag("form");
 Attributes formAttributes = new Attributes().put("action", "/submit").put("method", "GET");
 formElement = new FormElement(formTag, "", formAttributes);
 }

 @Test
 public void testElements() {
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 Element passwordInput = new Element("input", new Attributes().put("type", "password").put("name", "password"));
 formElement.addElement(textInput);
 formElement.addElement(passwordInput);

 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(2, elements.size());
 assertEquals(textInput, elements.get(0));
 assertEquals(passwordInput, elements.get(1));
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(0, formData.size());

 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username").put("value", "test"));
 formElement.addElement(textInput);
 formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("username", keyVal.key);
 assertEquals("test", keyVal.value);

 Element selectElement = new Element("select", new Attributes().put("name", "status"));
 Element optionOne = new Element("option", new Attributes().put("value", "open").put("selected", "selected"));
 Element optionTwo = new Element("option", new Attributes().put("value", "closed"));
 selectElement.add(optionOne);
 selectElement.add(optionTwo);
 formElement.addElement(selectElement);
 formData = formElement.formData();
 assertEquals(2, formData.size());
 Connection.KeyVal keyValOne = formData.get(0);
 assertEquals("username", keyValOne.key);
 assertEquals("test", keyValOne.value);
 Connection.KeyVal keyValTwo = formData.get(1);
 assertEquals("status", keyValTwo.key);
 assertEquals("open", keyValTwo.value);
 }
}

class FormElementParser extends DefaultHtmlParser {
 @Override
 public Element parseTag(String tagName, Attributes attr, boolean isOpeningTag, int begin, int end) {
 if ("form".equalsIgnoreCase(tagName)) {
 return new FormElement(Tag.valueOf(tagName), "", attr);
 }
 return super.parseTag(tagName, attr, isOpeningTag, begin, end);
 }
}