package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FormElement.class)
public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", Mockito.mock(Attributes.class));
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", Mockito.mock(Attributes.class));
 List<Element> elements = new ArrayList<>();
 elements.add(Mockito.mock(Element.class));
 PowerMockito.when(formElement.elements()).thenReturn(elements);
 List<Element> returnedElements = formElement.elements();
 assertEquals(elements, returnedElements);
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", Mockito.mock(Attributes.class));
 Element element = Mockito.mock(Element.class);
 formElement.addElement(element);
 PowerMockito.verifyNew(Elements.class).withArguments();
 PowerMockito.verify(formElement.elements()).add(element);
 }

 @Test
 public void testRemoveChild() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", Mockito.mock(Attributes.class));
 Node nodeToRemove = Mockito.mock(Node.class);
 formElement.removeChild(nodeToRemove);
 PowerMockito.verify(formElement).removeChild(nodeToRemove);
 }

 @Test
 public void testSubmitWithBaseUri() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "http://baseUri.com", Mockito.mock(Attributes.class));
 Connection connection = formElement.submit();
 String expectedAction = "http://baseUri.com/somepage";
 PowerMockito.when(formElement.absUrl("action")).thenReturn(expectedAction);
 assertEquals(expectedAction, connection.url().absolutePath());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithMissingBaseUri() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", Mockito.mock(Attributes.class));
 formElement.submit();
 }

 @Test
 public void testFormData() {
 FormElement formElement = Mockito.spy(new FormElement(Mockito.mock(Tag.class), "", Mockito.mock(Attributes.class)) {
 @Override
 protected List<Element> getChildNodes() {
 return new ArrayList<>();
 }
 });
 Element inputElement = Mockito.mock(Element.class);
 Element selectElement = Mockito.mock(Element.class);
 Elements options = Mockito.mock(Elements.class);
 PowerMockito.when(selectElement.select("option[selected]")).thenReturn(options);
 PowerMockito.when(options.size()).thenReturn(1);
 PowerMockito.when(options.first().val()).thenReturn("OptionValue");
 PowerMockito.when(formElement.elements()).thenReturn(new Elements(inputElement, selectElement));
 PowerMockito.when(inputElement.tagName()).thenReturn("input");
 PowerMockito.when(inputElement.attr("name")).thenReturn("InputName");
 PowerMockito.when(inputElement.val()).thenReturn("InputValue");
 PowerMockito.when(selectElement.tagName()).thenReturn("select");
 PowerMockito.when(selectElement.attr("name")).thenReturn("SelectName");

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("InputName", "InputValue"));
 expectedData.add(HttpConnection.KeyVal.create("SelectName", "OptionValue"));
 assertEquals(expectedData, formData);
 }

 @Test
 public void testFormDataSelectNoOptionSelected() {
 FormElement formElement = Mockito.spy(new FormElement(Mockito.mock(Tag.class), "", Mockito.mock(Attributes.class)) {
 @Override
 protected List<Element> getChildNodes() {
 return new ArrayList<>();
 }
 });
 Element selectElement = Mockito.mock(Element.class);
 PowerMockito.when(formElement.elements()).thenReturn(new Elements(selectElement));
 PowerMockito.when(selectElement.tagName()).thenReturn("select");
 PowerMockito.when(formElement.formData()).thenCallRealMethod();
 assertNull(formElement.formData().get(0).getValue());
 }
}