import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private Element formElement;
 private Attributes attributes;
 private Tag tag;

 @Before
 public void setup() {
 tag = mock(Tag.class);
 attributes = mock(Attributes.class);
 when(tag.tagName()).thenReturn("form");
 when(tag.attr("name", (String) anyObject())).thenReturn(tag);
 when(tag.name()).thenReturn(tag.tagName());
 when(tag.baseUri()).thenReturn("http://example.com");
 when(tag.attributes()).thenReturn(attributes);
 formElement = new FormElement(tag, "http://example.com", attributes);
 }

 @Test
 public void testFormElementCreation() {
 assertEquals("form", formElement.tagName());
 assertNotNull(formElement.baseUri());
 assertNotNull(formElement.elements());
 }

 @Test
 public void testGetFormControls() {
 Element element1 = mock(Element.class);
 Element element2 = mock(Element.class);
 when(element1.tagName()).thenReturn("input");
 when(element2.tagName()).thenReturn("textarea");
 when(formElement.elements()).thenReturn(new Elements(element1, element2));

 List<Element> formControls = formElement.elements();
 assertEquals(2, formControls.size());
 assertEquals("input", formControls.get(0).tagName());
 assertEquals("textarea", formControls.get(1).tagName());
 }

 @Test
 public void testAddElement() {
 Element newElement = mock(Element.class);
 formElement.addElement(newElement);
 List<Element> formControls = formElement.elements();
 assertEquals(1, formControls.size());
 assertEquals(newElement, formControls.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 formElement = new FormElement(tag, null, attributes);
 formElement.submit();
 }

 @Test
 public void testFormData() {
 Element inputElement1 = mock(Element.class);
 Element inputElement2 = mock(Element.class);
 Element selectElement = mock(Element.class);
 Element checkboxElement = mock(Element.class);
 when(inputElement1.attr("name")).thenReturn("name1");
 when(inputElement1.val()).thenReturn("value1");
 when(inputElement2.attr("name")).thenReturn("name2");
 when(inputElement2.val()).thenReturn("value2");
 when(selectElement.tagName()).thenReturn("select");
 when(selectElement.val()).thenReturn("option3");
 when(selectElement.attr("name")).thenReturn("name3");
 when(checkboxElement.attr("name")).thenReturn("name4");
 when(checkboxElement.hasAttr("checked")).thenReturn(true);
 when(checkboxElement.val()).thenReturn("value4");
 when(formElement.elements()).thenReturn(new Elements(inputElement1, inputElement2, selectElement, checkboxElement));

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 assertEquals("name1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("name2", formData.get(1).key);
 assertEquals("value2", formData.get(1).value);
 assertEquals("name3", formData.get(2).key);
 assertEquals("option3", formData.get(2).value);
 assertEquals("name4", formData.get(3).key);
 assertEquals("value4", formData.get(3).value);
 }

 @Test
 public void testSubmit() {
 Connection connection = mock(Connection.class);
 when(formElement.submit()).thenReturn(connection);
 formElement.submit();
 assertEquals(formElement, connection.request());
 }
}