import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest extends CompilerPassTestCase {

  public static void main(String[] args) {
    runCompilerPassTest(InlineObjectLiteralsTest.class, "InlineObjectLiterals.java");
  }

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(getCompiler(), getSafeNameIdSupplier());
  }

  public void testInlineObjectLiterals() {
    String jsCode =
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {c: 2}" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b.c;" +
        "  return x + y;" +
        "}";

    Node root = parseJsCode(jsCode);

    runPassAndCheckResult(root);

    String expectedJsCode =
        "function foo() {" +
        "  var a = 1;" +
        "  var c = 2;" +
        "  return a + c;" +
        "}";

    String actualJsCode = getSourceCode(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  public void testInlineNestedObjectLiterals() {
    String jsCode =
        "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 1," +
        "      c: {" +
        "        d: 2" +
        "      }" +
        "    }," +
        "    e: 3" +
        "  };" +
        "  var y = obj.a.b;" +
        "  var z = obj.a.c.d;" +
        "  return y + z;" +
        "}";

    Node root = parseJsCode(jsCode);

    runPassAndCheckResult(root);

    String expectedJsCode =
        "function foo() {" +
        "  var b = 1;" +
        "  var d = 2;" +
        "  return b + d;" +
        "}";

    String actualJsCode = getSourceCode(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  public void testInlineEmptyObjectLiterals() {
    String jsCode =
        "function foo() {" +
        "  var obj = {}";

    Node root = parseJsCode(jsCode);

    runPassAndCheckResult(root);

    String expectedJsCode = "function foo() {}";

    String actualJsCode = getSourceCode(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  public void testInlineObjectLiteralsWithSymbolKeys() {
    String jsCode =
        "function foo() {" +
        "  var obj = {" +
        "    [Symbol('key1')]: 1," +
        "    [Symbol('key2')]: 2" +
        "  };" +
        "  var x = obj[Symbol('key1')]";

    Node root = parseJsCode(jsCode);

    runPassAndCheckResult(root);

    String expectedJsCode =
        "function foo() {" +
        "  var key1 = 1;" +
        "  return key1;" +
        "}";

    String actualJsCode = getSourceCode(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  public void testInlineObjectLiteralsWithFunctions() {
    String jsCode =
        "function foo() {" +
        "  var obj = {" +
        "    method: function() {return 1}" +
        "  };" +
        "  var x = obj.method()";

    Node root = parseJsCode(jsCode);

    runPassAndCheckResult(root);

    String expectedJsCode =
        "function foo() {" +
        "  var method = function() {return 1};" +
        "  return method()" +
        "}";

    String actualJsCode = getSourceCode(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  public void testInlineObjectLiteralsWithForLoop() {
    String jsCode =
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  for (var key in obj) {" +
        "    var value = obj[key];" +
        "    if (key == 'a'){" +
        "      var a = value;" +
        "    } else if (key == 'b'){" +
        "      var b = value;" +
        "    }" +
        "  }" +
        "  return a + b;" +
        "}";

    Node root = parseJsCode(jsCode);

    runPassAndCheckResult(root);

    String expectedJsCode =
        "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  return a + b;" +
        "}";

    String actualJsCode = getSourceCode(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  public void testInlineObjectLiteralsWithConditionalExpression() {
    String jsCode =
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  return (obj.a > obj.b) ? obj.a : obj.b;" +
        "}";

    Node root = parseJsCode(jsCode);

    runPassAndCheckResult(root);

    String expectedJsCode =
        "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  return (a > b) ? a : b;" +
        "}";

    String actualJsCode = getSourceCode(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  public void testInlineObjectLiteralsWithSpreadOperator() {
    String jsCode =
        "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  var obj2 = {" +
        "    c: 3," +
        "    d: 4" +
        "  };" +
        "  var result = {...obj1, ...obj2};" +
        "  return result.a + result.b + result.c + result.d;" +
        "}";

    Node root = parseJsCode(jsCode);

    runPassAndCheckResult(root);

    String expectedJsCode =
        "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = 3;" +
        "  var d = 4;" +
        "  return a + b + c + d;" +
        "}";

    String actualJsCode = getSourceCode(root);
    assertEquals(expectedJsCode, actualJsCode);
  }

  @Override
  protected AbstractCompiler getCompiler() {
    return new Compiler(new CompilerOptions());
  }

  @Override
  protected Supplier<String> getSafeNameIdSupplier() {
    return new Supplier<String>() {
      private int counter = 0;

      @Override
      public String get() {
        return String.valueOf(counter++);
      }
    };
  }

  @Override
  protected void runPass(Node externs, Node root) {
    getPass().process(externs, root);
  }

  @Override
  protected void checkResult(Node root) {
    // No need to check the result as it's already done in each test case.
  }
}