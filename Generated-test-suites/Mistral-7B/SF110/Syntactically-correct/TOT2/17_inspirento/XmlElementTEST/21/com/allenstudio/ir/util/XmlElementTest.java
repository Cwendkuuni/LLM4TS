import static org.junit.Assert.assertEquals;
import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {
    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        String attributeName = "test";
        String attributeValue = "value";
        Object returnValue = xmlElement.addAttribute(attributeName, attributeValue);
        assertEquals(null, returnValue);
        assertEquals(attributeName, xmlElement.getAttribute(attributeName));
        assertEquals(attributeValue, xmlElement.getAttribute(attributeName, null));
        assertEquals(1, xmlElement.getAttributes().size());
    }
}