import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    // Create an ArrowType with explicit parameters and return types
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType = new IntType(registry);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("ArrowType with explicit parameters and return type", arrowType, arrowType);

    // Create an ArrowType with implicit return type inference
    parameters = registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE));
    JSType arrowTypeWithInferredReturn = new ArrowType(registry, parameters);
    assertTrue("ArrowType with inferred return type should have returnTypeInferred = true", arrowTypeWithInferredReturn.returnTypeInferred);

    // Create an ArrowType with no parameters and no return type
    ArrowType arrowTypeWithNoParamsOrReturn = new ArrowType(registry);
    assertEquals("ArrowType with no parameters or return type", arrowTypeWithNoParamsOrReturn, getNativeType(UNKNOWN_TYPE));
  }

  @Test
  public void testArrowTypeComparison() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test equality with the same ArrowType
    Node parameters1 = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType1 = new IntType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    Node parameters2 = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType2 = new IntType(registry);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue("ArrowTypes created with the same parameters and return types should be equal", arrowType1.equals(arrowType2));
    assertTrue("ArrowTypes created with the same parameters and return types should be equal (hashcodes)", arrowType1.hashCode() == arrowType2.hashCode());

    // Test inequality with different ArrowTypes
    Node parameters3 = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType3 = new StringType(registry);
    ArrowType arrowType3 = new ArrowType(registry, parameters3, returnType3);

    assertFalse("ArrowTypes with different parameters or return types should not be equal", arrowType1.equals(arrowType3));
    assertFalse("ArrowTypes with different parameters or return types should not be equal (hashcodes)", arrowType1.hashCode() == arrowType3.hashCode());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test that an ArrowType is a subtype of itself
    Node parameters1 = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType1 = new IntType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    assertTrue("An ArrowType should be a subtype of itself", arrowType1.isSubtype(arrowType1));

    // Test that an ArrowType is a subtype of an ArrowType with the same parameters and return type
    Node parameters2 = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType2 = new IntType(registry);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue("An ArrowType should be a subtype of an ArrowType with the same parameters and return type", arrowType1.isSubtype(arrowType2));
    assertTrue("An ArrowType with the same parameters and return type should be a subtype of an ArrowType", arrowType2.isSubtype(arrowType1));

    // Test that an ArrowType is a subtype of an ArrowType with different return types
    JSType returnType3 = new StringType(registry);
    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType3);

    assertTrue("An ArrowType with covariant return types should be a subtype of an ArrowType with a supertype return type", arrowType1.isSubtype(arrowType3));

    // Test that an ArrowType is not a subtype of an ArrowType with a subtype return type
    JSType returnType4 = new IntType(registry);
    ArrowType arrowType4 = new ArrowType(registry, parameters1, returnType4);

    assertFalse("An ArrowType should not be a subtype of an ArrowType with a subtype return type", arrowType1.isSubtype(arrowType4));

    // Test that an ArrowType is not a subtype of an ArrowType with a contravariant parameter type
    Node parameters4 = registry.createNamedParameters(registry, "p", new IntType(registry));
    parameters4 = registry.createParametersWithVarArgs(parameters4);
    ArrowType arrowType5 = new ArrowType(registry, parameters4, returnType1);

    assertFalse("An ArrowType should not be a subtype of an ArrowType with a contravariant parameter type", arrowType1.isSubtype(arrowType5));

    // Test that an ArrowType is not a subtype of an ArrowType with a missing required parameter
    JSType arrowType6 = new ArrowType(registry, parameters1);

    assertFalse("An ArrowType should not be a subtype of an ArrowType with a missing required parameter", arrowType1.isSubtype(arrowType6));
  }

  @Test
  public void testArrowTypeHashcode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test that ArrowTypes with the same parameters and return types have the same hashcode
    Node parameters1 = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType1 = new IntType(registry);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType2 = new IntType(registry);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertEquals("ArrowTypes with the same parameters and return types should have the same hashcode", arrowType1.hashCode(), arrowType2.hashCode());

    // Test that ArrowTypes with different parameters or return types have different hashcodes
    Node parameters3 = registry.createParametersWithVarArgs(
        new IntType(registry));
    JSType returnType3 = new StringType(registry);
    ArrowType arrowType3 = new ArrowType(registry, parameters3, returnType3);

    assertNotEquals("ArrowTypes with different parameters or return types should have different hashcodes", arrowType1.hashCode(), arrowType3.hashCode());
  }
}