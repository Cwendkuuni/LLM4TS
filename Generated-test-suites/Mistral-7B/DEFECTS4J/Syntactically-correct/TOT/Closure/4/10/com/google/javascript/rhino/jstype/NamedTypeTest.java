import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.PolymerTestRunner;
import com.google.javascript.rhino.testing.TypeEqualityTester;

import static com.google.common.truth.Truth.assertThat;

public class NamedTypeTest {

  public static void main(String[] args) {
    PolymerTestRunner runner = new PolymerTestRunner(System.out);
    runner.setStrictMode(false);
    runner.runTest(NamedTypeTest.class);
  }

  /**
   * Test that a named type can be resolved via the registry.
   */
  public void testNamedTypeResolverViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType myType = new NamedType(registry, reference, "MyFile.js", 1, 1);

    // MyType should be resolved to a new ObjectType with the given name
    JSType resolvedType = myType.resolve(new ErrorReporter(), enclosing);
    JSType expectedType = new ObjectType(registry, "MyType", JSTypeNative.OBJECT);
    TypeEqualityTester.assertTypesEqual(expectedType, resolvedType);
  }

  /**
   * Test that a named type can be resolved via properties.
   */
  public void testNamedTypeResolverViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType.SubType";
    StaticScope<JSType> enclosing = new StaticScope<>();

    // Define MyType and SubType
    JSType myType = new ObjectType(registry, "MyType", JSTypeNative.OBJECT);
    JSType subType = new ObjectType(registry, "SubType", JSTypeNative.OBJECT);
    myType.defineProperty("SubType", subType, false, null);

    // Resolve MyType.SubType via properties
    JSType resolvedType = new NamedType(registry, reference, "MyFile.js", 1, 1)
        .resolve(new ErrorReporter(), enclosing);

    JSType expectedType = subType;
    TypeEqualityTester.assertTypesEqual(expectedType, resolvedType);
  }

  /**
   * Test that a named type is unresolved when it's not defined in the registry
   * or via properties.
   */
  public void testNamedTypeUnresolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    StaticScope<JSType> enclosing = new StaticScope<>();

    // Create a named type with an unresolved reference
    JSType myType = new NamedType(registry, reference, "MyFile.js", 1, 1);

    // The type should be unresolved
    JSType resolvedType = myType.resolve(new ErrorReporter(), enclosing);
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.NO_TYPE);
    TypeEqualityTester.assertTypesEqual(expectedType, resolvedType);
  }

  /**
   * Test that a named type can have properties defined after its resolution.
   */
  public void testNamedTypePropertyContinuation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    StaticScope<JSType> enclosing = new StaticScope<>();

    // Define MyType with a continuation
    JSType myType = new NamedType(registry, reference, "MyFile.js", 1, 1);

    // Define a property named "prop" with type "string"
    JSType stringType = registry.getNativeStringType();
    myType.defineProperty("prop", stringType, false, null);

    // Resolve MyType
    JSType resolvedType = myType.resolve(new ErrorReporter(), enclosing);
    JSType expectedType = new ObjectType(registry, "MyType", JSTypeNative.OBJECT);
    expectedType.defineProperty("prop", stringType, false, null);
    TypeEqualityTester.assertTypesEqual(expectedType, resolvedType);
  }

  /**
   * Test that a named type can have multiple properties defined after its
   * resolution.
   */
  public void testNamedTypeMultiplePropertyContinuations() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    StaticScope<JSType> enclosing = new StaticScope<>();

    // Define MyType with multiple property continuations
    JSType myType = new NamedType(registry, reference, "MyFile.js", 1, 1);

    // Define properties named "prop1" and "prop2" with types "number" and "boolean", respectively
    JSType numberType = registry.getNativeNumberType();
    JSType booleanType = registry.getNativeBooleanType();
    myType.defineProperty("prop1", numberType, false, null);
    myType.defineProperty("prop2", booleanType, false, null);

    // Resolve MyType
    JSType resolvedType = myType.resolve(new ErrorReporter(), enclosing);
    JSType expectedType = new ObjectType(registry, "MyType", JSTypeNative.OBJECT);
    expectedType.defineProperty("prop1", numberType, false, null);
    expectedType.defineProperty("prop2", booleanType, false, null);
    TypeEqualityTester.assertTypesEqual(expectedType, resolvedType);
  }

  /**
   * Test that a named type can be validated after being resolved.
   */
  public void testNamedTypeValidation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    StaticScope<JSType> enclosing = new StaticScope<>();

    // Define MyType with a reference to a non-existent type "NonExistentType"
    JSType myType = new NamedType(registry, reference, "MyFile.js", 1, 1);
    Predicate<JSType> validator = jst -> jst instanceof ObjectType &&
        ((ObjectType) jst).getName().equals("NonExistentType");
    myType.setValidator(validator);

    // Resolve MyType, which should fail due to the invalid validation
    try {
      myType.resolve(new ErrorReporter(), enclosing);
      fail("Expected resolution to fail due to invalid validation.");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }
}