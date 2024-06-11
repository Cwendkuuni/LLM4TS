import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Given a simple Java script
    String jsCode = "function test() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  x = y;" +
        "  if (x > y) {" +
        "    var z = 3;" +
        "  } else {" +
        "    var z = 4;" +
        "  }" +
        "  return x;";

    // When performing LiveVariablesAnalysis on the given Java script
    ControlFlowGraph cfg = new ControlFlowGraph(new Scope(null), jsCode);
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, cfg.getRootScope(), null);

    // Then the analysis should mark x, y, and z as live at the return statement
    Node returnStatement = findReturnStatement(cfg.getRootNode());
    LiveVariableLattice lattice = liveVariablesAnalysis.getLattice(returnStatement);
    assertEquals("liveSet = {0, 1, 2}", lattice.toString());

    // Given a Java script with a try block
    jsCode = "function test() {" +
        "  try {" +
        "    var x = 1;" +
        "    x = 2;" +
        "  } catch (e) {" +
        "  }" +
        "  return x;";

    // When performing LiveVariablesAnalysis on the given Java script
    cfg = new ControlFlowGraph(new Scope(null), jsCode);
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, cfg.getRootScope(), null);

    // Then the analysis should mark x as live at the return statement
    lattice = liveVariablesAnalysis.getLattice(returnStatement);
    assertEquals("liveSet = {0}", lattice.toString());

    // Given a Java script with a for loop
    jsCode = "function test() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    var x = i;" +
        "  }" +
        "  return x;";

    // When performing LiveVariablesAnalysis on the given Java script
    cfg = new ControlFlowGraph(new Scope(null), jsCode);
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, cfg.getRootScope(), null);

    // Then the analysis should mark x as live at the return statement
    lattice = liveVariablesAnalysis.getLattice(returnStatement);
    assertEquals("liveSet = {9}", lattice.toString());

    // Given a Java script with a function call
    jsCode = "function test() {" +
        "  function innerFunction() {" +
        "    var x = 1;" +
        "  }" +
        "  innerFunction();" +
        "  return x;";

    // When performing LiveVariablesAnalysis on the given Java script
    cfg = new ControlFlowGraph(new Scope(null), jsCode);
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, cfg.getRootScope(), null);

    // Then the analysis should mark x as not live at the return statement
    lattice = liveVariablesAnalysis.getLattice(returnStatement);
    assertFalse("liveSet should not contain x", lattice.isLive(0));
  }

  private Node findReturnStatement(Node node) {
    List<DiGraphEdge<Node, Branch>> edges = node.getGraph().getOutEdges(node);
    for (DiGraphEdge<Node, Branch> edge : edges) {
      if (Branch.RETURN.equals(edge.getValue())) {
        return edge.getTarget();
      }
    }
    throw new RuntimeException("No return statement found");
  }
}