import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createArrowType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createParametersWithVarArgs;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createType;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeCreation() {
    Node params = createParametersWithVarArgs(registry, createType(registry, "number"));
    JSType returnType = createType(registry, "string");
    ArrowType arrowType = new ArrowType(registry, params, returnType);

    assertEquals("Expected arrow type with correct parameters and return type",
        arrowType.toString(), "(number) -> string");
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    Node params = createParametersWithVarArgs(registry, createType(registry, "number"));
    ArrowType arrowType = new ArrowType(registry, params, null, true);

    assertEquals("Expected arrow type with inferred return type and correct parameters",
        arrowType.toString(), "(number) -> unknown");
  }

  @Test
  public void testArrowTypeCreationWithNoParameters() {
    ArrowType arrowType = new ArrowType(registry, null, createType(registry, "string"));

    assertEquals("Expected arrow type with no parameters and correct return type",
        arrowType.toString(), "() -> string");
  }

  @Test
  public void testArrowTypeCreationWithEmptyParameters() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertEquals("Expected arrow type with no parameters and unknown return type",
        arrowType.toString(), "() -> unknown");
  }

  @Test
  public void testArrowTypeIsSubtype() {
    Node params1 = createParametersWithVarArgs(registry, createType(registry, "number"));
    JSType returnType1 = createType(registry, "string");
    ArrowType arrowType1 = new ArrowType(registry, params1, returnType1);

    Node params2 = createParametersWithVarArgs(registry, createType(registry, "number"));
    JSType returnType2 = createType(registry, "number");
    ArrowType arrowType2 = new ArrowType(registry, params2, returnType2);

    assertTrue("Expected arrowType1 to be a subtype of arrowType2",
        arrowType1.isSubtype(arrowType2));

    // In this case, arrowType2 should not be a subtype of arrowType1
    // since it has a different return type
    assertFalse("Expected arrowType2 not to be a subtype of arrowType1",
        arrowType2.isSubtype(arrowType1));

    params2 = createParametersWithVarArgs(registry, createType(registry, "number"));
    JSType returnType3 = createType(registry, "string");
    ArrowType arrowType3 = new ArrowType(registry, params2, returnType3, false);

    // In this case, arrowType1 should not be a subtype of arrowType3
    // since it has inferred return type
    assertFalse("Expected arrowType1 not to be a subtype of arrowType3",
        arrowType1.isSubtype(arrowType3));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node params1 = createParametersWithVarArgs(registry, createType(registry, "number"));
    Node params2 = createParametersWithVarArgs(registry, createType(registry, "number"));
    ArrowType arrowType1 = new ArrowType(registry, params1, null);
    ArrowType arrowType2 = new ArrowType(registry, params2, null);

    assertTrue("Expected arrowType1 and arrowType2 to have equal parameters",
        arrowType1.hasEqualParameters(arrowType2));

    // In this case, arrowType3 should not have equal parameters with arrowType1
    Node params3 = createParametersWithVarArgs(registry, createType(registry, "boolean"));
    ArrowType arrowType3 = new ArrowType(registry, params3, null);
    assertFalse("Expected arrowType1 and arrowType3 to not have equal parameters",
        arrowType1.hasEqualParameters(arrowType3));
  }

  @Test
  public void testArrowTypeEquivalence() {
    Node params1 = createParametersWithVarArgs(registry, createType(registry, "number"));
    Node params2 = createParametersWithVarArgs(registry, createType(registry, "number"));
    JSType returnType1 = createType(registry, "string");
    JSType returnType2 = createType(registry, "number");
    ArrowType arrowType1 = new ArrowType(registry, params1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, params2, returnType2);

    // arrowType1 and arrowType2 should not be equivalent
    assertFalse("Expected arrowType1 and arrowType2 not to be equivalent",
        arrowType1.isEquivalentTo(arrowType2));

    // arrowType1 and arrowType1 should be equivalent
    ArrowType arrowType1_1 = new ArrowType(registry, params1, returnType1);
    assertTrue("Expected arrowType1 and arrowType1_1 to be equivalent",
        arrowType1.isEquivalentTo(arrowType1_1));

    // arrowType1 and arrowType3 should not be equivalent (different return type)
    Node params3 = createParametersWithVarArgs(registry, createType(registry, "boolean"));
    ArrowType arrowType3 = new ArrowType(registry, params3, returnType1);
    assertFalse("Expected arrowType1 and arrowType3 not to be equivalent",
        arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node params1 = createParametersWithVarArgs(registry, createType(registry, "number"));
    Node params2 = createParametersWithVarArgs(registry, createType(registry, "number"));
    JSType returnType1 = createType(registry, "string");
    JSType returnType2 = createType(registry, "number");
    ArrowType arrowType1 = new ArrowType(registry, params1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, params2, returnType2);

    int hashCodeArrowType1 = arrowType1.hashCode();
    int hashCodeArrowType2 = arrowType2.hashCode();

    // hashCode should be different for different arrow types
    assertNotEquals("Expected hashCode of arrowType1 to be different from hashCode of arrowType2",
        hashCodeArrowType1, hashCodeArrowType2);

    // Creating a new instance of arrowType1 should return the same hashCode
    ArrowType arrowType1_1 = new ArrowType(registry, params1, returnType1);
    assertEquals("Expected hashCode of arrowType1 and arrowType1_1 to be the same",
        arrowType1.hashCode(), arrowType1_1.hashCode());
  }

  @Test
  public void testArrowTypeToString() {
    Node params = createParametersWithVarArgs(registry, createType(registry, "number"));
    JSType returnType = createType(registry, "string");
    ArrowType arrowType = new ArrowType(registry, params, returnType);

    String expectedToString = "(number) -> string";
    assertEquals("Expected arrowType to have correct toString representation",
        expectedToString, arrowType.toString());
  }
}