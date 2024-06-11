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

public class FormElementTest {

 private Element form;

 @Before
 public void setUp() {
 String html = "<html><body><form action=\"https://example.com\">" +
         "<input type=\"text\" name=\"name\" value=\"John\"/>" +
         "<select name=\"color\">" +
         "<option value=\"red\" selected=\"selected\">Red</option>" +
         "<option value=\"blue\">Blue</option>" +
         "</select>" +
         "<input type=\"checkbox\" name=\"agree\" checked=\"checked\" value=\"true\"/>" +
         "<input type=\"submit\"/>" +
         "</form></body></html>";
 form = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testFormElementConstruction() {
 Tag tag = form.tag();
 String baseUri = form.baseUri();
 Attributes attributes = form.attributes();
 FormElement formElement = new FormElement(tag, baseUri, attributes);
 assertEquals(tag.tagName(), formElement.tagName());
 assertEquals(baseUri, formElement.baseUri());
 assertEquals(attributes, formElement.attributes());
 }

 @Test
 public void testGetFormControls() {
 Elements formControls = form.elements();
 FormElement formElement = new FormElement(form.tag(), form.baseUri(), form.attributes());
 Elements expectedFormControls = form.select("input, select, textarea");
 assertEquals(expectedFormControls.size(), formElement.elements().size());
 for (Element control : expectedFormControls) {
 assertEquals(control, formElement.elements().get(formElement.elements().indexOf(control)));
 }
 }

 @Test
 public void testAddElement() {
 Element input = form.selectFirst("input");
 FormElement formElement = new FormElement(form.tag(), form.baseUri(), form.attributes());
 formElement.addElement(input);
 FormElement expectedFormElement = new FormElement(form.tag(), form.baseUri(), form.attributes());
 expectedFormElement.add(input);
 assertEquals(expectedFormElement, formElement);
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(new Connection.KeyVal("name", "John"));
 expectedFormData.add(new Connection.KeyVal("color", "red"));
 expectedFormData.add(new Connection.KeyVal("agree", "true"));
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 FormElement formElementWithoutAction = new FormElement(form.tag().cloneAttributes(), "", form.attributes());
 formElementWithoutAction.submit();
 }
}