import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithSingleParameterAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createIdentifier("param");
    ArrowType arrowType = new ArrowType(registry, parameters, JSTypeNative.UNKNOWN_TYPE);

    // Basic functionality test
    assertTrue(arrowType.isSubtype(arrowType));

    // Edge cases and exception handling scenarios
    
    // Test with null parameters
    try {
      new ArrowType(registry, null, JSTypeNative.UNKNOWN_TYPE);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }

    // Test with null return type
    try {
      new ArrowType(registry, parameters, null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }

    // Test with invalid parameters
    try {
      new ArrowType(registry, registry.createIdentifier("invalid"), JSTypeNative.UNKNOWN_TYPE);
      fail("Expected Error");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with invalid return type
    try {
      new ArrowType(registry, parameters, registry.createIdentifier("invalid"));
      fail("Expected Error");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test with same return type
    ArrowType arrowType1 = new ArrowType(registry, parameters, JSTypeNative.UNKNOWN_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, parameters, JSTypeNative.UNKNOWN_TYPE);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));

    // Test with subtypes
    JSType subsTypeReturnType = registry.createBoolean();
    ArrowType arrowTypeSubtypes = new ArrowType(registry, parameters, subsTypeReturnType);
    assertTrue(arrowType.isSubtype(arrowTypeSubtypes));
    assertFalse(arrowTypeSubtypes.isSubtype(arrowType));

    // Test with supertypes
    JSType superTypeReturnType = registry.createString();
    ArrowType arrowTypeSupertypes = new ArrowType(registry, parameters, superTypeReturnType);
    assertFalse(arrowType.isSubtype(arrowTypeSupertypes));
    assertTrue(arrowTypeSupertypes.isSubtype(arrowType));

    // Test with no return type
    ArrowType arrowTypeNoReturnType = new ArrowType(registry, parameters, null, true);
    assertTrue(arrowType.isSubtype(arrowTypeNoReturnType));
    assertFalse(arrowTypeNoReturnType.isSubtype(arrowType));
  }
}