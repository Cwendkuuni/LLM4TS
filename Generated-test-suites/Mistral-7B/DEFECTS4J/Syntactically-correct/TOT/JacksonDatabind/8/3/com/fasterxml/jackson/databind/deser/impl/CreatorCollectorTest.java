package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreatorCollectorTest {

    private DeserializationConfig config;
    private ObjectMapper mapper;
    private BeanDescription beanDesc;

    @Before
    public void setup() throws Exception {
        config = Mockito.spy(new DeserializationConfig());
        mapper = new ObjectMapper(new JsonFactory());
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        beanDesc = Mockito.spy(new BeanDescription(mapper.getTypeFactory().constructType(TestClass.class)));
    }

    @Test
    public void testDefaultCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams defaultCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(defaultCreator);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(TestClass.class);
        Object result = instantiator.createValueInstance(config, null, type, null);
        assertNotNull(result);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test
    public void testStringCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String creatorName = "createStringInstance";
        AnnotatedWithParams stringCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(stringCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(TestClass.class);
        Object result = instantiator.createValueInstance(config, null, type, new String[]{"testString"});
        assertNotNull(result);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test
    public void testIntCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String creatorName = "createIntInstance";
        AnnotatedWithParams intCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(intCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(TestClass.class);
        Object result = instantiator.createValueInstance(config, null, type, new Integer(42));
        assertNotNull(result);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test
    public void testLongCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String creatorName = "createLongInstance";
        AnnotatedWithParams longCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(longCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(TestClass.class);
        Object result = instantiator.createValueInstance(config, null, type, new Long(42L));
        assertNotNull(result);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String creatorName = "createDoubleInstance";
        AnnotatedWithParams doubleCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(doubleCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(TestClass.class);
        Object result = instantiator.createValueInstance(config, null, type, new Double(42.0));
        assertNotNull(result);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test
    public void testBooleanCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String creatorName = "createBooleanInstance";
        AnnotatedWithParams booleanCreator = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(booleanCreator, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType type = mapper.getTypeFactory().constructType(TestClass.class);
        Object result = instantiator.createValueInstance(config, null, type, new Boolean(true));
        assertNotNull(result);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String creatorName = "createDelegateInstance";
        AnnotatedWithParams delegateCreator = Mockito.mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = Mockito.mock(CreatorProperty.class);
        collector.addDelegatingCreator(delegateCreator, true, injectables);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        JavaType delegateType = mapper.getTypeFactory().constructType(TestDelegate.class);
        Object delegateResult = instantiator.createValueInstance(config, null, delegateType, null);
        assertEquals(TestDelegate.class, delegateResult.getClass());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String creatorName = "createPropertyBasedInstance";
        AnnotatedWithParams propertyCreator = Mockito.mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = Mockito.mock(CreatorProperty.class);
        properties[1] = Mockito.mock(CreatorProperty.class);
        collector.addPropertyCreator(propertyCreator, true, properties);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        TypeReference<TestClass> typeRef = new TypeReference<TestClass>() {
        };
        Object result = instantiator.createValueInstance(config, null, typeRef, new String[]{"testProperty1", "testProperty2"}, null);
        assertNotNull(result);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        String creatorName = "createStringInstance";
        AnnotatedWithParams stringCreator1 = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams stringCreator2 = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(stringCreator1, true);
        collector.addStringCreator(stringCreator2, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        // Intentionally empty
    }
}

class TestClass {
    // empty class for testing
}

class TestDelegate {
    // empty delegate class for testing
}