package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.ValueDeserializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test for {@link StdValueInstantiator} class.
 */
public class StdValueInstantiatorTest {

    @Test
    public void testStdValueInstantiator() throws IOException, IllegalAccessException,
            InstantiationException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test default constructor
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                String.class);
        assertEquals("UNKNOWN TYPE", instantiator.getValueTypeDesc());
        assertNull(instantiator.getDefaultCreator());

        // Test constructor with JavaType
        instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), String.class.getType());
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertNull(instantiator.getDefaultCreator());

        // Test copy constructor
        AnnotatedClass ac = mapper.getTypeFactory().constructType(MyClass.class);
        StdValueInstantiator src = new StdValueInstantiator(mapper.getDeserializationConfig(), ac);
        StdValueInstantiator dest = new StdValueInstantiator(src);
        assertEquals(src.getValueTypeDesc(), dest.getValueTypeDesc());
        assertEquals(src.getDefaultCreator(), dest.getDefaultCreator());
        assertEquals(src.getWithArgsCreator(), dest.getWithArgsCreator());
        assertEquals(src.getDelegateCreator(), dest.getDelegateCreator());
        assertEquals(src.getArrayDelegateCreator(), dest.getArrayDelegateCreator());
        assertEquals(src.getFromStringCreator(), dest.getFromStringCreator());
        assertEquals(src.getFromIntCreator(), dest.getFromIntCreator());
        assertEquals(src.getFromLongCreator(), dest.getFromLongCreator());
        assertEquals(src.getFromDoubleCreator(), dest.getFromDoubleCreator());
        assertEquals(src.getFromBooleanCreator(), dest.getFromBooleanCreator());
        assertEquals(src.getIncompleteParameter(), dest.getIncompleteParameter());

        // Test configure methods
        JavaType delegateType = mapper.getTypeFactory().constructType(MyDelegateClass.class);
        instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), MyClass.class);
        instantiator.configureFromObjectSettings(null, null, null, null,
                null, null);
        instantiator.configureFromArraySettings(null, delegateType, new SettableBeanProperty[0]);
        instantiator.configureFromStringCreator(null);
        instantiator.configureFromIntCreator(null);
        instantiator.configureFromLongCreator(null);
        instantiator.configureFromDoubleCreator(null);
        instantiator.configureFromBooleanCreator(null);

        AnnotatedConstructor constructor = ac.getDeclaredConstructors()[0];
        AnnotatedParameter[] parameters = constructor.getParameters();
        ValueDeserializerProvider vdp = mapper.getValueDeserializerProvider();
        ValueDeserializer fromStringDeserializer = vdp.findValueDeserializer(mapper.getTypeFactory()
                .constructType(String.class), null);
        ValueDeserializer fromIntDeserializer = vdp.findValueDeserializer(mapper.getTypeFactory()
                .constructType(Integer.class), null);
        ValueDeserializer fromLongDeserializer = vdp.findValueDeserializer(mapper.getTypeFactory()
                .constructType(Long.class), null);
        ValueDeserializer fromDoubleDeserializer = vdp.findValueDeserializer(mapper.getTypeFactory()
                .constructType(Double.class), null);
        ValueDeserializer fromBooleanDeserializer = vdp.findValueDeserializer(mapper.getTypeFactory()
                .constructType(Boolean.class), null);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().findTypeDeserializer(MyClass, null);

        instantiator.configureFromObjectSettings(new AnnotatedWithParams(constructor, null, null),
                null, null, new SettableBeanProperty[0],
                new AnnotatedWithParams(constructor, fromStringDeserializer, null),
                new SettableBeanProperty[] { parameters[0] });
        instantiator.configureFromArraySettings(new AnnotatedWithParams(constructor, null, typeDeserializer),
                delegateType, new SettableBeanProperty[0]);
        instantiator.configureFromStringCreator(new AnnotatedWithParams(constructor, fromStringDeserializer, null));
        instantiator.configureFromIntCreator(new AnnotatedWithParams(constructor, fromIntDeserializer, null));
        instantiator.configureFromLongCreator(new AnnotatedWithParams(constructor, fromLongDeserializer, null));
        instantiator.configureFromDoubleCreator(new AnnotatedWithParams(constructor, fromDoubleDeserializer, null));
        instantiator.configureFromBooleanCreator(new AnnotatedWithParams(constructor, fromBooleanDeserializer, null));

        // Test getters
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertEquals(String.class, instantiator.getValueClass());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertEquals(MyClass.class, instantiator.getDelegateType(mapper.getDeserializationConfig()).getRawClass());
        assertNull(instantiator.getArrayDelegateType(mapper.getDeserializationConfig()));
        assertArrayEquals(new SettableBeanProperty[0], instantiator.getFromObjectArguments(mapper.getDeserializationContext()));

        // Test createUsingDefault
        Object result = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertInstanceOf(MyClass.class, result);

        // Test createFromObjectWith
        Object[] args = new Object[] { "test" };
        result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), args);
        assertInstanceOf(MyClass.class, result);
        assertEquals("test", result.toString());

        // Test createUsingDelegate
        MyDelegateClass delegate = mapper.readerForType(MyDelegateClass.class).readValue(mapper.createObjectTextReader("{\"name\": \"test\"}"));
        result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
        assertInstanceOf(MyClass.class, result);

        // Test createUsingArrayDelegate
        result = instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), delegate);
        assertInstanceOf(MyClass.class, result);

        // Test createFromString
        result = instantiator.createFromString(mapper.getDeserializationContext(), "test");
        assertInstanceOf(MyClass.class, result);
        assertEquals("test", result.toString());

        // Test createFromInt
        result = instantiator.createFromInt(mapper.getDeserializationContext(), 1);
        assertInstanceOf(MyClass.class, result);
        assertEquals("1", result.toString());

        // Test createFromLong
        result = instantiator.createFromLong(mapper.getDeserializationContext(), 1L);
        assertInstanceOf(MyClass.class, result);
        assertEquals("1", result.toString());

        // Test createFromDouble
        result = instantiator.createFromDouble(mapper.getDeserializationContext(), 1.0);
        assertInstanceOf(MyClass.class, result);
        assertEquals("1", result.toString());

        // Test createFromBoolean
        result = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertInstanceOf(MyClass.class, result);
        assertEquals("true", result.toString());
    }

    static class MyClass {
        @JsonProperty("name")
        private String name;

        @JsonCreator
        public MyClass(@JsonProperty("name") String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class MyDelegateClass {
        @JsonProperty("name")
        private String name;

        @JsonCreator
        public MyDelegateClass(@JsonProperty("name") String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}