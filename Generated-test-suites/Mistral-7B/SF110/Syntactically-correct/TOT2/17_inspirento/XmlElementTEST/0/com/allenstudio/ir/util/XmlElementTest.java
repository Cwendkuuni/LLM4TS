import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        String key = "testKey";
        String value = "testValue";
        xmlElement.addAttribute(key, value);
        assertEquals(value, xmlElement.getAttribute(key));
    }
}