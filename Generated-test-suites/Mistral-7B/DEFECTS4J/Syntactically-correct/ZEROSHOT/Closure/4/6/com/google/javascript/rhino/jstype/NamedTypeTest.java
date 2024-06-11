import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.error.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

public class NamedTypeTest {

  private static final ErrorReporter REPORTER = new ErrorReporter();

  @org.junit.Test
  public void testNamedTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    NamedType namedType = new NamedType(registry, "MyType", "script.js", 1, 1);
    assertEquals("MyType", namedType.getReferenceName());
    assertEquals("MyType", namedType.toString());
    assertNull(namedType.getReferencedType());
    assertFalse(namedType.isResolved());

    JSType referencedType = new ObjectType(registry, "MyEnum", "MyEnum.js", 1, 1);
    NamedType namedTypeWithReferencedType = new NamedType(registry, "MyEnum", "script.js", 1, 1);
    namedTypeWithReferencedType.setReferencedType(referencedType);
    assertEquals(referencedType, namedTypeWithReferencedType.getReferencedType());
    assertTrue(namedTypeWithReferencedType.isResolved());
  }

  @org.junit.Test
  public void testNamedTypeResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("MyNamedType", new ObjectType(registry, "MyNamedType"));

    NamedType namedType = new NamedType(registry, "MyNamedType", "script.js", 1, 1);
    assertEquals(registry.getType("MyNamedType"), namedType.getReferencedType());
    assertTrue(namedType.isResolved());
  }

  @org.junit.Test
  public void testNamedTypeResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType parent = new ObjectType(registry, "Parent");
    ObjectType child = new ObjectType(registry, "Child", parent);

    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> parentSlot = scope.addSlot("Parent", parent);

    NamedType namedType = new NamedType(registry, "Child.Property", "script.js", 1, 1);
    namedType.resolveInternal(REPORTER, scope);
    ObjectType childObjectType = (ObjectType) namedType.getReferencedType();
    assertEquals(child, childObjectType.getParentType());
    assertEquals("Parent.Child", namedType.getReferenceName());
    assertTrue(namedType.isResolved());
  }

  @org.junit.Test
  public void testNamedTypeResolveViaPropertiesNotFound() {
    JSTypeRegistry registry = new JSTypeRegistry();

    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> parentSlot = scope.addSlot("Parent", new ObjectType(registry, "Parent"));

    NamedType namedType = new NamedType(registry, "Child.NotFound", "script.js", 1, 1);
    namedType.resolveInternal(REPORTER, scope);
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
    assertFalse(namedType.isResolved());
  }

  @org.junit.Test
  public void testNamedTypeResolveViaRegistryCircleDetected() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType("MyType", new NamedType(registry, "MyType", "MyType.js", 1, 1));

    NamedType namedType = new NamedType(registry, "MyType", "script.js", 1, 1);
    namedType.resolveInternal(REPORTER, new StaticScope<>(registry));
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    assertFalse(namedType.isResolved());
  }

  @org.junit.Test
  public void testNamedTypeResolveViaPropertiesCircleDetected() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType parent = new ObjectType(registry, "Parent");
    ObjectType child = new ObjectType(registry, "Child", parent);
    ObjectType grandChild = new ObjectType(registry, "GrandChild", child);

    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> parentSlot = scope.addSlot("Parent", parent);
    StaticSlot<JSType> childSlot = scope.addSlot("Child", child);

    NamedType namedType = new NamedType(registry, "GrandChild.Child", "script.js", 1, 1);
    namedType.resolveInternal(REPORTER, scope);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    assertFalse(namedType.isResolved());
  }

  @org.junit.Test
  public void testNamedTypeResolveViaPropertiesEnumElementCycleDetected() {
    JSTypeRegistry registry = new JSTypeRegistry();
    EnumType enumType = new EnumType(registry, "MyEnum", "MyEnum.js", 1, 1);
    EnumElementType enumElement = new EnumElementType(registry, "MyEnum", "MyEnumElement", enumType);

    StaticScope<JSType> scope = new StaticScope<>(registry);
    StaticSlot<JSType> enumSlot = scope.addSlot("MyEnum", enumType);

    NamedType namedType = new NamedType(registry, "MyEnumElement", "script.js", 1, 1);
    namedType.resolveInternal(REPORTER, scope);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    assertFalse(namedType.isResolved());
  }
}