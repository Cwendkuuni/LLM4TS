import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    rootNode = NodeUtil.parseJsonString("{\"x\": 1}");
  }

  @After
  public void teardown() {
    compiler = null;
  }

  @Test
  public void testBasicFunctionality() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, rootNode);
    assertFalse("Unreachable code should not be present.", NodeUtil.containsNode(rootNode, NodeUtil.getNameNode("alert")));
  }

  @Test
  public void testRemoveNoOpStatement() {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, rootNode);
    assertTrue("No op statement should be removed.", NodeUtil.containsNode(rootNode, NodeUtil.getNameNode("true")));
  }

  @Test
  public void testRemoveUnconditionalBranch() {
    Node statement = NodeUtil.makeStatement(Token.BREAK, null, null);
    Node breakNode = NodeUtil.makeStatement(Token.BREAK, null, statement);
    Node loopNode = NodeUtil.makeStatement(Token.DO, null, NodeUtil.makeStatement(Token.IF, null, NodeUtil.makeLiteral(Token.FALSE), NodeUtil.makeStatement(Token.BLOCK, null, NodeUtil.makeStatement(Token.RETURN, null, NodeUtil.makeLiteral(1)), breakNode)));
    rootNode.setFirstChild(loopNode);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, rootNode);

    Node expected = NodeUtil.makeStatement(Token.DO, null, NodeUtil.makeStatement(Token.IF, null, NodeUtil.makeLiteral(Token.FALSE), NodeUtil.makeStatement(Token.RETURN, null, NodeUtil.makeLiteral(1))));
    assertTrue("Unconditional branch should be removed.", NodeUtil.isIdentical(loopNode, expected));
  }

  @Test(expected = AssertionError.class)
  public void testRemoveUnconditionalBranchWithConditional() {
    Node statement = NodeUtil.makeStatement(Token.BREAK, null, null);
    Node ifNode = NodeUtil.makeStatement(Token.IF, null, NodeUtil.makeLiteral(Token.TRUE), NodeUtil.makeStatement(Token.BLOCK, null, statement));
    Node breakNode = NodeUtil.makeStatement(Token.BREAK, null, NodeUtil.makeLiteral(1));
    Node loopNode = NodeUtil.makeStatement(Token.DO, null, NodeUtil.makeStatement(Token.IF, null, NodeUtil.makeLiteral(Token.FALSE), ifNode), breakNode);
    rootNode.setFirstChild(loopNode);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, rootNode);
  }
}