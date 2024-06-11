import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private static ParseSetup setup;

 @BeforeClass
 public static void init() {
 setup = Jsoup.parseBodyFragment("<form action=\"http://example.com/\"><input type=\"text\" name=\"username\"><input type=\"submit\"></form>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = setup.selectFirst("form");
 assertNotNull(formElement);
 }

 @Test
 public void testGetElementsMethod() {
 FormElement formElement = setup.selectFirst("form");
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(0, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = setup.selectFirst("form");
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "password"));
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 }

 @Test
 public void testSubmitMethod() {
 FormElement formElement = setup.selectFirst("form");
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithoutActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 Connection connection = formElement.submit();
 }

 @Test
 public void testFormDataMethodWithTextInput() {
 FormElement formElement = setup.selectFirst("form");
 Element inputElement = formElement.selectFirst("input[name=username]");
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("", formData.get(0).value());
 }

 @Test
 public void testFormDataMethodWithSelectedOption() {
 FormElement formElement = setup.selectFirst("form");
 Element selectElement = formElement.selectFirst("select[name=country]");
 Element optionElement = selectElement.selectFirst("option[value=USA]");
 selectElement.attributes().put("multiple", "multiple");
 formElement.addElement(optionElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 assertEquals("country", formData.get(0).key());
 assertEquals("USA", formData.get(0).value());
 }

 @Test
 public void testFormDataMethodWithCheckboxInput() {
 FormElement formElement = setup.selectFirst("form");
 Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "agree").put("checked", "checked"));
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 assertEquals("agree", formData.get(0).key());
 assertEquals("on", formData.get(0).value());
 }
}