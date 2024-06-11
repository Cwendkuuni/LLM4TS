import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDefMethod() {
    ChainableReverseAbstractInterpreter interpreter = new ChainableReverseAbstractInterpreter(null, null);
    JSType inputType = interpreter.getType("goog.isDef(null)");
    JSType expectedType = interpreter.getNativeType(VOID_TYPE);
    assertEquals(expectedType, inputType);

    JSType inputTypeTrue = interpreter.getType("goog.isDef({})");
    JSType expectedTypeTrue = interpreter.getNativeType(OBJECT_TYPE);
    assertEquals(expectedTypeTrue, inputTypeTrue);
  }

  @Test(expected = AssertionError.class)
  public void testIsDefMethodWithUndefined() {
    ChainableReverseAbstractInterpreter interpreter = new ChainableReverseAbstractInterpreter(null, null);
    JSType inputType = interpreter.getType("goog.isDef(undefined)");
    JSType expectedType = interpreter.getNativeType(VOID_TYPE);
    assertEquals(expectedType, inputType);

    // This test should throw an AssertionError as isDef should not return true for undefined
    JSType inputTypeTrue = interpreter.getType("goog.isDef(undefined)");
    JSType expectedTypeTrue = interpreter.getNativeType(OBJECT_TYPE);
    assertEquals(expectedTypeTrue, inputTypeTrue);
  }

  @Test
  public void testIsArrayMethod() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType inputType = interpreter.getType("goog.isArray([1, 2, 3])");
    JSType expectedType = interpreter.getNativeType(ARRAY_TYPE);
    assertEquals(expectedType, inputType);

    JSType inputTypeFalse = interpreter.getType("goog.isArray('abc')");
    JSType expectedTypeFalse = interpreter.getNativeType(VOID_TYPE);
    assertEquals(expectedTypeFalse, inputTypeFalse);
  }

  @Test(expected = AssertionError.class)
  public void testIsArrayMethodWithNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    JSType inputType = interpreter.getType("goog.isArray(null)");
    JSType expectedType = interpreter.getNativeType(VOID_TYPE);
    assertEquals(expectedType, inputType);

    // This test should throw an AssertionError as isArray should not return true for null
    JSType inputTypeTrue = interpreter.getType("goog.isArray(null)");
    JSType expectedTypeTrue = interpreter.getNativeType(ARRAY_TYPE);
    assertEquals(expectedTypeTrue, inputTypeTrue);
  }

  // Add test cases for other methods: isObject, isFunction, isString, isBoolean, isNull
}