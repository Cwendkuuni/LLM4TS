import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;

  @BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  /**
   * Test for ArrowType with known parameter and return types.
   */
  @Test
  public void testArrowTypeWithKnownTypes() {
    Node functionNode = makeFunctionNode("function(int x, string y) { return x + y; }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), INT, STRING);

    JSType expectedType = new FunctionType(registry, INT, new ArrowType(registry, null, STRING));
    assertEquals(expectedType, arrowType);

    assertTrue(arrowType.isSubtype(arrowType));
    assertTrue(arrowType.isSubtype(new ArrowType(registry, null, INT, STRING)));
    assertTrue(arrowType.isSubtype(new ArrowType(registry, null, INT, STRING, false, null, INT.getTag())));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, STRING, INT)));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, INT, VOID)));
  }

  /**
   * Test for ArrowType with optional and varargs parameter and unknown return type.
   */
  @Test
  public void testArrowTypeWithOptionalAndVarargsParamsAndUnknownReturnType() {
    Node functionNode = makeFunctionNode("function(int x = 0, int... ys) { return; }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), UNKNOWN_TYPE, false);

    JSType expectedType = new FunctionType(
        registry,
        new JSType[] {
          INT,
          JSTypeRegistry.createParametersWithVarArgs(registry.createOptionalParamType(INT))
        },
        UNKNOWN_TYPE
    );

    assertEquals(expectedType, arrowType);

    assertTrue(arrowType.isSubtype(arrowType));
    assertTrue(arrowType.isSubtype(new ArrowType(registry, null, INT, UNKNOWN_TYPE, false, null, INT.getTag())));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, INT, STRING)));
  }

  /**
   * Test for ArrowType with unknown parameter types.
   */
  @Test
  public void testArrowTypeWithUnknownParameterTypes() {
    Node functionNode = makeFunctionNode("function(a, b) { return a + b; }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), UNKNOWN_TYPE, false);

    assertTrue(arrowType.isSubtype(arrowType));

    // ArrowType with known parameter types is a subtype of ArrowType with unknown types
    Node knownFunctionNode = makeFunctionNode("function(number a, number b) { return a + b; }");
    ArrowType knownArrowType = new ArrowType(registry, knownFunctionNode.getParameters(), NUMBER, false);
    assertTrue(arrowType.isSubtype(knownArrowType));

    // ArrowType with optional parameters is a supertype of ArrowType with unknown types
    Node optionalFunctionNode = makeFunctionNode("function(number a = 0, number b) { return a + b; }");
    ArrowType optionalArrowType = new ArrowType(registry, optionalFunctionNode.getParameters(), NUMBER, false);
    assertTrue(knownArrowType.isSubtype(optionalArrowType));
    assertTrue(arrowType.isSubtype(optionalArrowType));

    // ArrowType with varargs is a supertype of ArrowType with unknown types
    Node varargsFunctionNode = makeFunctionNode("function(number a, number... b) { return a + Array.prototype.slice(b).join(''); }");
    ArrowType varargsArrowType = new ArrowType(registry, varargsFunctionNode.getParameters(), NUMBER, false);
    assertTrue(knownArrowType.isSubtype(varargsArrowType));
    assertTrue(arrowType.isSubtype(varargsArrowType));
  }

  /**
   * Test for ArrowType with unknown return type and known parameter types.
   */
  @Test
  public void testArrowTypeWithUnknownReturnTypeAndKnownParameterTypes() {
    Node functionNode = makeFunctionNode("function(int x, int y) { return; }");
    ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), UNKNOWN_TYPE, true);

    assertTrue(arrowType.isSubtype(arrowType));

    // ArrowType with known return type is a subtype of ArrowType with unknown return type
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, INT, INT, false, null, INT.getTag())));

    // ArrowType with optional parameters is a supertype of ArrowType with unknown return type
    Node optionalFunctionNode = makeFunctionNode("function(int x = 0, int y) { return; }");
    ArrowType optionalArrowType = new ArrowType(registry, optionalFunctionNode.getParameters(), UNKNOWN_TYPE, true);
    assertTrue(arrowType.isSubtype(optionalArrowType));

    // ArrowType with varargs is a supertype of ArrowType with unknown return type
    Node varargsFunctionNode = makeFunctionNode("function(int x, int... ys) { return; }");
    ArrowType varargsArrowType = new ArrowType(registry, varargsFunctionNode.getParameters(), UNKNOWN_TYPE, true);
    assertTrue(arrowType.isSubtype(varargsArrowType));
  }

  /**
   * Test for ArrowType with cyclic references.
   */
  @Test(expected = AssertionError.class)
  public void testArrowTypeWithCyclicReference() {
    Node functionNode = makeFunctionNode("function(int a) { return function(int b) { return a; } }");
    new ArrowType(registry, functionNode.getParameters(), UNKNOWN_TYPE, false);
  }

  private static Node makeFunctionNode(String code) {
    Node node = registry.parseFunctionDeclaration(code, null, null, null, null, null, null);
    node.setSourceName("TestArrowType");
    return node;
  }
}