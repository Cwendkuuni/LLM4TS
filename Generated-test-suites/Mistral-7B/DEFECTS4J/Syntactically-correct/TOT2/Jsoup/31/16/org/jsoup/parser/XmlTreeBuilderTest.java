import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFinder;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", null, null);
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
    }
}