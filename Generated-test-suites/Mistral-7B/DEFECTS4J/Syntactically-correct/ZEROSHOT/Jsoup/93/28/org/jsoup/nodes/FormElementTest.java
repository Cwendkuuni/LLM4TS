package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

    private static final String HTML_FORM_WITH_INPUTS =
            "<html><head></head><body>" +
                    "<form action=\"https://example.com\" method=\"post\">" +
                    "<input type=\"text\" name=\"name1\" value=\"value1\">" +
                    "<input type=\"text\" name=\"name2\" value=\"value2\">" +
                    "<select name=\"name3\">" +
                    "<option value=\"value3\">Option 1</option>" +
                    "<option value=\"value4\" selected>Option 2</option>" +
                    "</select>" +
                    "<input type=\"checkbox\" name=\"name4\" checked>" +
                    "<input type=\"checkbox\" name=\"name5\">" +
                    "<input type=\"submit\" value=\"Submit\">" +
                    "</form>" +
                    "</body></html>";

    private Element formElement;

    @Before
    public void setUp() {
        Document doc = Jsoup.parse(HTML_FORM_WITH_INPUTS);
        formElement = doc.select("form").first();
    }

    @Test
    public void testFormElementCreation() {
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
    }

    @Test
    public void testGetElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(5, elements.size());
    }

    @Test
    public void testAddElement() {
        Element newInput = Jsoup.parse("<input type=\"text\" name=\"newName\" value=\"newValue\">").first();
        formElement.addElement(newInput);
        Elements elements = formElement.elements();
        assertEquals(6, elements.size());
        assertEquals(newInput, elements.get(5));
    }

    @Test
    public void testRemoveChild() {
        Elements elements = formElement.elements();
        Element inputToRemove = elements.get(0);
        formElement.removeChild(inputToRemove);
        elements = formElement.elements();
        assertEquals(5, elements.size());
        assertNull(formElement.child(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("POST", connection.method());
        assertEquals("https://example.com", connection.url());
    }

    @Test
    public void testFormDataEmptyForm() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataTextInput() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        Connection.KeyVal name1 = formData.get(0);
        assertEquals("name1", name1.key());
        assertEquals("value1", name1.value());

        Connection.KeyVal name2 = formData.get(1);
        assertEquals("name2", name2.key());
        assertEquals("value2", name2.value());
    }

    @Test
    public void testFormDataSelectInput() {
        List<Connection.KeyVal> formData = formElement.formData();
        Connection.KeyVal name3 = formData.get(2);
        assertEquals("name3", name3.key());
        assertEquals("value4", name3.value());
    }

    @Test
    public void testFormDataCheckboxInput() {
        List<Connection.KeyVal> formData = formElement.formData();
        int checkboxIndex = formData.size() - 2;
        Connection.KeyVal name4 = formData.get(checkboxIndex);
        assertEquals("name4", name4.key());
        assertEquals("on", name4.value());

        checkboxIndex -= 1;
        Connection.KeyVal name5 = formData.get(checkboxIndex);
        assertEquals("name5", name5.key());
        assertNull(name5.value());
    }
}