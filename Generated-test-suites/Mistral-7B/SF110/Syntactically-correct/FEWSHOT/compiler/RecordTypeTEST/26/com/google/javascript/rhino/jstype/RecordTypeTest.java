package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.types.ReferenceType;
import com.google.javascript.rhino.types.UnionType;

public class RecordTypeTest {

    @Test
    public void testIsSynthetic() {
        RecordType record = new RecordType(null, null, false);
        assertTrue(record.isSynthetic());
        RecordType declaredRecord = new RecordType(null, null, true);
        assertFalse(declaredRecord.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType record1 = new RecordType(null, null, true);
        RecordType record2 = new RecordType(null, null, true);
        RecordType record3 = new RecordType(null, null, true);
        RecordTypeBuilder builder = new RecordTypeBuilder(null);
        RecordProperty property1 = builder.addProperty("p1", null, null);
        RecordProperty property2 = builder.addProperty("p2", null, null);
        RecordType record4 = builder.build();

        record1.defineProperty("p1", null, false, null);
        record1.defineProperty("p2", null, false, null);

        record2.defineProperty("p1", null, false, null);
        record2.defineProperty("p2", null, false, null);

        record3.defineProperty("p1", null, false, null);
        record3.defineProperty("p2", null, false, null);
        record3.defineProperty("p3", null, false, null);

        assertTrue(record1.checkRecordEquivalenceHelper(record1, EquivalenceMethod.IDENTITY));
        assertTrue(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.IDENTITY));
        assertTrue(record1.checkRecordEquivalenceHelper(record3, EquivalenceMethod.IDENTITY));
        assertTrue(record1.checkRecordEquivalenceHelper(record4, EquivalenceMethod.IDENTITY));

        assertFalse(record1.checkRecordEquivalenceHelper(null, EquivalenceMethod.IDENTITY));
        assertFalse(record1.checkRecordEquivalenceHelper(new Object(), EquivalenceMethod.IDENTITY));
        RecordType record5 = new RecordType(null, null, true);
        record5.defineProperty("p1", new ReferenceType(null, null), false, null);
        assertFalse(record1.checkRecordEquivalenceHelper(record5, EquivalenceMethod.IDENTITY));

        UnionType record6 = new UnionType(new ObjectType[0]);
        assertFalse(record1.checkRecordEquivalenceHelper(record6, EquivalenceMethod.IDENTITY));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType record = new RecordType(null, null, true);
        ObjectType implicitPrototype = record.getImplicitPrototype();
        assertTrue(implicitPrototype.isInstanceOfType(JSTypeNative.OBJECT_TYPE));
    }

    @Test
    public void testDefineProperty() {
        RecordType record = new RecordType(null, null, true);
        assertTrue(record.defineProperty("p1", null, false, null));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType record1 = new RecordType(null, null, true);
        RecordType record2 = new RecordType(null, null, true);
        RecordType record3 = new RecordType(null, null, true);
        RecordProperty property1 = new RecordProperty("p1", null, null);
        RecordProperty property2 = new RecordProperty("p2", null, null);
        RecordTypeBuilder builder = new RecordTypeBuilder(null);
        builder.setSynthesized(true);

        builder.addProperty("p1", null, null);
        builder.addProperty("p2", null, null);
        RecordType record4 = builder.build();

        JSType record5 = record1.getGreatestSubtypeHelper(record2);
        assertTrue(record5.isInstanceOfType(JSTypeNative.OBJECT_TYPE));

        JSType record6 = record1.getGreatestSubtypeHelper(record3);
        assertTrue(record6.isInstanceOfType(JSTypeNative.OBJECT_TYPE));

        JSType record7 = record1.getGreatestSubtypeHelper(record4);
        assertTrue(record7.isInstanceOfType(record4));

        JSType record8 = record1.getGreatestSubtypeHelper(null);
        assertTrue(record8.isInstanceOfType(JSTypeNative.NO_TYPE));

        JSType record9 = record1.getGreatestSubtypeHelper(new Object());
        assertTrue(record9.isInstanceOfType(JSTypeNative.NO_TYPE));

        JSType record10 = record1.getGreatestSubtypeHelper(new UnionType(new ObjectType[0]));
        assertTrue(record10.isInstanceOfType(JSTypeNative.NO_TYPE));
    }

    @Test
    public void testIsSubtype() {
        RecordType record1 = new RecordType(null, null, true);
        RecordType record2 = new RecordType(null, null, true);
        RecordType record3 = new RecordType(null, null, true);
        RecordType record4 = new RecordType(null, null, true);
        RecordProperty property1 = new RecordProperty("p1", null, null);
        RecordProperty property2 = new RecordProperty("p2", null, null);
        RecordTypeBuilder builder = new RecordTypeBuilder(null);

        builder.addProperty("p1", null, null);
        builder.addProperty("p2", null, null);
        RecordType record5 = builder.build();

        assertTrue(record1.isSubtype(record1));
        assertTrue(record1.isSubtype(record2));
        assertTrue(record1.isSubtype(record3));
        assertTrue(record1.isSubtype(record4));
        assertTrue(record1.isSubtype(record5));
        assertTrue(record1.isSubtype(JSTypeNative.OBJECT_TYPE));

        UnionTypeUnionBuilder unionBuilder = new UnionTypeUnionBuilder(null);
        ObjectType objectType = new ObjectType(null);
        unionBuilder.addAlternate(objectType);
        UnionType unionType = unionBuilder.build();

        assertTrue(record1.isSubtype(unionType));

        RecordType record6 = new RecordType(null, null, true);
        RecordType record7 = new RecordType(null, null, true);
        record6.defineProperty("p1", null, false, null);
        record7.defineProperty("p2", null, false, null);
        record7.defineProperty("p1", null, false, null);

        assertFalse(record1.isSubtype(record6));
        assertFalse(record1.isSubtype(record7));

        RecordType record8 = new RecordType(null, null, true);
        RecordType record9 = new RecordType(null, null, true);
        record8.defineProperty("p1", null, false, null);
        record9.defineProperty("p1", new ReferenceType(null, null), false, null);

        assertFalse(record1.isSubtype(record8));
        assertFalse(record1.isSubtype(record9));

        RecordType record10 = new RecordType(null, null, true);
        RecordType record11 = new RecordType(null, null, true);
        record10.defineProperty("p1", null, false, null);
        record11.defineProperty("p1", null, false, null);
        record11.defineProperty("p2", null, false, null);

        assertFalse(record1.isSubtype(record10));
        assertFalse(record1.isSubtype(record11));

        RecordType record12 = new RecordType(null, null, true);
        record12.defineProperty("p1", null, false, null);
        RecordType record13 = new RecordType(null, null, true);
        record13.defineProperty("p2", null, false, null);

        assertFalse(record1.isSubtype(record12));
        assertFalse(record1.isSubtype(record13));

        RecordType record14 = new RecordType(null, null, true);
        RecordType record15 = new RecordType(null, null, true);
        record14.defineProperty("p1", null, false, null);
        record15.defineProperty("p1", null, true, null);

        assertFalse(record1.isSubtype(record14));
        assertFalse(record1.isSubtype(record15));

        RecordType record16 = new RecordType(null, null, true);
        RecordType record17 = new RecordType(null, null, true);
        record16.defineProperty("p1", null, false, null);
        record17.defineProperty("p1", null, false, null);
        record17.defineProperty("p2", null, false, null);
        RecordType record18 = new RecordType(null, null, true);
        record18.defineProperty("p1", null, false, null);
        record18.defineProperty("p2", null, false, null);

        assertFalse(record1.isSubtype(record16));
        assertFalse(record1.isSubtype(record17));
        assertFalse(record1.isSubtype(record18));

        RecordType record19 = new RecordType(null, null, true);
        RecordType record20 = new RecordType(null, null, true);
        record19.defineProperty("p1", new ReferenceType(null, null), false, null);
        record20.defineProperty("p1", null, false, null);

        assertFalse(record1.isSubtype(record19));
        assertFalse(record1.isSubtype(record20));
    }
}