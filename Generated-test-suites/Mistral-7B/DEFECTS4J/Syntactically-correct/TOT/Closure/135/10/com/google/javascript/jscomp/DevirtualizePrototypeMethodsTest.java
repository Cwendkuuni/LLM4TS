import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSType.*;
import static com.google.javascript.jscomp.JSTypeRegistry.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionSite;
import com.google.javascript.jscomp.Definition;
import com.google.javascript.jscomp.DefinitionSite;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;

public class DevirtualizePrototypeMethodsTest {

  private static Compiler createCompiler() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new AbstractCompiler(options);
    return compiler;
  }

  @Test
  public void testSimpleDevirtualization() {
    Compiler compiler = createCompiler();
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);

    String jsCode = "function A() {} A.prototype.accumulate = function(value) {" +
        "this.total += value; return this.total" +
        "} var a = new A(); var total = a.accumulate(2);";

    Node root = Parser.parse(jsCode, "test.js", null);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(CompilerOptions.emptyExterns, root);

    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      rewriteDefinitionIfEligible(defSite, defFinder, pass);
    }

    Node transformedCode = compiler.getTransformedSource();
    String expectedTransformedCode = "var accumulate = function(self, value) {" +
        "self.total += value; return self.total" +
        "} var a = new A(); var total = accumulate(a, 2);";

    assertEquals(expectedTransformedCode, transformedCode.toString());
  }

  @Test
  public void testDevirtualizationWithArguments() {
    Compiler compiler = createCompiler();
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);

    String jsCode = "function A() {" +
        "this.accumulate = function(values) {" +
        "for (var i = 0, len = values.length; i < len; i++) {" +
        "this.total += values[i]" +
        "}" +
        "return this.total" +
        "} var a = new A(); var values = [1, 2, 3]; var total = a.accumulate(values);";

    Node root = Parser.parse(jsCode, "test.js", null);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(CompilerOptions.emptyExterns, root);

    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      rewriteDefinitionIfEligible(defSite, defFinder, pass);
    }

    Node transformedCode = compiler.getTransformedSource();
    String expectedTransformedCode = "var accumulate = function(self, values) {" +
        "for (var i = 0, len = values.length; i < len; i++) {" +
        "self.total += values[i]" +
        "}" +
        "return self.total" +
        "} var a = new A(); var values = [1, 2, 3]; var total = accumulate(a, values);";

    assertEquals(expectedTransformedCode, transformedCode.toString());
  }

  @Test
  public void testDevirtualizationWithComplexExpression() {
    Compiler compiler = createCompiler();
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);

    String jsCode = "function A() {" +
        "this.accumulate = function() {" +
        "return (this.x = this.x || 0) + arguments[0]" +
        "}" +
        "} A.prototype.accumulate.call(null, 1);";

    Node root = Parser.parse(jsCode, "test.js", null);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(CompilerOptions.emptyExterns, root);

    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      rewriteDefinitionIfEligible(defSite, defFinder, pass);
    }

    Node transformedCode = compiler.getTransformedSource();
    String expectedTransformedCode = "var accumulate = function() {" +
        "return (accumulate.self = accumulate.self || 0) + arguments[0]" +
        "};" +
        "accumulate.call(null, 1);";

    assertEquals(expectedTransformedCode, transformedCode.toString());
  }

  @Test
  public void testDevirtualizationWithFunctionCall() {
    Compiler compiler = createCompiler();
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);

    String jsCode = "function A() {" +
        "this.accumulate = function() {" +
        "return this.x = this.x || 0 + this.append(1)" +
        "}" +
        "} A.prototype.append = function(x) {" +
        "return x * 2" +
        "};" +
        "var a = new A(); var total = a.accumulate();";

    Node root = Parser.parse(jsCode, "test.js", null);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(CompilerOptions.emptyExterns, root);

    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      rewriteDefinitionIfEligible(defSite, defFinder, pass);
    }

    Node transformedCode = compiler.getTransformedSource();
    String expectedTransformedCode = "var accumulate = function() {" +
        "return accumulate.self = accumulate.self || 0 + append(1)" +
        "};" +
        "function append(x) {" +
        "return x * 2" +
        "};" +
        "var a = new A(); var total = accumulate();";

    assertEquals(expectedTransformedCode, transformedCode.toString());
  }

  @Test
  public void testDevirtualizationWithVarArgs() {
    Compiler compiler = createCompiler();
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);

    String jsCode = "function A() {" +
        "this.accumulate = function() {" +
        "var sum = 0;" +
        "for (var i = 0, len = arguments.length; i < len; i++) {" +
        "sum += arguments[i]" +
        "}" +
        "return sum" +
        "}" +
        "} A.prototype.accumulate.call(null, 1, 2, 3);";

    Node root = Parser.parse(jsCode, "test.js", null);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(CompilerOptions.emptyExterns, root);

    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      rewriteDefinitionIfEligible(defSite, defFinder, pass);
    }

    Node transformedCode = compiler.getTransformedSource();
    String expectedTransformedCode = "var accumulate = function() {" +
        "var sum = 0;" +
        "for (var i = 0, len = arguments.length; i < len; i++) {" +
        "sum += arguments[i]" +
        "}" +
        "return sum" +
        "};" +
        "var a = null;" +
        "var args = [1, 2, 3];" +
        "var total = accumulate.apply(a, args);";

    assertEquals(expectedTransformedCode, transformedCode.toString());
  }

  @Test
  public void testDevirtualizationWithCyclicReference() {
    Compiler compiler = createCompiler();
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);

    String jsCode = "function A() {" +
        "this.accumulate = function() {" +
        "return this.x = this.x || new B().accumulate()" +
        "}" +
        "} function B() {" +
        "this.accumulate = function() {" +
        "return this.x = this.x || new A().accumulate()" +
        "}" +
        "} A.prototype.accumulate.call(null);";

    Node root = Parser.parse(jsCode, "test.js", null);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(CompilerOptions.emptyExterns, root);

    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      rewriteDefinitionIfEligible(defSite, defFinder, pass);
    }

    try {
      Node transformedCode = compiler.getTransformedSource();
      fail("Expected compile error due to cyclic reference.");
    } catch (RuntimeException e) {
      // expected
    }
  }

  private void rewriteDefinitionIfEligible(DefinitionSite defSite,
                                          SimpleDefinitionFinder defFinder,
                                          CompilerPass pass) {
    if (defSite.inExterns ||
        !defSite.inGlobalScope ||
        !isEligibleDefinition(defFinder, defSite)) {
      return;
    }

    Definition definition = defSite.definition;
    pass.rewriteDefinitionIfEligible(definitionSite, defFinder);
  }

  private boolean isEligibleDefinition(SimpleDefinitionFinder defFinder,
                                       DefinitionSite definitionSite) {
    // TODO(user) Implement isEligibleDefinition method for test cases.
    return true;
  }
}