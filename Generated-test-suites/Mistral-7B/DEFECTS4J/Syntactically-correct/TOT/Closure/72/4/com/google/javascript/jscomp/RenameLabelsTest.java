import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private Compiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = mock(Compiler.class);
    options = mock(CompilerOptions.class);
    nodeFactory = mock(NodeFactory.class);
  }

  @org.junit.Test
  public void testRenameLabels() {
    String input = "function foo() {" +
        "  a: { " +
        "    b: { " +
        "      x = 1; " +
        "      break a; " +
        "    } " +
        "  } " +
        "}" +
        "function bar() {" +
        "  a: { " +
        "    b: { " +
        "      y = 2; " +
        "      break a; " +
        "    } " +
        "  } " +
        "}" +
        "function baz() {" +
        "  a: { " +
        "    b: { " +
        "      z = 3; " +
        "      continue a; " +
        "    } " +
        "  } " +
        "}" +
        "function qux() {" +
        "  a: { " +
        "    b: { " +
        "      x = 1; " +
        "      break a; " +
        "    } " +
        "  } " +
        "  a: { " +
        "    b: { " +
        "      y = 2; " +
        "      break a; " +
        "    } " +
        "  } " +
        "}" +
        "function quux() {" +
        "  a: { " +
        "    b: { " +
        "      x = 1; " +
        "      break a; " +
        "    } " +
        "  } " +
        "  a: { " +
        "    b: { " +
        "      y = 2; " +
        "      continue a; " +
        "    } " +
        "  } " +
        "  a: { " +
        "    b: { " +
        "      z = 3; " +
        "      break a; " +
        "    } " +
        "  } " +
        "}" +
        "function coro() {" +
        "  a: { " +
        "    b: { " +
        "      function* gen() {" +
        "        yield 1;" +
        "        yield 2;" +
        "      } " +
        "      var it = gen(); " +
        "      it.next(); " +
        "      it.next(); " +
        "      break a; " +
        "    } " +
        "  } " +
        "}" +
        "function quux2() {" +
        "  a: { " +
        "    b: { " +
        "      break a; " +
        "    } " +
        "  } " +
        "  a: { " +
        "    b: { " +
        "      continue a; " +
        "    } " +
        "  } " +
        "}";

    Node externs = nodeFactory.createProgram(input, null, null);
    Node root = nodeFactory.createProgram(null, externs, null);

    when(compiler.getOptions()).thenReturn(options);
    when(options.getOutputInfo()).thenReturn(new OutputInfo());

    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    renameLabels.new ProcessLabels().traverse(nodeTraversal);

    Node output = nodeTraversal.getOutput();

    String expectedOutput = "function foo() {" +
        "  " +
        "    a: { " +
        "      " +
        "        x = 1; " +
        "        break a; " +
        "      " +
        "    } " +
        "}" +
        "function bar() {" +
        "  " +
        "    a: { " +
        "      " +
        "        y = 2; " +
        "        break a; " +
        "      " +
        "    } " +
        "}" +
        "function baz() {" +
        "  " +
        "    a: { " +
        "      " +
        "        z = 3; " +
        "        continue a; " +
        "      " +
        "    } " +
        "}" +
        "function qux() {" +
        "  a: { " +
        "    b: { " +
        "      x = 1; " +
        "      break a; " +
        "    } " +
        "  } " +
        "  a: { " +
        "    b: { " +
        "      y = 2; " +
        "      break a; " +
        "    } " +
        "  } " +
        "}" +
        "function quux() {" +
        "  a: { " +
        "    b: { " +
        "      x = 1; " +
        "      break a; " +
        "    } " +
        "  } " +
        "  a: { " +
        "    b: { " +
        "      y = 2; " +
        "      continue a; " +
        "    } " +
        "  } " +
        "  a: { " +
        "    b: { " +
        "      z = 3; " +
        "      break a; " +
        "    } " +
        "  } " +
        "}" +
        "function coro() {" +
        "  a: { " +
        "    b: { " +
        "      function* gen() {" +
        "        yield 1;" +
        "        yield 2;" +
        "      } " +
        "      var it = gen(); " +
        "      it.next(); " +
        "      it.next(); " +
        "      break a; " +
        "    } " +
        "  } " +
        "}" +
        "function quux2() {" +
        "  a: { " +
        "    b: { " +
        "      break a; " +
        "    } " +
        "  } " +
        "  a: { " +
        "    b: { " +
        "      continue a; " +
        "    } " +
        "  } " +
        "}" +
        "";

    assertEquals(expectedOutput, nodeTraversal.getSource());
  }

  private static class OutputInfo implements Compiler.OutputInfo {
    @Override
    public void printError(String message) {
      //Do nothing
    }

    @Override
    public void printWarning(String message) {
      //Do nothing
    }
  }
}