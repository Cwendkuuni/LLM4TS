import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import junit.framework.TestCase;

public class ArrowTypeTest extends TestCase {

  private static final ErrorReporter t = new ErrorReporter();
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @SuppressWarnings("unchecked")
  private static <T> ArrowType createArrowType(Node parameters, JSType returnType) {
    return new ArrowType(registry, parameters, returnType, false);
  }

  private static ArrowType createArrowTypeWithInferredReturn(Node parameters) {
    return new ArrowType(registry, parameters, null, true);
  }

  private static void testArrowType(Node arrowTypeNode, JSType expectedType) {
    Node parametersNode = arrowTypeNode.getFirstChild();
    Node returnTypeNode = arrowTypeNode.getLastChild();

    JSType actualType = new ArrowType(registry, parametersNode,
        returnTypeNode == null ? null : (JSType) returnTypeNode.getValue(),
        false).resolve(t, new StaticScope<>(registry));

    assertEquals("Unexpected type for arrow type", expectedType, actualType);
  }

  public void testArrowTypeWithNoParametersAndVoidReturn() {
    Node arrowTypeNode = registry.parseTypeNode("( ) => void");
    testArrowType(arrowTypeNode, ArrowType.VOID);
  }

  public void testArrowTypeWithNoParametersAndNumberReturn() {
    Node arrowTypeNode = registry.parseTypeNode("( ) => number");
    testArrowType(arrowTypeNode, JSType.NUMBER);
  }

  public void testArrowTypeWithOneParameterAndStringReturn() {
    Node arrowTypeNode = registry.parseTypeNode("( string ) => string");
    testArrowType(arrowTypeNode, new ArrowType(registry, null, JSType.STRING, false));
  }

  public void testArrowTypeWithOneParameterAndBooleanReturn() {
    Node arrowTypeNode = registry.parseTypeNode("( boolean ) => boolean");
    testArrowType(arrowTypeNode, new ArrowType(registry, null, JSType.BOOLEAN, false));
  }

  public void testArrowTypeWithOneParameterAndUnknownReturn() {
    Node arrowTypeNode = registry.parseTypeNode("( boolean ) => UNKNOWN_TYPE");
    ArrowType expectedType = new ArrowType(registry, null, null, false);
    testArrowType(arrowTypeNode, expectedType);
  }

  public void testArrowTypeWithMultipleParametersAndVoidReturn() {
    Node arrowTypeNode = registry.parseTypeNode(
        "( string, number ) => void");
    ArrowType expectedType = new ArrowType(registry,
        registry.createParametersWithVarArgs(
            new JSType[] { JSType.STRING, JSType.NUMBER }),
        ArrowType.VOID, false);
    testArrowType(arrowTypeNode, expectedType);
  }

  public void testArrowTypeWithMultipleParametersAndNumberReturn() {
    Node arrowTypeNode = registry.parseTypeNode(
        "( string, number ) => number");
    ArrowType expectedType = new ArrowType(registry,
        registry.createParametersWithVarArgs(
            new JSType[] { JSType.STRING, JSType.NUMBER }),
        JSType.NUMBER, false);
    testArrowType(arrowTypeNode, expectedType);
  }

  public void testArrowTypeWithMultipleParametersAndUnknownReturn() {
    Node arrowTypeNode = registry.parseTypeNode(
        "( string, number ) => UNKNOWN_TYPE");
    ArrowType expectedType = new ArrowType(registry,
        registry.createParametersWithVarArgs(
            new JSType[] { JSType.STRING, JSType.NUMBER }),
        null, false);
    testArrowType(arrowTypeNode, expectedType);
  }

  public void testArrowTypeWithInferredReturnAndOneParameter() {
    Node arrowTypeNode = registry.parseTypeNode("( boolean ) => ?");
    ArrowType expectedType = createArrowTypeWithInferredReturn(null);
    testArrowType(arrowTypeNode, expectedType);
  }

  public void testArrowTypeWithInferredReturnAndMultipleParameters() {
    Node arrowTypeNode = registry.parseTypeNode(
        "( string, number ) => ?");
    ArrowType expectedType = createArrowTypeWithInferredReturn(
        registry.createParametersWithVarArgs(
            new JSType[] { JSType.STRING, JSType.NUMBER }));
    testArrowType(arrowTypeNode, expectedType);
  }

  public void testArrowTypeWithInvalidSyntax() {
    Node arrowTypeNode = registry.parseTypeNode("( ) => number)");
    assertEquals("Unexpected type for arrow type", null, arrowTypeNode.getValue());
  }

  public void testArrowTypeWithMissingReturnType() {
    Node arrowTypeNode = registry.parseTypeNode("( ) =>");
    assertEquals("Unexpected type for arrow type", null, arrowTypeNode.getValue());
  }

  public void testArrowTypeWithMissingParameterType() {
    Node arrowTypeNode = registry.parseTypeNode("( ) => string");
    assertEquals("Unexpected type for arrow type", null, arrowTypeNode.getValue());
  }

  public void testArrowTypeWithEmptyParameterList() {
    Node arrowTypeNode = registry.parseTypeNode("() => number");
    ArrowType expectedType = new ArrowType(registry, null, JSType.NUMBER, false);
    testArrowType(arrowTypeNode, expectedType);
  }

  public void testArrowTypeWithMissingReturnTypeAndEmptyParameterList() {
    Node arrowTypeNode = registry.parseTypeNode("() =>");
    assertEquals("Unexpected type for arrow type", null, arrowTypeNode.getValue());
  }

  public void testArrowTypeWithVariableArity() {
    Node arrowTypeNode = registry.parseTypeNode("( ...args: any[] ) => number");
    ArrowType expectedType = new ArrowType(registry,
        registry.createParametersWithVarArgs(JSType.ANY),
        JSType.NUMBER, false);
    testArrowType(arrowTypeNode, expectedType);
  }

  public void testArrowTypeWithVariableArityAndInferredReturn() {
    Node arrowTypeNode = registry.parseTypeNode("( ...args: any[] ) => ?");
    ArrowType expectedType = createArrowTypeWithInferredReturn(
        registry.createParametersWithVarArgs(JSType.ANY));
    testArrowType(arrowTypeNode, expectedType);
  }
}