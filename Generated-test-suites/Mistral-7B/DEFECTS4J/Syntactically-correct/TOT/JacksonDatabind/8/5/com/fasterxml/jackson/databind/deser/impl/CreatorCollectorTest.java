package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectionWithDefaultConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithDefaultConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Check default constructor
        collector.setDefaultCreator(null);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        Object createdInstance = inst.createValue(null);
        assertNotNull(createdInstance);
    }

    @Test
    public void testCreatorCollectionWithStringConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithStringConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        String constructorString = "Foo";

        AnnotatedWithParams stringCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithStringConstructor.class, "constructorString", null),
                new ParameterName("constructorString"), new String[] { "constructorString" },
                new Object[] { constructorString }, new AnnotatedClass(TestClassWithStringConstructor.class),
                null);

        collector.addStringCreator(stringCreator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        Object createdInstance = inst.createValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals("Foo", createdInstance);
    }

    @Test
    public void testCreatorCollectionWithIntConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithIntConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        int constructorInt = 1;

        AnnotatedWithParams intCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithIntConstructor.class, "constructorInt", null),
                new ParameterName("constructorInt"), new String[] { "constructorInt" },
                new Object[] { constructorInt }, new AnnotatedClass(TestClassWithIntConstructor.class),
                null);

        collector.addIntCreator(intCreator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        Object createdInstance = inst.createValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(constructorInt, createdInstance);
    }

    @Test
    public void testCreatorCollectionWithLongConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithLongConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        long constructorLong = 1L;

        AnnotatedWithParams longCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithLongConstructor.class, "constructorLong", null),
                new ParameterName("constructorLong"), new String[] { "constructorLong" },
                new Object[] { constructorLong }, new AnnotatedClass(TestClassWithLongConstructor.class),
                null);

        collector.addLongCreator(longCreator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        Object createdInstance = inst.createValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(constructorLong, createdInstance);
    }

    @Test
    public void testCreatorCollectionWithDoubleConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithDoubleConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        double constructorDouble = 1.0;

        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithDoubleConstructor.class, "constructorDouble", null),
                new ParameterName("constructorDouble"), new String[] { "constructorDouble" },
                new Object[] { constructorDouble }, new AnnotatedClass(TestClassWithDoubleConstructor.class),
                null);

        collector.addDoubleCreator(doubleCreator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        Object createdInstance = inst.createValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(constructorDouble, createdInstance);
    }

    @Test
    public void testCreatorCollectionWithBooleanConstructor() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithBooleanConstructor.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        boolean constructorBoolean = true;

        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithBooleanConstructor.class, "constructorBoolean", null),
                new ParameterName("constructorBoolean"), new String[] { "constructorBoolean" },
                new Object[] { constructorBoolean }, new AnnotatedClass(TestClassWithBooleanConstructor.class),
                null);

        collector.addBooleanCreator(booleanCreator, false);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        Object createdInstance = inst.createValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(constructorBoolean, createdInstance);
    }

    @Test
    public void testCreatorCollectionWithDelegateCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithDelegateCreator.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        String delegateClassName = "java.lang.String";
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithDelegateCreator.class, "delegateCreator", null),
                new ParameterName("delegate"), new String[] { "delegate" },
                new Object[] { mapper.readValue(new ObjectNode(mapper.createObjectNode()), String.class) },
                new AnnotatedClass(TestClassWithDelegateCreator.class),
                new CreatorProperty[] { new CreatorProperty("delegate", null) });

        collector.addDelegatingCreator(delegateCreator, false, null);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        Object createdInstance = inst.createValue(new ObjectNode(mapper.createObjectNode()));
        assertNotNull(createdInstance);
    }

    @Test
    public void testCreatorCollectionWithPropertyBasedCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithPropertyBasedCreator.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        CreatorProperty property1 = new CreatorProperty("property1", new JsonProperty("prop1"));
        CreatorProperty property2 = new CreatorProperty("property2", new JsonProperty("prop2"));

        AnnotatedWithParams propertyBasedCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithPropertyBasedCreator.class, "propertyBasedCreator", null),
                new ParameterName(""), new String[] {}, null, null,
                new AnnotatedClass(TestClassWithPropertyBasedCreator.class), null);

        collector.addPropertyCreator(propertyBasedCreator, false, new CreatorProperty[] { property1, property2 });

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        ObjectNode node = mapper.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop2", "value2");

        Object createdInstance = inst.createValue(node);
        assertNotNull(createdInstance);

        TestClassWithPropertyBasedCreator testInstance = (TestClassWithPropertyBasedCreator) createdInstance;
        assertEquals("value1", testInstance.getProperty1());
        assertEquals("value2", testInstance.getProperty2());
    }

    @Test
    public void testCreatorCollectionWithMixedCreators() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.introspect(TestClassWithMixedCreators.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // String constructor
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithMixedCreators.class, "constructorString", null),
                new ParameterName("constructorString"), new String[] { "constructorString" },
                new Object[] { "Foo" }, new AnnotatedClass(TestClassWithMixedCreators.class),
                null);

        collector.addStringCreator(stringCreator, false);

        // Int constructor
        AnnotatedWithParams intCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithMixedCreators.class, "constructorInt", null),
                new ParameterName("constructorInt"), new String[] { "constructorInt" },
                new Object[] { 1 }, new AnnotatedClass(TestClassWithMixedCreators.class),
                null);

        collector.addIntCreator(intCreator, false);

        // Property-based constructor
        CreatorProperty property1 = new CreatorProperty("property1", new JsonProperty("prop1"));
        CreatorProperty property2 = new CreatorProperty("property2", new JsonProperty("prop2"));

        AnnotatedWithParams propertyBasedCreator = new AnnotatedWithParams(
                new AnnotatedMethod(TestClassWithMixedCreators.class, "propertyBasedCreator", null),
                new ParameterName(""), new String[] {}, null, null,
                new AnnotatedClass(TestClassWithMixedCreators.class), null);

        collector.addPropertyCreator(propertyBasedCreator, false, new CreatorProperty[] { property1, property2 });

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNotNull(inst);

        Object createdInstance1 = inst.createValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals("Foo", createdInstance1);

        ObjectNode node = mapper.createObjectNode();
        node.put("prop1", "value1");
        node.put("prop2", "value2");

        Object createdInstance2 = inst.createValue(node);
        assertNotNull(createdInstance2);

        TestClassWithMixedCreators testInstance1 = (TestClassWithMixedCreators) createdInstance1;
        assertEquals("Foo", testInstance1.getProperty1());
        assertEquals(null, testInstance1.getProperty2());

        TestClassWithMixedCreators testInstance2 = (TestClassWithMixedCreators) createdInstance2;
        assertEquals(null, testInstance2.getProperty1());
        assertEquals("value1", testInstance2.getProperty2());
    }
}

class TestClassWithDefaultConstructor {
}

class TestClassWithStringConstructor {
    @JsonCreator
    public TestClassWithStringConstructor(String constructorString) {
        this.constructorString = constructorString;
    }

    public String constructorString;
}

class TestClassWithIntConstructor {
    @JsonCreator
    public TestClassWithIntConstructor(int constructorInt) {
        this.constructorInt = constructorInt;
    }

    public int constructorInt;
}

class TestClassWithLongConstructor {
    @JsonCreator
    public TestClassWithLongConstructor(long constructorLong) {
        this.constructorLong = constructorLong;
    }

    public long constructorLong;
}

class TestClassWithDoubleConstructor {
    @JsonCreator
    public TestClassWithDoubleConstructor(double constructorDouble) {
        this.constructorDouble = constructorDouble;
    }

    public double constructorDouble;
}

class TestClassWithBooleanConstructor {
    @JsonCreator
    public TestClassWithBooleanConstructor(boolean constructorBoolean) {
        this.constructorBoolean = constructorBoolean;
    }

    public boolean constructorBoolean;
}

class TestClassWithDelegateCreator {
    @JsonCreator
    public TestClassWithDelegateCreator(@JsonProperty("delegate") String delegate) {
        this.delegate = delegate;
    }

    public String delegate;
}

class TestClassWithPropertyBasedCreator {
    @JsonCreator
    public TestClassWithPropertyBasedCreator(
            @JsonProperty("property1") String prop1,
            @JsonProperty("property2") String prop2) {
        this.property1 = prop1;
        this.property2 = prop2;
    }

    public String property1;
    public String property2;
}

class TestClassWithMixedCreators {
    @JsonCreator
    public TestClassWithMixedCreators(@JsonProperty("constructorString") String constructorString) {
        this.property1 = "default";
        this.property2 = null;
        this.constructorString = constructorString;
    }

    @JsonCreator
    public TestClassWithMixedCreators(int constructorInt) {
        this.property1 = null;
        this.property2 = "default";
        this.constructorInt = constructorInt;
    }

    @JsonProperty
    public String getProperty1() {
        return property1;
    }

    @JsonProperty
    public String getProperty2() {
        return property2;
    }

    private String property1;
    private String property2;
}