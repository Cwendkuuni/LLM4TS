import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Test;

public class UnionTypeTest {

  private static JSTypeRegistry registry = JSTypeFactory.getRegistry();

  @Test
  public void testUnionTypeBasic() {
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(intType(), stringType()));
    assertEquals("(int|string)", intOrString.toString());

    // Basic equality test
    UnionType sameUnionType = new UnionType(registry, ImmutableSet.of(intType(), stringType()));
    assertTrue(intOrString.equals(sameUnionType));

    // Basic subtype test
    assertTrue(intOrString.isSubtype(intType()));
    assertTrue(intOrString.isSubtype(stringType()));

    // Basic type checking
    assertFalse(intOrString.isNullType());
    assertFalse(intOrString.isVoidType());
    assertFalse(intOrString.isObject());

    // Test conversions
    assertEquals(intType(), intOrString.restrictByNotNullOrUndefined());
    assertEquals(stringType(), intOrString.restrictByNotNullOrUndefined().getLeastSupertype(stringType()));

    // Test assignments
    UnionType intOrString2 = new UnionType(registry, ImmutableSet.of(stringType(), intType()));
    assertFalse(intOrString.canAssignTo(intOrString2));
    assertFalse(intOrString2.canAssignTo(intOrString));

    // Test property access
    assertNull(intOrString.findPropertyType("toString"));
    assertEquals(stringType(), intOrString.findPropertyType("length"));

    // Test callability
    assertFalse(intOrString.canBeCalled());

    // Test nullability
    assertFalse(intOrString.isNullable());

    // Test meets
    UnionType intOrString3 = new UnionType(registry, ImmutableSet.of(intType(), stringType(), booleanType()));
    assertEquals(intOrString, intOrString.meet(intOrString));
    assertEquals(boolType(), intOrString.meet(booleanType()));
    assertEquals(intOrString3, intOrString.meet(intOrString3));
  }

  @Test
  public void testUnionTypeNested() {
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(intType(), new UnionType(registry, ImmutableSet.of(stringType()))));
    assertEquals("(int|(string|null))", intOrString.toString());

    // Test conversions
    assertEquals(intType(), intOrString.restrictByNotNullOrUndefined());
    assertEquals(stringType(), intOrString.restrictByNotNullOrUndefined().getLeastSupertype(stringType()));

    // Test assignments
    UnionType intOrString2 = new UnionType(registry, ImmutableSet.of(stringType(), new UnionType(registry, ImmutableSet.of(intType()))));
    assertFalse(intOrString.canAssignTo(intOrString2));
    assertFalse(intOrString2.canAssignTo(intOrString));

    // Test property access
    assertNull(intOrString.findPropertyType("toString"));
    assertEquals(stringType(), intOrString.findPropertyType("length"));

    // Test callability
    assertFalse(intOrString.canBeCalled());

    // Test nullability
    assertFalse(intOrString.isNullable());
  }

  @Test
  public void testUnionTypeComplex() {
    UnionType intOrBoolOrObj = new UnionType(registry, ImmutableSet.of(
        intType(),
        boolType(),
        objType(registry),
        new UnionType(registry, ImmutableSet.of(intType(), stringType())),
        new UnionType(registry, ImmutableSet.of(objType(registry), intType()))
    ));
    assertEquals("(int|bool|object|(int|string)|(object|int))", intOrBoolOrObj.toString());
  }

  @Test
  public void testUnionTypeRestriction() {
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(intType(), stringType()));
    UnionType intOnly = intOrString.getRestrictedUnion(intType());
    assertEquals("int", intOnly.toString());
    assertEquals(intOnly, intOnly.getLeastSupertype(intType()));
    assertEquals(intOnly, intOnly.getLeastSupertype(stringType()));

    UnionType stringOnly = intOrString.getRestrictedUnion(stringType());
    assertEquals("string", stringOnly.toString());
    assertEquals(stringOnly, stringOnly.getLeastSupertype(intType()));
    assertEquals(stringOnly, stringOnly.getLeastSupertype(stringType()));

    UnionType intOrBool = new UnionType(registry, ImmutableSet.of(intType(), boolType()));
    UnionType intOrStringOrBool = new UnionType(registry, ImmutableSet.of(intType(), stringType(), boolType()));
    assertEquals(intOrBool, intOrStringOrBool.getRestrictedUnion(intType()));
    assertEquals(boolType(), intOrStringOrBool.getRestrictedUnion(boolType()));

    UnionType intOrNull = new UnionType(registry, ImmutableSet.of(intType(), nullType()));
    UnionType intOrNullOrBool = new UnionType(registry, ImmutableSet.of(intType(), nullType(), boolType()));
    assertEquals(intOrNull, intOrNullOrBool.getRestrictedUnion(intType()));
    assertEquals(nullType(), intOrNullOrBool.getRestrictedUnion(nullType()));
  }
}