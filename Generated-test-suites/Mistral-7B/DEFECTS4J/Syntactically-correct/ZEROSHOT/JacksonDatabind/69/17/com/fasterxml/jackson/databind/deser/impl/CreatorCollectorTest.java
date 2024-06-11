package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector.FindCreatorAnnotation;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class CreatorCollectorTest {

    @Mock
    private AnnotationIntrospector _annotationIntrospector;

    @InjectMocks
    private CreatorCollector _creatorCollector;

    @Mock
    private DeserializationConfig _config;

    @Mock
    private MapperConfig<?> _mapperConfig;

    @Mock
    private BeanDescription _beanDescription;

    @Mock
    private AnnotatedElement _element;

    @Mock
    private JavaType _type;

    @Mock
    private FindCreatorAnnotation _annotation;

    @Mock
    private SettableBeanProperty _beanProperty;

    @Mock
    private AnnotatedWithParams _creator;

    @Mock
    private Method _method;

    @Before
    public void setup() {
        Mockito.when(_annotationIntrospector.findCreator(_beanDescription)).thenReturn(_annotation);
        Mockito.when(_annotation.getClass()).thenReturn(CreatorProperty.class);
        Mockito.when(_annotation.getMember()).thenReturn(_method);
        Mockito.when(_annotation.getAnnotated()).thenReturn(_element);
        Mockito.when(_annotation.getCreatorProperties()).thenReturn(new CreatorProperty[] { _beanProperty });

        Mockito.when(_beanDescription.getType()).thenReturn(_type);
        Mockito.when(_beanDescription.getBeanClass()).thenReturn(Object.class);
        Mockito.when(_mapperConfig.canOverrideAccessModifiers()).thenReturn(true);
        Mockito.when(_mapperConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        BeanPropertyDefinition propertyDefinition = Mockito.mock(BeanPropertyDefinition.class);
        Mockito.when(propertyDefinition.getName()).thenReturn("defaultProperty");
        Mockito.when(_beanDescription.findPropertyDefinition("defaultProperty")).thenReturn(propertyDefinition);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.setDefaultCreator(Mockito.mock(AnnotatedWithParams.class));

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);
    }

    @Test
    public void testStringCreator() throws Exception {
        String creatorName = "stringCreator";
        _creator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(_creator.getName()).thenReturn(creatorName);
        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addStringCreator(_creator, false);

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);

        // Check that string creator was registered
        AnnotatedWithParams registeredCreator = _creatorCollector._creators[CreatorCollector.C_STRING];
        assertEquals(creatorName, registeredCreator.getName());
    }

    @Test
    public void testIntCreator() throws Exception {
        String creatorName = "intCreator";
        int creatorParamCount = 0;

        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(_creator.getName()).thenReturn(creatorName);
        Mockito.when(_creator.getParameterTypes()).thenReturn(new Class[0]);
        Mockito.when(_creator.getParameterCount()).thenReturn(creatorParamCount);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addIntCreator(creator, false);

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);

        // Check that int creator was registered
        AnnotatedWithParams registeredCreator = _creatorCollector._creators[CreatorCollector.C_INT];
        assertEquals(creatorName, registeredCreator.getName());
    }

    @Test
    public void testLongCreator() throws Exception {
        String creatorName = "longCreator";
        int creatorParamCount = 1;
        long creatorParamValue = 1L;

        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(_creator.getName()).thenReturn(creatorName);
        Mockito.when(_creator.getParameterTypes()).thenReturn(new Class[] { long.class });
        Mockito.when(_creator.getParameterCount()).thenReturn(creatorParamCount);
        Mockito.when(_creator.getParameter(0)).thenReturn(Mockito.mock(AnnotatedElement.class));
        Mockito.when(_creator.getParameter(0).getValue()).thenReturn(creatorParamValue);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addLongCreator(creator, false);

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);

        // Check that long creator was registered
        AnnotatedWithParams registeredCreator = _creatorCollector._creators[CreatorCollector.C_LONG];
        assertEquals(creatorName, registeredCreator.getName());
        assertEquals(long.class, registeredCreator.getParameterTypes()[0]);
        assertEquals(creatorParamValue, registeredCreator.getParameter(0).getValue());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        String creatorName = "doubleCreator";
        int creatorParamCount = 1;
        double creatorParamValue = 1.0;

        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(_creator.getName()).thenReturn(creatorName);
        Mockito.when(_creator.getParameterTypes()).thenReturn(new Class[] { double.class });
        Mockito.when(_creator.getParameterCount()).thenReturn(creatorParamCount);
        Mockito.when(_creator.getParameter(0)).thenReturn(Mockito.mock(AnnotatedElement.class));
        Mockito.when(_creator.getParameter(0).getValue()).thenReturn(creatorParamValue);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addDoubleCreator(creator, false);

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);

        // Check that double creator was registered
        AnnotatedWithParams registeredCreator = _creatorCollector._creators[CreatorCollector.C_DOUBLE];
        assertEquals(creatorName, registeredCreator.getName());
        assertEquals(double.class, registeredCreator.getParameterTypes()[0]);
        assertEquals(creatorParamValue, registeredCreator.getParameter(0).getValue());
    }

    @Test
    public void testBooleanCreator() throws Exception {
        String creatorName = "booleanCreator";
        int creatorParamCount = 1;
        boolean creatorParamValue = true;

        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(_creator.getName()).thenReturn(creatorName);
        Mockito.when(_creator.getParameterTypes()).thenReturn(new Class[] { boolean.class });
        Mockito.when(_creator.getParameterCount()).thenReturn(creatorParamCount);
        Mockito.when(_creator.getParameter(0)).thenReturn(Mockito.mock(AnnotatedElement.class));
        Mockito.when(_creator.getParameter(0).getValue()).thenReturn(creatorParamValue);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addBooleanCreator(creator, false);

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);

        // Check that boolean creator was registered
        AnnotatedWithParams registeredCreator = _creatorCollector._creators[CreatorCollector.C_BOOLEAN];
        assertEquals(creatorName, registeredCreator.getName());
        assertEquals(boolean.class, registeredCreator.getParameterTypes()[0]);
        assertEquals(creatorParamValue, registeredCreator.getParameter(0).getValue());
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        String creatorName = "delegatingCreator";
        int creatorParamCount = 2;

        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[] { _beanProperty, _beanProperty };

        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(_creator.getName()).thenReturn(creatorName);
        Mockito.when(_creator.getParameterTypes()).thenReturn(new Class[] { Object.class, Object.class });
        Mockito.when(_creator.getParameterCount()).thenReturn(creatorParamCount);
        Mockito.when(_creator.getParameter(0)).thenReturn(_element);
        Mockito.when(_creator.getParameter(1)).thenReturn(_element);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addDelegatingCreator(creator, false, delegateArgs);

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);

        // Check that delegating creator was registered
        AnnotatedWithParams registeredCreator = _creatorCollector._creators[CreatorCollector.C_DELEGATE];
        assertEquals(creatorName, registeredCreator.getName());
        assertEquals(Object[].class, registeredCreator.getParameterTypes());
        assertEquals(delegateArgs, registeredCreator.getParameters());
    }

    @Test
    public void testArrayDelegatingCreator() throws Exception {
        String creatorName = "arrayDelegatingCreator";
        int creatorParamCount = 1;

        SettableBeanProperty[] arrayDelegateArgs = new SettableBeanProperty[] { _beanProperty };

        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        Mockito.when(_creator.getName()).thenReturn(creatorName);
        Mockito.when(_creator.getParameterTypes()).thenReturn(new Class[] { Object[].class });
        Mockito.when(_creator.getParameterCount()).thenReturn(creatorParamCount);
        Mockito.when(_creator.getParameter(0)).thenReturn(_element);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addDelegatingCreator(creator, false, arrayDelegateArgs);

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);

        // Check that array delegating creator was registered
        AnnotatedWithParams registeredCreator = _creatorCollector._creators[CreatorCollector.C_ARRAY_DELEGATE];
        assertEquals(creatorName, registeredCreator.getName());
        assertEquals(Object[].class, registeredCreator.getParameterTypes()[0]);
        assertEquals(arrayDelegateArgs, registeredCreator.getParameters());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        String creatorName = "propertyBasedCreator";

        List<SettableBeanProperty> propertyBasedArgs = Arrays.asList(_beanProperty, _beanProperty);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addPropertyCreator(Mockito.mock(AnnotatedWithParams.class), false, propertyBasedArgs.toArray(new SettableBeanProperty[0]));

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        assertNotNull(valueInstantiator);

        // Check that property based creator was registered
        AnnotatedWithParams registeredCreator = _creatorCollector._creators[CreatorCollector.C_PROPS];
        assertEquals(creatorName, registeredCreator.getName());
        assertEquals(SettableBeanProperty[].class, registeredCreator.getParameterTypes());
        assertEquals(propertyBasedArgs.toArray(new SettableBeanProperty[0]), registeredCreator.getParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyBasedCreatorWithDuplicateProperties() throws Exception {
        String creatorName = "propertyBasedCreator";

        List<SettableBeanProperty> propertyBasedArgs = Arrays.asList(_beanProperty, _beanProperty);

        _creatorCollector = new CreatorCollector(_beanDescription, _mapperConfig);
        _creatorCollector.addPropertyCreator(Mockito.mock(AnnotatedWithParams.class), false, propertyBasedArgs.toArray(new SettableBeanProperty[0]));
        _creatorCollector.addPropertyCreator(Mockito.mock(AnnotatedWithParams.class), false, propertyBasedArgs.toArray(new SettableBeanProperty[0]));

        ValueInstantiator valueInstantiator = _creatorCollector.constructValueInstantiator(_config);
        fail(); // should throw an IllegalArgumentException
    }
}