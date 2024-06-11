import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessingEnvironment.NodeType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Resolution;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProcessCommonJSModulesTest {

  public static void main(String[] args) throws Exception {
    runTest(new ProcessCommonJSModulesTest());
  }

  public void testProcessCommonJSModules() throws Exception {
    String source = "" +
        "// Entry point.\n" +
        "var module1 = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      console.log('Hello from module1!');\n" +
        "    },\n" +
        "    bar: 42\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Dependent module.\n" +
        "var module2 = require('./module1');\n" +
        "module2.foo();\n" +
        "console.log(module2.bar);\n";

    SourceFile input = new TestSourceFile("test.js", source);
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel("QUIET");
    Compiler compiler = new Compiler();
    compiler.init(input, options);
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
    pcm.process(input, null);

    List<Node> nodes = compiler.getOutput();
    Node root = nodes.get(0);
    NodeTraversal.traverse(compiler, root, new NodeChecker());

    assertEquals(MODULE, root.getType());
    assertEquals("test", root.getSourceName());

    Node exports = findChildByName(root, "exports");
    assertEquals(OBJ_LITERAL, exports.getType());

    Node foo = findChildByName(exports, "foo");
    assertEquals(FUNCTION, foo.getType());

    Node call = findCallByName(root, "console.log");
    assertEquals(1, call.getChildCount());
    assertEquals("Hello from module1!", call.getChildAtIndex(0).getString());

    Node bar = findChildByName(exports, "bar");
    assertEquals(NUMBER, bar.getType());
    assertEquals(42, bar.getDoubleValue());
  }

  private Node findChildByName(Node parent, String name) {
    for (Node child : parent.getChildren()) {
      if (name.equals(child.getString())) {
        return child;
      }
    }
    throw new AssertionError("Could not find child node with name: " + name);
  }

  private Node findCallByName(Node parent, String name) {
    for (Node child : parent.getChildren()) {
      if (child instanceof Node.Call) {
        Node func = ((Node.Call) child).getCallTarget();
        if (name.equals(func.getString())) {
          return child;
        }
      }
    }
    throw new AssertionError("Could not find call node with name: " + name);
  }

  private static class NodeChecker extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isModule()) {
        assertEquals("test", n.getSourceName());
      }
    }
  }
}