package org.jsoup.nodes;

import org.jsoup.nodes.*;
import org.jsoup.nodes.elements.FormElement;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {
    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertNotNull(formElement);
    }

    @Test
    public void testElementsMethod() throws Exception {
        Document doc = Jsoup.parse("<form><input type='text' name='username' class='username'></form>");
        Element formElement = doc.select("form").first();
        Elements expectedElements = doc.select("input[name='username']");

        Elements actualElements = formElement.elements();

        assertEquals(expectedElements.size(), actualElements.size());
    }

    @Test
    public void testAddElementMethod() throws Exception {
        Document doc = Jsoup.parse("<form></form>");
        Element formElement = doc.select("form").first();
        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");

        FormElement formElementAfterAdd = formElement.addElement(inputElement);
        Elements actualElements = formElementAfterAdd.elements();

        assertEquals(new Elements(new Element("input")), actualElements);
    }

    @Test
    public void testSubmitMethod() throws Exception {
        Document doc = Jsoup.parse("<form action='http://example.com' method='post'></form>");
        Element formElement = doc.select("form").first();

        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormDataMethod() throws Exception {
        Document doc = Jsoup.parse("<form><input type='text' name='username' value='test'></form>");
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("username", "test"));

        List<Connection.KeyVal> actualFormData = formElement.formData();
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataMethodWithSelectTag() throws Exception {
        Document doc = Jsoup.parse("<form><select name='username'><option value='test1'>Test 1</option><option value='test2' selected>Test 2</option></select></form>");
        Element formElement = doc.select("form").first();
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("username", "test2"));

        List<Connection.KeyVal> actualFormData = formElement.formData();
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataMethodWithDisabledElement() throws Exception {
        Document doc = Jsoup.parse("<form><input type='text' name='username' value='test' disabled></form>");
        Element formElement = doc.select("form").first();

        List<Connection.KeyVal> actualFormData = formElement.formData();
        assertTrue(actualFormData.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithEmptyAction() throws Exception {
        Document doc = Jsoup.parse("<form action='' method='post'></form>");
        Element formElement = doc.select("form").first();

        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithEmptyMethod() throws Exception {
        Document doc = Jsoup.parse("<form action='http://example.com' method=''></form>");
        Element formElement = doc.select("form").first();

        formElement.submit();
    }
}