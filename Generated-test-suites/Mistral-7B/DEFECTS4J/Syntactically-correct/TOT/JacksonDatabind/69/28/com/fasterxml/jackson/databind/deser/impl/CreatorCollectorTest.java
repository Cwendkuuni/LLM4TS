package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.AnnotatedClass;
import com.fasterxml.jackson.core.base.AnnotatedWithParams;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassHelper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.AccessUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreatorCollectorTest {

    private static class TestBean {
        private int value;
        private String stringValue;
        private boolean booleanValue;
        
        public TestBean(int value, String stringValue, boolean booleanValue) {
            this.value = value;
            this.stringValue = stringValue;
            this.booleanValue = booleanValue;
        }
        
        @JsonCreator
        public TestBean(@JsonProperty("value") int value,
                        @JsonProperty("stringValue") String stringValue,
                        @JsonProperty("booleanValue") boolean booleanValue) {
            this(value, stringValue, booleanValue);
        }
        
        @JsonProperty("value")
        public int getValue() {
            return value;
        }
        
        @JsonProperty("stringValue")
        public String getStringValue() {
            return stringValue;
        }
        
        @JsonProperty("booleanValue")
        public boolean getBooleanValue() {
            return booleanValue;
        }
    }
    
    @BeforeClass
    public static void setup() throws Exception {
        MapperFactory factory = new MapperFactory();
        ObjectMapper mapper = factory.getMapper();
        _mapper = mapper;
    }
    
    private ObjectMapper _mapper;

    @Test
    public void testDefaultCreator() throws IOException {
        CreatorCollector collector = new CreatorCollector(_beanDescFor(TestBean.class), _mapper.getConfig());

        ValueInstantiator inst = collector.constructValueInstantiator(_mapper.getConfig());
        TestBean obj = inst.createValueInstance(null);
        assertNotNull(obj);
        assertEquals(0, obj.getValue());
    }

    @Test
    public void testStringCreator() throws IOException {
        TestBean obj = _mapper.readValue(
                "{ \"stringValue\": \"test\" }", TestBean.class);
        CreatorCollector collector = new CreatorCollector(_beanDescFor(TestBean.class), _mapper.getConfig());

        collector.setStringCreator(AnnotatedWithParams.construct(
                new MethodCreatorAnnotatedMember(TestBean.class, "TestBean", null), new String[0]));

        ValueInstantiator inst = collector.constructValueInstantiator(_mapper.getConfig());
        TestBean obj2 = inst.createValueInstance(new String[]{"test"});
        assertEquals(obj.getStringValue(), obj2.getStringValue());
    }

    @Test
    public void testIntCreator() throws IOException {
        TestBean obj = _mapper.readValue(
                "{ \"value\": 1 }", TestBean.class);
        CreatorCollector collector = new CreatorCollector(_beanDescFor(TestBean.class), _mapper.getConfig());

        collector.addClassIntrospector(new NopAnnotationIntrospector());
        collector.setIntCreator(AnnotatedWithParams.construct(
                new MethodCreatorAnnotatedMember(TestBean.class, "TestBean", new Class[]{int.class}), new int[0], 1));

        ValueInstantiator inst = collector.constructValueInstantiator(_mapper.getConfig());
        TestBean obj2 = inst.createValueInstance(new int[]{1});
        assertEquals(obj.getValue(), obj2.getValue());
    }

    @Test
    public void testLongCreator() throws IOException {
        TestBean obj = _mapper.readValue(
                "{ \"value\": 1 }", TestBean.class);
        CreatorCollector collector = new CreatorCollector(_beanDescFor(TestBean.class), _mapper.getConfig());

        collector.addClassIntrospector(new NopAnnotationIntrospector());
        collector.setLongCreator(AnnotatedWithParams.construct(
                new MethodCreatorAnnotatedMember(TestBean.class, "TestBean", new Class[]{long.class}), new long[0], 1L));

        ValueInstantiator inst = collector.constructValueInstantiator(_mapper.getConfig());
        TestBean obj2 = inst.createValueInstance(new long[]{1L});
        assertEquals(obj.getValue(), obj2.getValue());
    }

    @Test
    public void testBooleanCreator() throws IOException {
        TestBean obj = _mapper.readValue(
                "{ \"booleanValue\": true }", TestBean.class);
        CreatorCollector collector = new CreatorCollector(_beanDescFor(TestBean.class), _mapper.getConfig());

        collector.addClassIntrospector(new NopAnnotationIntrospector());
        collector.setBooleanCreator(AnnotatedWithParams.construct(
                new MethodCreatorAnnotatedMember(TestBean.class, "TestBean", new Class[]{boolean.class}), new boolean[0], true));

        ValueInstantiator inst = collector.constructValueInstantiator(_mapper.getConfig());
        TestBean obj2 = inst.createValueInstance(new boolean[]{true});
        assertEquals(obj.getBooleanValue(), obj2.getBooleanValue());
    }

    @Test
    public void testConstructorCreator() throws IOException {
        TestBean obj = _mapper.readValue(
                "{ \"value\": 1, \"stringValue\": \"test\", \"booleanValue\": true }", TestBean.class);
        CreatorCollector collector = new CreatorCollector(_beanDescFor(TestBean.class), _mapper.getConfig());

        AnnotatedClass ac = AnnotatedClassHelper.findCreatorFromAnnotation(TestBean.class, _mapper.getAnnotationIntrospector());
        AnnotatedWithParams constructor = AnnotatedWithParams.construct(ac.getDeclaredConstructors()[0], new Class[0]);
        collector.setDefaultCreator(constructor);

        ValueInstantiator inst = collector.constructValueInstantiator(_mapper.getConfig());
        TestBean obj2 = inst.createValueInstance(null);
        assertEquals(obj.getValue(), obj2.getValue());
        assertEquals(obj.getStringValue(), obj2.getStringValue());
        assertEquals(obj.getBooleanValue(), obj2.getBooleanValue());
    }

    @Test
    public void testDelegateCreator() throws IOException {
        TestDelegate delegate = new TestDelegate();
        TestBean obj = _mapper.readValue(
                "{ \"delegate\": {"+
                        "\"value\": 1,"+
                        "\"stringValue\": \"test\","+
                        "\"booleanValue\": true"+
                "}}", TestBean.class);
        CreatorCollector collector = new CreatorCollector(_beanDescFor(TestBean.class), _mapper.getConfig());

        AnnotatedClass ac = AnnotatedClassHelper.findCreatorFromAnnotation(TestBean.class, _mapper.getAnnotationIntrospector());
        AnnotatedMember delegateMember = AnnotatedClassHelper.findMember(ac, "delegate");
        CreatorProperty[] injectables = new CreatorProperty[] {
                new CreatorProperty(new BeanPropertyDefinition(AccessUtil.getAnnotated(delegateMember, _mapper.getAnnotationIntrospector()), null), null, null)
        };
        AnnotatedWithParams constructor = AnnotatedWithParams.construct(ac.getDeclaredConstructors()[0], new Class[0]);
        collector.addDelegatingCreator(constructor, true, injectables);

        ValueInstantiator inst = collector.constructValueInstantiator(_mapper.getConfig());
        TestBean obj2 = inst.createValueInstance(new Object[]{delegate});
        assertEquals(obj.getValue(), obj2.getValue());
        assertEquals(obj.getStringValue(), obj2.getStringValue());
        assertEquals(obj.getBooleanValue(), obj2.getBooleanValue());
    }

    @Test
    public void testPropertyBasedCreator() throws IOException {
        TestBean obj = _mapper.readValue(
                "{ \"value\": 1, \"stringValue\": \"test\""+
                ", \"booleanValue\": true, \"prop1\": \"value1\""+
                ", \"prop2\": 2 }", TestBean.class);
        CreatorCollector collector = new CreatorCollector(_beanDescFor(TestBean.class), _mapper.getConfig());

        AnnotatedClass ac = AnnotatedClassHelper.findCreatorFromAnnotation(TestBean.class, _mapper.getAnnotationIntrospector());
        BeanPropertyDefinition prop1 = _beanDescFor(TestBean.class).findProperty("prop1");
        CreatorProperty prop1Prop = new CreatorProperty(prop1, null, null);
        BeanPropertyDefinition prop2 = _beanDescFor(TestBean.class).findProperty("prop2");
        CreatorProperty prop2Prop = new CreatorProperty(prop2, null, null);
        SettableBeanProperty[] properties = new SettableBeanProperty[] {
                new SettableBeanProperty(prop1, null, null),
                new SettableBeanProperty(prop2, null, null)
        };
        AnnotatedWithParams constructor = AnnotatedWithParams.construct(ac.getDeclaredConstructors()[0], new Class[0]);
        collector.addPropertyCreator(constructor, true, properties);

        ValueInstantiator inst = collector.constructValueInstantiator(_mapper.getConfig());
        TestBean obj2 = inst.createValueInstance(new Object[]{1, "value1", 2});
        assertEquals(obj.getValue(), obj2.getValue());
        assertEquals(obj.getStringValue(), obj2.getStringValue());
        assertEquals(obj.getBooleanValue(), obj2.getBooleanValue());
        assertEquals(obj.getStringValue("prop1"), obj2.getStringValue("prop1"));
        assertEquals(obj.getIntValue("prop2"), obj2.getIntValue("prop2"));
    }

    private BeanDescription _beanDescFor(Class<?> c) {
        return _mapper.getFactory().findbeanDescription(c, null);
    }
}