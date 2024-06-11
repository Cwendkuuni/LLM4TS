
package org.junit.tests;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    private static Element setupFormElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://localhost:8080/";
        Attributes attributes = new Attributes();

        return new FormElement(formTag, baseUri, attributes);
    }

    private static Element setupFormElementWithControls() {
        Tag formTag = new Tag("form");
        String baseUri = "http://localhost:8080/";
        Attributes attributes = new Attributes();

        Elements controls = new Elements();
        controls.add(new Element("input", new Attributes().put("type", "text").put("name", "name1")));
        controls.add(new Element("textarea", new Attributes().put("name", "name2")));
        controls.add(new Element("select", new Attributes().put("name", "name3")));

        formTag.appendChildren(controls.toArray(new Element[0]));

        return new FormElement(formTag, baseUri, attributes);
    }

    private static Element setupFormElementWithControlById() {
        Tag formTag = new Tag("form");
        String baseUri = "http://localhost:8080/";
        Attributes attributes = new Attributes();

        Elements controls = new Elements();
        Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "name1").put("id", "control-id"));

        formTag.appendChildren(controls.toArray(new Element[0]));
        formTag.appendChild(inputElement);

        return new FormElement(formTag, baseUri, attributes);
    }

    private static Element setupFormElementWithNoActionURL() {
        Tag formTag = new Tag("form");
        String baseUri = null;
        Attributes attributes = new Attributes();

        return new FormElement(formTag, baseUri, attributes);
    }

    @Test
    public void testFormElementConstruction() {
        // Expert 1 Test Case 1
    }

    @Test
    public void testFormElementGetFormControls() {
        // Expert 1 Test Case 2
    }

    @Test
    public void testFormElementAddElement() {
        // Expert 1 Test Case 3
    }

    @Test
    public void testFormElementSubmit() {
        // Expert 1 Test Case 4
    }

    @Test
    public void testFormElementEmptyFormData() {
        // Expert 1 Test Case 5
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitMissingActionURL() {
        // Expert 1 Test Case 6
    }

    @Test
    public void testFormElementGetFormControlById() {
        // Expert 3 Test Case 2
    }

    @Test
    public void testFormElementAddElement() {
        // Expert 3 Test Case 3
    }

    @Test
    public void testFormElementSubmit() {
        // Expert 3 Test Case 4
    }

    @Test
    public void testFormElementSubmitMissingFormControl() {
        // Expert 3 Test Case 5
    }
}
