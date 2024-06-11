import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String jsCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    HotSwapCompilerPass compilerPass = new ScopedAliases(null, null, null);
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    compiler.init();
    Node root = compiler.parse(jsCode, "Test.js");
    compilerPass.process(null, root);

    // Assert that the alias "dom" has been correctly transformed.
    Node transformedCode = root.getFirstChild();
    assertNotNull(transformedCode);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        transformedCode.getString());

    // Test invalid usage of goog.scope
    jsCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "});";

    root = compiler.parse(jsCode, "Test2.js");
    compilerPass.process(null, root);

    // Assert that there are errors reporting the usage of goog.scope more than once.
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isError());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getName());
  }

  @Test
  public void testScopedAliasesWithFunction() {
    String jsCode =
        "goog.scope(function foo() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  function bar() {" +
        "    goog.dom.createElement(DIV);" +
        "  }" +
        "  bar();" +
        "});";

    HotSwapCompilerPass compilerPass = new ScopedAliases(null, null, null);
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    compiler.init();
    Node root = compiler.parse(jsCode, "Test3.js");
    compilerPass.process(null, root);

    // Assert that the alias "dom" has been correctly transformed in the function "bar".
    Node transformedCode = root.getFirstChild();
    assertNotNull(transformedCode);
    Node barFunction = transformedCode.getChildAtIndex(1);
    Node barCode = barFunction.getLastChild();
    assertNotNull(barCode);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        barCode.getString());
  }

  @Test
  public void testScopedAliasesWithMultipleAliases() {
    String jsCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var arr = [];" +
        "  var num = 0;" +
        "  arr.push(function() {" +
        "    var div = goog.dom.createElement(DIV);" +
        "    num++;" +
        "  });" +
        "  arr[0]();" +
        "  arr.push(function() {" +
        "    var div = goog.dom.createElement(DIV);" +
        "    num++;" +
        "  });" +
        "  arr[1]();" +
        "});";

    HotSwapCompilerPass compilerPass = new ScopedAliases(null, null, null);
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    compiler.init();
    Node root = compiler.parse(jsCode, "Test4.js");
    compilerPass.process(null, root);

    // Assert that the aliases "dom" and "DIV" have been correctly transformed in both functions.
    Node transformedCode = root.getFirstChild();
    assertNotNull(transformedCode);
    Node arrFunction1 = transformedCode.getChildAtIndex(1);
    Node arrFunction2 = transformedCode.getChildAtIndex(3);
    Node function1Code = arrFunction1.getLastChild();
    Node function2Code = arrFunction2.getLastChild();
    assertNotNull(function1Code);
    assertNotNull(function2Code);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        function1Code.getString());
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        function2Code.getString());

    // Assert that the variable "num" has not been transformed, as it is not an alias.
    Node numNode = transformedCode.getChildAtIndex(2).getLastChild();
    assertEquals("num", numNode.getString());
  }

  @Test
  public void testScopedAliasesError1() {
    String jsCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "  });" + // Missing anonymous function
        "  goog.dom.createElement(DIV);" +
        "});";

    HotSwapCompilerPass compilerPass = new ScopedAliases(null, null, null);
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    compiler.init();
    Node root = compiler.parse(jsCode, "Test5.js");
    compilerPass.process(null, root);

    // Assert that there is an error reporting the missing anonymous function inside the nested goog.scope call.
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isError());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostic.getName());
  }

  @Test
  public void testScopedAliasesError2() {
    String jsCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    this.x = 1;" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});";

    HotSwapCompilerPass compilerPass = new ScopedAliases(null, null, null);
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    compiler.init();
    Node root = compiler.parse(jsCode, "Test6.js");
    compilerPass.process(null, root);

    // Assert that there is an error reporting the usage of "this" inside a goog.scope function.
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isError());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnostic.getName());
  }

  @Test
  public void testScopedAliasesError3() {
    String jsCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    return;" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});";

    HotSwapCompilerPass compilerPass = new ScopedAliases(null, null, null);
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    compiler.init();
    Node root = compiler.parse(jsCode, "Test7.js");
    compilerPass.process(null, root);

    // Assert that there is an error reporting the usage of "return" inside a goog.scope function.
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isError());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnostic.getName());
  }

  @Test
  public void testScopedAliasesError4() {
    String jsCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    throw new Error('Oops');" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});";

    HotSwapCompilerPass compilerPass = new ScopedAliases(null, null, null);
    AbstractCompiler compiler = new AbstractCompiler(null, null, null);
    compiler.init();
    Node root = compiler.parse(jsCode, "Test8.js");
    compilerPass.process(null, root);

    // Assert that there is an error reporting the usage of "throw" inside a goog.scope function.
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isError());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", diagnostic.getName());
  }
}