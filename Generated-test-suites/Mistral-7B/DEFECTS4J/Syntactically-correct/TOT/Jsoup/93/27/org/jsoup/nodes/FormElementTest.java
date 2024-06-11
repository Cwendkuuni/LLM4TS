import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html><body><form action='https://example.com' method='post'><input type='text' name='name'><input type='text' name='age' disabled><input type='submit' name='submit'></form></body></html>";
 formElement = Jsoup.parse(html).body().getElementsByTag("form").get(0).getElementsByTag("form").get(0);
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 assertEquals("form", formElement.tagName());
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 Element inputElement = new Element("input");
 inputElement.attr("type", "text");
 inputElement.attr("name", "newElement");
 formElement.addElement(inputElement);
 Elements elementsAfterAdd = formElement.elements();
 assertEquals(4, elementsAfterAdd.size());
 }

 @Test
 public void testRemoveChild() {
 Element inputElementToRemove = formElement.select("input[name='name']").get(0);
 formElement.removeChild(inputElementToRemove);
 Elements elementsAfterRemove = formElement.elements();
 assertEquals(2, elementsAfterRemove.size());
 }

 @Test
 public void testSubmit() {
 List<Connection.KeyVal> formData = formElement.submit().data();
 assertEquals(3, formData.size());
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElementWithSelect = new FormElement(new Tag("form"), null, new Attributes());
 Element selectElement = new Element("select");
 selectElement.attr("name", "selectElement");
 Element optionElement1 = new Element("option");
 optionElement1.val("Option 1");
 optionElement1.text("Option 1");
 selectElement.add(optionElement1);
 formElementWithSelect.add(selectElement);
 List<Connection.KeyVal> formData = formElementWithSelect.submit().data();
 assertEquals(1, formData.size());
 assertEquals("selectElement", formData.get(0).key());
 assertEquals("Option 1", formData.get(0).value());
 }

 @Test
 public void testFormDataWithCheckboxElement() {
 FormElement formElementWithCheckbox = new FormElement(new Tag("form"), null, new Attributes());
 Element checkboxElement = new Element("input");
 checkboxElement.attr("type", "checkbox");
 checkboxElement.attr("name", "checkboxElement");
 checkboxElement.attr("checked", "checked");
 formElementWithCheckbox.add(checkboxElement);
 List<Connection.KeyVal> formData = formElementWithCheckbox.submit().data();
 assertEquals(1, formData.size());
 assertEquals("checkboxElement", formData.get(0).key());
 assertEquals("on", formData.get(0).value());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 FormElement formElementWithNoAction = new FormElement(new Tag("form"), null, new Attributes());
 List<Connection.KeyVal> formData = formElementWithNoAction.submit().data();
 }
}