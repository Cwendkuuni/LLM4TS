 package org.jsoup.safety;

import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.nodes.*;
import org.junit.jupiter.api.*;

public class CleanerTest {

    @TestFactory
    public List<DynamicTest> testMethods() {
        return List.of(
                dynamicTest("testConstructor", () -> testConstructor()),
                dynamicTest("testConstructorWithNullWhitelist", () -> testConstructorWithNullWhitelist()),
                dynamicTest("testCleanMethodWithValidDocument", () -> testCleanMethodWithValidDocument()),
                dynamicTest("testCleanMethodWithNullDocument", () -> testCleanMethodWithNullDocument()),
                dynamicTest("testCleanMethodWithUnallowedElements", () -> testCleanMethodWithUnallowedElements()),
                dynamicTest("testIsValidMethodWithValidDocument", () -> testIsValidMethodWithValidDocument()),
                dynamicTest("testIsValidMethodWithNullDocument", () -> testIsValidMethodWithNullDocument()),
                dynamicTest("testIsValidMethodWithUnallowedElements", () -> testIsValidMethodWithUnallowedElements()),
                dynamicTest("testCopySafeNodesMethodWithValidElements", () -> testCopySafeNodesMethodWithValidElements()),
                dynamicTest("testCopySafeNodesMethodWithUnallowedElements", () -> testCopySafeNodesMethodWithUnallowedElements()),
                dynamicTest("testCreateSafeElementMethodWithSafeElement", () -> testCreateSafeElementMethodWithSafeElement()),
                dynamicTest("testCreateSafeElementMethodWithUnallowedAttributes", () -> testCreateSafeElementMethodWithUnallowedAttributes())
        );
    }

    @Test
    public void testConstructor() {
        Whitelist whitelist = new Whitelist(); // assuming Whitelist class is testable
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanMethodWithValidDocument() {
        Document dirtyDocument = new Document("", "http://example.com") // create a valid document
                .body(new Element("p", null, new Attributes().put("class", "test")));
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        // verify that the cleaned document contains only the allowed elements and attributes
    }

    // ... test cases for other methods

    @Test(expected = NullPointerException.class)
    public void testCleanMethodWithNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleanedDocument = cleaner.clean(null);
        assertNull(cleanedDocument);
    }

    @Test
    public void testIsValidMethodWithValidDocument() {
        Document dirtyDocument = new Document("", "http://example.com") // create a valid document
                .body(new Element("p", null, new Attributes().put("class", "test")));
        Cleaner cleaner = new Cleaner(new Whitelist());
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidMethodWithNullDocument() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        boolean isValid = cleaner.isValid(null);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidMethodWithUnallowedElements() {
        Document dirtyDocument = new Document("", "http://example.com") // create a document with unallowed elements
                .body(new Element("script", null, new Attributes().put("src", "javascript:alert('XSS')")));
        Cleaner cleaner = new Cleaner(new Whitelist());
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    // ... test cases for copySafeNodes and createSafeElement methods
}