import org.jdom2.*;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class SAXOutputterTest {
    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertNotNull(saxOutputter.getContentHandler());
        assertNotNull(saxOutputter.getErrorHandler());
        assertNotNull(saxOutputter.getDTDHandler());
        assertNotNull(saxOutputter.getEntityResolver());
        assertNotNull(saxOutputter.getLexicalHandler());
        assertNotNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.isReportNamespaceDeclarations());
        assertTrue(saxOutputter.isReportDTDEvents());
        assertNotNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }
}