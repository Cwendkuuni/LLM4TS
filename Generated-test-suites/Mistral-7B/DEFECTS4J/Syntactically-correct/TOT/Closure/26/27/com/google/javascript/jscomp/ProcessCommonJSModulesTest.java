import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String input = "// Input file\n" +
        "goog.require('module1');\n" +
        "goog.provide('module2');\n" +
        "module2.exports = function() {\n" +
        "  return 'module2';\n" +
        "};\n" +
        "goog.require('module1').call(null, module2.exports);\n" +
        "goog.exportSymbol('test', module2.exports, 'Module2');\n" +
        "goog.exportSymbol('test2', module2.exports, 'Module2.test');\n" +
        "function module1() {\n" +
        "  this.test = function() {\n" +
        "    return 'module1';\n" +
        "  };\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, "");
    Node externs = IR.emptyNode();
    Node root = IR.parseTree(input);
    processCommonJSModules.process(externs, root);

    Node output = root.getFirstChild();
    Node module2 = findModuleWithName("module2", output);

    Node testMethod = findMethodWithName("test", module2);
    assertEquals("module1", testMethod.getFirstChild().getString());

    Node test2Method = findMethodWithName("test2", module2);
    assertEquals("Module2.test", test2Method.getFirstChild().getString());
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleModules() {
    String input = "// Input file\n" +
        "goog.require('module1');\n" +
        "goog.require('module2');\n" +
        "goog.provide('module3');\n" +
        "module3.exports = function() {\n" +
        "  return 'module3';\n" +
        "};\n" +
        "goog.require('module1').call(null, module3.exports);\n" +
        "goog.exportSymbol('test1', module1.exports, 'Module1');\n" +
        "goog.exportSymbol('test2', module1.exports, 'Module1.test1');\n" +
        "goog.require('module2').call(null, module3.exports);\n" +
        "goog.exportSymbol('test3', module2.exports, 'Module2');\n" +
        "goog.exportSymbol('test4', module2.exports, 'Module2.test3');\n" +
        "function module1() {\n" +
        "  this.test1 = function() {\n" +
        "    return 'module1';\n" +
        "  };\n" +
        "}\n" +
        "function module2() {\n" +
        "  this.test3 = function() {\n" +
        "    return 'module2';\n" +
        "  };\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, "");
    Node externs = IR.emptyNode();
    Node root = IR.parseTree(input);
    processCommonJSModules.process(externs, root);

    Node output = root.getFirstChild();
    Node module1 = findModuleWithName("module1", output);
    Node test1Method = findMethodWithName("test1", module1);
    assertEquals("module3", test1Method.getFirstChild().getString());

    Node module3 = findModuleWithName("module3", output);
    Node test3Method = findMethodWithName("test3", module3);
    assertEquals("module2", test3Method.getFirstChild().getString());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() {
    String input = "// Input file\n" +
        "goog.provide('module');\n" +
        "module.exports = function() {\n" +
        "  return 'module';\n" +
        "};\n" +
        "goog.require('submodule');\n" +
        "function submodule() {\n" +
        "  this.test = function() {\n" +
        "    return 'submodule';\n" +
        "  };\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, "");
    Node externs = IR.emptyNode();
    Node root = IR.parseTree(input);
    processCommonJSModules.process(externs, root);

    Node output = root.getFirstChild();
    Node module = findModuleWithName("module", output);
    Node testMethod = findMethodWithName("test", module);
    assertEquals("submodule", testMethod.getFirstChild().getString());
  }

  @Test
  public void testProcessCommonJSModulesWithReportDependencies() {
    String input = "// Input file\n" +
        "goog.provide('module');\n" +
        "module.exports = function() {\n" +
        "  return 'module';\n" +
        "};\n" +
        "goog.require('submodule');\n" +
        "goog.require('anotherModule');\n" +
        "function submodule() {\n" +
        "  this.test = function() {\n" +
        "    return 'submodule';\n" +
        "  };\n" +
        "}\n" +
        "function anotherModule() {\n" +
        "  this.test = function() {\n" +
        "    return 'anotherModule';\n" +
        "  };\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        "", true);
    Node externs = IR.emptyNode();
    Node root = IR.parseTree(input);
    processCommonJSModules.process(externs, root);

    Set<String> dependencies = Sets.newHashSet(processCommonJSModules.getModule()
        .getDependencies());
    assertEquals(Sets.newHashSet("submodule", "anotherModule"), dependencies);
  }

  private Node findModuleWithName(String name, Node node) {
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      if (child.isObjectLiteral() && child.getFirstChild().getString().equals(name)) {
        return child;
      }
      if (child.isName()) {
        if (child.getString().equals(name)) {
          return child.getParent();
        }
        Node parent = findModuleWithName(name, child);
        if (parent != null) {
          return parent;
        }
      }
    }
    return null;
  }

  private Node findMethodWithName(String name, Node node) {
    for (int i = 0; i < node.getChildCount(); i++) {
      Node child = node.getChildAtIndex(i);
      if (child.isFunction()) {
        if (child.getFirstChild().getString().equals(name)) {
          return child;
        }
      }
      if (child.isObjectLiteral()) {
        for (int j = 0; j < child.getChildCount(); j++) {
          Node property = child.getChildAtIndex(j);
          if (property.isName() && property.getString().equals(name)) {
            return child.getChildAtIndex(j + 1);
          }
        }
      }
      if (child.isGetProp()) {
        if (child.getQualifiedName().equals(name)) {
          return child.getParent();
        }
      }
    }
    return null;
  }
}