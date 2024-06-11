import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE =
      "function foo(x, y) {"
          + "var a = 1;"
          + "var b = 2;"
          + "var c = 3;"
          + "var z = 0;"
          + "x = x || 1;"
          + "y = y || 2;"
          + "z = x + y;"
          + "if (x > y) {"
          + "  a = b;"
          + "} else {"
          + "  a = c;"
          + "}"
          + "return z;"
          + "}";

  private Scope jsScope;
  private MustBeReachingVariableDef analyzer;

  @Before
  public void setup() {
    jsScope = new Scope();
    analyzer = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new AbstractCompiler(), parseTree(TEST_CODE), jsScope),
        jsScope, new AbstractCompiler());
  }

  @Test
  public void testSimpleAssignment() {
    Node useNode = parseTree(TEST_CODE).getFirstChild().getLastChild();
    Node defNode = analyzer.getDef("z", useNode);
    assertNotNull(defNode);
    assertEquals(getFirstChild(getFirstChild(getFirstChild(parseTree(TEST_CODE)))),
        defNode);
  }

  @Test
  public void testAssignmentWithCondition() {
    Node useNode = parseTree(TEST_CODE).getLastChild();
    Node defNode = analyzer.getDef("a", useNode);
    assertNotNull(defNode);
    assertEquals(isAssignmentOp(getFirstChild(getFirstChild(getFirstChild(parseTree(TEST_CODE)))),
        getFirstChild(getFirstChild(getFirstChild(parseTree(TEST_CODE))))),
        defNode.node);
  }

  @Test
  public void testSimpleVariableUsage() {
    Node useNode = parseTree(TEST_CODE).getFirstChild().getLastChild();
    Node defNode = analyzer.getDef("x", useNode);
    assertNotNull(defNode);
    assertEquals(getFirstChild(getFirstChild(parseTree(TEST_CODE))), defNode.node);
  }

  @Test
  public void testVariableUsageWithCondition() {
    Node useNode = parseTree(TEST_CODE).getLastChild();
    Node defNode = analyzer.getDef("x", useNode);
    assertNotNull(defNode);
    assertEquals(getFirstChild(parseTree(TEST_CODE)).getFirstChild(),
        defNode.node);
  }

  @Test
  public void testReachingDefinitionOfEscapedVariable() {
    String escapedVar = "arguments";
    Node useNode = parseTree(TEST_CODE).getLastChild();
    Node defNode = analyzer.getDef(escapedVar, useNode);
    assertNotNull(defNode);
    assertEquals(getNodeByString(jsScope, escapedVar), defNode.node);
  }

  @Test
  public void testReachingDefinitionOfVariableWithCatchBlock() {
    String variableName = "x";
    String catchBlockName = "catch";
    String testCode =
        "try {"
        + "  throw new Error('test');"
        + "  var " + variableName + " = 5;"
        + "} catch (" + catchBlockName + " " + variableName + ")" + " {"
        + "  " + variableName + " = 1;"
        + "}";

    Scope scope = new Scope();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new AbstractCompiler(), parseTree(testCode), scope),
        scope, new AbstractCompiler());
    Node useNode = getNodeByName(parseTree(testCode), variableName);
    Node defNode = analyzer.getDef(variableName, useNode);
    assertNotNull(defNode);
    assertEquals(getNodeByName(parseTree(testCode), catchBlockName), defNode.node);
  }

  @Test
  public void testReachingDefinitionOfVariableWithParameter() {
    String variableName = "x";
    Node useNode = parseTree(TEST_CODE).getFirstChild().getLastChild();
    Node defNode = analyzer.getDef(variableName, useNode);
    assertNotNull(defNode);
    assertEquals(getFirstChild(parseTree(TEST_CODE)).getFirstChild().getFirstChild(),
        defNode.node);
  }

  @Test
  public void testReachingDefinitionOfVariableWithFunctionCall() {
    String variableName = "z";
    Node useNode = parseTree(TEST_CODE).getLastChild();
    Node defNode = analyzer.getDef(variableName, useNode);
    assertNotNull(defNode);
    assertEquals(getLastChild(getLastChild(parseTree(TEST_CODE))), defNode.node);
  }

  @Test
  public void testReachingDefinitionOfVariableWithFunctionCallAndCondition() {
    String variableName = "z";
    Node useNode = parseTree(TEST_CODE).getLastChild();
    Node defNode = analyzer.getDef(variableName, useNode);
    assertNotNull(defNode);
    Node conditionNode = getNodeByString(parseTree(TEST_CODE), "if");
    Node defConditionNode = analyzer.getDef("x", conditionNode);
    assertNotNull(defConditionNode);
    assertEquals(isComparisonOp(getFirstChild(getFirstChild(conditionNode)), getFirstChild(getFirstChild(getFirstChild(parseTree(TEST_CODE))))),
        defConditionNode.node);
    Node funcCallNode = getLastChild(getLastChild(parseTree(TEST_CODE)));
    Node defFuncCallNode = analyzer.getDef("z", funcCallNode);
    assertNotNull(defFuncCallNode);
    assertEquals(funcCallNode, defFuncCallNode.node);
  }

  @Test
  public void testReachingDefinitionOfLocalVariableWithLoop() {
    String variableName = "i";
    String testCode =
        "for (var i = 0; i < 5; i++) {"
        + "  alert(i);"
        + "}"
        + "alert(i);";

    Scope scope = new Scope();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new AbstractCompiler(), parseTree(testCode), scope),
        scope, new AbstractCompiler());
    Node useNode = getLastChild(getLastChild(parseTree(testCode)));
    Node defNode = analyzer.getDef(variableName, useNode);
    assertNotNull(defNode);
    assertEquals(getNodeByName(parseTree(testCode), "for"), defNode.node);
  }

  @Test
  public void testReachingDefinitionOfLocalVariableWithNestedCondition() {
    String variableName = "x";
    String testCode =
        "if (1 > 2) {"
        + "  if (3 > 4) {"
        + "    var x = 5;"
        + "  } else {"
        + "    var x = 6;"
        + "  }"
        + "} else {"
        + "  var x = 7;"
        + "}"
        + "alert(x);";

    Scope scope = new Scope();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new AbstractCompiler(), parseTree(testCode), scope),
        scope, new AbstractCompiler());
    Node useNode = getLastChild(parseTree(testCode));
    Node defNode = analyzer.getDef(variableName, useNode);
    assertNotNull(defNode);
    Node firstIfNode = getNodeByString(parseTree(testCode), "if");
    Node firstIfCondition = getFirstChild(getFirstChild(firstIfNode));
    Node secondIfNode = getNodeByString(parseTree(testCode), "if");
    Node secondIfCondition = getFirstChild(getFirstChild(secondIfNode));
    Node defFirstIfCondition = analyzer.getDef("x", firstIfCondition);
    assertNotNull(defFirstIfCondition);
    assertEquals(firstIfCondition, defFirstIfCondition.node);
    Node defSecondIfCondition = analyzer.getDef("x", secondIfCondition);
    assertNotNull(defSecondIfCondition);
    assertEquals(secondIfCondition, defSecondIfCondition.node);
  }

  @Test
  public void testReachingDefinitionOfVariableWhenDependsOnOuterScopeVariable() {
    String variableName = "x";
    String outerVariableName = "y";
    String testCode =
        "function foo(x, y) {"
        + "  var z = 0;"
        + "  if (x > y) {"
        + "    x = y;"
        + "  }"
        + "  x = x + 1;"
        + "  return x;"
        + "}"
        + "alert(foo(1, 2));";

    Scope scope = new Scope();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new AbstractCompiler(), parseTree(testCode), scope),
        scope, new AbstractCompiler());
    Node useNode = getLastChild(parseTree(testCode));
    Node defNode = analyzer.getDef(variableName, useNode);
    assertTrue(analyzer.dependsOnOuterScopeVars(variableName, useNode));
    assertEquals(getNodeByName(parseTree(testCode), "y"), defNode.node);
  }

  @Test(expected = AssertionError.class)
  public void testReachingDefinitionOfUndefinedVariable() {
    String variableName = "undefinedVariable";
    Node useNode = parseTree(TEST_CODE).getLastChild();
    analyzer.getDef(variableName, useNode);
  }

  @Test(expected = AssertionError.class)
  public void testReachingDefinitionOfVariableThatIsFunction() {
    String variableName = "foo";
    Node useNode = parseTree(TEST_CODE).getFirstChild().getLastChild();
    analyzer.getDef(variableName, useNode);
  }

  @Test(expected = AssertionError.class)
  public void testReachingDefinitionOfVariableThatIsEscaped() {
    String variableName = "arguments";
    Node useNode = parseTree(TEST_CODE).getLastChild();
    analyzer.getDef(variableName, useNode);
  }

  @Test
  public void testReachingDefinitionOfVariableWithComplexCondition() {
    String variableName = "x";
    String testCode =
        "function foo(x) {"
        + "  if (x > 2 && x < 5) {"
        + "    var x = 10;"
        + "  } else {"
        + "    var x = 20;"
        + "  }"
        + "  return x;"
        + "}"
        + "alert(foo(3));";

    Scope scope = new Scope();
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new AbstractCompiler(), parseTree(testCode), scope),
        scope, new AbstractCompiler());
    Node useNode = getLastChild(parseTree(testCode));
    Node defNode = analyzer.getDef(variableName, useNode);
    Node conditionNode = getNodeByString(parseTree(testCode), "if");
    Node firstCondition = getFirstChild(getFirstChild(conditionNode));
    Node secondCondition = getFirstChild(getFirstChild(getFirstChild(conditionNode)));
    Node defFirstCondition = analyzer.getDef("x", firstCondition);
    Node defSecondCondition = analyzer.getDef("x", secondCondition);
    assertNotNull(defFirstCondition);
    assertEquals(firstCondition, defFirstCondition.node);
    assertNotNull(defSecondCondition);
    assertEquals(secondCondition, defSecondCondition.node);
    assertEquals(getNodeByName(parseTree(testCode), "x"), defNode.node);
  }

  // Add test cases for the other methods in the MustBeReachingVariableDef class.
  // Be sure to cover all edge cases and error scenarios.
}