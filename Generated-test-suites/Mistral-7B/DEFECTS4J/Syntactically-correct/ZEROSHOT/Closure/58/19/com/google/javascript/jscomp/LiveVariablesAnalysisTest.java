import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    String jsCode = "function foo(x, y) {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = 3;\n" +
        "  if (x > y) {\n" +
        "    x = 5; // x is killed here in the if branch\n" +
        "  } else {\n" +
        "    y = 7; // y is killed here in the else branch\n" +
        "    return x + y; // a and c are live here\n" +
        "  }\n" +
        "  return x + y; // a and c are live here\n" +
        "}";

    Scope jsScope = new Scope(NodeFactory.createScopeNode(Token.SCRIPT, null), 0);
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getControlFlowGraph(jsScope, jsCode);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Set<Var> escapedLocals = analysis.getEscapedLocals();

    assertEquals("Unexpected number of escaped locals.", 0, escapedLocals.size());

    LiveVariablesAnalysis expectedAnalysis = new LiveVariablesAnalysis() {
      @Override
      LiveVariableLattice createEntryLattice() {
        return new LiveVariableLattice(new LiveVariableLattice(jsScope.getVarCount()) {{
          set(VAR_a);
          set(VAR_b);
          set(VAR_c);
        }});
      }

      @Override
      LiveVariableLattice flowThrough(Node node, LiveVariableLattice input) {
        LiveVariableLattice result = new LiveVariableLattice(input);

        switch (node.getType()) {
          case Token.FUNCTION:
            result.liveSet.or(input.liveSet);
            result.liveSet.set(VAR_a);
            result.liveSet.set(VAR_b);
            result.liveSet.set(VAR_c);
            break;

          case Token.IF:
            result.liveSet.andNot(input.liveSet);
            result.liveSet.set(VAR_x);
            result.liveSet.set(VAR_y);
            result.liveSet.or(input.liveSet);
            break;

          case Token.RETURN:
            result.liveSet.or(input.liveSet);
            result.liveSet.set(VAR_a);
            result.liveSet.set(VAR_c);
            break;

          default:
            result.liveSet.and(input.liveSet);
            addToSetIfLocal(node, result);
        }

        return result;
      }
    };

    LiveVariableLattice expectedAnalysisResult = expectedAnalysis.computeAnalysis(new LiveVariableLattice(new LiveVariableLattice(jsScope.getVarCount()) {{
      set(VAR_a);
      set(VAR_b);
      set(VAR_c);
    }}));

    LiveVariableLattice actualAnalysisResult = analysis.computeAnalysis(analysis.createEntryLattice());

    assertEquals("Unexpected liveness of local variable a.", expectedAnalysisResult.isLive(VAR_a), actualAnalysisResult.isLive(VAR_a));
    assertEquals("Unexpected liveness of local variable b.", expectedAnalysisResult.isLive(VAR_b), actualAnalysisResult.isLive(VAR_b));
    assertEquals("Unexpected liveness of local variable c.", expectedAnalysisResult.isLive(VAR_c), actualAnalysisResult.isLive(VAR_c));

    ImmutableSet<Var> expectedEscapedLocals = Sets.newHashSet(VAR_x, VAR_y);
    assertEquals("Unexpected number of escaped locals.", expectedEscapedLocals, escapedLocals);
  }
}

class LiveVariablesAnalysisTest$LiveVariableLattice implements LiveVariablesAnalysis.LatticeElement {
  private final LiveVariablesAnalysis.LiveVariableLattice base;
  private final BitSet liveSet;

  LiveVariablesAnalysisTest$LiveVariableLattice(LiveVariablesAnalysis.LiveVariableLattice base) {
    this.base = base;
    this.liveSet = new BitSet(base.liveSet.size());
  }

  LiveVariablesAnalysisTest$LiveVariableLattice(LiveVariablesAnalysis.LiveVariableLattice base, BitSet liveSet) {
    this.base = base;
    this.liveSet = liveSet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LiveVariablesAnalysisTest$LiveVariableLattice that = (LiveVariablesAnalysisTest$LiveVariableLattice) o;
    return base.equals(that.base) && liveSet.equals(that.liveSet);
  }

  @Override
  public int hashCode() {
    int result = base.hashCode();
    result = 31 * result + liveSet.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "LiveVariablesAnalysisTest$LiveVariableLattice{" +
        "base=" + base +
        ", liveSet=" + liveSet +
        '}';
  }

  public boolean isLive(Var v) {
    return liveSet.get(v.index);
  }

  public boolean isLive(int index) {
    return liveSet.get(index);
  }
}