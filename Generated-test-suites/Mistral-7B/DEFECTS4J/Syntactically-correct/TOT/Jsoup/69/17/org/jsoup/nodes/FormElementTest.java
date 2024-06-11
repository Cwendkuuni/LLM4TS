import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private Element document;

 @Before
 public void setup() {
 Parser parser = new Parser.Builder().parseBodyElementsOnly(true).build();
 String html = "<html>\n" +
 "        <body>\n" +
 "            <form method=\"get\">\n" +
 "                <input type=\"text\" name=\"username\" value=\"John\">\n" +
 "                <input type=\"hidden\" name=\"password\" value=\"password\">\n" +
 "                <select name=\"color\">\n" +
 "                    <option value=\"red\">Red</option>\n" +
 "                    <option value=\"green\" selected>Green</option>\n" +
 "                </select>\n" +
 "                <input type=\"submit\" value=\"Submit\">\n" +
 "            </form>\n" +
 "        </body>\n" +
 "    </html>";
 document = Jsoup.parse(html, parser);
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 assertEquals(Tag.FORM, formElement.tag());
 assertNull(formElement.classes());
 assertNull(formElement.id());
 }

 @Test
 public void testGetElements() {
 Elements elements = document.select("form > input, form > select");
 FormElement formElement = (FormElement) elements.first();
 Elements formControls = formElement.elements();
 assertEquals(3, formControls.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "newInput"));
 formElement.addElement(inputElement);
 Elements formControls = formElement.elements();
 assertEquals(1, formControls.size());
 assertEquals(inputElement, formControls.first());
 }

 @Test
 public void testSubmitFormWithValidAction() {
 FormElement formElement = (FormElement) document.select("form").first();
 Connection connection = formElement.submit();
 assertEquals(Connection.Method.GET, connection.method());
 assertEquals("http://localhost/", connection.url().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithInvalidAction() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes().put("action", ""));
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithMissingBaseUri() {
 Parser parser = new Parser.Builder().parseBodyElementsOnly(true).build();
 String html = "<html>\n" +
 "        <body>\n" +
 "            <form method=\"get\">\n" +
 "                <input type=\"text\" name=\"username\" value=\"John\">\n" +
 "                <input type=\"hidden\" name=\"password\" value=\"password\">\n" +
 "            </form>\n" +
 "        </body>\n" +
 "    </html>";
 document = Jsoup.parse(html, parser);
 FormElement formElement = (FormElement) document.select("form").first();
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithMissingMethod() {
 Parser parser = new Parser.Builder().parseBodyElementsOnly(true).build();
 String html = "<html>\n" +
 "        <body>\n" +
 "            <form action=\"http://localhost/\">\n" +
 "                <input type=\"text\" name=\"username\" value=\"John\">\n" +
 "            </form>\n" +
 "        </body>\n" +
 "    </html>";
 document = Jsoup.parse(html, parser);
 FormElement formElement = (FormElement) document.select("form").first();
 formElement.submit();
 }

 @Test
 public void testFormDataWithTextInput() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> keyValues = formElement.formData();
 assertEquals(3, keyValues.size());
 assertEquals("John", keyValues.get(0).getValue());
 assertEquals("username", keyValues.get(0).getName());
 }

 @Test
 public void testFormDataWithHiddenInput() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> keyValues = formElement.formData();
 assertEquals(4, keyValues.size());
 assertEquals("password", keyValues.get(2).getName());
 assertEquals("password", keyValues.get(2).getValue());
 }

 @Test
 public void testFormDataWithSelect() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> keyValues = formElement.formData();
 assertEquals("green", keyValues.get(2).getValue());
 assertEquals("color", keyValues.get(2).getName());
 }

 @Test
 public void testFormDataWithCheckbox() {
 Parser parser = new Parser.Builder().parseBodyElementsOnly(true).build();
 String html = "<html>\n" +
 "        <body>\n" +
 "            <form method=\"get\">\n" +
 "                <input type=\"checkbox\" name=\"checkbox\" value=\"value\">\n" +
 "                <input type=\"submit\" value=\"Submit\">\n" +
 "            </form>\n" +
 "        </body>\n" +
 "    </html>";
 document = Jsoup.parse(html, parser);
 FormElement formElement = (FormElement) document.select("form").first();
 formElement.select("input:checked").first().attr("checked", "checked");
 List<Connection.KeyVal> keyValues = formElement.formData();
 assertEquals(2, keyValues.size());
 assertEquals("value", keyValues.get(0).getValue());
 assertEquals("checkbox", keyValues.get(0).getName());
 }

 @Test
 public void testFormDataWithDisabledInput() {
 Parser parser = new Parser.Builder().parseBodyElementsOnly(true).build();
 String html = "<html>\n" +
 "        <body>\n" +
 "            <form method=\"get\">\n" +
 "                <input type=\"text\" name=\"username\" value=\"John\" disabled>\n" +
 "                <input type=\"submit\" value=\"Submit\">\n" +
 "            </form>\n" +
 "        </body>\n" +
 "    </html>";
 document = Jsoup.parse(html, parser);
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> keyValues = formElement.formData();
 assertEquals(0, keyValues.size());
 }
}