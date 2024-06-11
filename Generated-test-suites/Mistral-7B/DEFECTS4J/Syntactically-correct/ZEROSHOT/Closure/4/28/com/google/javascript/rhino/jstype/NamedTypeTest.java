import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.errors.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyType;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.TypeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    String reference = "String";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    JSType stringType = TypeUtils.getBuiltinType("String");
    Mockito.when(registry.getType(reference)).thenReturn(stringType);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(stringType, resolvedType);
  }

  @Test
  public void testResolveViaProperties() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    ObjectType fooType = createMockObjectType("Foo");
    StaticSlot<JSType> fooSlot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosingScope.getSlot("Foo")).thenReturn(fooSlot);
    Mockito.when(fooSlot.getType()).thenReturn(fooType);

    ObjectType barType = createMockObjectType("Bar");
    PropertyType barProperty = createMockPropertyType("Baz");
    Mockito.when(fooType.getPropertyType("Bar")).thenReturn(barType);
    Mockito.when(barType.getPropertyType("Baz")).thenReturn(barProperty);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(barProperty, resolvedType);
  }

  @Test
  public void testResolveViaPropertiesWithNullType() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    ObjectType fooType = createMockObjectType("Foo");
    StaticSlot<JSType> fooSlot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosingScope.getSlot("Foo")).thenReturn(fooSlot);
    Mockito.when(fooSlot.getType()).thenReturn(null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(JSTypeNative.NO_TYPE, resolvedType);
  }

  @Test
  public void testResolveViaPropertiesWithNoObjectType() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    ObjectType fooType = createMockObjectType("Foo");
    StaticSlot<JSType> fooSlot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosingScope.getSlot("Foo")).thenReturn(fooSlot);
    Mockito.when(fooSlot.getType()).thenReturn(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE));

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(registry.getNativeFunctionType(JSTypeNative.NO_OBJECT_TYPE).getInstanceType(), resolvedType);
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    ObjectType fooType = createMockObjectType("Foo");
    StaticSlot<JSType> fooSlot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosingScope.getSlot("Foo")).thenReturn(fooSlot);
    Mockito.when(fooSlot.getType()).thenReturn(fooType);

    EnumType fooEnum = Mockito.mock(EnumType.class);
    Mockito.when(fooType.getElementsType()).thenReturn(fooEnum);

    ObjectType barType = createMockObjectType("Bar");
    PropertyType barProperty = createMockPropertyType("Baz");
    Mockito.when(fooEnum.getElementsType()).thenReturn(Lists.newArrayList(barProperty));

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(barProperty, resolvedType);
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    ObjectType fooType = createMockObjectType("Foo");
    StaticSlot<JSType> fooSlot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosingScope.getSlot("Foo")).thenReturn(fooSlot);
    Mockito.when(fooSlot.getType()).thenReturn(fooType);

    ObjectType barType = createMockObjectType("Bar");
    FunctionType barFunctionType = createMockFunctionType();
    Mockito.when(fooType.getPropertyType("Bar")).thenReturn(barType);
    Mockito.when(barType.getInstanceType()).thenReturn(barFunctionType);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(barFunctionType, resolvedType);
  }

  @Test
  public void testResolveViaPropertiesWithInterfaceType() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    ObjectType fooType = createMockObjectType("Foo");
    StaticSlot<JSType> fooSlot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosingScope.getSlot("Foo")).thenReturn(fooSlot);
    Mockito.when(fooSlot.getType()).thenReturn(fooType);

    ObjectType barType = createMockObjectType("Bar");
    InterfaceType barInterfaceType = createMockInterfaceType();
    Mockito.when(fooType.getPropertyType("Bar")).thenReturn(barType);
    Mockito.when(barType.getInterfaces()).thenReturn(Lists.newArrayList(barInterfaceType));

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(barInterfaceType, resolvedType);
  }

  @Test
  public void testResolveViaPropertiesWithUnknownType() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    ObjectType fooType = createMockObjectType("Foo");
    StaticSlot<JSType> fooSlot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosingScope.getSlot("Foo")).thenReturn(fooSlot);
    Mockito.when(fooSlot.getType()).thenReturn(null);

    ObjectType barType = createMockObjectType("Bar");
    PropertyType barProperty = createMockPropertyType("Baz");
    Mockito.when(fooType.getPropertyType("Bar")).thenReturn(barType);
    Mockito.when(barType.getPropertyType("Baz")).thenReturn(barProperty);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(JSTypeNative.NO_RESOLVED_TYPE, resolvedType);
  }

  @Test
  public void testSetValidator() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    ObjectType fooType = createMockObjectType("Foo");
    StaticSlot<JSType> fooSlot = Mockito.mock(StaticSlot.class);
    Mockito.when(enclosingScope.getSlot("Foo")).thenReturn(fooSlot);
    Mockito.when(fooSlot.getType()).thenReturn(fooType);

    ObjectType barType = createMockObjectType("Bar");
    PropertyType barProperty = createMockPropertyType("Baz");
    Mockito.when(fooType.getPropertyType("Bar")).thenReturn(barType);
    Mockito.when(barType.getPropertyType("Baz")).thenReturn(barProperty);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Assert.assertFalse(namedType.isResolved());

    Predicate<JSType> validator = TypeUtils.createJSTypePredicate("String");
    Assert.assertTrue(namedType.setValidator(validator));

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    Assert.assertEquals(registry.getType("String"), resolvedType);
  }

  @Test
  public void testUnresolvedTypeWithIgnoreForwardReferencedTypesTrue() {
    String reference = "Foo";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    Mockito.when(registry.isForwardDeclaredType(reference)).thenReturn(true);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(JSTypeNative.NO_RESOLVED_TYPE, resolvedType);
  }

  @Test
  public void testUnresolvedTypeWithIgnoreForwardReferencedTypesFalse() {
    String reference = "Foo";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 15;

    JSTypeRegistry registry = Mockito.mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = Mockito.mock(StaticScope.class);

    Mockito.when(registry.isForwardDeclaredType(reference)).thenReturn(false);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    Assert.assertEquals(JSTypeNative.NO_RESOLVED_TYPE, resolvedType);
  }

  private ObjectType createMockObjectType(String name) {
    ObjectType objectType = Mockito.mock(ObjectType.class);
    Mockito.when(objectType.getName()).thenReturn(name);
    return objectType;
  }

  private PropertyType createMockPropertyType(String name) {
    PropertyType propertyType = Mockito.mock(PropertyType.class);
    Mockito.when(propertyType.getName()).thenReturn(name);
    return propertyType;
  }

  private FunctionType createMockFunctionType() {
    FunctionType functionType = Mockito.mock(FunctionType.class);
    Mockito.when(functionType.isConstructor()).thenReturn(false);
    Mockito.when(functionType.isInterface()).thenReturn(false);
    return functionType;
  }

  private InterfaceType createMockInterfaceType() {
    InterfaceType interfaceType = Mockito.mock(InterfaceType.class);
    return interfaceType;
  }
}