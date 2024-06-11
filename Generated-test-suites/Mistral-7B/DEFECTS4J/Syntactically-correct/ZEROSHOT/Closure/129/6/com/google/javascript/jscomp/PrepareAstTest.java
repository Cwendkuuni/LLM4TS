import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private PrepareAst prepareAst;
  private PrepareAnnotations prepareAnnotations;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    prepareAst = new PrepareAst(compiler);
    prepareAnnotations = new PrepareAnnotations();
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node root = createNode("root");
    Node child1 = createNode("child1");
    Node child2 = createNode("child2");
    Node grandChild = createNode("grandChild");

    root.addChildrenToFront(child1, child2);
    child1.addChildrenToFront(grandChild);

    prepareAst.process(null, root);

    assertEquals(Node.BLOCK, root.getType());
    assertEquals(Node.BLOCK, child1.getType());
    assertEquals(Node.BLOCK, child2.getType());
    assertEquals(Node.EMPTY, grandChild.getType());
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = createNode("root");
    Node ifStatement = createIfStatement();

    root.addChildrenToFront(ifStatement);

    prepareAst.process(null, root);

    Node block = ifStatement.getFirstChild();
    assertEquals(Node.BLOCK, block.getType());
    assertNull(block.getNext());
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = createObjectLiteral("root", createFunctionLiteral("func"));

    prepareAst.process(null, root);

    Node func = root.getFirstChild();
    assertEquals("func", func.getString());
    assertNotNull(func.getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotationsWithExplicitThis() {
    Node root = createObjectLiteral("root", createFunctionLiteralWithThis("thisArg"));

    prepareAst.process(null, root);

    Node func = root.getFirstChild();
    assertEquals("thisArg", func.getFirstChild().getString());
    assertNotNull(func.getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotationsWithJavaDispatch() {
    Node root = createObjectLiteral("root", createFunctionLiteral("func"), createObjectLiteral("obj"));

    Node func = root.getFirstChild().getNext();
    Node obj = root.getLastChild();

    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.getJSDocInfoFromJSDocComment(obj, 0)).thenReturn(new JsDocInfo());

    prepareAst.process(null, root);

    assertTrue(func.isDispatcher());
  }

  private Node createNode(String name) {
    return nodeFactory.createNode(Token.NAME, name);
  }

  private Node createIfStatement() {
    Node ifStatement = nodeFactory.createNode(Token.IF);
    Node condition = nodeFactory.createNode(Token.NAME, "condition");
    Node block = nodeFactory.createBlock();

    ifStatement.addChildrenToFront(condition, block);
    return ifStatement;
  }

  private Node createFunctionLiteral(String name) {
    Node function = nodeFactory.createNode(Token.FUNCTION);
    Node body = nodeFactory.createBlock();
    Node returnStatement = nodeFactory.createReturnStatement(createNode("returnValue"));

    function.addChildrenToFront(nodeFactory.createName("function", nodeFactory.createName(name)), body);
    body.addChildrenToFront(returnStatement);

    return function;
  }

  private Node createFunctionLiteralWithThis(String thisArg) {
    Node thisNode = nodeFactory.createName("this");
    Node function = nodeFactory.createNode(Token.FUNCTION);
    Node body = nodeFactory.createBlock();
    Node returnStatement = nodeFactory.createReturnStatement(createNode("returnValue"));

    function.addChildrenToFront(thisNode, thisNode);
    function.addChildrenToFront(nodeFactory.createName("function"), body);
    body.addChildrenToFront(returnStatement);

    Node arguments = createArgumentsNode(thisArg);
    function.addChildrenToFront(arguments);

    return function;
  }

  private Node createArgumentsNode(String arg) {
    Node args = nodeFactory.createNode(Token.NEW_ARRAY, Token.INT_LITERAL, 1);
    Node argNode = nodeFactory.createNode(Token.NAME, arg);

    args.addChildrenToFront(argNode);
    return args;
  }

  private Node createObjectLiteral(String name, Node... children) {
    Node objectLit = nodeFactory.createObjectLiteral(name);
    for (Node child : children) {
      objectLit.addChildrenToFront(child);
    }

    return objectLit;
  }

  private boolean isDispatcher(Node node) {
    return node.isFunction() && node.getBooleanProp(Node.IS_DISPATCHER);
  }
}