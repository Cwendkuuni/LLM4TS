import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private Node conditionNode;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    conditionNode = new Node();
    conditionNode.setType(Type.OBJECT);
  }

  @Test
  public void testIsDefTrue() {
    Node callee = new Node();
    callee.setType(Type.NAME);
    callee.setString("goog.isDef");

    Node paramNode = new Node();
    paramNode.setType(Type.NAME);
    paramNode.setString("someValue");

    callee.setLastChild(paramNode);

    when(typeRegistry.createUnionType(Type.NUMBER, Type.STRING))
        .thenReturn(Type.NUMBER);

    JSType expectedType = Type.NUMBER;
    Node expectedCondition = conditionNode;

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(callee, null, true);
    assertEquals(expectedType, scope.getVariableName(paramNode.getName()).getType());
    assertEquals(expectedCondition, scope);
  }

  @Test
  public void testIsDefFalse() {
    Node callee = new Node();
    callee.setType(Type.NAME);
    callee.setString("goog.isDef");

    Node paramNode = new Node();
    paramNode.setType(Type.NAME);
    paramNode.setString("nullValue");

    callee.setLastChild(paramNode);

    JSType expectedType = Type.VOID;
    Node expectedCondition = conditionNode;

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(callee, null, false);
    assertEquals(expectedType, scope.getVariableName(paramNode.getName()).getType());
    assertEquals(expectedCondition, scope);
  }

  @Test
  public void testIsDefNullParameter() {
    Node callee = new Node();
    callee.setType(Type.NAME);
    callee.setString("goog.isDef");

    Node paramNode = new Node();
    paramNode.setType(Type.NULL);

    callee.setLastChild(paramNode);

    JSType expectedType = Type.VOID;
    Node expectedCondition = conditionNode;

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(callee, null, true);
    assertEquals(expectedType, scope.getVariableName(paramNode.getName()).getType());
    assertEquals(expectedCondition, scope);
  }

  @Test
  public void testIsDefUndefinedParameter() {
    Node callee = new Node();
    callee.setType(Type.NAME);
    callee.setString("goog.isDef");

    Node paramNode = new Node();
    paramNode.setType(Type.UNDEFINED);

    callee.setLastChild(paramNode);

    JSType expectedType = Type.NUMBER;
    Node expectedCondition = conditionNode;

    when(typeRegistry.createUnionType(Type.NUMBER, Type.STRING))
        .thenReturn(Type.NUMBER);

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(callee, null, true);
    assertEquals(expectedType, scope.getVariableName(paramNode.getName()).getType());
    assertEquals(expectedCondition, scope);
  }

  @Test
  public void testIsDefFunction() {
    Node callee = new Node();
    callee.setType(Type.NAME);
    callee.setString("goog.isDef");
    callee.setLastChild(new Node().setType(Type.FUNCTION));

    JSType expectedType = Type.BOOLEAN;
    Node expectedCondition = conditionNode;

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(callee, null, true);
    assertEquals(expectedType, scope.getVariableName(callee.getName()).getType());
    assertEquals(expectedCondition, scope);
  }

  @Test
  public void testIsDefArray() {
    Node callee = new Node();
    callee.setType(Type.NAME);
    callee.setString("goog.isDef");

    Node arrayNode = new Node().setType(Type.ARRAY);
    arrayNode.setLastChild(new Node().setType(Type.NUMBER));

    callee.setLastChild(arrayNode);

    JSType expectedType = Type.BOOLEAN;
    Node expectedCondition = conditionNode;

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(callee, null, true);
    assertEquals(expectedType, scope.getVariableName(callee.getName()).getType());
    assertEquals(expectedCondition, scope);
  }

  // Add more test cases for other methods like isNull, isDefAndNotNull, isString, isBoolean, isNumber, isFunction, isArray, and isObject.
}