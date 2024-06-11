package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link StdValueInstantiator}.
 */
public class StdValueInstantiatorTest {

    @Test
    public void testDefaultConstruction() throws Exception {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, null);
        assertEquals("UNKNOWN TYPE", instantiator.getValueTypeDesc());
        assertNotNull(instantiator.getValueClass());
    }

    @Test
    public void testCustomConstruction() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(String.class));
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertNotNull(instantiator.getValueClass());
    }

    @Test
    public void testCopyConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator original = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(String.class));

        StdValueInstantiator copy = new StdValueInstantiator(original);
        assertEquals(original.getValueTypeDesc(), copy.getValueTypeDesc());
        assertEquals(original.getValueClass(), copy.getValueClass());
        assertEquals(original._defaultCreator, copy._defaultCreator);
        assertEquals(original._constructorArguments, copy._constructorArguments);
        assertEquals(original._delegateType, copy._delegateType);
        assertEquals(original._delegateCreator, copy._delegateCreator);
        assertEquals(original._delegateArguments, copy._delegateArguments);
        assertEquals(original._arrayDelegateType, copy._arrayDelegateType);
        assertEquals(original._arrayDelegateCreator, copy._arrayDelegateCreator);
        assertEquals(original._arrayDelegateArguments, copy._arrayDelegateArguments);
        assertEquals(original._fromStringCreator, copy._fromStringCreator);
        assertEquals(original._fromIntCreator, copy._fromIntCreator);
        assertEquals(original._fromLongCreator, copy._fromLongCreator);
        assertEquals(original._fromDoubleCreator, copy._fromDoubleCreator);
        assertEquals(original._fromBooleanCreator, copy._fromBooleanCreator);
        assertEquals(original._incompleteParameter, copy._incompleteParameter);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(String.class));
        instantiator.configureFromObjectSettings(mapper.findValueInstantiator(String.class), null, null, null, null, null);
        String value = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertEquals("", value);
    }

    @Test
    public void testDelegateCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(String[].class));
        instantiator.configureFromArraySettings(mapper.findValueInstantiator(String[].class), mapper.getTypeFactory().constructType(String[].class),
                new SettableBeanProperty[] { new SettableBeanProperty.Std("0", null), new SettableBeanProperty.Std("1", null), new SettableBeanProperty.Std("2", null) });
        String[] values = instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new String[] { "one", "two", "three" });
        assertEquals("one", values[0]);
        assertEquals("two", values[1]);
        assertEquals("three", values[2]);
    }

    @Test
    public void testFromStringCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(BigInteger.class));
        instantiator.configureFromStringCreator(mapper.findValueInstantiator(BigInteger.class, "_fromString"));
        BigInteger value = instantiator.createFromString(mapper.getDeserializationContext(), "42");
        assertEquals("42", value.toString());
    }

    @Test
    public void testFromIntCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(Integer.class));
        instantiator.configureFromIntCreator(mapper.findValueInstantiator(Integer.class, "_fromInt"));
        Integer value = instantiator.createFromInt(mapper.getDeserializationContext(), 42);
        assertEquals(42, value.intValue());
    }

    @Test
    public void testFromLongCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(Long.class));
        instantiator.configureFromLongCreator(mapper.findValueInstantiator(Long.class, "_fromLong"));
        Long value = instantiator.createFromLong(mapper.getDeserializationContext(), 42L);
        assertEquals(42L, value.longValue());
    }

    @Test
    public void testFromDoubleCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(Double.class));
        instantiator.configureFromDoubleCreator(mapper.findValueInstantiator(Double.class, "_fromDouble"));
        Double value = instantiator.createFromDouble(mapper.getDeserializationContext(), 42.0d);
        assertEquals(42.0d, value.doubleValue());
    }

    @Test
    public void testFromBooleanCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                mapper.getTypeFactory().constructType(Boolean.class));
        instantiator.configureFromBooleanCreator(mapper.findValueInstantiator(Boolean.class, "_fromBoolean"));
        Boolean value = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertTrue(value.booleanValue());
    }

    @Test
    public void testIncompleteParameter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), mapper.getTypeFactory().constructType(String[].class));
        instantiator.configureIncompleteParameter(mapper.findParameter(_ValueInstantiator.class, "_incompleteParameter"));
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> instantiator.createUsingDefault(mapper.getDeserializationContext()));
        assertEquals("No value present to instantiate Java class [class java.lang.String]", exception.getMessage());
    }
}