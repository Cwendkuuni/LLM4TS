import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.jscomp.JSType.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.NodeConstants;
import com.google.javascript.jscomp.NodeType;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

@RunWith(JUnit4.class)
public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testClosureReverseAbstractInterpreter() {
    CompilerOptions options = new CompilerOptions(new String[0]);
    JSTypeRegistry typeRegistry = new JSTypeRegistry(options);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);

    Node conditionNode = Node.newCall(CALL, "goog.isDef",
        Node.newQualifiedName(NAME, "goog", "isDef"), Node.newUndefined());

    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);

    JSType typeInformed = informedScope.getVariableValue(conditionNode);
    JSType expectedType = NO_OBJECT_TYPE.isDef();
    assertEquals(expectedType, typeInformed);

    conditionNode = Node.newCall(CALL, "goog.isDef",
        Node.newQualifiedName(NAME, "goog", "isDef"), Node.newInt(1));

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);

    typeInformed = informedScope.getVariableValue(conditionNode);
    expectedType = ARRAY_TYPE.isDef();
    assertEquals(expectedType, typeInformed);

    conditionNode = Node.newCall(CALL, "goog.isDef",
        Node.newQualifiedName(NAME, "goog", "isDef"), Node.newNull());

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);

    typeInformed = informedScope.getVariableValue(conditionNode);
    assertNull(typeInformed);

    conditionNode = Node.newCall(CALL, "goog.isDefAndNotNull",
        Node.newQualifiedName(NAME, "goog", "isDefAndNotNull"),
        Node.newQualifiedName(NAME, "goog", "isNull"), Node.newInt(1));

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);

    typeInformed = informedScope.getVariableValue(conditionNode);
    expectedType = INT_TYPE.isDef().and(NO_NULL_TYPE);
    assertEquals(expectedType, typeInformed);

    conditionNode = Node.newCall(CALL, "goog.isDefAndNotNull",
        Node.newQualifiedName(NAME, "goog", "isDefAndNotNull"),
        Node.newQualifiedName(NAME, "goog", "isNull"), Node.newNull());

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);

    typeInformed = informedScope.getVariableValue(conditionNode);
    assertNull(typeInformed);

    conditionNode = Node.newCall(CALL, "goog.isArray",
        Node.newQualifiedName(NAME, "goog", "isArray"), Node.newNull());

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);

    typeInformed = informedScope.getVariableValue(conditionNode);
    assertNull(typeInformed);

    conditionNode = Node.newCall(CALL, "goog.isArray",
        Node.newQualifiedName(NAME, "goog", "isArray"), Node.newInt(1));

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);

    typeInformed = informedScope.getVariableValue(conditionNode);
    expectedType = ARRAY_TYPE;
    assertEquals(expectedType, typeInformed);

    conditionNode = Node.newCall(CALL, "goog.isObject",
        Node.newQualifiedName(NAME, "goog", "isObject"), Node.newNull());

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);

    typeInformed = informedScope.getVariableValue(conditionNode);
    assertNull(typeInformed);

    conditionNode = Node.newCall(CALL, "goog.isObject",
        Node.newQualifiedName(NAME, "goog", "isObject"), Node.newObject(
            new ObjectType("myObject", new FunctionType(NULL_TYPE, NULL_TYPE))));

    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);

    typeInformed = informedScope.getVariableValue(conditionNode);
    expectedType = OBJECT_TYPE.isDef().and(FunctionType.instanceTypeAny());
    assertEquals(expectedType, typeInformed);
  }

  private static class CodingConvention {}
}