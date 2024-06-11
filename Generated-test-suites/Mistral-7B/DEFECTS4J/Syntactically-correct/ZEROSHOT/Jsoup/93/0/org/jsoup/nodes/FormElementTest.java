package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Elements;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private Document document;

    @Before
    public void setUp() {
        String html = "<html>" +
                "<body>" +
                "<form action=\"http://example.com\">" +
                "<input type=\"text\" name=\"name\" value=\"John\">" +
                "<select name=\"age\">" +
                "<option value=\"18\">Eighteen</option>" +
                "<option value=\"21\" selected>Twenty One</option>" +
                "</select>" +
                "<input type=\"checkbox\" name=\"favoriteColor\" value=\"red\">Red" +
                "<input type=\"checkbox\" name=\"favoriteColor\" value=\"blue\">Blue" +
                "<input type=\"submit\" name=\"submit\">" +
                "</form>" +
                "</body>" +
                "</html>";
        this.document = Jsoup.parse(html);
    }

    @After
    public void tearDown() {
        this.document = null;
    }

    @Test
    public void testCreateFormElement() {
        Element formElement = this.document.select("form").first();
        FormElement formElementObj = (FormElement) formElement;
        assertNotNull(formElementObj);
    }

    @Test
    public void testElements() {
        Element formElement = this.document.select("form").first();
        FormElement formElementObj = (FormElement) formElement;
        Elements elements = formElementObj.elements();
        assertEquals(5, elements.size());
        assertTrue(elements.contains(formElement.select("input[type=text]").first()));
        assertTrue(elements.contains(formElement.select("select").first()));
        assertTrue(elements.contains(formElement.select("input[type=checkbox]").first()));
        assertTrue(elements.contains(formElement.select("input[type=checkbox]").last()));
        assertTrue(elements.contains(formElement.select("input[type=submit]").first()));
    }

    @Test
    public void testAddElement() {
        Element formElement = this.document.select("form").first();
        FormElement formElementObj = (FormElement) formElement;
        Element newElement = new Parser().parse("<input type=\"hidden\" name=\"newElement\">").first();
        formElementObj.addElement(newElement);
        Element addedElement = formElementObj.elements().last();
        assertEquals(newElement.outerHtml(), addedElement.outerHtml());
    }

    @Test
    public void testSubmit() {
        Element formElement = this.document.select("form").first();
        FormElement formElementObj = (FormElement) formElement;
        Connection connection = formElementObj.submit();
        assertNotNull(connection);
        List<Connection.KeyVal> formData = formElementObj.formData();
        assertEquals(6, formData.size());
        assertEquals("John", formData.get(0).value());
        assertEquals("name", formData.get(0).key());
        assertEquals("age", formData.get(1).key());
        Element selectedOption = formElement.select("select option:checked").first();
        assertEquals(selectedOption.val(), formData.get(2).value());
        assertEquals("name", formData.get(2).key());
        assertTrue(formData.get(3).value().equals("red") || formData.get(3).value().equals("blue"));
        assertEquals("favoriteColor", formData.get(3).key());
        assertEquals("on", formData.get(4).value());
        assertEquals("name", formData.get(4).key());
        assertEquals("submit", formData.get(5).key());
    }

    @Test
    public void testFormDataEmptyForm() {
        Element formElement = this.document.select("form").first();
        FormElement formElementObj = (FormElement) formElement;
        List<Connection.KeyVal> formData = formElementObj.formData();
        assertEquals(0, formData.size());
    }
}