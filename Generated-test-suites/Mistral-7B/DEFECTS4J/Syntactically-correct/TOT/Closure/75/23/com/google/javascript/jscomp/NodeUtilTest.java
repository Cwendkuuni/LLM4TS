import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createFor;
import static com.google.javascript.rhino.NodeFactory.createLabel;
import static com.google.javascript.rhino.NodeFactory.createScript;

import org.junit.Test;

import com.google.javascript.jscomp.NodeUtil;
import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createFor(Token.FOR, null, createLabel("label"), null, null, null, null);
    Node notForInNode = createFunction(Token.FUNCTION, null, null, null, null);

    assertFalse(NodeUtil.isForIn(notForInNode));
    assertTrue(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node node = createLabel("label");
    Node scriptNode = createScript(null, null, null);
    Node blockNode = createBlock(null, null, null);

    assertFalse(NodeUtil.isStatement(node));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createFunction(Token.FUNCTION, "foo", null, null, null);
    Node statementFunctionNode = createFunction(Token.FUNCTION, "bar", null, null, createBlock(null, null, null));

    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isFunctionDeclaration(statementFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclaration = createFunction(Token.FUNCTION, "foo", null, null, null);
    Node scriptNode = createScript(null, null, functionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getFirstChild()));
  }
}