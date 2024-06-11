import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeUtil;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    String sourceCode = "function Foo() {}\n" +
                        "Foo.prototype.bar = function(x) {\n" +
                        "  this.baz = x;\n" +
                        "  return this.baz;\n" +
                        "}\n" +
                        "var foo = new Foo();\n" +
                        "var result = foo.bar(5);\n" +
                        "assertEquals(5, result);";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.compile(sourceCode, "test.js", options);

    Node root = options.ast;
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(options.externs, root);

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    for (DefinitionSite defSite : definitionSites) {
      if (defSite.definition.getName().equals("Foo.prototype.bar")) {
        rewriteDefinitionIfEligible(defSite, defFinder);
        break;
      }
    }

    String rewrittenCode = compiler.getSourceFile("test.js");
    assertEquals(getExpectedRewrittenCode(), rewrittenCode);
  }

  private void rewriteDefinitionIfEligible(DefinitionSite defSite,
                                           SimpleDefinitionFinder defFinder) {
    if (!defSite.inGlobalScope ||
        !isPrototypeMethodDefinition(defSite.node)) {
      return;
    }

    String newMethodName = getRewrittenMethodName(
        defSite.node.getLastChild().getString());
    rewriteDefinition(defSite.node, newMethodName);
    rewriteCallSites(defFinder, defSite.definition, newMethodName);
  }

  private boolean isPrototypeMethodDefinition(Node node) {
    // Add your implementation here
    // ...
  }

  private String getRewrittenMethodName(String orginalMethodName) {
    // Add your implementation here
    // ...
    return "JSCompiler_StaticMethods_" + orginalMethodName;
  }

  private void rewriteDefinition(Node node, String newMethodName) {
    // Add your implementation here
    // ...
  }

  private String getExpectedRewrittenCode() {
    return "function Foo() {}\n" +
           "var bar = function(self, x) {\n" +
           "  self.baz = x;\n" +
           "  return self.baz;\n" +
           "}\n" +
           "Foo.prototype = {\n" +
           "  bar: bar\n" +
           "};\n" +
           "var foo = new Foo();\n" +
           "var result = bar(foo, 5);\n" +
           "assertEquals(5, result);";
  }

  @Test
  public void testDevirtualizePrototypeMethodsErrorScenario() {
    String sourceCode = "function Foo() {}\n" +
                        "Foo.prototype.bar = function() {\n" +
                        "  this.baz = arguments[0];\n" +
                        "  return this.baz;\n" +
                        "}\n" +
                        "var foo = new Foo();\n" +
                        "var result = foo.bar(5, 10);\n" +
                        "assertEquals(5, result);";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.compile(sourceCode, "test.js", options);

    Node root = options.ast;
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(options.externs, root);

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    for (DefinitionSite defSite : definitionSites) {
      if (defSite.definition.getName().equals("Foo.prototype.bar")) {
        try {
          rewriteDefinitionIfEligible(defSite, defFinder);
          fail();
        } catch (Exception e) {
          // Expected exception
        }
        break;
      }
    }

    String rewrittenCode = compiler.getSourceFile("test.js");
    assertEquals(sourceCode, rewrittenCode);
  }
}