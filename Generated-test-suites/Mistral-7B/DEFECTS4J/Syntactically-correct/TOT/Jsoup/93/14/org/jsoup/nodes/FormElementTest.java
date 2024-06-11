import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

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

 private Element formElement;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 " <form action=\"/submit\" method=\"post\">" +
 " <input type=\"text\" name=\"name\" value=\"John\"/>" +
 " <input type=\"hidden\" name=\"hidden\" value=\"secret\"/>" +
 " <select name=\"select\">" +
 " <option value=\"option1\" selected>Option 1</option>" +
 " <option value=\"option2\">Option 2</option>" +
 " </select>" +
 " <input type=\"checkbox\" name=\"checkbox\" checked/>" +
 " <input type=\"radio\" name=\"radio\" checked/>" +
 " <input type=\"submit\" value=\"Submit\"/>" +
 " </form>" +
 " </body></html>";

 Document doc = Jsoup.parse(html);
 formElement = doc.select("form").first();
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 List<Element> expectedElements = new ArrayList<>(Arrays.asList(
 formElement.select("input[type!=submit]"),
 formElement.select("select"),
 formElement.select("input[type=hidden]")
 ));
 assertEquals(expectedElements, elements);
 }

 @Test
 public void testAddElement() {
 Element newElement = Parser.parseInto(formElement, "<input type=\"text\" name=\"newName\"/>");
 formElement.addElement(newElement);
 Elements elements = formElement.elements();
 List<Element> expectedElements = new ArrayList<>(Arrays.asList(
 formElement.select("input[type!=submit]"),
 formElement.select("select"),
 formElement.select("input[type=hidden]"),
 newElement
 ));
 assertEquals(expectedElements, elements);
 }

 @Test
 public void testSubmitWithValidFormData() {
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>(Arrays.asList(
 new Connection.KeyVal("name", "John"),
 new Connection.KeyVal("hidden", "secret"),
 new Connection.KeyVal("select", "option1"),
 new Connection.KeyVal("checkbox", "on"),
 new Connection.KeyVal("radio", "on")
 ));
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidFormData() {
 formElement.removeAttr("action");
 Connection connection = formElement.submit();
 }

 @Test
 public void testFormDataWithDisabledInput() {
 formElement.firstChild().attr("disabled", "disabled");
 List<Connection.KeyVal> formData = formElement.submit().requestBody();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 assertEquals(expectedFormData, formData);
 }
}