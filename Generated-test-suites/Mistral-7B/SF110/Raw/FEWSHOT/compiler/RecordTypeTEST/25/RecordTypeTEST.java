package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

    @Test
    public void testIsSynthetic() {
        RecordType record = new RecordType(
                new JSTypeRegistry(), Maps.newHashMap());
        assertTrue(record.isSynthetic());

        RecordType declaredRecord = new RecordType(
                new JSTypeRegistry(), Maps.newHashMap(), true);
        assertFalse(declaredRecord.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
        RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", prop1);
        properties1.put("prop2", prop2);

        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", prop1);
        properties2.put("prop2", prop2);

        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.WEAK));

        RecordProperty prop3 = new RecordProperty("prop3", registry.getIntType());
        properties2.put("prop3", prop3);

        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.WEAK));

        properties1.put("prop3", prop3);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.WEAK));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertTrue(implicitPrototype instanceof NativeObjectType);
        assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getId());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, Maps.newHashMap());

        String propertyName = "property";
        JSType propertyType = registry.getStringType();

        Node propertyNode = new Node_StringLiteral("property");

        assertTrue(recordType.defineProperty(propertyName, propertyType, false, propertyNode));

        JSType type = recordType.getPropertyType(propertyName);
        assertEquals(propertyType, type);
    }

    @Test
    public void testDefinePropertyFrozen() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, Maps.newHashMap());
        recordType.setFrozen(true);

        String propertyName = "property";
        JSType propertyType = registry.getStringType();

        Node propertyNode = new Node_StringLiteral("property");

        assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
        RecordType recordType2 = new RecordType(registry, Maps.newHashMap());

        RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
        RecordProperty prop2 = new RecordProperty("prop2", registry.getStringType());

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", prop1);
        RecordType recordType1Subtype = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", prop1);
        properties2.put("prop2", prop2);

        RecordType recordType2Subtype = new RecordType(registry, properties2);

        assertTrue(recordType1.getGreatestSubtypeHelper(recordType1Subtype).isEquivalentTo(recordType1Subtype));
        assertTrue(recordType1Subtype.getGreatestSubtypeHelper(recordType1).isEquivalentTo(recordType1));

        UnionType unionType = new UnionTypeBuilder(registry).build();
        recordType1.defineProperty("prop3", unionType, false, null);

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertTrue(greatestSubtype.isRecordType());
        assertEquals(3, greatestSubtype.getOwnPropertyNames().size());
        assertEquals("prop1", greatestSubtype.getPropertyNames().get(0));
        assertEquals("prop2", greatestSubtype.getPropertyNames().get(1));
        assertEquals("prop3", greatestSubtype.getPropertyNames().get(2));

        greatestSubtype = recordType2.getGreatestSubtypeHelper(recordType1);
        assertTrue(greatestSubtype.isRecordType());
        assertEquals(3, greatestSubtype.getOwnPropertyNames().size());
        assertEquals("prop1", greatestSubtype.getPropertyNames().get(0));
        assertEquals("prop2", greatestSubtype.getPropertyNames().get(1));
        assertEquals("prop3", greatestSubtype.getPropertyNames().get(2));

        JSType recordType3 = new RecordType(registry, Maps.newHashMap());
        recordType3.defineProperty("prop1", registry.getNumberType(), false, null);
        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
        assertTrue(greatestSubtype.isRecordType());
        assertEquals(2, greatestSubtype.getOwnPropertyNames().size());
        assertFalse(greatestSubtype.getPropertyNames().contains("prop3"));
    }

    @Test
    public void testGetGreatestSubtypeHelperEmpty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
        RecordType recordType2 = new RecordType(registry, Maps.newHashMap());

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertTrue(registry.getNativeObjectType(JSTypeNative.NO_TYPE)
                .isEquivalentTo(greatestSubtype));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
        RecordProperty prop2 = new RecordProperty("prop2", registry.getIntType());

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", prop1);
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", prop1);
        properties2.put("prop2", prop2);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));

        RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
        recordType3.defineProperty("prop1", registry.getNumberType(), false, null);

        assertFalse(recordType1.isSubtype(recordType3));
        assertFalse(recordType3.isSubtype(recordType1));

        RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
        recordType4.defineProperty("prop1", registry.getStringType(), false, null);
        recordType4.defineProperty("prop2", registry.getIntType(), false, null);

        assertTrue(recordType1.isSubtype(recordType4));
        assertTrue(recordType4.isSubtype(recordType1));
    }
}