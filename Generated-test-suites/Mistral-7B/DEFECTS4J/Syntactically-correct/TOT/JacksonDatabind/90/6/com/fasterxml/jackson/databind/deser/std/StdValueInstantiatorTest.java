package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StdValueInstantiatorTest {

    private static final String TEST_TYPE = "com.example.TestClass";

    @Test
    public void testCreateUsingDefault() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                JavaType.construct(TEST_TYPE));
        assertEquals(TEST_TYPE, instantiator.getValueTypeDesc());
        assertNotNull(instantiator.createUsingDefault(mapper.getDeserializationContext()));
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = JavaType.construct(TestClassWithArgs.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), javaType);
        TestClassWithArgs testClassWithArgs = new TestClassWithArgs("name");
        instantiator.configureFromObjectSettings(Mockito.mock(AnnotatedWithParams.class),
                Mockito.mock(AnnotatedWithParams.class), null, null,
                Mockito.mock(AnnotatedWithParams.class), new SettableBeanProperty[]{
                        new SettableBeanProperty.Std(TestClassWithArgs.class, "name")});
        Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(),
                new Object[]{testClassWithArgs.getName()});
        assertEquals(testClassWithArgs, result);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedField testField = Mockito.mock(AnnotatedField.class);
        AnnotatedClass testClass = Mockito.mock(AnnotatedClass.class);
        when(testClass.getDeclaredConstructors()).thenReturn(new AnnotatedMethod[0]);
        when(testClass.findMethod(Mockito.eq("methodWithArgs"))).thenReturn(
                Mockito.mock(AnnotatedMethod.class));
        when(testClass.findValueDeserializer(Mockito.eq(TestClass.class))).thenReturn(null);
        when(testClass.findValueDeserializer(Mockito.eq(TestClassWithArgs[].class))).thenReturn(
                Mockito.mock(ValueDeserializer.class));
        AnnotatedWithParams constructorCreator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[]{
                new SettableBeanProperty.Std(TestClass.class, "field1"),
                new SettableBeanProperty.Std(TestClass.class, "field2")};
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                JavaType.construct(TestClass.class));
        instantiator.configureFromObjectSettings(constructorCreator, null, null, constructorArguments,
                null, null);
        Object delegate = mock(TestClass.class);
        Object result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), delegate);
        Method methodWithArgs = TestClass.class.getDeclaredMethod("methodWithArgs", TestClassWithArgs[].class);
        methodWithArgs.setAccessible(true);
        TestClassWithArgs[] testClassWithArgs = new TestClassWithArgs[]{new TestClassWithArgs("name1"),
                new TestClassWithArgs("name2")};
        Object[] delegateArguments = new Object[]{delegate, testClassWithArgs};
        assertEquals(methodWithArgs.invoke(delegate, testClassWithArgs), result);
    }

    @Test
    public void testCreateFromString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                JavaType.construct(String.class));
        instantiator.configureFromStringCreator(Mockito.mock(AnnotatedWithParams.class));
        String testString = "test";
        assertEquals(String.class, instantiator.getValueClass());
        assertEquals(testString, instantiator.createFromString(mapper.getDeserializationContext(), testString));
    }

    @Test
    public void testCreateFromInt() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                JavaType.construct(Integer.class));
        instantiator.configureFromIntCreator(Mockito.mock(AnnotatedWithParams.class));
        assertEquals(Integer.class, instantiator.getValueClass());
        assertEquals(42, instantiator.createFromInt(mapper.getDeserializationContext(), 42));
    }

    @Test
    public void testCreateFromLong() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                JavaType.construct(Long.class));
        instantiator.configureFromLongCreator(Mockito.mock(AnnotatedWithParams.class));
        assertEquals(Long.class, instantiator.getValueClass());
        assertEquals(42L, instantiator.createFromLong(mapper.getDeserializationContext(), 42L));
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                JavaType.construct(Double.class));
        instantiator.configureFromDoubleCreator(Mockito.mock(AnnotatedWithParams.class));
        assertEquals(Double.class, instantiator.getValueClass());
        assertEquals(3.14, instantiator.createFromDouble(mapper.getDeserializationContext(), 3.14), 0.0);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                JavaType.construct(Boolean.class));
        instantiator.configureFromBooleanCreator(Mockito.mock(AnnotatedWithParams.class));
        assertEquals(Boolean.class, instantiator.getValueClass());
        assertEquals(true, instantiator.createFromBoolean(mapper.getDeserializationContext(), true));
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = JavaType.construct(int[].class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), javaType);
        instantiator.configureFromArraySettings(Mockito.mock(AnnotatedWithParams.class), javaType, null);
        int[] ints = new int[]{1, 2, 3};
        assertEquals(int[].class, instantiator.getValueClass());
        assertEquals(ints, instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), ints));
    }

    @Test
    public void testCreateUsingDelegateWithIncompleteParameter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedParameter incompleteParameter = Mockito.mock(AnnotatedParameter.class);
        StdValueInstantiator instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(),
                JavaType.construct(IncompleteClass.class));
        instantiator.configureIncompleteParameter(incompleteParameter);
        IncompleteClass instance = new IncompleteClass();
        instance.setField1("field1");
        assertEquals(IncompleteClass.class, instantiator.getValueClass());
        assertEquals(instance, instantiator.createUsingDelegate(mapper.getDeserializationContext(), null));
    }

    private static class TestClass {
        String field1;
        String field2;

        TestClass() {
        }

        void methodWithArgs(TestClassWithArgs[] args) {
            for (TestClassWithArgs testClassWithArgs : args) {
                assertEquals(testClassWithArgs.getName(), testClassWithArgs.getName());
            }
        }
    }

    private static class TestClassWithArgs {
        String name;

        TestClassWithArgs(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class IncompleteClass {
        String field1;

        public void setField1(String field1) {
            this.field1 = field1;
        }
    }
}