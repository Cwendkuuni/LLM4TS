import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String inputCode = "// input code\n" +
        "var a = require('module1');\n" +
        "var b = require('module2');\n" +
        "function main() {\n" +
        "  a.foo();\n" +
        "  b.bar();\n" +
        "}\n" +
        "module.exports = main;\n" +
        "// module1.js\n" +
        "var x = 1;\n" +
        "module.exports = {\n" +
        "  foo: function() {\n" +
        "    console.log('Module 1: foo called');\n" +
        "  }\n" +
        "};\n" +
        "// module2.js\n" +
        "var y = 2;\n" +
        "module.exports = {\n" +
        "  bar: function() {\n" +
        "    console.log('Module 2: bar called');\n" +
        "  }\n" +
        "};";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".");
    Node root = NodeOperations.parseTree(inputCode);
    processCommonJSModules.process(null, root);

    String outputCode = NodeOperations.printTree(root.getFirstChild());

    String expectedOutputCode = "// output code\n" +
        "goog.provide('module1');\n" +
        "goog.require('module2');\n" +
        "var a = module1;\n" +
        "function main() {\n" +
        "  a.foo();\n" +
        "  module2.bar();\n" +
        "}\n" +
        "goog.provide('module2');\n" +
        "goog.require('module1');\n" +
        "var y = 2;\n" +
        "goog.addType('module1', function() {\n" +
        "  this.foo = function() {\n" +
        "    console.log('Module 1: foo called');\n" +
        "  };\n" +
        "});\n" +
        "goog.addType('module2', function() {\n" +
        "  this.bar = function() {\n" +
        "    console.log('Module 2: bar called');\n" +
        "  };\n" +
        "});\n" +
        "module1.foo = module1.foo;\n" +
        "module2.bar = module2.bar;\n" +
        "module.exports = main;\n";

    assertEquals(expectedOutputCode, outputCode);
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() {
    String inputCode = "// input code\n" +
        "var a = require('./module1');\n" +
        "var b = require('../module2');\n" +
        "function main() {\n" +
        "  a.foo();\n" +
        "  b.bar();\n" +
        "}\n" +
        "module.exports = main;\n" +
        "// module1.js\n" +
        "var x = 1;\n" +
        "module.exports = {\n" +
        "  foo: function() {\n" +
        "    console.log('Module 1: foo called');\n" +
        "  }\n" +
        "};\n" +
        "// module2.js\n" +
        "var y = 2;\n" +
        "module.exports = {\n" +
        "  bar: function() {\n" +
        "    console.log('Module 2: bar called');\n" +
        "  }\n" +
        "};";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".");
    Node root = NodeOperations.parseTree(inputCode);
    processCommonJSModules.process(null, root);

    String outputCode = NodeOperations.printTree(root.getFirstChild());

    String expectedOutputCode = "// output code\n" +
        "goog.provide('module1');\n" +
        "goog.require('module2');\n" +
        "var a = module1;\n" +
        "function main() {\n" +
        "  a.foo();\n" +
        "  module2.bar();\n" +
        "}\n" +
        "goog.provide('module2');\n" +
        "goog.require('module1');\n" +
        "var y = 2;\n" +
        "goog.addType('module1', function() {\n" +
        "  this.foo = function() {\n" +
        "    console.log('Module 1: foo called');\n" +
        "  };\n" +
        "});\n" +
        "goog.addType('module2', function() {\n" +
        "  this.bar = function() {\n" +
        "    console.log('Module 2: bar called');\n" +
        "  };\n" +
        "});\n" +
        "module1.foo = module1.foo;\n" +
        "module2.bar = module2.bar;\n" +
        "module.exports = main;\n";

    assertEquals(expectedOutputCode, outputCode);
  }

  @Test
  public void testProcessCommonJSModulesWithReportDependencies() {
    String inputCode = "// input code\n" +
        "var a = require('module1');\n" +
        "var b = require('module2');\n" +
        "function main() {\n" +
        "  a.foo();\n" +
        "  b.bar();\n" +
        "}\n" +
        "module.exports = main;\n" +
        "// module1.js\n" +
        "var x = 1;\n" +
        "module.exports = {\n" +
        "  foo: function() {\n" +
        "    console.log('Module 1: foo called');\n" +
        "  }\n" +
        "};\n" +
        "// module2.js\n" +
        "var y = 2;\n" +
        "module.exports = {\n" +
        "  bar: function() {\n" +
        "    console.log('Module 2: bar called');\n" +
        "  }\n" +
        "};";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".", true);
    Node root = NodeOperations.parseTree(inputCode);
    processCommonJSModules.process(null, root);

    String outputCode = NodeOperations.printTree(root.getFirstChild());
    Set<String> requiredModules = compiler.getInput().getRequireNames();

    String expectedOutputCode = "// output code\n" +
        "goog.provide('module1');\n" +
        "goog.require('module2');\n" +
        "var a = module1;\n" +
        "function main() {\n" +
        "  a.foo();\n" +
        "  b.bar();\n" +
        "}\n" +
        "goog.provide('module2');\n" +
        "goog.require('module1');\n" +
        "var y = 2;\n" +
        "goog.addType('module1', function() {\n" +
        "  this.foo = function() {\n" +
        "    console.log('Module 1: foo called');\n" +
        "  };\n" +
        "});\n" +
        "goog.addType('module2', function() {\n" +
        "  this.bar = function() {\n" +
        "    console.log('Module 2: bar called');\n" +
        "  };\n" +
        "});\n" +
        "module1.foo = module1.foo;\n" +
        "module2.bar = module2.bar;\n" +
        "module.exports = main;\n";

    assertEquals(expectedOutputCode, outputCode);
    assertEquals(Sets.newHashSet("module1", "module2"), requiredModules);
  }

  @Test
  public void testProcessCommonJSModulesWithEmptyModule() {
    String inputCode = "// input code\n" +
        "var a = require('module1');\n" +
        "function main() {\n" +
        "  a.foo();\n" +
        "}\n" +
        "module.exports = main;\n" +
        "// module1.js\n" +
        "module.exports = {};\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".");
    Node root = NodeOperations.parseTree(inputCode);
    processCommonJSModules.process(null, root);

    String outputCode = NodeOperations.printTree(root.getFirstChild());

    String expectedOutputCode = "// output code\n" +
        "goog.provide('module1');\n" +
        "goog.require('module1');\n" +
        "var a = module1;\n" +
        "function main() {\n" +
        "  a.foo();\n" +
        "}\n" +
        "goog.provide('module1');\n" +
        "goog.require('module1');\n" +
        "module1.foo = module1.foo;\n" +
        "module.exports = main;\n";

    assertEquals(expectedOutputCode, outputCode);
  }

  @Test
  public void testProcessCommonJSModulesWithExportDefault() {
    String inputCode = "// input code\n" +
        "var a = require('module1');\n" +
        "function main() {\n" +
        "  var x = a;\n" +
        "}\n" +
        "module.exports = main;\n" +
        "// module1.js\n" +
        "var x = 1;\n" +
        "module.exports = x;\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".");
    Node root = NodeOperations.parseTree(inputCode);
    processCommonJSModules.process(null, root);

    String outputCode = NodeOperations.printTree(root.getFirstChild());

    String expectedOutputCode = "// output code\n" +
        "goog.provide('module1');\n" +
        "goog.require('module1');\n" +
        "var x = module1;\n" +
        "function main() {\n" +
        "  var x1 = module1;\n" +
        "}\n" +
        "goog.provide('module1');\n" +
        "goog.require('module1');\n" +
        "module.exports = main;\n" +
        "module1 = module1.module$exports;\n";

    assertEquals(expectedOutputCode, outputCode);
  }
}