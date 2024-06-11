package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private FormElement form;

 @Before
 public void setup() {
 String html = "<html>" +
 " <body>" +
 " <form action=\"https://example.com\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" value=\"test\">" +
 " <input type=\"password\" name=\"password\" value=\"password\">" +
 " <select name=\"country\">" +
 " <option value=\"US\">United States</option>" +
 " <option value=\"CA\">Canada</option>" +
 " </select>" +
 " <input type=\"submit\" value=\"Submit\">" +
 " </form>" +
 " </body>" +
 " </html>";
 form = Jsoup.parseBodyFragment(html).selectFirst(Tag.NAME.equals("form"));
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(form.elements().get(0)); // input[type="text"]
 expectedElements.add(form.elements().get(1)); // input[type="password"]
 expectedElements.add(form.elements().get(2)); // select
 expectedElements.add(form.elements().get(3)); // input[type="submit"]

 Assert.assertEquals(expectedElements, form.elements());
 }

 @Test
 public void testAddElement() {
 Element input = new Element("input");
 input.attr("type", "hidden");
 input.attr("name", "hiddenField");
 input.attr("value", "hiddenValue");
 form.addElement(input);

 Elements elements = form.elements();
 Assert.assertEquals(5, elements.size());
 Assert.assertEquals(input, elements.get(4));
 }

 @Test
 public void testSubmit() throws Exception {
 Connection connection = form.submit();
 List<Connection.KeyVal> formData = connection.requestBody();

 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("username", "test"));
 expectedFormData.add(HttpConnection.KeyVal.create("password", "password"));
 expectedFormData.add(HttpConnection.KeyVal.create("country", "US")); // default value

 Assert.assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoActionUrl() {
 FormElement formNoAction = new FormElement(new Tag("form"), "", new Attributes());
 formNoAction.submit();
 }
}