import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndIsProcessingInstructionFalse() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("xml version=\"\" encoding=\"\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWhenNameIsNull() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "baseUri", false);
        // This test will throw a NullPointerException since name is marked as not null
    }

    @Test
    public void testXmlDeclarationWithNameXmlAndBaseUriAndIsProcessingInstructionTrue() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml version=\"\" encoding=\"\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testToStringMethodReturnsCorrectXmlDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("<declaration xml=\"xml\" version=\"\" encoding=\"\">", xmlDeclaration.toString());
    }
}