import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleVariableUsage() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  alert(x);" +
        "  x = 2;" +
        "}";

    Scope jsScope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(null, jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node entryNode = cfg.getEntryNode();
    ReachingUses initialState = analysis.computeSingleStep(entryNode, null);

    Set<Node> uses = analysis.getUses("x", entryNode);
    assertEquals(Sets.newHashSet(cfg.getNode(3)), uses); // alert(x)
  }

  @Test
  public void testVariableUsageInCondition() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x) {" +
        "    alert(x);" +
        "  }" +
        "  x = 2;" +
        "}";

    Scope jsScope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(null, jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node entryNode = cfg.getEntryNode();
    ReachingUses initialState = analysis.computeSingleStep(entryNode, null);

    Set<Node> uses = analysis.getUses("x", entryNode);
    assertEquals(Sets.newHashSet(cfg.getNode(3)), uses); // alert(x)
  }

  @Test
  public void testVariableEscapedFromFunction() {
    String jsCode = "function foo(x) {" +
        "  return function() {" +
        "    alert(x);" +
        "  }" +
        "}" +
        "foo(1)();";

    Scope jsScope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(null, jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node entryNode = cfg.getEntryNode();
    ReachingUses initialState = analysis.computeSingleStep(entryNode, null);

    Set<Node> uses = analysis.getUses("x", entryNode);
    assertEquals(Sets.newHashSet(), uses); // x is escaped from the function
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNonLocalVariable() {
    String jsCode = "var x = 1;" +
        "function foo() {" +
        "  alert(x);" +
        "}";

    Scope jsScope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(null, jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node entryNode = cfg.getEntryNode();
    analysis.getUses("x", entryNode); // throws IllegalArgumentException
  }

  @Test
  public void testVariableAssignmentInLoop() {
    String jsCode = "function foo() {" +
        "  var x = 0;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    x += i;" +
        "  }" +
        "  alert(x);" +
        "}";

    Scope jsScope = new Scope(null);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(null, jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    Node entryNode = cfg.getEntryNode();
    ReachingUses initialState = analysis.computeSingleStep(entryNode, null);

    Set<Node> uses = analysis.getUses("x", entryNode);
    assertEquals(Sets.newHashSet(cfg.getNode(12)), uses); // alert(x)
  }
}