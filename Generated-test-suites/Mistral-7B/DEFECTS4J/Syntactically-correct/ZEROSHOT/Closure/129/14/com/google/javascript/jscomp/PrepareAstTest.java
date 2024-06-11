import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isControlStructure;
import static com.google.javascript.jscomp.NodeUtil.isControlStructureCodeBlock;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal.Callback;

public class PrepareAstTest {

  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  @org.junit.Test
  public void testNormalizeNodeTypes() {
    Node root = new TestSourceFile("test.js")
        .parse()
        .getRootNode();

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Node controlNode = root.getFirstChild();
    Node controlBlock = controlNode.getFirstChild();

    // Control structures should have a block as their first child.
    assertTrue(isControlStructure(controlNode));
    assertTrue(isControlStructureCodeBlock(controlNode, controlBlock));

    // Control structures should not be empty.
    assertFalse(controlBlock.isEmpty());

    // Check the tree after normalization.
    Node normalizedControlNode = prepareAst.compiler.getRootNode();
    Node normalizedControlBlock = normalizedControlNode.getFirstChild();

    // Control structures should have a block as their first child.
    assertTrue(isControlStructure(normalizedControlNode));
    assertTrue(isControlStructureCodeBlock(normalizedControlNode, normalizedControlBlock));

    // Control structures should not be empty.
    assertFalse(normalizedControlBlock.isEmpty());
  }

  @org.junit.Test
  public void testPrepareAnnotations() {
    Node root = new TestSourceFile("test.js")
        .parse()
        .getRootNode();

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    // Check the annotations on the root node.
    assertNull(root.getJSDocInfo());

    // Check the annotations on a call node.
    Node callNode = root.getFirstChild().getFirstChild();
    assertNull(callNode.getJSDocInfo());

    // Check the annotations on a function node.
    Node functionNode = root.getFirstChild().getNext();
    assertNull(functionNode.getJSDocInfo());

    Node dispatcherNode = functionNode.getFirstChild();
    assertTrue(dispatcherNode.isAssign());
    assertTrue(dispatcherNode.getLastChild().isFunction());
    assertTrue(dispatcherNode.getLastChild().getJSDocInfo() != null);

    // Check the annotations on an object literal node.
    Node objectLiteralNode = root.getFirstChild().getNext();
    assertNotNull(objectLiteralNode.getJSDocInfo());

    // Check the annotations on an object literal key value node.
    Node keyValueNode = objectLiteralNode.getFirstChild().getFirstChild();
    assertNotNull(keyValueNode.getJSDocInfo());

    // Check the annotations on a function literal node.
    Node functionLiteralNode = objectLiteralNode.getFirstChild().getNext();
    assertNotNull(functionLiteralNode.getJSDocInfo());
  }

  @org.junit.Test
  public void testFreeCalls() {
    Node root = new TestSourceFile("test.js")
        .parse()
        .getRootNode();

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    // Check that a normal call is not marked as free.
    Node normalCallNode = root.getFirstChild().getNext();
    assertFalse(normalCallNode.getBooleanProp(Node.FREE_CALL));

    // Check that a call with explicit "this" is not marked as free.
    Node explicitThisCallNode = root.getFirstChild().getNext().getNext();
    assertFalse(explicitThisCallNode.getBooleanProp(Node.FREE_CALL));

    // Check that a call without explicit "this" is marked as free.
    Node freeCallNode = root.getFirstChild().getNext().getNext().getNext();
    assertTrue(freeCallNode.getBooleanProp(Node.FREE_CALL));
  }

  @org.junit.Test
  public void testDirectEval() {
    Node root = new TestSourceFile("test.js")
        .parse()
        .getRootNode();

    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);

    // Check that a normal call to eval is not marked as direct eval.
    Node evalCallNode = root.getFirstChild().getNext();
    assertFalse(evalCallNode.getBooleanProp(Node.DIRECT_EVAL));

    // Check that an explicit call to eval is marked as direct eval.
    Node directEvalNode = root.getFirstChild().getNext().getNext();
    assertTrue(directEvalNode.getBooleanProp(Node.DIRECT_EVAL));

    // Check that an indirect call to eval is not marked as direct eval.
    Node indirectEvalNode = root.getFirstChild().getNext().getNext().getNext();
    assertFalse(indirectEvalNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @org.junit.Test
  public void testPrepareAnnotationsTraversal() {
    Node root = new TestSourceFile("test.js")
        .parse()
        .getRootNode();

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    NodeTraversal.traverse(
        compiler, root, new PrepareAnnotations(), true, null);

    // Check the annotations on the root node.
    assertNull(root.getJSDocInfo());

    // Check the annotations on a call node.
    Node callNode = root.getFirstChild().getFirstChild();
    assertNull(callNode.getJSDocInfo());

    // Check the annotations on a function node.
    Node functionNode = root.getFirstChild().getNext();
    assertNull(functionNode.getJSDocInfo());

    Node dispatcherNode = functionNode.getFirstChild();
    assertTrue(dispatcherNode.isAssign());
    assertTrue(dispatcherNode.getLastChild().isFunction());
    assertTrue(dispatcherNode.getLastChild().getJSDocInfo() != null);

    // Check the annotations on an object literal node.
    Node objectLiteralNode = root.getFirstChild().getNext();
    assertNotNull(objectLiteralNode.getJSDocInfo());

    // Check the annotations on an object literal key value node.
    Node keyValueNode = objectLiteralNode.getFirstChild().getFirstChild();
    assertNotNull(keyValueNode.getJSDocInfo());

    // Check the annotations on a function literal node.
    Node functionLiteralNode = objectLiteralNode.getFirstChild().getNext();
    assertNull(functionLiteralNode.getJSDocInfo());

    // Check that annotations were copied correctly.
    String callName = callNode.getFirstChild().getString();
    assertEquals("callName", callNode.getLastChild().getJSDocInfo().getName());
    assertEquals(callName, callNode.getLastChild().getJSDocInfo().getDescription());

    String functionName = functionNode.getString();
    assertEquals("functionName", functionNode.getJSDocInfo().getName());
    assertEquals(functionName, functionNode.getJSDocInfo().getDescription());

    String dispatcherName = dispatcherNode.getLastChild().getString();
    assertEquals("dispatcherName", dispatcherNode.getLastChild().getJSDocInfo().getName());
    assertEquals(dispatcherName, dispatcherNode.getLastChild().getJSDocInfo().getDescription());

    String objectLiteralName = objectLiteralNode.getJSDocInfo().getName();
    assertEquals("objectLiteralName", objectLiteralNode.getJSDocInfo().getDescription());

    String keyValueName = keyValueNode.getJSDocInfo().getName();
    assertEquals("keyValueName", keyValueNode.getJSDocInfo().getDescription());

    String functionLiteralName = functionLiteralNode.getJSDocInfo().getName();
    assertEquals("functionLiteralName", functionLiteralNode.getJSDocInfo().getDescription());
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testReportChangeWithCheckOnly() {
    Node root = new TestSourceFile("test.js")
        .parse()
        .getRootNode();

    PrepareAst prepareAst = new PrepareAst(compiler, true);
    prepareAst.process(null, root);

    // This statement should throw an exception because we are running in check
    // only mode.
    prepareAst.reportChange();
  }
}

class TestSourceFile {
  private Node node;

  TestSourceFile(String fileName) {
    node = new IR().parse(fileName, null, null).getRootNode();
  }

  Node getRootNode() {
    return node;
  }

  Node getFirstChild(Node node) {
    return node.getFirstChild();
  }

  Node getNext(Node node) {
    Node next = node.getNext();
    if (next == null) {
      throw new IllegalStateException();
    }
    return next;
  }

  String getString(Node node) {
    return node.toString();
  }
}