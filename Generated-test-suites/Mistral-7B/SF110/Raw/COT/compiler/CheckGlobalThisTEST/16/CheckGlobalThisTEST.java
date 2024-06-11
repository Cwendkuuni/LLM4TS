import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static com.google.javascript.jscomp.NodeTraversal.Kind.ASSIGN;
import static com.google.javascript.jscomp.NodeTraversal.Kind.FUNCTION;
import static com.google.javascript.jscomp.NodeTraversal.Kind.GET_PROP;
import static com.google.javascript.jscomp.NodeTraversal.Kind.NAME;
import static com.google.javascript.jscomp.NodeTraversal.Kind.SCRIPT;
import static com.google.javascript.rhino.Token.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode(NAME, "functionName");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseConstructorFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, "@constructor", null, null);
    Node parentNode = Node.newNameNode(NAME, "functionName");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, null, "@this", null);
    Node parentNode = Node.newNameNode(NAME, "functionName");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode(NAME, "functionName");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionInBlock() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, null, null, null);
    Node parentNode = Node.newBlockNode(SCRIPT, null);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionInScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode(NAME, "scriptName");

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionInPrototypeProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode(NAME, "prototype");
    Node prototypeNode = Node.newNameNode(NAME, "Prototype");
    Node objectLiteralNode = Node.newObjectLiteralNode(SCRIPT, new Node[0]);
    Node getPrototypePropNode = Node.newGetPropNode(GET_PROP, functionNode, prototypeNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node gramps = Node.newNameNode(NAME, "Object");
    Node lhs = Node.newNameNode(NAME, "obj");
    Node assignNode = Node.newAssignNode(ASSIGN, lhs, getPrototypePropNode);
    NodeTraversal.TraversalResult result = NodeTraversal.traverse(traversal, objectLiteralNode, gramps);
    Node parent = result.getParent();

    assertTrue(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverseFunctionInPrototypeSubproperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode(NAME, "prototype");
    Node prototypeNode = Node.newNameNode(NAME, "Prototype");
    Node objectLiteralNode = Node.newObjectLiteralNode(SCRIPT, new Node[0]);
    Node getPrototypePropNode = Node.newGetPropNode(GET_PROP, functionNode, prototypeNode);
    Node getPrototypeSubPropNode = Node.newGetPropNode(GET_PROP, getPrototypePropNode, Node.newStringLiteralNode(Token.STRING_LITERAL, "subproperty"));

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node gramps = Node.newNameNode(NAME, "Object");
    Node lhs = Node.newNameNode(NAME, "obj");
    Node assignNode = Node.newAssignNode(ASSIGN, lhs, getPrototypeSubPropNode);
    NodeTraversal.TraversalResult result = NodeTraversal.traverse(traversal, objectLiteralNode, gramps);
    Node parent = result.getParent();

    assertTrue(checker.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node thisNode = Node.newThisNode(NAME, null);
    Node assignNode = Node.newAssignNode(ASSIGN, Node.newNameNode(NAME, "lhs"), thisNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node parent = new Node[0][0];
    checker.visit(traversal, assignNode, parent);
    assertNotNull(compiler.getLastError());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node thisNode = Node.newThisNode(NAME, null);
    Node getNode = Node.newGetPropNode(GET_PROP, Node.newNameNode(NAME, "lhs"), thisNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node parent = new Node[0][0];
    checker.visit(traversal, getNode, parent);
    assertNotNull(compiler.getLastError());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node lhsNode = Node.newNameNode(NAME, "lhs");
    Node thisNode = Node.newThisNode(NAME, null);
    Node assignNode = Node.newAssignNode(ASSIGN, lhsNode, thisNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node parent = new Node[0][0];
    checker.visit(traversal, assignNode, parent);
    assertNull(compiler.getLastError());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfSimplePropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node lhsNode = Node.newNameNode(NAME, "lhs");
    Node thisNode = Node.newThisNode(NAME, null);
    Node getNode = Node.newGetPropNode(GET_PROP, lhsNode, thisNode);

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node parent = new Node[0][0];
    checker.visit(traversal, getNode, parent);
    assertNull(compiler.getLastError());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfComplexPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node lhsNode = Node.newNameNode(NAME, "lhs");
    Node thisNode = Node.newThisNode(NAME, null);
    Node getPrototypePropNode = Node.newGetPropNode(GET_PROP, lhsNode, Node.newNameNode(NAME, "prototype"));
    Node getNode = Node.newGetPropNode(GET_PROP, getPrototypePropNode, Node.newStringLiteralNode(Token.STRING_LITERAL, "subproperty"));

    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Node parent = new Node[0][0];
    checker.visit(traversal, getNode, parent);
    assertNull(compiler.getLastError());
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunctionNode(FUNCTION, null, null, null, null, null, null);
    Node parentNode = Node.newNameNode(NAME, "functionName");

    JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);
    assertNull(jsDocInfo);

    Node functionNodeWithJSDoc = Node.newFunctionNode(FUNCTION, null, null, null, null, "@functionName", null);
    jsDocInfo = checker.getFunctionJsDocInfo(functionNodeWithJSDoc);
    assertNotNull(jsDocInfo);
    assertEquals("functionName", jsDocInfo.getName());
  }

  private void assertEquals(String expected, String actual) {
    if (!expected.equals(actual)) {
      throw new AssertionError("Expected: " + expected + ", but was: " + actual);
    }
  }
}