import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

public class LeafNodeTest {
    @Test
    public void testHasAttributes() {
        LeafNode node = new Elements.SingleTagElement("div").text("Test");
        // Test with empty node
        assertFalse(node.hasAttributes());

        // Test with node having attributes
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        node.attributes().put("class", "testClass");
        node.coreValue("Test Value");
        assertTrue(node.hasAttributes());
    }
}