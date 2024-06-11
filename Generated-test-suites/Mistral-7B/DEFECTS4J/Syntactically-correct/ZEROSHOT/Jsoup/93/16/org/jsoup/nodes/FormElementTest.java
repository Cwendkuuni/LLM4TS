package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @org.junit.Before
    public void setUp() {
        // Set up the HTML for testing
        String html = "<html><body>" +
                "<form action=\"http://example.com\" method=\"post\">" +
                "<input type=\"text\" name=\"username\" />" +
                "<select name=\"country\">" +
                "<option value=\"US\">United States</option>" +
                "<option value=\"CA\" selected>Canada</option>" +
                "</select>" +
                "<input type=\"checkbox\" name=\"agree\" checked />Agree" +
                "<input type=\"submit\" value=\"Submit\" />" +
                "</form>" +
                "</body></html>";
        Document doc = Jsoup.parse(html);
        formElement = doc.select("form").first();
    }

    private FormElement formElement;

    @org.junit.Test
    public void testGetElements() {
        Elements expectedElements = formElement.elements();
        Elements actualElements = formElement.elements();

        assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            Element expectedElement = expectedElements.get(i);
            Element actualElement = actualElements.get(i);
            assertEquals(expectedElement.tagName(), actualElement.tagName());
        }
    }

    @org.junit.Test
    public void testAddElement() {
        Element newElement = new Parser().parse("<input type=\"hidden\" name=\"newElement\" />").first();
        FormElement formElementWithNewElement = formElement.addElement(newElement);

        Elements expectedElements = formElement.elements();
        Elements actualElements = formElementWithNewElement.elements();

        assertEquals(expectedElements.size() + 1, actualElements.size());
        assertTrue(formElementWithNewElement.equals(formElementWithNewElement.addElement(newElement)));
    }

    @org.junit.Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("username", ""));
        expectedFormData.add(new Connection.KeyVal("country", "CA"));
        expectedFormData.add(new Connection.KeyVal("agree", "on"));

        assertEquals(expectedFormData.size(), formData.size());
        for (Connection.KeyVal expectedFormDataEntry : expectedFormData) {
            Connection.KeyVal formDataEntry = formData.get(formData.indexOf(expectedFormDataEntry));
            assertEquals(expectedFormDataEntry.key(), formDataEntry.key());
            assertEquals(expectedFormDataEntry.value(), formDataEntry.value());
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formElementWithoutBaseUri = new FormElement(new Tag("form"), null, new Attributes());
        formElementWithoutBaseUri.submit();
    }
}