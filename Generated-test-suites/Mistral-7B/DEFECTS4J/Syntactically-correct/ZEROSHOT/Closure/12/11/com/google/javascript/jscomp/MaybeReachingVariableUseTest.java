import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private AbstractCompiler compiler;
  private Scope jsScope;

  @Before
  public void setup() {
    compiler = new Compiler();
    jsScope = new Scope(null, new Scope.FunctionScope(null, null));
    cfg = new ControlFlowGraph<>(compiler, jsScope);
  }

  @Test
  public void testSimple() {
    String source = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  alert(a);" +
        "  alert(b);" +
        "}";
    cfg = compiler.compile(NodePrinter.print(src), jsScope);

    MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    ReachingUses result = analyzer.analyze();

    ImmutableSet<Node> aUses = analyzer.getUses("a", cfg.getEntryNode());
    ImmutableSet<Node> bUses = analyzer.getUses("b", cfg.getEntryNode());

    Table<Node, String, Node> expected = Tables.newHashMap();
    expected.put(cfg.getNode(cfg.getEntryNode()).get(), "a", cfg.getNode(1));
    expected.put(cfg.getNode(cfg.getEntryNode()).get(), "b", null);

    for (Node node : ImmutableSet.of(cfg.getNode(1), cfg.getNode(2))) {
      for (String variable : ImmutableSet.of("a", "b")) {
        assertEquals(expected.get(node, variable), aUses.contains(node) ? node : null);
      }
    }

    assertEquals(ImmutableSet.of(cfg.getNode(1), cfg.getNode(2)), aUses);
    assertEquals(ImmutableSet.of(), bUses);
  }

  @Test
  public void testComplex() {
    String source = "function foo() {" +
        "  var a = 1;" +
        "  if (true) {" +
        "    var a = 2;" +
        "  } else {" +
        "    var b = 3;" +
        "  }" +
        "  alert(a);" +
        "  alert(b);" +
        "}";
    cfg = compiler.compile(NodePrinter.print(source), jsScope);

    MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    ReachingUses result = analyzer.analyze();

    ImmutableSet<Node> aUses = analyzer.getUses("a", cfg.getEntryNode());
    ImmutableSet<Node> bUses = analyzer.getUses("b", cfg.getEntryNode());

    Table<Node, String, Node> expected = Tables.newHashMap();
    expected.put(cfg.getNode(cfg.getEntryNode()).get(), "a", cfg.getNode(1));
    expected.put(cfg.getNode(3), "a", cfg.getNode(1));
    expected.put(cfg.getNode(3), "b", cfg.getNode(4));

    for (Node node : ImmutableSet.of(cfg.getNode(1), cfg.getNode(2), cfg.getNode(3), cfg.getNode(4))) {
      for (String variable : ImmutableSet.of("a", "b")) {
        assertEquals(expected.get(node, variable), aUses.contains(node) ? node : null);
      }
    }

    assertEquals(ImmutableSet.of(cfg.getNode(1), cfg.getNode(3)), aUses);
    assertEquals(ImmutableSet.of(cfg.getNode(3)), bUses);
  }
}