import org.jdom2.output.SAXOutputter;
import org.junit.Test;
import static org.junit.Assert.*;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertNotNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getDTDHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertTrue(saxOutputter.getReportDTDEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }
}