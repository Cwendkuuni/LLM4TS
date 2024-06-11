package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper(new JsonFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDefaultCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
        assertEquals("Default creator test", instantiator.createUsingDefault(mapper.getDeserializationContext()));
    }

    @Test
    public void testDelegateCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(String[].class);
        String[] args = new String[]{"test1", "test2"};
        assertEquals(args, instantiator.createUsingDelegate(mapper.getDeserializationContext(), new String[0]));
    }

    @Test
    public void testDelegateCreatorWithArguments() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(StringBuffer.class);
        assertEquals("", instantiator.createUsingDelegate(mapper.getDeserializationContext(), null));
        StringBuffer expected = new StringBuffer("test");
        assertEquals(expected, instantiator.createUsingDelegate(mapper.getDeserializationContext(), "test"));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingDefaultCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingDelegateCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyClass.class);
        instantiator.createUsingDelegate(mapper.getDeserializationContext(), new Object());
    }

    @Test
    public void testFromStringCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
        instantiator.configureFromStringCreator(mapper.getDeserializationConfig().findValueInstantiator(Integer.class));
        assertEquals(1, instantiator.createFromString(mapper.getDeserializationContext(), "1"));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedFromStringCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
        instantiator.configureFromStringCreator(mapper.getDeserializationConfig().findValueInstantiator(Boolean.class));
        instantiator.createFromString(mapper.getDeserializationContext(), "true"); // should fail
    }

    @Test
    public void testFromIntCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
        instantiator.configureFromIntCreator(mapper.getDeserializationConfig().findValueInstantiator(Integer.class));
        assertEquals(2, instantiator.createFromInt(mapper.getDeserializationContext(), 2));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedFromIntCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Integer.class);
        instantiator.configureFromIntCreator(mapper.getDeserializationConfig().findValueInstantiator(String.class));
        instantiator.createFromInt(mapper.getDeserializationContext(), "2"); // should fail
    }

    @Test
    public void testFromLongCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Long.class);
        instantiator.configureFromLongCreator(mapper.getDeserializationConfig().findValueInstantiator(Long.class));
        assertEquals(3L, instantiator.createFromLong(mapper.getDeserializationContext(), 3L));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedFromLongCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Long.class);
        instantiator.configureFromLongCreator(mapper.getDeserializationConfig().findValueInstantiator(String.class));
        instantiator.createFromLong(mapper.getDeserializationContext(), "3"); // should fail
    }

    @Test
    public void testFromDoubleCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Double.class);
        instantiator.configureFromDoubleCreator(mapper.getDeserializationConfig().findValueInstantiator(Double.class));
        assertEquals(3.0, instantiator.createFromDouble(mapper.getDeserializationContext(), 3.0), 0.01);
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedFromDoubleCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Double.class);
        instantiator.configureFromDoubleCreator(mapper.getDeserializationConfig().findValueInstantiator(String.class));
        instantiator.createFromDouble(mapper.getDeserializationContext(), "3.0"); // should fail
    }

    @Test
    public void testFromBooleanCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
        instantiator.configureFromBooleanCreator(mapper.getDeserializationConfig().findValueInstantiator(Boolean.class));
        assertTrue(instantiator.createFromBoolean(mapper.getDeserializationContext(), true));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedFromBooleanCreator() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(Boolean.class);
        instantiator.configureFromBooleanCreator(mapper.getDeserializationConfig().findValueInstantiator(String.class));
        instantiator.createFromBoolean(mapper.getDeserializationContext(), "true"); // should fail
    }

    @Test
    public void testIncompleteParameter() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyClass.class);
        instantiator.configureIncompleteParameter(mapper.findParameter(MyClass.class, "param"));
        MyClass obj = new MyClass();
        obj.setParam(mapper.getValueFactory().valueOf(42));
        assertEquals(obj, instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[0]), 0);
    }

    @Test
    public void testNullIncompleteParameter() throws IOException {
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyClass.class);
        instantiator.configureIncompleteParameter(null);
        assertNull(instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[0]));
    }

    static class MyClass {
        private int param;

        public int getParam() {
            return param;
        }

        public void setParam(Object value) {
            param = ((Number) value).intValue();
        }
    }
}