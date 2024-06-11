import org.jdom2.output.SAXOutputter;
import org.junit.Test;
import static org.junit.Assert.*;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertNotNull(saxOutputter);
        assertFalse(saxOutputter.isReportNamespaceDeclarations());
        assertFalse(saxOutputter.isReportDTDEvents());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
    }
}