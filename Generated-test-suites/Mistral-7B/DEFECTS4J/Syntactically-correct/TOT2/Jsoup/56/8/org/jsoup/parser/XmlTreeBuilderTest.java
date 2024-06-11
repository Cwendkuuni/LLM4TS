import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlInput = "<root><child>Hello World</child></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "");
        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.nodeName());
        Child child = root.child(0);
        assertEquals("child", child.nodeName());
        Text childText = child.child(0);
        assertEquals("Hello World", childText.text());
    }
}