package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BasicDeserializerProvider.Finder;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private AnnotatedClass ac;
    private BeanDescription beanDesc;
    private CreatorCollector collector;
    private MapperConfig<?> config;
    private JavaType javaType;

    @Before
    public void setUp() throws Exception {
        ac = mock(AnnotatedClass.class);
        BeanPropertyDefinition bpd = mock(BeanPropertyDefinition.class);
        List<BeanPropertyDefinition> bpdList = Arrays.asList(bpd);
        AnnotationIntrospector ai = mock(AnnotationIntrospector.class);
        Finder finder = mock(Finder.class);
        javaType = mock(JavaType.class);
        config = new MapperConfig<Object>(ai, finder, Collections.emptyMap(), null, null, null, false, null, null, null, null);
        when(ac.getRawClass()).thenReturn(Object.class);
        when(ac.findProperties()).thenReturn(bpdList);
        when(ac.findValueDeserializer()).thenReturn(null);
        when(ac.findCreator(null)).thenReturn(null);
        when(ac.findValueDeserializer(JavaType.constructType(String[].class))).thenReturn(null);

        beanDesc = new BasicBeanDescription(ac, Object.class);
        collector = new CreatorCollector(beanDesc, config);
    }

    @After
    public void tearDown() throws Exception {
        // no cleanup needed
    }

    @Test
    public void testDefaultCreator() throws Exception {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creator);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);
    }

    @Test
    public void testStringCreator() throws Exception {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        String methodName = "testStringCreator";
        Method method = mock(Method.class);
        when(creator.getParameterType(0)).thenReturn(String.class);
        when(creator.getAnnotatedMember()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);
        collector.addStringCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        inst.configureFromStringCreator(creator);
        assertNotNull(inst);
    }

    @Test
    public void testIntCreator() throws Exception {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        String methodName = "testIntCreator";
        Method method = mock(Method.class);
        when(creator.getParameterType(0)).thenReturn(int.class);
        when(creator.getAnnotatedMember()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);
        collector.addIntCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        inst.configureFromIntCreator(creator);
        assertNotNull(inst);
    }

    @Test
    public void testLongCreator() throws Exception {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        String methodName = "testLongCreator";
        Method method = mock(Method.class);
        when(creator.getParameterType(0)).thenReturn(long.class);
        when(creator.getAnnotatedMember()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);
        collector.addLongCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        inst.configureFromLongCreator(creator);
        assertNotNull(inst);
    }

    @Test
    public void testDoubleCreator() throws Exception {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        String methodName = "testDoubleCreator";
        Method method = mock(Method.class);
        when(creator.getParameterType(0)).thenReturn(double.class);
        when(creator.getAnnotatedMember()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);
        collector.addDoubleCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        inst.configureFromDoubleCreator(creator);
        assertNotNull(inst);
    }

    @Test
    public void testBooleanCreator() throws Exception {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        String methodName = "testBooleanCreator";
        Method method = mock(Method.class);
        when(creator.getParameterType(0)).thenReturn(boolean.class);
        when(creator.getAnnotatedMember()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);
        collector.addBooleanCreator(creator, false);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        inst.configureFromBooleanCreator(creator);
        assertNotNull(inst);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        String methodName = "testDelegatingCreator";
        Method method = mock(Method.class);
        Class<?> delegateType = Object.class;
        when(creator.getParameterType(0)).thenReturn(delegateType);
        when(creator.getAnnotatedMember()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        delegateArgs[0] = mock(SettableBeanProperty.class);
        collector.addDelegatingCreator(creator, false, delegateArgs);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        inst.configureFromDelegateSettings(creator, delegateType, delegateArgs);
        assertNotNull(inst);
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        String methodName = "testPropertyBasedCreator";
        Method method = mock(Method.class);
        when(creator.getParameterType(0)).thenReturn(Object.class);
        when(creator.getAnnotatedMember()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        propertyBasedArgs[0] = mock(SettableBeanProperty.class);
        propertyBasedArgs[1] = mock(SettableBeanProperty.class);
        collector.addPropertyCreator(creator, false, propertyBasedArgs);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        inst.configureFromPropertyBasedSettings(creator, propertyBasedArgs);
        assertNotNull(inst);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        String methodName1 = "testCreator";
        Method method1 = mock(Method.class);
        when(creator1.getParameterType(0)).thenReturn(int.class);
        when(creator1.getAnnotatedMember()).thenReturn(method1);
        when(method1.getName()).thenReturn(methodName1);

        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        when(creator2.getParameterType(0)).thenReturn(int.class);
        when(creator2.getAnnotatedMember()).thenReturn(method1);

        collector.addIntCreator(creator1, false);
        collector.addIntCreator(creator2, false);
        ValueInstantiator inst = collector.constructValueInstantiator(null);
        // should throw exception
    }

    @Test
    public void testConstructValueInstantiator() throws Exception {
        AnnotatedWithParams creator1 = mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2 = mock(AnnotatedWithParams.class);
        String methodName1 = "testCreator1";
        Method method1 = mock(Method.class);
        String methodName2 = "testCreator2";
        Method method2 = mock(Method.class);
        when(creator1.getParameterType(0)).thenReturn(int.class);
        when(creator1.getAnnotatedMember()).thenReturn(method1);
        when(method1.getName()).thenReturn(methodName1);

        when(creator2.getParameterType(0)).thenReturn(long.class);
        when(creator2.getAnnotatedMember()).thenReturn(method2);
        when(method2.getName()).thenReturn(methodName2);

        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        delegateArgs[0] = mock(SettableBeanProperty.class);

        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        propertyBasedArgs[0] = mock(SettableBeanProperty.class);
        propertyBasedArgs[1] = mock(SettableBeanProperty.class);

        collector.addIntCreator(creator1, true);
        collector.addLongCreator(creator2, true);
        collector.addPropertyCreator(creator1, true, propertyBasedArgs);
        collector.addDelegatingCreator(creator1, true, delegateArgs);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        inst.configureFromIntCreator(creator1);
        inst.configureFromLongCreator(creator2);
        inst.configureFromPropertyBasedSettings(creator1, propertyBasedArgs);
        inst.configureFromDelegateSettings(creator1, Object.class, delegateArgs);

        assertNotNull(inst);
        StdValueInstantiator stdInst = (StdValueInstantiator) inst;
        // default creator is not set
        assertNull(stdInst.getDefaultCreator());
    }
}