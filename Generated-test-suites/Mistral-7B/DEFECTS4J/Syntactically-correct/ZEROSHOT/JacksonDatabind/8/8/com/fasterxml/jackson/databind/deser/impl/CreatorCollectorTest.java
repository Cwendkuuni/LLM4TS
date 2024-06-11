package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.CreatorProperty;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for CreatorCollector.java
 */
@RunWith(MockitoJUnitRunner.class)
public class CreatorCollectorTest {

    private BeanDescription beanDesc;
    private CreatorCollector creatorCollector;
    private DeserializationConfig config;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        config = new DeserializationConfig();
        objectMapper = new ObjectMapper(config);

        Class<?> testClass = getClass();
        BeanDescription testBeanDescription = JacksonAnnotationIntrospector.findBeanDescription(testClass, config);
        creatorCollector = new CreatorCollector(testBeanDescription, true);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        AnnotatedWithParams defaultCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(defaultCreator);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        JavaType type = objectMapper.getTypeFactory().constructType(getClass());

        assertNotNull(instantiator);
        assertSame(instantiator.getClass(), Vanilla.class);
        assertEquals(type.getRawClass().getName(), instantiator.getValueTypeDesc());
    }

    @Test
    public void testStringCreator() throws Exception {
        String testString = "testString";
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertEquals(String.class.getName(), instantiator.getValueTypeDesc());
        assertEquals(String.class, instantiator.getType());
        assertNotNull(instantiator.getValueType());
        assertSame(instantiator.getValueType().toString(), testString);

        Method createUsingStringMethod = instantiator.getClass().getMethod("createUsingString");
        assertEquals(String.class, createUsingStringMethod.getReturnType());
        assertEquals(testString, createUsingStringMethod.invoke(instantiator));
    }

    @Test
    public void testIntCreator() throws Exception {
        int testInt = 1;
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(intCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertEquals(Integer.TYPE.getName(), instantiator.getValueTypeDesc());
        assertEquals(Integer.TYPE, instantiator.getType());
        assertNotNull(instantiator.getValueType());
        assertEquals(testInt, instantiator.getValueType().intValue());

        Method createUsingIntMethod = instantiator.getClass().getMethod("createUsingInt");
        assertEquals(Integer.TYPE, createUsingIntMethod.getReturnType());
        assertEquals(testInt, createUsingIntMethod.invoke(instantiator, testInt));
    }

    @Test
    public void testLongCreator() throws Exception {
        long testLong = 1L;
        AnnotatedWithParams longCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(longCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertEquals(Long.TYPE.getName(), instantiator.getValueTypeDesc());
        assertEquals(Long.TYPE, instantiator.getType());
        assertNotNull(instantiator.getValueType());
        assertEquals(testLong, instantiator.getValueType().longValue());

        Method createUsingLongMethod = instantiator.getClass().getMethod("createUsingLong");
        assertEquals(Long.TYPE, createUsingLongMethod.getReturnType());
        assertEquals(testLong, createUsingLongMethod.invoke(instantiator, testLong));
    }

    @Test
    public void testDoubleCreator() throws Exception {
        double testDouble = 1.0;
        AnnotatedWithParams doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(doubleCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertEquals(Double.TYPE.getName(), instantiator.getValueTypeDesc());
        assertEquals(Double.TYPE, instantiator.getType());
        assertNotNull(instantiator.getValueType());
        assertEquals(testDouble, instantiator.getValueType().doubleValue(), 0.0);

        Method createUsingDoubleMethod = instantiator.getClass().getMethod("createUsingDouble");
        assertEquals(Double.TYPE, createUsingDoubleMethod.getReturnType());
        assertEquals(testDouble, createUsingDoubleMethod.invoke(instantiator, testDouble), 0.0);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        boolean testBoolean = true;
        AnnotatedWithParams booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(booleanCreator, true);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertEquals(Boolean.TYPE.getName(), instantiator.getValueTypeDesc());
        assertEquals(Boolean.TYPE, instantiator.getType());
        assertNotNull(instantiator.getValueType());
        assertEquals(testBoolean, instantiator.getValueType().booleanValue());

        Method createUsingBooleanMethod = instantiator.getClass().getMethod("createUsingBoolean");
        assertEquals(Boolean.TYPE, createUsingBooleanMethod.getReturnType());
        assertEquals(testBoolean, createUsingBooleanMethod.invoke(instantiator, testBoolean));
    }

    @Test
    public void testDelegateCreator() throws Exception {
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        CreatorProperty[] delegateArgs = new CreatorProperty[] { Mockito.mock(CreatorProperty.class) };
        AnnotatedWithParams delegateCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addDelegatingCreator(delegateCreator, true, delegateArgs);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertNotNull(instantiator.getValueType());
        assertSame(delegateType, instantiator.getType());
        assertSame(delegateType, instantiator.getValueType().getType());
        assertEquals(delegateArgs[0], instantiator.getValueType().getInjectableValue());

        Method createUsingDelegateMethod = instantiator.getClass().getMethod("createUsingDelegate");
        assertEquals(delegateType, createUsingDelegateMethod.getReturnType());
        assertEquals(delegateType, createUsingDelegateMethod.invoke(instantiator, delegateArgs[0]));
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        List<BeanPropertyDefinition> properties = Arrays.asList(
            new BeanPropertyDefinition(new PropertyName("testProperty1"), null, null, null, null),
            new BeanPropertyDefinition(new PropertyName("testProperty2"), null, null, null, null)
        );
        CreatorProperty[] propertyBasedArgs = new CreatorProperty[] { Mockito.mock(CreatorProperty.class), Mockito.mock(CreatorProperty.class) };
        AnnotatedWithParams propertyCreator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addPropertyCreator(propertyCreator, true, propertyBasedArgs);

        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);
        assertNotNull(instantiator.getValueType());
        assertSame(getClass(), instantiator.getType());
        assertArrayEquals(propertyBasedArgs, instantiator.getValueType().getInjectableValues());

        Method createUsingPropertiesMethod = instantiator.getClass().getMethod("createUsingProperties");
        assertEquals(getClass(), createUsingPropertiesMethod.getReturnType());
        Object result = createUsingPropertiesMethod.invoke(instantiator, Arrays.asList(new Object[0], new Object[0]));
        assertEquals(getClass(), result.getClass());

        BeanPropertyDefinition testProperty1 = properties.get(0);
        BeanPropertyDefinition testProperty2 = properties.get(1);
        assertEquals(testProperty1.getName().getText(), testProperty1.getName().toString());
        assertEquals(testProperty2.getName().getText(), testProperty2.getName().toString());
        assertEquals(testProperty1.getName().toString(), instantiator.getValueType().getInjectableValueNames().get(0).getText());
        assertEquals(testProperty2.getName().toString(), instantiator.getValueType().getInjectableValueNames().get(1).getText());
    }

    @Test
    public void testCanFixAccess() throws Exception {
        AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);
        AnnotatedConstructor constructor = Mockito.mock(AnnotatedConstructor.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        AnnotatedParameter parameter = Mockito.mock(AnnotatedParameter.class);

        Method fixAccessMethod = ClassUtil.class.getDeclaredMethod("checkAndFixAccess", AnnotatedMember.class);
        fixAccessMethod.setAccessible(true);

        creatorCollector = new CreatorCollector(null, false);
        creatorCollector._canFixAccess = true;

        // constructor
        Mockito.when(constructor.getAnnotated()).thenReturn(annotatedClass);
        creatorCollector._fixAccess(constructor);
        verify(constructor).getAnnotated();

        // method
        Mockito.when(method.getAnnotated()).thenReturn(annotatedClass);
        creatorCollector._fixAccess(method);
        verify(method).getAnnotated();

        // parameter
        Mockito.when(parameter.getAnnotated()).thenReturn(annotatedClass);
        creatorCollector._fixAccess(parameter);
        verify(parameter).getAnnotated();

        creatorCollector = new CreatorCollector(null, false);
        creatorCollector._canFixAccess = false;

        // constructor
        Mockito.when(constructor.getAnnotated()).thenReturn(annotatedClass);
        try {
            creatorCollector._fixAccess(constructor);
            fail();
        } catch (IllegalAccessException e) {
            // expected
        }

        // method
        Mockito.when(method.getAnnotated()).thenReturn(annotatedClass);
        try {
            creatorCollector._fixAccess(method);
            fail();
        } catch (IllegalAccessException e) {
            // expected
        }

        // parameter
        Mockito.when(parameter.getAnnotated()).thenReturn(annotatedClass);
        try {
            creatorCollector._fixAccess(parameter);
            fail();
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        String testString = "testString";
        AnnotatedWithParams stringCreator1 = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator1, true);
        AnnotatedWithParams stringCreator2 = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator2, true);
        creatorCollector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateExplicitCreator() throws Exception {
        String testString = "testString";
        AnnotatedWithParams stringCreator1 = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator1, true);
        AnnotatedWithParams stringCreator2 = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator2, true);
        creatorCollector.addStringCreator(stringCreator1, true);
        creatorCollector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateNonExplicitCreator() throws Exception {
        String testString = "testString";
        AnnotatedWithParams stringCreator1 = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator1);
        AnnotatedWithParams stringCreator2 = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(stringCreator2);
        creatorCollector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyCreator() throws Exception {
        String testString1 = "testString1";
        String testString2 = "testString2";
        CreatorProperty creatorProperty1 = Mockito.mock(CreatorProperty.class);
        CreatorProperty creatorProperty2 = Mockito.mock(CreatorProperty.class);
        creatorCollector.addPropertyCreator(Mockito.mock(AnnotatedWithParams.class), true, new CreatorProperty[] { creatorProperty1 });
        creatorCollector.addPropertyCreator(Mockito.mock(AnnotatedWithParams.class), true, new CreatorProperty[] { creatorProperty2 });
        creatorCollector.addPropertyCreator(Mockito.mock(AnnotatedWithParams.class), true, new CreatorProperty[] { creatorProperty1, creatorProperty2 });
        creatorCollector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCreator() throws Exception {
        creatorCollector.addStringCreator(null, true);
        creatorCollector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDelegateArgs() throws Exception {
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        creatorCollector.addDelegatingCreator(Mockito.mock(AnnotatedWithParams.class), true, (CreatorProperty[]) null);
        creatorCollector.constructValueInstantiator(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyBasedArgs() throws Exception {
        creatorCollector.addPropertyCreator(Mockito.mock(AnnotatedWithParams.class), true, (CreatorProperty[]) null);
        creatorCollector.constructValueInstantiator(config);
    }
}

class AnnotatedWithParams {
    // emptyclass
}