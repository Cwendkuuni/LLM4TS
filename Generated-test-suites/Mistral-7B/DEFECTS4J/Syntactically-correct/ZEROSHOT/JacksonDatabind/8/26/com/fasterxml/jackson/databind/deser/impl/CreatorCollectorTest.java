package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private static class TestBean {
        private int intValue;
        private String stringValue;
        private boolean booleanValue;
        private TestOther testOther;

        @JsonCreator(mode = Mode.PROPERTIES)
        public TestBean(@JsonProperty("intValue") int intValue,
                @JsonProperty("stringValue") String stringValue,
                @JsonProperty("booleanValue") boolean booleanValue,
                @JsonProperty("testOther") TestOther testOther) {
            this.intValue = intValue;
            this.stringValue = stringValue;
            this.booleanValue = booleanValue;
            this.testOther = testOther;
        }

        public TestBean() {
        }

        @JsonProperty
        public int getIntValue() {
            return intValue;
        }

        @JsonProperty
        public String getStringValue() {
            return stringValue;
        }

        @JsonProperty
        public boolean getBooleanValue() {
            return booleanValue;
        }

        @JsonProperty
        public TestOther getTestOther() {
            return testOther;
        }

        public void setTestOther(TestOther testOther) {
            this.testOther = testOther;
        }
    }

    private static class TestOther {
        private String otherStringValue;

        public TestOther(String otherStringValue) {
            this.otherStringValue = otherStringValue;
        }

        @JsonProperty
        public String getOtherStringValue() {
            return otherStringValue;
        }

        public void setOtherStringValue(String otherStringValue) {
            this.otherStringValue = otherStringValue;
        }
    }

    private BeanDescription _beanDesc;
    private CreatorCollector _collector;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        _beanDesc = mapper.getTypeFactory().constructType(TestBean.class);
        _collector = new CreatorCollector(_beanDesc, true);
    }

    @Test
    public void testDefaultConstructor() {
        _collector.setDefaultCreator(ValueIntrospector.findDefaultConstructor(_beanDesc.getRawClass()));
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean testBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), null);
        assertTrue(testBean instanceof TestBean);
        assertEquals(0, testBean.getIntValue());
        assertNull(testBean.getStringValue());
        assertFalse(testBean.booleanValue);
        assertNull(testBean.getTestOther());
    }

    @Test
    public void testStringConstructor() throws Exception {
        TestBean testBean = new TestBean("1", "testString", true, new TestOther("testOtherString"));
        AnnotatedConstructor constructor = ClassIntrospector.findConstructor(_beanDesc.getRawClass(), TestBean.class);
        _collector.setStringCreator(new AnnotatedWithParams(constructor, null, null));
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instantiatedBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), null);
        assertEquals(testBean.getIntValue(), instantiatedBean.getIntValue());
        assertEquals(testBean.getStringValue(), instantiatedBean.getStringValue());
        assertEquals(testBean.booleanValue, instantiatedBean.booleanValue);
        assertEquals(testBean.getTestOther(), instantiatedBean.getTestOther());
    }

    @Test
    public void testIntConstructor() throws Exception {
        TestBean testBean = new TestBean(1, "", false, null);
        AnnotatedConstructor constructor = ClassUtil.findConstructor(_beanDesc.getRawClass(), TestBean.class, int.class);
        _collector.setIntCreator(new AnnotatedWithParams(constructor, null, null));
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instantiatedBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), null);
        assertEquals(testBean.getIntValue(), instantiatedBean.getIntValue());
        assertNull(instantiatedBean.getStringValue());
        assertFalse(instantiatedBean.booleanValue);
        assertNull(instantiatedBean.getTestOther());
    }

    @Test
    public void testLongConstructor() throws Exception {
        TestBean testBean = new TestBean(1L, "", false, null);
        AnnotatedConstructor constructor = ClassUtil.findConstructor(_beanDesc.getRawClass(), TestBean.class, long.class);
        _collector.setLongCreator(new AnnotatedWithParams(constructor, null, null));
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instantiatedBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), null);
        assertEquals(testBean.getIntValue(), instantiatedBean.getIntValue());
        assertNull(instantiatedBean.getStringValue());
        assertFalse(instantiatedBean.booleanValue);
        assertNull(instantiatedBean.getTestOther());
    }

    @Test
    public void testDoubleConstructor() throws Exception {
        TestBean testBean = new TestBean(1.0, "", false, null);
        AnnotatedConstructor constructor = ClassUtil.findConstructor(_beanDesc.getRawClass(), TestBean.class, double.class);
        _collector.setDoubleCreator(new AnnotatedWithParams(constructor, null, null));
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instantiatedBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), null);
        assertEquals(testBean.getIntValue(), instantiatedBean.getIntValue());
        assertNull(instantiatedBean.getStringValue());
        assertFalse(instantiatedBean.booleanValue);
        assertNull(instantiatedBean.getTestOther());
    }

    @Test
    public void testBooleanConstructor() throws Exception {
        TestBean testBean = new TestBean(true, "", false, null);
        AnnotatedConstructor constructor = ClassUtil.findConstructor(_beanDesc.getRawClass(), TestBean.class, boolean.class);
        _collector.setBooleanCreator(new AnnotatedWithParams(constructor, null, null));
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instantiatedBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), null);
        assertEquals(testBean.getIntValue(), instantiatedBean.getIntValue());
        assertNull(instantiatedBean.getStringValue());
        assertTrue(instantiatedBean.booleanValue);
        assertNull(instantiatedBean.getTestOther());
    }

    @Test
    public void testDelegateConstructor() throws Exception {
        TestDelegate testDelegate = new TestDelegate();
        TestBean testBean = new TestBean(testDelegate);
        AnnotatedConstructor constructor = ClassUtil.findConstructor(_beanDesc.getRawClass(), TestBean.class, TestDelegate.class);
        _collector.addDelegatingCreator(new AnnotatedWithParams(constructor, null, null), null, new CreatorProperty[0]);
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instantiatedBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), null);
        assertEquals(testBean.getIntValue(), instantiatedBean.getIntValue());
        assertNull(instantiatedBean.getStringValue());
        assertSame(testDelegate, instantiatedBean.getTestOther());
    }

    @Test
    public void testPropertyBasedConstructor() throws Exception {
        TestBean testBean = new TestBean(1, "testString", true, new TestOther("testOtherString"));
        BeanPropertyDefinition intPropDefinition = _beanDesc.findPropertyDefinition("intValue");
        BeanPropertyDefinition stringPropDefinition = _beanDesc.findPropertyDefinition("stringValue");
        BeanPropertyDefinition booleanPropDefinition = _beanDesc.findPropertyDefinition("booleanValue");
        BeanPropertyDefinition testOtherPropDefinition = _beanDesc.findPropertyDefinition("testOther");
        AnnotatedMethod intSetter = ClassIntrospector.findMethod(_beanDesc.getRawClass(), "setIntValue", int.class);
        AnnotatedMethod stringSetter = ClassIntrospector.findMethod(_beanDesc.getRawClass(), "setStringValue", String.class);
        AnnotatedMethod booleanSetter = ClassIntrospector.findMethod(_beanDesc.getRawClass(), "setBooleanValue", boolean.class);
        AnnotatedMethod testOtherSetter = ClassIntrospector.findMethod(_beanDesc.getRawClass(), "setTestOther", TestOther.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("intValue", 1);
        node.put("stringValue", "testString");
        node.put("booleanValue", true);
        JavaType valueType = TypeFactory.defaultInstance().constructType(TestOther.class);
        ObjectNode testOtherNode = mapper.createObjectNode();
        testOtherNode.put("otherStringValue", "testOtherString");
        node.set("testOther", mapper.valueToTree(testOtherNode));
        _collector.addPropertyCreator(new AnnotatedWithParams(intSetter, null, new CreatorProperty[]{ intPropDefinition }), false, new CreatorProperty[]{ intPropDefinition });
        _collector.addPropertyCreator(new AnnotatedWithParams(stringSetter, null, new CreatorProperty[]{ stringPropDefinition }), false, new CreatorProperty[]{ stringPropDefinition });
        _collector.addPropertyCreator(new AnnotatedWithParams(booleanSetter, null, new CreatorProperty[]{ booleanPropDefinition }), false, new CreatorProperty[]{ booleanPropDefinition });
        _collector.addPropertyCreator(new AnnotatedWithParams(testOtherSetter, null, new CreatorProperty[]{ testOtherPropDefinition }), false, new CreatorProperty[]{ testOtherPropDefinition });
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instantiatedBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), node);
        assertEquals(testBean.getIntValue(), instantiatedBean.getIntValue());
        assertEquals(testBean.getStringValue(), instantiatedBean.getStringValue());
        assertEquals(testBean.booleanValue, instantiatedBean.booleanValue);
        assertSame(testBean.getTestOther(), instantiatedBean.getTestOther());
    }

    @Test(expected = JsonMappingException.class)
    public void testDuplicateConstructor() throws Exception {
        TestBean testBean = new TestBean("1", "testString", true, new TestOther("testOtherString"));
        AnnotatedConstructor intConstructor = ClassUtil.findConstructor(_beanDesc.getRawClass(), TestBean.class, int.class);
        AnnotatedConstructor stringConstructor = ClassUtil.findConstructor(_beanDesc.getRawClass(), TestBean.class, String.class);
        _collector.setIntCreator(new AnnotatedWithParams(intConstructor, null, null));
        _collector.setStringCreator(new AnnotatedWithParams(stringConstructor, null, null));
        ValueInstantiator instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
        TestBean instantiatedBean = instantiator.createValueInstance(new DeserializationContext(_beanDesc, null, null), null);
        assertEquals(testBean.getIntValue(), instantiatedBean.getIntValue());
        assertEquals(testBean.getStringValue(), instantiatedBean.getStringValue());
        assertEquals(testBean.booleanValue, instantiatedBean.booleanValue);
        assertEquals(testBean.getTestOther(), instantiatedBean.getTestOther());
        _collector.setIntCreator(new AnnotatedWithParams(intConstructor, null, null)); // duplicate
        instantiator = _collector.constructValueInstantiator(new DeserializationConfig());
    }
}

class AnnotatedWithParams implements Annotated {
    private AnnotatedMember _member;
    private Object _value;

    public AnnotatedWithParams(AnnotatedMember member, Object value) {
        _member = member;
        _value = value;
    }

    @Override
    public Annotated getAnnotated() {
        return _member;
    }

    @Override
    public Object getValue() {
        return _value;
    }
}