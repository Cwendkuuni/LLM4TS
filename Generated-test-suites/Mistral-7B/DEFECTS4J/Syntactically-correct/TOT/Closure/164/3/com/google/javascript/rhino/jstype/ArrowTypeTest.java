import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testArrowTypeCreation() {
    // Simple arrow type with no arguments and no return type
    Node params = null;
    Node returnTypeNode = null;
    ArrowType arrowType = new ArrowType(registry, params, returnTypeNode);
    assertEquals("ArrowType{parameters=null, returnType=UNKNOWN_TYPE, " +
        "returnTypeInferred=false}", arrowType.toString());

    // Arrow type with arguments and no return type
    Node param1 = registry.createParameter("string");
    Node paramsWithVarArgs = registry.createParametersWithVarArgs(param1);
    ArrowType arrowTypeWithParams = new ArrowType(registry, paramsWithVarArgs, null);
    assertEquals("ArrowType{parameters=" + paramsWithVarArgs.toString() +
        ", returnType=UNKNOWN_TYPE, " +
        "returnTypeInferred=false}", arrowTypeWithParams.toString());

    // Arrow type with arguments and return type
    Node param2 = registry.createParameter("number");
    Node returnType = registry.getNativeType(JSTypeNative.INT);
    ArrowType arrowTypeWithParamsAndReturn = new ArrowType(registry, paramsWithVarArgs, returnType);
    assertEquals("ArrowType{parameters=" + paramsWithVarArgs.toString() +
        ", returnType=" + returnType.toString() + ", " +
        "returnTypeInferred=false}", arrowTypeWithParamsAndReturn.toString());

    // Arrow type with arguments, return type, and return type inferred
    ArrowType arrowTypeWithParamsAndReturnInferred = new ArrowType(
        registry, paramsWithVarArgs, returnType, true);
    assertEquals("ArrowType{parameters=" + paramsWithVarArgs.toString() +
        ", returnType=" + returnType.toString() + ", " +
        "returnTypeInferred=true}", arrowTypeWithParamsAndReturnInferred.toString());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    Node param1 = registry.createParameter("number");
    Node param2 = registry.createParameter("string");
    Node returnType1 = registry.getNativeType(JSTypeNative.INT);
    Node returnType2 = registry.getNativeType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType2);
    ArrowType arrowType3 = new ArrowType(registry, param1, returnType1);
    ArrowType arrowType4 = new ArrowType(registry, param1, returnType2);
    ArrowType arrowType5 = new ArrowType(registry, param2, returnType1);
    ArrowType arrowType6 = new ArrowType(registry, param2, returnType2);

    // Test subtype with same parameters and return type
    assertTrue(arrowType3.isSubtype(arrowType1));

    // Test subtype with same return type but different parameters
    assertTrue(arrowType4.isSubtype(arrowType1));

    // Test subtype with different parameters but same return type
    assertTrue(arrowType5.isSubtype(arrowType2));

    // Test subtype with different parameters and return types
    assertFalse(arrowType3.isSubtype(arrowType5));

    // Test subtype with the same arrow type
    assertTrue(arrowType4.isSubtype(arrowType4));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node param1 = registry.createParameter("number");
    Node param2 = registry.createParameter("string");
    Node returnType = registry.getNativeType(JSTypeNative.INT);

    ArrowType arrowType1 = new ArrowType(registry, param1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, param2, returnType);
    ArrowType arrowType3 = new ArrowType(registry, param1, returnType);

    // Test equal parameters
    assertTrue(arrowType1.hasEqualParameters(arrowType3));

    // Test different parameters
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeEquivalence() {
    Node param1 = registry.createParameter("number");
    Node param2 = registry.createParameter("string");
    Node returnType1 = registry.getNativeType(JSTypeNative.INT);
    Node returnType2 = registry.getNativeType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, param1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, param2, returnType1);
    ArrowType arrowType3 = new ArrowType(registry, param1, returnType2);
    ArrowType arrowType4 = new ArrowType(registry, param1, returnType1);

    // Test equal arrow types
    assertTrue(arrowType1.isEquivalentTo(arrowType4));

    // Test non-equal arrow types with different return types
    assertFalse(arrowType1.isEquivalentTo(arrowType2));

    // Test non-equal arrow types with different parameters
    assertFalse(arrowType1.isEquivalentTo(arrowType3));
  }

  @Test
  public void testArrowTypeHashCode() {
    Node param1 = registry.createParameter("number");
    Node param2 = registry.createParameter("string");
    Node returnType1 = registry.getNativeType(JSTypeNative.INT);
    Node returnType2 = registry.getNativeType(JSTypeNative.STRING);

    ArrowType arrowType1 = new ArrowType(registry, param1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, param2, returnType1);
    ArrowType arrowType3 = new ArrowType(registry, param1, returnType2);

    // Test equal arrow types with the same hash code
    assertEquals(arrowType1.hashCode(), arrowType1.hashCode());
    assertEquals(arrowType1.hashCode(), arrowType4.hashCode());

    // Test different arrow types with different hash codes
    assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
    assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());
  }

  @Test
  public void testArrowTypeResolve() {
    Node param1 = registry.createParameter("number");
    Node returnType = registry.getNativeType(JSTypeNative.INT);

    ArrowType arrowType = new ArrowType(registry, param1, returnType);

    // Test resolve with valid types
    assertNotNull(arrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry)));

    // Test resolve with null parameters
    Node nullParams = null;
    ArrowType nullArrowType = new ArrowType(registry, nullParams, returnType);
    assertNull(nullArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry)));

    // Test resolve with invalid or unknown types
    Node unknownType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    ArrowType unknownArrowType = new ArrowType(registry, param1, unknownType);
    assertNull(unknownArrowType.resolveInternal(errorReporter, new StaticScope<JSType>(registry)));
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturn() {
    Node param1 = registry.createParameter("number");
    Node param2 = registry.createParameter("string");
    Node returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);

    ArrowType arrowType1 = new ArrowType(registry, param1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, param1, returnType);
    ArrowType arrowType3 = new ArrowType(registry, param2, returnType);

    // Test hasUnknownParamsOrReturn with same arrow types
    assertTrue(arrowType1.hasUnknownParamsOrReturn());
    assertTrue(arrowType2.hasUnknownParamsOrReturn());

    // Test hasUnknownParamsOrReturn with different arrow types
    assertTrue(arrowType3.hasUnknownParamsOrReturn());

    // Test hasUnknownParamsOrReturn with arrow types with known types
    Node knownType = registry.getNativeType(JSTypeNative.INT);
    ArrowType knownArrowType1 = new ArrowType(registry, param1, knownType);
    ArrowType knownArrowType2 = new ArrowType(registry, param2, knownType);
    assertFalse(knownArrowType1.hasUnknownParamsOrReturn());
    assertFalse(knownArrowType2.hasUnknownParamsOrReturn());
  }
}