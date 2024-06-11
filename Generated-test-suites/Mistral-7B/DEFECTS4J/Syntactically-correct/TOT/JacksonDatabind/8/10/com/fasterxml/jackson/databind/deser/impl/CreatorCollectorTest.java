package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonCreator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.type.ClassTypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

public class CreatorCollectorTest {

    private DeserializationConfig _config;
    private ObjectMapper _mapper;
    private CreatorCollector _collector;

    @BeforeMethod
    public void setUp() throws Exception {
        _config = new DeserializationConfig();
        _mapper = new ObjectMapper(_config);
    }

    @Test
    public void testDefaultCreator() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(List.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        collector.setDefaultCreator(new AnnotatedConstructor(_mapper.getConstructors(List.class)[0]));

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(List.class);
        Object instance = instantiator.createValueInstance(type, _config);

        assertNull(instance);
    }

    @Test
    public void testStringCreator() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(LinkedHashMap.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod method = new AnnotatedMethod(LinkedHashMap.class.getDeclaredMethod("new", String.class));
        collector.addStringCreator(method, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(LinkedHashMap.class);
        Object instance = instantiator.createValueInstance(type, _config, "test");

        assertEquals(instance, new LinkedHashMap<Object, Object>(){{put("test", "test");}});
    }

    @Test
    public void testIntCreator() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(HashMap.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod method = new AnnotatedMethod(HashMap.class.getDeclaredMethod("new", int.class));
        class IntCreator implements ValueInstantiator.ValueInstantiatorImpl {
            int size;

            @Override
            public Object createValueInstance(JavaType type, DeserializationConfig config) throws IOException {
                return new HashMap<Object, Object>(size);
            }

            @Override
            public void configure(ValueInstantiator inst, JavaType beanType, BeanDescription valueDesc) throws IOException {
                inst.configureFromIntCreator(this);
            }
        } intCreator = new IntCreator();
        collector.addIntCreator(new AnnotatedMethod(intCreator, "createValueInstance"), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(HashMap.class);
        Object instance = instantiator.createValueInstance(type, _config, 10);

        assertEquals(instance, new HashMap<Object, Object>(10));
    }

    @Test
    public void testLongCreator() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(ArrayList.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod method = new AnnotatedMethod(ArrayList.class.getDeclaredMethod("new", long.class));
        collector.addLongCreator(method, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(ArrayList.class);
        Object instance = instantiator.createValueInstance(type, _config, 10L);

        assertEquals(instance, new ArrayList<Object>(10));
    }

    @Test
    public void testDoubleCreator() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(HashMap.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod method = new AnnotatedMethod(HashMap.class.getDeclaredMethod("new", double.class));
        class DoubleCreator implements ValueInstantiator.ValueInstantiatorImpl {
            double size;

            @Override
            public Object createValueInstance(JavaType type, DeserializationConfig config) throws IOException {
                return new HashMap<Object, Object>((int)size);
            }

            @Override
            public void configure(ValueInstantiator inst, JavaType beanType, BeanDescription valueDesc) throws IOException {
                inst.configureFromDoubleCreator(this);
            }
        } doubleCreator = new DoubleCreator();
        collector.addDoubleCreator(new AnnotatedMethod(doubleCreator, "createValueInstance"), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(HashMap.class);
        Object instance = instantiator.createValueInstance(type, _config, 10.5);

        assertEquals(instance, new HashMap<Object, Object>((int)10.5));
    }

    @Test
    public void testBooleanCreator() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(ArrayList.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedMethod method = new AnnotatedMethod(ArrayList.class.getDeclaredMethod("new", boolean.class));
        collector.addBooleanCreator(method, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(ArrayList.class);
        Object instance = instantiator.createValueInstance(type, _config, false);

        assertEquals(instance, new ArrayList<Object>(0));

        Object instance2 = instantiator.createValueInstance(type, _config, true);

        assertEquals(instance2, new ArrayList<Object>(1));
    }

    @Test
    public void testDelegateCreator() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(HashMap.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedClass delegateClass = _mapper.getTypeHandlerFor(String.class).getDescriptor();
        AnnotatedMethod delegateMethod = new AnnotatedMethod(delegateClass.getRawType().getDeclaredMethod("valueOf", String.class));

        AnnotatedConstructor constructor = new AnnotatedConstructor(String.class.getDeclaredConstructor());

        collector.addDelegatingCreator(new AnnotatedMethod(HashMap.class.getDeclaredMethod("new", String[].class)), false, new CreatorProperty[] {
                new CreatorProperty(constructor, new PropertyName("delegate"), null),
                new CreatorProperty(delegateMethod, new PropertyName("keys"), null),
                new CreatorProperty(delegateMethod, new PropertyName("values"), null)
        });

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(HashMap.class);
        Object[] args = new Object[] {"key1", "value1", "key2", "value2"};
        Object instance = instantiator.createValueInstance(type, _config, args);

        Map<String, Object> expected = new LinkedHashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        assertEquals(instance, expected);
    }

    @Test
    public void testPropertyBasedCreator() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(HashMap.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        BeanPropertyDefinition keyDefinition = _mapper.getRootValueFactory().findValueProperty("key");
        BeanPropertyDefinition valueDefinition = _mapper.getRootValueFactory().findValueProperty("value");

        AnnotatedMethod method = new AnnotatedMethod(HashMap.class.getDeclaredMethod("new", PropertyName[].class));
        CreatorProperty[] properties = new CreatorProperty[] {
            new CreatorProperty(keyDefinition, new PropertyName("key"), null),
            new CreatorProperty(valueDefinition, new PropertyName("value"), null)
        };

        collector.addPropertyCreator(method, true, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(HashMap.class);
        Object[] args = new Object[] {new PropertyName("key1"), new PropertyName("value1"), new PropertyName("key2"), new PropertyName("value2")};
        Object instance = instantiator.createValueInstance(type, _config, args);

        Map<String, Object> expected = new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        assertEquals(instance, expected);
    }

    @Test
    public void testMultipleCreators() throws JsonMappingException, JsonProcessingException {
        BeanDescription beanDesc = _mapper.getTypeHandlerFor(LinkedHashMap.class).getBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        List<AnnotatedMethod> constructors = Arrays.asList(
            new AnnotatedMethod(LinkedHashMap.class.getDeclaredMethod("new", String.class)),
            new AnnotatedMethod(LinkedHashMap.class.getDeclaredMethod("new", int.class))
        );

        for (AnnotatedMethod method : constructors) {
            collector.addStringCreator(method, false);
            collector.addIntCreator(method, false);
        }

        ValueInstantiator instantiator = collector.constructValueInstantiator(_config);

        JavaType type = _mapper.getTypeFactory().constructType(LinkedHashMap.class);

        Object instance1 = instantiator.createValueInstance(type, _config, "test");
        assertEquals(instance1, new LinkedHashMap<Object, Object>(){{put("test", "test");}});

        Object instance2 = instantiator.createValueInstance(type, _config, 10);
        assertEquals(instance2, new LinkedHashMap<Object, Object>(10));
    }
}