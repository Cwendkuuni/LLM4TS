package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Document;
import org.jsoup.Nodes;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement form = new FormElement(tag, "", new Attributes());
        assertNotNull(form);
    }

    @Test
    public void testElements() {
        Document doc = Parser.parse("<html><body><form><input type='text' name='field1'><input type='text' name='field2'></form></body></html>");
        FormElement form = (FormElement) doc.select("form").first();
        Elements expected = doc.select("input");
        Elements actual = form.elements();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testAddElement() {
        Document doc = Parser.parse("<html><body><form><input type='text' name='field1'></form></body></html>");
        FormElement form = (FormElement) doc.select("form").first();
        Element input = new Element("input", new Attributes().put("name", "field2"));
        form.addElement(input);

        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(2, elements.size());
    }

    @Test
    public void testRemoveChild() {
        Document doc = Parser.parse("<html><body><form><input type='text' name='field1'></form></body></html>");
        FormElement form = (FormElement) doc.select("form").first();
        Element child = form.child(0);

        form.removeChild(child);
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        Document doc = Parser.parse("<html><body><form></form></body></html>");
        FormElement form = (FormElement) doc.select("form").first();
        form.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        Document doc = Parser.parse("<html><body><form action='javascript:alert(\"Hello\")'></form></body></html>");
        FormElement form = (FormElement) doc.select("form").first();
        form.submit();
    }

    @Test
    public void testFormDataEmpty() {
        Document doc = Parser.parse("<html><body><form></form></body></html>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithInputs() {
        Document doc = Parser.parse("<html><body><form><input type='text' name='field1' value='value1'><input type='text' name='field2' value='value2'></form></body></html>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = form.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("field1", keyVal1.key());
        assertEquals("value1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("field2", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }
}
//