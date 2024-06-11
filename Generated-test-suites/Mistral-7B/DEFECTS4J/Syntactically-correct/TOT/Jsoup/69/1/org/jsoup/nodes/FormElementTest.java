import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html><body>\n" +
 "  <form action=\"/submit\" method=\"post\">\n" +
 "    <input type=\"text\" name=\"username\" value=\"John Doe\">\n" +
 "    <input type=\"email\" name=\"email\" value=\"john.doe@example.com\">\n" +
 "    <textarea name=\"message\">Hello World!</textarea>\n" +
 "    <select name=\"country\">\n" +
 "      <option value=\"USA\">United States</option>\n" +
 "      <option selected value=\"CA\">Canada</option>\n" +
 "    </select>\n" +
 "    <input type=\"checkbox\" name=\"terms\" value=\"accept\">Accept Terms\n" +
 "    <input type=\"submit\">Submit</form>\n" +
 "  </body>\n" +
 " </html>";
 formElement = Jsoup.parse(html).body().selectFirst(FormElement.class);
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 Element newElement = new Parser().parseBody("<input type=\"text\" name=\"password\" value=\"password\">");
 FormElement newFormElement = new FormElement(Tag.FORM, "", new Attributes());
 newFormElement.addElement(newElement);
 assertEquals(1, newFormElement.elements().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodNoActionAttribute() {
 FormElement formWithoutAction = new FormElement(Tag.FORM, "", new Attributes());
 formWithoutAction.submit();
 }

 @Test
 public void testSubmitMethodValidFormData() {
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.data();
 assertEquals(6, formData.size());

 assertEquals("John Doe", formData.get(0).getKey());
 assertEquals("John Doe", formData.get(0).getValue());
 assertEquals("john.doe@example.com", formData.get(1).getKey());
 assertEquals("john.doe@example.com", formData.get(1).getValue());
 assertEquals("Hello World!", formData.get(2).getKey());
 assertEquals("Hello World!", formData.get(2).getValue());
 assertEquals("CA", formData.get(3).getKey());
 assertEquals("Canada", formData.get(3).getValue());
 assertEquals("accept", formData.get(4).getKey());
 assertEquals("accept", formData.get(4).getValue());
 assertEquals("", formData.get(5).getKey());
 assertEquals("", formData.get(5).getValue());
 }

 @Test
 public void testSubmitMethodEmptyFormData() {
 FormElement emptyFormElement = new FormElement(Tag.FORM, "", new Attributes());
 Connection connection = emptyFormElement.submit();
 List<Connection.KeyVal> formData = connection.data();
 assertEquals(0, formData.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodNoNameAttribute() {
 Element inputElement = new Parser().parseBody("<input type=\"text\" value=\"value\">");
 FormElement formElementWithEmptyNameInput = new FormElement(Tag.FORM, "", new Attributes());
 formElementWithEmptyNameInput.addElement(inputElement);
 formElementWithEmptyNameInput.submit();
 }

 @Test
 public void testSubmitMethodMultipleInputsWithSameName() {
 String html = "<html><body>\n" +
 "  <form action=\"/submit\" method=\"post\">\n" +
 "    <input type=\"text\" name=\"username\" value=\"John Doe\">\n" +
 "    <input type=\"text\" name=\"username\" value=\"Jane Doe\">\n" +
 "    <input type=\"submit\">Submit</form>\n" +
 "  </body>\n" +
 " </html>";
 FormElement formElementWithMultipleInputs = Jsoup.parse(html).body().selectFirst(FormElement.class);
 List<Connection.KeyVal> formData = formElementWithMultipleInputs.submit().data();
 assertEquals(3, formData.size());
 assertEquals("John Doe", formData.get(0).getKey());
 assertEquals("John Doe", formData.get(0).getValue());
 assertEquals("Jane Doe", formData.get(1).getKey());
 assertEquals("Jane Doe", formData.get(1).getValue());
 assertEquals("", formData.get(2).getKey());
 assertEquals("", formData.get(2).getValue());
 }
}