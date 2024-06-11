import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.net.ssl.*"})
public class FormElementTest {

 private Element formElement;
 private Tag tag;
 private Attributes attributes;
 private String baseUri;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 tag = mock(Tag.class);
 attributes = mock(Attributes.class);
 baseUri = "http://localhost:8080";
 formElement = new FormElement(tag, baseUri, attributes);
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 assertEquals(tag.tagName(), formElement.tagName());
 assertEquals(baseUri, formElement.baseUri());
 }

 @Test
 public void testGetElements() {
 List<Element> elements = new ArrayList<>();
 elements.add(mock(Element.class));
 when(tag.children()).thenReturn(elements.iterator());
 assertEquals(elements, formElement.elements());
 }

 @Test
 public void testAddElement() {
 Element element = mock(Element.class);
 FormElement formElement = new FormElement(tag, baseUri, attributes);
 formElement.addElement(element);
 assertEquals(formElement, formElement.addElement(element));
 assertEquals(element, formElement.elements().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWhenNoAction() {
 PowerMockito.doThrow(new IllegalArgumentException("Could not determine a form action URL for submit."))
 .when(formElement, "hasAttr", "action");
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test
 public void testSubmit() {
 String action = "http://localhost:8080/submit";
 when(tag.attr("action")).thenReturn(action);
 Connection connection = formElement.submit();
 assertNotNull(connection);
 assertEquals(Connection.Method.POST, connection.method());
 }

 @Test
 public void testFormData() {
 Element inputElement = mock(Element.class);
 Element selectElement = mock(Element.class);
 when(inputElement.attr("name")).thenReturn("inputName");
 when(inputElement.attr("type")).thenReturn("text");
 when(inputElement.val()).thenReturn("inputValue");
 when(selectElement.attr("name")).thenReturn("selectName");
 when(selectElement.attr("type")).thenReturn("select-one");
 when(selectElement.val()).thenReturn("selectOptionValue");
 when(selectElement.select("option[selected]")).thenReturn(new ArrayList<Element>().iterator());
 when(selectElement.select("option")).thenReturn(new ArrayList<Element>().iterator());
 when(tag.children()).thenReturn(new ArrayList<Element>().iterator());
 when(tag.children()).thenReturn(new ArrayList<Element>().iterator()).thenReturn(inputElement).thenReturn(selectElement);

 List<Connection.KeyVal> expected = new ArrayList<>();
 expected.add(Connection.KeyVal.create("inputName", "inputValue"));
 expected.add(Connection.KeyVal.create("selectName", "selectOptionValue"));
 List<Connection.KeyVal> actual = formElement.formData();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormDataEmpty() {
 when(tag.children()).thenReturn(new ArrayList<Element>().iterator());
 List<Connection.KeyVal> actual = formElement.formData();
 assertNull(actual);
 }
}