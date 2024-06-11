import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.AssertThrows;
import com.google.javascript.rhino.testing.StaticScopeTest;
import com.google.javascript.rhino.testing.TypeEquivalence;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class NamedTypeTest extends StaticScopeTest {

  private static final String REFERENCE = "MyNamedType";
  private static final int LINE_NUMBER = 1;
  private static final int CHAR_NUMBER = 1;

  @Override
  protected void init() {
    super.init();

    // Initialize the registry with some types for testing.
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setGlobalObject(getGlobalObject());

    registry.define("MyBaseType", FunctionType.create(new ObjectType(registry, "BaseType"), null));
    registry.define("MyFunctionType", FunctionType.create(new ObjectType(registry, "FunctionType"), null));
    registry.define("MyEnumType", EnumType.create(registry, "MyEnumType", null, Lists.newArrayList("MyEnumElement1", "MyEnumElement2")));
    registry.define("MyEnumElement1", EnumElementType.create(registry, "MyEnumElement1", new ObjectType(registry, "NumberType")));

    registry.define(REFERENCE, new NamedType(registry, REFERENCE, "MyPackage", LINE_NUMBER, CHAR_NUMBER));
  }

  @org.junit.Test
  public void testBasicFunctionality() {
    NamedType namedType = (NamedType) lookupVariable(REFERENCE);

    // Test toString method
    assertThat(namedType.toString()).isEqualTo(REFERENCE);

    // Test getReferenceName method
    assertThat(namedType.getReferenceName()).isEqualTo(REFERENCE);

    // Test isNamedType method
    assertThat(namedType.isNamedType()).isTrue();

    // Test isNominalType method
    assertThat(namedType.isNominalType()).isTrue();

    // Test isResolved method
    assertThat(namedType.isResolved()).isFalse();

    // Test defineProperty method when unresolved
    assertThrows(UnsupportedOperationException.class,
        () -> namedType.defineProperty("testProperty", new NumberType(registry), false, null));

    // Test resolve method
    namedType.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));

    // Test defineProperty method when resolved
    namedType.defineProperty("testProperty", new NumberType(registry), false, null);
    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isNotNull();
    ObjectType resolvedObjectType = ObjectType.cast(resolvedType);
    assertThat(resolvedObjectType).isNotNull();
    assertThat(resolvedObjectType.hasProperty("testProperty")).isTrue();
  }

  @org.junit.Test
  public void testResolveViaRegistry() {
    // Test resolveViaRegistry with a resolved type.
    JSType resolvedType1 = registry.getType("MyBaseType");
    NamedType namedType1 = new NamedType(registry, "MyBaseType", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    namedType1.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));
    assertThat(namedType1.getReferencedType()).isSameInstanceAs(resolvedType1);

    // Test resolveViaRegistry with an unresolved type.
    assertThrows(RuntimeException.class, () -> {
      JSType resolvedType2 = registry.getType("UnknownType");
      NamedType namedType2 = new NamedType(registry, "UnknownType", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
      namedType2.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));
      namedType2.getReferencedType();
    });
  }

  @org.junit.Test
  public void testResolveViaProperties() {
    // Test resolveViaProperties with a resolved type.
    ObjectType baseType = (ObjectType) lookupVariable("MyBaseType");
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "MyProperty", new NumberType(registry), false, null);
    baseType.defineProperty("MyProperty", new NumberType(registry), false, null);
    NamedType namedType = new NamedType(registry, "MyBaseType.MyProperty", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    namedType.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));
    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isSameInstanceAs(propertyContinuation.type);

    // Test resolveViaProperties with an unresolved type.
    StaticScope<JSType> emptyScope = new StaticScope<JSType>(Lists.newArrayList());
    PropertyContinuation propertyContinuation2 = new PropertyContinuation(
        "MyUnknownProperty", new NumberType(registry), false, null);
    ObjectType baseType2 = (ObjectType) lookupVariable("MyBaseType");
    baseType2.defineProperty("MyUnknownProperty", new NumberType(registry), false, null);
    NamedType namedType2 = new NamedType(registry, "MyBaseType.MyUnknownProperty", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    namedType2.resolve(getErrorReporter(), emptyScope);
    JSType resolvedType2 = namedType2.getReferencedType();
    assertThat(resolvedType2).isNull();
  }

  @org.junit.Test
  public void testResolveWithCycle() {
    ObjectType cycleType = new ObjectType(registry, "CycleType");
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "CycleProperty", new NumberType(registry), false, null);
    cycleType.defineProperty("CycleProperty", new NamedType(registry, "CycleType", "MyPackage", LINE_NUMBER, CHAR_NUMBER), false, null);
    NamedType namedType = new NamedType(registry, "CycleType.CycleProperty", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    namedType.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));
    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isSameInstanceAs(namedType);
    // This test case is expected to trigger a warning because of the cycle.
  }

  @org.junit.Test
  public void testResolveWithForwardDeclaredType() {
    JSType forwardDeclaredType = registry.getType("MyEnumType");
    NamedType namedType = new NamedType(registry, "MyEnumType", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    namedType.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));
    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isSameInstanceAs(forwardDeclaredType);
  }

  @org.junit.Test
  public void testResolveWithUnresolvedType() {
    NamedType namedType = new NamedType(registry, "UnknownType", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    namedType.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));
    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isNull();
  }

  @org.junit.Test
  public void testResolveWithInvalidType() {
    NamedType namedType = new NamedType(registry, "MyFunctionType", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    namedType.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));
    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isInstanceOf(FunctionType.class);
  }

  @org.junit.Test
  public void testResolveWithEnumElementType() {
    EnumType enumType = (EnumType) lookupVariable("MyEnumType");
    EnumElementType enumElementType = enumType.getEnumElements().get(0);
    NamedType namedType = new NamedType(registry, enumElementType.getName(), "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    namedType.resolve(getErrorReporter(), new StaticScope<JSType>(this.declaredVariables));
    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isInstanceOf(NumberType.class);
  }

  @org.junit.Test
  public void testSetValidator() {
    NamedType namedType = new NamedType(registry, "MyNamedType", "MyPackage", LINE_NUMBER, CHAR_NUMBER);
    TypeEquivalence validator = new TypeEquivalence();
    namedType.setValidator(validator);
    JSType resolvedType = namedType.getReferencedType();
    validator.apply(resolvedType);
  }
}