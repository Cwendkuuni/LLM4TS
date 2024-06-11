import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects_FunctionCallWithNoResult_Warning() {
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "function testFunction() {} testFunction();";
    Node rootNode = Parser.parse(sourceCode, null);

    compiler.setOption(CompilerOptions.Warnings, CheckSideEffects.USELESS_CODE_ERROR.name());
    NodeTraversal.traverse(compiler, rootNode, checkSideEffects);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertFalse(warnings.isEmpty());
    assertEquals(1, warnings.size());
    Node warningNode = warnings.get(0);
    assertEquals(Token.FUNCTION_DEF, warningNode.getType());
    assertEquals("testFunction", warningNode.getString());

    Node callNode = NodeUtil.getFirstChild(warningNode);
    assertEquals(Token.CALL_EXPR, callNode.getType());
  }

  @Test
  public void testCheckSideEffects_EmptyBlock_Warning() {
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "{}";
    Node rootNode = Parser.parse(sourceCode, null);

    compiler.setOption(CompilerOptions.Warnings, CheckSideEffects.USELESS_CODE_ERROR.name());
    NodeTraversal.traverse(compiler, rootNode, checkSideEffects);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertFalse(warnings.isEmpty());
    assertEquals(1, warnings.size());
    Node warningNode = warnings.get(0);
    assertEquals(Token.BLOCK, warningNode.getType());
  }

  @Test
  public void testCheckSideEffects_StringConcatenation_NoWarning() {
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "var str = \"Hello, \"; str += \"world!\";";
    Node rootNode = Parser.parse(sourceCode, null);

    compiler.setOption(CompilerOptions.Warnings, CheckSideEffects.USELESS_CODE_ERROR.name());
    NodeTraversal.traverse(compiler, rootNode, checkSideEffects);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertTrue(warnings.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffects_ProtectedCode_AssertionError() {
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "function testFunction() {" +
        "  var a = 1; " +
        "  JSCOMPILER_PRESERVE(a); " +
        "} testFunction();";
    Node rootNode = Parser.parse(sourceCode, null);

    compiler.setOption(CompilerOptions.Warnings, CheckSideEffects.USELESS_CODE_ERROR.name());
    NodeTraversal.traverse(compiler, rootNode, checkSideEffects);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertTrue(warnings.isEmpty());

    // This should throw an AssertionError as the protected code should not be reached.
    Node protectedNode = NodeUtil.getFirstChild(NodeUtil.getFirstChild(NodeUtil.getFirstChild(rootNode)));
    assertNull(protectedNode);
  }

  @Test
  public void testCheckSideEffects_ProtectedCodeIsRemoved() {
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    StripProtection stripProtection = new StripProtection(compiler);

    String sourceCode = "function testFunction() {" +
        "  var a = 1; " +
        "  JSCOMPILER_PRESERVE(a); " +
        "} testFunction();";
    Node rootNode = Parser.parse(sourceCode, null);

    compiler.setOption(CompilerOptions.Warnings, CheckSideEffects.USELESS_CODE_ERROR.name());
    NodeTraversal.traverse(compiler, rootNode, checkSideEffects);
    NodeTraversal.traverse(compiler, rootNode, stripProtection);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertTrue(warnings.isEmpty());

    // After the stripProtection pass, the protected code should be removed.
    Node protectedNode = NodeUtil.getFirstChild(NodeUtil.getFirstChild(NodeUtil.getFirstChild(rootNode)));
    assertNull(protectedNode);
  }
}