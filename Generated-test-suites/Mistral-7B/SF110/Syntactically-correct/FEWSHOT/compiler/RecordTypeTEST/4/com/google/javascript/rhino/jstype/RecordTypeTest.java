package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getNumberType(), null)
        );

        RecordType recordType = new RecordType(registry, properties, true);
        RecordType syntheticRecordType = new RecordType(registry, properties, false);

        assertTrue(recordType.isSynthetic() == false);
        assertTrue(syntheticRecordType.isSynthetic() == true);
    }

    @Test
    public void testRecordTypeProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> properties = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getNumberType(), null)
        );

        RecordType recordType = new RecordType(registry, properties, true);

        Set<String> keys = recordType.getOwnPropertyNames();
        assertTrue(keys.containsAll(properties.keySet()));

        for (String key : keys) {
            JSType type = recordType.getPropertyType(key);
            assertTrue(type.getClass().isAssignableFrom(properties.get(key).getType().getClass()));
        }
    }

    @Test
    public void testRecordTypeEquivalence() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> propertiesA = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getNumberType(), null)
        );
        Map<String, RecordTypeBuilder.RecordProperty> propertiesB = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getInt32Type(), null)
        );

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, RecordType.EquivalenceMethod.NAME_AND_TYPE));

        RecordType recordTypeC = new RecordType(registry, propertiesA, true);
        RecordType recordTypeD = new RecordType(registry, propertiesB, true);

        // RecordType.checkRecordEquivalenceHelper should be transitive.
        assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, RecordType.EquivalenceMethod.NAME_AND_TYPE) &&
                recordTypeC.checkRecordEquivalenceHelper(recordTypeD, RecordType.EquivalenceMethod.NAME_AND_TYPE) &&
                recordTypeA.checkRecordEquivalenceHelper(recordTypeD, RecordType.EquivalenceMethod.NAME_AND_TYPE));
    }

    @Test
    public void testRecordTypeGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> propertiesA = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getNumberType(), null)
        );
        Map<String, RecordTypeBuilder.RecordProperty> propertiesB = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getNumberType().getGreatestSubtype(registry.getInt32Type()), null)
        );

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertNotNull(greastestSubtype);
        assertTrue(greastestSubtype.getGreatestSubtype(registry.getInt32Type()).isEmptyType());

        propertiesB.put("bar", new RecordTypeBuilder.RecordProperty(registry.getInt32Type(), null));
        RecordType recordTypeC = new RecordType(registry, propertiesB, true);

        greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
        assertNull(greastestSubtype);
    }

    @Test
    public void testRecordTypeSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> propertiesA = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getNumberType(), null)
        );
        Map<String, RecordTypeBuilder.RecordProperty> propertiesB = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getInt32Type(), null)
        );

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        assertFalse(recordTypeA.isSubtype(recordTypeB));

        RecordType recordTypeC = new RecordType(registry, propertiesA, true);
        RecordType recordTypeD = new RecordType(registry, propertiesB, false);

        assertTrue(recordTypeC.isSubtype(recordTypeD));
    }

    @Test
    public void testRecordTypeSubtypeTransitivity() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> propertiesA = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getNumberType(), null)
        );
        Map<String, RecordTypeBuilder.RecordProperty> propertiesB = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getInt32Type(), null)
        );
        Map<String, RecordTypeBuilder.RecordProperty> propertiesC = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getBooleanType(), null)
        );

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);
        RecordType recordTypeC = new RecordType(registry, propertiesC, true);

        // RecordType.isSubtype should be transitive.
        assertTrue(recordTypeA.isSubtype(recordTypeC) &&
                recordTypeA.isSubtype(recordTypeB) &&
                recordTypeB.isSubtype(recordTypeC));
    }

    @Test
    public void testRecordTypeSubtypeWithUnionTypes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordTypeBuilder.RecordProperty> propertiesA = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getNumberType(), null)
        );
        Map<String, RecordTypeBuilder.RecordProperty> propertiesB = ImmutableMap.of(
            "foo", new RecordTypeBuilder.RecordProperty(registry.getStringType(), null),
            "bar", new RecordTypeBuilder.RecordProperty(registry.getBooleanType(), null)
        );

        RecordType recordTypeA = new RecordType(registry, propertiesA, true);
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        // RecordType.isSubtype should handle union types correctly.
        JSType greatestSubtype = new UnionTypeBuilder(registry)
                .addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
                .addAlternate(registry.getNativeObjectType(JSTypeNative.NULL_TYPE))
                .build();

        UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
        RecordType recordTypeC = new RecordType(registry, propertiesB, false);
        unionTypeBuilder.addAlternate(recordTypeC);
        JSType greatestSubtypeC = unionTypeBuilder.build();

        assertTrue(recordTypeA.isSubtype(greatestSubtypeC));
    }
}