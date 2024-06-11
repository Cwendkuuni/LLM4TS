import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSType.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.test.setup.JSTestCase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest extends JSTestCase {

  private static final CodingConvention CONVENTION = new CodingConvention();
  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private static final ClosureReverseAbstractInterpreter INTERPRETER =
      new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);

  @SuppressWarnings("unchecked")
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Test for isDef method.
   */
  public void testIsDef() {
    Node node = createCallNode("goog.isDef", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertTrue(type != null);
    Type restrictedType = INTERPRETER.getRestrictedType(type, true);
    assertEquals(NUMBER_TYPE, restrictedType);
  }

  /**
   * Test for isDef method with null value.
   */
  public void testIsDefNull() {
    Node node = createCallNode("goog.isDef", ImmutableMap.of("x", null));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isDef method with undefined value.
   */
  public void testIsDefUndefined() {
    Node node = createCallNode("goog.isDef", ImmutableMap.of("x", UNDEFINED));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isNull method.
   */
  public void testIsNull() {
    Node node = createCallNode("goog.isNull", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isNull method with null value.
   */
  public void testIsNullNull() {
    Node node = createCallNode("goog.isNull", ImmutableMap.of("x", null));
    Type type = INTERPRETER.getType(node);
    assertEquals(BOOLEAN_TYPE, type);
    assertTrue(type.getBooleanValue());
  }

  /**
   * Test for isNull method with non-null value.
   */
  public void testIsNullNonnull() {
    Node node = createCallNode("goog.isNull", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isDefAndNotNull method.
   */
  public void testIsDefAndNotNull() {
    Node node = createCallNode("goog.isDefAndNotNull", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertEquals(NUMBER_TYPE, type);
  }

  /**
   * Test for isDefAndNotNull method with null value.
   */
  public void testIsDefAndNotNullNull() {
    Node node = createCallNode("goog.isDefAndNotNull", ImmutableMap.of("x", null));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isDefAndNotNull method with undefined value.
   */
  public void testIsDefAndNotNullUndefined() {
    Node node = createCallNode("goog.isDefAndNotNull", ImmutableMap.of("x", UNDEFINED));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isString method.
   */
  public void testIsString() {
    Node node = createCallNode("goog.isString", ImmutableMap.of("x", "test"));
    Type type = INTERPRETER.getType(node);
    assertEquals(STRING_TYPE, type);
  }

  /**
   * Test for isString method with non-string value.
   */
  public void testIsStringNonString() {
    Node node = createCallNode("goog.isString", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isBoolean method.
   */
  public void testIsBoolean() {
    Node node = createCallNode("goog.isBoolean", ImmutableMap.of("x", true));
    Type type = INTERPRETER.getType(node);
    assertEquals(BOOLEAN_TYPE, type);
  }

  /**
   * Test for isBoolean method with non-boolean value.
   */
  public void testIsBooleanNonBoolean() {
    Node node = createCallNode("goog.isBoolean", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isNumber method.
   */
  public void testIsNumber() {
    Node node = createCallNode("goog.isNumber", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertEquals(NUMBER_TYPE, type);
  }

  /**
   * Test for isNumber method with non-number value.
   */
  public void testIsNumberNonNumber() {
    Node node = createCallNode("goog.isNumber", ImmutableMap.of("x", "test"));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isFunction method.
   */
  public void testIsFunction() {
    Node node = createCallNode("goog.isFunction", ImmutableMap.of("x", createFunctionNode()));
    Type type = INTERPRETER.getType(node);
    assertEquals(FUNCTION_TYPE, type);
  }

  /**
   * Test for isFunction method with non-function value.
   */
  public void testIsFunctionNonFunction() {
    Node node = createCallNode("goog.isFunction", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isArray method.
   */
  public void testIsArray() {
    Node node = createCallNode("goog.isArray", ImmutableMap.of("x", createArrayNode()));
    Type type = INTERPRETER.getType(node);
    assertEquals(ARRAY_TYPE, type);
  }

  /**
   * Test for isArray method with non-array value.
   */
  public void testIsArrayNonArray() {
    Node node = createCallNode("goog.isArray", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  /**
   * Test for isObject method.
   */
  public void testIsObject() {
    Node node = createCallNode("goog.isObject", ImmutableMap.of("x", createObjectNode()));
    Type type = INTERPRETER.getType(node);
    assertEquals(OBJECT_TYPE, type);
  }

  /**
   * Test for isObject method with non-object value.
   */
  public void testIsObjectNonObject() {
    Node node = createCallNode("goog.isObject", ImmutableMap.of("x", 1));
    Type type = INTERPRETER.getType(node);
    assertNull(type);
  }

  private Node createCallNode(String name, Map<String, Node> arguments) {
    Node callee = createCallNode(name);
    Node argsNode = createArgumentsNode(arguments);
    return createCallNode(callee, argsNode);
  }

  private Node createCallNode(String name) {
    return createCallNode(createNameNode(name), ImmutableMap.of());
  }

  private Node createCallNode(Node callee, Node arguments) {
    return new CallNode(callee, arguments);
  }

  private Node createCallNode(Node callee, Map<String, Node> arguments) {
    Node argsNode = createArgumentsNode(arguments);
    return createCallNode(callee, argsNode);
  }

  private Node createArgumentsNode(Map<String, Node> arguments) {
    ArrayNode args = new ArrayNode();
    for (Map.Entry<String, Node> entry : arguments.entrySet()) {
      args.addChild(entry.getValue());
      args.addChild(createStringNode(entry.getKey()));
    }
    return args;
  }

  private Node createStringNode(String value) {
    return new StringNode(value);
  }

  private Node createFunctionNode() {
    FunctionType functionType = new FunctionType(new ArrayType(new VoidType()),
        new ArrayType(new ObjectType()));
    Node functionNode = new FunctionNode(functionType);
    return functionNode;
  }

  private Node createObjectNode() {
    ObjectType objectType = new ObjectType();
    Node objectNode = new ObjectLiteralNode(objectType);
    return objectNode;
  }

  private Node createArrayNode() {
    ArrayType arrayType = new ArrayType(new IntType());
    Node arrayNode = new ArrayLiteralNode(arrayType);
    return arrayNode;
  }

  private Node createNameNode(String name) {
    return new NameNode(name);
  }

  private Node createIntNode(int value) {
    return new IntLiteralNode(value);
  }
}