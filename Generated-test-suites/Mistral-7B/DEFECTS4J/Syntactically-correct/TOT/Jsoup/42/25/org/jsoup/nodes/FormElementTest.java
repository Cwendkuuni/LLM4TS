import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private FormElement form;

 @Before
 public void setUp() {
 String html = "<html><body><form action=\"/submit\" method=\"post\"><input type=\"text\" name=\"username\"><input type=\"password\" name=\"password\"><input type=\"submit\" name=\"submit\"></form></body></html>";
 form = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testElements() {
 Elements elements = form.elements();
 assertEquals(3, elements.size());
 assertNotNull(elements.get(0));
 assertNotNull(elements.get(1));
 assertNotNull(elements.get(2));
 }

 @Test
 public void testAddElement() {
 Element input = new Element("input");
 input.setAttribute("type", "text");
 input.setAttribute("name", "newInput");
 FormElement formWithNewElement = form.addElement(input);
 assertEquals(4, formWithNewElement.elements().size());
 }

 @Test
 public void testSubmitWithNoAction() {
 assertThrows(IllegalArgumentException.class, () -> form.submit());
 }

 @Test
 public void testSubmitWithEmptyFormData() {
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testSubmitWithValidFormData() {
 List<Connection.KeyVal> formData = form.formData();
 Map<String, String> expectedFormData = new HashMap<>();
 expectedFormData.put("username", "valueForUsername");
 expectedFormData.put("password", "valueForPassword");
 expectedFormData.put("submit", "");
 for (Connection.KeyVal kv : formData) {
 expectedFormData.put(kv.key(), kv.value());
 }
 assertEquals(expectedFormData, form.formData());
 }

 @Test
 public void testSubmitWithInvalidFormControls() {
 FormElement formWithInvalidControl = (FormElement) Jsoup.parse("<html><body><form action=\"/submit\" method=\"post\"><input type=\"not-a-form-control\"><input type=\"text\" name=\"username\"><input type=\"password\" name=\"password\"><input type=\"submit\" name=\"submit\"></form></body></html>").body().selectFirst("form");
 assertFalse(formWithInvalidControl.submit() instanceof Connection);
 }

 @Test
 public void testSubmitWithMissingNameAttribute() {
 Element inputWithoutName = new Element("input");
 FormElement formWithMissingNameInput = new FormElement(Tag.FORM, "", new Attributes());
 formWithMissingNameInput.addElement(inputWithoutName);
 assertThrows(IllegalArgumentException.class, () -> formWithMissingNameInput.submit());
 }
}