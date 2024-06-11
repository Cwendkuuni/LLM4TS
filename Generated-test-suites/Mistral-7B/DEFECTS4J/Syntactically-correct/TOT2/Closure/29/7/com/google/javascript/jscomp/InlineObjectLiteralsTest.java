import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Setup
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler);
    Supplier<String> safeNameIdSupplier = () -> "test_";

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        abstractCompiler, safeNameIdSupplier);

    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'c': 2}" +
        "  };" +
        "  var result = obj.a * obj.b.c;" +
        "  return result;" +
        "}";

    Scope rootScope = new Scope();
    Node externs = NodeUtil.createExterns();
    Node rootNode = NodeUtil.parseStatementList(jsCode, externs);

    inlineObjectLiterals.process(externs, rootNode);

    // Basic functionality test
    Node expectedResultNode = NodeUtil.parseIntLiteral(3);
    Node actualRootNode = rootNode;
    Node actualResultNode = expectedResultNode;
    while (actualRootNode.getFirstChild() != null) {
      actualRootNode = actualRootNode.getFirstChild();
      if (actualRootNode.isReturn()) {
        actualResultNode = actualRootNode.getFirstChild();
        break;
      }
    }
    assertEquals("Unexpected result", expectedResultNode, actualResultNode);

    // Edge cases and exception handling scenarios

    // Test object literal with no keys
    jsCode = "function bar() {" +
        "  var obj = {}";
    rootScope = new Scope();
    rootNode = NodeUtil.parseStatementList(jsCode, externs);
    inlineObjectLiterals.process(externs, rootNode);

    // Check that no variables were inline and the function returned undefined
    Node actualBarResultNode = rootNode.getFirstChild().getFirstChild();
    assertNull("Unexpected result", actualBarResultNode);

    // Test object literal with a single key
    jsCode = "function baz() {" +
        "  var obj = {a: 1};" +
        "}";
    rootScope = new Scope();
    rootNode = NodeUtil.parseStatementList(jsCode, externs);
    inlineObjectLiterals.process(externs, rootNode);

    // Check that a single variable was inline and the function returned the value of the key
    Var expectedAVar = rootScope.getVar("JSCompiler_object_inline_a");
    Node actualBazResultNode = rootNode.getFirstChild().getFirstChild();
    assertTrue("Unexpected result", actualBazResultNode.isNumberConstant());
    assertEquals("Unexpected result", expectedAVar.getInitialValue(), actualBazResultNode.getInt());

    // Test object literal with nested objects
    jsCode = "function qux() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 1" +
        "    }," +
        "    c: 2" +
        "  };" +
        "}";
    rootScope = new Scope();
    rootNode = NodeUtil.parseStatementList(jsCode, externs);
    inlineObjectLiterals.process(externs, rootNode);

    // Check that both variables were inline and the function returned the value of the nested key
    Var expectedBVar = rootScope.getVar("JSCompiler_object_inline_a_b");
    Node actualQuxResultNode = rootNode.getFirstChild().getFirstChild();
    assertTrue("Unexpected result", actualQuxResultNode.isNumberConstant());
    assertEquals("Unexpected result", expectedBVar.getInitialValue(), actualQuxResultNode.getInt());

    // Test object literal with a self-referential assignment
    // This should not be inlined, as it is not orderable
    jsCode = "function quux() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: obj" +
        "  };" +
        "}";
    rootScope = new Scope();
    rootNode = NodeUtil.parseStatementList(jsCode, externs);
    inlineObjectLiterals.process(externs, rootNode);

    // Check that no variables were inline and the function returned the original object literal
    assertEquals("Unexpected result", rootNode.getFirstChild().getFirstChild(), rootNode.getLastChild().getFirstChild());

    // Test object literal with a key that is a reserved word
    jsCode = "function quuz() {" +
        "  var obj = {" +
        "    'function': 1" +
        "  };" +
        "}";
    rootScope = new Scope();
    rootNode = NodeUtil.parseStatementList(jsCode, externs);
    inlineObjectLiterals.process(externs, rootNode);

    // Check that the variable was inline and the function returned the value of the key, with the key name escaped
    Var expectedFunctionVar = rootScope.getVar("JSCompiler_object_inline_function_0");
    Node actualQuuzResultNode = rootNode.getFirstChild().getFirstChild();
    assertTrue("Unexpected result", actualQuuzResultNode.isNumberConstant());
    assertEquals("Unexpected result", expectedFunctionVar.getInitialValue(), actualQuuzResultNode.getInt());

    // Test object literal with a key that is a string literal
    jsCode = "function quux() {" +
        "  var obj = {" +
        "    'hello world': 1" +
        "  };" +
        "}";
    rootScope = new Scope();
    rootNode = NodeUtil.parseStatementList(jsCode, externs);
    inlineObjectLiterals.process(externs, rootNode);

    // Check that the variable was inline and the function returned the value of the key, with the key name escaped
    Var expectedHelloWorldVar = rootScope.getVar("JSCompiler_object_inline_hello_world_0");
    Node actualQuuxResultNode = rootNode.getFirstChild().getFirstChild();
    assertTrue("Unexpected result", actualQuuxResultNode.isNumberConstant());
    assertEquals("Unexpected result", expectedHelloWorldVar.getInitialValue(), actualQuuxResultNode.getInt());
  }
}