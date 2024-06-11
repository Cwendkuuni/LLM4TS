import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    // Given
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node rootNode = Node.objectLiteral();

    // When
    checkSideEffects.process(null, rootNode);

    // Then
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Basic functionality test
    Node node1 = Node.stringLiteral("This is a test string");
    Node node2 = Node.name("console");
    Node node3 = Node.member(node2, "log");
    Node node4 = Node.call(node3, Node.array(node1));
    rootNode.addChildToBack(node4);
    checkSideEffects.process(null, rootNode);
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Edge case: empty statement
    Node node5 = Node.emptyStatement();
    rootNode.addChildToBack(node5);
    checkSideEffects.process(null, rootNode);
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Edge case: statement with side effect in if condition
    Node node6 = Node.ifStatement(
        Node.boolLiteral(true),
        Node.assignment(Node.name("x"), Node.numberLiteral(5)),
        Node.emptyStatement());
    rootNode.addChildToBack(node6);
    checkSideEffects.process(null, rootNode);
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Exception scenario: divide by zero
    Node node7 = Node.assignment(Node.name("x"), Node.numberLiteral(10));
    Node node8 = Node.assignment(Node.name("y"), Node.numberLiteral(0));
    Node node9 = Node.assignment(Node.name("z"), Node.binaryOp(
        Node.token(Token.DIVIDE), Node.name("x"), Node.name("y")));
    rootNode.addChildrenToBack(node7, node8, node9);
    try {
      checkSideEffects.process(null, rootNode);
    } catch (IllegalArgumentException e) {
      // Expected
    }
    assertEquals(0, checkSideEffects.problemNodes.size());
  }
}