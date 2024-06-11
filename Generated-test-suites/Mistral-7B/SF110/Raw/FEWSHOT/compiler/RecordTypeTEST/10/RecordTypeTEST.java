package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.Set;

public class RecordTypeTest {
  @Test
  public void testIsSynthetic() {
    RecordType record = new RecordType(null, Maps.newHashMap(), false);
    assertTrue(record.isSynthetic());

    RecordType declaredRecord = new RecordType(null, Maps.newHashMap(), true);
    assertFalse(declaredRecord.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType record1 = new RecordType(null, Maps.newHashMap(), true);
    RecordType record2 = new RecordType(null, Maps.newHashMap(), true);

    assertEquals(record1, record2);

    RecordProperty prop1 = new RecordProperty("prop1", JSType.INT_TYPE);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.STRING_TYPE);

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", prop1);
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop2", prop2);

    RecordType record3 = new RecordType(null, properties1, true);
    RecordType record4 = new RecordType(null, properties2, true);

    Set<String> ownProperties1 = record3.getOwnPropertyNames();
    Set<String> ownProperties2 = record4.getOwnPropertyNames();

    Set<String> allProperties = Sets.union(ownProperties1, ownProperties2);

    for (String property : allProperties) {
      JSType propertyType1 = record3.getPropertyType(property);
      JSType propertyType2 = record4.getPropertyType(property);

      UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(null);
      unionTypeBuilder.addAlternate(record3);
      unionTypeType1 = unionTypeBuilder.build();
      UnionTypeBuilder unionTypeBuilder2 = new UnionTypeBuilder(null);
      unionTypeBuilder2.addAlternate(record4);
      UnionType unionType2 = unionTypeBuilder2.build();

      assertTrue(unionType1.isSubtype(unionType2));
      assertTrue(unionType2.isSubtype(unionType1));
      assertTrue(propertyType1.checkEquivalenceHelper(propertyType2, EquivalenceMethod.STRICT));
      assertTrue(propertyType1.isEquivalentTo(propertyType2));
    }

    assertFalse(record3.checkRecordEquivalenceHelper(record4, EquivalenceMethod.STRICT));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType record1 = new RecordType(null, Maps.newHashMap(), true);

    RecordProperty prop1 = new RecordProperty("prop1", JSType.INT_TYPE);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.STRING_TYPE);

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", prop1);
    properties1.put("prop2", prop2);

    RecordType record2 = new RecordType(null, properties1, true);

    RecordType record3 = new RecordType(null, Maps.newHashMap(), true);

    JSType greatestSubtype = record1.getGreatestSubtypeHelper(record2);
    assertEquals(record2, greatestSubtype);

    greatestSubtype = record1.getGreatestSubtypeHelper(record3);
    assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    RecordType record1 = new RecordType(null, Maps.newHashMap(), true);
    RecordType record2 = new RecordType(null, Maps.newHashMap(), true);

    RecordProperty prop1 = new RecordProperty("prop1", JSType.INT_TYPE);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.STRING_TYPE);

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", prop1);
    properties1.put("prop2", prop2);

    RecordType record3 = new RecordType(null, properties1, true);

    // Record1 is a subtype of Record1
    assertTrue(record1.isSubtype(record1));

    // Record1 is a subtype of Record2 because Record2 has all the properties of Record1
    assertTrue(record1.isSubtype(record2));

    // Record1 is not a subtype of ObjectType because ObjectType does not have the same properties as Record1
    assertFalse(record1.isSubtype(JSTypeNative.OBJECT_TYPE));

    // Record1 is a subtype of Record3 because Record3 has all the properties of Record1
    assertTrue(record1.isSubtype(record3));

    // Record3 is not a subtype of Record1 because Record1 has properties that Record3 does not have
    assertFalse(record3.isSubtype(record1));

    // Record2 is a subtype of Record3 because Record3 has all the properties of Record2
    assertTrue(record2.isSubtype(record3));

    // Record1 is a subtype of Record2 because all properties of Record1 are subtypes of the corresponding properties in Record2
    assertTrue(record1.isSubtype(record2));
  }
}

class EquivalenceMethod {
  static final EquivalenceMethod STRICT = new EquivalenceMethod();

  private EquivalenceMethod() {}
}