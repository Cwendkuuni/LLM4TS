import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Element formElement;
 private Element inputElement;
 private Element selectElement;
 private Element checkboxElement;

 @Before
 public void setup() {
 Parser parser = new Parser.MalformedEncodingDetecting();
 Tag formTag = new Tag("form");
 Tag inputTag = new Tag("input");
 Tag selectTag = new Tag("select");
 Tag optionTag1 = new Tag("option");
 Tag optionTag2 = new Tag("option");
 Tag checkboxTag = new Tag("input");
 checkboxTag.attr("type", "checkbox");

 formTag.attr("action", "http://example.com");
 formTag.attr("method", "get");
 formElement = new FormElement(formTag, null, new Attributes());

 inputElement = new Element("input");
 inputElement.attr("name", "inputName");
 inputElement.attr("type", "text");
 inputElement.attr("value", "inputValue");
 formElement.addElement(inputElement);

 selectElement = new Element("select");
 selectElement.attr("name", "selectName");
 formElement.addElement(selectElement);
 optionTag1.text("option1");
 optionTag1.attr("value", "option1Value");
 optionTag2.text("option2");
 optionTag2.attr("value", "option2Value");
 selectElement.add(optionTag1);
 selectElement.add(optionTag2);
 selectElement.attr("selectedIndex", "0");

 checkboxElement = formElement.addElement(checkboxElement);
 checkboxElement.attr("name", "checkboxName");
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 assertEquals("form", formElement.tagName());
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 assertFalse(elements.isEmpty());
 }

 @Test
 public void testAddElement() {
 FormElement newFormElement = formElement.addElement(new Element("div"));
 assertEquals(4, formElement.elements().size());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutAction() {
 FormElement formWithoutAction = new FormElement(new Tag("form"), null, new Attributes());
 try {
 formWithoutAction.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());

 assertEquals("inputName", formData.get(0).key);
 assertEquals("inputValue", formData.get(0).val);

 assertEquals("selectName", formData.get(1).key);
 assertEquals("option1Value", formData.get(1).val);

 assertEquals("checkboxName", formData.get(2).key);
 assertNull(formData.get(2).val);

 assertEquals("selectName", formData.get(3).key);
 assertEquals("option1Value", formData.get(3).val);
 }
}