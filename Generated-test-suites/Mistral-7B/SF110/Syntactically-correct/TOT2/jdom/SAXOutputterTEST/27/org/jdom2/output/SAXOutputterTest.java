import org.jdom2.output.SAXOutputter;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class SAXOutputterTest {
    @Test
    public void testDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();

        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        assertFalse(saxOutputter.getReportDTDEvents());
        assertSame(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertSame(Format.getRawFormat(), saxOutputter.getFormat());
        assertNull(saxOutputter.getContentHandler());
        assertNull(saxOutputter.getErrorHandler());
        assertNull(saxOutputter.getElementByIdHandler());
        assertNull(saxOutputter.getEntityResolver());
        assertNull(saxOutputter.getLexicalHandler());
        assertNull(saxOutputter.getDeclHandler());
    }
}