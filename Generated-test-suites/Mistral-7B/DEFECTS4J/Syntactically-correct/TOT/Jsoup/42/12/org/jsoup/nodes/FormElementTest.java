import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.parser.Parser;
import org.jsoup.select.ElementsFilter;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Assertions.assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = createFormElementWithControls();
 Elements elements = formElement.elements();
 Assertions.assertNotNull(elements);
 Assertions.assertEquals(2, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "inputName"));
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 Assertions.assertEquals(1, elements.size());
 Assertions.assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testSubmitWithValidFormData() {
 FormElement formElement = createFormElementWithControls();
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();

 // Validate the submitted connection data matches the form data
 Assertions.assertEquals(3, formData.size());
 Assertions.assertEquals("inputName1", formData.get(0).key);
 Assertions.assertEquals("inputValue1", formData.get(0).value);
 Assertions.assertEquals("selectName", formData.get(1).key);
 Assertions.assertEquals("selectValue1", formData.get(1).value);
 Assertions.assertEquals("textAreaName", formData.get(2).key);
 Assertions.assertEquals("textAreaValue", formData.get(2).value);

 // Validate the submitted connection
 Assertions.assertEquals("GET", connection.method().name());
 Assertions.assertEquals("baseUri/path", connection.url().toString());
 }

 @Test
 public void testSubmitWithNoActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), new Attributes());
 Assertions.assertThrows(IllegalArgumentException.class, formElement::submit);
 }

 @Test
 public void testSubmitWithEmptyFormData() {
 FormElement formElement = new FormElement(new Tag("form"), new Attributes(), new Elements());
 Assertions.assertThrows(IllegalArgumentException.class, formElement::submit);
 }

 @Test
 public void testFormDataForSelectElementWithSingleSelectedOption() {
 FormElement formElement = createFormElementWithSelectElement();
 List<Connection.KeyVal> formData = formElement.formData();
 Assertions.assertEquals(1, formData.size());
 Assertions.assertEquals("selectName", formData.get(0).key);
 Assertions.assertEquals("selectValue1", formData.get(0).value);
 }

 @Test
 public void testFormDataForSelectElementWithMultipleSelectedOptions() {
 FormElement formElement = createFormElementWithSelectElementMultipleSelectedOptions();
 List<Connection.KeyVal> formData = formElement.formData();
 Assertions.assertEquals(2, formData.size());
 Assertions.assertEquals("selectName", formData.get(0).key);
 Assertions.assertEquals("selectValue1", formData.get(0).value);
 Assertions.assertEquals("selectName", formData.get(1).key);
 Assertions.assertEquals("selectValue2", formData.get(1).value);
 }

 @Test
 public void testFormDataForCheckboxOrRadioElementWithCheckedState() {
 FormElement formElement = createFormElementWithCheckboxOrRadioElementChecked();
 List<Connection.KeyVal> formData = formElement.formData();
 Assertions.assertEquals(1, formData.size());
 Assertions.assertEquals("checkboxOrRadioName", formData.get(0).key);
 Assertions.assertEquals("checkboxOrRadioValue", formData.get(0).value);
 }

 @Test
 public void testFormDataForCheckboxOrRadioElementWithoutCheckedState() {
 FormElement formElement = createFormElementWithCheckboxOrRadioElement();
 List<Connection.KeyVal> formData = formElement.formData();
 Assertions.assertEquals(0, formData.size());
 }

 private FormElement createFormElementWithControls() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element inputElement1 = new Element("input", new Attributes().put("type", "text").put("name", "inputName1"));
 formElement.addElement(inputElement1);
 Element selectElement = new Element("select", new Attributes().put("name", "selectName"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "selectValue1"));
 selectElement.addElement(optionElement1);
 formElement.addElement(selectElement);
 Element textAreaElement = new Element("textarea", new Attributes().put("name", "textAreaName"));
 formElement.addElement(textAreaElement);
 formElement.addElement(new Element("input", new Attributes().put("type", "submit")));
 return formElement;
 }

 private FormElement createFormElementWithSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), new Attributes());
 Element selectElement = new Element("select", new Attributes().put("name", "selectName"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "selectValue1").put("selected", "selected"));
 selectElement.addElement(optionElement1);
 formElement.addElement(selectElement);
 formElement.addElement(new Element("input", new Attributes().put("type", "submit")));
 return formElement;
 }

 private FormElement createFormElementWithSelectElementMultipleSelectedOptions() {
 FormElement formElement = new FormElement(new Tag("form"), new Attributes());
 Element selectElement = new Element("select", new Attributes().put("name", "selectName"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "selectValue1").put("selected", "selected"));
 selectElement.addElement(optionElement1);
 Element optionElement2 = new Element("option", new Attributes().put("value", "selectValue2").put("selected", "selected"));
 selectElement.addElement(optionElement2);
 formElement.addElement(selectElement);
 formElement.addElement(new Element("input", new Attributes().put("type", "submit")));
 return formElement;
 }

 private FormElement createFormElementWithCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(new Tag("form"), new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxOrRadioName").put("value", "checkboxOrRadioValue"));
 formElement.addElement(inputElement);
 formElement.addElement(new Element("input", new Attributes().put("type", "submit")));
 return formElement;
 }

 private FormElement createFormElementWithCheckboxOrRadioElementChecked() {
 FormElement formElement = new FormElement(new Tag("form"), new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxOrRadioName").put("value", "checkboxOrRadioValue").put("checked", "checked"));
 formElement.addElement(inputElement);
 formElement.addElement(new Element("input", new Attributes().put("type", "submit")));
 return formElement;
 }
}