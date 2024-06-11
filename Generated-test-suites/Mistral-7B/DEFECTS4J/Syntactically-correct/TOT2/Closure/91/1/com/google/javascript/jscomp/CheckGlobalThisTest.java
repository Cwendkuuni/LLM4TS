import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
        Node n = Node.newFunction("function foo() {}");
        Node parent = Node.newBlock();
        assertTrue(checker.shouldTraverse(null, n, parent));
    }

    // Add other test cases for edge cases and exceptions here.
}