import org.jdom2.output.SAXOutputter;
import org.junit.Test;
import static org.junit.Assert.*;

public class SAXOutputterTest {
    @Test
    public void testDefaultConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        assertFalse(saxOutputter.getDeclareNamespaces());
        assertTrue(saxOutputter.getReportDtdEvents());
        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, saxOutputter.getSAXOutputProcessor());
        assertEquals(Format.getRawFormat(), saxOutputter.getFormat());
    }
}