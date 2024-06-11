package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("x", new RecordProperty("x", registry.stringType()));
        properties.put("y", new RecordProperty("y", registry.numberType()));

        RecordType recordType = new RecordType(registry, properties, true);
        RecordType syntheticRecordType = new RecordType(registry, properties, false);

        assertEquals(recordType.getClass(), RecordType.class);
        assertEquals(syntheticRecordType.getClass(), RecordType.class);
        assertEquals(recordType.isSynthetic(), false);
        assertEquals(syntheticRecordType.isSynthetic(), true);

        assertNull(recordType.getImplicitPrototype());
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
                recordType.getImplicitPrototype());

        assertEquals(recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)), recordType);
        assertEquals(recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE)), recordType);
        assertNotEquals(recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.STRING_TYPE)), recordType);

        assertEquals(recordType, recordType.toMaybeRecordType());
        assertNotEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.toMaybeRecordType());

        // Check subtyping
        RecordType subType = new RecordType(registry, properties, true);
        assertTrue(recordType.isSubtype(subType));
        assertFalse(subType.isSubtype(recordType));

        // Check equivalent types
        RecordType equivalentType = new RecordType(registry, properties, true);
        assertEquals(recordType, equivalentType);
        assertEquals(recordType.hashCode(), equivalentType.hashCode());

        // Check synthesized record type
       RecordType syntheticEquivalentType = new RecordType(registry, properties, false);
        assertEquals(recordType, syntheticEquivalentType);
        assertEquals(recordType.hashCode(), syntheticEquivalentType.hashCode());

        // Check frozen record type
        RecordType frozenRecordType = new RecordType(registry, properties, true);
        frozenRecordType.freeze();
        assertFalse(frozenRecordType.defineProperty("x", registry.booleanType(), false, null));

        // Check record equivalence
        RecordType otherRecord = new RecordType(registry, properties, true);
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        RecordType equivalentRecord = builder.setSynthesized(true)
                .addProperty("x", registry.stringType(), null)
                .addProperty("y", registry.numberType(), null)
                .build();
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, RecordType.EQUIVALENCE_STRICT));
        assertTrue(recordType.checkRecordEquivalenceHelper(equivalentRecord, RecordType.EQUIVALENCE_STRICT));
        assertFalse(recordType.checkRecordEquivalenceHelper(new RecordType(registry, Arrays.asList("x", "y"), null), RecordType.EQUIVALENCE_STRICT));

        // Check record type properties
        assertFalse(recordType.defineProperty("z", registry.stringType(), true, null));
        assertEquals(properties.size(), recordType.getOwnPropertyNames().size());
        assertEquals(properties.get("x").getPropertyName(), recordType.getPropertyNames()[0]);
        assertEquals(properties.get("y").getPropertyName(), recordType.getPropertyNames()[1]);
    }
}