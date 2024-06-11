package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testIsSubtype() {
    JSType typeA = new RecordType(null, null);
    JSType typeB = new RecordType(null, null);
    assertTrue(typeA.isSubtype(typeB));

    JSType typeC = new RecordType(null, null, false);
    JSType typeD = new RecordType(null, null, false);
    assertTrue(typeC.isSubtype(typeD));

    JSType recordType = new RecordType(null, null, true);
    JSType recordTypeWithExtraProperty = new RecordType(null, null, true);
    recordTypeWithExtraProperty.defineProperty("extra", JSType.ANY, false, null);
    assertFalse(recordType.isSubtype(recordTypeWithExtraProperty));

    JSType recordTypeWithInvariantProperty = new RecordType(null, null, true);
    JSType recordTypeWithNonInvariantProperty = new RecordType(null, null, true);
    recordTypeWithNonInvariantProperty.defineProperty("nonInvariant", JSType.ANY, false, null);
    recordTypeWithNonInvariantProperty.getPropertyType("nonInvariant")
        .setInvariant(false);
    assertFalse(recordType.isSubtype(recordTypeWithNonInvariantProperty));

    JSType recordTypeWithSubtypeProperty = new RecordType(null, null, true);
    JSType subtypeRecord = new RecordType(null, null, true);
    RecordTypeBuilder subtypeRecordBuilder = new RecordTypeBuilder(null);
    subtypeRecordBuilder.setSynthesized(true);
    subtypeRecordBuilder.addProperty("subtype", JSType.ANY, null);
    JSType subtypeRecordWithProperty = subtypeRecordBuilder.build();
    recordTypeWithSubtypeProperty.defineProperty("subtype", JSType.ANY, false, null);
    recordTypeWithSubtypeProperty.setPropertyNode("subtype", null);
    assertFalse(recordType.isSubtype(subtypeRecordWithProperty));

    JSType recordTypeWithInvariantSubtypeProperty = new RecordType(null, null, true);
    JSType subtypeRecordWithInvariantProperty = new RecordType(null, null, true);
    subtypeRecordWithInvariantProperty.defineProperty("subtype", JSType.ANY, false, null);
    subtypeRecordWithInvariantProperty.getPropertyType("subtype")
        .setInvariant(true);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
    recordTypeBuilder.setSynthesized(true);
    recordTypeBuilder.addProperty("subtype", JSType.ANY, null);
    JSType recordTypeWithSubtype = recordTypeBuilder.build();
    recordTypeWithSubtypeProperty.setPropertyNode("subtype", recordTypeWithSubtype.getPropertyNode("subtype"));
    assertTrue(recordType.isSubtype(subtypeRecordWithInvariantProperty));

    JSType recordTypeWithUnionSubtypeProperty = new RecordType(null, null, true);
    JSType unionType = new UnionTypeBuilder(null);
    unionType.addAlternate(recordTypeWithSubtypeProperty);
    JSType recordTypeWithUnionSubtype = new RecordTypeBuilder(null).build();
    recordTypeWithUnionSubtypeProperty.setPropertyNode("subtype", unionType);
    assertTrue(recordType.isSubtype(recordTypeWithUnionSubtype));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType recordTypeA = new RecordType(null, null, true);
    JSType recordTypeB = new RecordType(null, null, true);
    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertTrue(recordTypeA.isSubtype(greatestSubtype));

    JSType recordTypeC = new RecordType(null, null, true);
    JSType recordTypeD = new RecordType(null, null, false);
    JSType greatestSubtype2 = recordTypeC.getGreatestSubtypeHelper(recordTypeD);
    assertTrue(recordTypeC.isSubtype(greatestSubtype2));

    RecordType recordTypeE = new RecordType(null, null, true);
    RecordType recordTypeF = new RecordType(null, null, true);
    recordTypeF.defineProperty("extra", JSType.ANY, false, null);
    JSType greatestSubtype3 = recordTypeE.getGreatestSubtypeHelper(recordTypeF);
    assertFalse(recordTypeE.isSubtype(greatestSubtype3));

    RecordType recordTypeG = new RecordType(null, null, true);
    RecordType recordTypeH = new RecordType(null, null, true);
    recordTypeH.defineProperty("nonInvariant", JSType.ANY, false, null);
    recordTypeH.getPropertyType("nonInvariant").setInvariant(false);
    JSType greatestSubtype4 = recordTypeG.getGreatestSubtypeHelper(recordTypeH);
    assertFalse(recordTypeG.isSubtype(greatestSubtype4));

    RecordType recordTypeI = new RecordType(null, null, true);
    RecordType recordTypeJ = new RecordType(null, null, true);
    RecordType recordTypeK = new RecordType(null, null, true);
    recordTypeJ.defineProperty("subtype", recordTypeK, false, null);
    JSType greatestSubtype5 = recordTypeI.getGreatestSubtypeHelper(recordTypeJ);
    assertTrue(recordTypeI.isSubtype(greatestSubtype5));

    RecordType recordTypeL = new RecordType(null, null, true);
    RecordType recordTypeM = new RecordType(null, null, true);
    recordTypeM.defineProperty("subtype", JSType.ANY, false, null);
    RecordType recordTypeN = new RecordType(null, null, true);
    RecordTypeBuilder recordTypeNBuilder = new RecordTypeBuilder(null);
    recordTypeNBuilder.setSynthesized(true);
    recordTypeNBuilder.addProperty("subtype", recordTypeK, null);
    JSType greatestSubtype6 = recordTypeL.getGreatestSubtypeHelper(recordTypeM);
    assertTrue(recordTypeL.isSubtype(greatestSubtype6));

    RecordType recordTypeO = new RecordType(null, null, true);
    RecordType recordTypeP = new RecordType(null, null, true);
    recordTypeP.defineProperty("subtype", recordTypeK, false, null);
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(null);
    unionTypeBuilder.addAlternate(recordTypeN);
    JSType greatestSubtype7 = recordTypeO.getGreatestSubtypeHelper(recordTypeP);
    assertTrue(recordTypeO.isSubtype(greatestSubtype7));
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordTypeA = new RecordType(null, null, true);
    RecordType recordTypeB = new RecordType(null, null, true);
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.STRUCTURAL));

    RecordType recordTypeC = new RecordType(null, null, true);
    RecordType recordTypeD = new RecordType(null, null, true);
    recordTypeD.defineProperty("extra", JSType.ANY, false, null);
    assertFalse(recordTypeC.checkRecordEquivalenceHelper(recordTypeD, EquivalenceMethod.STRUCTURAL));

    RecordType recordTypeE = new RecordType(null, null, true);
    RecordType recordTypeF = new RecordType(null, null, true);
    recordTypeF.defineProperty("nonInvariant", JSType.ANY, false, null);
    recordTypeF.getPropertyType("nonInvariant").setInvariant(false);
    assertFalse(recordTypeE.checkRecordEquivalenceHelper(recordTypeF, EquivalenceMethod.STRUCTURAL));

    RecordType recordTypeG = new RecordType(null, null, true);
    RecordType recordTypeH = new RecordType(null, null, true);
    recordTypeH.defineProperty("subtype", recordTypeG, false, null);
    RecordType recordTypeI = new RecordType(null, null, true);
    RecordTypeBuilder recordTypeIBuilder = new RecordTypeBuilder(null);
    recordTypeIBuilder.setSynthesized(true);
    recordTypeIBuilder.addProperty("subtype", recordTypeG, null);
    RecordType recordTypeI2 = recordTypeIBuilder.build();
    assertTrue(recordTypeG.checkRecordEquivalenceHelper(recordTypeI, EquivalenceMethod.STRUCTURAL));
    assertTrue(recordTypeG.checkRecordEquivalenceHelper(recordTypeI2, EquivalenceMethod.STRUCTURAL));

    RecordType recordTypeJ = new RecordType(null, null, true);
    RecordType recordTypeK = new RecordType(null, null, true);
    recordTypeK.defineProperty("subtype", recordTypeJ, false, null);
    RecordType recordTypeL = new RecordType(null, null, true);
    RecordTypeBuilder recordTypeLBuilder = new RecordTypeBuilder(null);
    recordTypeLBuilder.setSynthesized(true);
    recordTypeLBuilder.addProperty("subtype", recordTypeJ, null);
    RecordType recordTypeM = recordTypeLBuilder.build();
    assertTrue(recordTypeJ.checkRecordEquivalenceHelper(recordTypeM, EquivalenceMethod.STRUCTURAL));

    RecordType recordTypeN = new RecordType(null, null, true);
    RecordType recordTypeO = new RecordType(null, null, true);
    recordTypeO.defineProperty("subtype", recordTypeN, false, null);
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(null);
    unionTypeBuilder.addAlternate(recordTypeN);
    RecordType recordTypeP = new RecordType(null, null, true);
    RecordTypeBuilder recordTypePBuilder = new RecordTypeBuilder(null);
    recordTypePBuilder.setSynthesized(true);
    recordTypePBuilder.addProperty("subtype", recordTypeN, null);
    RecordType recordTypeQ = recordTypePBuilder.build();
    assertTrue(recordTypeN.checkRecordEquivalenceHelper(recordTypeQ, EquivalenceMethod.STRUCTURAL));
  }
}