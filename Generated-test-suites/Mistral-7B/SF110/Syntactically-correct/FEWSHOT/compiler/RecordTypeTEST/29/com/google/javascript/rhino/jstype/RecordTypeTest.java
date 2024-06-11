package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import org.junit.Test;

import java.util.Map;

public class RecordTypeTest {

    @Test
    public void testIsSynthetic() {
        RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(), Maps.newHashMap());
        assertTrue(recordType1.isSynthetic());

        RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(), Maps.newHashMap(), true);
        assertFalse(recordType2.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordProperty> propertiesMap1 = Maps.newHashMap();
        propertiesMap1.put("name", new RecordProperty("name", JSType.STRING));
        RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(), propertiesMap1);

        Map<String, RecordProperty> propertiesMap2 = Maps.newHashMap();
        propertiesMap2.put("name", new RecordProperty("name", JSType.STRING));
        propertiesMap2.put("age", new RecordProperty("age", JSType.NUMBER));
        RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(), propertiesMap2);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.PROPERTY_TYPES));
        assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, EquivalenceMethod.PROPERTY_TYPES));

        propertiesMap2.put("age", new RecordProperty("age", JSType.BOOLEAN));
        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.PROPERTY_TYPES));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType = new RecordType(JSTypeRegistry.getInstance(), Maps.newHashMap());
        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertTrue(implicitPrototype.getClass().equals(JSTypeRegistry.getInstance().getNativeObjectType(JSTypeNative.OBJECT_TYPE).getClass()));
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(JSTypeRegistry.getInstance(), Maps.newHashMap());
        String propertyName = "name";
        JSType propertyType = JSType.STRING;
        Node propertyNode = Node.newStringLiteralNode("John Doe");

        assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
        assertFalse(recordType.hasProperty(propertyName));

        recordType.isFrozen = false;

        assertTrue(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
        assertTrue(recordType.hasProperty(propertyName));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = new RecordType(JSTypeRegistry.getInstance(), Maps.newHashMap());
        RecordType recordType2 = new RecordType(JSTypeRegistry.getInstance(), Maps.newHashMap());
        recordType2.defineProperty("name", JSType.STRING, false, Node.newStringLiteralNode("John Doe"));

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertTrue(JSType.isRecordType(grearestSubtype));
        assertTrue(greestSubtype.hasProperty("name"));
        assertEquals(JSType.STRING, greatestSubtype.getPropertyType("name"));
    }
}