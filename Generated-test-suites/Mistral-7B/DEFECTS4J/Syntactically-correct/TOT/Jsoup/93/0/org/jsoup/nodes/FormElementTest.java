import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Assert.assertNotNull(formElement);
 }

 @Test
 public void testFormElementElements() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testFormElementRemoveChild() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 formElement.addElement(inputElement);
 formElement.removeChild(inputElement);
 Elements elements = formElement.elements();
 Assert.assertEquals(0, elements.size());
 }

 @Test
 public void testFormElementSubmitValid() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes().put("action", "/submit"));
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "username").put("value", "testUser"));
 formElement.addElement(inputElement);
 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitInvalidBaseUri() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/submit"));
 formElement.submit();
 }

 @Test
 public void testFormElementFormDataValid() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element inputElementText = new Element("input", new Attributes().put("type", "text").put("name", "username").put("value", "testUser"));
 formElement.addElement(inputElementText);
 Element inputElementCheckbox = new Element("input", new Attributes().put("type", "checkbox").put("name", "agreeTerms").put("checked", "checked"));
 formElement.addElement(inputElementCheckbox);
 Element selectElement = new Element("select", new Attributes().put("name", "selectElement"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "option2"));
 selectElement.addElement(optionElement1);
 selectElement.addElement(optionElement2);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(4, formData.size());
 Assert.assertEquals("username", formData.get(0).key);
 Assert.assertEquals("testUser", formData.get(0).val);
 Assert.assertEquals("agreeTerms", formData.get(1).key);
 Assert.assertEquals("on", formData.get(1).val);
 Assert.assertEquals("selectElement", formData.get(2).key);
 Assert.assertEquals("option1", formData.get(3).val);
 }

 @Test
 public void testFormElementFormDataEmptySelect() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element inputElementText = new Element("input", new Attributes().put("type", "text").put("name", "username").put("value", "testUser"));
 formElement.addElement(inputElementText);
 Element selectElement = new Element("select", new Attributes().put("name", "selectElement"));
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Assert.assertEquals("username", formData.get(0).key);
 Assert.assertEquals("testUser", formData.get(0).val);
 }

 @Test
 public void testFormElementFormDataEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }
}