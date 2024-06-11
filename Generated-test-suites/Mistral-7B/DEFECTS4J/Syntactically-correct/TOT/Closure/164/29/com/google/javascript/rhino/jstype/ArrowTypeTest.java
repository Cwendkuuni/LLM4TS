import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithValidParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("number"));
    JSType returnType = registry.createNamedType("string");
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test isSubtype
    FunctionType functionType = new FunctionType(registry,
        registry.createParametersWithVarArgs(registry.createNamedType("number")),
        registry.createNamedType("string"));
    assertTrue(arrowType.isSubtype(functionType));

    // Test hasEqualParameters
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));

    // Test isEquivalentTo
    ArrowType arrowType3 = new ArrowType(registry, parameters, returnType, true);
    assertEquals(arrowType1, arrowType3);

    // Test hashCode
    ArrowType arrowType4 = new ArrowType(registry, parameters, returnType);
    assertEquals(arrowType1.hashCode(), arrowType4.hashCode());
  }

  @Test
  public void testArrowTypeWithNullParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertNull(arrowType.returnType);
    assertNull(arrowType.parameters);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("number"));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertNull(arrowType.returnType);
    assertEquals(JSTypeNative.FUNCTION_TYPE, arrowType.getLeastSupertype(new FunctionType(registry,
        registry.createParametersWithVarArgs(registry.createNamedType("number")),
        registry.createNamedType("number"))));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("number"));
    ArrowType arrowType = new ArrowType(registry, parameters,
        registry.createNamedType("string"));
    arrowType.getLeastSupertype(new FunctionType(registry,
        registry.createParametersWithVarArgs(registry.createNamedType("number")),
        registry.createNamedType("number")));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("number"));
    ArrowType arrowType = new ArrowType(registry, parameters,
        registry.createNamedType("string"));
    arrowType.getGreatestSubtype(new FunctionType(registry,
        registry.createParametersWithVarArgs(registry.createNamedType("number")),
        registry.createNamedType("number")));
  }

  @Test
  public void testArrowTypeWithMismatchedParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node thisParam = registry.createNamedType("number");
    Node thatParam = registry.createNamedType("string");
    Node parameters = registry.createParameters(thisParam, thatParam);
    ArrowType arrowType = new ArrowType(registry, parameters,
        registry.createNamedType("string"));

    // Test isSubtype
    ArrowType arrowType1 = new ArrowType(registry, thisParam,
        registry.createNamedType("string"));
    assertFalse(arrowType.isSubtype(arrowType1));

    // Test hasEqualParameters
    ArrowType arrowType2 = new ArrowType(registry, thisParam,
        registry.createNamedType("string"), false);
    assertFalse(arrowType.hasEqualParameters(arrowType2));

    // Test isEquivalentTo
    ArrowType arrowType3 = new ArrowType(registry, thisParam,
        registry.createNamedType("number"), false);
    assertNotEquals(arrowType, arrowType3);
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("number"));
    ArrowType arrowType = new ArrowType(registry, parameters,
        registry.createNamedType("string"));

    // Test isSubtype
    FunctionType functionType = new FunctionType(registry,
        registry.createParametersWithVarArgs(registry.createNamedType("number")),
        registry.createNamedType("string"));
    assertFalse(arrowType.isSubtype(functionType));

    // Test hasEqualParameters
    ArrowType arrowType1 = new ArrowType(registry, parameters,
        registry.createNamedType("string"));
    assertFalse(arrowType.hasEqualParameters(arrowType1));

    // Test isEquivalentTo
    ArrowType arrowType2 = new ArrowType(registry, parameters,
        registry.createNamedType("string"), true);
    assertEquals(arrowType, arrowType2);
  }
}