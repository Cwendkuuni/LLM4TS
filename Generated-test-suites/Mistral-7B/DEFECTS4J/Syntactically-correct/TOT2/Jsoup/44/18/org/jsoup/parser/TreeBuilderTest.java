import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

public class TreeBuilderTest {
  @Test
  public void testProcessStartTag() {
    TreeBuilder treeBuilder = new TreeBuilder() { // Anonymous inner class to override processStartTag method
        @Override
        boolean process(Token token) {
            return false;
        }

        @Override
        boolean processStartTag(String name) {
            Element element = new Element("test");
            Assert.assertNotNull(element);
            return true;
        }
    };

    treeBuilder.initialiseParse("test input", "baseUri", null);
    treeBuilder.processStartTag("test");
    Element currentElement = treeBuilder.currentElement();
    Assert.assertEquals("test", currentElement.tagName());
  }
}