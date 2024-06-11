package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Tests for {@link StdValueInstantiator}.
 */
public class StdValueInstantiatorTest {

    private static final String JSON_TEXT = "{\"value\":\"StringValue\"}";

    @Test
    public void testDefaultCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(String.class);

        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class, null);
        assertEquals(deserializer.getClass(), valueInstantiator.getDefaultCreator().getDeclaringClass());
        assertEquals("value", valueInstantiator.getDefaultCreator().getName());

        ValueInstantiator instance = valueInstantiator.createUsingDefault(mapper.getDeserializationContext());
        assertEquals("StringValue", instance.getClass().getMethod("getValue").invoke(instance));
    }

    @Test
    public void testWithArgsCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(WithArgs.class);

        AnnotatedClass valueClassAnnotated = mapper.getTypeFactory().constructType(WithArgs.class);
        AnnotatedConstructor constructor = valueClassAnnotated.getConstructor(String.class);
        AnnotatedWithParams constructorAnnotated = constructor.getParameterAnnotations()[0];

        assertEquals(constructor.getDeclaringClass(), valueInstantiator.getWithArgsCreator().getDeclaringClass());
        assertEquals("constructor", valueInstantiator.getWithArgsCreator().getName());

        ValueInstantiator instance = valueInstantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[]{new String("arg1")});
        assertEquals(new WithArgs("arg1"), instance.getClass().getConstructor(String.class).newInstance(new Object[]{"arg1"}));
    }

    @Test
    public void testDelegateCreator() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(Delegate.class);

        JavaType delegateType = mapper.getTypeFactory().constructType(Delegate.class);
        AnnotatedConstructor constructor = mapper.getTypeFactory().findValueConstructor(Delegate.class, Delegate.class);
        AnnotatedWithParams constructorAnnotated = constructor.getParameterAnnotations()[0];

        assertEquals(delegateType, valueInstantiator.getDelegateType(null));
        assertEquals("delegateCreator", valueInstantiator.getDelegateCreator().getName());

        Object delegateInstance = new Delegate();
        ValueInstantiator instance = valueInstantiator.createUsingDelegate(mapper.getDeserializationContext(), delegateInstance);
        assertEquals(delegateInstance, instance.getClass().getDeclaredField("delegate").get(instance));
    }

    @Test
    public void testArrayDelegateCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(ArrayDelegate.class);

        JavaType arrayDelegateType = mapper.getTypeFactory().constructType(ArrayDelegate.class);
        AnnotatedConstructor constructor = mapper.getTypeFactory().findValueConstructor(ArrayDelegate.class, int[].class);
        AnnotatedWithParams constructorAnnotated = constructor.getParameterAnnotations()[0];

        assertEquals(arrayDelegateType, valueInstantiator.getArrayDelegateType(null));
        assertEquals("arrayDelegateCreator", valueInstantiator.getArrayDelegateCreator().getName());

        int[] array = {1, 2, 3};
        ValueInstantiator instance = valueInstantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new Object[]{array});
        assertEquals(new ArrayDelegate(array), instance.getClass().getConstructor(int[].class).newInstance(new Object[]{array}));
    }

    @Test
    public void testFromStringCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(FromString.class);

        AnnotatedMethod method = ClassUtil.findMethod(FromString.class, "valueOf", String.class);
        AnnotatedWithParams methodAnnotated = method.getParameterAnnotations()[0];

        assertEquals(FromString.class, valueInstantiator.getFromStringCreator().getDeclaringClass());
        assertEquals("valueOf", valueInstantiator.getFromStringCreator().getName());

        ValueInstantiator instance = valueInstantiator.createFromString(mapper.getDeserializationContext(), JSON_TEXT);
        assertEquals(new FromString("StringValue"), instance.getClass().getMethod("getValue").invoke(instance));
    }

    @Test
    public void testFromIntCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(FromInt.class);

        AnnotatedMethod method = ClassUtil.findMethod(FromInt.class, "valueOf", int.class);
        AnnotatedWithParams methodAnnotated = method.getParameterAnnotations()[0];

        assertEquals(FromInt.class, valueInstantiator.getFromIntCreator().getDeclaringClass());
        assertEquals("valueOf", valueInstantiator.getFromIntCreator().getName());

        ValueInstantiator instance = valueInstantiator.createFromInt(mapper.getDeserializationContext(), 1);
        assertEquals(new FromInt(1), instance.getClass().getMethod("getValue").invoke(instance));
    }

    @Test
    public void testFromLongCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(FromLong.class);

        AnnotatedMethod method = ClassUtil.findMethod(FromLong.class, "valueOf", long.class);
        AnnotatedWithParams methodAnnotated = method.getParameterAnnotations()[0];

        assertEquals(FromLong.class, valueInstantiator.getFromLongCreator().getDeclaringClass());
        assertEquals("valueOf", valueInstantiator.getFromLongCreator().getName());

        ValueInstantiator instance = valueInstantiator.createFromLong(mapper.getDeserializationContext(), 1L);
        assertEquals(new FromLong(1L), instance.getClass().getMethod("getValue").invoke(instance));
    }

    @Test
    public void testFromDoubleCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(FromDouble.class);

        AnnotatedMethod method = ClassUtil.findMethod(FromDouble.class, "valueOf", double.class);
        AnnotatedWithParams methodAnnotated = method.getParameterAnnotations()[0];

        assertEquals(FromDouble.class, valueInstantiator.getFromDoubleCreator().getDeclaringClass());
        assertEquals("valueOf", valueInstantiator.getFromDoubleCreator().getName());

        ValueInstantiator instance = valueInstantiator.createFromDouble(mapper.getDeserializationContext(), 1.0);
        assertEquals(new FromDouble(1.0), instance.getClass().getMethod("getValue").invoke(instance));
    }

    @Test
    public void testFromBooleanCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(FromBoolean.class);

        AnnotatedMethod method = ClassUtil.findMethod(FromBoolean.class, "valueOf", boolean.class);
        AnnotatedWithParams methodAnnotated = method.getParameterAnnotations()[0];

        assertEquals(FromBoolean.class, valueInstantiator.getFromBooleanCreator().getDeclaringClass());
        assertEquals("valueOf", valueInstantiator.getFromBooleanCreator().getName());

        ValueInstantiator instance = valueInstantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        assertEquals(new FromBoolean(true), instance.getClass().getMethod("getValue").invoke(instance));
    }

    @Test(expected = JsonMappingException.class)
    public void testFailedToCreate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator valueInstantiator = mapper.findValueInstantiator(FailedToCreate.class);

        ValueInstantiator instance = valueInstantiator.createUsingDefault(mapper.getDeserializationContext());
    }
}