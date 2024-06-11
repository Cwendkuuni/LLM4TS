import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private static final String HTML = "" +
         "<html>" +
         "<body>" +
         "<form method='get' action='/'> " +
         "<input type='text' name='username' value='test'>" +
         "<select name='select'>" +
         "<option value='option1' selected>Option 1</option>" +
         "<option value='option2'>Option 2</option>" +
         "</select>" +
         "<input type='checkbox' name='checkbox' checked>" +
         "<input type='radio' name='radio' checked>" +
         "<input type='submit' value='Submit'>" +
         "</form>" +
         "</body>" +
         "</html>";

 private Element formElement;

 @Before
 public void setUp() {
 Parser parser = new Parser.Builder().addSetting(ParseSetting.OBEF).build();
 formElement = parser.parse(HTML).body().selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertEquals(4, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 Element input = new Elements(new Tag("input", new Attributes().put("name", "test"))).first();
 formElement.addElement(input);
 assertEquals(1, formElement.elements().size());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertEquals(Connection.Method.GET, connection.method());
 assertEquals("/", connection.url().toString());
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>() {{
 add(Connection.KeyVal.create("username", "test"));
 add(Connection.KeyVal.create("select", "option1"));
 add(Connection.KeyVal.create("checkbox", ""));
 add(Connection.KeyVal.create("radio", ""));
 }};
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(Tag.FORM, "", new Attributes());
 FormElement formElement2 = new FormElement(Tag.FORM, "", new Attributes());
 assertNotEquals(formElement1, formElement2);
 FormElement formElement3 = new FormElement(Tag.FORM, "", new Attributes());
 formElement3.elements().add(formElement.elements().get(0));
 assertEquals(formElement1, formElement3);
 }
}