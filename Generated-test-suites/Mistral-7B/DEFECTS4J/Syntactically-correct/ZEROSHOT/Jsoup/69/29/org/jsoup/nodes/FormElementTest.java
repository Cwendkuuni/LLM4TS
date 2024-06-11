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
 private Connection connection;

 @Before
 public void setup() {
 String html = "<form action=\"http://example.com\">" +
 "  <input name=\"username\" type=\"text\">" +
 "  <input name=\"password\" type=\"password\">" +
 "  <input name=\"submit\" type=\"submit\" value=\"Submit\">" +
 "  <select name=\"selectElement\">" +
 "    <option value=\"option1\">Option 1</option>" +
 "    <option value=\"option2\" selected>Option 2</option>" +
 "  </select>" +
 "  <input name=\"checkboxElement\" type=\"checkbox\" checked>" +
 "  <input name=\"radioElement\" type=\"radio\" checked>" +
 "  <input name=\"disabledElement\" type=\"text\" disabled>" +
 "  <input name=\"hiddenElement\" type=\"hidden\" value=\"hiddenValue\">" +
 "  <input name=\"buttonElement\" type=\"button\" value=\"Button\">" +
 "  <input name=\"fileElement\" type=\"file\">" +
 "  <textarea name=\"textareaElement\">Textarea</textarea>" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue2\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue3\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue4\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue5\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue6\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue7\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue8\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue9\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue10\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue11\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue12\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue13\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue14\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue15\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue16\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue17\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue18\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue19\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue20\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue21\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue22\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue23\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue24\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue25\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue26\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue27\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue28\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue29\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue30\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue31\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue32\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue33\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue34\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue35\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue36\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue37\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue38\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue39\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue40\">" +
 "  <input name=\"multipleElement[]\" type=\"text\" value=\"multipleValue41\">" +
 "</form>";

 Parser parser = new Parser.Builder().settings(new ParseSetting.Builder().build()).getDocumentParser();
 formElement = parser.parseBodyFragment(html).selectFirst("form");
 }

 @Test
 public void testFormElementConstruction() {
 assertNotNull(formElement);
 assertEquals("form", formElement.tagName());
 }

 @Test
 public void testElementsMethod() {
 Elements elements = formElement.elements();
 assertEquals(19, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 Element newElement = new Element("div", new Attributes().put("id", "newDiv"));
 formElement.addElement(newElement);
 Elements elements = formElement.elements();
 assertEquals(20, elements.size());
 assertEquals("div", elements.get(19).tagName());
 }

 @Test
 public void testSubmitMethod() throws Exception {
 connection = formElement.submit();
 assertNotNull(connection);
 assertEquals("GET", connection.method().name());
 assertEquals("http://example.com", connection.url().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodNoActionUrl() {
 formElement = new Element("form", new Attributes().put("id", "formWithoutAction"));
 formElement.submit();
 }

 @Test
 public void testFormDataMethod() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(15, formData.size());

 String[] expectedKeys = {"username", "password", "submit", "selectElement", "checkboxElement", "radioElement",
 "disabledElement", "hiddenElement", "buttonElement", "fileElement", "textareaElement", "multipleElement[]",
 "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]",
 "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]",
 "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]",
 "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]",
 "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]",
 "multipleElement[]", "multipleElement[]", "multipleElement[]", "multipleElement[]"};

 for (int i = 0; i < formData.size(); i++) {
 String key = formData.get(i).key();
 assertEquals(expectedKeys[i], key);
 }
 }
}