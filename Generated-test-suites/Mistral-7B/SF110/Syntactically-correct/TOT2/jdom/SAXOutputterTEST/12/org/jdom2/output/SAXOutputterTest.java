import org.junit.Test;
import org.jdom2.DocumentBuilderFactory;
import org.jdom2.Document;
import org.jdom2.output.SAXOutputter;
import static org.junit.Assert.assertNotNull;

public class SAXOutputterTest {

    @Test
    public void testSAXOutputterDefaultConstructor() {
        SAXOutputter outputter = new SAXOutputter();
        assertNotNull(outputter.getContentHandler());
    }
}