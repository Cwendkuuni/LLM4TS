import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyType;
import com.google.javascript.rhino.jstype.StaticPropertyType;

import java.util.List;

public class NamedTypeTest {

  private static final ErrorReporter REPORTER = new ErrorReporter() {};
  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private static final String SOURCE_NAME = "NamedTypeTest";

  @org.junit.Test
  public void testNamedType_Valid() {
    // Given
    String reference = "MyType";
    JSType referencedType = new ObjectType(TYPE_REGISTERY, "MyType", SOURCE_NAME, 0, 0);
    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When
    PropertyType propertyType = namedType.getPropertyType("property");

    // Then
    assertNotNull(propertyType);
  }

  @org.junit.Test
  public void testNamedType_Unresolved() {
    // Given
    String reference = "UnknownType";
    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When
    PropertyType propertyType = namedType.getPropertyType("property");

    // Then
    assertNull(propertyType);
  }

  @org.junit.Test
  public void testNamedType_InvalidReference() {
    // Given
    String reference = "";
    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When
    PropertyType propertyType = namedType.getPropertyType("property");

    // Then
    assertNull(propertyType);
  }

  @org.junit.Test
  public void testNamedType_ResolveViaRegistry() {
    // Given
    String reference = "MyType";
    JSType referencedType = new ObjectType(TYPE_REGISTERY, "MyType", SOURCE_NAME, 0, 0);
    TYPE_REGISTERY.setType(reference, referencedType);

    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(REPORTER, null);

    // Then
    assertEquals(referencedType, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_SingleComponent() {
    // Given
    String reference = "MyClass.myProperty";
    JSType myClass = new ObjectType(TYPE_REGISTERY, "MyClass", SOURCE_NAME, 0, 0);
    PropertyType propertyType = new PropertyType(TYPE_REGISTERY, "myProperty", JSTypeNative.ANY);
    myClass.defineProperty("myProperty", propertyType, false, null);

    StaticScope<JSType> enclosingScope = new StaticScope<>();
    enclosingScope.putSlot("MyClass", myClass);

    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(REPORTER, enclosingScope);

    // Then
    assertEquals(propertyType.getType(), resolvedType);
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_MultipleComponents() {
    // Given
    String reference = "MyClass.subClass.myProperty";
    JSType myClass = new ObjectType(TYPE_REGISTERY, "MyClass", SOURCE_NAME, 0, 0);
    JSType subClass = new ObjectType(TYPE_REGISTERY, "subClass", SOURCE_NAME, 0, 0);
    PropertyType propertyType = new PropertyType(TYPE_REGISTERY, "myProperty", JSTypeNative.ANY);
    myClass.defineProperty("subClass", subClass, false, null);
    subClass.defineProperty("myProperty", propertyType, false, null);

    StaticScope<JSType> enclosingScope = new StaticScope<>();
    enclosingScope.putSlot("MyClass", myClass);

    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(REPORTER, enclosingScope);

    // Then
    assertEquals(propertyType.getType(), resolvedType);
  }

  @org.junit.Test
  public void testNamedType_ResolveViaProperties_Cycle() {
    // Given
    String reference = "MyEnum.MY_ELEMENT";

    JSType myEnum = new EnumType(TYPE_REGISTERY, "MyEnum", SOURCE_NAME, 0, 0);
    PropertyType myElementPropertyType = new PropertyType(TYPE_REGISTERY, "MY_ELEMENT", JSTypeNative.ANY);
    myEnum.defineProperty("MY_ELEMENT", myElementPropertyType, false, null);
    PropertyType myEnumElementPropertyType = new PropertyType(TYPE_REGISTERY, "MyEnum", JSTypeNative.ANY);
    myElementPropertyType.setPrototype(myEnumElementPropertyType);
    myEnumElementPropertyType.setPrototype(myEnumPropertyType);

    StaticScope<JSType> enclosingScope = new StaticScope<>();
    enclosingScope.putSlot("MyEnum", myEnum);

    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When & Then
    assertThrows(IllegalStateException.class, () -> namedType.resolveInternal(REPORTER, enclosingScope));
  }

  @org.junit.Test
  public void testNamedType_UnresolvableType() {
    // Given
    String reference = "MyUnknownType";

    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(REPORTER, null);

    // Then
    assertNotNull(resolvedType);
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_UnresolvableType_WithValidator() {
    // Given
    String reference = "MyUnknownType";

    Predicate<JSType> validator = jstype -> jstype.isFunctionType();
    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);
    namedType.setValidator(validator);

    // When
    JSType resolvedType = namedType.resolveInternal(REPORTER, null);

    // Then
    assertNotNull(resolvedType);
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, resolvedType);
    assertThrows(IllegalArgumentException.class, () -> validator.apply(resolvedType));
  }

  @org.junit.Test
  public void testNamedType_ForwardDeclaredType() {
    // Given
    String reference = "MyForwardDeclaredType";

    TYPE_REGISTERY.setForwardDeclaredType(reference);

    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);

    // When
    JSType resolvedType = namedType.resolveInternal(REPORTER, null);

    // Then
    assertEquals(JSTypeNative.FORWARD_DECLARED_TYPE, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_ForwardDeclaredType_WithValidator() {
    // Given
    String reference = "MyForwardDeclaredType";

    TYPE_REGISTERY.setForwardDeclaredType(reference);

    Predicate<JSType> validator = jstype -> jstype.isFunctionType();
    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);
    namedType.setValidator(validator);

    // When
    JSType resolvedType = namedType.resolveInternal(REPORTER, null);

    // Then
    assertEquals(JSTypeNative.FORWARD_DECLARED_TYPE, resolvedType);
    assertThrows(IllegalArgumentException.class, () -> validator.apply(resolvedType));
  }

  @org.junit.Test
  public void testNamedType_SetValidator() {
    // Given
    String reference = "MyType";
    JSType referencedType = new ObjectType(TYPE_REGISTERY, "MyType", SOURCE_NAME, 0, 0);
    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);
    Predicate<JSType> validator = jstype -> jstype.isFunctionType();

    // When
    namedType.setValidator(validator);

    // Then
    JSType resolvedType = namedType.resolveInternal(REPORTER, null);
    assertThrows(IllegalArgumentException.class, () -> validator.apply(resolvedType));

    // Given (reset)
    namedType.setValidator(null);
    resolvedType = namedType.resolveInternal(REPORTER, null);
    assertEquals(referencedType, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_SetValidator_WithInferred() {
    // Given
    String reference = "MyType";
    JSType referencedType = new ObjectType(TYPE_REGISTERY, "MyType", SOURCE_NAME, 0, 0);
    NamedType namedType = new NamedType(TYPE_REGISTERY, reference, SOURCE_NAME, 1, 1);
    PropertyType propertyType = new PropertyType(TYPE_REGISTERY, "property", JSTypeNative.STRING);

    // When
    namedType.defineProperty("property", propertyType, true, null);
    namedType.setValidator(jstype -> jstype.isFunctionType());

    // Then
    JSType resolvedType = namedType.resolveInternal(REPORTER, null);
    assertThrows(IllegalArgumentException.class, () -> validator.apply(resolvedType));

    // Given (reset)
    namedType.setValidator(null);
    resolvedType = namedType.resolveInternal(REPORTER, null);
    assertEquals(referencedType, resolvedType);
  }
}