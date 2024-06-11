import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

    @Test
    public void testgetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }
}