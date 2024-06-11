import static org.junit.Assert.*;
import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("key1", "value1");
        String result = xmlElement.getAttribute("key1");
        assertEquals("value1", result);
    }
}