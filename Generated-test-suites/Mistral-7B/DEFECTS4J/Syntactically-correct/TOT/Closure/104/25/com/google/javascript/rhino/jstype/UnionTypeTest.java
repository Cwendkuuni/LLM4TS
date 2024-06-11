import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import static com.google.javascript.rhino.jstype.UnionType.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {
  private static final ErrorReporter ERR_REP = ErrorReporter.NULL;
  private JSTypeRegistry registry;
  private UnionType stringUnionType;
  private UnionType booleanUnionType;
  private UnionType stringBooleanUnionType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    stringUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance()));
    booleanUnionType = new UnionType(registry, ImmutableSet.of(BooleanType.instance()));
    stringBooleanUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), BooleanType.instance()));
  }

  @Test
  public void testUnionTypeGetAlternates() {
    ImmutableSet<JSType> alternates = stringUnionType.getAlternates();
    assertEquals(ImmutableSet.of(StringType.instance()), alternates);

    alternates = booleanUnionType.getAlternates();
    assertEquals(ImmutableSet.of(BooleanType.instance()), alternates);

    alternates = stringBooleanUnionType.getAlternates();
    ImmutableSet<JSType> expected = ImmutableSet.of(StringType.instance(), BooleanType.instance());
    assertEquals(expected, alternates);
  }

  @Test
  public void testCanAssignToString() {
    JSType stringType = StringType.instance();
    assertTrue(stringUnionType.canAssignTo(stringType));
    assertTrue(booleanUnionType.canAssignTo(stringType));
    assertTrue(stringBooleanUnionType.canAssignTo(stringType));

    JSType nullType = NullType.instance();
    assertFalse(stringUnionType.canAssignTo(nullType));
    assertFalse(booleanUnionType.canAssignTo(nullType));
    assertFalse(stringBooleanUnionType.canAssignTo(nullType));

    JSType voidType = VoidType.instance();
    assertFalse(stringUnionType.canAssignTo(voidType));
    assertFalse(booleanUnionType.canAssignTo(voidType));
    assertFalse(stringBooleanUnionType.canAssignTo(voidType));
  }

  @Test
  public void testCanBeCalled() {
    assertTrue(stringUnionType.canBeCalled());
    assertTrue(booleanUnionType.canBeCalled());
    assertTrue(stringBooleanUnionType.canBeCalled());
  }

  @Test
  public void testForgiveUnknownNames() {
    stringUnionType.forgiveUnknownNames();
    booleanUnionType.forgiveUnknownNames();
    stringBooleanUnionType.forgiveUnknownNames();
  }

  @Test
  public void testMatchesNumberContext() {
    assertFalse(stringUnionType.matchesNumberContext());
    assertFalse(booleanUnionType.matchesNumberContext());
    assertFalse(stringBooleanUnionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    assertTrue(stringUnionType.matchesStringContext());
    assertTrue(booleanUnionType.matchesStringContext());
    assertTrue(stringBooleanUnionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    assertTrue(stringUnionType.matchesObjectContext());
    assertTrue(booleanUnionType.matchesObjectContext());
    assertTrue(stringBooleanUnionType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    JSType stringPropertyType = StringType.instance();
    JSType booleanPropertyType = BooleanType.instance();
    StringProperty propertyName = new StringProperty("foo");

    JSType stringStringPropertyType = StringType.instance();
    JSType stringBooleanStringPropertyType = StringType.instance();
    JSType booleanStringPropertyType = BooleanType.instance();

    JSType stringStringProperty =
        new PropertyType(stringStringPropertyType, propertyName);
    JSType stringBooleanStringProperty =
        new PropertyType(booleanStringPropertyType, propertyName);

    JSType stringString = new PropertyType(stringStringPropertyType, null);
    JSType stringBoolean = new PropertyType(booleanStringPropertyType, null);

    JSType stringStringUnionType =
        new UnionType(registry, ImmutableSet.of(stringStringPropertyType, stringBooleanPropertyType));
    JSType stringBooleanUnionType =
        new UnionType(registry, ImmutableSet.of(stringStringPropertyType, booleanStringPropertyType));

    assertEquals(stringStringPropertyType, stringStringUnionType.findPropertyType("foo"));
    assertEquals(booleanStringPropertyType, stringBooleanUnionType.findPropertyType("foo"));
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    NullType nullType = NullType.instance();
    UnionType nullableStringUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), nullType));
    UnionType nullableBooleanUnionType =
        new UnionType(registry, ImmutableSet.of(BooleanType.instance(), nullType));
    UnionType nullableStringBooleanUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), BooleanType.instance(), nullType));

    UnionType restrictedNullableStringUnionType = nullableStringUnionType.restrictByNotNullOrUndefined();
    UnionType restrictedNullableBooleanUnionType = nullableBooleanUnionType.restrictByNotNullOrUndefined();
    UnionType restrictedNullableStringBooleanUnionType = nullableStringBooleanUnionType.restrictByNotNullOrUndefined();

    assertEquals(StringType.instance(), restrictedNullableStringUnionType);
    assertEquals(BooleanType.instance(), restrictedNullableBooleanUnionType);
    assertEquals(StringType.instance(), restrictedNullableStringBooleanUnionType);
  }

  @Test
  public void testTestForEquality() {
    UnionType stringStringUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), StringType.instance()));
    UnionType stringBooleanUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), BooleanType.instance()));
    UnionType booleanStringUnionType = new UnionType(registry, ImmutableSet.of(BooleanType.instance(), StringType.instance()));
    Object obj = "foo";
    JSType stringType = StringType.instance();
    JSType booleanType = BooleanType.instance();

    JSType stringString = new PropertyType(stringType, null);
    JSType stringBoolean = new PropertyType(booleanType, null);
    JSType booleanString = new PropertyType(booleanType, null);

    assertEquals(UNKNOWN, stringStringUnionType.testForEquality(stringType));
    assertEquals(UNKNOWN, stringBooleanUnionType.testForEquality(booleanType));
    assertEquals(UNKNOWN, booleanStringUnionType.testForEquality(stringType));

    assertTrue(stringStringUnionType.testForEquality(stringString));
    assertTrue(stringBooleanUnionType.testForEquality(booleanString));

    assertEquals(TernaryValue.LEFT, stringStringUnionType.testForEquality(obj));
    assertEquals(TernaryValue.LEFT, stringBooleanUnionType.testForEquality(obj));

    assertEquals(TernaryValue.RIGHT, stringStringUnionType.testForEquality(booleanType));
    assertEquals(TernaryValue.RIGHT, stringBooleanUnionType.testForEquality(stringType));
  }

  @Test
  public void testIsNullable() {
    assertFalse(StringType.instance().isNullable());
    assertFalse(BooleanType.instance().isNullable());

    UnionType nullableStringUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), NullType.instance()));
    UnionType nullableBooleanUnionType = new UnionType(registry, ImmutableSet.of(BooleanType.instance(), NullType.instance()));

    assertTrue(nullableStringUnionType.isNullable());
    assertTrue(nullableBooleanUnionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType stringUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance()));
    UnionType booleanUnionType = new UnionType(registry, ImmutableSet.of(BooleanType.instance()));

    assertFalse(stringUnionType.isUnknownType());
    assertFalse(booleanUnionType.isUnknownType());

    UnionType stringBooleanUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), BooleanType.instance()));

    assertFalse(stringBooleanUnionType.isUnknownType());

    UnionType unknownStringBooleanUnionType = new UnionType(registry, ImmutableSet.of(UnknownType.instance(), StringType.instance()));

    assertTrue(unknownStringBooleanUnionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType stringStringUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), StringType.instance()));
    UnionType stringNumberUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), NumberType.instance()));
    UnionType stringBooleanUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), BooleanType.instance()));
    UnionType numberStringUnionType = new UnionType(registry, ImmutableSet.of(NumberType.instance(), StringType.instance()));

    assertEquals(StringType.instance(), stringStringUnionType.getLeastSupertype(StringType.instance()));
    assertEquals(StringType.instance(), stringStringUnionType.getLeastSupertype(stringStringUnionType));
    assertEquals(StringType.instance(), stringStringUnionType.getLeastSupertype(numberStringUnionType));

    assertEquals(NumberType.instance(), stringNumberUnionType.getLeastSupertype(StringType.instance()));
    assertEquals(NumberType.instance(), stringNumberUnionType.getLeastSupertype(stringNumberUnionType));
    assertEquals(StringType.instance(), stringNumberUnionType.getLeastSupertype(stringStringUnionType));

    assertEquals(StringType.instance(), stringBooleanUnionType.getLeastSupertype(StringType.instance()));
    assertEquals(BooleanType.instance(), stringBooleanUnionType.getLeastSupertype(StringType.instance()));
    assertEquals(StringType.instance(), stringBooleanUnionType.getLeastSupertype(stringBooleanUnionType));
    assertEquals(BooleanType.instance(), stringBooleanUnionType.getLeastSupertype(BooleanType.instance()));
    assertEquals(StringType.instance(), stringBooleanUnionType.getLeastSupertype(stringStringUnionType));

    assertEquals(NumberType.instance(), numberStringUnionType.getLeastSupertype(StringType.instance()));
    assertEquals(NumberType.instance(), numberStringUnionType.getLeastSupertype(numberStringUnionType));
    assertEquals(StringType.instance(), numberStringUnionType.getLeastSupertype(stringStringUnionType));
  }

  @Test
  public void testMeet() {
    UnionType stringUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance()));
    UnionType numberUnionType = new UnionType(registry, ImmutableSet.of(NumberType.instance()));

    UnionType stringStringUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), StringType.instance()));
    UnionType stringNumberUnionType = new UnionType(registry, ImmutableSet.of(StringType.instance(), NumberType.instance()));

    assertEquals(StringType.instance(), stringUnionType.meet(StringType.instance()));
    assertEquals(StringType.instance(), stringUnionType.meet(stringStringUnionType));
    assertEquals(StringType.instance(), stringUnionType.meet(numberStringUnionType));

    assertEquals(NumberType.instance(), numberUnionType.meet(StringType.instance()));
    assertEquals(NumberType.instance(), numberUnionType.meet(numberStringUnionType));
    assertEquals(StringType.instance(), numberUnionType.meet(stringStringUnionType));

    assertEquals(StringType.instance(), stringStringUnionType.meet(StringType.instance()));
    assertEquals(NumberType.instance(), stringStringUnionType.meet(numberStringUnionType));
    assertEquals(StringType.instance(), stringStringUnionType.meet(stringUnionType));
    assertEquals(StringType.instance(), stringStringUnionType.meet(numberUnionType));

    assertEquals(StringType.instance(), stringNumberUnionType.meet(StringType.instance()));
    assertEquals(StringType.instance(), stringNumberUnionType.meet(stringStringUnionType));
    assertEquals(NumberType.instance(), stringNumberUnionType.meet(stringUnionType));
    assertEquals(StringType.instance(), stringNumberUnionType.meet(numberUnionType));
    assertEquals(StringType.instance(), stringNumberUnionType.meet(numberStringUnionType));
  }
}