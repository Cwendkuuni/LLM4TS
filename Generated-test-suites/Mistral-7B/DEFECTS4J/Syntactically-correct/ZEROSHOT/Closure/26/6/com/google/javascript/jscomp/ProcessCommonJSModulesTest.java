import static org.junit.Assert.*;
import static com.google.javascript.jscomp. ProcessCommonJSModules.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

  private static Compiler createCompiler() {
    Compiler compiler = new Compiler();
    compiler.addPass(new ProcessCommonJSModules(compiler, ""));
    return compiler;
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "var module = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      console.log('hello world');\n" +
        "    },\n" +
        "    bar: 42\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.foo();\n" +
        "exports.bar++;\n";

    File inputFile = File.createTempFile("test", ".js");
    inputFile.setWritable(true);
    inputFile.setReadable(true);
    inputFile.writeBytes(input.getBytes());

    Compiler compiler = createCompiler();
    CompilerInput ci = new CompilerInput(inputFile, null, null, null);
    Node externs = Node.root();
    Node root = Node.parse("() => {}");

    compiler.process(externs, root, ci);

    Node result = ci.getOutput();
    assertEquals("expected one script node", 1, result.getChildCount());

    Node script = result.getChildAtIndex(0);
    assertEquals("expected script node to have expected content",
        "function anonymous(module, exports) {\n" +
        "  (function() {\n" +
        "    exports.$$module = module;\n" +
        "    exports.$$exports = exports;\n" +
        "    var foo = exports.foo;\n" +
        "    exports.foo = function() {\n" +
        "      foo.call(exports.exports);\n" +
        "    };\n" +
        "    exports.exports.bar++;\n" +
        "  }())(\n" +
        "    this,\n" +
        "    {\n" +
        "      foo: function() {\n" +
        "        console.log('hello world');\n" +
        "      },\n" +
        "      exports: undefined\n" +
        "    }\n" +
        "  );\n" +
        "}", script.getChildAtIndex(0).getString());

    // Check that no global variables were added or renamed
    Set<String> variables = Sets.newHashSet();
    NodeTraversal.traverse(compiler, script, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName()) {
          variables.add(n.getString());
        }
      }
    });

    assertEquals("expected no global variables to be present", 0,
        variables.size());

    inputFile.delete();
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithDependencies() throws Exception {
    String input = "var module = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      require('dep1').doSomething();\n" +
        "    },\n" +
        "    bar: 42\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.foo();\n" +
        "exports.bar++;\n";

    String dep1Input = "var dep1 = {\n" +
        "  exports: {\n" +
        "    doSomething: function() {\n" +
        "      console.log('dependency called');\n" +
        "    }\n" +
        "  }\n" +
        "};\n";

    File inputFile = File.createTempFile("test", ".js");
    inputFile.setWritable(true);
    inputFile.setReadable(true);
    inputFile.writeBytes(input.getBytes());

    File dep1InputFile = File.createTempFile("dep1", ".js");
    dep1InputFile.setWritable(true);
    dep1InputFile.setReadable(true);
    dep1InputFile.writeBytes(dep1Input.getBytes());

    Compiler compiler = createCompiler();
    CompilerInput ci = new CompilerInput(inputFile, null, null, null);
    Node externs = Node.root();
    Node root = Node.parse("() => {}");

    Node dep1Externs = Node.parse("var dep1 = {};");
    Node dep1Root = Node.parse("() => {}");

    compiler.process(externs, root, ci);

    // Check dependency was reported correctly
    Set<String> requires = Sets.newHashSet();
    NodeTraversal.traverse(compiler, Node.root(), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 1 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          requires.add(n.getChildAtIndex(1).getString());
        }
      }
    });

    assertEquals("expected one dependency to be present", 1, requires.size());
    assertEquals("expected dependency to match expected name", "dep1",
        requires.iterator().next());

    // Check rewritten code does not depend on require
    Node result = ci.getOutput();
    assertEquals("expected one script node", 1, result.getChildCount());

    Node script = result.getChildAtIndex(0);
    assertEquals("expected script node to have expected content",
        "function anonymous(module, exports) {\n" +
        "  (function() {\n" +
        "    exports.$$module = module;\n" +
        "    exports.$$exports = exports;\n" +
        "    var foo = exports.foo;\n" +
        "    exports.foo = function() {\n" +
        "      // require('dep1') is replaced with dep1 directly\n" +
        "      dep1.doSomething.call(dep1.exports);\n" +
        "    };\n" +
        "    exports.exports.bar++;\n" +
        "  }())(\n" +
        "    this,\n" +
        "    {\n" +
        "      foo: function() {},\n" +
        "      exports: undefined,\n" +
        "      dep1: {\n" +
        "        exports: {\n" +
        "          doSomething: function() {\n" +
        "            console.log('dependency called');\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  );\n" +
        "}", script.getChildAtIndex(0).getString());

    inputFile.delete();
    dep1InputFile.delete();
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithMultipleModules() throws Exception {
    String input = "var module1 = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      require('module2').bar();\n" +
        "    },\n" +
        "    bar: 42\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.foo();\n" +
        "exports.bar++;\n" +
        "\n" +
        "var module2 = {\n" +
        "  exports: {\n" +
        "    bar: function() {\n" +
        "      console.log('module2 called');\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "exports.bar++;\n";

    File inputFile = File.createTempFile("test", ".js");
    inputFile.setWritable(true);
    inputFile.setReadable(true);
    inputFile.writeBytes(input.getBytes());

    Compiler compiler = createCompiler();
    CompilerInput ci = new CompilerInput(inputFile, null, null, null);
    Node externs = Node.root();
    Node root = Node.parse("() => {}");

    compiler.process(externs, root, ci);

    // Check that all dependencies are reported correctly
    Set<String> requires = Sets.newHashSet();
    NodeTraversal.traverse(compiler, Node.root(), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 1 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          requires.add(n.getChildAtIndex(1).getString());
        }
      }
    });

    assertEquals("expected two dependencies to be present", 2, requires.size());
    assertEquals("expected first dependency to match expected name", "module2",
        requires.iterator().next());

    // Check rewritten code does not depend on require
    Node result = ci.getOutput();
    assertEquals("expected one script node", 1, result.getChildCount());

    Node script = result.getChildAtIndex(0);
    assertEquals("expected script node to have expected content",
        "function anonymous(module1, exports) {\n" +
        "  (function() {\n" +
        "    exports.$$module = module1;\n" +
        "    exports.$$exports = exports;\n" +
        "    var foo = exports.foo;\n" +
        "    exports.foo = function() {\n" +
        "      module2.bar.call(module2.exports);\n" +
        "    };\n" +
        "    exports.exports.bar++;\n" +
        "  }())(\n" +
        "    this,\n" +
        "    {\n" +
        "      foo: function() {},\n" +
        "      exports: undefined,\n" +
        "      module2: {\n" +
        "        exports: {\n" +
        "          bar: function() {\n" +
        "            console.log('module2 called');\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  );\n" +
        "}", script.getChildAtIndex(0).getString());

    inputFile.delete();
  }
}