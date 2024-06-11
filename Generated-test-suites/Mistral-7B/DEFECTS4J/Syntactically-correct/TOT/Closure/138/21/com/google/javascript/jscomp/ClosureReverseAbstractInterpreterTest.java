import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.jstype.JSType;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    // Test with a defined value
    JSType type = interpreter.getType("goog.isDef(window)");
    assertTrue(type != null);
    assertEquals(JSType.BOOLEAN, type.getBaseType());

    // Test with an undefined value
    type = interpreter.getType("goog.isDef(undefined)");
    assertNull(type);
  }

  @Test
  public void testIsDefAndNotNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    // Test with a defined and not null value
    JSType type = interpreter.getType("goog.isDefAndNotNull(window)");
    assertTrue(type != null);
    assertEquals(JSType.OBJECT_TYPE, type.getBaseType());

    // Test with an undefined or null value
    type = interpreter.getType("goog.isDefAndNotNull(undefined)");
    assertNull(type);
  }

  @Test
  public void testIsString() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    // Test with a string value
    JSType type = interpreter.getType("goog.isString('hello')");
    assertTrue(type != null);
    assertEquals(JSType.STRING, type.getBaseType());

    // Test with a non-string value
    type = interpreter.getType("goog.isString(123)");
    assertNull(type);
  }

  @Test
  public void testIsBoolean() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    // Test with a boolean value
    JSType type = interpreter.getType("goog.isBoolean(true)");
    assertTrue(type != null);
    assertEquals(JSType.BOOLEAN, type.getBaseType());

    // Test with a non-boolean value
    type = interpreter.getType("goog.isBoolean(123)");
    assertNull(type);
  }

  @Test
  public void testIsNumber() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    // Test with a number value
    JSType type = interpreter.getType("goog.isNumber(123)");
    assertTrue(type != null);
    assertEquals(JSType.NUMBER, type.getBaseType());

    // Test with a non-number value
    type = interpreter.getType("goog.isNumber('hello')");
    assertNull(type);
  }

  @Test
  public void testIsFunction() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    // Test with a function value
    JSType type = interpreter.getType("goog.isFunction(function() {})");
    assertTrue(type != null);
    assertEquals(JSType.FUNCTION_TYPE, type.getBaseType());

    // Test with a non-function value
    type = interpreter.getType("goog.isFunction(123)");
    assertNull(type);
  }

  @Test
  public void testIsArray() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    // Test with an array value
    JSType type = interpreter.getType("goog.isArray([1,2,3])");
    assertTrue(type != null);
    assertEquals(JSType.ARRAY_TYPE, type.getBaseType());

    // Test with a non-array value
    type = interpreter.getType("goog.isArray('hello')");
    assertNull(type);
  }

  @Test
  public void testIsObject() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    // Test with an object value
    JSType type = interpreter.getType("goog.isObject({})");
    assertTrue(type != null);
    assertEquals(JSType.OBJECT_TYPE, type.getBaseType());

    // Test with a non-object value
    type = interpreter.getType("goog.isObject(123)");
    assertNull(type);
  }

  @Test(expected = AssertionError.class)
  public void testInvalidFunctionCall() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    interpreter.getType("goog.isDef(123)"); // Should fail with an AssertionError
  }
}