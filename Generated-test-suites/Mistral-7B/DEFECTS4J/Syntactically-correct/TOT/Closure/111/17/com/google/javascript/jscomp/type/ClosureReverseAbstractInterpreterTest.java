import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    convention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDef"),
        Node.newString("value"));

    JSType expectedType = Node.newUndefined().getType();
    when(typeRegistry.createUnionType(Node.newNumber(0), Node.newVoid()))
        .thenReturn(Node.newNativeType(VOID_TYPE));

    JSType actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);

    // edge case: null value
    expectedType = Node.newNull().getType();
    when(typeRegistry.createUnionType(Node.newNumber(0), Node.newVoid()))
        .thenReturn(Node.newNativeType(NULL_TYPE));

    actualType = interpreter.getType(condition, Node.newObject(), false).getType();

    assertEquals(expectedType, actualType);

    // error case: not a valid function call
    expectedType = null;
    condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDef"), Node.newNumber(0));

    actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNull() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDefAndNotNull"),
        Node.newName("value"));

    JSType expectedType = Node.newVoid().getType();
    when(typeRegistry.createUnionType(Node.newNumber(0), Node.newVoid()))
        .thenReturn(Node.newNativeType(NULL_VOID));

    JSType actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);

    // edge case: null value
    expectedType = Node.newUndefined().getType();
    when(typeRegistry.createUnionType(Node.newNumber(0), Node.newVoid()))
        .thenReturn(Node.newNativeType(NULL_TYPE));

    actualType = interpreter.getType(condition, Node.newObject(), false).getType();

    assertEquals(expectedType, actualType);

    // error case: not a valid function call
    expectedType = null;
    condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDefAndNotNull"),
        Node.newNumber(0));

    actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsArray() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isArray"), Node.newArray());

    JSType expectedType = Node.newNativeType(ARRAY_TYPE);
    when(typeRegistry.createUnionType(Node.newNumber(0), Node.newVoid()))
        .thenReturn(Node.newNativeType(VOID_TYPE));

    JSType actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);

    // edge case: null value
    expectedType = Node.newNull().getType();
    when(typeRegistry.createUnionType(Node.newNumber(0), Node.newVoid()))
        .thenReturn(Node.newNativeType(NULL_TYPE));

    actualType = interpreter.getType(condition, Node.newObject(), false).getType();

    assertEquals(expectedType, actualType);

    // error case: not a valid function call
    expectedType = null;
    condition = Node.newCall(
        Node.newName("goog"), Node.newName("isArray"), Node.newNumber(0));

    actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsObject() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isObject"), Node.newString("value"));

    JSType expectedType = Node.newNativeType(OBJECT_TYPE);
    when(typeRegistry.createUnionType(Node.newNumber(0), Node.newVoid()))
        .thenReturn(Node.newNativeType(VOID_TYPE));

    JSType actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);

    // edge case: null value
    expectedType = Node.newNull().getType();
    when(typeRegistry.createUnionType(Node.newNumber(0), Node.newVoid()))
        .thenReturn(Node.newNativeType(NULL_TYPE));

    actualType = interpreter.getType(condition, Node.newObject(), false).getType();

    assertEquals(expectedType, actualType);

    // error case: not a valid function call
    expectedType = null;
    condition = Node.newCall(
        Node.newName("goog"), Node.newName("isObject"), Node.newNumber(0));

    actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsString() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isString"), Node.newString("value"));

    JSType expectedType = Node.newNativeType("string");

    JSType actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);

    // edge case: null value
    expectedType = Node.newNull().getType();
    actualType = interpreter.getType(condition, Node.newObject(), false).getType();

    assertEquals(expectedType, actualType);

    // error case: not a valid function call
    expectedType = null;
    condition = Node.newCall(
        Node.newName("goog"), Node.newName("isString"), Node.newNumber(0));

    actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsBoolean() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isBoolean"), Node.newBoolean(true));

    JSType expectedType = Node.newNativeType("boolean");

    JSType actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);

    // edge case: null value
    expectedType = Node.newNull().getType();
    actualType = interpreter.getType(condition, Node.newObject(), false).getType();

    assertEquals(expectedType, actualType);

    // error case: not a valid boolean value
    expectedType = null;
    condition = Node.newCall(
        Node.newName("goog"), Node.newName("isBoolean"), Node.newNumber(0));

    actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsFunction() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isFunction"), Node.newString("value"));

    JSType expectedType = Node.newNativeType(FunctionType.VOID);

    JSType actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);

    // edge case: null value
    expectedType = Node.newNull().getType();
    actualType = interpreter.getType(condition, Node.newObject(), false).getType();

    assertEquals(expectedType, actualType);

    // error case: not a valid function name
    expectedType = null;
    condition = Node.newCall(
        Node.newName("goog"), Node.newName("isFunction"), Node.newString("notAFunction"));

    actualType = interpreter.getType(condition, Node.newObject(), true).getType();

    assertEquals(expectedType, actualType);
  }
}