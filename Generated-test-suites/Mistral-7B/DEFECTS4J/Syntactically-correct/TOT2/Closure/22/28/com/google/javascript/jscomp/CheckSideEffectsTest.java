import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checker = new CheckSideEffects(null, level, false);

    Node root = createNode("var x = 1; x = foo();");
    checker.process(null, root);
    assertTrue(checker.problemNodes.isEmpty());

    root = createNode("var x = 1; x = 2;");
    checker.process(null, root);
    assertTrue(checker.problemNodes.size() == 1);
    Node problemNode = checker.problemNodes.get(0);
    assertTrue(problemNode.isAssign());
    assertTrue(problemNode.getFirstChild().isName());
    assertTrue(problemNode.getNext().isName());
    assertTrue(problemNode.getNext().getString().equals("x"));

    root = createNode("var x = 1; x += 1;");
    checker.process(null, root);
    assertTrue(checker.problemNodes.isEmpty());

    root = createNode("if (condition) { var x = 1; x = foo(); }");
    checker.process(null, root);
    assertTrue(checker.problemNodes.isEmpty());

    root = createNode("try { var x = 1; x = foo(); } catch (e) {}");
    checker.process(null, root);
    assertTrue(checker.problemNodes.isEmpty());
  }

  private Node createNode(String code) {
    // Create a new JavaScript context and parse the code.
    // ...
    return rootNode;
  }
}