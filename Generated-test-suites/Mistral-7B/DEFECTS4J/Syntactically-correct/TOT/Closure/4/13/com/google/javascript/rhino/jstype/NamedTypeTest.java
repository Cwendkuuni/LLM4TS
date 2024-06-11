package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.PropertyContinuation;
import com.google.javascript.rhino.PropertyType;
import com.google.javascript.rhino.Registry;
import com.google.javascript.rhino.Symbol;
import com.google.javascript.rhino.SymbolTable;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.NamedType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class NamedTypeTest {

  private static final String NAMED_TYPE_REFERENCE = "Foo";
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> enclosingScope;
  private NamedType namedType;

  @Before
  public void setUp() {
    registry = new Registry();
    errorReporter = mock(ErrorReporter.class);
    enclosingScope = mock(StaticScope.class);

    namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, null, 0, 0);
  }

  @Test
  public void testDefinePropertyInUnresolvedNamedType() {
    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(enclosingScope.getSlot(NAMED_TYPE_REFERENCE)).thenReturn(slot);

    PropertyType returnedProperty = namedType.defineProperty("test", null, false, null);

    ArgumentCaptor<PropertyContinuation> continuationArgumentCaptor =
        ArgumentCaptor.forClass(PropertyContinuation.class);
    verify(slot).setPropertyContinuation(continuationArgumentCaptor.capture());

    PropertyContinuation capturedContinuation = continuationArgumentCaptor.getValue();

    JSType expectedType = registry.getNativeObjectType(JSTypeNative.ANY_TYPE);
    assertThat(capturedContinuation.type).isEqualTo(expectedType);
    assertThat(namedType.isResolved()).isFalse();
  }

  @Test
  public void testDefinePropertyInResolvedNamedType() {
    JSType referencedType = mock(JSType.class);
    when(referencedType.isFunctionType()).thenReturn(false);
    when(referencedType.isInterface()).thenReturn(false);
    when(referencedType.isObjectType()).thenReturn(mock(ObjectType.class));
    when(referencedType.isInstanceOf(ObjectType.class)).thenReturn(true);
    when(referencedType.isUnknownType()).thenReturn(false);

    namedType.setReferencedType(referencedType);
    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(enclosingScope.getSlot(NAMED_TYPE_REFERENCE)).thenReturn(slot);

    PropertyType returnedProperty = namedType.defineProperty("test", null, false, null);

    ArgumentCaptor<PropertyType> propertyArgumentCaptor =
        ArgumentCaptor.forClass(PropertyType.class);
    verify(slot).setProperty(propertyArgumentCaptor.capture());

    PropertyType capturedProperty = propertyArgumentCaptor.getValue();
    assertThat(capturedProperty).isNotNull();
    assertThat(namedType.isResolved()).isTrue();
  }

  @Test(expected = TypeError.class)
  public void testDefinePropertyInUnresolvedNamedTypeWithInvalidType() {
    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(enclosingScope.getSlot(NAMED_TYPE_REFERENCE)).thenReturn(slot);

    namedType.defineProperty("test", null, false, null);
  }

  @Test
  public void testResolveViaRegistry() {
    JSType referencedType = mock(JSType.class);
    when(referencedType.isFunctionType()).thenReturn(false);
    when(referencedType.isInterface()).thenReturn(false);
    when(referencedType.isObjectType()).thenReturn(mock(ObjectType.class));
    when(referencedType.isInstanceOf(ObjectType.class)).thenReturn(true);
    when(referencedType.isUnknownType()).thenReturn(false);

    when(registry.getType(NAMED_TYPE_REFERENCE)).thenReturn(referencedType);

    JSType resolvedType = namedType.resolve(errorReporter, enclosingScope);

    assertThat(resolvedType).isEqualTo(referencedType);
  }

  @Test
  public void testResolveViaProperties() {
    ObjectType mockObjectType = mock(ObjectType.class);
    when(mockObjectType.getPropertyType("test")).thenReturn(mock(PropertyType.class));
    when(mockObjectType.getName()).thenReturn("TestObjectType");
    when(mockObjectType.isInstanceOf(ObjectType.class)).thenReturn(true);

    SymbolTable symbolTable = new SymbolTable();
    StaticScope<JSType> mockStaticScope = new StaticScope<>(symbolTable);

    Symbol mockSymbol = new Symbol(NAMED_TYPE_REFERENCE, 0, 0);
    StaticSlot<JSType> mockStaticSlot = new StaticSlot<>(mockSymbol, mockObjectType);
    mockStaticScope.setSlot(mockSymbol, mockStaticSlot);

    JSType resolvedType = namedType.resolve(errorReporter, mockStaticScope);

    PropertyType mockPropertyType = mock(PropertyType.class);
    when(mockPropertyType.getPropertyName()).thenReturn("test");

    ArgumentCaptor<Symbol> symbolArgumentCaptor =
        ArgumentCaptor.forClass(Symbol.class);
    verify(symbolTable).define("%s.test", symbolArgumentCaptor.capture());

    Symbol capturedSymbol = symbolArgumentCaptor.getValue();
    StaticSlot<JSType> capturedStaticSlot = mockStaticSlot;
    verify(capturedStaticSlot).getType();
    verify(mockStaticScope).getSlot(symbolArgumentCaptor.getValue());
    verify(mockObjectType).getPropertyType(symbolArgumentCaptor.getValue().getName());

    ObjectType expectedObjectType = mockObjectType;
    JSType expectedType = expectedObjectType.getInstanceType();

    ArgumentCaptor<JSType> typeArgumentCaptor =
        ArgumentCaptor.forClass(JSType.class);
    verify(namedType).setReferencedAndResolvedType(typeArgumentCaptor.capture());

    JSType capturedType = typeArgumentCaptor.getValue();
    assertThat(capturedType).isEqualTo(expectedType);
  }

  @Test
  public void testResolveViaPropertiesWithCycle() {
    ObjectType mockObjectType = mock(ObjectType.class);
    when(mockObjectType.getPropertyType("test")).thenReturn(mock(PropertyType.class));
    when(mockObjectType.getName()).thenReturn("TestObjectType");
    when(mockObjectType.isInstanceOf(ObjectType.class)).thenReturn(true);

    SymbolTable symbolTable = new SymbolTable();
    StaticScope<JSType> mockStaticScope = new StaticScope<>(symbolTable);

    Symbol mockSymbol = new Symbol(NAMED_TYPE_REFERENCE, 0, 0);
    StaticSlot<JSType> mockStaticSlot = new StaticSlot<>(mockSymbol, mockObjectType);
    mockStaticScope.setSlot(mockSymbol, mockStaticSlot);

    when(mockStaticSlot.getType()).thenReturn(namedType);

    verify(errorReporter).warning("Cycle detected in inheritance chain of type " + NAMED_TYPE_REFERENCE, null, 0, 0);
  }

  @Test
  public void testResolveViaPropertiesWithInvalidType() {
    ObjectType mockObjectType = mock(ObjectType.class);
    when(mockObjectType.getPropertyType("test")).thenReturn(mock(PropertyType.class));
    when(mockObjectType.getName()).thenReturn("TestObjectType");
    when(mockObjectType.isInstanceOf(ObjectType.class)).thenReturn(true);

    SymbolTable symbolTable = new SymbolTable();
    StaticScope<JSType> mockStaticScope = new StaticScope<>(symbolTable);

    Symbol mockSymbol = new Symbol(NAMED_TYPE_REFERENCE, 0, 0);
    StaticSlot<JSType> mockStaticSlot = new StaticSlot<>(mockSymbol, mockObjectType);
    mockStaticScope.setSlot(mockSymbol, mockStaticSlot);

    when(mockStaticSlot.getType()).thenReturn(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE));

    verify(errorReporter).warning("Cycle detected in inheritance chain of type " + NAMED_TYPE_REFERENCE, null, 0, 0);
  }

  @Test
  public void testResolveViaPropertiesWithUnknownType() {
    ObjectType mockObjectType = mock(ObjectType.class);
    when(mockObjectType.getPropertyType("test")).thenReturn(mock(PropertyType.class));
    when(mockObjectType.getName()).thenReturn("TestObjectType");
    when(mockObjectType.isInstanceOf(ObjectType.class)).thenReturn(true);

    SymbolTable symbolTable = new SymbolTable();
    StaticScope<JSType> mockStaticScope = new StaticScope<>(symbolTable);

    Symbol mockSymbol = new Symbol(NAMED_TYPE_REFERENCE, 0, 0);
    StaticSlot<JSType> mockStaticSlot = new StaticSlot<>(mockSymbol, mockObjectType);
    mockStaticScope.setSlot(mockSymbol, mockStaticSlot);

    when(mockStaticSlot.getType()).thenReturn(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE));

    verify(errorReporter, never()).warning("Cycle detected in inheritance chain of type " + NAMED_TYPE_REFERENCE);
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    ObjectType mockObjectType = mock(ObjectType.class);
    EnumType mockEnumType = mock(EnumType.class);
    when(mockObjectType.getName()).thenReturn("TestObjectType");
    when(mockObjectType.isInstanceOf(ObjectType.class)).thenReturn(true);
    when(mockObjectType.isEnumType()).thenReturn(true);
    when(mockObjectType.getInstanceType()).thenReturn(mockEnumType);

    SymbolTable symbolTable = new SymbolTable();
    StaticScope<JSType> mockStaticScope = new StaticScope<>(symbolTable);

    Symbol mockSymbol = new Symbol(NAMED_TYPE_REFERENCE, 0, 0);
    StaticSlot<JSType> mockStaticSlot = new StaticSlot<>(mockSymbol, mockObjectType);
    mockStaticScope.setSlot(mockSymbol, mockStaticSlot);

    when(mockStaticSlot.getType()).thenReturn(namedType);

    JSType resolvedType = namedType.resolve(errorReporter, mockStaticScope);

    JSType expectedType = mockEnumType;

    ArgumentCaptor<JSType> typeArgumentCaptor =
        ArgumentCaptor.forClass(JSType.class);
    verify(namedType).setReferencedAndResolvedType(typeArgumentCaptor.capture());

    JSType capturedType = typeArgumentCaptor.getValue();
    assertThat(capturedType).isEqualTo(expectedType);
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    FunctionType mockFunctionType = mock(FunctionType.class);
    when(mockFunctionType.getInstanceType()).thenReturn(mock(ObjectType.class));

    ObjectType mockObjectType = mock(ObjectType.class);
    when(mockObjectType.getPropertyType("test")).thenReturn(mock(PropertyType.class));
    when(mockObjectType.getName()).thenReturn("TestObjectType");
    when(mockObjectType.isInstanceOf(ObjectType.class)).thenReturn(true);

    SymbolTable symbolTable = new SymbolTable();
    StaticScope<JSType> mockStaticScope = new StaticScope<>(symbolTable);

    Symbol mockSymbol = new Symbol(NAMED_TYPE_REFERENCE, 0, 0);
    StaticSlot<JSType> mockStaticSlot = new StaticSlot<>(mockSymbol, mockObjectType);
    mockStaticScope.setSlot(mockSymbol, mockStaticSlot);

    when(mockStaticSlot.getType()).thenReturn(namedType);

    when(mockFunctionType.isConstructor()).thenReturn(true);

    JSType resolvedType = namedType.resolve(errorReporter, mockStaticScope);

    JSType expectedType = mockFunctionType.getInstanceType();

    ArgumentCaptor<JSType> typeArgumentCaptor =
        ArgumentCaptor.forClass(JSType.class);
    verify(namedType).setReferencedAndResolvedType(typeArgumentCaptor.capture());

    JSType capturedType = typeArgumentCaptor.getValue();
    assertThat(capturedType).isEqualTo(expectedType);
  }

  @Test
  public void testResolveViaPropertiesWithInterfaceType() {
    ObjectType mockObjectType = mock(ObjectType.class);
    when(mockObjectType.getPropertyType("test")).thenReturn(mock(PropertyType.class));
    when(mockObjectType.getName()).thenReturn("TestObjectType");
    when(mockObjectType.isInstanceOf(ObjectType.class)).thenReturn(true);
    when(mockObjectType.isInterface()).thenReturn(true);

    SymbolTable symbolTable = new SymbolTable();
    StaticScope<JSType> mockStaticScope = new StaticScope<>(symbolTable);

    Symbol mockSymbol = new Symbol(NAMED_TYPE_REFERENCE, 0, 0);
    StaticSlot<JSType> mockStaticSlot = new StaticSlot<>(mockSymbol, mockObjectType);
    mockStaticScope.setSlot(mockSymbol, mockStaticSlot);

    when(mockStaticSlot.getType()).thenReturn(namedType);

    JSType resolvedType = namedType.resolve(errorReporter, mockStaticScope);

    JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    ArgumentCaptor<JSType> typeArgumentCaptor =
        ArgumentCaptor.forClass(JSType.class);
    verify(namedType).setReferencedAndResolvedType(typeArgumentCaptor.capture());

    JSType capturedType = typeArgumentCaptor.getValue();
    assertThat(capturedType).isEqualTo(expectedType);
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = mock(Predicate.class);

    namedType.setValidator(validator);

    ArgumentCaptor<Predicate<JSType>> predicateArgumentCaptor =
        ArgumentCaptor.forClass(Predicate.class);
    verify(namedType).setValidator(predicateArgumentCaptor.capture());

    Predicate<JSType> capturedValidator = predicateArgumentCaptor.getValue();

    JSType referencedType = mock(JSType.class);
    when(referencedType.isFunctionType()).thenReturn(false);
    when(referencedType.isInterface()).thenReturn(false);
    when(referencedType.isObjectType()).thenReturn(mock(ObjectType.class));
    when(referencedType.isInstanceOf(ObjectType.class)).thenReturn(true);
    when(referencedType.isUnknownType()).thenReturn(false);

    capturedValidator.apply(referencedType);
  }
}