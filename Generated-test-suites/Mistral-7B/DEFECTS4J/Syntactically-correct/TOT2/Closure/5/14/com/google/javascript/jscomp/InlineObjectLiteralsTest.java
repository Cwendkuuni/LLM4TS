import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassParam;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String code = "function f() {" +
        "  var x = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: {d: 3}" +
        "  };" +
        "  var y = x.b;" +
        "  return x.c.d;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    Node root = new Node(Token.PROGRAM);
    root.setSourceFile("test.js");
    Node externs = new Node(Token.PROGRAM);
    Node body = new Node(Token.BLOCK);
    root.addChildToBack(externs);
    root.addChildToBack(body);
    NodeUtil.parseAndBind(code, externs, body);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "temp_");
    CompilerPassParam param = new CompilerPassParam(pass);
    passManager.addPass(param);
    passManager.runPasses(root);

    Node result = body.getFirstChild();

    // Test the original functionality
    Object funcResult = compiler.compile(code, null).eval();
    assertEquals(3, funcResult);

    // Test the inlined functionality
    Object resultValue = result.eval();
    Node inlineX = result.getFirstChild();
    Node inlineAC = inlineX.getFirstChild();
    Node inlineB = inlineX.getChildAtIndex(1);
    Node inlineCD = inlineB.getFirstChild();
    Node inlineD = inlineCD.getFirstChild();

    assertEquals(1, inlineAC.eval());
    assertEquals(2, inlineB.eval());
    assertEquals(3, inlineD.eval());

    // Test edge cases
    code = "function g() {" +
        "  var x = {" +
        "    a: {b: {c: 1}}," +
        "    b: 2," +
        "    c: 3," +
        "    d: function() {return 4}" +
        "  };" +
        "  var y = x.b;" +
        "  return x.c;" +
        "}";

    root = new Node(Token.PROGRAM);
    root.setSourceFile("test2.js");
    externs = new Node(Token.PROGRAM);
    body = new Node(Token.BLOCK);
    root.addChildToBack(externs);
    root.addChildToBack(body);
    NodeUtil.parseAndBind(code, externs, body);

    passManager.reset();
    passManager.runPasses(root);

    result = body.getFirstChild();

    Object edgeCaseResult = compiler.compile(code, null).eval();
    assertEquals(3, edgeCaseResult);

    Node inlineXEdge = result.getFirstChild();
    Node inlineAB = inlineXEdge.getFirstChild();
    Node inlineAC1 = inlineAB.getFirstChild();
    Node inlineBEdge = inlineAB.getChildAtIndex(1);
    Node inlineC = inlineBEdge.getFirstChild();

    assertEquals(1, inlineAC1.eval());
    assertEquals(2, inlineBEdge.eval());
    assertEquals(3, inlineC.eval());

    // Test exception handling
    code = "function h() {" +
        "  var x = {" +
        "    a: {b: {c: 1}}," +
        "    b: 2," +
        "    c: function() {throw new Error('Boom!')}" +
        "  };" +
        "  return x.c();" +
        "}";

    root = new Node(Token.PROGRAM);
    root.setSourceFile("test3.js");
    externs = new Node(Token.PROGRAM);
    body = new Node(Token.BLOCK);
    root.addChildToBack(externs);
    root.addChildToBack(body);
    NodeUtil.parseAndBind(code, externs, body);

    passManager.reset();
    passManager.runPasses(root);

    result = body.getFirstChild();
    try {
      resultValue = result.eval();
      fail("Expected an error");
    } catch (Error e) {
      assertEquals("Boom!", e.getMessage());
    }
  }
}