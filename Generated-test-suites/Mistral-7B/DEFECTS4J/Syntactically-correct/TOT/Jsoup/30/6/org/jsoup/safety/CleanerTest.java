package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Cleaner class.
 */
public class CleanerTest {

    /**
     * Test constructor - checks that a new Cleaner instance can be created with a valid Whitelist.
     */
    @Test
    public void testConstructor() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertNotNull(cleaner);
    }

    /**
     * Test clean method - checks that a new, clean document is created, containing only elements allowed by the whitelist.
     */
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><h1>Hello</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals(0, cleanDocument.body().childNodeSize());

        // Check that the clean document does not contain the script tag
        Assert.assertFalse(cleanDocument.body().hasMetaElements());
        Assert.assertFalse(cleanDocument.body().hasText());
    }

    /**
     * Test isValid method - checks that a valid document is considered valid against the whitelist.
     */
    @Test
    public void testIsValid_ValidDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(valid);
    }

    /**
     * Test isValid method - checks that an invalid document is considered invalid against the whitelist.
     */
    @Test
    public void testIsValid_InvalidDocument() {
        Whitelist whitelist = new Whitelist.SimpleTagFilter(Tag.valueOf("p"), Tag.valueOf("script"));
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello</p><script>alert('XSS');</script></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse(valid);
    }

    /**
     * Test copySafeNodes method - checks that trusted nodes are cloned and copied into the destination.
     */
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><h1 id='h1'>Hello</h1><p id='p'>World</p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        copySafeNodesAndAssert(cleaner, dirtyDocument.body(), cleanDocument.body(), Arrays.asList("h1", "p"));
    }

    /**
     * Test copySafeNodes method - checks that untrusted nodes are discarded.
     */
    @Test
    public void testCopySafeNodes_UnsafeTag() {
        Whitelist whitelist = new Whitelist.SimpleTagFilter(Tag.valueOf("p"));
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><h1 id='h1'>Hello</h1><script>alert('XSS');</script></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        copySafeNodesAndAssert(cleaner, dirtyDocument.body(), cleanDocument.body(), Arrays.asList("p"));

        // Check that the script tag has been discarded
        Assert.assertFalse(cleanDocument.body().hasMetaElements());
        Assert.assertFalse(cleanDocument.body().childNodeSize() > 0);
    }

    /**
     * Helper method for testing copySafeNodes.
     *
     * @param cleaner      Cleaner instance
     * @param source       Original dirty document's body
     * @param destination  New, clean document's body
     * @param allowedTags  List of allowed tags
     */
    private void copySafeNodesAndAssert(Cleaner cleaner, Element source, Element destination, List<String> allowedTags) {
        int numDiscarded = cleaner.copySafeNodes(source, destination);

        for (String tag : allowedTags) {
            Element expectedElement = source.getElementsByTag(tag).first();
            Element actualElement = destination.getElementsByTag(tag).first();

            Assert.assertNotNull(actualElement);
            Assert.assertEquals(expectedElement.text(), actualElement.text());
            Assert.assertEquals(expectedElement.className(), actualElement.className());
            for (String name : expectedElement.attributes().keySet()) {
                String expectedAttribute = expectedElement.attribute(name);
                String actualAttribute = actualElement.attribute(name);

                if (expectedAttribute == null) {
                    Assert.assertNull(actualAttribute);
                } else {
                    Assert.assertEquals(expectedAttribute, actualAttribute);
                }
            }
        }

        Assert.assertEquals(0, numDiscarded);
    }

}