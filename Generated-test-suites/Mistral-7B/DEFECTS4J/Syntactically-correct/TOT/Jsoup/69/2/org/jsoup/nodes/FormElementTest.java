import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private FormElement formElement;
 private Element formControlElement1;
 private Element formControlElement2;
 private Connection connection;

 @Before
 public void setUp() {
 formControlElement1 = mock(Element.class);
 formControlElement2 = mock(Element.class);
 formElement = new FormElement(new Tag("form"), "", new Attributes());

 when(formControlElement1.attr("name")).thenReturn("name1");
 when(formControlElement1.tag().isFormSubmittable()).thenReturn(true);
 when(formControlElement1.val()).thenReturn("value1");
 when(formControlElement2.attr("name")).thenReturn("name2");
 when(formControlElement2.tag().isFormSubmittable()).thenReturn(false);
 when(formControlElement2.val()).thenReturn("value2");

 formElement.addElement(formControlElement1);
 formElement.addElement(formControlElement2);
 connection = mock(Connection.class);
 }

 @Test
 public void testElements() {
 assertEquals(2, formElement.elements().size());
 assertNotNull(formElement.elements().get(0));
 assertNotNull(formElement.elements().get(1));
 }

 @Test
 public void testSubmitWithValidFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("name1", formData.get(0).key);
 assertEquals("value1", formData.get(0).val);
 assertEquals("name2", formData.get(1).key);
 assertNull(formData.get(1).val);

 Connection connection = Jsoup.connect("http://test.com")
 .data(formData)
 .method(Connection.Method.POST)
 .execute();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWhenFormActionUrlCannotBeDetermined() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.addElement(formControlElement1);
 when(formElement.baseUri()).thenReturn(null);
 formElement.submit();
 }
}