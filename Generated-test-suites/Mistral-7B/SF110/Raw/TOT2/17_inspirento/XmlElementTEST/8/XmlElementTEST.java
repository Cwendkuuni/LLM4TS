import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testXmlElementEmptyConstructor() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getName());
        assertNull(xmlElement.getData());
        assertTrue(xmlElement.getAttributes().isEmpty());
        assertTrue(xmlElement.getSubElements().isEmpty());
    }
}