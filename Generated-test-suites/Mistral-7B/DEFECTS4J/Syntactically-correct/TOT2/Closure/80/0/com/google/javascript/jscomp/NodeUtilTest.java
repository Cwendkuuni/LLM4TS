import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class IsForInTest {
    @Test
    public void testIsForIn() {
        Node forNode = new Node(Token.FOR);
        forNode.setChildCount(3);
        assertTrue(NodeUtil.isForIn(forNode));
    }
}