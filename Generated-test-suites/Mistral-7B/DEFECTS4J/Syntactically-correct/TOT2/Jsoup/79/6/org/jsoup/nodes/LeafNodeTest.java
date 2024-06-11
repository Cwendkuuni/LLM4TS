import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode();
        leafNode.value = "someValue";

        boolean result = leafNode.hasAttributes();
        assertEquals(false, result);

        leafNode.value = new Attributes();
        result = leafNode.hasAttributes();
        assertEquals(true, result);
    }
}