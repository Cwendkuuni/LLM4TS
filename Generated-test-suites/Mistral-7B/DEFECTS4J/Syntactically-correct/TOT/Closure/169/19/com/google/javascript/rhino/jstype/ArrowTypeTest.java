import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.node.Node;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
  }

  /**
   * Test for a simple arrow type with no inferred return type.
   */
  @org.junit.Test
  public void testSimpleArrowType() {
    Node parametersNode = createNodeFromString("(number, number)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, false);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        AnyFunctionType, false);

    assertEquals(expectedArrowType.toString(), arrowType.toString());
  }

  /**
   * Test for an arrow type with an inferred return type.
   */
  @org.junit.Test
  public void testArrowTypeWithInferredReturnType() {
    Node parametersNode = createNodeFromString("(number, number)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        UnknownType, true);

    assertEquals(expectedArrowType.toString(), arrowType.toString());
  }

  /**
   * Test for an arrow type with a single required parameter.
   */
  @org.junit.Test
  public void testArrowTypeWithSingleRequiredParameter() {
    Node parametersNode = createNodeFromString("(number)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, NumberType, false);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        new FunctionType(registry, new JSType[]{NumberType}, NumberType), false);

    assertEquals(expectedArrowType.toString(), arrowType.toString());
  }

  /**
   * Test for an arrow type with a single optional parameter.
   */
  @org.junit.Test
  public void testArrowTypeWithSingleOptionalParameter() {
    Node parametersNode = createNodeFromString("(number?)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, NumberType, false);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        new UnionType(registry, NumberType, VoidType), false);

    assertEquals(expectedArrowType.toString(), arrowType.toString());
  }

  /**
   * Test for an arrow type with multiple required parameters.
   */
  @org.junit.Test
  public void testArrowTypeWithMultipleRequiredParameters() {
    Node parametersNode = createNodeFromString("(number, number, boolean)");
    ArrowType arrowType = new ArrowType(registry, parametersNode, BooleanType, false);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        new FunctionType(registry, new JSType[]{
            NumberType, NumberType, BooleanType
        }, BooleanType), false);

    assertEquals(expectedArrowType.toString(), arrowType.toString());
  }

  /**
   * Test for an arrow type with variable arguments.
   */
  @org.junit.Test
  public void testArrowTypeWithVariableArguments() {
    Node parametersNode = createNodeFromString("(number, ...number[])");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, false);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        new FunctionType(registry, new JSType[]{NumberType},
            new ArrayType(registry, NumberType, 0)), false);

    assertEquals(expectedArrowType.toString(), arrowType.toString());
  }

  /**
   * Test for an arrow type with an inferred return type and variable arguments.
   */
  @org.junit.Test
  public void testArrowTypeWithInferredReturnTypeAndVariableArguments() {
    Node parametersNode = createNodeFromString("(number, ...number[])");
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    ArrowType expectedArrowType = new ArrowType(registry, parametersNode,
        UnknownType, true);

    assertEquals(expectedArrowType.toString(), arrowType.toString());
  }

  /**
   * Test for an arrow type with an invalid parameter list.
   */
  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithInvalidParameterList() {
    Node parametersNode = null;
    new ArrowType(registry, parametersNode, NumberType, false);
  }

  /**
   * Test for an arrow type with null return type.
   */
  @org.junit.Test(expected = NullPointerException.class)
  public void testArrowTypeWithNullReturnType() {
    Node parametersNode = createNodeFromString("(number, number)");
    new ArrowType(registry, parametersNode, null, false);
  }

  /**
   * Helper method to create a Node object from a string representation of
   * a parameter list.
   */
  private Node createNodeFromString(String parameterListString) {
    Node parametersNode = new Node_Literal();
    parametersNode.setStringValue(parameterListString);
    return parametersNode;
  }
}