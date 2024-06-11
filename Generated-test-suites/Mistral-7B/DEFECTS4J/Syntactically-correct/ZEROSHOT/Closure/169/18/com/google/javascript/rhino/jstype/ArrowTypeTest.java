import static org.junit.Assert.*;
import static com.google.javascript.rhino.node.NodeFactory.createArrowTypeNode;
import static com.google.javascript.rhino.node.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.testing.Assertions.assertEquals;
import static com.google.javascript.rhino.testing.Assertions.assertFalse;
import static com.google.javascript.rhino.testing.Assertions.assertNotNull;
import static com.google.javascript.rhino.testing.Assertions.assertNull;
import static com.google.javascript.rhino.testing.Assertions.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.CodeToRun;
import com.google.javascript.rhino.testing.PolymerTestRunner;

public class ArrowTypeTest {

  @BeforeClass
  public static void setUp() {
    PolymerTestRunner.init();
  }

  private JSTypeRegistry createRegistry() {
    return createJSTypeRegistry(new ErrorReporter());
  }

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNode = createArrowTypeNode(Node.newIntLiteral(1), createJSType(registry, Node.STRING_TYPE));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode.parameters, arrowTypeNode.returnType);

    assertEquals(arrowTypeNode.parameters, arrowType.parameters);
    assertEquals(arrowTypeNode.returnType, arrowType.returnType);
  }

  @Test
  public void testArrowTypeCreationWithNullParameters() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertEquals(registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE)), arrowType.parameters);
  }

  @Test
  public void testArrowTypeCreationWithNullReturnType() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newIntLiteral(1), null);
    assertEquals(getNativeType(UNKNOWN_TYPE), arrowType.returnType);
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    JSTypeRegistry registry = createRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newIntLiteral(1), null, true);
    assertEquals(getNativeType(UNKNOWN_TYPE), arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), createJSType(registry, Node.STRING_TYPE));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), createJSType(registry, Node.NUMBER_TYPE));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertTrue(arrowTypeA.isSubtype(arrowTypeB));
  }

  @Test
  public void testArrowTypeIsNotSubtype() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), createJSType(registry, Node.STRING_TYPE));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(2), createJSType(registry, Node.NUMBER_TYPE));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));
  }

  @Test
  public void testArrowTypeIsSubtypeWithDifferentReturnType() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), createJSType(registry, Node.STRING_TYPE));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), createJSType(registry, Node.NUMBER_TYPE));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertTrue(arrowTypeA.hasEqualParameters(arrowTypeB, true));
  }

  @Test
  public void testArrowTypeDoesNotHaveEqualParameters() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("Number"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertFalse(arrowTypeA.hasEqualParameters(arrowTypeB, true));
  }

  @Test
  public void testArrowTypeHasEqualParametersWithToleratedUnknowns() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType(UNKNOWN_TYPE));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertTrue(arrowTypeA.hasEqualParameters(arrowTypeB, true));
  }

  @Test
  public void testArrowTypeDoesNotHaveEqualParametersWithToleratedUnknowns() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("Number"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertFalse(arrowTypeA.hasEqualParameters(arrowTypeB, false));
  }

  @Test
  public void testArrowTypeIsEqual() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertEquals(arrowTypeA, arrowTypeB);
  }

  @Test
  public void testArrowTypeIsNotEqual() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("Number"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertNotSame(arrowTypeA, arrowTypeB);
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(2), Node.createNamedType("String"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    int hashCodeA = arrowTypeA.hashCode();
    int hashCodeB = arrowTypeB.hashCode();

    assertNotSame(hashCodeA, hashCodeB);
  }

  @Test
  public void testArrowTypeWithUnknownReturnTypeHasSubtypeWithKnownReturnType() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), null);
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertTrue(arrowTypeA.isSubtype(arrowTypeB));
  }

  @Test
  public void testArrowTypeWithUnknownParametersHasSubtypeWithKnownParameters() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), null);
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertTrue(arrowTypeA.isSubtype(arrowTypeB));
  }

  @Test
  public void testArrowTypeDoesNotHaveSubtypeWithUnknownParameters() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), null);
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));
  }

  @Test
  public void testArrowTypeDoesNotHaveSubtypeWithUnknownReturnType() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    Node arrowTypeNodeB = createArrowTypeNode(Node.newIntLiteral(1), null);
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);
    ArrowType arrowTypeB = new ArrowType(registry, arrowTypeNodeB.parameters, arrowTypeNodeB.returnType);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));
  }

  @Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), null);
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);

    assertTrue(arrowTypeA.hasUnknownParamsOrReturn());
  }

  @Test
  public void testArrowTypeDoesNotHaveUnknownParamsOrReturn() {
    JSTypeRegistry registry = createRegistry();
    Node arrowTypeNodeA = createArrowTypeNode(Node.newIntLiteral(1), Node.createNamedType("String"));
    ArrowType arrowTypeA = new ArrowType(registry, arrowTypeNodeA.parameters, arrowTypeNodeA.returnType);

    assertFalse(arrowTypeA.hasUnknownParamsOrReturn());
  }

  private JSType createJSType(JSTypeRegistry registry, Node node) {
    return registry.getOrCreateNamedType(node.getTypeName());
  }

  private static JSTypeRegistry createJSTypeRegistry(ErrorReporter errorReporter) {
    return new JSTypeRegistry(errorReporter);
  }
}