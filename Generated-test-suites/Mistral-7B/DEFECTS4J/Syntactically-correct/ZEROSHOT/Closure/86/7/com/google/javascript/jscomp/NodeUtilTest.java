import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.parser.bare.BareTokenFactory;
import com.google.javascript.rhino.NodeFactory;

import java.util.Arrays;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class NodeUtilTest {

  private NodeFactory nodeFactory = Mockito.mock(NodeFactory.class);
  private BareTokenFactory tokenFactory = Mockito.mock(BareTokenFactory.class);

  @Test
  public void testIsForIn() {
    Node node = createNode(Token.FOR, 0, 0, Arrays.asList(createNode(Token.IDENT, "for"), createNode(Token.LPAREN), createNode(Token.RPAREN), createNode(Token.LPAREN), createNode(Token.RPAREN), createNode(Token.SEMICOLON), createNode(Token.STMT), createNode(Token.RPAREN)), Arrays.asList());
    assertTrue(NodeUtil.isForIn(node));

    node = createNode(Token.FOR, 0, 0, Arrays.asList(createNode(Token.IDENT, "for"), createNode(Token.LPAREN), createNode(Token.RPAREN), createNode(Token.LPAREN), createNode(Token.RPAREN), createNode(Token.SEMICOLON), createNode(Token.STMT), createNode(Token.RPAREN), createNode(Token.SEMICOLON), createNode(Token.STMT), createNode(Token.RPAREN)), Arrays.asList());
    assertFalse(NodeUtil.isForIn(node));

    node = createNode(Token.IDENT, "notForin", null, null);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createNode(Token.SCRIPT, 0, 0, Arrays.asList(createNode(Token.IDENT, "x"), createNode(Token.ASSIGN, null, createNode(Token.NUM, 1), node)), Arrays.asList());
    assertTrue(NodeUtil.isStatement(node));

    node = createNode(Token.ASSIGN, null, createNode(Token.IDENT, "x"), createNode(Token.NUM, 1));
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNode(Token.FUNCTION, 0, 0, Arrays.asList(createNode(Token.IDENT, "foo"), createNode(Token.LPAREN), createNode(Token.RPAREN), Arrays.asList(createNode(Token.RPAREN), createNode(Token.RPAREN))), Arrays.asList());
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = createNode(Token.FUNCTION, 0, 0, Arrays.asList(createNode(Token.IDENT, "foo"), createNode(Token.LPAREN), createNode(Token.RPAREN), Arrays.asList(createNode(Token.IDENT, "x"), createNode(Token.SEMICOLON))), Arrays.asList());
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(Token.SCRIPT, 0, 0, Arrays.asList(
        createNode(Token.FUNCTION, 0, 0, Arrays.asList(createNode(Token.IDENT, "foo"), createNode(Token.LPAREN), createNode(Token.RPAREN), Arrays.asList(createNode(Token.RPAREN), createNode(Token.RPAREN))), Arrays.asList()),
        createNode(Token.IDENT, "bar", null, null),
        nodeFactory.parse("function baz() { }")
    ), Arrays.asList());

    Node functionNode = scriptNode.getChild(0);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node innerFunctionNode = functionNode.getChild(2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(innerFunctionNode));
  }

  private Node createNode(int type, int line, int column, List<Node> children, List<List<Node>> grandChildren) {
    return nodeFactory.parse(String.format("function() { return %s(%d, %d); }", NodeUtil.class.getName(), type, line), tokenFactory, null, children, grandChildren);
  }

  private Node createNode(int type, int line, int column, Node... children) {
    return createNode(type, line, column, Arrays.asList(children), null);
  }
}