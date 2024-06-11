import org.junit.Test;
import org.jdom2.Content;
import org.jdom2.JDOMFactory;
import org.jdom2.input.sax.SAXBuilder;
import org.xml.sax.SAXException;

public class JDOMResultTest {

    @Test
    public void testSetResult() throws SAXException {
        JDOMResult jdomResult = new JDOMResult();
        List<Content> contentList = new ArrayList<>();
        Content content = new Element("test");
        contentList.add(content);

        jdomResult.setResult(contentList);
        List<Content> result = jdomResult.getResult();

        assertNotNull(result);
        assertEquals(contentList, result);
    }
}