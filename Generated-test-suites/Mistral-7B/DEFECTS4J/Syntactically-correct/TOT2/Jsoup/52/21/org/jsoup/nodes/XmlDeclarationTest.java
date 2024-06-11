// XmlDeclarationTest.java - Test Case 1

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.XmlDeclaration;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expected = "#declaration";
        String actual = xmlDeclaration.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testXmlDeclarationNameWithProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
        String expected = "#declaration";
        String actual = xmlDeclaration.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testXmlDeclarationGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expected = "xml";
        String actual = xmlDeclaration.getWholeDeclaration();
        assertEquals(expected, actual);

        XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", null, false);
        xmlDeclarationWithVersion.attributes().put("version", "1.0");
        String expectedVersion = "xml version=\"1.0\"";
        String actualVersion = xmlDeclarationWithVersion.getWholeDeclaration();
        assertEquals(expectedVersion, actualVersion);

        XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationWithEncoding.attributes().put("encoding", "UTF-8");
        String expectedEncoding = "xml version=\"1.0\" encoding=\"UTF-8\"";
        String actualEncoding = xmlDeclarationWithEncoding.getWholeDeclaration();
        assertEquals(expectedEncoding, actualEncoding);

        XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", null, false);
        xmlDeclarationWithVersionAndEncoding.attributes().put("version", "1.0");
        xmlDeclarationWithVersionAndEncoding.attributes().put("encoding", "UTF-8");
        String expectedVersionAndEncoding = "xml version=\"1.0\" encoding=\"UTF-8\"";
        String actualVersionAndEncoding = xmlDeclarationWithVersionAndEncoding.getWholeDeclaration();
        assertEquals(expectedVersionAndEncoding, actualVersionAndEncoding);
    }
}