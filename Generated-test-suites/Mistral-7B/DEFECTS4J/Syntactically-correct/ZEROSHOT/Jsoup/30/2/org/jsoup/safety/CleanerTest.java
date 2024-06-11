package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new Whitelist.SimpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCreateCleanDocument() {
        Whitelist whitelist = new Whitelist.SimpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("http://example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(cleanDocument.body().tagName(), "body");
    }

    @Test
    public void testCreateInvalidDocument() {
        Whitelist whitelist = new Whitelist.SimpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("http://example.com");
        Element invalidElement = new Element(Tag.valueOf("script"), dirtyDocument.baseUri(), new Attributes().put("src", "malicious.js"));
        dirtyDocument.body().appendChild(invalidElement);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCreateValidDocument() {
        Whitelist whitelist = new Whitelist.SimpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("http://example.com");
        Element validElement = new Element(Tag.valueOf("p"), dirtyDocument.baseUri(), new Attributes().put("class", "test"));
        dirtyDocument.body().appendChild(validElement);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist.SimpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("http://example.com");
        Element root = new Element(Tag.valueOf("div"), dirtyDocument.baseUri(), new Attributes());
        Element child1 = new Element(Tag.valueOf("p"), dirtyDocument.baseUri(), new Attributes().put("class", "test"));
        Element child2 = new Element(Tag.valueOf("script"), dirtyDocument.baseUri(), new Attributes().put("src", "malicious.js"));
        root.appendChild(child1);
        root.appendChild(child2);
        dirtyDocument.body().appendChild(root);

        Document cleanDocument = Document.createShell("http://example.com");
        Element destination = cleanDocument.body();
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(numDiscarded, 1);
        assertNotNull(destination.child(0));
        assertEquals(destination.child(0).tagName(), "p");
        assertNull(destination.child(1));
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist.SimpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element(Tag.valueOf("p"), null, new Attributes().put("class", "test").put("id", "testId"));

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destElement = elementMeta.el;

        assertEquals(destElement.tagName(), "p");
        assertEquals(destElement.baseUri(), sourceElement.baseUri());
        List<Attribute> attributes = destElement.attributes();
        assertTrue(attributes.size() == 1);
        assertEquals(attributes.get(0).getName(), "class");
        assertEquals(attributes.get(0).getValue(), "test");
        assertNull(attributes.get(0).get("id"));
    }

    @Test
    public void testIsSafeTag() {
        Whitelist whitelist = new Whitelist.SimpleText();
        assertTrue(Cleaner.class.getDeclaredMethod("isSafeTag", String.class).invoke(whitelist, "p"));
        assertTrue(Cleaner.class.getDeclaredMethod("isSafeTag", String.class, Element.class, Attribute.class).invoke(whitelist, "p", null, null));
        assertFalse(Cleaner.class.getDeclaredMethod("isSafeTag", String.class, Element.class, Attribute.class).invoke(whitelist, "script", null, null));
    }

    @Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = new Whitelist.SimpleText();

        Element sourceElement = new Element(Tag.valueOf("p"), null, new Attributes().put("class", "test").put("data-test", "value"));
        assertTrue(Cleaner.class.getDeclaredMethod("isSafeAttribute", String.class, String.class, Element.class, Attribute.class).invoke(whitelist, "class", "p", sourceElement, null));
        assertTrue(Cleaner.class.getDeclaredMethod("isSafeAttribute", String.class, String.class, Element.class, Attribute.class).invoke(whitelist, "data-test", "value", sourceElement, null));
        assertFalse(Cleaner.class.getDeclaredMethod("isSafeAttribute", String.class, String.class, Element.class, Attribute.class).invoke(whitelist, "onclick", "javascript:alert('XSS');", sourceElement, null));
    }

    @Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = new Whitelist.SimpleText();

        List<Attribute> attributes = whitelist.getEnforcedAttributes("p");
        assertTrue(attributes.isEmpty());

        attributes = whitelist.getEnforcedAttributes("img");
        assertTrue(attributes.size() > 0);
        assertNotNull(attributes.get(0).getName());
        assertNotNull(attributes.get(0).getValue());
    }
}