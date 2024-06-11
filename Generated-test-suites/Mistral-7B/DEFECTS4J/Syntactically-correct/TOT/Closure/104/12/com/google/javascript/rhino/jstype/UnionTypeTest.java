package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.VoidType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionType() {
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = JSTypeRegistry.getInstance().getBuiltins().number;
    JSType stringType = JSTypeRegistry.getInstance().getBuiltins().string;
    JSType booleanType = JSTypeRegistry.getInstance().getBuiltins().booleanType;

    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), alternates);

    unionType.addAlternate(numberType);
    unionType.addAlternate(stringType);
    unionType.addAlternate(booleanType);

    // Test equals
    UnionType sameUnionType = new UnionType(JSTypeRegistry.getInstance(), alternates);
    assertTrue(unionType.equals(sameUnionType));

    // Test hashCode
    UnionType differentUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, stringType));
    assertNotSame(unionType.hashCode(), differentUnionType.hashCode());

    // Test canAssignTo
    assertTrue(numberType.canAssignTo(unionType));
    assertFalse(stringType.canAssignTo(unionType));
    assertFalse(booleanType.canAssignTo(unionType));
    assertFalse(unionType.canAssignTo(numberType));
    assertFalse(unionType.canAssignTo(stringType));
    assertFalse(unionType.canAssignTo(booleanType));

    // Test matchesNumberContext
    assertTrue(numberType.matchesNumberContext());
    assertTrue(unionType.matchesNumberContext());

    // Test matchesStringContext
    assertTrue(stringType.matchesStringContext());
    assertTrue(unionType.matchesStringContext());

    // Test matchesObjectContext
    assertTrue(numberType.matchesObjectContext());
    assertTrue(stringType.matchesObjectContext());
    assertTrue(unionType.matchesObjectContext());

    // Test findPropertyType
    assertEquals(numberType, unionType.findPropertyType("length"));
    assertEquals(stringType, unionType.findPropertyType("toString"));

    // Test isNullType
    assertFalse(unionType.isNullType());

    // Test isVoidType
    assertFalse(unionType.isVoidType());

    // Test getLeastSupertype
    JSType leastSupertype = unionType.getLeastSupertype(numberType);
    assertEquals(numberType, leastSupertype);
    leastSupertype = unionType.getLeastSupertype(booleanType);
    assertEquals(booleanType, leastSupertype);

    // Test restrictByNotNullOrUndefined
    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertEquals(booleanType, restrictedUnionType.getAlternates().iterator().next());

    // Test testForEquality
    UnionType otherUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(booleanType, stringType));
    assertEquals(UNKNOWN, unionType.testForEquality(otherUnionType));

    // Test canBeCalled
    assertTrue(stringType.canBeCalled());
    assertTrue(unionType.canBeCalled());

    // Test meets
    UnionType meetsUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, booleanType));
    JSType meetResult = unionType.meet(meetsUnionType);
    assertNull(meetResult);

    meetsUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, stringType));
    meetResult = unionType.meet(meetsUnionType);
    assertEquals(unionType, meetResult);

    // Test toString
    assertEquals("(number, string, boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeWithVoidType() {
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = JSTypeRegistry.getInstance().getBuiltins().number;
    JSType stringType = JSTypeRegistry.getInstance().getBuiltins().string;
    JSType booleanType = JSTypeRegistry.getInstance().getBuiltins().booleanType;
    JSType voidType = JSTypeRegistry.getInstance().getBuiltins().voidType;

    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), alternates);

    unionType.addAlternate(numberType);
    unionType.addAlternate(stringType);
    unionType.addAlternate(booleanType);
    unionType.addAlternate(voidType);

    // Test equals
    UnionType sameUnionType = new UnionType(JSTypeRegistry.getInstance(), alternates);
    assertTrue(unionType.equals(sameUnionType));

    // Test hashCode
    UnionType differentUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, stringType));
    assertNotSame(unionType.hashCode(), differentUnionType.hashCode());

    // Test canAssignTo
    assertTrue(numberType.canAssignTo(unionType));
    assertFalse(stringType.canAssignTo(unionType));
    assertFalse(booleanType.canAssignTo(unionType));
    assertFalse(voidType.canAssignTo(unionType));
    assertFalse(unionType.canAssignTo(numberType));
    assertFalse(unionType.canAssignTo(stringType));
    assertFalse(unionType.canAssignTo(booleanType));
    assertFalse(unionType.canAssignTo(voidType));

    // Test matchesNumberContext
    assertTrue(numberType.matchesNumberContext());
    assertTrue(unionType.matchesNumberContext());

    // Test matchesStringContext
    assertTrue(stringType.matchesStringContext());
    assertTrue(unionType.matchesStringContext());

    // Test matchesObjectContext
    assertTrue(numberType.matchesObjectContext());
    assertTrue(stringType.matchesObjectContext());
    assertTrue(booleanType.matchesObjectContext());
    assertFalse(voidType.matchesObjectContext());
    assertTrue(unionType.matchesObjectContext());

    // Test findPropertyType
    assertEquals(numberType, unionType.findPropertyType("length"));
    assertEquals(stringType, unionType.findPropertyType("toString"));

    // Test isNullType
    assertFalse(unionType.isNullType());

    // Test isVoidType
    assertTrue(unionType.isVoidType());

    // Test getLeastSupertype
    JSType leastSupertype = unionType.getLeastSupertype(numberType);
    assertEquals(numberType, leastSupertype);
    leastSupertype = unionType.getLeastSupertype(booleanType);
    assertEquals(booleanType, leastSupertype);

    // Test restrictByNotNullOrUndefined
    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertEquals(booleanType, restrictedUnionType.getAlternates().iterator().next());

    // Test testForEquality
    UnionType otherUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(booleanType, voidType));
    assertEquals(UNKNOWN, unionType.testForEquality(otherUnionType));

    // Test canBeCalled
    assertTrue(stringType.canBeCalled());
    assertFalse(voidType.canBeCalled());
    assertTrue(unionType.canBeCalled());

    // Test meets
    UnionType meetsUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, booleanType));
    JSType meetResult = unionType.meet(meetsUnionType);
    assertNull(meetResult);

    meetsUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, stringType));
    meetResult = unionType.meet(meetsUnionType);
    assertEquals(unionType, meetResult);

    meetsUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, voidType));
    meetResult = unionType.meet(meetsUnionType);
    assertEquals(numberType, meetResult);

    // Test toString
    assertEquals("(number, string, boolean, void)", unionType.toString());
  }

  @Test
  public void testUnionTypeWithNullType() {
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = JSTypeRegistry.getInstance().getBuiltins().number;
    JSType stringType = JSTypeRegistry.getInstance().getBuiltins().string;
    JSType booleanType = JSTypeRegistry.getInstance().getBuiltins().booleanType;
    JSType nullType = JSTypeRegistry.getInstance().getBuiltins().nullType;

    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), alternates);

    unionType.addAlternate(numberType);
    unionType.addAlternate(stringType);
    unionType.addAlternate(booleanType);
    unionType.addAlternate(nullType);

    // Test equals
    UnionType sameUnionType = new UnionType(JSTypeRegistry.getInstance(), alternates);
    assertTrue(unionType.equals(sameUnionType));

    // Test hashCode
    UnionType differentUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, stringType));
    assertNotSame(unionType.hashCode(), differentUnionType.hashCode());

    // Test canAssignTo
    assertTrue(numberType.canAssignTo(unionType));
    assertFalse(stringType.canAssignTo(unionType));
    assertFalse(booleanType.canAssignTo(unionType));
    assertFalse(nullType.canAssignTo(unionType));
    assertFalse(unionType.canAssignTo(numberType));
    assertFalse(unionType.canAssignTo(stringType));
    assertFalse(unionType.canAssignTo(booleanType));
    assertFalse(unionType.canAssignTo(nullType));

    // Test matchesNumberContext
    assertTrue(numberType.matchesNumberContext());
    assertTrue(unionType.matchesNumberContext());

    // Test matchesStringContext
    assertTrue(stringType.matchesStringContext());
    assertTrue(unionType.matchesStringContext());

    // Test matchesObjectContext
    assertTrue(numberType.matchesObjectContext());
    assertTrue(stringType.matchesObjectContext());
    assertTrue(booleanType.matchesObjectContext());
    assertFalse(nullType.matchesObjectContext());
    assertTrue(unionType.matchesObjectContext());

    // Test findPropertyType
    assertEquals(numberType, unionType.findPropertyType("length"));
    assertEquals(stringType, unionType.findPropertyType("toString"));

    // Test isNullType
    assertTrue(unionType.isNullable());

    // Test isVoidType
    assertFalse(unionType.isVoidType());

    // Test getLeastSupertype
    JSType leastSupertype = unionType.getLeastSupertype(numberType);
    assertEquals(numberType, leastSupertype);
    leastSupertype = unionType.getLeastSupertype(booleanType);
    assertEquals(booleanType, leastSupertype);

    // Test restrictByNotNullOrUndefined
    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertEquals(booleanType, restrictedUnionType.getAlternates().iterator().next());

    // Test testForEquality
    UnionType otherUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(booleanType, nullType));
    assertEquals(UNKNOWN, unionType.testForEquality(otherUnionType));

    // Test canBeCalled
    assertTrue(stringType.canBeCalled());
    assertFalse(nullType.canBeCalled());
    assertTrue(unionType.canBeCalled());

    // Test meets
    UnionType meetsUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, booleanType));
    JSType meetResult = unionType.meet(meetsUnionType);
    assertNull(meetResult);

    meetsUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, stringType));
    meetResult = unionType.meet(meetsUnionType);
    assertEquals(unionType, meetResult);

    meetsUnionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(numberType, nullType));
    meetResult = unionType.meet(meetsUnionType);
    assertEquals(numberType, meetResult);

    // Test toString
    assertEquals("(number, string, boolean, null)", unionType.toString());
  }
}