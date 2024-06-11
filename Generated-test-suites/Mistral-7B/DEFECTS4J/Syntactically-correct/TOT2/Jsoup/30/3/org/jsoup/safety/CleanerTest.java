import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
        dirtyDocument = Document.create("test.html");
    }

    @Test
    public void testCleanerConstructor() {
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanMethod() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Expected clean document", "", cleanDocument.body().html());
    }

    @Test
    public void testIsValidMethod() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse("Expected invalid document", isValid);
    }

    @Test
    public void testCopySafeNodesMethod() {
        Element root = new Element("html", null);
        Element destination = new Element("body", null);
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals("Expected zero discarded nodes", 0, numDiscarded);
    }

    @Test
    public void testCreateSafeElementMethod() {
        Element sourceElement = new Element("div", null);
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        assertNotNull("Expected output from createSafeElement", elementMeta.el);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanerConstructorWithNullWhitelist() {
        Whitelist nullWhitelist = null;
        Cleaner cleaner = new Cleaner(nullWhitelist);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanMethodWithNullDocument() {
        Document nullDocument = null;
        cleaner.clean(nullDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMethodWithNullDocument() {
        Document nullDocument = null;
        cleaner.isValid(nullDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopySafeNodesMethodWithNullRoot() {
        Element nullRoot = null;
        Element destination = new Element("body", null);
        cleaner.copySafeNodes(nullRoot, destination);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSafeElementMethodWithNullSourceElement() {
        Element nullSourceElement = null;
        cleaner.createSafeElement(nullSourceElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMethodWithEmptyDocument() {
        Document emptyDocument = Document.create("");
        boolean isValid = cleaner.isValid(emptyDocument);
        assertFalse("Expected invalid document", isValid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSafeElementMethodWithInvalidTag() {
        Element invalidSourceElement = new Element("invalid", null);
        ElementMeta elementMeta = cleaner.createSafeElement(invalidSourceElement);
        assertNull("Expected null output", elementMeta.el);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateSafeElementMethodWithInvalidAttribute() {
        Element sourceElement = new Element("div", null);
        sourceElement.attr("xss", "script:alert('Hello World')");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        assertNull("Expected null output", elementMeta.el);
    }
}