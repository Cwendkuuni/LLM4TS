import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.XmlDeclaration;

public class XmlDeclarationTest {
    @Test
    void testXmlDeclarationCreation() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertNotNull(xmlDeclaration);
    }

    @Test
    void testXmlDeclarationNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    void testXmlDeclarationWithNullValues() {
        assertThrows(NullPointerException.class, () -> new XmlDeclaration(null, "baseUri", false));
        assertThrows(NullPointerException.class, () -> new XmlDeclaration("name", null, false));
        assertThrows(NullPointerException.class, () -> new XmlDeclaration("name", "baseUri", null));
    }

    @Test
    void testXmlDeclarationEmptyStringValues() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", "", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("", xmlDeclaration.name());
        assertEquals("", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    void testXmlDeclarationWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new XmlDeclaration("xml%", "baseUri", false));
    }

    @Test
    void testXmlDeclarationWithInvalidAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("version", null); // Test null value
        attributes.put("encoding", ""); // Test empty string value
        assertThrows(NullPointerException.class, () -> new XmlDeclaration("xml", "baseUri", false, attributes));

        attributes = new Attributes();
        attributes.put("invalidAttributeName", "value"); // Test invalid attribute name
        assertThrows(IllegalArgumentException.class, () -> new XmlDeclaration("xml", "baseUri", false, attributes));
    }

    // Test other methods with edge cases and exceptions here

    @Test
    void testXmlDeclarationToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("<declaration version=\"version\" encoding=\"encoding\">", xmlDeclaration.toString());
    }
}