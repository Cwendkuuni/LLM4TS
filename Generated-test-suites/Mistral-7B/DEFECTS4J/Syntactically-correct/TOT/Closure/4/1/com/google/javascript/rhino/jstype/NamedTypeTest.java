import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.error.ErrorReporter;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.PropertyType;
import com.google.javascript.rhino.types.StaticScope;
import com.google.javascript.rhino.types.StaticSlot;
import com.google.javascript.rhino.types.function.FunctionType;
import com.google.javascript.rhino.types.object.NamedType;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

  @Test
  public void testNamedTypeResolution() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosingScope = new StaticScope<>(registry);

    registry.putType("String", String.class);
    registry.putType("Number", Number.class);

    // Test NamedType resolution via registry
    NamedType namedTypeString = new NamedType(registry, "String", "StringTest.java", 1, 1);
    assertEquals("String", namedTypeString.toString());
    assertEquals(JSTypeNative.STRING, namedTypeString.getReferencedType().getTypeID());

    // Test NamedType resolution via properties
    ObjectType stringObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    stringObjectType.defineProperty("String", JSTypeNative.STRING, false, null);

    NamedType namedTypeNumber = new NamedType(registry, "Number", "NumberTest.java", 1, 1);
    PropertyType numberProperty = stringObjectType.getPropertyType("Number");
    assertEquals("Number", namedTypeNumber.toString());
    assertEquals(JSTypeNative.NUMBER, namedTypeNumber.getReferencedType().getTypeID());

    // Test NamedType resolution with unresolved type
    NamedType unresolvedType = new NamedType(registry, "UnresolvedType", "UnresolvedTypeTest.java", 1, 1);
    assertNull(unresolvedType.getReferencedType());

    // Test NamedType resolution with cycle
    ObjectType cycleType = new ObjectType(registry, JSTypeNative.UNKNOWN_TYPE, "CycleType");
    cycleType.defineProperty("Cycle", cycleType, false, null);

    StaticSlot<JSType> cycleSlot = enclosingScope.putSlot("CycleType", cycleType);

    NamedType cycleNamedType = new NamedType(registry, "Cycle", "CycleTest.java", 1, 1);
    assertEquals("Cycle", cycleNamedType.toString());
    assertEquals(cycleSlot.getType(), cycleNamedType.getReferencedType());

    // Test NamedType resolution with forward-declared type
    registry.putForwardDeclaration("Foo", String.class);
    NamedType forwardDeclaredType = new NamedType(registry, "Foo", "FooTest.java", 1, 1);
    assertNotNull(forwardDeclaredType.getReferencedType());
    assertEquals(String.class, forwardDeclaredType.getReferencedType().getJavaType());

    // Test NamedType as a property type
    FunctionType functionType = registry.getNativeFunctionType(JSTypeNative.FUNCTION_TYPE);
    ObjectType functionTypeObject = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    functionTypeObject.defineProperty("func", functionType, false, null);

    PropertyType funcProperty = functionTypeObject.getPropertyType("func");
    assertEquals(FunctionType.class, funcProperty.getType().getClass());
    NamedType funcNamedType = new NamedType(registry, "func", "FuncTest.java", 1, 1);
    assertEquals("func", funcNamedType.toString());
    assertEquals(FunctionType.class, funcNamedType.getReferencedType().getClass());
  }

  @Test
  public void testNamedTypePropertyContinuations() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosingScope = new StaticScope<>(registry);

    registry.putType("String", String.class);

    ObjectType stringObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    stringObjectType.defineProperty("prop", JSTypeNative.STRING, false, null);

    NamedType namedType = new NamedType(registry, "String", "StringTest.java", 1, 1);
    namedType.defineProperty("prop", JSTypeNative.STRING, false, null);
    namedType.finishPropertyContinuations();

    ObjectType resolvedNamedType = (ObjectType) namedType.getReferencedType();
    assertEquals("String", resolvedNamedType.getName());
    assertEquals(1, resolvedNamedType.getPropertyCount());
    assertEquals("prop", resolvedNamedType.getPropertyNames()[0]);
    assertEquals(JSTypeNative.STRING, resolvedNamedType.getPropertyType("prop").getTypeID());
  }

  @Test
  public void testNamedTypeImplicitPrototypeCycle() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test NamedType implicit prototype cycle with unresolved types
    ObjectType cycleType1 = new ObjectType(registry, JSTypeNative.UNKNOWN_TYPE, "CycleType1");
    cycleType1.defineProperty("cycle", cycleType1, false, null);
    registry.putForwardDeclaration("CycleType1", cycleType1);

    ObjectType cycleType2 = new ObjectType(registry, JSTypeNative.UNKNOWN_TYPE, "CycleType2");
    cycleType2.defineProperty("cycle", cycleType2, false, null);
    registry.putForwardDeclaration("CycleType2", cycleType2);

    NamedType namedType1 = new NamedType(registry, "CycleType1", "CycleTest1.java", 1, 1);
    NamedType namedType2 = new NamedType(registry, "CycleType2", "CycleTest2.java", 1, 1);

    // Test NamedType implicit prototype cycle with resolved types
    ObjectType cycleType1Resolved = new ObjectType(registry, JSTypeNative.OBJECT_TYPE, "CycleType1Resolved");
    cycleType1Resolved.defineProperty("cycle", cycleType1Resolved, false, null);

    ObjectType cycleType2Resolved = new ObjectType(registry, JSTypeNative.OBJECT_TYPE, "CycleType2Resolved");
    cycleType2Resolved.defineProperty("cycle", cycleType2Resolved, false, null);

    StaticScope<JSType> enclosingScope = new StaticScope<>(registry);
    StaticSlot<JSType> cycleType1Slot = enclosingScope.putSlot("CycleType1", cycleType1Resolved);
    StaticSlot<JSType> cycleType2Slot = enclosingScope.putSlot("CycleType2", cycleType2Resolved);

    NamedType namedType1Resolved = new NamedType(registry, "CycleType1", "CycleTest1Resolved.java", 1, 1);
    NamedType namedType2Resolved = new NamedType(registry, "CycleType2", "CycleTest2Resolved.java", 1, 1);

    namedType1Resolved.resolveInternal(t, enclosingScope);
    namedType2Resolved.resolveInternal(t, enclosingScope);

    // Test NamedType implicit prototype cycle with unresolved types should result in an error
    namedType1.resolveInternal(t, null);
    namedType2.resolveInternal(t, null);

    assertTrue("Expected a type cycle warning", t.getWarnings().size() > 0);
  }

  @Test
  public void testNamedTypeEnumElementCycle() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    ObjectType enumerationType = new ObjectType(registry, JSTypeNative.OBJECT_TYPE, "Enumeration");
    enumerationType.defineProperty("First", JSTypeNative.STRING, false, null);
    enumerationType.defineProperty("Second", JSTypeNative.STRING, false, null);

    StaticScope<JSType> enclosingScope = new StaticScope<>(registry);
    StaticSlot<JSType> enumerationSlot = enclosingScope.putSlot("Enumeration", enumerationType);

    PropertyType firstProperty = enumerationType.getPropertyType("First");
    assertEquals(JSTypeNative.STRING, firstProperty.getTypeID());

    PropertyType secondProperty = enumerationType.getPropertyType("Second");
    assertEquals(JSTypeNative.STRING, secondProperty.getTypeID());

    NamedType namedTypeFirst = new NamedType(registry, "First", "EnumerationTestFirst.java", 1, 1);
    NamedType namedTypeSecond = new NamedType(registry, "Second", "EnumerationTestSecond.java", 1, 1);

    namedTypeFirst.resolveInternal(t, enclosingScope);
    namedTypeSecond.resolveInternal(t, enclosingScope);

    // Test NamedType enum element cycle should not result in an error
    assertEquals(firstProperty.getReferencedType(), namedTypeFirst.getReferencedType());
    assertEquals(secondProperty.getReferencedType(), namedTypeSecond.getReferencedType());
    assertEquals(enumerationType, namedTypeFirst.getReferencedType().getReferencedType());
    assertEquals(enumerationType, namedTypeSecond.getReferencedType().getReferencedType());
  }
}