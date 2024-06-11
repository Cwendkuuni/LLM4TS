package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input name=\"username\" type=\"text\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        // Test FormElement creation
        assertEquals(Tag.FORM, formElement.tag());
        assertNotNull(formElement.elements());
        assertFalse(formElement.elements().isEmpty());
    }

    @Test
    public void testFormElementGetElements() {
        String html = "<form><input name=\"username\" type=\"text\"><input name=\"password\" type=\"password\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        // Test getting form elements
        Elements formControls = formElement.elements();
        assertEquals(2, formControls.size());
        Element usernameInput = formControls.get(0);
        Element passwordInput = formControls.get(1);

        assertEquals("input", usernameInput.tagName());
        assertEquals("text", usernameInput.attr("type"));
        assertEquals("username", usernameInput.attr("name"));

        assertEquals("input", passwordInput.tagName());
        assertEquals("password", passwordInput.attr("type"));
        assertEquals("password", passwordInput.attr("name"));
    }

    @Test
    public void testFormElementAddElement() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        // Test adding a form control element
        Element input = new Element("input");
        input.attr("type", "text");
        input.attr("name", "username");
        formElement.addElement(input);

        Elements formControls = formElement.elements();
        assertEquals(1, formControls.size());
        Element addedInput = formControls.get(0);

        assertEquals("input", addedInput.tagName());
        assertEquals("text", addedInput.attr("type"));
        assertEquals("username", addedInput.attr("name"));
    }

    @Test
    public void testFormElementSubmitWithValidForm() {
        String html = "<form action=\"/submit\" method=\"post\"><input name=\"username\" type=\"text\"><input name=\"password\" type=\"password\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        // Test submitting a valid form
        List<Connection.KeyVal> formData = formElement.formData();
        Connection connection = formElement.submit();

        assertEquals("/submit", connection.url());
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals(formData.size(), connection.requestBody().size());

        for (Connection.KeyVal keyVal : formData) {
            String key = keyVal.key();
            String value = keyVal.val();

            assertEquals("username", key);
            assertEquals("", value); // FormElement does not store form element values
            assertEquals("password", formElement.elements().get(1).attr("name"));
            assertEquals("", formElement.elements().get(1).attr("value"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithInvalidForm() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");

        // Test submitting an invalid form (no action URL)
        formElement.submit();
    }
}