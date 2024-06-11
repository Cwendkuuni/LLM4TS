import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Node.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions.Option;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

@RunWith(JUnit4.class)
public class PeepholeOptimizationsPassTest {

  @Test
  public void testPeepholeOptimizationsPass() {
    CompilerPass pass = new PeepholeOptimizationsPass(new Compiler(),
        new ConstantFolding(), new UnreachableCodeRemover());

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setOption(Option. WARN_UNUSED_IMPORTS, false);
    options.setOption(Option. DEAD_CODE, CompilerOptions.WarningLevel.QUIRK);

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerOptions getOptions() {
        return options;
      }
    };

    String sourceCode = "function foo(x) {\n" +
        "  if (x > 10) {\n" +
        "    return 1;\n" +
        "  } else if (x > 5) {\n" +
        "    return 2;\n" +
        "  } else {\n" +
        "    return 3;\n" +
        "  }\n" +
        "}";

    Node externs = NodeOperations.parse(sourceCode);
    Node root = NodeOperations.parse("function main() { print(foo(11)); }");

    pass.process(externs, root);

    Node expectedResult = NodeOperations.parse("function main() { print(1); }");
    assertEquals(expectedResult, root);
  }

  private static class ConstantFolding implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node n) {
      if (n.getNodeType() != NOP && n.isConstant()) {
        return n.cloneTree();
      }
      return null;
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {
      // empty
    }

    @Override
    public void endTraversal(AbstractCompiler compiler) {
      // empty
    }
  }

  private static class UnreachableCodeRemover implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node n) {
      if (n.isFunction() || n.isScript()) {
        for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
          Node newVersionOfChild = optimizeSubtree(child);
          if (newVersionOfChild != child) {
            Node parent = newVersionOfChild.getParent();
            parent.replaceChild(child, newVersionOfChild);
          }
        }

        if (n.getChildCount() == 0 && n.isFunction()) {
          return null;
        }
      }
      return null;
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {
      // empty
    }

    @Override
    public void endTraversal(AbstractCompiler compiler) {
      // empty
    }
  }
}