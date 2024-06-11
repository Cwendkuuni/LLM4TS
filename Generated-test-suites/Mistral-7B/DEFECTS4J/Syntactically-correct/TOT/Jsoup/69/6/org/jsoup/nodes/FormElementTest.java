import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ElementFactory;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private static final String HTML_FORM_TEMPLATE = "<form><input type='text' name='textField'>" +
         "<input type='text' name='textField2' disabled>" +
         "<select name='selectField'><option value='option1'>Option 1</option>" +
         "<option value='option2' selected>Option 2</option></select>" +
         "<input type='checkbox' name='checkField' checked>" +
         "<input type='radio' name='radioField' checked>" +
         "<input type='hidden' name='hiddenField' value='hiddenValue'>" +
         "</form>";

 private Element formElement;

 @Before
 public void setUp() {
 Document document = Jsoup.parseBodyFragment(HTML_FORM_TEMPLATE);
 formElement = document.selectFirst("form");
 }

 @Test
 public void testElements() {
 List<Element> expectedElements = Arrays.asList(
         formElement.select("input[type=text]").get(0),
         formElement.select("input[type=text]").get(1),
         formElement.select("select").get(0),
         formElement.select("input[type=checkbox][checked]").get(0),
         formElement.select("input[type=radio][checked]").get(0),
         formElement.select("input[type=hidden]").get(0)
 );
 assertEquals("Unexpected number of form elements", expectedElements.size(), formElement.elements().size());
 for (Element element : formElement.elements()) {
 assertTrue("Form element not expected: " + element, expectedElements.contains(element));
 }
 }

 @Test
 public void testSubmit() throws Exception {
 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("textField", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("textField2", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("selectField", "option2"));
 expectedFormData.add(HttpConnection.KeyVal.create("checkField", "on"));
 expectedFormData.add(HttpConnection.KeyVal.create("radioField", "on"));
 expectedFormData.add(HttpConnection.KeyVal.create("hiddenField", "hiddenValue"));
 assertEquals("Unexpected form data", expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoBaseUri() {
 ((FormElement) formElement).submit();
 }
}