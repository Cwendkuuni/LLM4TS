package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testIsSubtype() {
    JSType typeA = new IntType();
    JSType recordTypeB = new RecordType(null, null);
    assertFalse(RecordType.isSubtype(typeA, recordTypeB));

    RecordType recordTypeC = new RecordType(null, null);
    assertTrue(RecordType.isSubtype(recordTypeC, recordTypeB));

    RecordType recordTypeD = new RecordType(null, null, false);
    assertTrue(RecordType.isSubtype(recordTypeD, recordTypeB));

    RecordType recordTypeE = new RecordType(null, null, false);
    RecordType recordTypeF = new RecordType(null, null, false);
    RecordType recordTypeG = new RecordType(null, null, false);
    recordTypeG.defineProperty("prop1", new IntType(), false, null);

    assertTrue(RecordType.isSubtype(recordTypeE, recordTypeG));
    assertFalse(RecordType.isSubtype(recordTypeG, recordTypeE));

    RecordType recordTypeH = new RecordType(null, null, false);
    recordTypeH.defineProperty("prop1", new IntType(), false, null);
    recordTypeH.defineProperty("prop2", new StringType(), false, null);

    RecordType recordTypeI = new RecordType(null, null, false);
    recordTypeI.defineProperty("prop1", new IntType(), false, null);
    recordTypeI.defineProperty("prop2", new StringType(), false, null);

    assertTrue(RecordType.isSubtype(recordTypeI, recordTypeH));
    assertTrue(RecordType.isSubtype(recordTypeH, recordTypeI));

    RecordType recordTypeJ = new RecordType(null, null, false);
    recordTypeJ.defineProperty("prop1", new IntType(), false, null);
    recordTypeJ.defineProperty("prop2", new StringType(), false, null);
    recordTypeJ.defineProperty("prop3", new IntType(), false, null);

    RecordType recordTypeK = new RecordType(null, null, false);
    recordTypeK.defineProperty("prop1", new IntType(), false, null);
    recordTypeK.defineProperty("prop2", new StringType(), false, null);

    assertTrue(RecordType.isSubtype(recordTypeK, recordTypeJ));
    assertFalse(RecordType.isSubtype(recordTypeJ, recordTypeK));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType typeA = new IntType();
    JSType recordTypeB = new RecordType(null, null);
    JSType recordTypeC = new RecordType(null, null, false);
    recordTypeC.defineProperty("prop1", new IntType(), false, null);

    JSType result = recordTypeB.getGreatestSubtypeHelper(typeA);
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, result);

    result = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
    assertEquals(recordTypeC, result);

    RecordType recordTypeD = new RecordType(null, null, false);
    recordTypeD.defineProperty("prop1", new IntType(), false, null);
    recordTypeD.defineProperty("prop2", new StringType(), false, null);

    UnionType unionType = new UnionTypeBuilder(null)
        .addAlternate(recordTypeC)
        .addAlternate(recordTypeD)
        .build();

    result = recordTypeC.getGreatestSubtypeHelper(unionType);
    assertEquals(recordTypeD, result);

    RecordType recordTypeE = new RecordType(null, null, false);
    recordTypeE.defineProperty("prop1", new IntType(), false, null);
    recordTypeE.defineProperty("prop2", new StringType(), false, null);
    recordTypeE.defineProperty("prop3", new IntType(), false, null);

    result = recordTypeD.getGreatestSubtypeHelper(recordTypeE);
    assertEquals(recordTypeE, result);
  }

  @Test
  public void testIsSynthetic() {
    RecordType recordTypeA = new RecordType(null, null, true);
    assertTrue(recordTypeA.isSynthetic());

    RecordType recordTypeB = new RecordType(null, null, false);
    assertFalse(recordTypeB.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordTypeA = new RecordType(null, null, false);
    RecordType recordTypeB = new RecordType(null, null, false);
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB,
        EquivalenceMethod.STRUCTURAL));

    recordTypeB.defineProperty("prop1", new IntType(), false, null);
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB,
        EquivalenceMethod.STRUCTURAL));

    recordTypeB.defineProperty("prop1", new StringType(), false, null);
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB,
        EquivalenceMethod.STRUCTURAL));

    RecordType recordTypeC = new RecordType(null, null, false);
    recordTypeC.defineProperty("prop1", new IntType(), false, null);
    recordTypeC.defineProperty("prop2", new StringType(), false, null);

    recordTypeB.defineProperty("prop2", new IntType(), false, null);
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB,
        EquivalenceMethod.STRUCTURAL));

    recordTypeB.defineProperty("prop2", new StringType(), false, null);
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB,
        EquivalenceMethod.STRUCTURAL));

    recordTypeB.defineProperty("prop3", new IntType(), false, null);
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB,
        EquivalenceMethod.STRUCTURAL));
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(null, null, false);
    assertFalse(recordType.defineProperty("prop1", new IntType(), false, null));
    assertFalse(recordType.hasProperty("prop1"));

    recordType.defineProperty("prop1", new IntType(), false, null);
    assertTrue(recordType.hasProperty("prop1"));
    assertTrue(recordType.getPropertyType("prop1") instanceof IntType);
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullRecordProperty() {
    RecordType recordType = new RecordType(null, null, false);
    recordType.defineProperty("prop1", new IntType(), false, null);
    recordType.defineProperty("prop1", null);
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(null, null, false);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
  }

  @Test
  public void testGetOwnPropertyNames() {
    RecordType recordType = new RecordType(null, null, false);
    assertTrue(recordType.getOwnPropertyNames().isEmpty());

    recordType.defineProperty("prop1", new IntType(), false, null);
    recordType.defineProperty("prop2", new StringType(), false, null);

    Set<String> properties = recordType.getOwnPropertyNames();
    assertTrue(properties.contains("prop1"));
    assertTrue(properties.contains("prop2"));
  }
}