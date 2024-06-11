package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiator() throws Exception {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);

        // Test getValueClass
        assertEquals("java.lang.String", valueInstantiator.getValueClass().getName());

        // Test getValueTypeDesc
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());

        // Test canInstantiate
        assertFalse(valueInstantiator.canInstantiate());

        // Test canCreateUsingDefault
        assertFalse(valueInstantiator.canCreateUsingDefault());

        // Test canCreateUsingDelegate
        assertFalse(valueInstantiator.canCreateUsingDelegate());

        // Test canCreateFromObjectWith
        assertFalse(valueInstantiator.canCreateFromObjectWith());

        // Test createUsingDefault
        assertNull(valueInstantiator.createUsingDefault(null));

        // Test createFromObjectWith
        ObjectNode objectNode = ObjectMapper.getDefaultInstance().createObjectNode();
        objectNode.put("field1", "value1");
        assertNull(valueInstantiator.createFromObjectWith(null, objectNode.fields()));

        // Test createUsingDelegate
        assertNull(valueInstantiator.createUsingDelegate(null, null));

        // Test createFromString
        assertNull(valueInstantiator.createFromString(null, ""));
        assertNull(valueInstantiator.createFromString(null, "null"));
        assertNull(valueInstantiator.createFromString(null, "true"));
        assertNull(valueInstantiator.createFromString(null, "false"));
        String stringValue = new String("test");
        assertEquals(stringValue, valueInstantiator.createFromString(null, stringValue));

        // Test createFromInt
        assertNull(valueInstantiator.createFromInt(null, 0));

        // Test createFromLong
        assertNull(valueInstantiator.createFromLong(null, 0L));

        // Test createFromDouble
        assertNull(valueInstantiator.createFromDouble(null, 0.0));

        // Test createFromBoolean
        assertNull(valueInstantiator.createFromBoolean(null, false));

        // Test getDefaultCreator
        assertNull(valueInstantiator.getDefaultCreator());

        // Test getDelegateCreator
        assertNull(valueInstantiator.getDelegateCreator());

        // Test getArrayDelegateCreator
        assertNull(valueInstantiator.getArrayDelegateCreator());

        // Test getWithArgsCreator
        assertNull(valueInstantiator.getWithArgsCreator());

        // Test getIncompleteParameter
        assertNull(valueInstantiator.getIncompleteParameter());

        // Test StdValueInstantiator
        ValueInstantiator stdValueInstantiator = new StdStringValueInstantiator();

        // Test getValueClass
        assertEquals("java.lang.String", stdValueInstantiator.getValueClass().getName());

        // Test getValueTypeDesc
        assertEquals("java.lang.String", stdValueInstantiator.getValueTypeDesc());

        // Test canInstantiate
        assertTrue(stdValueInstantiator.canInstantiate());

        // Test canCreateUsingDefault
        assertTrue(stdValueInstantiator.canCreateUsingDefault());

        // Test canCreateUsingDelegate
        assertFalse(stdValueInstantiator.canCreateUsingDelegate());

        // Test canCreateFromObjectWith
        assertTrue(stdValueInstantiator.canCreateFromObjectWith());

        // Test createUsingDefault
        assertEquals("", stdValueInstantiator.createUsingDefault(null));

        // Test createFromObjectWith
        ObjectNode objectNode2 = ObjectMapper.getDefaultInstance().createObjectNode();
        objectNode2.put("field1", "value1");
        assertEquals("value1", stdValueInstantiator.createFromObjectWith(null, objectNode2.fields()));

        // Test createUsingDelegate
        assertNull(stdValueInstantiator.createUsingDelegate(null, null));

        // Test createFromString
        assertEquals("test", stdValueInstantiator.createFromString(null, "test"));

        // Test createFromInt
        assertNull(stdValueInstantiator.createFromInt(null, 0));

        // Test createFromLong
        assertNull(stdValueInstantiator.createFromLong(null, 0L));

        // Test createFromDouble
        assertNull(stdValueInstantiator.createFromDouble(null, 0.0));

        // Test createFromBoolean
        assertNull(stdValueInstantiator.createFromBoolean(null, false));

        // Test getDefaultCreator
        AnnotatedMethod defaultCreator = new AnnotatedMethod(String.class.getDeclaredMethod("valueOf", String.class), null);
        assertEquals(defaultCreator, stdValueInstantiator.getDefaultCreator());

        // Test getDelegateCreator
        assertNull(stdValueInstantiator.getDelegateCreator());

        // Test getArrayDelegateCreator
        assertNull(stdValueInstantiator.getArrayDelegateCreator());

        // Test getWithArgsCreator
        assertNull(stdValueInstantiator.getWithArgsCreator());

        // Test getIncompleteParameter
        assertNull(stdValueInstantiator.getIncompleteParameter());
    }

    static class StdStringValueInstantiator extends ValueInstantiator.StdValueInstantiator<String> {
        @JsonCreator
        public static String valueOf(String value) {
            return value;
        }
    }
}