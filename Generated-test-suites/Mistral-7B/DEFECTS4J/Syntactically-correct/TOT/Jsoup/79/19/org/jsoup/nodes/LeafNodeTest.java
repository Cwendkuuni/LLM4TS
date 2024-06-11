import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode node1 = new Elements.SingleElement("<div></div>").first();
        assertFalse(node1.hasAttributes());

        LeafNode node2 = new Elements.SingleElement("<div id='test'></div>").first();
        assertTrue(node2.hasAttributes());
    }
}