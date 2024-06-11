import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("root");
        xmlElement.addAttribute("attribute1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attribute1"));

        xmlElement.addAttribute("attribute2", "value2");
        assertEquals("value2", xmlElement.getAttribute("attribute2"));

        xmlElement.addAttribute("attribute1", "value3");
        assertEquals("value3", xmlElement.getAttribute("attribute1"));

        assertNull(xmlElement.getAttribute("nonExistentAttribute"));
    }
}