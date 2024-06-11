import static org.junit.Assert.*;
import com.allenstudio.ir.util.XmlElement;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("attr1", "value1");
        String actual = xmlElement.getAttribute("attr1");
        assertEquals("value1", actual);
    }
}