import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleCase() {
    String jsCode = "function f() {" +
        "  var x = 1;" +
        "  alert(x);" +
        "  x = 2;" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node entryNode = cfg.getEntryNode();
    ReachingUses result = analysis.computeResult(entryNode);

    Collection<Node> uses = analysis.getUses("x", entryNode);
    assertFalse("No uses at the entry", uses.isEmpty());
    Node useNode = uses.iterator().next();
    assertNotNull("Use node is null", useNode);
    assertTrue("Use node is not the definition", !useNode.equals(entryNode));
  }

  @Test
  public void testNoUse() {
    String jsCode = "function f() {" +
        "  var x = 1;" +
        "  if (false) {" +
        "    x = 2;" +
        "  }" +
        "  alert(x);" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node entryNode = cfg.getEntryNode();
    ReachingUses result = analysis.computeResult(entryNode);

    Collection<Node> uses = analysis.getUses("x", entryNode);
    assertTrue("Use should be empty", uses.isEmpty());
  }

  @Test
  public void testMultipleDefinitions() {
    String jsCode = "function f() {" +
        "  var x = 1;" +
        "  alert(x);" +
        "  x = 2;" +
        "  alert(x);" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node entryNode = cfg.getEntryNode();
    ReachingUses result = analysis.computeResult(entryNode);

    Collection<Node> uses = analysis.getUsages("x");
    assertFalse("There should be only two uses", uses.size() > 2);
    Node firstUse = uses.iterator().next();
    assertNotNull("First use is null", firstUse);
    Node secondUse = uses.iterator().next();
    assertNotNull("Second use is null", secondUse);
    assertFalse("First use should not be the second use", firstUse.equals(secondUse));
  }

  @Test
  public void testConditional() {
    String jsCode = "function f() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  }" +
        "  alert(x);" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node entryNode = cfg.getEntryNode();
    ReachingUses result = analysis.computeResult(entryNode);

    Collection<Node> uses = analysis.getUses("x", entryNode);
    Node useNode = uses.iterator().next();
    assertTrue("Use node should be the second definition", useNode.getNext().equals(cfg.getNode(12)));
  }

  @Test(expected = AssertionError.class)
  public void testInvalidVariable() {
    String jsCode = "function f() {" +
        "  var x = 1;" +
        "  alert(y);" +
        "}";

    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(jsCode, scope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node entryNode = cfg.getEntryNode();
    analysis.getUsages("y");
  }
}