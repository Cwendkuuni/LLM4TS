package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    private Element formElement;
    private Connection connection;
    private static final String BASE_URL = "http://example.com";

    @Before
    public void setup() {
        // Create a simple HTML document with a form element
        String html = "<html><body>" +
                "<form action=\"submit.html\" method=\"post\">" +
                "<input type=\"text\" name=\"username\" value=\"test\">" +
                "<input type=\"text\" name=\"password\" value=\"password\">" +
                "<select name=\"country\">" +
                "<option value=\"US\">United States</option>" +
                "<option value=\"CA\">Canada</option>" +
                "</select>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</form>" +
                "</body></html>";
        Document document = Jsoup.parse(html);
        formElement = document.select("form").first();
    }

    @Test
    public void testFormElementConstruction() {
        // Test constructor with tag, baseUri, and attributes
        Tag tag = new Tag("form");
        FormElement expectedFormElement = new FormElement(tag, BASE_URL, new Attributes());
        FormElement actualFormElement = new FormElement(tag, BASE_URL, new Attributes());
        assertEquals(expectedFormElement.getClass(), actualFormElement.getClass());
        assertEquals(expectedFormElement, actualFormElement);
    }

    @Test
    public void testGetElements() {
        Elements expectedElements = new Elements();
        expectedElements.add(new ElementTag("input", new Attributes()
                .put("type", "text")
                .put("name", "username")
                .put("value", "test")));
        expectedElements.add(new ElementTag("input", new Attributes()
                .put("type", "text")
                .put("name", "password")
                .put("value", "password")));
        expectedElements.add(new ElementTag("select", new Attributes()
                .put("name", "country")));

        Elements actualElements = formElement.elements();
        assertEquals(expectedElements.size(), actualElements.size());
        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testAddElement() {
        Element inputElement = new ElementTag("input", new Attributes()
                .put("type", "hidden")
                .put("name", "csrf_token")
                .put("value", "csrf_value"));

        FormElement formElement = new FormElement(new Tag("form"), BASE_URL, new Attributes());
        formElement.addElement(inputElement);
        Elements actualElements = formElement.elements();
        assertEquals(1, actualElements.size());
        assertEquals(inputElement, actualElements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Element inputElement = new ElementTag("input", new Attributes()
                .put("type", "hidden")
                .put("name", "csrf_token")
                .put("value", "csrf_value"));

        FormElement formElement = new FormElement(new Tag("form"), BASE_URL, new Attributes());
        formElement.addElement(inputElement);
        formElement.removeChild(inputElement);
        Elements actualElements = formElement.elements();
        assertEquals(0, actualElements.size());
    }

    @Test
    public void testSubmit() throws Exception {
        // Test submit method and formData method
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("username", "test"));
        expectedFormData.add(new Connection.KeyVal("password", "password"));
        expectedFormData.add(new Connection.KeyVal("country", "US"));

        List<Connection.KeyVal> actualFormData = formElement.submit().data();
        assertEquals(expectedFormData.size(), actualFormData.size());
        assertEquals(expectedFormData, actualFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidActionUrl() {
        formElement.removeAttr("action");
        formElement.submit();
    }
}