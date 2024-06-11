import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testRestrictIsDef() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDef"), Node.newArray(
            Node.newName("x")));
    JSType expectedType = JSTypeFactory.getArrayType(JSTypeFactory.getUndefinedType());
    when(typeRegistry.createArrayType(expectedType)).thenReturn(expectedType);

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getUndefinedType();
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isDef"), true);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsDefFalse() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDef"), Node.newArray(
            Node.newName("x")));
    JSType expectedType = JSTypeFactory.getVoidType();
    when(typeRegistry.createVoidType()).thenReturn(expectedType);

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getNumberStringBooleanType();
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isDef"), false);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsString() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isString"), Node.newArray(
            Node.newName("x")));
    JSType expectedType = JSTypeFactory.getStringType();

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getUndefinedType();
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isString"), true);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsNull() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isNull"), Node.newArray(
            Node.newName("x")));
    JSType expectedType = JSTypeFactory.getNullType();

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getNumberStringBooleanType();
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isNull"), true);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDef"), Node.newArray(
            Node.newName("x"))).getOrCreateConditional();
    Node notNullCondition = Node.newCall(
        Node.newName("goog"), Node.newName("isNotNull"), Node.newArray(
            Node.newName("x")));
    JSType expectedType = JSTypeFactory.getVoidType();

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getNumberStringBooleanType();
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isDefAndNotNull"), false);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsFunction() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isFunction"), Node.newArray(
            Node.newName("x")));
    FunctionType expectedType = JSTypeFactory.getFunctionType(
        JSTypeFactory.getUndefinedType(), JSTypeFactory.getUndefinedType());

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getUndefinedType();
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isFunction"), true);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsArray() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isArray"), Node.newArray(
            Node.newName("x")));
    JSType expectedType = JSTypeFactory.getArrayType(JSTypeFactory.getUndefinedType());

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getUndefinedType();
    FlowScope blindScope = FlowScope.createRootScope();

    when(typeRegistry.createArrayType(expectedType)).thenReturn(expectedType);

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isArray"), true);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsObject() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isObject"), Node.newArray(
            Node.newName("x")));
    ObjectType expectedType = JSTypeFactory.getObjectType();

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getUndefinedType();
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isObject"), true);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsNotArray() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isArray"), Node.newArray(
            Node.newName("x")));
    JSType expectedType = JSTypeFactory.getAllType();

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getObjectType();
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isArray"), false);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }

  @Test
  public void testRestrictIsNotObject() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isObject"), Node.newArray(
            Node.newName("x")));
    JSType expectedType = JSTypeFactory.getAllType();

    Node parameter = Node.newName("x");
    JSType type = JSTypeFactory.getFunctionType(
        JSTypeFactory.getUndefinedType(), JSTypeFactory.getUndefinedType());
    FlowScope blindScope = FlowScope.createRootScope();

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, interpreter.restricters.get("isObject"), false);
    assertEquals(expectedType, result.getVariable(parameter.getName()).getType());
  }
}