import org.junit.Test;
import org.jdom2.output.SAXOutputter;
import static org.junit.Assert.*;

public class SAXOutputterTest {
    @Test
    public void testSAXOutputterCreation() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertNotNull(saxOutputter);
        assertFalse(saxOutputter.getDeclareNamespaces());
        assertTrue(saxOutputter.getReportDtdEvents());
        assertNull(saxOutputter.getSAXOutputProcessor());
        assertNotNull(saxOutputter.getFormat());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }
}