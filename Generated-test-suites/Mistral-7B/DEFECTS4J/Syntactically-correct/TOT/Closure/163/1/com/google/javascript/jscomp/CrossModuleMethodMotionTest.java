import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.jscomp.TestUtils.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ParserTestCase;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest extends ParserTestCase {

  private Compiler compiler;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = newCompiler();
  }

  @Override
  public void tearDown() throws Exception {
    compiler = null;
    super.tearDown();
  }

  public void testSimpleMoveMethod() throws Exception {
    String sourceCode =
        "function Foo() {}" +
        "Foo.prototype.bar = function() {};" +
        "function A() {}" +
        "function B() {}" +
        "A.prototype.baz = function() { A.prototype.bar(); }";

    Node root = parse(sourceCode);
    Node externs = null;

    CompilerOptions options = newCompilerOptions();
    options.setAllowAsync(true);
    options.setAllowAllTypes(true);
    options.setCrossModuleMethodMotion(true);

    Node result = compiler.compile(root, externs, options);

    Node expectedResult = parse(
        "function Foo() {}" +
        "function A() {" +
        "  function JSCompiler_stubMethod_0(JSCompiler_stubMethod_0_id) {" +
        "    return function() {" +
        "      return Foo.prototype.bar.apply(this, arguments);" +
        "    };" +
        "  }" +
        "  var JSCompiler_stubMap = [];" +
        "  A.prototype.baz = JSCompiler_stubMethod_0(0);" +
        "  function B() {}" +
        "  function JSCompiler_unstubMethod_1(JSCompiler_unstubMethod_1_id, JSCompiler_unstubMethod_1_body) {" +
        "    return JSCompiler_stubMap[JSCompiler_unstubMethod_1_id] = JSCompiler_unstubMethod_1_body;" +
        "  }" +
        "  B.prototype.bar = JSCompiler_unstubMethod_1(1, function() {});" +
        "}");

    assertEquals(printTree(expectedResult), printTree(result));
  }

  public void testMoveMethodToDifferentModule() throws Exception {
    String sourceCode =
        "function Foo() {}" +
        "Foo.prototype.bar = function() {};" +
        "function A() {}" +
        "function B() {}" +
        "A.prototype.baz = function() { A.prototype.bar(); }" +
        "function C() {}";

    Node root = parse(sourceCode);
    Node externs = null;

    CompilerOptions options = newCompilerOptions();
    options.setAllowAsync(true);
    options.setAllowAllTypes(true);
    options.setCrossModuleMethodMotion(true);

    Node result = compiler.compile(root, externs, options);

    Node expectedResult = parse(
        "function Foo() {}" +
        "function C() {" +
        "  function JSCompiler_stubMethod_0(JSCompiler_stubMethod_0_id) {" +
        "    return function() {" +
        "      return Foo.prototype.bar.apply(this, arguments);" +
        "    };" +
        "  }" +
        "  var JSCompiler_stubMap = [];" +
        "  C.prototype.baz = JSCompiler_stubMethod_0(0);" +
        "  function A() {}" +
        "  function B() {}" +
        "  function JSCompiler_unstubMethod_1(JSCompiler_unstubMethod_1_id, JSCompiler_unstubMethod_1_body) {" +
        "    return JSCompiler_stubMap[JSCompiler_unstubMethod_1_id] = JSCompiler_unstubMethod_1_body;" +
        "  }" +
        "  A.prototype.bar = JSCompiler_unstubMethod_1(1, function() {});" +
        "  function C() {}" +
        "}");

    assertEquals(printTree(expectedResult), printTree(result));
  }

  public void testMoveMethodToSameModule() throws Exception {
    String sourceCode =
        "function Foo() {}" +
        "Foo.prototype.bar = function() {};" +
        "function A() { A.prototype.baz = Foo.prototype.bar; }";

    Node root = parse(sourceCode);
    Node externs = null;

    CompilerOptions options = newCompilerOptions();
    options.setAllowAsync(true);
    options.setAllowAllTypes(true);
    options.setCrossModuleMethodMotion(true);

    Node result = compiler.compile(root, externs, options);

    Node expectedResult = parse(sourceCode);

    assertEquals(printTree(expectedResult), printTree(result));
  }

  public void testMoveMethodToSameModuleWithStub() throws Exception {
    String sourceCode =
        "function Foo() {" +
        "  Foo.prototype.bar = function() {};" +
        "}" +
        "function A() {" +
        "  A.prototype.baz = Foo.prototype.bar;" +
        "}" +
        "function B() {}";

    Node root = parse(sourceCode);
    Node externs = null;

    CompilerOptions options = newCompilerOptions();
    options.setAllowAsync(true);
    options.setAllowAllTypes(true);
    options.setCrossModuleMethodMotion(true);

    Node result = compiler.compile(root, externs, options);

    Node expectedResult = parse(
        "function Foo() {" +
        "  Foo.prototype.bar = function() {};" +
        "}" +
        "function A() {" +
        "  function JSCompiler_stubMethod_0(JSCompiler_stubMethod_0_id) {" +
        "    return function() {" +
        "      return Foo.prototype.bar.apply(this, arguments);" +
        "    };" +
        "  }" +
        "  var JSCompiler_stubMap = [];" +
        "  A.prototype.baz = JSCompiler_stubMethod_0(0);" +
        "}" +
        "function B() {}");

    assertEquals(printTree(expectedResult), printTree(result));
  }

  public void testMoveGetMethod() throws Exception {
    String sourceCode =
        "function Foo() {}" +
        "Object.defineProperty(Foo.prototype, 'x', {" +
        "  get: function() { return 1; }" +
        "});" +
        "function A() {}" +
        "A.prototype.y = Object.getPrototypeOf(Foo.prototype).x";

    Node root = parse(sourceCode);
    Node externs = null;

    CompilerOptions options = newCompilerOptions();
    options.setAllowAsync(true);
    options.setAllowAllTypes(true);
    options.setCrossModuleMethodMotion(true);

    Node result = compiler.compile(root, externs, options);

    // Get methods cannot be moved cross-module as they don't return a function
    Node expectedResult = parse(sourceCode);
    assertEquals(printTree(expectedResult), printTree(result));
  }

  public void testMoveSetMethod() throws Exception {
    String sourceCode =
        "function Foo() {}" +
        "Object.defineProperty(Foo.prototype, 'x', {" +
        "  set: function(value) { this.y = value; }" +
        "});" +
        "function A() {}" +
        "A.prototype.y = Foo.prototype.x";

    Node root = parse(sourceCode);
    Node externs = null;

    CompilerOptions options = newCompilerOptions();
    options.setAllowAsync(true);
    options.setAllowAllTypes(true);
    options.setCrossModuleMethodMotion(true);

    try {
      Node result = compiler.compile(root, externs, options);
      fail("Expected CompilerException");
    } catch (CompilerException e) {
      // Expected error
    }
  }

  public void testMoveSetMethodToSameModule() throws Exception {
    String sourceCode =
        "function Foo() {}" +
        "Object.defineProperty(Foo.prototype, 'x', {" +
        "  set: function(value) { this.y = value; }" +
        "});" +
        "function A() { A.prototype.z = Foo.prototype.x; }";

    Node root = parse(sourceCode);
    Node externs = null;

    CompilerOptions options = newCompilerOptions();
    options.setAllowAsync(true);
    options.setAllowAllTypes(true);
    options.setCrossModuleMethodMotion(true);

    Node result = compiler.compile(root, externs, options);

    // Set methods cannot be moved cross-module as they don't return a function
    Node expectedResult = parse(sourceCode);
    assertEquals(printTree(expectedResult), printTree(result));
  }
}