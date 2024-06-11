package org.jsoup.nodes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 @Test
 public void testCreateFormElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Assertions.assertNotNull(formElement);
 }

 @Test
 public void testGetListOfFormControlElements() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element inputElement = new Elements(new Tag("input")).first();
 formElement.add(inputElement);
 Elements expectedElements = new Elements(inputElement);
 Elements actualElements = formElement.elements();
 Assertions.assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element inputElement = new Elements(new Tag("input")).first();
 formElement.add(inputElement);
 Elements expectedElements = new Elements(inputElement);
 Elements actualElements = formElement.elements();
 Assertions.assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testSubmitForm() {
 String html = "<form action='http://example.com' method='POST'>" +
 "  <input type='text' name='username'>" +
 "  <input type='password' name='password'>" +
 "  <input type='submit' name='submit'>" +
 "  <select name='select'>" +
 "    <option value='option1' selected></option>" +
 "    <option value='option2'></option>" +
 "  </select>" +
 "  <input type='checkbox' name='checkbox1' checked>" +
 "  <input type='checkbox' name='checkbox2'>" +
 "  <input type='radio' name='radio' value='value1' checked>" +
 "  <input type='radio' name='radio' value='value2'>" +
 "  <input type='hidden' name='hidden' value='hiddenValue'>" +
 " </form>";
 Parser parser = new Parser(ParseSettings.standardParserSettings());
 FormElement formElement = parser.parse(html).selectFirst("form");
 List<Connection.KeyVal> formData = formElement.formData();

 Connection connection = formElement.submit();
 List<String> keys = new ArrayList<>();
 keys.add("username");
 keys.add("password");
 keys.add("submit");
 keys.add("option1");
 keys.add("checkbox1");

 for (Connection.KeyVal keyVal : formData) {
 String key = keyVal.key();
 Assertions.assertTrue(keys.contains(key));
 keys.remove(keys.indexOf(key));
 }

 Assertions.assertEquals(0, keys.size());
 }

 @Test
 public void testFormDataForSelectElement() {
 String html = "<form action='http://example.com' method='POST'>" +
 "  <input type='text' name='username'>" +
 "  <select name='select'>" +
 "    <option value='option1' selected></option>" +
 "    <option value='option2'></option>" +
 "  </select>" +
 "  <input type='submit' name='submit'>" +
 " </form>";
 Parser parser = new Parser(ParseSettings.standardParserSettings());
 FormElement formElement = parser.parse(html).selectFirst("form");
 List<Connection.KeyVal> formData = formElement.formData();

 List<String> keys = new ArrayList<>();
 keys.add("username");
 keys.add("select");

 for (Connection.KeyVal keyVal : formData) {
 String key = keyVal.key();
 Assertions.assertTrue(keys.contains(key));
 keys.remove(keys.indexOf(key));
 }

 Assertions.assertEquals(1, keys.size());
 Assertions.assertEquals("select", keys.get(0));
 }

 @Test
 public void testFormDataForCheckboxElement() {
 String html = "<form action='http://example.com' method='POST'>" +
 "  <input type='text' name='username'>" +
 "  <input type='checkbox' name='checkbox1' checked>" +
 "  <input type='checkbox' name='checkbox2'>" +
 "  <input type='submit' name='submit'>" +
 " </form>";
 Parser parser = new Parser(ParseSettings.standardParserSettings());
 FormElement formElement = parser.parse(html).selectFirst("form");
 List<Connection.KeyVal> formData = formElement.formData();

 List<String> keys = new ArrayList<>();
 keys.add("username");
 keys.add("checkbox1");

 for (Connection.KeyVal keyVal : formData) {
 String key = keyVal.key();
 Assertions.assertTrue(keys.contains(key));
 keys.remove(keys.indexOf(key));
 }

 Assertions.assertEquals(1, keys.size());
 Assertions.assertEquals("checkbox2", keys.get(0));
 }

 @Test
 public void testFormDataForRadioElement() {
 String html = "<form action='http://example.com' method='POST'>" +
 "  <input type='text' name='username'>" +
 "  <input type='radio' name='radio' value='value1' checked>" +
 "  <input type='radio' name='radio' value='value2'>" +
 "  <input type='submit' name='submit'>" +
 " </form>";
 Parser parser = new Parser(ParseSettings.standardParserSettings());
 FormElement formElement = parser.parse(html).selectFirst("form");
 List<Connection.KeyVal> formData = formElement.formData();

 List<String> keys = new ArrayList<>();
 keys.add("username");
 keys.add("radio");

 for (Connection.KeyVal keyVal : formData) {
 String key = keyVal.key();
 Assertions.assertTrue(keys.contains(key));
 keys.remove(keys.indexOf(key));
 }

 Assertions.assertEquals(1, keys.size());
 Assertions.assertEquals("radio", keys.get(0));
 }
}