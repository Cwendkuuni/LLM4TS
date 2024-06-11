import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class POJONodeTest {

    @Test
    public void testgetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }
}