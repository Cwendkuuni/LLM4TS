import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private Scope jsScope;
  private ControlFlowGraph<Node> cfg;
  private MaybeReachingVariableUse analysis;

  @Before
  public void setup() {
    jsScope = new Scope(); // create a dummy scope
    cfg = new ControlFlowGraph<>();

    analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
  }

  @Test
  public void testEmpty() {
    ReachingUses initial = analysis.compute(Node.EMPTY_NODE);
    assertNotNull(initial);
    assertEquals(Sets.newHashSet(), initial.mayUseMap.keySet());
  }

  @Test
  public void testSimpleAssignment() {
    Node root = Node.newBlock(
        Node.newAssignment(Token.VAR, "a", Node.newNumberLiteral(1)),
        Node.newPrint(Node.newName("a"))
    );

    analysis.compute(root);

    Collection<Node> uses = analysis.getUses("a", root);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(root, uses.iterator().next());
  }

  @Test
  public void testConditional() {
    Node root = Node.newBlock(
        Node.newIf(Node.newName("x"), Node.newBlock(
            Node.newAssignment(Token.VAR, "a", Node.newNumberLiteral(1)),
            Node.newPrint(Node.newName("a"))),
            Node.newBlock(Node.newPrint(Node.newName("b")))
        )
    );

    analysis.compute(root);

    Collection<Node> usesA = analysis.getUses("a", root);
    assertNotNull(usesA);
    assertEquals(1, usesA.size());
    assertEquals(root, usesA.iterator().next());

    Collection<Node> usesB = analysis.getUses("b", root);
    assertNotNull(usesB);
    assertFalse(usesB.isEmpty());
  }

  @Test
  public void testLoop() {
    Node root = Node.newBlock(
        Node.newVar("i"),
        Node.newBlock(
            Node.newFor(
                Node.newName("i"),
                Node.newName("j"),
                Node.newBlock(
                    Node.newAssignment(Token.VAR, "a", Node.newNumberLiteral(1)),
                    Node.newPrint(Node.newName("a"))),
                0,
                Node.newNumberLiteral(10)
            )
        )
    );

    analysis.compute(root);

    Collection<Node> usesA = analysis.getUsages("a");

    for (Node node : usesA) {
      Collection<Node> uses = analysis.getUses("a", node);
      assertNotNull(uses);
      assertEquals(1, uses.size());
      assertEquals(node, uses.iterator().next());
    }
  }

  @Test
  public void testEscape() {
    Node root = Node.newBlock(
        Node.newVar("x"),
        Node.newBlock(
            Node.newIf(Node.newName("x"), Node.newBlock(
                Node.newVar("y"), // escape
                Node.newFunction(
                    Node.newName("foo"),
                    Node.newBlock(
                        Node.newAssignment(Token.VAR, "a", Node.newName("y")),
                        Node.newPrint(Node.newName("a")))
                )
            ), Node.newBlock())
        )
    );

    analysis.compute(root);

    Collection<Node> usesA = analysis.getUses("a", root);
    assertNotNull(usesA);
    assertEquals(0, usesA.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidName() {
    analysis.getUses("x", Node.EMPTY_NODE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNode() {
    analysis.getUses("a", Node.EMPTY_NODE);
  }
}