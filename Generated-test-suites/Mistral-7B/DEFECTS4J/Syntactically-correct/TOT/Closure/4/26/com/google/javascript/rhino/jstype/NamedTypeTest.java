package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.testing.TypeEqualityTester.assertTypesEqual;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.testing.PrivateTester;

import junit.framework.TestCase;

import java.util.List;

public class NamedTypeTest extends TestCase {

  private static final String SOME_NAME = "SomeType";
  private static final String SOME_OTHER_NAME = "SomeOtherType";
  private static final String SOME_ENUM_NAME = "SomeEnum";
  private static final String SOME_ENUM_VALUE_NAME = "SomeEnumValue";

  public void testCreateNamedTypeWithRegistryType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType someType = registry.getIntType();
    NamedType namedType = new NamedType(registry, SOME_NAME, "Test", 1, 1);
    assertTypesEqual(someType, namedType.getReferencedType());
  }

  public void testCreateNamedTypeWithResolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType someType = registry.getIntType();
    NamedType namedType = new NamedType(registry, SOME_NAME, "Test", 1, 1);
    namedType.resolve(null, null);
    assertTypesEqual(namedType, namedType.getReferencedType());
  }

  public void testCreateNamedTypeWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionType functionType = FunctionType.create(null, null);
    NamedType namedType = new NamedType(registry, SOME_NAME, "Test", 1, 1);
    namedType.setReferencedType(functionType);
    namedType.resolve(null, null);
    FunctionType resolvedType = namedType.getReferencedType().toMaybeFunctionType();
    assertEquals(resolvedType.getInstanceType(), namedType.getReferencedType());
  }

  public void testCreateNamedTypeWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    EnumType enumType = registry.createEnumType(SOME_ENUM_NAME, null);
    NamedType namedType = new NamedType(registry, SOME_ENUM_NAME, "Test", 1, 1);
    namedType.setReferencedType(enumType);
    namedType.resolve(null, null);
    EnumType resolvedType = (EnumType) namedType.getReferencedType();
    assertEquals(resolvedType.getName(), SOME_ENUM_NAME);
  }

  public void testCreateNamedTypeWithUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String unresolvedTypeName = "UnresolvedType";
    NamedType namedType = new NamedType(registry, unresolvedTypeName, "Test", 1, 1);
    assertNull(namedType.getReferencedType());
  }

  public void testCreateNamedTypeWithUnresolvedTypeAndValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String unresolvedTypeName = "UnresolvedType";
    Predicate<JSType> validator = jst -> jst instanceof FunctionType;
    NamedType namedType = new NamedType(registry, unresolvedTypeName, "Test", 1, 1);
    namedType.setValidator(validator);
    assertNull(namedType.getReferencedType());
  }

  public void testDefinePropertyOnUnresolvedNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String unresolvedTypeName = "UnresolvedType";
    NamedType namedType = new NamedType(registry, unresolvedTypeName, "Test", 1, 1);
    PropertyContinuation continuation = new PropertyContinuation(
        "propName", registry.getIntType(), true, null);
    namedType.defineProperty("propName", null, true, null);
    PropertyContinuation savedContinuation = namedType.getPropertyContinuations().get(0);
    assertEquals(continuation.propertyName, savedContinuation.propertyName);
    assertEquals(continuation.type, savedContinuation.type);
    assertEquals(continuation.inferred, savedContinuation.inferred);
    assertNull(namedType.getReferencedType());
  }

  public void testFinishPropertiesOnUnresolvedNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String unresolvedTypeName = "UnresolvedType";
    NamedType namedType = new NamedType(registry, unresolvedTypeName, "Test", 1, 1);
    PropertyContinuation continuation1 = new PropertyContinuation(
        "propName1", registry.getIntType(), true, null);
    namedType.defineProperty("propName1", null, true, null);
    PropertyContinuation continuation2 = new PropertyContinuation(
        "propName2", registry.getStringType(), true, null);
    namedType.defineProperty("propName2", null, true, null);
    namedType.finishPropertyContinuations();
    ObjectType referencedObjectType = namedType.getReferencedObjTypeInternal();
    assertEquals(2, referencedObjectType.getPropertyCount());
  }

  public void testFinishPropertiesOnResolvedNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String resolvedTypeName = "ResolvedType";
    JSType resolvedType = registry.getIntType();
    NamedType namedType = new NamedType(registry, resolvedTypeName, "Test", 1, 1);
    namedType.setReferencedType(resolvedType);
    namedType.finishPropertyContinuations();
    ObjectType referencedObjectType = namedType.getReferencedObjTypeInternal();
    assertEquals(0, referencedObjectType.getPropertyCount());
  }

  private static class PropertyContinuation {
    private final String propertyName;
    private final JSType type;
    private final boolean inferred;
    private final Node propertyNode;

    PropertyContinuation(String propertyName, JSType type, boolean inferred, Node propertyNode) {
      this.propertyName = propertyName;
      this.type = type;
      this.inferred = inferred;
      this.propertyNode = propertyNode;
    }
  }

  private static class PrivateTester {
    // Private utility class for accessing private methods
  }
}