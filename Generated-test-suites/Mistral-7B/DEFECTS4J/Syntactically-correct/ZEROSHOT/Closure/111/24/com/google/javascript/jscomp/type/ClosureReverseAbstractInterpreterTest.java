import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictParameterIsDef() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, true);
    JSType expectedType = interpreters.get(0).restricters.get("isDef")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isDef"), true)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsDefAndNotNull() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, true);
    JSType expectedType = interpreters.get(0).restricters.get("isDefAndNotNull")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isDefAndNotNull"), true)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsDefButNull() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(null, true);
    JSType expectedType = interpreters.get(0).restricters.get("isDef")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isDef"), false)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsNotNull() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, false);
    JSType expectedType = interpreters.get(0).restricters.get("isNull")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isNull"), false)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsObject() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, true);
    JSType expectedType = interpreters.get(0).restricters.get("isObject")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isObject"), true)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsNotObject() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, false);
    JSType expectedType = interpreters.get(0).restricters.get("isNotObject")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isNotObject"), false)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsArray() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, true);
    JSType expectedType = interpreters.get(0).restricters.get("isArray")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isArray"), true)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsNotArray() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, false);
    JSType expectedType = interpreters.get(0).restricters.get("isNotArray")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isNotArray"), false)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsString() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, true);
    JSType expectedType = interpreters.get(0).restricters.get("isString")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isString"), true)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsNotString() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.StringType.get();
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, false);
    JSType expectedType = interpreters.get(0).restricters.get("isNotString")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isNotString"), false)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsFunction() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.FunctionType.get(ObjectType.get(JSTypeNative.ObjectType),
        VoidType.getInstance());
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, true);
    JSType expectedType = interpreters.get(0).restricters.get("isFunction")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isFunction"), true)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsNotFunction() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = JSTypeNative.FunctionType.get(ObjectType.get(JSTypeNative.ObjectType),
        VoidType.getInstance());
    Node parameter = Node.newName("param");

    TypeRestriction expectedTypeRestriction = new TypeRestriction(type, false);
    JSType expectedType = interpreters.get(0).restricters.get("isNotFunction")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(parameter, type,
        createBlindScope(), interpreters.get(0).restricters.get("isNotFunction"), false)
        .getTypeInThisScope());
  }

  @Test
  public void testRestrictParameterIsNull() {
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    TypeRestriction expectedTypeRestriction = new TypeRestriction(null, false);
    JSType expectedType = interpreters.get(0).restricters.get("isAnyType")
        .apply(expectedTypeRestriction);

    assertEquals(expectedType, interpreter.restrictParameter(null, JSTypeNative.AnyType,
        createBlindScope(), interpreters.get(0).restricters.get("isAnyType"), false)
        .getTypeInThisScope());
  }

  private FlowScope createBlindScope() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    return new FlowScope(typeRegistry, null, null, null, null, null);
  }

  private static final Interpreter[] interpreters = {
    new Interpreter() {},
    new ChainableReverseAbstractInterpreter(mock(CodingConvention.class), mock(JSTypeRegistry.class)) {},
    new ClosureReverseAbstractInterpreter(mock(CodingConvention.class), mock(JSTypeRegistry.class)) {}
  };

  private static class Interpreter {
  }
}