import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.Node;

import java.util.Collections;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  public static void main(String[] args) throws Exception {
    runTest();
  }

  public static void runTest() throws Exception {
    String sourceCode = "" +
        "function Foo() {}\n" +
        "Foo.prototype.foo = function(a, b) {\n" +
        "  this.x = a + b;\n" +
        "  return this.x;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setSourceFile("test.js");
    options.setOutputFile("output.js");
    options.setGenerateCode(true);
    options.setWarningLevel(0);

    compiler.compile(sourceCode, Collections.singletonList(options), null);

    Node root = compiler.getRootNode();
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    Node rewrittenRoot = compiler.getRootNode();
    Node fooFunction = rewrittenRoot.getFirstChild().getFirstChild();
    Node fooCall = rewrittenRoot.getLastChild();

    assertEquals("function JSCompiler_StaticMethods_foo(self, a, b) {\n" +
        "  self.x = a + b;\n" +
        "  return self.x;\n" +
        "}", fooFunction.toString());

    assertEquals("JSCompiler_StaticMethods_foo(Foo, 1, 2)", fooCall.toString());

    sourceCode = "" +
        "function Foo() {\n" +
        "  this.x = 0;\n" +
        "}";

    Foo foo = new Foo();
    assertEquals(0, foo.foo(1, 2));
    assertEquals(3, foo.x);

    // Test with error scenarios
    sourceCode = "" +
        "function Foo() {\n" +
        "  this.x = 0;\n" +
        "}\n" +
        "Foo.prototype.foo = function() {\n" +
        "  throw new Error('Error');\n" +
        "}";

    compiler = new Compiler();
    options = new CompilerOptions();
    options.setSourceFile("test.js");
    options.setOutputFile("output.js");
    options.setGenerateCode(true);
    options.setWarningLevel(0);

    compiler.compile(sourceCode, Collections.singletonList(options), null);

    root = compiler.getRootNode();
    defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    definitionSites = defFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);

    devirtualizer = new DevirtualizePrototypeMethods(compiler);
    devirtualizer.process(null, root);

    rewrittenRoot = compiler.getRootNode();
    fooFunction = rewrittenRoot.getFirstChild().getFirstChild();

    assertThrows(RuntimeException.class, () -> {
      fooFunction.call(null);
    });
  }
}