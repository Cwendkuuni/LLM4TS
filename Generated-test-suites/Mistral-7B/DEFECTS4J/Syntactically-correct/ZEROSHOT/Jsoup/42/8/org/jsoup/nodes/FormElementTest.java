import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html><head></head>" +
 "<body>" +
 "<form action='/' method='post'>" +
 "<input type='text' name='username'>" +
 "<select name='color'>" +
 "<option value='red' selected='selected'>Red</option>" +
 "<option value='green'>Green</option>" +
 "</select>" +
 "<input type='checkbox' name='agree' checked='checked'>" +
 "<input type='hidden' name='csrf' value='token'>" +
 "</form>" +
 "</body></html>";

 formElement = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testGetFormControls() {
 Elements expectedControls = Arrays.asList(
 formElement.select("input[type='text']"),
 formElement.select("select"),
 formElement.select("input[type='checkbox']"),
 formElement.select("input[type='hidden']")
 ).closest(FormElement.class);

 List<Element> actualControls = formElement.elements().stream().collect(Collectors.toList());

 assertEquals(expectedControls, actualControls);
 }

 @Test
 public void testSubmitForm() {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("username", ""),
 new Connection.KeyVal("color", "red"),
 new Connection.KeyVal("agree", ""),
 new Connection.KeyVal("csrf", "token")
 );

 Connection connection = formElement.submit();
 List<Connection.KeyVal> actualFormData = connection.requestBody();

 assertEquals(expectedFormData, actualFormData);
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitFormWithoutBaseUri() {
 FormElement formElementWithoutBaseUri = new FormElement(
 Tag.valueOf("<form action='/' method='post'>"),
 "",
 new Attributes()
 );

 formElementWithoutBaseUri.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithNoActionAttribute() {
 FormElement formElementWithNoActionAttribute = new FormElement(
 Tag.valueOf("<form method='post'>"),
 "",
 new Attributes()
 );

 formElementWithNoActionAttribute.submit();
 }

 @Test
 public void testFormElementHashCodeAndEquals() {
 FormElement formElement1 = new FormElement(
 Tag.valueOf("<form action='/' method='post'>"),
 "",
 new Attributes()
 );

 FormElement formElement2 = new FormElement(
 Tag.valueOf("<form action='/' method='post'>"),
 "",
 new Attributes()
 );

 FormElement formElement3 = new FormElement(
 Tag.valueOf("<form action='/' method='post'>"),
 "",
 new Attributes().put("someKey", "someValue")
 );

 FormElement formElement4 = new FormElement(
 Tag.valueOf("<form action='/' method='get'>"),
 "",
 new Attributes()
 );

 FormElement formElement5 = formElement1;

 assertEquals(formElement1.hashCode(), formElement1.hashCode());
 assertEquals(formElement1, formElement1);
 assertEquals(formElement1, formElement5);

 assertNotSame(formElement1, formElement2);
 assertNotSame(formElement1, formElement3);
 assertNotSame(formElement1, formElement4);
 }
}