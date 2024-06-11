package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Tests for {@link StdValueInstantiator} class.
 */
public class StdValueInstantiatorTest {

    private ObjectMapper mapper;
    private StdValueInstantiator instantiator;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        instantiator = mapper.findValueInstantiator(String.class);
    }

    /**
     * Test for default constructor.
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        Object result = instantiator.createUsingDefault(mapper.getDeserializationContext());
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    /**
     * Test for constructor with arguments.
     */
    @Test
    public void testConstructorWithArguments() throws IOException {
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreatorAnnotation(String.class);
        Method method = annotatedClass.getDeclaredMethod("valueOf", new TypeReference<CharSequence>() {}.getType());

        ObjectNode node = mapper.createObjectNode();
        node.put("value", "test");

        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{node});
        assertNotNull(result);
        assertEquals("test", result);
    }

    /**
     * Test for delegate constructor.
     */
    @Test
    public void testDelegateConstructor() throws IOException {
        JavaType delegateType = mapper.getTypeFactory().constructType(String[].class);
        AnnotatedClass annotatedClass = AnnotationIntrospector.findCreatorAnnotation(String[].class);
        Method method = annotatedClass.getDeclaredMethod("newValueArray", new Class[0]);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[test1, test2]");

        Object[] args = new Object[]{node};
        Object result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), args[0]);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        assertEquals(2, ((String[]) result).length);
        assertEquals("test1", ((String[]) result)[0]);
        assertEquals("test2", ((String[]) result)[1]);
    }

    /**
     * Test for failure when default constructor is not available.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoDefaultConstructor() throws IOException {
        instantiator = mapper.findValueInstantiator(MyClass.class);
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    /**
     * Test for failure when constructor with arguments is not available.
     */
    @Test(expected = IllegalStateException.class)
    public void testNoConstructorWithArguments() throws IOException {
        JavaType delegateType = mapper.getTypeFactory().constructType(MyClass.class);
        instantiator = mapper.findValueInstantiator(delegateType);
        instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[0]);
    }

    /**
     * Test for failure when delegate constructor is not available.
     */
    @Test(expected = IllegalStateException.class)
    public void testNoDelegateConstructor() throws IOException {
        instantiator = mapper.findValueInstantiator(MyClass.class);
        instantiator.createUsingDelegate(mapper.getDeserializationContext(), new Object[0]);
    }

    /**
     * Test for failure when annotated constructor is not available.
     */
    @Test(expected = IllegalStateException.class)
    public void testNoAnnotatedConstructor() throws IOException {
        AnnotatedClass annotatedClass = mapper.findClassAnnotations(MyClass.class, ValueInstantiator.ValueClassAnnotated.class);
        instantiator = mapper.findValueInstantiator(annotatedClass.getRawClass());
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    /**
     * Test for failure when delegate annotated constructor is not available.
     */
    @Test(expected = IllegalStateException.class)
    public void testNoDelegateAnnotatedConstructor() throws IOException {
        AnnotatedClass annotatedClass = mapper.findClassAnnotations(MyClass.class, ValueInstantiator.ValueClassAnnotated.class);
        instantiator = mapper.findValueInstantiator(annotatedClass.getRawClass());
        JavaType delegateType = mapper.getTypeFactory().constructType(MyClass[].class);
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[test1, test2]");
        instantiator.createUsingDelegate(mapper.getDeserializationContext(), node);
    }

    /**
     * Test for failure when creator method is not public.
     */
    @Test(expected = IllegalStateException.class)
    public void testPrivateCreator() throws IOException {
        class MyPrivateClass {
            @JsonCreator
            private MyPrivateClass(@JsonProperty("value") int value) {}
        }

        instantiator = mapper.findValueInstantiator(MyPrivateClass.class);
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    /**
     * Test for failure when creator method does not have a valid constructor signature.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreatorSignature() throws IOException {
        class MyInvalidClass {
            @JsonDeserialize(using = MyInvalidValueInstantiator.class)
            static class Inner {
                private Inner() {}
            }

            @JsonCreator
            public MyInvalidClass(@JsonProperty("value") String value) {
                new Inner();
            }
        }

        instantiator = mapper.findValueInstantiator(MyInvalidClass.class);
        instantiator.createUsingDefault(mapper.getDeserializationContext());
    }

    private static class MyClass {
        public MyClass() {}
    }
}