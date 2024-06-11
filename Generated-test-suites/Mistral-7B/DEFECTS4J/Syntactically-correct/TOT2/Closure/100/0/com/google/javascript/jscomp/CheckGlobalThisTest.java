import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Checker checker = new Checker(new Compiler(), new CheckGlobalThis(null, WARNING));

    Node node = NodeFactory.createFunctionDeclaration("evil", null, null, null, false);
    Node body = NodeFactory.createExpressionStatement(NodeFactory.createAssignment(NodeFactory.createThis(), NodeFactory.createName("useful")));
    node.setBody(body);

    NodeTraversal traversal = new NodeTraversal(checker, null);
    traversal.traverse(node);

    assertTrue(checker.getDiagnostics().isEmpty());

    node.setBody(NodeFactory.createStatementList(NodeFactory.createExpressionStatement(NodeFactory.createAssignment(NodeFactory.createName("a"), NodeFactory.createThis()))));

    traversal.traverse(node);

    assertFalse(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeProperty() {
    Checker checker = new Checker(new Compiler(), new CheckGlobalThis(null, WARNING));

    Node node = NodeFactory.createFunctionDeclaration("Foo", null, null, null, false);
    Node constructor = NodeFactory.createFunction("constructor", null, null, null, false);
    constructor.setBody(NodeFactory.createStatementList());
    node.setPrototype(constructor);

    Node body = NodeFactory.createStatementList(
        NodeFactory.createVariableDeclarationStatement("prototype", NodeFactory.createType("Object"), NodeFactory.createThis(), null),
        NodeFactory.createStatementList(NodeFactory.createExpressionStatement(NodeFactory.createAssignment(NodeFactory.createGetProperty(NodeFactory.createThis(), "prototype"), NodeFactory.createName("x"))),
            NodeFactory.createExpressionStatement(NodeFactory.createAssignment(NodeFactory.createGetProperty(NodeFactory.createGetProperty(NodeFactory.createThis(), "prototype"), "x"), NodeFactory.createNumberLiteral(42)))));

    node.setBody(body);

    NodeTraversal traversal = new NodeTraversal(checker, null);
    traversal.traverse(node);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThisWithThrowingException() {
    Checker checker = new Checker(new Compiler(), new CheckGlobalThis(null, WARNING));

    Node node = NodeFactory.createFunctionDeclaration("Foo", null, null, null, false);
    Node body = NodeFactory.createBlock(NodeFactory.createThrowStatement(NodeFactory.createStringLiteral("Error message")));
    node.setBody(body);

    NodeTraversal traversal = new NodeTraversal(checker, null);
    traversal.traverse(node);

    assertTrue(checker.getDiagnostics().isEmpty());

    node.setBody(NodeFactory.createStatementList(NodeFactory.createExpressionStatement(NodeFactory.createAssignment(NodeFactory.createThis(), NodeFactory.createName("x")))));

    traversal.traverse(node);

    throw new AssertionError("Test should have thrown exception");
  }
}