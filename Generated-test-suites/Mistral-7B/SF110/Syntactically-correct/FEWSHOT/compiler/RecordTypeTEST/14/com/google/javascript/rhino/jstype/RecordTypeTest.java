package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

public class RecordTypeTest {
  @Test
  public void testIsSubtype() {
    JSTypeType typeA = JSType.getBooleanType();
    RecordType recordB = new RecordType(JSTypeRegistry.getInstance(), null, true);
    RecordType recordC = new RecordType(JSTypeRegistry.getInstance(), null, true);
    RecordType recordD = new RecordType(JSTypeRegistry.getInstance(), null, true);
    recordD.defineProperty("foo", JSType.getNumberType(), false, null);

    // Boolean type is not a subtype of any record type
    assertFalse(typeA.isSubtype(recordB));
    assertFalse(typeA.isSubtype(recordC));
    assertFalse(typeA.isSubtype(recordD));

    // Empty record type is a subtype of any record type
    assertTrue(recordB.isSubtype(recordC));
    assertTrue(recordB.isSubtype(recordD));

    // Record type with no properties is a subtype of any record type
    assertTrue(recordC.isSubtype(recordB));
    assertTrue(recordC.isSubtype(recordD));

    // Record type with properties is a subtype of another record type with
    // the same properties and types
    RecordTypeBuilder builder = new RecordTypeBuilder(JSTypeRegistry.getInstance());
    builder.addProperty("foo", JSType.getStringType(), null);
    builder.addProperty("bar", JSType.getNumberType(), null);
    RecordType recordE = builder.build();
    assertTrue(recordB.isSubtype(recordE));
    assertTrue(recordC.isSubtype(recordE));

    // Record type with properties is not a subtype of another record type with
    // different properties or types
    RecordType recordF = new RecordType(JSTypeRegistry.getInstance(), null, true);
    recordF.defineProperty("baz", JSType.getNumberType(), false, null);
    assertFalse(recordB.isSubtype(recordF));
    assertFalse(recordC.isSubtype(recordF));

    // Record type with properties is a subtype of a record type with the same
    // properties and types, even if the order is different
    RecordType recordG = new RecordType(JSTypeRegistry.getInstance(), null, true);
    recordG.defineProperty("bar", JSType.getNumberType(), false, null);
    recordG.defineProperty("foo", JSType.getStringType(), false, null);
    assertTrue(recordB.isSubtype(recordG));
    assertTrue(recordC.isSubtype(recordG));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    ObjectType implicitPrototype = record.getImplicitPrototype();
    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getClassName());
  }

  @Test
  public void testDefineProperty() {
    RecordType record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    record.defineProperty("foo", JSType.getStringType(), true, null);
    assertTrue(record.hasProperty("foo"));
    assertEquals(JSType.getStringType(), record.getPropertyType("foo"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullProperties() {
    RecordType record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    record.defineProperty("foo", JSType.getStringType(), true, null);
    record.defineProperty("foo", null, true, null);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordA = new RecordType(JSTypeRegistry.getInstance(), null, true);
    recordA.defineProperty("foo", JSType.getStringType(), true, null);

    RecordType recordB = new RecordType(JSTypeRegistry.getInstance(), null, true);
    recordB.defineProperty("foo", JSType.getNumberType(), true, null);

    JSType greatestSubtype = recordA.getGreatestSubtypeHelper(recordB);
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype);

    greatestSubtype = recordA.getGreatestSubtypeHelper(recordA);
    assertEquals(recordA, greatestSubtype);
  }

  @Test
  public void testHasProperty() {
    RecordType record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    assertFalse(record.hasProperty("foo"));

    record.defineProperty("foo", JSType.getStringType(), true, null);
    assertTrue(record.hasProperty("foo"));
  }

  @Test
  public void testGetPropertyType() {
    RecordType record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    record.defineProperty("foo", JSType.getStringType(), true, null);
    assertEquals(JSType.getStringType(), record.getPropertyType("foo"));
  }

  @Test
  public void testGetPropertyNode() {
    RecordType record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    record.defineProperty("foo", JSType.getStringType(), true, null);
    assertNull(record.getPropertyNode("foo"));
  }

  @Test
  public void testIsFrozen() {
    RecordType record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    assertFalse(record.isFrozen());

    record = new RecordType(JSTypeRegistry.getInstance(), null, false);
    assertFalse(record.isFrozen());

    record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    record.setFrozen(true);
    assertTrue(record.isFrozen());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testDefinePropertyWhenFrozen() {
    RecordType record = new RecordType(JSTypeRegistry.getInstance(), null, true);
    record.setFrozen(true);
    record.defineProperty("foo", JSType.getStringType(), true, null);
  }
}