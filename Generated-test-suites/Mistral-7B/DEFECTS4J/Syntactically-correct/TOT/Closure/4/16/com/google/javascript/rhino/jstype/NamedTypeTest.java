import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.AssertThrows;
import com.google.javascript.rhino.testing.PropertyAssert;
import com.google.javascript.rhino.testing.TypeEqualityTester;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class NamedTypeTest {
  private StaticScope<JSType> enclosingScope;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter() {};
    enclosingScope = new StaticScope<JSType>(null, errorReporter);
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(JSType.STRING, resolvedType);
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Object.prototype.toString";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(
        registry.getNativeObjectType(JSTypeNative.OBJECT), errorReporter);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeFunctionType(JSTypeNative.STRING_TO_STRING_FUNCTION),
        resolvedType);
  }

  @Test
  public void testResolveCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "NamedTypeTest.testResolveCycle";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(
        registry.getNativeObjectType(JSTypeNative.OBJECT), errorReporter);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    NamedType nestedNamedType = new NamedType(registry, "NamedTypeTest", sourceName, lineno, charno);
    NestedNamedType.setReferencedType(namedType);
    NamedType.setReferencedType(nestedNamedType);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        resolvedType);

    errorReporter.assertNoErrors();
  }

  @Test
  public void testResolveCycleWarning() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "NamedTypeTest.testResolveCycle";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(
        registry.getNativeObjectType(JSTypeNative.OBJECT), errorReporter);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    NamedType nestedNamedType = new NamedType(registry, "NamedTypeTest", sourceName, lineno, charno);
    NestedNamedType.setReferencedType(namedType);
    NamedType.setReferencedType(nestedNamedType);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        resolvedType);

    String expectedWarning = "Cycle detected in inheritance chain of type " + reference;
    errorReporter.assertWarningMatches(expectedWarning);
  }

  @Test
  public void testResolveUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "UnresolvedType";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE),
        resolvedType);
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "NamedTypeTest";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    Predicate<JSType> validator = jstype -> jstype instanceof EnumType;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.setValidator(validator);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    boolean isValidatorApplied = validator.apply(resolvedType);
    assertThat(isValidatorApplied).isTrue();
  }

  @Test
  public void testResolveForwardDeclaredType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    registry.setForwardDeclaredType(reference, JSType.NUMBER);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(JSType.NUMBER, resolvedType);
  }

  @Test
  public void testResolveUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "UnknownType";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        resolvedType);
  }

  @Test
  public void testResolveFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Function";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeFunctionType(JSTypeNative.FUNCTION),
        resolvedType);
  }

  @Test
  public void testResolveInterfaceType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Interface";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.INTERFACE),
        resolvedType);
  }

  @Test
  public void testResolveEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyEnum";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    EnumType enumType = new EnumType("MyEnum", JSType.NUMBER, null);
    registry.addType(enumType);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(enumType, resolvedType);
  }

  @Test
  public void testResolveUnknownFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "UnknownFunctionType";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeFunctionType(JSTypeNative.NO_OBJECT_TYPE),
        resolvedType);
  }

  @Test
  public void testDefinePropertyWithType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "NamedTypeTest";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(
        registry.getNativeObjectType(JSTypeNative.OBJECT), errorReporter);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    PropertyAssert.assertPropertyDoesNotExist(namedType, "testProperty");
    JSType propertyType = JSType.STRING;
    namedType.defineProperty("testProperty", propertyType, false, null);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.OBJECT).cast(
            new ObjectType(
                registry, "NamedTypeTest", null, null, null, null, null,
                Lists.newArrayList(), null, null,
                new PropertyType("testProperty", propertyType, null))),
        resolvedType);
  }

  @Test
  public void testDefinePropertyWithInferredType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "NamedTypeTest";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(
        registry.getNativeObjectType(JSTypeNative.OBJECT), errorReporter);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    PropertyAssert.assertPropertyDoesNotExist(namedType, "testProperty");
    Node testPropertyNode = Node.newIdent("testProperty");
    namedType.defineProperty("testProperty", null, true, testPropertyNode);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.OBJECT).cast(
            new ObjectType(
                registry, "NamedTypeTest", null, null, null, null, null,
                Lists.newArrayList(), null, null,
                new PropertyType("testProperty", JSType.UNKNOWN, null))),
        resolvedType);
  }

  @Test
  public void testDefinePropertyAfterResolution() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "NamedTypeTest";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    StaticScope<JSType> enclosingScope = new StaticScope<JSType>(
        registry.getNativeObjectType(JSTypeNative.OBJECT), errorReporter);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    PropertyAssert.assertPropertyDoesNotExist(namedType, "testProperty");
    namedType.defineProperty("testProperty", JSType.STRING, false, null);
    JSType resolvedTypeAfterPropertyDefinition = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.OBJECT).cast(
            new ObjectType(
                registry, "NamedTypeTest", null, null, null, null, null,
                Lists.newArrayList(), null, null,
                new PropertyType("testProperty", JSType.STRING, null))),
        resolvedTypeAfterPropertyDefinition);
  }

  @Test
  public void testResolveForwardDeclaredTypeWithValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    Predicate<JSType> validator = jstype -> jstype instanceof EnumType;
    registry.setForwardDeclaredType(reference, JSType.NUMBER);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.setValidator(validator);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    boolean isValidatorApplied = validator.apply(resolvedType);
    assertThat(isValidatorApplied).isTrue();
  }

  @Test
  public void testResolveForwardDeclaredTypeWithoutValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyType";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    registry.setForwardDeclaredType(reference, JSType.NUMBER);
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    TypeEqualityTester.assertTypesEqual(JSType.NUMBER, resolvedType);
  }

  @Test
  public void testInvalidReference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "InvalidReference";
    String sourceName = "NamedTypeTest";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    AssertThrows.assertThrows(
        IllegalArgumentException.class,
        () -> namedType.resolveInternal(errorReporter, enclosingScope));
  }
}