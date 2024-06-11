import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("key", "value");
        String value = xmlElement.getAttribute("key");
        assertNotNull(value);
        assertEquals("value", value);
    }
}