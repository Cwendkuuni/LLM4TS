import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionFalse() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration.outerHtmlHead(new StringBuilder(), 0, null);
        assertEquals("<?!xml>\n", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndProcessingInstructionTrue() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration.outerHtmlHead(new StringBuilder(), 0, null);
        assertEquals("<?xml>\n", xmlDeclaration.toString());
    }

    // Add test cases for other methods and edge cases here
}