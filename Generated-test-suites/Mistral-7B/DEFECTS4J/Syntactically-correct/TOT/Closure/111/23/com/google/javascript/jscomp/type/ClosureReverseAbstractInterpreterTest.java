import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
  }

  @Test
  public void testIsDefTrueForDefinedValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));

    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isDef"), true);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsDefFalseForUndefinedValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = VoidType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(null, false);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isDef"), false);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsDefTrueForNullValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = NullType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(null, true);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isDefAndNotNull"), true);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsDefFalseForNullValueAndIsDefAndNotNull() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = VoidType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(null, false);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isDefAndNotNull"), false);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsStringTrueForStringValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    String stringValue = "test";
    JSType expectedOutputType = StringType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isString"), true);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsStringFalseForNonStringValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = VoidType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(new IntType(1), false);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isString"), false);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsBooleanTrueForBooleanValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    boolean booleanValue = true;
    JSType expectedOutputType = BooleanType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isBoolean"), true);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsBooleanFalseForNonBooleanValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = VoidType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(new IntType(1), false);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isBoolean"), false);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsNumberTrueForNumberValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    double numberValue = 1.0;
    JSType expectedOutputType = NumberType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isNumber"), true);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsNumberFalseForNonNumberValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = VoidType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(new StringType("test"), false);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isNumber"), false);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsFunctionTrueForFunctionValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    FunctionType functionType = new FunctionType(new Node[0], VoidType.INSTANCE);
    JSType expectedOutputType = functionType;

    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isFunction"), true);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsFunctionFalseForNonFunctionValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = VoidType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(new StringType("test"), false);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isFunction"), false);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsArrayTrueForArrayValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType arrayType = new ArrayType(NumberType.INSTANCE);
    JSType expectedOutputType = arrayType;

    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isArray"), true);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsArrayFalseForNonArrayValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = VoidType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(new StringType("test"), false);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isArray"), false);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsObjectTrueForObjectValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    ObjectType objectType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = objectType;

    TypeRestriction restriction = new TypeRestriction(inputType, true);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isObject"), true);

    assertEquals(expectedOutputType, actualOutputType);
  }

  @Test
  public void testIsObjectFalseForNonObjectValue() {
    JSType inputType = new ObjectType(new FunctionType(new Node[0], VoidType.INSTANCE));
    JSType expectedOutputType = VoidType.INSTANCE;

    TypeRestriction restriction = new TypeRestriction(new IntType(1), false);
    JSType actualOutputType = interpreter.restrictParameter(
        mock(Node.class), inputType, null, interpreter.restricters.get("isObject"), false);

    assertEquals(expectedOutputType, actualOutputType);
  }
}