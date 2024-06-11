import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowType;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createNoType;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createParametersWithVarArgs;
import static com.google.javascript.rhino.NodeFactory.createTypeName;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

/**
 * ArrowTypeTest.
 */
public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter t;

  @Before
  public void setUp() {
    registry = createJSTypeRegistry();
    t = ErrorReporter.NULL;
  }

  @Test
  public void testArrowTypeWithParametersAndReturnType() {
    // Test ArrowType with valid parameters and returnType
    Node arrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("B"));
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, null, false);

    assertEquals("Expected return type 'B', got 'B'", "B", arrowType.returnType.getName());
    assertTrue("Expected parameters to be 'A[]' and found '" + arrowType.parameters.toString() + "', got 'A[]'",
        arrowType.parameters.toString().contains("A[]"));
  }

  @Test
  public void testArrowTypeWithNullParametersAndReturnType() {
    // Test ArrowType with null parameters and null returnType
    Node arrowTypeNode = createNode(null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, null, false);

    assertEquals("Expected return type to be null", null, arrowType.returnType);
    assertNull("Expected parameters to be null", arrowType.parameters);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    // Test ArrowType with inferred return type
    Node arrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")), null);
    ArrowType arrowType = new ArrowType(registry, arrowTypeNode, null, true);

    assertNull("Expected return type to be null", arrowType.returnType);
    assertTrue("Expected parameters to be 'A[]'", arrowType.parameters.toString().contains("A[]"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullRegistry() {
    // Test exception when creating ArrowType with null registry
    ErrorReporter errorReporter = new ErrorReporter();
    ArrowType arrowType = new ArrowType(null, null, null, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullParameters() {
    // Test exception when creating ArrowType with null parameters
    ErrorReporter errorReporter = new ErrorReporter();
    ArrowType arrowType = new ArrowType(registry, null, null, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithNullReturnType() {
    // Test exception when creating ArrowType with null returnType
    ErrorReporter errorReporter = new ErrorReporter();
    ArrowType arrowType = new ArrowType(registry, null, null, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithUnknownTypeParameters() {
    // Test exception when creating ArrowType with unknown type parameters
    Node parameters = createParametersWithVarArgs(createNoType());
    ErrorReporter errorReporter = new ErrorReporter();
    ArrowType arrowType = new ArrowType(registry, parameters, null, false);
  }

  @Test
  public void testArrowTypeWithEqualParameters() {
    // Test ArrowType with equal parameters
    Node thisArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("B"));
    ArrowType thisArrowType = new ArrowType(registry, thisArrowTypeNode, null, false);

    Node thatArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("B"));
    ArrowType thatArrowType = new ArrowType(registry, thatArrowTypeNode, null, false);

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @Test
  public void testArrowTypeWithDifferentParameters() {
    // Test ArrowType with different parameters
    Node thisArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("B"));
    ArrowType thisArrowType = new ArrowType(registry, thisArrowTypeNode, null, false);

    Node thatArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("B")),
        createTypeName("C"));
    ArrowType thatArrowType = new ArrowType(registry, thatArrowTypeNode, null, false);

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @Test
  public void testArrowTypeWithEqualParametersAndInferredReturnType() {
    // Test ArrowType with equal parameters and inferred return type
    Node thisArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")), null);
    ArrowType thisArrowType = new ArrowType(registry, thisArrowTypeNode, null, true);

    Node thatArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")), null);
    ArrowType thatArrowType = new ArrowType(registry, thatArrowTypeNode, null, true);

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @Test
  public void testArrowTypeWithDifferentParametersAndInferredReturnType() {
    // Test ArrowType with different parameters and inferred return type
    Node thisArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")), null);
    ArrowType thisArrowType = new ArrowType(registry, thisArrowTypeNode, null, true);

    Node thatArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("B")), null);
    ArrowType thatArrowType = new ArrowType(registry, thatArrowTypeNode, null, true);

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType, false));
  }

  @Test
  public void testArrowTypeWithUnknownParametersAndEqualReturnType() {
    // Test ArrowType with unknown parameters and equal return type
    Node thisArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createNoType()),
        createTypeName("B"));
    ArrowType thisArrowType = new ArrowType(registry, thisArrowTypeNode, null, false);

    Node thatArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createNoType()),
        createTypeName("B"));
    ArrowType thatArrowType = new ArrowType(registry, thatArrowTypeNode, null, false);

    assertTrue(thisArrowType.hasEqualParameters(thatArrowType, true));
  }

  @Test
  public void testArrowTypeWithUnknownParametersAndDifferentReturnType() {
    // Test ArrowType with unknown parameters and different return type
    Node thisArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createNoType()),
        createTypeName("B"));
    ArrowType thisArrowType = new ArrowType(registry, thisArrowTypeNode, null, false);

    Node thatArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createNoType()),
        createTypeName("C"));
    ArrowType thatArrowType = new ArrowType(registry, thatArrowTypeNode, null, false);

    assertFalse(thisArrowType.hasEqualParameters(thatArrowType, true));
  }

  @Test
  public void testArrowTypeEquality() {
    // Test ArrowType equality
    Node thisArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("B"));
    ArrowType thisArrowType = new ArrowType(registry, thisArrowTypeNode, null, false);

    Node thatArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("B"));
    ArrowType thatArrowType = new ArrowType(registry, thatArrowTypeNode, null, false);

    assertEquals("Expected ArrowType equality to be true", thisArrowType, thatArrowType);

    Node differentReturnTypeArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("C"));
    ArrowType differentReturnTypeArrowType = new ArrowType(
        registry, differentReturnTypeArrowTypeNode, null, false);

    assertNotEquals("Expected ArrowType inequality to be true", thisArrowType, differentReturnTypeArrowType);
  }

  @Test
  public void testArrowTypeHashCode() {
    // Test ArrowType hashCode
    Node thisArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("B"));
    ArrowType thisArrowType = new ArrowType(registry, thisArrowTypeNode, null, false);

    Node thatArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("B"));
    ArrowType thatArrowType = new ArrowType(registry, thatArrowTypeNode, null, false);

    int thisArrowTypeHashCode = thisArrowType.hashCode();
    int thatArrowTypeHashCode = thatArrowType.hashCode();

    assertEquals("Expected ArrowType hashcodes to be equal", thisArrowTypeHashCode, thatArrowTypeHashCode);

    Node differentReturnTypeArrowTypeNode = createArrowType(
        registry, createParametersWithVarArgs(createTypeName("A")),
        createTypeName("C"));
    ArrowType differentReturnTypeArrowType = new ArrowType(
        registry, differentReturnTypeArrowTypeNode, null, false);

    int thisArrowTypeDifferentHashCode = thisArrowType.hashCode();
    int differentReturnTypeArrowTypeHashCode = differentReturnTypeArrowType.hashCode();

    assertNotEquals("Expected ArrowType hashcodes to be different", thisArrowTypeDifferentHashCode, differentReturnTypeArrowTypeHashCode);
  }
}