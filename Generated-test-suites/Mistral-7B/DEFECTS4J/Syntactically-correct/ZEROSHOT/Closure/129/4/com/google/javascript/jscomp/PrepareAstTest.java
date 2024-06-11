import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.PrepareAst.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

import org.junit.Test;

/**
 * Test class for PrepareAst class.
 */
public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = new Compiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = new Node(Token.ROOT);
    Node ifNode = new Node(Token.IF);
    Node ifCondition = new Node(Token.BOOL_LITERAL, true);
    Node ifBody = new Node(Token.NUMBER_LITERAL, 1);
    ifNode.addChildrenToFront(ifCondition, ifBody);
    root.addChildrenToFront(ifNode);

    prepareAst.process(null, root);

    Node block = root.getFirstChild();
    Node controlStructure = block.getNext();
    assertEquals(NodeType.BLOCK, controlStructure.getType());

    Node ifConditionNode = controlStructure.getFirstChild();
    assertEquals(NodeType.IF, ifConditionNode.getType());
    assertEquals(ifCondition, ifConditionNode.getFirstChild());
    assertEquals(ifBody, ifConditionNode.getNext());
  }

  @Test
  public void testNormalizeBlocks() {
    AbstractCompiler compiler = new Compiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = new Node(Token.ROOT);
    Node ifNode = new Node(Token.IF);
    Node ifCondition = new Node(Token.BOOL_LITERAL, true);
    Node ifBody = new Node(Token.NUMBER_LITERAL, 1);
    ifNode.addChildrenToFront(ifCondition, ifBody);
    root.addChildrenToFront(ifNode);

    prepareAst.process(null, root);

    Node block = root.getFirstChild();
    Node controlStructure = block.getNext();
    Node controlStructureCodeBlock = controlStructure.getFirstChild();

    assertEquals(NodeType.BLOCK, controlStructureCodeBlock.getType());
  }

  @Test(expected = AssertionError.class)
  public void testCheckOnlyNormalizeNodeTypes() {
    AbstractCompiler compiler = new Compiler();
    PrepareAst prepareAst = new PrepareAst(compiler, true);

    Node root = new Node(Token.ROOT);
    Node ifNode = new Node(Token.IF);
    Node ifCondition = new Node(Token.BOOL_LITERAL, true);
    Node ifBody = new Node(Token.NUMBER_LITERAL, 1);
    ifNode.addChildrenToFront(ifCondition, ifBody);
    root.addChildrenToFront(ifNode);

    prepareAst.process(null, root);
  }

  @Test
  public void testAnnotateCalls() {
    AbstractCompiler compiler = new Compiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = new Node(Token.ROOT);
    Node callNode = new Node(Token.CALL, null, new Node(Token.NAME, "eval"));
    root.addChildrenToFront(callNode);

    prepareAst.process(null, root);

    Node directEvalNode = callNode.getFirstChild();
    assertTrue(directEvalNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotateDispatchers() {
    AbstractCompiler compiler = new Compiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = new Node(Token.ROOT);
    Node assignNode = new Node(Token.ASSIGN);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildrenToFront(new Node(Token.NAME, "dispatch"));
    assignNode.addChildrenToFront(functionNode, new Node(Token.NUMBER_LITERAL, 1));
    root.addChildrenToFront(assignNode);

    Node dispatcherNode = functionNode.getFirstChild();
    Node jsDocInfo = dispatcherNode.getJSDocInfo();

    prepareAst.process(null, root);

    assertTrue(dispatcherNode.getBooleanProp(Node.IS_DISPATCHER));
    assertEquals(jsDocInfo, assignNode.getJSDocInfo());
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    AbstractCompiler compiler = new Compiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = new Node(Token.ROOT);
    Node objectLiteralNode = new Node(Token.OBJECT_LITERAL);
    Node keyNode = new Node(Token.NAME, "a");
    Node annotatedValueNode = new Node(Token.FUNCTION);
    Node annotatedValueFunctionNode = new Node(Token.FUNCTION);
    annotatedValueFunctionNode.addChildrenToFront(new Node(Token.NAME, "value"));
    annotatedValueNode.addChildrenToFront(annotatedValueFunctionNode);

    Node valueNode = new Node(Token.OBJECT_LITERAL);
    Node innerKeyNode = new Node(Token.NAME, "b");
    Node innerValueNode = new Node(Token.NUMBER_LITERAL, 2);
    valueNode.addChildrenToFront(innerKeyNode, innerValueNode);

    objectLiteralNode.addChildrenToFront(keyNode, valueNode);
    root.addChildrenToFront(objectLiteralNode);

    prepareAst.process(null, root);

    Node keyNodeInValue = valueNode.getFirstChild();
    Node jsDocInfo = keyNodeInValue.getJSDocInfo();

    assertEquals(jsDocInfo, keyNode.getJSDocInfo());
  }
}