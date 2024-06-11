package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setUp() {
        String html = "<html><body>" +
                "<form action='/' method='get'>" +
                "<input type='text' name='name'>" +
                "<input type='submit' value='Submit'>" +
                "</form>" +
                "</body></html>";
        Document doc = Jsoup.parse(html);
        form = (FormElement) doc.body().selectFirst("form");
    }

    @Test
    public void testEmptyForm() {
        assertNotNull(form);
        assertNotNull(form.elements());
        assertEquals(0, form.elements().size());
    }

    @Test
    public void testAddElement() {
        Element input = Jsoup.parse("<input type='text' name='input' value='test'/>").body();
        FormElement form = (FormElement) Jsoup.parse("<html><body><form action='/' method='get'>" +
                "<input type='text' name='name'>" +
                "</form></body></html>").body().selectFirst("form");
        form.addElement(input);

        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertEquals(input, elements.get(0));
    }

    @Test
    public void testRemoveElement() {
        FormElement form = (FormElement) Jsoup.parse("<html><body><form action='/' method='get'>" +
                "<input type='text' name='name'>" +
                "<input type='text' name='input' value='test'/>" +
                "</form></body></html>").body().selectFirst("form");
        Element input = form.elements().get(1);
        form.removeChild(input);

        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertNull(form.elements().get(1));
    }

    @Test
    public void testFormDataWhenNoElements() {
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWhenSelectElement() {
        String html = "<html><body>" +
                "<form action='/' method='get'>" +
                "<select name='colors'>" +
                "<option value='red' selected>" +
                "Red" +
                "</option>" +
                "<option value='green'>Green</option>" +
                "<option value='blue'>Blue</option>" +
                "</select>" +
                "</form>" +
                "</body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("colors", formData.get(0).key);
        assertEquals("red", formData.get(0).value);
    }

    @Test
    public void testFormDataWhenCheckboxOrRadioElement() {
        String html = "<html><body>" +
                "<form action='/' method='get'>" +
                "<input type='checkbox' name='color' value='red' checked>" +
                "</form>" +
                "</body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("color", formData.get(0).key);
        assertEquals("on", formData.get(0).value);

        html = "<html><body>" +
                "<form action='/' method='get'>" +
                "<input type='radio' name='color' value='red' checked>" +
                "</form>" +
                "</body></html>";
        doc = Jsoup.parse(html);
        form = (FormElement) doc.body().selectFirst("form");
        formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("color", formData.get(0).key);
        assertEquals("red", formData.get(0).value);
    }

    @Test
    public void testFormDataWhenTextInputElement() {
        String html = "<html><body>" +
                "<form action='/' method='get'>" +
                "<input type='text' name='name' value='test'>" +
                "</form>" +
                "</body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        assertEquals("name", formData.get(0).key);
        assertEquals("test", formData.get(0).value);
    }

    @Test
    public void testFormDataWhenMixedElements() {
        String html = "<html><body>" +
                "<form action='/' method='get'>" +
                "<input type='text' name='name' value='test'>" +
                "<select name='colors'>" +
                "<option value='red' selected>" +
                "Red" +
                "</option>" +
                "<option value='green'>Green</option>" +
                "<option value='blue'>Blue</option>" +
                "</select>" +
                "<input type='checkbox' name='agree' value='yes' checked>" +
                "</form>" +
                "</body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.body().selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(4, formData.size());
        assertEquals("name", formData.get(0).key);
        assertEquals("test", formData.get(0).value);
        assertEquals("colors", formData.get(1).key);
        assertEquals("red", formData.get(1).value);
        assertEquals("agree", formData.get(2).key);
        assertEquals("on", formData.get(2).value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormDataWhenNoActionOrBaseUri() {
        String html = "<html><body>" +
                "<form method='get'>" +
                "<input type='text' name='name' value='test'>" +
                "</form>" +
                "</body></html>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.body().selectFirst("form");
        form.submit();
    }
}