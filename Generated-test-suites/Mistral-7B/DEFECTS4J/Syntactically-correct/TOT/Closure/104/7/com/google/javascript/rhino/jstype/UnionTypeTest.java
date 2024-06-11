import static com.google.javascript.rhino.testing.AssertThrows.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.testing.AssertType;

import java.util.Set;

public class UnionTypeTest {

  @org.junit.Test
  public void testUnionTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringBooleanAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.BOOLEAN)
    );

    UnionType stringBooleanUnion = new UnionType(registry, stringBooleanAlternates);

    assertEquals("(String|Boolean)", stringBooleanUnion.toString());
  }

  @org.junit.Test
  public void testUnionTypeMatchesNumberContext() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> intBooleanAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.INT),
        registry.getJSType(JSTypeNative.BOOLEAN)
    );

    UnionType intBooleanUnion = new UnionType(registry, intBooleanAlternates);

    assertTrue(intBooleanUnion.matchesNumberContext());

    // Test inverse case
    assertFalse(registry.getJSType(JSTypeNative.STRING).matchesNumberContext());
  }

  @org.junit.Test
  public void testUnionTypeMatchesStringContext() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringBooleanAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.BOOLEAN)
    );

    UnionType stringBooleanUnion = new UnionType(registry, stringBooleanAlternates);

    assertTrue(stringBooleanUnion.matchesStringContext());

    // Test inverse case
    assertFalse(registry.getJSType(JSTypeNative.INT).matchesStringContext());
  }

  @org.junit.Test
  public void testUnionTypeMatchesObjectContext() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> objectNullAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.OBJECT),
        registry.getJSType(JSTypeNative.NULL)
    );

    UnionType objectNullUnion = new UnionType(registry, objectNullAlternates);

    assertTrue(objectNullUnion.matchesObjectContext());

    // Test inverse case
    assertFalse(registry.getJSType(JSTypeNative.INT).matchesObjectContext());
  }

  @org.junit.Test
  public void testUnionTypeFindPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnion = new UnionType(registry, stringNumberAlternates);

    JSType stringPropertyType = registry.getJSType(JSTypeNative.STRING);
    JSType numberPropertyType = registry.getJSType(JSTypeNative.NUMBER);

    JSType stringProperty = stringNumberUnion.findPropertyType("length");
    assertEquals(stringPropertyType, stringProperty);

    JSType numberProperty = stringNumberUnion.findPropertyType("toFixed");
    assertEquals(numberPropertyType, numberProperty);
  }

  @org.junit.Test
  public void testUnionTypeCanAssignTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnion = new UnionType(registry, stringNumberAlternates);

    JSType stringType = registry.getJSType(JSTypeNative.STRING);
    JSType numberType = registry.getJSType(JSTypeNative.NUMBER);

    assertTrue(stringNumberUnion.canAssignTo(stringType));
    assertTrue(stringNumberUnion.canAssignTo(numberType));

    // Test inverse case
    assertFalse(stringType.canAssignTo(numberType));
  }

  @org.junit.Test
  public void testUnionTypeCanBeCalled() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> functionStringBooleanAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.FUNCTION),
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.BOOLEAN)
    );

    UnionType functionStringBooleanUnion = new UnionType(registry, functionStringBooleanAlternates);

    assertTrue(functionStringBooleanUnion.canBeCalled());
  }

  @org.junit.Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> nullNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.NULL),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType nullNumberUnion = new UnionType(registry, nullNumberAlternates);

    JSType restrictedType = nullNumberUnion.restrictByNotNullOrUndefined();

    assertNotNull(restrictedType);
    assertEquals(registry.getJSType(JSTypeNative.NUMBER), restrictedType);
  }

  @org.junit.Test
  public void testUnionTypeTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnionA = new UnionType(registry, stringNumberAlternates);
    UnionType stringNumberUnionB = new UnionType(registry, stringNumberAlternates);

    TernaryValue result = stringNumberUnionA.testForEquality(stringNumberUnionB);

    assertEquals(TernaryValue.TRUE, result);
  }

  @org.junit.Test
  public void testUnionTypeIsNullable() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> nullNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.NULL),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType nullNumberUnion = new UnionType(registry, nullNumberAlternates);

    assertTrue(nullNumberUnion.isNullable());
  }

  @org.junit.Test
  public void testUnionTypeIsUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> unknownNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.UNKNOWN),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType unknownNumberUnion = new UnionType(registry, unknownNumberAlternates);

    assertTrue(unknownNumberUnion.isUnknownType());
  }

  @org.junit.Test
  public void testUnionTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> numberStringAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.NUMBER),
        registry.getJSType(JSTypeNative.STRING)
    );

    UnionType numberStringUnion = new UnionType(registry, numberStringAlternates);

    JSType leastSupertype = numberStringUnion.getLeastSupertype(registry.getJSType(JSTypeNative.OBJECT));

    assertEquals(registry.getJSType(JSTypeNative.OBJECT), leastSupertype);
  }

  @org.junit.Test
  public void testUnionTypeGetLeastSupertypeWithSameType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> numberNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.NUMBER),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType numberNumberUnion = new UnionType(registry, numberNumberAlternates);

    JSType leastSupertype = numberNumberUnion.getLeastSupertype(registry.getJSType(JSTypeNative.NUMBER));

    assertEquals(registry.getJSType(JSTypeNative.NUMBER), leastSupertype);
  }

  @org.junit.Test
  public void testUnionTypeMeet() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternatesA = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    Set<JSType> booleanNumberAlternatesB = ImmutableSet.of(
        registry.getJSType(JSTypeNative.BOOLEAN),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnionA = new UnionType(registry, stringNumberAlternatesA);
    UnionType booleanNumberUnionB = new UnionType(registry, booleanNumberAlternatesB);

    UnionType result = stringNumberUnionA.meet(booleanNumberUnionB);

    assertEquals(registry.getJSType(JSTypeNative.NUMBER), result);
  }

  @org.junit.Test
  public void testUnionTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternatesA = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    Set<JSType> stringNumberAlternatesB = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnionA = new UnionType(registry, stringNumberAlternatesA);
    UnionType stringNumberUnionB = new UnionType(registry, stringNumberAlternatesB);

    assertTrue(stringNumberUnionA.equals(stringNumberUnionB));
  }

  @org.junit.Test
  public void testUnionTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnion = new UnionType(registry, stringNumberAlternates);

    UnionType stringNumberUnionCopy = new UnionType(registry, stringNumberAlternates);

    assertEquals(stringNumberUnion.hashCode(), stringNumberUnionCopy.hashCode());
  }

  @org.junit.Test
  public void testUnionTypeIsUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnion = new UnionType(registry, stringNumberAlternates);

    assertTrue(stringNumberUnion.isUnionType());
  }

  @org.junit.Test
  public void testUnionTypeIsObject() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> objectNullAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.OBJECT),
        registry.getJSType(JSTypeNative.NULL)
    );

    UnionType objectNullUnion = new UnionType(registry, objectNullAlternates);

    assertTrue(objectNullUnion.isObject());
  }

  @org.junit.Test
  public void testUnionTypeContains() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnion = new UnionType(registry, stringNumberAlternates);

    assertTrue(stringNumberUnion.contains(registry.getJSType(JSTypeNative.STRING)));
    assertTrue(stringNumberUnion.contains(registry.getJSType(JSTypeNative.NUMBER)));

    assertFalse(registry.getJSType(JSTypeNative.OBJECT).isAssignableFrom(stringNumberUnion));
  }

  @org.junit.Test
  public void testUnionTypeGetRestrictedUnion() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringNumberAlternates = ImmutableSet.of(
        registry.getJSType(JSTypeNative.STRING),
        registry.getJSType(JSTypeNative.NUMBER)
    );

    UnionType stringNumberUnion = new UnionType(registry, stringNumberAlternates);

    JSType restrictedUnion = stringNumberUnion.getRestrictedUnion(registry.getJSType(JSTypeNative.STRING));

    assertEquals(registry.getJSType(JSTypeNative.STRING), restrictedUnion);
  }
}