import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeEqualsTester;
import com.google.javascript.rhino.testing.TypeThrowsTester;
import com.google.javascript.rhino.testing.ValueAssert;
import com.google.javascript.rhino.type.ObjectType;
import com.google.javascript.rhino.type.StaticScope;
import com.google.javascript.rhino.type.TypeFactory;
import com.google.javascript.rhino.type.ValueType;
import com.google.javascript.rhino.type.error.TypeError;

import static com.google.common.truth.Truth.assertThat;

public class NamedTypeTest {
  private static final StaticScope<JSType> ENCLOSING = new StaticScope<>();
  private static final ErrorReporter ERROR_REPORTER = new ErrorReporter() {
    @Override
    public void warning(String message, String sourceName, int line, int charPosition) {
      System.out.println("Warning: " + message);
    }

    @Override
    public void error(String message, String sourceName, int line, int charPosition) {
      throw new AssertionError("Unexpected error: " + message);
    }
  };

  @org.junit.Test
  public void testResolveViaRegistry() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    TypeFactory typeFactory = new TypeFactory(new JSTypeFactory(), registry);
    registry.registerType(new StringType("StringType"));
    registry.registerType(new BooleanType("BooleanType"));
    NamedType namedType = new NamedType(registry, "StringType", "MySource.java", 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING);

    // Then
    ValueType expectedType = ValueType.get("String");
    TypeEqualsTester.assertEquals(expectedType, resolvedType);
  }

  @org.junit.Test
  public void testResolveViaRegistryUnknownType() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    TypeFactory typeFactory = new TypeFactory(new JSTypeFactory(), registry);
    NamedType namedType = new NamedType(registry, "UnknownType", "MySource.java", 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING);

    // Then
    JSType expectedType = registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE);
    TypeEqualsTester.assertEquals(expectedType, resolvedType);
  }

  @org.junit.Test
  public void testResolveViaProperties() {
    // Given
    TypeFactory typeFactory = new TypeFactory(new JSTypeFactory(), null);
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType baseType = typeFactory.createObjectType("BaseType");
    ObjectType derivedType = typeFactory.createObjectType("DerivedType");
    derivedType.setPrototypeOf(baseType);
    registry.registerType(baseType);
    registry.registerType(derivedType);

    StaticScope<JSType> scope = new StaticScope<>();
    scope.setSlot("BaseType", baseType);

    NamedType namedType = new NamedType(registry, "BaseType", "MySource.java", 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, scope);

    // Then
    TypeEqualsTester.assertEquals(baseType, resolvedType);
  }

  @org.junit.Test
  public void testResolveViaPropertiesUnknownType() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    TypeFactory typeFactory = new TypeFactory(new JSTypeFactory(), registry);
    ObjectType baseType = typeFactory.createObjectType("BaseType");
    registry.registerType(baseType);

    StaticScope<JSType> scope = new StaticScope<>();
    scope.setSlot("UnknownType", null);

    NamedType namedType = new NamedType(registry, "UnknownType", "MySource.java", 1, 1);

    // When & Then
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, scope);
    ValueAssert.that(resolvedType).isNull();
  }

  @org.junit.Test
  public void testResolveViaPropertiesDifferentNameSpace() {
    // Given
    TypeFactory typeFactory = new TypeFactory(new JSTypeFactory(), null);
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType baseType1 = typeFactory.createObjectType("BaseType1");
    ObjectType baseType2 = typeFactory.createObjectType("BaseType2");
    ObjectType derivedType = typeFactory.createObjectType("DerivedType");
    derivedType.setPrototypeOf(baseType1);
    registry.registerType(baseType1);
    registry.registerType(baseType2);

    StaticScope<JSType> scope1 = new StaticScope<>();
    scope1.setSlot("BaseType1", baseType1);

    NamedType namedType = new NamedType(registry, "BaseType2", "MySource.java", 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, scope1);

    // Then
    ValueAssert.that(resolvedType).isNull();
  }

  @org.junit.Test
  public void testResolveViaPropertiesWithError() {
    // Given
    TypeFactory typeFactory = new TypeFactory(new JSTypeFactory(), null);
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType baseType = typeFactory.createObjectType("BaseType");
    registry.registerType(baseType);

    StaticScope<JSType> scope = new StaticScope<>();
    scope.setSlot("UnknownType", null);

    NamedType namedType = new NamedType(registry, "UnknownType", "MySource.java", 1, 1);

    // When & Then
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, scope);
    TypeThrowsTester.assertThrows(TypeError.class, () -> resolvedType.toString());
  }
}