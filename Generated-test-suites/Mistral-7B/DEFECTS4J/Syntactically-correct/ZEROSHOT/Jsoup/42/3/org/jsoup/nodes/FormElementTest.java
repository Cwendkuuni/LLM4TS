import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes().put("id", "testForm")), "", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "testInput"));
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "testInput"));
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 }

 @Test
 public void testSubmitWithNoAction() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithAction() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes().put("action", "/test")), "", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "testInput"));
 formElement.addElement(inputElement);
 formElement.addElement(new Element("input", new Attributes().put("type", "text").put("name", "testInput2")));
 formElement.addElement(new Element("input", new Attributes().put("type", "submit").put("value", "Submit Form")));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 Connection connection = formElement.submit();
 // verify connection setup
 }

 @Test
 public void testFormDataForSelect() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes().put("id", "testForm")), "", new Attributes());
 Element selectElement = new Element("select", new Attributes().put("name", "testSelect"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "Option 1"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "Option 2").put("selected", true));
 selectElement.addChildren(optionElement1, optionElement2);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("Option 2", formData.get(0).val());
 }

 @Test
 public void testFormDataForCheckboxAndRadio() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 Element inputElement1 = new Element("input", new Attributes().put("type", "checkbox").put("name", "testCheckbox").put("checked", true));
 Element inputElement2 = new Element("input", new Attributes().put("type", "radio").put("name", "testRadio").put("checked", true));
 formElement.addElement(inputElement1);
 formElement.addElement(inputElement2);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 }

 @Test
 public void testFormDataWithoutCheckedCheckboxOrRadio() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 Element inputElement1 = new Element("input", new Attributes().put("type", "checkbox").put("name", "testCheckbox"));
 Element inputElement2 = new Element("input", new Attributes().put("type", "radio").put("name", "testRadio"));
 formElement.addElement(inputElement1);
 formElement.addElement(inputElement2);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormEquals() {
 FormElement formElement1 = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 assertFalse(formElement1.equals(formElement2));
 FormElement formElement3 = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 formElement3.addElement(new Element("input", new Attributes().put("type", "text").put("name", "testInput")));
 assertTrue(formElement1.equals(formElement3));
 }
}