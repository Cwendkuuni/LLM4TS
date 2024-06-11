package com.google.javascript.rhino.jstype.test;

import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;

public class NamedTypeTest {

  private static JSTypeRegistry registry = new JSTypeRegistry();
  private static ErrorReporter errorReporter = new ErrorReporter();
  private static StaticScope<JSType> enclosingScope;

  @org.junit.BeforeClass
  public static void init() {
    enclosingScope = registry.createStaticScope();
  }

  @org.junit.Test
  public void testNamedType() {
    // Test creating a named type with a valid name.
    String reference = "MyNamedType";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    assertEquals(reference, namedType.getReferenceName());
    assertEquals(UnknownType, namedType.getType());

    // Test creating a named type with a non-existent name.
    JSType referencedType = registry.getType("NonExistentType");
    NamedType nonExistentNamedType = new NamedType(registry, reference, null, 0, 0);
    assertEquals(UnknownType, nonExistentNamedType.getType());
    assertEquals(nonExistentNamedType, nonExistentNamedType.resolve(errorReporter, enclosingScope));

    // Test creating a named type with a cyclic reference.
    String cyclicReference = "CyclicType";
    JSType cyclicType1 = new ObjectType(registry, JSTypeNative.UNKNOWN_TYPE);
    cyclicType1.setName(cyclicReference);
    NamedType cyclicNamedType1 = new NamedType(registry, cyclicReference, null, 0, 0);
    cyclicNamedType1.setReferencedType(cyclicType1);
    assertEquals(cyclicType1, cyclicNamedType1.resolve(errorReporter, enclosingScope));
    assertEquals(cyclicNamedType1, cyclicType1.getProperties().get(0).getType());

    // Test creating a named type with a reference to an existing type.
    JSType existingType = new ObjectType(registry, JSTypeNative.UNKNOWN_TYPE);
    existingType.setName("ExistingType");
    new PropertyContinuation("prop", FunctionType.create(registry), false, null)
        .commit(existingType);
    NamedType existingNamedType = new NamedType(registry, "ExistingType", null, 0, 0);
    assertEquals(existingType, existingNamedType.resolve(errorReporter, enclosingScope));

    // Test creating a named type with a reference to an interface.
    String interfaceReference = "MyInterface";
    FunctionType interfaceType = new FunctionType(registry, new ObjectType(registry, NO_OBJECT_TYPE), new ObjectType(registry, NO_OBJECT_TYPE));
    interfaceType.setInterface(true);
    interfaceType.setName(interfaceReference);
    new PropertyContinuation("prop", FunctionType.create(registry), false, null)
        .commit(interfaceType);
    NamedType interfaceNamedType = new NamedType(registry, interfaceReference, null, 0, 0);
    assertEquals(interfaceType, interfaceNamedType.resolve(errorReporter, enclosingScope));

    // Test creating a named type with a reference to a constructor.
    String constructorReference = "MyConstructor";
    FunctionType constructorType = new FunctionType(registry, new ObjectType(registry, NO_OBJECT_TYPE), new ObjectType(registry, NO_OBJECT_TYPE));
    constructorType.setConstructor(true);
    constructorType.setName(constructorReference);
    NamedType constructorNamedType = new NamedType(registry, constructorReference, null, 0, 0);
    assertEquals(constructorType.getInstanceType(), constructorNamedType.resolve(errorReporter, enclosingScope));

    // Test creating a named type with a reference to an enum.
    String enumReference = "MyEnum";
    EnumType enumType = new EnumType(registry, "MyEnumElement");
    enumType.setName(enumReference);
    NamedType enumNamedType = new NamedType(registry, enumReference, null, 0, 0);
    assertEquals(enumType.getElementsType(), enumNamedType.resolve(errorReporter, enclosingScope));

    // Test creating a named type with an invalid name.
    String invalidReference = "MyType\\";
    NamedType invalidNamedType = new NamedType(registry, invalidReference, null, 0, 0);
    assertEquals(UnknownType, invalidNamedType.getType());

    // Test creating a named type with a forward declared name.
    String forwardDeclaredReference = "MyForwardDeclaredType";
    registry.registerForwardDeclaration(forwardDeclaredReference);
    NamedType forwardDeclaredNamedType = new NamedType(registry, forwardDeclaredReference, null, 0, 0);
    assertNotNull(forwardDeclaredNamedType.getType());

    // Test creating a named type with an invalid forward declaration.
    registry.registerForwardDeclaration("NonExistentType");
    NamedType nonExistentForwardDeclaredNamedType = new NamedType(registry, "NonExistentType", null, 0, 0);
    assertEquals(UnknownType, nonExistentForwardDeclaredNamedType.getType());

    // Test creating a named type with a circular forward declaration.
    String circularForwardDeclarationReference = "CircularForwardDeclarationType";
    registry.registerForwardDeclaration(circularForwardDeclarationReference);
    NamedType circularForwardDeclaredNamedType1 = new NamedType(registry, circularForwardDeclarationReference, null, 0, 0);
    assertEquals(UnknownType, circularForwardDeclaredNamedType1.getType());
    JSType circularForwardDeclaredType1 = circularForwardDeclaredNamedType1.resolve(errorReporter, enclosingScope);
    assertEquals(UnknownType, circularForwardDeclaredType1.getType());
    StaticSlot<JSType> circularForwardDeclarationSlot =
        enclosingScope.getSlot(circularForwardDeclarationReference);
    circularForwardDeclarationSlot.setType(circularForwardDeclaredType1);
    NamedType circularForwardDeclaredNamedType2 = new NamedType(registry, circularForwardDeclarationReference, null, 0, 0);
    assertEquals(circularForwardDeclaredType1, circularForwardDeclaredNamedType2.resolve(errorReporter, enclosingScope));

    // Test creating a named type with a missing forward declaration.
    String missingForwardDeclarationReference = "MissingForwardDeclarationType";
    registry.registerForwardDeclaration(missingForwardDeclarationReference);
    StaticSlot<JSType> missingForwardDeclarationSlot =
        enclosingScope.getSlot(missingForwardDeclarationReference);
    missingForwardDeclarationSlot.setType(null);
    NamedType missingForwardDeclarationNamedType = new NamedType(registry, missingForwardDeclarationReference, null, 0, 0);
    assertEquals(UnknownType, missingForwardDeclarationNamedType.getType());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testNamedTypeTypeMismatch() {
    String reference = "MyNamedType";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    namedType.setReferencedType(registry.getNativeObjectType(NUMBER));
    assertEquals(UnknownType, namedType.getType());
  }

  @org.junit.Test
  public void testNamedTypeSetValidator() {
    String reference = "MyNamedType";
    Predicate<JSType> validator = jst -> jst instanceof ObjectType;
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    namedType.setValidator(validator);
    JSType referencedType = new ObjectType(registry, NO_OBJECT_TYPE);
    assertEquals(true, validator.apply(referencedType));
    assertEquals(referencedType, namedType.resolve(errorReporter, enclosingScope));
  }

  @org.junit.Test
  public void testNamedTypeHasProperty() {
    String reference = "MyNamedType";
    String propertyName = "prop";
    FunctionType propertyType = FunctionType.create(registry);
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    namedType.setReferencedType(registry.getNativeObjectType(OBJECT));
    ObjectType objectType = (ObjectType) namedType.resolve(errorReporter, enclosingScope);
    assertNotNull(objectType.getPropertyType(propertyName));
    assertEquals(propertyType, objectType.getPropertyType(propertyName));
  }

  @org.junit.Test
  public void testNamedTypeToString() {
    String reference = "MyNamedType";
    String expected = reference;
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    assertEquals(expected, namedType.toString(true));
  }
}