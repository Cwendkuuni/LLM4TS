import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 " <form action=\"http://example.com/form\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" value=\"John\">" +
 " <input type=\"password\" name=\"password\" value=\"password123\">" +
 " <select name=\"gender\">" +
 " <option value=\"male\" selected>Male</option>" +
 " <option value=\"female\">Female</option>" +
 " </select>" +
 " <input type=\"submit\" value=\"Submit\">" +
 " </form>" +
 "</body></html>";
 formElement = Jsoup.parse(html).body().getElementsByTag("form").first().getElementsByTag("form").first();
 }

 @Test
 public void testFormElementCreation() {
 // Test that a new FormElement instance is created correctly
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethodReturnsCorrectly() {
 // Test that the elements() method returns the expected list of form control elements
 List<Element> expectedElements = new ArrayList<>();
 expectedElements.add(formElement.getElementsByTag("input").get(0));
 expectedElements.add(formElement.getElementsByTag("select").get(0));
 List<Element> actualElements = formElement.elements().toList();
 assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testAddElementMethod() {
 // Test that the addElement() method correctly adds a new form control element to the FormElement
 Element newElement = new Element("input", new Attributes().put("type", "hidden").put("name", "newElement").put("value", "test"));
 formElement.addElement(newElement);
 List<Element> actualElements = formElement.elements().toList();
 List<Element> expectedElements = new ArrayList<>();
 expectedElements.add(newElement);
 expectedElements.addAll(formElement.elements().toList());
 assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testRemoveChildMethod() {
 // Test that the removeChild() method correctly removes a child node from the FormElement and its Elements list
 Element childElementToRemove = formElement.getElementsByTag("input").get(0);
 formElement.removeChild(childElementToRemove);
 List<Element> actualElements = formElement.elements().toList();
 assertFalse(actualElements.contains(childElementToRemove));
 }

 @Test
 public void testSubmitMethodThrowsIllegalArgumentExceptionWithoutActionAttribute() {
 // Test that the submit() method throws an IllegalArgumentException when called on a FormElement without an "action" attribute
 try {
 FormElement formElementWithoutAction = new FormElement(new Tag("form"), "", new Attributes());
 formElementWithoutAction.submit();
 fail("Expected an IllegalArgumentException to be thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSubmitMethodReturnsCorrectConnectionObject() {
 // Test that the submit() method returns a Connection object with the correct request data
 Connection connection = formElement.submit();
 Map<String, String> expectedRequestData = new HashMap<>();
 expectedRequestData.put("username", "John");
 expectedRequestData.put("password", "password123");
 expectedRequestData.put("gender", "male");
 List<Connection.KeyVal> actualRequestData = formElement.formData();
 assertEquals(expectedRequestData, connection.data());
 }

 @Test
 public void testSubmitMethodThrowsIllegalArgumentExceptionWithEmptyActionAttribute() {
 // Test that the submit() method throws an IllegalArgumentException when called on a FormElement with an empty "action" attribute
 try {
 FormElement formElementWithEmptyAction = new FormElement(new Tag("form"), "", new Attributes().put("action", ""));
 formElementWithEmptyAction.submit();
 fail("Expected an IllegalArgumentException to be thrown.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}