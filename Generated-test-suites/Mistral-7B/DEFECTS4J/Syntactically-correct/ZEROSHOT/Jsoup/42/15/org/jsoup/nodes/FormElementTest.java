import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 @Test
 public void testNewFormElement() {
 // Given
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Tag tag = new Tag("form");

 // When
 FormElement formElement = new FormElement(tag, baseUri, attributes);

 // Then
 assertInstanceof(FormElement.class, formElement);
 assertEquals("form", formElement.tagName());
 assertEquals(baseUri, formElement.baseUri());
 assertEquals(attributes, formElement.attributes());
 }

 @Test
 public void testGetElements() {
 // Given
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element element1 = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 Element element2 = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 formElement.addElement(element1);
 formElement.addElement(element2);

 // When
 Elements elements = formElement.elements();

 // Then
 assertEquals(2, elements.size());
 assertEquals(element1, elements.get(0));
 assertEquals(element2, elements.get(1));
 }

 @Test
 public void testSubmit() {
 // Given
 String baseUri = "http://example.com";
 String action = "http://example.com/submit";
 FormElement formElement = new FormElement(new Tag("form"), baseUri, new Attributes().put("action", action));
 Element inputElement = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 inputElement.attr("name", "name");
 inputElement.attr("type", "text");
 inputElement.attr("value", "value");
 formElement.addElement(inputElement);

 // When
 Connection connection = formElement.submit();

 // Then
 assertNotNull(connection);
 assertEquals(action, connection.url());
 assertEquals(Connection.Method.POST, connection.method());

 // Verify that form data is correct
 List<Connection.KeyVal> formData = connection.data();
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("value", formData.get(0).val());
 }

 @Test
 public void testFormDataEmptyForm() {
 // Given
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());

 // When
 List<Connection.KeyVal> formData = formElement.formData();

 // Then
 assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormDataSingleInput() {
 // Given
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element inputElement = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 inputElement.attr("name", "name");
 inputElement.attr("type", "text");
 inputElement.attr("value", "value");
 formElement.addElement(inputElement);

 // When
 List<Connection.KeyVal> formData = formElement.formData();

 // Then
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("value", formData.get(0).val());
 }

 @Test
 public void testFormDataMultipleInputs() {
 // Given
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element inputElement1 = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 inputElement1.attr("name", "name1");
 inputElement1.attr("type", "text");
 inputElement1.attr("value", "value1");
 formElement.addElement(inputElement1);

 Element inputElement2 = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 inputElement2.attr("name", "name2");
 inputElement2.attr("type", "text");
 inputElement2.attr("value", "value2");
 formElement.addElement(inputElement2);

 // When
 List<Connection.KeyVal> formData = formElement.formData();

 // Then
 assertEquals(2, formData.size());
 assertEquals("name1", formData.get(0).key());
 assertEquals("value1", formData.get(0).val());
 assertEquals("name2", formData.get(1).key());
 assertEquals("value2", formData.get(1).val());
 }

 @Test
 public void testFormDataSelectElement() {
 // Given
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element selectElement = new Element("select") {
 @Override
 public String tagName() {
 return "select";
 }
 };
 selectElement.attr("name", "name");
 selectElement.attr("id", "id");
 Element optionElement1 = new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 };
 optionElement1.attr("value", "value1");
 optionElement1.attr("selected", "selected");
 selectElement.add(optionElement1);
 Element optionElement2 = new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 };
 optionElement2.attr("value", "value2");
 selectElement.add(optionElement2);
 formElement.addElement(selectElement);

 // When
 List<Connection.KeyVal> formData = formElement.formData();

 // Then
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("value1", formData.get(0).val());
 }

 @Test
 public void testFormDataCheckboxElement() {
 // Given
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element checkboxElement = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 checkboxElement.attr("name", "name");
 checkboxElement.attr("type", "checkbox");
 formElement.addElement(checkboxElement);

 // When
 checkBoxElement.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();

 // Then
 assertTrue(formData.isEmpty());

 // When
 checkBoxElement.attr("checked", null);
 formData = formElement.formData();

 // Then
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("on", formData.get(0).val());
 }

 @Test
 public void testFormDataRadioElement() {
 // Given
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element radioButtonElement1 = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 radioButtonElement1.attr("name", "name");
 radioButtonElement1.attr("type", "radio");
 radioButtonElement1.attr("id", "id1");
 radioButtonElement1.attr("value", "value1");
 FormElement labelElement1 = new Element("label") {
 @Override
 public String tagName() {
 return "label";
 }
 };
 labelElement1.attr("for", "id1");
 labelElement1.text("Label 1");
 formElement.addElement(radioButtonElement1);
 formElement.addElement(labelElement1);

 Element radioButtonElement2 = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 radioButtonElement2.attr("name", "name");
 radioButtonElement2.attr("type", "radio");
 radioButtonElement2.attr("id", "id2");
 radioButtonElement2.attr("value", "value2");
 FormElement labelElement2 = new Element("label") {
 @Override
 public String tagName() {
 return "label";
 }
 };
 labelElement2.attr("for", "id2");
 labelElement2.text("Label 2");
 formElement.addElement(radioButtonElement2);
 formElement.addElement(labelElement2);

 // When
 radioButtonElement1.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();

 // Then
 assertTrue(formData.isEmpty());

 // When
 radioButtonElement2.attr("checked", "checked");
 formData = formElement.formData();

 // Then
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("value2", formData.get(0).val());
 }

 @Test
 public void testEquals() {
 // Given
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Tag tag1 = new Tag("form");
 Tag tag2 = new Tag("form");
 FormElement formElement1 = new FormElement(tag1, baseUri, attributes);
 FormElement formElement2 = new FormElement(tag2, baseUri, attributes);

 // When
 // Then
 assertFalse(formElement1.equals(formElement2));

 // Given
 formElement2 = new FormElement(tag1, baseUri, attributes);

 // When
 // Then
 assertTrue(formElement1.equals(formElement2));
 }
}