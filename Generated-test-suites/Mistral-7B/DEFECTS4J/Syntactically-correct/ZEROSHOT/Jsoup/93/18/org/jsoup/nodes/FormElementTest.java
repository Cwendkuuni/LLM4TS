package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseUtil;
import org.jsoup.select.Elements;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

 private Element form;
 private Connection connection;

 @Before
 public void setUp() {
 String html = " <form action=\"http://test.com\">" +
 " <input type=\"text\" name=\"username\" value=\"test\">" +
 " <input type=\"password\" name=\"password\" value=\"password\">" +
 " <input type=\"text\" name=\"age\" value=\"18\">" +
 " <select name=\"gender\">" +
 " <option value=\"M\" selected>Male</option>" +
 " <option value=\"F\">Female</option>" +
 " </select>" +
 " <input type=\"checkbox\" name=\"hobby\" value=\"football\">Football" +
 " <input type=\"checkbox\" name=\"hobby\" value=\"basketball\">Basketball" +
 " <input type=\"submit\" value=\"Submit\">" +
 " </form>";

 form = Jsoup.parse(html).selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNull(formElement.elements());
 }

 @Test
 public void testFormElementWithElements() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element inputText = new Element("input", new Attributes().put("type", "text")).setText("InputText");
 Element inputText2 = new Element("input", new Attributes().put("type", "text")).setText("InputText2");
 formElement.addElement(inputText);
 formElement.addElement(inputText2);
 assertEquals(2, formElement.elements().size());
 assertSame(inputText, formElement.elements().get(0));
 assertSame(inputText2, formElement.elements().get(1));
 }

 @Test
 public void testFormElementRemoveChild() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element inputText = new Element("input", new Attributes().put("type", "text")).setText("InputText");
 formElement.addElement(inputText);
 formElement.removeChild(inputText);
 assertEquals(0, formElement.elements().size());
 }

 @Test
 public void testFormSubmit() {
 List<Connection.KeyVal> formData = form.submit().data();
 assertEquals(6, formData.size());

 assertEquals("username", formData.get(0).key);
 assertEquals("test", formData.get(0).value);
 assertEquals("password", formData.get(1).key);
 assertEquals("password", formData.get(1).value);
 assertEquals("age", formData.get(2).key);
 assertEquals("18", formData.get(2).value);
 assertEquals("gender", formData.get(3).key);
 assertEquals("M", formData.get(3).value);
 assertEquals("hobby", formData.get(4).key);
 assertEquals("football", formData.get(4).value);
 assertFalse(formData.contains(HttpConnection.KeyVal.create("hobby", "basketball")));
 }

 @Test
 public void testFormDataForSelectElement() {
 List<Connection.KeyVal> formData = form.submit().data();
 assertEquals("gender", formData.get(3).key);
 assertEquals("M", formData.get(3).value);
 }

 @Test
 public void testFormDataForCheckboxElement() {
 List<Connection.KeyVal> formData = form.submit().data();
 assertFalse(formData.contains(HttpConnection.KeyVal.create("hobby", "football")));
 assertTrue(formData.contains(HttpConnection.KeyVal.create("hobby", "basketball")));
 }

 @Test
 public void testFormDataForDisabledElement() {
 Element textField = form.select("input[name=age]").first();
 textField.attributes().put("disabled", "disabled");
 List<Connection.KeyVal> formData = form.submit().data();
 assertEquals(5, formData.size());
 assertFalse(formData.contains(HttpConnection.KeyVal.create("age", "18")));
 }

 @Test
 public void testFormDataForFormWithoutAction() {
 Element formWithoutAction = ParseUtil.parseDoc(" <form><input name=\"username\" value=\"test\"></form>").selectFirst("form");
 Connection connection = formWithoutAction.submit();
 assertNull(connection.method());
 }
}