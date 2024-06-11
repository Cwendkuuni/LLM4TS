import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.FlowScope;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp. RhinoFunctionType;
import com.google.javascript.jscomp.RhinoObjectType;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClosureReverseAbstractInterpreterTest {

  @Mock
  private CodingConvention codingConvention;

  @Mock
  private JSTypeRegistry typeRegistry;

  @InjectMocks
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSTypeRegistry typeRegistryMock = mock(JSTypeRegistry.class);
    JSType arrayType = getNativeType(JSTypeNative.ARRAY_TYPE);
    when(typeRegistryMock.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(arrayType);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistryMock);

    ObjectType objectType = mock(RhinoObjectType.class);
    RhinoFunctionType functionType = mock(RhinoFunctionType.class);

    JSType result = interpreter.restrictToArrayVisitor.visit(objectType);
    assertNull(result);

    result = interpreter.restrictToArrayVisitor.visit(functionType);
    assertNull(result);

    result = interpreter.restrictToArrayVisitor.visit(arrayType);
    assertSame(arrayType, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSTypeRegistry typeRegistryMock = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistryMock);

    ObjectType objectType = mock(RhinoObjectType.class);
    RhinoFunctionType functionType = mock(RhinoFunctionType.class);

    JSType result = interpreter.restrictToNotArrayVisitor.visit(objectType);
    assertNull(result);

    result = interpreter.restrictToNotArrayVisitor.visit(functionType);
    assertNull(result);

    result = interpreter.restrictToNotArrayVisitor.visit(arrayType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    JSTypeRegistry typeRegistryMock = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistryMock);

    ObjectType objectType = mock(RhinoObjectType.class);
    RhinoFunctionType functionType = mock(RhinoFunctionType.class);

    JSType result = interpreter.restrictToObjectVisitor.visit(objectType);
    assertSame(objectType, result);

    result = interpreter.restrictToObjectVisitor.visit(functionType);
    assertSame(functionType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    JSTypeRegistry typeRegistryMock = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistryMock);

    ObjectType objectType = mock(RhinoObjectType.class);
    RhinoFunctionType functionType = mock(RhinoFunctionType.class);

    JSType result = interpreter.restrictToNotObjectVisitor.visit(objectType);
    assertNull(result);

    result = interpreter.restrictToNotObjectVisitor.visit(functionType);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefTrue() {
    Node condition = NodeOperations.createCallNode(
        "goog.isDef",
        NodeOperations.createQualifiedNameNode("myObject", "myProperty")
    );

    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
    JSType expectedType = getNativeType(JSTypeNative.OBJECT_TYPE);

    FlowScope blindScope = mock(FlowScope.class);
    FlowScope expectedFlowScope = mock(FlowScope.class);

    when(typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE)).thenReturn(type);
    when(codingConvention.getFlowScopeForVariable("myProperty", type)).thenReturn(expectedFlowScope);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertSame(expectedFlowScope, result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefFalse() {
    Node condition = NodeOperations.createCallNode(
        "goog.isDef",
        NodeOperations.createQualifiedNameNode("myObject", "myProperty")
    );

    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNullTrue() {
    Node condition = NodeOperations.createCallNode(
        "goog.isDefAndNotNull",
        NodeOperations.createQualifiedNameNode("myObject", "myProperty")
    );

    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);

    FlowScope blindScope = mock(FlowScope.class);

    when(codingConvention.getFlowScopeForVariable("myProperty", type)).thenReturn(mock(FlowScope.class));

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNullFalse() {
    Node condition = NodeOperations.createCallNode(
        "goog.isDefAndNotNull",
        NodeOperations.createQualifiedNameNode("myObject", "myProperty")
    );

    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isStringTrue() {
    Node condition = NodeOperations.createCallNode(
        "goog.isString",
        NodeOperations.createStringNode("myString")
    );

    JSType type = getNativeType(JSTypeNative.STRING);

    FlowScope blindScope = mock(FlowScope.class);

    when(codingConvention.getFlowScopeForVariable("myString", type)).thenReturn(mock(FlowScope.class));

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertSame(mock(FlowScope.class), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isStringFalse() {
    Node condition = NodeOperations.createCallNode(
        "goog.isString",
        NodeOperations.createStringNode("myString")
    );

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBooleanTrue() {
    Node condition = NodeOperations.createCallNode(
        "goog.isBoolean",
        NodeOperations.createBooleanNode(true)
    );

    JSType type = getNativeType(JSTypeNative.BOOLEAN);

    FlowScope blindScope = mock(FlowScope.class);

    when(codingConvention.getFlowScopeForVariable(Token.NAME, type)).thenReturn(mock(FlowScope.class));

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertSame(mock(FlowScope.class), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBooleanFalse() {
    Node condition = NodeOperations.createCallNode(
        "goog.isBoolean",
        NodeOperations.createBooleanNode(true)
    );

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumberTrue() {
    Node condition = NodeOperations.createCallNode(
        "goog.isNumber",
        NodeOperations.createNumberNode(1)
    );

    JSType type = getNativeType(JSTypeNative.NUMBER);

    FlowScope blindScope = mock(FlowScope.class);

    when(codingConvention.getFlowScopeForVariable(Token.NUMBER, type)).thenReturn(mock(FlowScope.class));

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertSame(mock(FlowScope.class), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumberFalse() {
    Node condition = NodeOperations.createCallNode(
        "goog.isNumber",
        NodeOperations.createNumberNode(1)
    );

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunctionTrue() {
    Node condition = NodeOperations.createCallNode(
        "goog.isFunction",
        NodeOperations.createCallNode(Token.NAME, NodeOperations.createStringNode("myFunc"))
    );

    JSType type = getNativeType(JSTypeNative.FUNCTION_TYPE);

    FlowScope blindScope = mock(FlowScope.class);

    RhinoFunctionType functionType = mock(RhinoFunctionType.class);

    when(typeRegistry.getFunctionType(NodeOperations.createStringNode("myFunc"))).thenReturn(functionType);
    when(codingConvention.getFlowScopeForFunction(functionType)).thenReturn(mock(FlowScope.class));

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertSame(mock(FlowScope.class), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunctionFalse() {
    Node condition = NodeOperations.createCallNode(
        "goog.isFunction",
        NodeOperations.createCallNode(Token.NAME, NodeOperations.createStringNode("myFunc"))
    );

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArrayTrue() {
    Node condition = NodeOperations.createCallNode(
        "goog.isArray",
        NodeOperations.createBooleanNode(true)
    );

    JSType type = getNativeType(JSTypeNative.ARRAY_TYPE);

    FlowScope blindScope = mock(FlowScope.class);

    when(codingConvention.getFlowScopeForVariable(Token.BOOLEAN, type)).thenReturn(mock(FlowScope.class));

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertSame(mock(FlowScope.class), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArrayFalse() {
    Node condition = NodeOperations.createCallNode(
        "goog.isArray",
        NodeOperations.createBooleanNode(true)
    );

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObjectTrue() {
    Node condition = NodeOperations.createCallNode(
        "goog.isObject",
        NodeOperations.createBooleanNode(true)
    );

    JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);

    FlowScope blindScope = mock(FlowScope.class);

    when(codingConvention.getFlowScopeForVariable(Token.BOOLEAN, type)).thenReturn(mock(FlowScope.class));

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertSame(mock(FlowScope.class), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObjectFalse() {
    Node condition = NodeOperations.createCallNode(
        "goog.isObject",
        NodeOperations.createBooleanNode(true)
    );

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  private NodeOperations nodeOperations = new NodeOperations();

  private JSType getNativeType(int type) {
    JSTypeNative jstypeNative = JSTypeNative.instance();
    return jstypeNative.getNativeType(type);
  }

  private JSType getNativeType(JSTypeNative jstypeNative) {
    return jstypeNative.getNativeType(null);
  }
}