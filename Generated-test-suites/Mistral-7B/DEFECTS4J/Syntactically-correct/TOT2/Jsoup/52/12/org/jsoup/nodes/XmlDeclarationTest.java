import org.junit.Test;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;

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

        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        assertEquals("<declaration>\n", accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullName() {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationWithNullBaseUri() {
        String name = "xml";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, isProcessingInstruction);
    }
}