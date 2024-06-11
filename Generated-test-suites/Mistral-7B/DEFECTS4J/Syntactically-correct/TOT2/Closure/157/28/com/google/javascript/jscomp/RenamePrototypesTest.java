import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

public class RenamePrototypesTest {

  @Test
  public void testBasicFunctionality() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "Foo.prototype.baz = 42;\n" +
        "var x = new Foo();\n" +
        "assertEquals(42, x.bar());\n" +
        "assertEquals(42, x.baz);");

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionNames(false);
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT6);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    compiler.addPasses(renamePrototypes);

    Node ast = compiler.parseAndCompile(input);
    assertEquals(0, ast.getErrorCount());

    VariableMap renamedProperties = renamePrototypes.getPropertyMap();
    String barName = renamedProperties.lookupRenamedName("Foo.prototype.bar");
    String bazName = renamedProperties.lookupRenamedName("Foo.prototype.baz");

    assertEquals("testBar", barName);
    assertEquals("testBaz", bazName);

    // Test that the renamed properties are used correctly
    input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype[" + barName + "] = function() { return 42; };\n" +
        "Foo.prototype[" + bazName + "] = 42;\n" +
        "var x = new Foo();\n" +
        "assertEquals(42, x." + barName + "());\n" +
        "assertEquals(42, x." + bazName + ");");

    ast = compiler.parseAndCompile(input);
    assertEquals(0, ast.getErrorCount());
  }

  @Test
  public void testEdgeCases() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "Foo.prototype.baz = 42;\n" +
        "var x = new Foo();\n" +
        "function Bar() {}\n" +
        "Bar.prototype.qux = function() { return 42; };\n" +
        "Bar.prototype.qux = 42;\n" +
        "var y = new Bar();\n" +
        "assertEquals(42, x.bar());\n" +
        "assertEquals(42, x.baz);\n" +
        "assertEquals(42, y.qux);\n" +
        "assertEquals(42, y.qux);");

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionNames(false);
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT6);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    compiler.addPasses(renamePrototypes);

    Node ast = compiler.parseAndCompile(input);
    assertEquals(0, ast.getErrorCount());

    VariableMap renamedProperties = renamePrototypes.getPropertyMap();

    // Test that properties with leading and trailing underscores are not renamed
    String barName = renamedProperties.lookupRenamedName("Foo.prototype.bar");
    String bazName = renamedProperties.lookupRenamedName("Foo.prototype.baz");
    String quxName = renamedProperties.lookupRenamedName("Bar.prototype.qux");

    assertEquals("bar", barName);
    assertEquals("baz", bazName);
    assertEquals("qux", quxName);

    // Test that properties with only one underscore are renamed only in agressive mode
    CompilerInput aggressiveInput = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.foo_ = function() { return 42; };\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "var x = new Foo();\n" +
        "assertEquals(42, x.foo_());\n" +
        "assertEquals(42, x.bar());");

    CompilerOptions aggressiveOptions = new CompilerOptions(options);
    aggressiveOptions.setAssumeFunctionNames(false);
    aggressiveOptions.setLanguageIn(CompilerOptions.Language.ECMASCRIPT6);
    aggressiveOptions.setAggressiveRenaming(true);

    AbstractCompiler aggressiveCompiler = new AbstractCompiler();
    aggressiveCompiler.init(aggressiveOptions);

    CompilerPass aggressiveRenamePrototypes = new RenamePrototypes(aggressiveCompiler, true, null, null);
    aggressiveCompiler.addPasses(aggressiveRenamePrototypes);

    Node aggressiveAst = aggressiveCompiler.parseAndCompile(aggressiveInput);
    assertEquals(0, aggressiveAst.getErrorCount());

    VariableMap aggressiveRenamedProperties = aggressiveRenamePrototypes.getPropertyMap();

    String fooName = aggressiveRenamedProperties.lookupRenamedName("Foo.prototype.foo_");
    assertEquals("foo__", fooName);
    String barNameAggressive = aggressiveRenamedProperties.lookupRenamedName("Foo.prototype.bar");
    assertEquals("testBar", barNameAggressive);
  }

  @Test
  public void testExceptionScenarios() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { throw new Error(); };\n" +
        "Foo.prototype.baz = function() { return 42; };\n" +
        "try {\n" +
        "  var x = new Foo();\n" +
        "  x.bar();\n" +
        "} catch (e) {\n" +
        "  fail('Expected an error but got: ' + e);\n" +
        "}\n" +
        "assertEquals(42, x.baz);");

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionNames(false);
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT6);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    compiler.addPasses(renamePrototypes);

    Node ast = compiler.parseAndCompile(input);
    assertEquals(0, ast.getErrorCount());

    VariableMap renamedProperties = renamePrototypes.getPropertyMap();

    String barName = renamedProperties.lookupRenamedName("Foo.prototype.bar");
    String bazName = renamedProperties.lookupRenamedName("Foo.prototype.baz");

    assertEquals("testBar", barName);
    assertEquals("testBaz", bazName);

    // Test that the renamed error function is thrown correctly
    input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype[" + barName + "] = function() { throw new Error(); };\n" +
        "Foo.prototype[" + bazName + "] = function() { return 42; };\n" +
        "try {\n" +
        "  var x = new Foo();\n" +
        "  x." + barName + "();\n" +
        "} catch (e) {\n" +
        "  assertEquals('Error', e.constructor.name);\n" +
        "}\n" +
        "assertEquals(42, x." + bazName + ");");

    ast = compiler.parseAndCompile(input);
    assertEquals(0, ast.getErrorCount());
  }

  @Test
  public void testPrototypeAssignment() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "Foo.prototype = { test: 42 };\n" +
        "var x = new Foo();\n" +
        "assertEquals(42, x.bar());\n" +
        "assertEquals(42, x.test);");

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionNames(false);
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT6);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    compiler.addPasses(renamePrototypes);

    Node ast = compiler.parseAndCompile(input);
    assertEquals(0, ast.getErrorCount());

    VariableMap renamedProperties = renamePrototypes.getPropertyMap();

    String barName = renamedProperties.lookupRenamedName("Foo.prototype.bar");
    String testName = renamedProperties.lookupRenamedName("Foo.prototype.test");

    assertEquals("testBar", barName);
    assertEquals("test", testName);
  }

  @Test
  public void testPrototypeAssignmentWithAggressiveRenaming() {
    CompilerInput input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "Foo.prototype = { test: 42 };\n" +
        "var x = new Foo();\n" +
        "assertEquals(42, x.bar());\n" +
        "assertEquals(42, x.test);");

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionNames(false);
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT6);
    options.setAggressiveRenaming(true);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.init(options);

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    compiler.addPasses(renamePrototypes);

    Node ast = compiler.parseAndCompile(input);
    assertEquals(0, ast.getErrorCount());

    VariableMap renamedProperties = renamePrototypes.getPropertyMap();

    String barName = renamedProperties.lookupRenamedName("Foo.prototype.bar");
    String testName = renamedProperties.lookupRenamedName("Foo.prototype.test");

    assertEquals("testBar", barName);
    assertEquals("test", testName);

    // Test that the renamed test property is not renamed when it is used as a property name
    input = new CompilerInput(
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "Foo.prototype = { test: { test: 42 } };\n" +
        "var x = new Foo();\n" +
        "assertEquals(42, x.bar());\n" +
        "assertEquals(42, x.test.test);");

    ast = compiler.parseAndCompile(input);
    assertEquals(0, ast.getErrorCount());

    renamedProperties = renamePrototypes.getPropertyMap();

    testName = renamedProperties.lookupRenamedName("Foo.prototype.test");
    assertEquals("test", testName);
  }
}