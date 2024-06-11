import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TernaryValue;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {
  private Compiler compiler;
  private MinimizeExitPoints minimizer;

  @Before
  public void setUp() {
    compiler = new Compiler();
    minimizer = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeExitPoints_EmptyCodeBlock() {
    Node root = Node.newBlock(null);
    minimizer.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testMinimizeExitPoints_SingleStatementBlock() {
    Node root = Node.newBlock(Node.newName("x"));
    root.addChildToBack(Node.newReturn(Node.newName("0")));
    minimizer.process(null, root);
    assertEquals(1, root.getChildCount());
    assertEquals(Node.TOKEN_RETURN, root.getLastChild().getType());
  }

  @Test
  public void testMinimizeExitPoints_SingleIfStatement() {
    Node root = Node.newBlock(Node.newName("x"));
    root.addChildToBack(Node.newIf(Node.newName("x"), Node.newBlock(Node.newName("y"))));
    minimizer.process(null, root);
    assertEquals(2, root.getChildCount());
    assertEquals(Node.TOKEN_IF, root.getChild(1).getType());
  }

  @Test
  public void testMinimizeExitPoints_MultipleStatementsInIfBlock() {
    Node root = Node.newBlock(Node.newName("x"));
    root.addChildToBack(
        Node.newIf(Node.newName("x"), Node.newBlock(Node.newName("y"), Node.newName("z"))));
    minimizer.process(null, root);
    assertEquals(2, root.getChildCount());
    assertEquals(Node.TOKEN_IF, root.getChild(1).getType());
  }

  @Test
  public void testMinimizeExitPoints_MultipleIfStatements() {
    Node root = Node.newBlock(Node.newName("x"));
    root.addChildToBack(
        Node.newIf(Node.newName("x"), Node.newBlock(Node.newName("y"))));
    root.addChildToBack(Node.newIf(Node.newName("!x"), Node.newBlock(Node.newName("z"))));
    minimizer.process(null, root);
    assertEquals(3, root.getChildCount());
    assertEquals(Node.TOKEN_IF, root.getChild(1).getType());
    assertEquals(Node.TOKEN_IF, root.getChild(2).getType());
  }

  @Test
  public void testMinimizeExitPoints_BreakStatement() {
    Node root = Node.newBlock(
        Node.newLabel("label1"),
        Node.newIf(Node.newName("true"), Node.newBlock(Node.newLabel("label1"))),
        Node.newBreak("label1")
    );
    minimizer.process(null, root);
    assertEquals(3, root.getChildCount());
    assertEquals(Node.TOKEN_IF, root.getChild(1).getType());
    assertEquals(Node.TOKEN_LABEL, root.getChild(2).getType());
    assertEquals(Node.TOKEN_BREAK, root.getChild(3).getType());
  }

  @Test
  public void testMinimizeExitPoints_ContinueStatement() {
    Node root = Node.newBlock(
        Node.newLabel("label1"),
        Node.newIf(Node.newName("true"), Node.newBlock(Node.newLabel("label1"))),
        Node.newContinue("label1")
    );
    minimizer.process(null, root);
    assertEquals(3, root.getChildCount());
    assertEquals(Node.TOKEN_IF, root.getChild(1).getType());
    assertEquals(Node.TOKEN_LABEL, root.getChild(2).getType());
    assertEquals(Node.TOKEN_CONTINUE, root.getChild(3).getType());
  }

  @Test
  public void testMinimizeExitPoints_ReturnStatement() {
    Node root = Node.newBlock(Node.newName("x"), Node.newReturn(Node.newName("y")));
    minimizer.process(null, root);
    assertEquals(2, root.getChildCount());
    assertEquals(Node.TOKEN_RETURN, root.getLastChild().getType());
  }

  @Test
  public void testMinimizeExitPoints_LabelStatement() {
    Node root = Node.newBlock(
        Node.newLabel("label1"), Node.newLabel("label1"), Node.newReturn(Node.newName("y"))) ;
    minimizer.process(null, root);
    assertEquals(3, root.getChildCount());
    assertEquals(Node.TOKEN_LABEL, root.getChild(0).getType());
    assertEquals(Node.TOKEN_LABEL, root.getChild(1).getType());
    assertEquals(Node.TOKEN_RETURN, root.getLastChild().getType());
  }

  @Test
  public void testMinimizeExitPoints_TryCatchBlock() {
    Node root = Node.newBlock(
        Node.newTry(
            Node.newBlock(Node.newName("x")),
            Node.newCatch(Node.newName("e"), Node.newBlock(Node.newName("y")))
        )
    );
    minimizer.process(null, root);
    assertEquals(2, root.getChildCount());
    assertEquals(Node.TOKEN_TRY, root.getChild(0).getType());
  }

  @Test
  public void testMinimizeExitPoints_TryCatchFinallyBlock() {
    Node root = Node.newBlock(
        Node.newTry(
            Node.newBlock(Node.newName("x")),
            Node.newCatch(Node.newName("e"), Node.newBlock(Node.newName("y"))),
            Node.newFinally(Node.newBlock(Node.newName("z")))
        )
    );
    minimizer.process(null, root);
    assertEquals(3, root.getChildCount());
    assertEquals(Node.TOKEN_TRY, root.getChild(0).getType());
  }

  @Test
  public void testMinimizeExitPoints_MultipleBlocksInTryCatch() {
    Node root = Node.newBlock(
        Node.newTry(
            Node.newBlock(Node.newName("x")),
            Node.newCatch(Node.newName("e1"), Node.newBlock(Node.newName("y1"))),
            Node.newCatch(Node.newName("e2"), Node.newBlock(Node.newName("y2")))
        )
    );
    minimizer.process(null, root);
    assertEquals(4, root.getChildCount());
    assertEquals(Node.TOKEN_TRY, root.getChild(0).getType());
  }

  @Test
  public void testMinimizeExitPoints_LabeledBreakStatement() {
    Node root = Node.newBlock(
        Node.newLabel("label1"),
        Node.newIf(Node.newName("true"), Node.newBlock(Node.newLabel("label1"))),
        Node.newBreak(Node.newName("label1"))
    );
    minimizer.process(null, root);
    assertEquals(3, root.getChildCount());
    assertEquals(Node.TOKEN_IF, root.getChild(1).getType());
    assertEquals(Node.TOKEN_LABEL, root.getChild(2).getType());
    assertEquals(Node.TOKEN_BREAK, root.getChild(3).getType());
  }

  @Test
  public void testMinimizeExitPoints_NestedIfStatements() {
    Node root = Node.newBlock(
        Node.newIf(Node.newName("x"), Node.newIf(Node.newName("y"), Node.newBlock(Node.newName("z")))));
    minimizer.process(null, root);
    assertEquals(3, root.getChildCount());
    assertEquals(Node.TOKEN_IF, root.getChild(1).getType());
    assertEquals(Node.TOKEN_IF, root.getChild(1).getFirstChild().getType());
  }
}