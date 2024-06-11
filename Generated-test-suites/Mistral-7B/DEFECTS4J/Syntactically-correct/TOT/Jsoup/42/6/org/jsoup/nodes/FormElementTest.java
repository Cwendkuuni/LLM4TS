package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for org.jsoup.nodes.FormElement class.
 */
public class FormElementTest {

    private static final String BASE_URL = "http://example.com";
    private static final String FORM_HTML = "<html><body><form action=\"/submit\" method=\"post\"><input type=\"text\" name=\"username\"/><input type=\"submit\"/></form></body></html>";

    private Element formElement;
    private Connection connection;

    @Before
    public void setup() throws Exception {
        Document doc = Jsoup.parse(FORMD_HTML);
        formElement = doc.selectFirst("form");
        Validate.notNull(formElement, "Failed to parse form element.");
        connection = formElement.submit();
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty(), "Form element should have at least one child.");
        assertEquals(2, elements.size(), "Form should have exactly two children: input and submit.");
    }

    @Test
    public void testElementsEmptyForm() {
        Element emptyFormElement = new FormElement(new Tag("form"), BASE_URL, new Attributes());
        Elements elements = emptyFormElement.elements();
        assertTrue(elements.isEmpty(), "Empty form should have no children.");
    }

    @Test
    public void testSubmitWithNoAction() {
        FormElement formElementNoAction = new FormElement(new Tag("form"), BASE_URL, new Attributes());
        try {
            formElementNoAction.submit();
            assertFalse(true, "IllegalArgumentException should be thrown when there is no form action URL.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSubmitWithInvalidHttpMethod() {
        FormElement formElementInvalidMethod = new FormElement(new Tag("form"), BASE_URL, new Attributes().put("method", "invalid"));
        try {
            formElementInvalidMethod.submit();
            assertFalse(true, "IllegalArgumentException should be thrown when the form method is invalid.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSubmitWithValidHttpMethod() {
        Connection submittedConnection = formElement.submit();
        assertNotNull(submittedConnection, "Submit should return a non-null Connection object.");
        assertEquals(Connection.Method.POST, submittedConnection.method(), "Form submit method should be POST.");
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData, "Form data should not be null.");
        assertEquals(1, formData.size(), "Form should have exactly one data pair: username.");
        assertEquals("username", formData.get(0).key(), "Form data key should be 'username'.");
        assertEquals(formElement.selectFirst("input[name=username]").val(), formData.get(0).val(), "Form data value should be the input value.");
    }

    @Test
    public void testFormDataWithMultipleInputs() {
        String FORM_HTML_MULTIPLE = "<html><body><form action=\"/submit\" method=\"post\"><input type=\"text\" name=\"username\"/><input type=\"text\" name=\"password\"/><input type=\"submit\"/></form></body></html>";

        Document doc = Jsoup.parse(FORM_HTML_MULTIPLE);
        Element formElementMultiple = doc.selectFirst("form");
        List<Connection.KeyVal> formDataMultiple = formElementMultiple.formData();

        assertEquals(2, formDataMultiple.size(), "Form should have exactly two data pairs: username and password.");
        assertEquals("username", formDataMultiple.get(0).key(), "Form data key should be 'username'.");
        assertEquals(formElementMultiple.selectFirst("input[name=username]").val(), formDataMultiple.get(0).val(), "Form data value should be the input value for username.");
        assertEquals("password", formDataMultiple.get(1).key(), "Form data key should be 'password'.");
        assertEquals(formElementMultiple.selectFirst("input[name=password]").val(), formDataMultiple.get(1).val(), "Form data value should be the input value for password.");
    }
}