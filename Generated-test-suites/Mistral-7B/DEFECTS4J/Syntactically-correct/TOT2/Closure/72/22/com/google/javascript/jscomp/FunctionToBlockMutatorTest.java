import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionToBlockMutator.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test class for FunctionToBlockMutator.
 */
public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("UNUSED_EXPORT", "QUIRK");
    compiler = new AbstractCompiler(options);
    safeNameIdSupplier = () -> compiler.getUniqueNameIdSupplier().get();
  }

  @org.junit.Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.createFunctionDeclaration(
        "testFunction", null, null, null, null, null);
    Node callNode = NodeFactory.newCall("testFunction", null);
    Node resultName = NodeFactory.newName("result");
    Node injectableBlock = mutate(fnName, fnNode, callNode, resultName, false, false);

    assertEquals(NodeUtil.newExpr(injectableBlock), injectableBlock);
  }

  @org.junit.Test
  public void testMutateFunctionWithOneArgument() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.newFunctionDeclaration(
        "testFunction", null, null, null, null, null,
        NodeFactory.newList(NodeFactory.newName("arg1")), null, null);
    Node callNode = NodeFactory.newCall("testFunction", NodeFactory.newList(
        NodeFactory.newNumberLiteral(42)));
    Node resultName = NodeFactory.newName("result");
    Node injectableBlock = mutate(fnName, fnNode, callNode, resultName, false, false);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test
  public void testMutateFunctionWithMultipleArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.newFunctionDeclaration(
        "testFunction", null, null, null, null, null,
        NodeFactory.newList(
            NodeFactory.newName("arg1"),
            NodeFactory.newName("arg2")), null, null);
    Node callNode = NodeFactory.newCall("testFunction", NodeFactory.newList(
        NodeFactory.newNumberLiteral(42), NodeFactory.newString("hello")));
    Node resultName = NodeFactory.newName("result");
    Node injectableBlock = mutate(fnName, fnNode, callNode, resultName, false, false);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test
  public void testMutateFunctionWithLoopCall() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.newFunctionDeclaration(
        "testFunction", null, null, null, null, null, null, null);
    Node callNode = NodeFactory.newCall("testFunction", null);
    Node resultName = NodeFactory.newName("result");
    Node injectableBlock = mutate(fnName, fnNode, callNode, resultName, true, false);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test
  public void testMutateFunctionWithArgumentsAndLoopCall() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.newFunctionDeclaration(
        "testFunction", null, null, null, null, null,
        NodeFactory.newList(
            NodeFactory.newName("arg1"),
            NodeFactory.newName("arg2")), null, null);
    Node callNode = NodeFactory.newCall("testFunction", NodeFactory.newList(
        NodeFactory.newNumberLiteral(42), NodeFactory.newString("hello")));
    Node resultName = NodeFactory.newName("result");
    Node injectableBlock = mutate(fnName, fnNode, callNode, resultName, true, false);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test
  public void testReplaceReturns() {
    String labelName = "foo";
    String resultName = "result";
    Node block = NodeFactory.newBlock(
        NodeFactory.newReturn(NodeFactory.newNumberLiteral(42)));
    Node injectableBlock = replaceReturns(block, resultName, labelName, false);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test
  public void testReplaceReturnsWithResult() {
    String labelName = "foo";
    String resultName = "result";
    Node resultNode = NodeFactory.newNumberLiteral(42);
    Node block = NodeFactory.newBlock(
        NodeFactory.newReturn(resultNode));
    Node injectableBlock = replaceReturns(block, resultName, labelName, true);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test
  public void testReplaceReturnsWithLabelAndResult() {
    String labelName = "foo";
    String resultName = "result";
    Node label = NodeFactory.newLabel(labelName);
    Node breakNode = NodeFactory.newBreak(labelName);
    Node resultNode = NodeFactory.newNumberLiteral(42);
    Node block = NodeFactory.newBlock(
        label,
        NodeFactory.newAssign(NodeFactory.newName(resultName), resultNode),
        breakNode);
    Node injectableBlock = replaceReturns(block, resultName, labelName, true);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test
  public void testReplaceReturnsWithEmptyFunction() {
    String labelName = "foo";
    String resultName = "result";
    Node block = NodeFactory.newBlock(NodeFactory.newReturn(null));
    Node injectableBlock = replaceReturns(block, resultName, labelName, false);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test
  public void testReplaceReturnsWithEmptyFunctionAndResult() {
    String labelName = "foo";
    String resultName = "result";
    Node label = NodeFactory.newLabel(labelName);
    Node breakNode = NodeFactory.newBreak(labelName);
    Node block = NodeFactory.newBlock(
        label, breakNode);
    Node injectableBlock = replaceReturns(block, resultName, labelName, true);

    // TODO(you): Check that injectableBlock is the expected result.
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testReplaceReturnsWithNullBlock() {
    String labelName = "foo";
    String resultName = "result";
    replaceReturns(null, resultName, labelName, false);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testReplaceReturnsWithNullLabelName() {
    String labelName = null;
    String resultName = "result";
    replaceReturns(NodeFactory.newBlock(), resultName, labelName, false);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testReplaceReturnsWithEmptyResultName() {
    String labelName = "foo";
    String resultName = "";
    replaceReturns(NodeFactory.newBlock(), resultName, labelName, false);
  }
}