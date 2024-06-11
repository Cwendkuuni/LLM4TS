import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionArgumentInjector;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    Node fnNode = Node.newBlock(
        "return a + b;", null, "functionName");
    Node callNode = Node.newCall("functionName", null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node expectedResult = Node.newBlock(
        "result = a + b; break functionName_functionName_0;", null, null);

    Node actualResult = functionToBlockMutator.mutate(
        "functionName", fnNode, callNode, resultName, needsDefaultResult,
        isCallInLoop);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", Node.newNumber(1));
    argMap.put("arg2", Node.newNumber(2));

    Node fnNode = Node.newBlock(
        "return a + b;", null, "functionName");
    Node callNode = Node.newCall("functionName", null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node expectedResult = Node.newBlock(
        "var arg1 = arg1; var arg2 = arg2; result = arg1 + arg2; break functionName_functionName_0;",
        null, null);

    Node actualResult = functionToBlockMutator.mutate(
        "functionName", fnNode, callNode, resultName, needsDefaultResult,
        isCallInLoop);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testMutateFunctionWithAliases() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    Set<String> namesToAlias = Set.of("arg1");

    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", Node.newNumber(1));
    argMap.put("arg2", Node.newNumber(2));

    Node fnNode = Node.newBlock(
        "return a + b;", null, "functionName");
    Node callNode = Node.newCall("functionName", null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node expectedResult = Node.newBlock(
        "functionName__arg1 = arg1; result = functionName__arg1 + arg2; break functionName_functionName_0;",
        null, null);

    Node actualResult = functionToBlockMutator.mutate(
        "functionName", fnNode, callNode, resultName, needsDefaultResult,
        isCallInLoop, namesToAlias, argMap);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturns() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    String resultName = "result";
    String labelName = "labelName";
    boolean resultMustBeSet = true;

    Node block = Node.newBlock("return 1 + 2;", null, null);

    Node expectedResult = Node.newBlock(
        labelName + ": { 1 + 2; break " + labelName + "; }", null, null);

    if (resultMustBeSet) {
      expectedResult = Node.newBlock(
          labelName + ": { result = 1 + 2; break " + labelName + "; }", null, null);
    }

    Node actualResult = FunctionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturnsWithNullResultName() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);

    String labelName = "labelName";
    boolean resultMustBeSet = true;

    Node block = Node.newBlock("return 1 + 2;", null, null);

    Node expectedResult = Node.newBlock("1 + 2; break labelName;", null, null);

    Node actualResult = FunctionToBlockMutator.replaceReturns(block, null, labelName, resultMustBeSet);

    assertEquals(expectedResult, actualResult);

    // test null result
    actualResult = FunctionToBlockMutator.replaceReturns(block, null, null, resultMustBeSet);

    assertNull(actualResult);
  }
}