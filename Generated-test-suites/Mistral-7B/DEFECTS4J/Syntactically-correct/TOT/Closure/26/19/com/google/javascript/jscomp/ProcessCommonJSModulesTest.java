import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Config;
import com.google.javascript.jscomp.Compiler.CompilationLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerPass processCommonJSModules;

  @Before
  public void setUp() {
    compiler = new Compiler();
    Config config = new Config();
    config.setCompilationLevel(CompilationLevel.SIMPLE);
    processCommonJSModules = new ProcessCommonJSModules(compiler, "", false);
  }

  @Test
  public void testBasicCommonJSModule() {
    String source = "// module1.js\n" +
        "define(function(require) {\n" +
        "  var dep1 = require('./dep1');\n" +
        "  var dep2 = require('dep2');\n" +
        "  return {\n" +
        "    exports: function() {\n" +
        "      return 'Hello World';\n" +
        "    }\n" +
        "  };\n" +
        "});\n" +

        "// dep1.js\n" +
        "var x = 1;\n";

    Node externs = Node.externalWithSource("xs", IR.literal(1));
    Node root = Node.parse(source);

    processCommonJSModules.process(externs, root);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() &&
            n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName()) &&
            n.getChildAtIndex(1).isString()) {
          String requiredModule = n.getChildAtIndex(1).getString();
          if ("dep1".equals(requiredModule)) {
            assertFalse("X should not be accessible from module1",
                n.getParent().getChildAtIndex(1).getChildAtIndex(0)
                    .getName().is("x"));
            assertEquals("1", n.getParent().getChildAtIndex(1)
                .getChildAtIndex(0).getFirstChild().getString());
          }
        }
      }
    });

    String expectedOutput = "// Module1\n" +
        "goog.provide('module1');\n" +
        "goog.require('dep1');\n" +
        "\n" +
        "module1.exports = function() {\n" +
        "  return 'Hello World';\n" +
        "};\n";

    String output = compiler.toString(root);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testRelativeRequire() {
    String source = "// module1.js\n" +
        "define(function(require) {\n" +
        "  var dep1 = require('./dep1');\n" +
        "  return {\n" +
        "    exports: function() {\n" +
        "      return 'Hello World';\n" +
        "    }\n" +
        "  };\n" +
        "});\n" +

        "// dep1.js\n" +
        "var x = 1;\n" +

        "// module2.js\n" +
        "define(function(require) {\n" +
        "  require('./module1');\n" +
        "  return {\n" +
        "    exports: function() {\n" +
        "      return 'Hello Module 2';\n" +
        "    }\n" +
        "  };\n" +
        "});\n";

    Node externs = Node.externalWithSource("xs", IR.literal(1));
    Node root = Node.parse(source);

    processCommonJSModules.process(externs, root);

    Node module1 = null;
    Node module2 = null;

    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() &&
            n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          String requiredModule = n.getChildAtIndex(1).getString();
          if ("module1".equals(requiredModule)) {
            module1 = n;
          } else if ("module2".equals(requiredModule)) {
            module2 = n;
          }
        }
      }
    });

    assertNotNull(module1);
    assertNotNull(module2);

    Node module1ModuleExports = module1.getChildAtIndex(1).getChildAtIndex(0);
    assertEquals("1", module1ModuleExports.getFirstChild().getString());

    Node module2ModuleExports = module2.getChildAtIndex(1).getChildAtIndex(0);
    assertEquals("module1", module2ModuleExports.getChildAtIndex(0)
        .getString());
    assertEquals("Hello World", module2ModuleExports.getChildAtIndex(1)
        .getString());
  }

  @Test
  public void testModuleOverride() {
    String source = "// module1.js\n" +
        "define(function(require) {\n" +
        "  var dep1 = require('./dep1');\n" +
        "  return {\n" +
        "    exports: function() {\n" +
        "      return 'Hello World';\n" +
        "    }\n" +
        "  };\n" +
        "});\n" +

        "// dep1.js\n" +
        "var x = 1;\n" +

        "// module2.js\n" +
        "define(function(require) {\n" +
        "  require('./dep1');\n" +
        "  return {\n" +
        "    exports: function() {\n" +
        "      return 'Hello Module 2';\n" +
        "    }\n" +
        "  };\n" +
        "  require('./module1').exports = function() {\n" +
        "    return 'Hello Module 1';\n" +
        "  };\n" +
        "});\n";

    Node externs = Node.externalWithSource("xs", IR.literal(1));
    Node root = Node.parse(source);

    processCommonJSModules.process(externs, root);

    Node module1 = null;
    Node module2 = null;

    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() &&
            n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          String requiredModule = n.getChildAtIndex(1).getString();
          if ("module1".equals(requiredModule)) {
            module1 = n;
          } else if ("module2".equals(requiredModule)) {
            module2 = n;
          }
        }
      }
    });

    assertNotNull(module1);
    assertNotNull(module2);

    Node module1ModuleExports = module1.getChildAtIndex(1).getChildAtIndex(0);
    assertFalse("Module1 exports should be overridden by Module2",
        module1ModuleExports.getChildAtIndex(0)
            .getChildAtIndex(0)
            .is("exports"));
    assertEquals("Hello Module 1", module1ModuleExports.getChildAtIndex(1)
        .getString());

    Node module2ModuleExports = module2.getChildAtIndex(1).getChildAtIndex(0);
    assertEquals("1", module2ModuleExports.getChildAtIndex(0)
        .getString());
    assertEquals("Hello Module 2", module2ModuleExports.getChildAtIndex(1)
        .getString());
  }

  @Test
  public void testModuleWithNoExports() {
    String source = "// module1.js\n" +
        "define(function(require) {\n" +
        "  var x = 1;\n" +
        "});";

    Node externs = Node.externalWithSource("xs", IR.literal(1));
    Node root = Node.parse(source);

    processCommonJSModules.process(externs, root);

    String output = compiler.toString(root);
    assertTrue("Module should be emitted", output.contains("goog.provide"));
    assertTrue("Module should not emit exports", !output.contains("exports"));
  }

  @Test
  public void testModuleWithMultipleExports() {
    String source = "// module1.js\n" +
        "define(function(require) {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return {\n" +
        "    exports: function() {\n" +
        "      return {'x': x, 'y': y};\n" +
        "    }\n" +
        "  };\n" +
        "});";

    Node externs = Node.externalWithSource("xs", IR.literal(1));
    Node root = Node.parse(source);

    processCommonJSModules.process(externs, root);

    String expectedOutput = "// Module1\n" +
        "goog.provide('module1');\n" +
        "goog.require('xs');\n" +
        "\n" +
        "module1.exports = function() {\n" +
        "  return {'x': goog.xs, 'y': undefined};\n" +
        "};\n";

    String output = compiler.toString(root);
    assertEquals(expectedOutput, output);
  }
}