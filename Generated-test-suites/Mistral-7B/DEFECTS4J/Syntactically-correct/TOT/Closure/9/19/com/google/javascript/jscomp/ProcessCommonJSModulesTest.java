import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String inputCode = "// Input code\n" +
        "var a = require('moduleA');\n" +
        "function main() {\n" +
        "  var b = require('moduleB');\n" +
        "  console.log(a, b);\n" +
        "}\n" +
        "module.exports = main;\n" +
        "// moduleA.js\n" +
        "var c = 1;\n" +
        "module.exports = c;\n" +
        "// moduleB.js\n" +
        "var d = 2;\n" +
        "module.exports = d;\n";

    CompilerInput input = new CompilerInput("input.js", inputCode);
    AbstractCompiler compiler = new AbstractCompiler();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(input, null);

    CompilerInput output = input; // The output is overwritten in place

    // Check if the correct goog.provide and require statements were emitted
    String expectedOutputCode = "// Output code\n" +
        "/**\n" +
        " * @const\n" +
        " */\n" +
        "goog.provide('moduleA');\n" +
        "goog.provide('moduleB');\n" +
        "\n" +
        "/**\n" +
        " * @const\n" +
        " */\n" +
        "var moduleA = /** @type {number} */(moduleA.module$exports);\n" +
        "\n" +
        "/**\n" +
        " * @const\n" +
        " */\n" +
        "var moduleB = /** @type {number} */(moduleB.module$exports);\n" +
        "\n" +
        "goog.require('moduleA');\n" +
        "goog.require('moduleB');\n" +
        "\n" +
        "function main() {\n" +
        "  var a = moduleA;\n" +
        "  var b = moduleB;\n" +
        "  console.log(a, b);\n" +
        "}\n" +
        "goog.require('main');\n";

    assertEquals(expectedOutputCode, output.getSourceCode());

    // Check if the correct module names are used in require calls
    NodeTraversal traverser = new NodeTraversal(output);
    Node requireNode = traverser.findChildByType(traverser.getRoot(), Node.CALL_EXPR);
    assertEquals("require", requireNode.getFirstChild().getString());
    Node moduleNameNode = requireNode.getChildAtIndex(1);
    assertEquals("moduleA", moduleNameNode.getString());

    // Check if global variable names were properly suffixed
    Node globalVarNodeA = traverser.findChildByType(traverser.getRoot(), Node.NAME);
    assertEquals("moduleA$$", globalVarNameA.getString());
    Node globalVarNodeB = traverser.getRoot().getChildAtIndex(1);
    assertEquals("moduleB$$", globalVarNodeB.getString());

    // Check if module.exports was properly renamed
    Node exportsNodeA = traverser.findChildByType(traverser.getRoot(), Node.GET_PROP);
    assertEquals("moduleA$module$exports", exportsNodeA.getQualifiedName());
    Node exportsNodeB = traverser.getRoot().getChildAtIndex(2);
    assertEquals("moduleB$module$exports", exportsNodeB.getQualifiedName());

    // Check if the correct value was assigned to the module variable
    Node moduleVarNodeA = traverser.getRoot().getChildAtIndex(3);
    Node moduleExportsNodeA = traverser.getRoot().getChildAtIndex(4);
    assertEquals(exportsNodeA, moduleVarNodeA.getChildAtIndex(0));
    Node moduleVarNodeB = traverser.getRoot().getChildAtIndex(5);
    Node moduleExportsNodeB = traverser.getRoot().getChildAtIndex(6);
    assertEquals(exportsNodeB, moduleVarNodeB.getChildAtIndex(0));
  }
}