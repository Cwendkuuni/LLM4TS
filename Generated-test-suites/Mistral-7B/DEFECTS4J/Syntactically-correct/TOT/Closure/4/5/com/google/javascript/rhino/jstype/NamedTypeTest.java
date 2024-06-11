import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.BaseTestCase;
import com.google.javascript.rhino.testing.AssertThrows;
import com.google.javascript.rhino.testing.PropertyChecker;

/**
 * Test cases for {@code NamedType} class in {@code com.google.javascript.rhino.jstype.package-name}.
 */
public class NamedTypeTest extends BaseTestCase {

  /**
   * Verifies that a new {@code NamedType} instance can be created and its properties can be defined.
   */
  public void testCreateNamedType() {
    JSTypeRegistry registry = getTestRegistry();
    NamedType namedType = new NamedType(registry, "String", "test.js", 1, 1);
    assertNotNull(namedType);

    // Define a property for the `namedType`.
    JSType type = registry.getType("Number");
    namedType.defineProperty("property", type, false, null);
    PropertyChecker.assertPropertyDefined(namedType, "property");
  }

  /**
   * Verifies that a {@code NamedType} instance can't be resolved if it doesn't have a referenced type.
   */
  public void testUnresolvedNamedType() {
    JSTypeRegistry registry = getTestRegistry();
    NamedType namedType = new NamedType(registry, "UnknownType", "test.js", 1, 1);
    assertNotNull(namedType);
    assertFalse(namedType.isResolved());

    // Test property definition fails when the named type is unresolved.
    JSType type = registry.getType("Number");
    namedType.defineProperty("property", type, false, null);
    assertFalse(namedType.hasProperty("property"));
  }

  /**
   * Verifies that a {@code NamedType} instance can be resolved to its referenced type.
   */
  public void testNamedTypeResolved() {
    JSTypeRegistry registry = getTestRegistry();
    NamedType namedType = new NamedType(registry, "String", "test.js", 1, 1);
    ObjectType stringType = registry.getType("String");

    // Set the referenced type for the `namedType`.
    namedType.setReferencedType(stringType);
    assertTrue(namedType.isResolved());
    assertEquals(stringType, namedType.getReferencedType());

    // Test property definition and retrieval after the named type is resolved.
    namedType.defineProperty("property", registry.getType("Number"), false, null);
    PropertyChecker.assertPropertyDefined(namedType, "property");
    JSType propertyType = namedType.getPropertyType("property");
    assertEquals(registry.getType("Number"), propertyType);
  }

  /**
   * Verifies that a {@code NamedType} instance can't be resolved to multiple types when using the same reference.
   */
  public void testNamedTypeImplicitPrototypeCycle() {
    JSTypeRegistry registry = getTestRegistry();
    NamedType namedTypeA = new NamedType(registry, "NamedTypeA", "test.js", 1, 1);
    NamedType namedTypeB = new NamedType(registry, "NamedTypeB", "test.js", 1, 1);

    ObjectType typeA = registry.getType("ObjectTypeA");
    ObjectType typeB = registry.getType("ObjectTypeB");

    // Set the referenced types for both `NamedType` instances.
    namedTypeA.setReferencedType(typeA);
    namedTypeB.setReferencedType(typeB);

    // Ensure that the named types can't be resolved to each other.
    assertFalse(namedTypeA.isResolved());
    assertFalse(namedTypeB.isResolved());

    namedTypeA.resolve(null, null);
    namedTypeB.resolve(null, null);

    // Check that the named types have been resolved to their referenced types.
    assertTrue(namedTypeA.isResolved());
    assertTrue(namedTypeB.isResolved());
    assertEquals(typeA, namedTypeA.getReferencedType());
    assertEquals(typeB, namedTypeB.getReferencedType());
  }

  /**
   * Tests that {@code NamedType} handles property continuations correctly.
   */
  public void testNamedTypePropertyContinuations() {
    JSTypeRegistry registry = getTestRegistry();
    NamedType namedType = new NamedType(registry, "NamedType", "test.js", 1, 1);

    // Define a property continuation for the `NamedType`.
    namedType.defineProperty("property1", registry.getType("Number"), false, null);
    PropertyChecker.assertPropertyDefined(namedType, "property1");

    ObjectType referencedType = registry.getType("ObjectType");
    namedType.setReferencedType(referencedType);

    // Ensure that the property continuation is committed when the named type is resolved.
    PropertyChecker.assertPropertyDefined(namedType, "property1");
    JSType propertyType = namedType.getPropertyType("property1");
    assertEquals(registry.getType("Number"), propertyType);
  }

  /**
   * Verifies that a {@code NamedType} instance throws an exception when its referenced type is a cycle.
   */
  public void testNamedTypeTypeCycle() {
    JSTypeRegistry registry = getTestRegistry();
    NamedType namedType1 = new NamedType(registry, "NamedType1", "test.js", 1, 1);
    NamedType namedType2 = new NamedType(registry, "NamedType2", "test.js", 1, 1);

    ObjectType type1 = registry.getType("ObjectType1");
    ObjectType type2 = registry.getType("ObjectType2");

    // Set the referenced types for both `NamedType` instances.
    namedType1.setReferencedType(type1);
    namedType2.setReferencedType(type2);

    // Ensure that the named types have a cycle in their referenced types.
    type1.setPrototypeOf(type2);
    type2.setPrototypeOf(type1);

    // Test that a cycle in referenced types causes an exception.
    AssertThrows.expect(IllegalArgumentException.class);
    namedType1.resolve(null, null);
  }

  private JSTypeRegistry getTestRegistry() {
    return new JSTypeRegistry() {{
      put("ObjectTypeA", createObjectType("ObjectTypeA", false));
      put("ObjectTypeB", createObjectType("ObjectTypeB", false));
      put("String", createStringType());
      put("Number", createNumberType());
    }};
  }
}