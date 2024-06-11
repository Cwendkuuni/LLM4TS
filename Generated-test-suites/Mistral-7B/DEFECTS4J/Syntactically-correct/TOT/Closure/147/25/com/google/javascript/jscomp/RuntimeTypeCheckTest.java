import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    String jsCode = "function foo(x) {" +
        "if (typeof x === 'number') {" +
        "  return x * 2;" +
        "} else if (x instanceof String) {" +
        "  return x.length;" +
        "} else {" +
        "  throw new Error('Invalid input')" +
        "}";

    CompilerInput input = new CompilerInput(
        "test", "test.js", jsCode, "test.js");
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = NodeUtil.parseCompilationUnit(jsCode);
    runtimeTypeCheck.process(null, root);

    Node result = compiler.getOutput(input, null);
    assertEquals("Expected number or String type but got other type.",
        "function foo(x) {" +
            "checkType(x, [" +
                "valueChecker(Number.prototype)," +
                "valueChecker(String.prototype)" +
            "]);" +
            "if (typeof x === 'number') {" +
                "return x * 2;" +
            "}" +
            "else if (x instanceof String) {" +
                "return x.length;" +
            "}" +
            "else {" +
                "throw new Error('Invalid input')" +
            "}" +
        "}", result.toString());

    // Test with number type
    String jsCodeWithNumber = "function foo(x) {" +
        "return x * 2;";
    input = new CompilerInput(
        "test", "test.js", jsCodeWithNumber, "test.js");
    compiler.setInput(input);
    compiler.addPass(runtimeTypeCheck);
    compiler.compileSingleFile();

    Node rootWithNumber = NodeUtil.parseCompilationUnit(compiler.getOutput(input, null).getCode().getCode());
    Node checkTypeCall = NodeUtil.getFirstDescendantOfType(rootWithNumber, Node.TokenType.CALL, "checkType");
    Node firstParam = checkTypeCall.getFirstChild();
    Node numberChecker = NodeUtil.getFirstDescendantOfType(firstParam, Node.TokenType.CALL, "valueChecker");
    assertNotNull(numberChecker);
    assertEquals("Value checker for Number", numberChecker.getFirstChild().toString(), "Number.prototype");

    // Test with string type
    String jsCodeWithString = "function foo(x) {" +
        "return x.length;";
    input = new CompilerInput(
        "test", "test.js", jsCodeWithString, "test.js");
    compiler.setInput(input);
    compiler.addPass(runtimeTypeCheck);
    compiler.compileSingleFile();

    rootWithNumber = NodeUtil.parseCompilationUnit(compiler.getOutput(input, null).getCode().getCode());
    checkTypeCall = NodeUtil.getFirstDescendantOfType(rootWithNumber, Node.TokenType.CALL, "checkType");
    firstParam = checkTypeCall.getFirstChild();
    Node stringChecker = NodeUtil.getFirstDescendantOfType(firstParam, Node.TokenType.CALL, "valueChecker");
    assertNotNull(stringChecker);
    assertEquals("Value checker for String", stringChecker.getFirstChild().toString(), "String.prototype");

    // Test with invalid type
    String jsCodeWithInvalidType = "function foo(x) {" +
        "throw new Error('Invalid input')";
    input = new CompilerInput(
        "test", "test.js", jsCodeWithInvalidType, "test.js");
    compiler.setInput(input);
    compiler.addPass(runtimeTypeCheck);
    compiler.compileSingleFile();

    rootWithNumber = NodeUtil.parseCompilationUnit(compiler.getOutput(input, null).getCode().getCode());
    checkTypeCall = NodeUtil.getFirstDescendantOfType(rootWithNumber, Node.TokenType.CALL, "checkType");
    firstParam = checkTypeCall.getFirstChild();
    Node[] checkers = NodeUtil.getAllDescendantsOfType(firstParam, Node.TokenType.CALL);
    assertTrue("Expected at least one checker.", checkers.length > 0);
    assertFalse("Invalid type should not match any checker.",
        NodeUtil.getFirstDescendantOfType(checkers[0], Node.TokenType.EQUALS) != null);

  }
}