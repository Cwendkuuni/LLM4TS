import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TestBase;

import java.util.Arrays;

public class ArrowTypeTest extends TestBase {

  private static class ArrowTypeTestHelper {
    JSTypeRegistry registry;
    ErrorReporter errorReporter;

    ArrowTypeTestHelper() {
      registry = new JSTypeRegistry();
      errorReporter = new ErrorReporter();
    }

    ArrowType createArrowType(Node parameters, JSType returnType) {
      return new ArrowType(registry, parameters, returnType);
    }
  }

  @org.junit.Test
  public void testArrowTypeWithNormalParametersAndReturnType() {
    ArrowTypeTestHelper helper = new ArrowTypeTestHelper();
    Node parameters = registry.createParameters(
        registry.createNamedType("number"),
        registry.createNamedType("string"));
    JSType returnType = registry.createNamedType("boolean");

    ArrowType arrowType = helper.createArrowType(parameters, returnType);

    // Positive test case
    assertNotNull(arrowType);
    assertEquals(UNKNOWN_TYPE, arrowType.getBaseType());
    assertEquals(2, arrowType.numParams());
    assertEquals("number, string", arrowType.paramTypesString());
    assertEquals(returnType, arrowType.getReturnType());
    assertFalse(arrowType.returnTypeInferred);

    // Test equals
    ArrowType arrowType2 = helper.createArrowType(parameters, returnType);
    assertTrue(arrowType.equals(arrowType2));
    assertEquals(hashCode(arrowType), hashCode(arrowType2));

    // Test hashcode
    ArrowType arrowType3 = helper.createArrowType(null, returnType);
    assertNotEquals(hashCode(arrowType), hashCode(arrowType3));

    // Test subtyping
    JSType arrowType4 =
        helper.createArrowType(registry.createParameters(null), returnType);
    assertTrue(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 =
        helper.createArrowType(registry.createParameters(null, registry.createNamedType("number")), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType5, true));

    // Test isSubtype when return types are not equal
    ArrowType arrowType6 = helper.createArrowType(parameters, registry.createNamedType("number"));
    assertFalse(arrowType.isSubtype(arrowType6));
  }

  @org.junit.Test
  public void testArrowTypeWithVarArgs() {
    ArrowTypeTestHelper helper = new ArrowTypeTestHelper();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createParameters(
            registry.createNamedType("number"),
            registry.createNamedType("string")));
    JSType returnType = registry.createNamedType("boolean");

    ArrowType arrowType = helper.createArrowType(parameters, returnType);

    // Positive test case
    assertNotNull(arrowType);
    assertEquals(UNKNOWN_TYPE, arrowType.getBaseType());
    assertEquals(2, arrowType.numParams());
    assertEquals("number, string, ...", arrowType.paramTypesString());
    assertEquals(returnType, arrowType.getReturnType());
    assertFalse(arrowType.returnTypeInferred);

    // Test equals
    ArrowType arrowType2 = helper.createArrowType(parameters, returnType);
    assertTrue(arrowType.equals(arrowType2));
    assertEquals(hashCode(arrowType), hashCode(arrowType2));

    // Test hashcode
    ArrowType arrowType3 = helper.createArrowType(
        registry.createParametersWithVarArgs(registry.createParameters(null)), returnType);
    assertNotEquals(hashCode(arrowType), hashCode(arrowType3));

    // Test subtyping
    ArrowType arrowType4 =
        helper.createArrowType(registry.createParametersWithVarArgs(
            registry.createParameters(registry.createNamedType("number"))), returnType);
    assertTrue(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 =
        helper.createArrowType(registry.createParametersWithVarArgs(
            registry.createParameters(registry.createNamedType("number"), null)), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType5, true));
  }

  @org.junit.Test
  public void testArrowTypeWithOptionalParameters() {
    ArrowTypeTestHelper helper = new ArrowTypeTestHelper();
    Node parameters = registry.createParameters(
        registry.createNamedType("number"),
        registry.createOptionalNamedType("string"));
    JSType returnType = registry.createNamedType("boolean");

    ArrowType arrowType = helper.createArrowType(parameters, returnType);

    // Positive test case
    assertNotNull(arrowType);
    assertEquals(UNKNOWN_TYPE, arrowType.getBaseType());
    assertEquals(2, arrowType.numParams());
    assertEquals("number, string?", arrowType.paramTypesString());
    assertEquals(returnType, arrowType.getReturnType());
    assertFalse(arrowType.returnTypeInferred);

    // Test equals
    ArrowType arrowType2 = helper.createArrowType(parameters, returnType);
    assertTrue(arrowType.equals(arrowType2));
    assertEquals(hashCode(arrowType), hashCode(arrowType2));

    // Test hashcode
    ArrowType arrowType3 = helper.createArrowType(
        registry.createParameters(registry.createNamedType("number"), null), returnType);
    assertNotEquals(hashCode(arrowType), hashCode(arrowType3));

    // Test subtyping
    ArrowType arrowType4 =
        helper.createArrowType(registry.createParameters(
            registry.createNamedType("number"),
            registry.createOptionalNamedType("string")), returnType);
    assertTrue(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 =
        helper.createArrowType(registry.createParameters(
            registry.createNamedType("number"),
            registry.createOptionalNamedType("number")), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType5, true));
  }

  @org.junit.Test
  public void testArrowTypeWithUnknownTypes() {
    ArrowTypeTestHelper helper = new ArrowTypeTestHelper();
    Node parameters = registry.createParameters(
        registry.createOptionalNamedType("number"),
        registry.createOptionalNamedType(UNKNOWN_TYPE));
    JSType returnType = registry.createNamedType("boolean");

    ArrowType arrowType = helper.createArrowType(parameters, returnType);

    // Positive test case
    assertNotNull(arrowType);
    assertEquals(UNKNOWN_TYPE, arrowType.getBaseType());
    assertEquals(2, arrowType.numParams());
    assertEquals("number?, unknownType?", arrowType.paramTypesString());
    assertEquals(returnType, arrowType.getReturnType());
    assertFalse(arrowType.returnTypeInferred);

    // Test equals
    ArrowType arrowType2 = helper.createArrowType(parameters, returnType);
    assertTrue(arrowType.equals(arrowType2));
    assertEquals(hashCode(arrowType), hashCode(arrowType2));

    // Test hashcode
    ArrowType arrowType3 = helper.createArrowType(
        registry.createParameters(registry.createOptionalNamedType(UNKNOWN_TYPE), null), returnType);
    assertNotEquals(hashCode(arrowType), hashCode(arrowType3));

    // Test subtyping
    ArrowType arrowType4 =
        helper.createArrowType(registry.createParameters(
            registry.createOptionalNamedType("number"),
            registry.createOptionalNamedType(UNKNOWN_TYPE)), returnType);
    assertTrue(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 =
        helper.createArrowType(registry.createParameters(
            registry.createOptionalNamedType("number"),
            registry.createOptionalNamedType("number")), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType5, true));
  }

  @org.junit.Test
  public void testArrowTypeWithEmptyParameterList() {
    ArrowTypeTestHelper helper = new ArrowTypeTestHelper();
    Node parameters = null;
    JSType returnType = registry.createNamedType("boolean");

    ArrowType arrowType = helper.createArrowType(parameters, returnType);

    // Positive test case
    assertNotNull(arrowType);
    assertEquals(UNKNOWN_TYPE, arrowType.getBaseType());
    assertEquals(0, arrowType.numParams());
    assertEquals("", arrowType.paramTypesString());
    assertEquals(returnType, arrowType.getReturnType());
    assertTrue(arrowType.returnTypeInferred);

    // Test equals
    ArrowType arrowType2 = helper.createArrowType(null, returnType);
    assertTrue(arrowType.equals(arrowType2));
    assertEquals(hashCode(arrowType), hashCode(arrowType2));

    // Test hashcode
    ArrowType arrowType3 = helper.createArrowType(
        registry.createParameters(null), returnType);
    assertNotEquals(hashCode(arrowType), hashCode(arrowType3));

    // Test subtyping
    ArrowType arrowType4 =
        helper.createArrowType(registry.createParametersWithVarArgs(null), returnType);
    assertTrue(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 =
        helper.createArrowType(registry.createParameters(null), null);
    assertTrue(arrowType.hasEqualParameters(arrowType5, true));
  }

  @org.junit.Test
  public void testArrowTypeWithInferredReturnType() {
    ArrowTypeTestHelper helper = new ArrowTypeTestHelper();
    Node parameters = registry.createParameters(
        registry.createNamedType("number"),
        registry.createNamedType("string"));
    JSType returnType = null;

    ArrowType arrowType = helper.createArrowType(parameters, returnType);

    // Positive test case
    assertNotNull(arrowType);
    assertEquals(UNKNOWN_TYPE, arrowType.getBaseType());
    assertEquals(2, arrowType.numParams());
    assertEquals("number, string", arrowType.paramTypesString());
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.returnTypeInferred);

    // Test equals
    ArrowType arrowType2 = helper.createArrowType(parameters, returnType);
    assertTrue(arrowType.equals(arrowType2));
    assertEquals(hashCode(arrowType), hashCode(arrowType2));

    // Test hashcode
    ArrowType arrowType3 = helper.createArrowType(
        registry.createParameters(registry.createNamedType("number"), null), returnType);
    assertNotEquals(hashCode(arrowType), hashCode(arrowType3));

    // Test subtyping
    ArrowType arrowType4 =
        helper.createArrowType(registry.createParameters(null), returnType);
    assertTrue(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 =
        helper.createArrowType(registry.createParameters(registry.createNamedType("number"), null), null);
    assertTrue(arrowType.hasEqualParameters(arrowType5, true));
  }

  @org.junit.Test
  public void testArrowTypeWithMismatchedParameterTypes() {
    ArrowTypeTestHelper helper = new ArrowTypeTestHelper();
    Node parameters = registry.createParameters(
        registry.createNamedType("number"),
        registry.createNamedType("boolean"));
    JSType returnType = registry.createNamedType("string");

    ArrowType arrowType = helper.createArrowType(parameters, returnType);

    // Test equals
    ArrowType arrowType2 = helper.createArrowType(
        registry.createParameters(registry.createNamedType("number"), null), returnType);
    assertFalse(arrowType.equals(arrowType2));

    // Test hashcode
    ArrowType arrowType3 = helper.createArrowType(
        registry.createParametersWithVarArgs(registry.createParameters(
            registry.createNamedType("number"))), returnType);
    assertNotEquals(hashCode(arrowType), hashCode(arrowType3));

    // Test subtyping
    ArrowType arrowType4 = helper.createArrowType(parameters, returnType);
    assertFalse(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 = helper.createArrowType(
        registry.createParametersWithVarArgs(registry.createParameters(
            registry.createNamedType("number"),
            registry.createNamedType("boolean"))), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType5, true));
  }

  @org.junit.Test
  public void testArrowTypeWithMismatchedReturnTypes() {
    ArrowTypeTestHelper helper = new ArrowTypeTestHelper();
    Node parameters = registry.createParameters(
        registry.createNamedType("number"),
        registry.createNamedType("string"));
    JSType returnType1 = registry.createNamedType("boolean");
    JSType returnType2 = registry.createNamedType("string");

    ArrowType arrowType1 = helper.createArrowType(parameters, returnType1);
    ArrowType arrowType2 = helper.createArrowType(parameters, returnType2);

    // Test equals
    assertFalse(arrowType1.equals(arrowType2));

    // Test hashcode
    assertNotEquals(hashCode(arrowType1), hashCode(arrowType2));

    // Test subtyping
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    // Test hasEqualParameters
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertTrue(arrowType2.hasEqualParameters(arrowType1, true));
  }
}