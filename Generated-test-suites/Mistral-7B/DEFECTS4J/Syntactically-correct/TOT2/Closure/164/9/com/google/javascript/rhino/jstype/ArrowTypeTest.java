import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

/**
 * Test for ArrowType class.
 */
public class ArrowTypeTest {

  private static JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testArrowTypeWithParametersAndReturnType() {
    Node parameters = registry.createParameters(
        registry.createParameter("a", INT),
        registry.createParameter("b", STRING));

    ArrowType arrowType = new ArrowType(registry, parameters, INT);

    // Basic functionality test
    ArrowType expectedArrowType = new ArrowType(registry, parameters, INT);
    assertEquals(expectedArrowType, arrowType);

    // Test for equals method
    ArrowType expectedArrowTypeWithSameParametersAndReturnType = new ArrowType(registry, parameters, INT);
    assertTrue(arrowType.isEquivalentTo(expectedArrowTypeWithSameParametersAndReturnType));

    // Test for equals method with different return types
    ArrowType arrowTypeWithDifferentReturnType = new ArrowType(registry, parameters, BOOL);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentReturnType));

    // Test for equals method with different parameters
    ArrowType arrowTypeWithDifferentParameters = new ArrowType(registry,
        registry.createParameters(registry.createParameter("a", INT), registry.createParameter("c", INT)), INT);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentParameters));

    // Test for equals method with null parameters
    ArrowType arrowTypeWithNullParameters = new ArrowType(registry, null, INT);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithNullParameters));

    // Test for equals method with null return type
    ArrowType arrowTypeWithNullReturnType = new ArrowType(registry, parameters, null);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithNullReturnType));

    // Test for equals method with varargs
    ArrowType arrowTypeWithVarargs = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createParameter("a", INT)), INT);
    ArrowType arrowTypeWithVarargsAndVarargs = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createParameter("a", INT)), INT);
    assertTrue(arrowTypeWithVarargs.isEquivalentTo(arrowTypeWithVarargsAndVarargs));

    // Test for hashCode method
    ArrowType arrowTypeHashCodeTest = new ArrowType(registry, parameters, INT);
    ArrowType arrowTypeHashCodeTest2 = new ArrowType(registry, parameters, INT);
    assertEquals(arrowTypeHashCodeTest.hashCode(), arrowTypeHashCodeTest2.hashCode());

    // Test for isSubtype method
    ArrowType arrowTypeWithSameReturnType = new ArrowType(registry, parameters, INT);
    ArrowType arrowTypeWithSameParametersAndInferredReturnType = new ArrowType(registry, parameters, null);
    assertTrue(arrowType.isSubtype(arrowTypeWithSameParametersAndInferredReturnType));

    ArrowType arrowTypeOtherReturnType = new ArrowType(registry, parameters, STRING);
    assertFalse(arrowType.isSubtype(arrowTypeOtherReturnType));

    ArrowType arrowTypeSameParametersAndDifferentReturnTypes = new ArrowType(registry, parameters, BOOL);
    assertFalse(arrowType.isSubtype(arrowTypeSameParametersAndDifferentReturnTypes));

    ArrowType arrowTypeWithDifferentParameters = new ArrowType(registry,
        registry.createParameters(registry.createParameter("a", INT), registry.createParameter("c", INT)), INT);
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentParameters));

    // Test for isSubtype method with null parameters
    ArrowType arrowTypeWithNullParameters2 = new ArrowType(registry, null, INT);
    assertFalse(arrowType.isSubtype(arrowTypeWithNullParameters2));

    // Test for isSubtype method with null return type
    ArrowType arrowTypeWithNullReturnType2 = new ArrowType(registry, parameters, null);
    assertFalse(arrowType.isSubtype(arrowTypeWithNullReturnType2));

    // Test for isSubtype method with varargs
    ArrowType arrowTypeWithVarargs2 = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.createParameter("a", INT)), INT);
    assertTrue(arrowType.isSubtype(arrowTypeWithVarargs2));

    // Test for isSubtype method with an arrow type with less parameters
    ArrowType arrowTypeWithLessParameters = new ArrowType(registry, null, INT);
    assertTrue(arrowType.isSubtype(arrowTypeWithLessParameters));

    // Test for hasEqualParameters method
    ArrowType arrowTypeWithSameParameters = new ArrowType(registry, parameters, INT);
    ArrowType arrowTypeWithSameParameters2 = new ArrowType(registry, parameters, INT);
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithSameParameters2));

    // Test for hasEqualParameters method with different parameters
    ArrowType arrowTypeWithDifferentParameters2 = new ArrowType(registry,
        registry.createParameters(registry.createParameter("a", INT), registry.createParameter("c", INT)), INT);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithDifferentParameters2));

    // Test for hasEqualParameters method with null parameters
    ArrowType arrowTypeWithNullParameters3 = new ArrowType(registry, null, INT);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithNullParameters3));

    // Test for toString method
    assertEquals("ArrowType[parameters:[Parameter[a, int], Parameter[b, string]], returnType:int]", arrowType.toString());
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    Node parameters = registry.createParametersWithVarArgs(
        registry.createParameter("a", INT));

    ArrowType arrowType = new ArrowType(registry, parameters, null);

    // Basic functionality test
    ArrowType expectedArrowType = new ArrowType(registry, parameters, null);
    assertEquals(expectedArrowType, arrowType);

    // Test for equals method
    ArrowType arrowTypeWithSameParametersAndInferredReturnType = new ArrowType(registry, parameters, null);
    assertTrue(arrowType.isEquivalentTo(arrowTypeWithSameParametersAndInferredReturnType));

    // Test for equals method with different parameters
    ArrowType arrowTypeWithDifferentParameters = new ArrowType(registry,
        registry.createParameters(registry.createParameter("a", INT), registry.createParameter("c", INT)), INT);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentParameters));

    // Test for equals method with null parameters
    ArrowType arrowTypeWithNullParameters = new ArrowType(registry, null, null);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithNullParameters));

    // Test for hashCode method
    ArrowType arrowTypeHashCodeTest3 = new ArrowType(registry, parameters, null);
    ArrowType arrowTypeHashCodeTest4 = new ArrowType(registry, parameters, null);
    assertEquals(arrowTypeHashCodeTest3.hashCode(), arrowTypeHashCodeTest4.hashCode());

    // Test for isSubtype method
    ArrowType arrowTypeWithSameParametersAndReturnType = new ArrowType(registry, parameters, INT);
    assertTrue(arrowType.isSubtype(arrowTypeWithSameParametersAndReturnType));

    ArrowType arrowTypeWithDifferentReturnType = new ArrowType(registry, parameters, STRING);
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentReturnType));

    ArrowType arrowTypeSameParametersAndDifferentReturnTypes2 = new ArrowType(registry, parameters, BOOL);
    assertFalse(arrowType.isSubtype(arrowTypeSameParametersAndDifferentReturnTypes2));

    ArrowType arrowTypeWithLessParameters = new ArrowType(registry, null, null);
    assertTrue(arrowType.isSubtype(arrowTypeWithLessParameters));

    // Test for hasEqualParameters method
    ArrowType arrowTypeWithSameParameters2 = new ArrowType(registry, parameters, null);
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithSameParameters2));

    ArrowType arrowTypeWithDifferentParameters2 = new ArrowType(registry,
        registry.createParameters(registry.createParameter("a", INT), registry.createParameter("c", INT)), INT);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithDifferentParameters2));

    ArrowType arrowTypeWithNullParameters4 = new ArrowType(registry, null, null);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithNullParameters4));
  }

  @Test
  public void testArrowTypeWithNullTypeParameters() {
    Node parameters = null;

    ArrowType arrowType = new ArrowType(registry, parameters, INT);

    // Basic functionality test
    ArrowType expectedArrowType = new ArrowType(registry, null, INT);
    assertEquals(expectedArrowType, arrowType);

    // Test for equals method
    ArrowType arrowTypeWithSameParametersAndInferredReturnType = new ArrowType(registry, null, null);
    assertTrue(arrowType.isEquivalentTo(arrowTypeWithSameParametersAndInferredReturnType));

    // Test for equals method with different parameters
    ArrowType arrowTypeWithDifferentParameters = new ArrowType(registry,
        registry.createParameters(registry.createParameter("a", INT), registry.createParameter("c", INT)), INT);
    assertFalse(arrowType.isEquivalentTo(arrowTypeWithDifferentParameters));

    // Test for hashCode method
    ArrowType arrowTypeHashCodeTest5 = new ArrowType(registry, null, INT);
    ArrowType arrowTypeHashCodeTest6 = new ArrowType(registry, null, INT);
    assertEquals(arrowTypeHashCodeTest5.hashCode(), arrowTypeHashCodeTest6.hashCode());

    // Test for isSubtype method
    ArrowType arrowTypeWithSameParametersAndReturnType = new ArrowType(registry, parameters, INT);
    assertTrue(arrowType.isSubtype(arrowTypeWithSameParametersAndReturnType));

    ArrowType arrowTypeWithDifferentReturnType = new ArrowType(registry, parameters, STRING);
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentReturnType));

    ArrowType arrowTypeSameParametersAndDifferentReturnTypes3 = new ArrowType(registry, parameters, BOOL);
    assertFalse(arrowType.isSubtype(arrowTypeSameParametersAndDifferentReturnTypes3));

    ArrowType arrowTypeWithLessParameters = new ArrowType(registry, null, null);
    assertTrue(arrowType.isSubtype(arrowTypeWithLessParameters));

    // Test for hasEqualParameters method
    ArrowType arrowTypeWithSameParameters3 = new ArrowType(registry, null, null);
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithSameParameters3));

    ArrowType arrowTypeWithDifferentParameters3 = new ArrowType(registry,
        registry.createParameters(registry.createParameter("a", INT), registry.createParameter("c", INT)), INT);
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithDifferentParameters3));
  }
}