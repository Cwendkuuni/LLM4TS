import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setup() {
 String html = "<html><body>" +
 " <form action='/' method='get'>" +
 " <input name='field1' type='text'>" +
 " <input name='field2' type='hidden' value='value2'>" +
 " <select name='field3'>" +
 " <option value='option1'>Option One</option>" +
 " <option value='option2' selected>Option Two</option>" +
 " </select>" +
 " <input name='field4' type='checkbox' checked>" +
 " <input name='field5' type='checkbox'>" +
 " <input type='submit' value='Submit'>" +
 " </form>" +
 " </body></html>";

 Document doc = Jsoup.parse(html);
 formElement = doc.body().selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 List<Element> elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 Element input = new Element("input");
 input.attr("type", "text");
 input.attr("name", "newField");
 FormElement formElementTest = new FormElement(Tag.FORM, "", new Attributes());
 FormElement formElementWithNewField = formElementTest.addElement(input);
 List<Element> elements = formElementWithNewField.elements();
 assertEquals(6, elements.size());
 assertEquals(input, elements.get(5));
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseUri() {
 FormElement formElementWithoutBaseUri = new FormElement(Tag.FORM, new Attributes(), new Attributes().put("action", ""));
 formElementWithoutBaseUri.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 FormElement formElementWithEmptyAction = new FormElement(Tag.FORM, new Attributes(), new Attributes()
 .put("action", "").put("method", "POST"));
 formElementWithEmptyAction.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidMethod() {
 FormElement formElementWithInvalidMethod = new FormElement(Tag.FORM, new Attributes(), new Attributes()
 .put("action", "").put("method", "PUT"));
 formElementWithInvalidMethod.submit();
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());
 Connection.KeyVal keyVal1 = formData.get(0);
 assertEquals("field1", keyVal1.key());
 assertEquals("", keyVal1.val());
 Connection.KeyVal keyVal2 = formData.get(1);
 assertEquals("field2", keyVal2.key());
 assertEquals("value2", keyVal2.val());
 Connection.KeyVal keyVal3 = formData.get(2);
 assertEquals("field3", keyVal3.key());
 assertEquals("option2", keyVal3.val());
 Connection.KeyVal keyVal4 = formData.get(3);
 assertEquals("field4", keyVal4.key());
 assertEquals("on", keyVal4.val());
 Connection.KeyVal keyVal5 = formData.get(4);
 assertEquals("field5", keyVal5.key());
 assertEquals("", keyVal5.val());
 }

 @Test
 public void testFormDataWithDisabledElement() {
 FormElement formElementWithDisabledElement = new FormElement(Tag.FORM, new Attributes(), new Attributes()
 .put("action", "").put("method", "POST"));
 Element disabledInput = new Element("input");
 disabledInput.attr("type", "text");
 disabledInput.attr("name", "disabledField");
 disabledInput.attr("disabled", "disabled");
 formElementWithDisabledElement.addElement(disabledInput);
 List<Connection.KeyVal> formData = formElementWithDisabledElement.formData();
 assertEquals(4, formData.size());
 assertFalse(formData.contains(hasKeyVal("disabledField")));
 }

 @Test
 public void testFormDataWithMixedElements() {
 Element textInput = new Element("input");
 textInput.attr("type", "text");
 textInput.attr("name", "mixedField");
 FormElement formElementWithMixedFields = new FormElement(Tag.FORM, new Attributes(), new Attributes()
 .put("action", "").put("method", "POST"));
 formElementWithMixedFields.addElement(textInput);
 Element selectElement = new Element("select");
 Element option1 = new Element("option");
 option1.val("option1");
 option1.text("Option One");
 Element option2 = new Element("option");
 option2.val("option2");
 option2.text("Option Two");
 option2.attr("selected", "selected");
 selectElement.add(option1);
 selectElement.add(option2);
 formElementWithMixedFields.addElement(selectElement);
 List<Connection.KeyVal> formData = formElementWithMixedFields.formData();
 assertEquals(6, formData.size());
 assertEquals("mixedField", formData.get(0).key());
 assertEquals("", formData.get(0).val());
 assertEquals("field3", formData.get(1).key());
 assertEquals("option2", formData.get(1).val());
 }
}