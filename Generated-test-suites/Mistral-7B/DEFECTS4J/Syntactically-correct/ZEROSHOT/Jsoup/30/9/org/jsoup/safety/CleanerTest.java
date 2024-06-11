package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import org.junit.Before;
import org.junit.Test;

/**
 * Test file for Cleaner class.
 */
public class CleanerTest {

    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        String[] allowedTags = {"p", "a"};
        String[] allowedAttributes = {"class"};

        whitelist = mock(Whitelist.class);
        when(whitelist.isSafeTag(Tag.valueOf("p"))).thenReturn(true);
        when(whitelist.isSafeTag(Tag.valueOf("a"))).thenReturn(true);
        when(whitelist.isSafeAttribute("p", null, Attribute.class)).thenReturn(true);
        when(whitelist.isSafeAttribute("p", null, new Attribute("class", "class"))).thenReturn(true);
        when(whitelist.isSafeTag(Tag.valueOf("div"))).thenReturn(false);

        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testCreateCleaner() {
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Document dirtyDocument = createDocument("<html><body><p class='test'>Hello <script>world</script></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleanDocument.body().hasMetaCharset());
        assertFalse(cleanDocument.body().hasDoctype());
        assertFalse(cleanDocument.body().hasXmlDeclaration());
        assertNull(cleanDocument.body().child(0).child(0).child(0));
        assertSame(cleanDocument.body().child(0), cleanDocument.body().getElementsByTag("p").first());
        assertEquals(1, cleanDocument.body().numSiblings());
        assertEquals("p", cleanDocument.body().tagName());
    }

    @Test
    public void testIsValidDocument() {
        Document dirtyDocument = createDocument("<html><body><p class='test'>Hello <script>world</script></p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testIsValidValidDocument() {
        Document dirtyDocument = createDocument("<html><body><p class='test'>Hello World</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = createDocument("<html><body><p class='test'>Hello <script>world</script></p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = createElement("<p class='test'>Hello <script>world</script></p>");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testIsSafeTag() {
        assertTrue(cleaner.whitelist.isSafeTag(Tag.valueOf("p")));
        assertFalse(cleaner.whitelist.isSafeTag(Tag.valueOf("div")));
    }

    @Test
    public void testIsSafeAttribute() {
        assertTrue(cleaner.whitelist.isSafeAttribute("p", null, new Attribute("class", "class")));
        assertFalse(cleaner.whitelist.isSafeAttribute("p", null, new Attribute("onclick", "alert('Hello')")));
    }

    @Test
    public void testGetEnforcedAttributes() {
        List<Attribute> attributes = Arrays.asList(
            new Attribute("class", "class"),
            new Attribute("id", "id")
        );

        when(whitelist.getEnforcedAttributes("p")).thenReturn(attributes);

        Element sourceElement = createElement("<p id='test' class='test'>Hello World</p>");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals(2, elementMeta.numAttribsDiscarded);
        assertEquals("class", elementMeta.el.tagName());
        assertEquals(1, elementMeta.el.numSiblings());
    }

    private Document createDocument(String html) {
        return Jsoup.parse(html);
    }

    private Element createElement(String html) {
        return Jsoup.parse(html).body().getElementsByTag("body").first().getElementsByTag("p").first();
    }
}