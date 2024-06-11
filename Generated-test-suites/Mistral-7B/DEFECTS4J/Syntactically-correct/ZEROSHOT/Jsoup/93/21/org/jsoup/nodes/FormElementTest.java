package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        // Given
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes formAttributes = new Attributes();

        // When
        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        // Then
        assertEquals("form", formElement.tag().name());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(formAttributes, formElement.attributes());
    }

    @Test
    public void testFormElements() {
        // Given
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes formAttributes = new Attributes();

        // When
        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        // Then
        assertTrue(formElement.elements().isEmpty());

        // Given
        Element inputElement = Parser.parseInput("input", "<input type='text'>").first();

        // When
        formElement.addElement(inputElement);

        // Then
        assertFalse(formElement.elements().isEmpty());
        assertEquals(1, formElement.elements().size());
        assertEquals(inputElement, formElement.elements().get(0));
    }

    @Test
    public void testFormElementRemoveChild() {
        // Given
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes formAttributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        Element inputElement = Parser.parseInput("input", "<input type='text'>").first();
        formElement.addElement(inputElement);

        // When
        formElement.removeChild(inputElement);

        // Then
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testFormElementSubmit() {
        // Given
        Tag formTag = new Tag("form", new Attributes().put("action", "http://example.com"));
        String baseUri = "https://example.com";
        Attributes formAttributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        // Given - Create a mock connection
        Connection mockConnection = Mockito.mock(Connection.class);

        // When
        Connection submittedConnection = formElement.submit();

        // Then
        assertEquals(mockConnection, submittedConnection);
        assertEquals("POST", submittedConnection.method().name());
        assertEquals("http://example.com", submittedConnection.url());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithNoAction() {
        // Given
        Tag formTag = new Tag("form", new Attributes());
        String baseUri = "https://example.com";
        Attributes formAttributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        // When
        formElement.submit();

        // Then
        // The test should fail with an IllegalArgumentException
    }

    @Test
    public void testFormData() {
        // Given
        Tag formTag = new Tag("form", new Attributes().put("action", "http://example.com"));
        String baseUri = "https://example.com";
        Attributes formAttributes = new Attributes();

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Element inputElement1 = Parser.parseInput("input", "<input type='text' name='input1' value='inputValue1'>").first();
        Element inputElement2 = Parser.parseInput("input", "<input type='text' name='input2'>").first();
        Element selectElement = Parser.parseInput("select", "<select><option value='option1'>Option 1</option><option value='option2' selected>Option 2</option></select>").first();

        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);
        formElement.addElement(selectElement);

        // When
        List<Connection.KeyVal> formData = formElement.formData();

        // Then
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(Connection.KeyVal.create("input1", "inputValue1"));
        expectedFormData.add(Connection.KeyVal.create("input2", ""));
        expectedFormData.add(Connection.KeyVal.create("option2", ""));

        assertEquals(expectedFormData, formData);
    }
}