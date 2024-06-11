import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private Node condition;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);

    condition = Node.newCall(
        Node.newName("goog"), Node.newName("isArray"), Node.newName("[]"));
  }

  @Test
  public void testIsDef() {
    JSType type = interpreter.getType(condition);
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    when(typeRegistry.getTypeFromString("boolean")).thenReturn(
        interpreter.getNativeType(BOOLEAN_TYPE));
    when(typeRegistry.getTypeFromString("function")).thenReturn(
        interpreter.getNativeType(FUNCTION_TYPE));
    when(typeRegistry.getTypeFromString("number")).thenReturn(
        interpreter.getNativeType(NUMBER_TYPE));
    when(typeRegistry.getTypeFromString("string")).thenReturn(
        interpreter.getNativeType(STRING_TYPE));

    // positive test
    Node defNode = Node.newCall(Node.newName("goog"), "isDef", Node.newName("x"));
    type = interpreter.getType(defNode);
    assertEquals(interpreter.getNativeType(BOOLEAN_TYPE), type);

    // negative test
    Node notDefNode = Node.newCall(Node.newName("goog"), "isDef", Node.newNull());
    type = interpreter.getType(notDefNode);
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // edge case: null
    Node nullNode = Node.newNull();
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isDef", nullNode));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);
  }

  @Test
  public void testIsArray() {
    JSType type = interpreter.getType(condition);
    assertEquals(interpreter.getNativeType(ARRAY_TYPE), type);

    // positive test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isArray", Node.newName("array")));
    assertEquals(interpreter.getNativeType(ARRAY_TYPE), type);

    // negative test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isArray", Node.newName("object")));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // edge case: null
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isArray", null));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);
  }

  @Test
  public void testIsObject() {
    JSType type = interpreter.getType(condition);
    assertEquals(interpreter.getNativeType(OBJECT_TYPE), type);

    // positive test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isObject", Node.newName("object")));
    assertEquals(interpreter.getNativeType(OBJECT_TYPE), type);

    // negative test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isObject", Node.newNumber(1)));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // edge case: null
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isObject", null));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);
  }

  @Test
  public void testIsFunction() {
    JSType type = interpreter.getType(condition);
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // positive test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isFunction", Node.newName("function")));
    assertEquals(interpreter.getNativeType(FUNCTION_TYPE), type);

    // negative test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isFunction", Node.newName("string")));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // edge case: null
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isFunction", null));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);
  }

  @Test
  public void testIsString() {
    JSType type = interpreter.getType(Node.newCall(Node.newName("goog"), "isString", Node.newString("string")));
    assertEquals(interpreter.getNativeType(STRING_TYPE), type);

    // negative test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isString", Node.newNumber(1)));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // edge case: null
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isString", null));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);
  }

  @Test
  public void testIsBoolean() {
    JSType type = interpreter.getType(condition);
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // positive test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isBoolean", Node.newBoolean(true)));
    assertEquals(interpreter.getNativeType(BOOLEAN_TYPE), type);

    // negative test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isBoolean", Node.newNumber(1)));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // edge case: null
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isBoolean", null));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);
  }

  @Test
  public void testIsNumber() {
    JSType type = interpreter.getType(condition);
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // positive test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isNumber", Node.newNumber(1)));
    assertEquals(interpreter.getNativeType(NUMBER_TYPE), type);

    // negative test
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isNumber", Node.newString("string")));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);

    // edge case: null
    type = interpreter.getType(Node.newCall(Node.newName("goog"), "isNumber", null));
    assertEquals(interpreter.getNativeType(VOID_TYPE), type);
  }

  // Add more test cases for other methods as needed
}