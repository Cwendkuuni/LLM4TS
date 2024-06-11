package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Elements;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 " <form action='/' method='post'>" +
 " <input type='text' name='username' />" +
 " <input type='password' name='password' />" +
 " <select name='favoriteColor'>" +
 " <option value='red'>Red</option>" +
 " <option value='green' selected>Green</option>" +
 " <option value='blue'>Blue</option>" +
 " </select>" +
 " <input type='submit' value='Submit' />" +
 " </form>" +
 " </body>" +
 "</html>";
 formElement = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testElements() {
 FormElement formElement = (FormElement) formElement;
 Elements elements = formElement.elements();
 assertEquals(4, elements.size());
 assertNotNull(elements.get(0).attr("type"));
 assertEquals("text", elements.get(0).attr("type"));
 assertNotNull(elements.get(1).attr("type"));
 assertEquals("password", elements.get(1).attr("type"));
 assertNotNull(elements.get(2).tagName());
 assertEquals("select", elements.get(2).tagName());
 }

 @Test
 public void testSubmitWithTextInputs() {
 FormElement formElement = (FormElement) formElement;
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("username", formData.get(0).key);
 assertEquals("", formData.get(0).value);
 assertEquals("password", formData.get(1).key);
 assertEquals("", formData.get(1).value);
 }

 @Test
 public void testSubmitWithSelectElement() {
 FormElement formElement = (FormElement) formElement;
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("favoriteColor", formData.get(0).key);
 assertEquals("green", formData.get(0).value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoActionUrl() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 Connection connection = formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidMethod() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 formElement.setAttribute("method", "invalid");
 Connection connection = formElement.submit();
 }
}