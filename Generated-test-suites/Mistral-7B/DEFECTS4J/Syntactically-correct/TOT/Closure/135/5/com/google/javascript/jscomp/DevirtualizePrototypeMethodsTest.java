import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  private static final String TEST_CASE_CODE =
      "function Foo() {}" +
          "Foo.prototype.bar = function(a, b) {" +
          "  return this.qux + a + b;" +
          "}" +
          "var foo = new Foo();" +
          "var a = 1;" +
          "var b = 2;" +
          "var result = foo.bar(a, b);" +
          "assertEquals(foo.qux + a + b, result);";

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

    Node root = NodeOperations.parse(TEST_CASE_CODE);
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    CompilerPass[] passes = new CompilerPass[] {devirtualizePrototypeMethods};
    compiler.compile(root, passes, null, Collections.emptyList());

    Node result = root.getLastChild();
    assertNotNull(result);

    String actualCode = result.toSource(compiler);
    String expectedCode =
        "var qux = Foo.prototype.qux;" +
            "function bar(self, a, b) {" +
            "  return self.qux + a + b;" +
            "}" +
            "var foo = new Foo();" +
            "var a = 1;" +
            "var b = 2;" +
            "var result = bar(foo, a, b);" +
            "assertEquals(qux + a + b, result);";

    assertEquals(expectedCode, actualCode);
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithMultipleCallSites() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

    String testCaseCode =
        "function Foo() {}" +
            "Foo.prototype.bar = function(a, b) {" +
            "  return this.qux + a + b;" +
            "}" +
            "function Foo2() {}" +
            "Foo2.prototype.qux = 1;" +
            "Foo2.prototype.baz = function() {" +
            "  return this.bar(1, 2);" +
            "}" +
            "var foo = new Foo();" +
            "var foo2 = new Foo2();" +
            "var result = foo2.baz();" +
            "assertEquals(2, result);";

    Node root = NodeOperations.parse(testCaseCode);
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    CompilerPass[] passes = new CompilerPass[] {devirtualizePrototypeMethods};
    compiler.compile(root, passes, null, Collections.emptyList());

    Node result = root.getLastChild();
    assertNotNull(result);

    String actualCode = result.toSource(compiler);
    String expectedCode =
        "var qux = Foo.prototype.qux;" +
            "function bar(self, a, b) {" +
            "  return self.qux + a + b;" +
            "}" +
            "function Foo2() {}" +
            "Foo2.prototype.qux = 1;" +
            "function Foo2$baz(self) {" +
            "  return bar(self, 1, 2);" +
            "}" +
            "var foo = new Foo();" +
            "var foo2 = new Foo2();" +
            "var result = Foo2$baz(foo2);" +
            "assertEquals(2, result);";

    assertEquals(expectedCode, actualCode);
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithPrivateVariable() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

    String testCaseCode =
        "function Foo() {" +
            "this.bar = function() {" +
            "  return this.qux;" +
            "};" +
            "var qux = 1;" +
            "}" +
            "var foo = new Foo();" +
            "assertEquals(1, foo.bar());";

    Node root = NodeOperations.parse(testCaseCode);
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    CompilerPass[] passes = new CompilerPass[] {devirtualizePrototypeMethods};
    compiler.compile(root, passes, null, Collections.emptyList());

    Node result = root.getLastChild();
    assertNotNull(result);

    String actualCode = result.toSource(compiler);
    String expectedCode =
        "function Foo() {" +
            "var qux = 1;" +
            "function bar() {" +
            "  return qux;" +
            "}" +
            "assertEquals(1, bar());";

    assertEquals(expectedCode, actualCode);
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithFunctionType() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

    String testCaseCode =
        "function Foo() {" +
            "this.bar = function(a) {" +
            "  return a + a;" +
            "};" +
            "}" +
            "var foo = new Foo();" +
            "assertEquals(4, foo.bar(2));";

    Node root = NodeOperations.parse(testCaseCode);
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    CompilerPass[] passes = new CompilerPass[] {devirtualizePrototypeMethods};
    compiler.compile(root, passes, null, Collections.emptyList());

    Node result = root.getLastChild();
    assertNotNull(result);

    String actualCode = result.toSource(compiler);
    String expectedCode =
        "function Foo() {}" +
            "var bar = function(self, a) {" +
            "  return a + a;" +
            "}" +
            "assertEquals(4, bar(foo, 2));";

    assertEquals(expectedCode, actualCode);
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithNoCallSites() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

    String testCaseCode =
        "function Foo() {" +
            "this.bar = function() {};" +
            "}";

    Node root = NodeOperations.parse(testCaseCode);
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    CompilerPass[] passes = new CompilerPass[] {devirtualizePrototypeMethods};
    compiler.compile(root, passes, null, Collections.emptyList());

    Node result = root.getLastChild();
    assertNotNull(result);

    String actualCode = result.toSource(compiler);
    String expectedCode = "function Foo() {var bar = function() {};}";

    assertEquals(expectedCode, actualCode);
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithStaticMethod() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

    String testCaseCode =
        "function Foo() {}" +
            "Foo.bar = function() {" +
            "  return 1;" +
            "}" +
            "assertEquals(1, Foo.bar());";

    Node root = NodeOperations.parse(testCaseCode);
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    CompilerPass[] passes = new CompilerPass[] {devirtualizePrototypeMethods};
    compiler.compile(root, passes, null, Collections.emptyList());

    Node result = root.getLastChild();
    assertNotNull(result);

    String actualCode = result.toSource(compiler);
    String expectedCode = "assertEquals(1, JSCompiler_StaticMethods_Foo_bar());";

    assertEquals(expectedCode, actualCode);
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethodsWithExportedMethod() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);
    JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

    String testCaseCode =
        "function Foo() {" +
            "this.bar = function() {" +
            "  return 1;" +
            "};" +
            "exports.bar = this.bar;" +
            "}";

    Node root = NodeOperations.parse(testCaseCode);
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    CompilerPass[] passes = new CompilerPass[] {devirtualizePrototypeMethods};
    compiler.compile(root, passes, null, Collections.emptyList());

    Node result = root.getLastChild();
    assertNotNull(result);

    String actualCode = result.toSource(compiler);
    String expectedCode = "function Foo() {}";

    assertNull(actualCode);
  }
}