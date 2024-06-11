package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Unit tests for {@link CreatorCollector} class.
 */
public class CreatorCollectorTest {

    @Test
    public void testSimpleClass() throws Exception {
        BeanDescription beanDesc = new BeanDescription(MySimpleClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default constructor
        Method defaultCtor = MySimpleClass.class.getDeclaredConstructor();
        AnnotatedMethod amDefaultCtor = new AnnotatedMethod(defaultCtor);
        collector.setDefaultCreator(new AnnotatedWithParams(amDefaultCtor));

        // Test construction
        MySimpleClass obj = (MySimpleClass) collector.constructValueInstantiator(new DeserializationConfig()).createValue(null);
        assertEquals("Default", obj.name);
        assertEquals(42, obj.value);

        // Test string constructor
        Method stringCtor = MySimpleClass.class.getDeclaredConstructor(String.class);
        AnnotatedMethod amStringCtor = new AnnotatedMethod(stringCtor);
        collector.addStringCreator(new AnnotatedWithParams(amStringCtor), false);

        obj = (MySimpleClass) collector.constructValueInstantiator(new DeserializationConfig()).createValue("Hello");
        assertEquals("Hello", obj.name);
        assertEquals("Hello", obj.value);

        // Test int constructor
        Method intCtor = MySimpleClass.class.getDeclaredConstructor(int.class);
        AnnotatedMethod amIntCtor = new AnnotatedMethod(intCtor);
        collector.addIntCreator(new AnnotatedWithParams(amIntCtor), false);

        obj = (MySimpleClass) collector.constructValueInstantiator(new DeserializationConfig()).createValue(42);
        assertEquals("Int", obj.name);
        assertEquals(42, obj.value);

        // Test boolean constructor
        Method booleanCtor = MySimpleClass.class.getDeclaredConstructor(boolean.class);
        AnnotatedMethod amBooleanCtor = new AnnotatedMethod(booleanCtor);
        collector.addBooleanCreator(new AnnotatedWithParams(amBooleanCtor), false);

        obj = (MySimpleClass) collector.constructValueInstantiator(new DeserializationConfig()).createValue(true);
        assertEquals("Boolean", obj.name);
        assertEquals(true, obj.value);
    }

    @Test
    public void testDelegateConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(MyDelegateClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default constructor
        Method defaultCtor = MyDelegateClass.class.getDeclaredConstructor();
        AnnotatedMethod amDefaultCtor = new AnnotatedMethod(defaultCtor);
        collector.setDefaultCreator(new AnnotatedWithParams(amDefaultCtor));

        // Delegate constructor
        Method delegateCtor = MyDelegateClass.class.getDeclaredConstructor(MySimpleClass.class);
        AnnotatedMethod amDelegateCtor = new AnnotatedMethod(delegateCtor);
        CreatorProperty[] delegateArgs = new CreatorProperty[] { new CreatorProperty("simple", AnnotatedParameter.emptyOrNull()) };
        collector.addDelegatingCreator(new AnnotatedWithParams(amDelegateCtor), false, delegateArgs);

        // Test construction with delegate
        MySimpleClass simpleObj = new MySimpleClass();
        simpleObj.name = "Simple";
        simpleObj.value = 42;
        MyDelegateClass obj = (MyDelegateClass) collector.constructValueInstantiator(new DeserializationConfig()).createValue(simpleObj);
        assertEquals("SimpleDelegate", obj.name);
        assertEquals(simpleObj, obj.simple);

        // Test construction without delegate
        obj = (MyDelegateClass) collector.constructValueInstantiator(new DeserializationConfig()).createValue(null);
        assertEquals("DefaultDelegate", obj.name);
        assertNull(obj.simple);
    }

    @Test
    public void testPropertiesConstructor() throws Exception {
        BeanDescription beanDesc = new BeanDescription(MyPropertiesClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default constructor
        Method defaultCtor = MyPropertiesClass.class.getDeclaredConstructor();
        AnnotatedMethod amDefaultCtor = new AnnotatedMethod(defaultCtor);
        collector.setDefaultCreator(new AnnotatedWithParams(amDefaultCtor));

        // Property-based constructor
        Method propertiesCtor = MyPropertiesClass.class.getDeclaredConstructor(String.class, int.class, boolean.class);
        AnnotatedMethod amPropertiesCtor = new AnnotatedMethod(propertiesCtor);
        CreatorProperty[] propertiesArgs = new CreatorProperty[] {
            new CreatorProperty("name", new AnnotatedParameter(propertiesCtor, 0, PropertyName.construct("name"))),
            new CreatorProperty("valueInt", new AnnotatedParameter(propertiesCtor, 1, PropertyName.construct("valueInt"))),
            new CreatorProperty("valueBool", new AnnotatedParameter(propertiesCtor, 2, PropertyName.construct("valueBool")))
        };
        collector.addPropertyCreator(new AnnotatedWithParams(amPropertiesCtor), false, propertiesArgs);

        // Test construction with properties
        MyPropertiesClass obj = (MyPropertiesClass) collector.constructValueInstantiator(new DeserializationConfig()).createValue("Simple", 42, true);
        assertEquals("SimpleProperties", obj.name);
        assertEquals("Simple", obj.nameProperty);
        assertEquals(42, obj.valueInt);
        assertEquals(true, obj.valueBool);

        // Test construction without properties
        obj = (MyPropertiesClass) collector.constructValueInstantiator(new DeserializationConfig()).createValue(null);
        assertEquals("DefaultProperties", obj.name);
        assertNull(obj.nameProperty);
        assertEquals(0, obj.valueInt);
        assertEquals(false, obj.valueBool);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateConstructors() throws Exception {
        BeanDescription beanDesc = new BeanDescription(MyDuplicateClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default constructor
        Method defaultCtor = MyDuplicateClass.class.getDeclaredConstructor();
        AnnotatedMethod amDefaultCtor = new AnnotatedMethod(defaultCtor);
        collector.setDefaultCreator(new AnnotatedWithParams(amDefaultCtor));

        // String constructor
        Method stringCtor = MyDuplicateClass.class.getDeclaredConstructor(String.class);
        AnnotatedMethod amStringCtor = new AnnotatedMethod(stringCtor);
        collector.addStringCreator(new AnnotatedWithParams(amStringCtor), false);

        // Int constructor
        Method intCtor = MyDuplicateClass.class.getDeclaredConstructor(int.class);
        AnnotatedMethod amIntCtor = new AnnotatedMethod(intCtor);
        collector.addIntCreator(new AnnotatedWithParams(amIntCtor), false);

        // Test creation
        collector.constructValueInstantiator(new DeserializationConfig());

        // Should not reach this point
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateExplicitConstructors() throws Exception {
        BeanDescription beanDesc = new BeanDescription(MyDuplicateClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default constructor
        Method defaultCtor = MyDuplicateClass.class.getDeclaredConstructor();
        AnnotatedMethod amDefaultCtor = new AnnotatedMethod(defaultCtor);
        collector.setDefaultCreator(new AnnotatedWithParams(amDefaultCtor));

        // String constructor
        Method stringCtor = MyDuplicateClass.class.getDeclaredConstructor(String.class);
        AnnotatedMethod amStringCtor = new AnnotatedMethod(stringCtor);
        collector.addStringCreator(new AnnotatedWithParams(amStringCtor), true);

        // Int constructor
        Method intCtor = MyDuplicateClass.class.getDeclaredConstructor(int.class);
        AnnotatedMethod amIntCtor = new AnnotatedMethod(intCtor);
        collector.addIntCreator(new AnnotatedWithParams(amIntCtor), true);

        // Test creation
        collector.constructValueInstantiator(new DeserializationConfig());

        // Should not reach this point
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyConstructors() throws Exception {
        BeanDescription beanDesc = new BeanDescription(MyDuplicatePropertiesClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        // Default constructor
        Method defaultCtor = MyDuplicatePropertiesClass.class.getDeclaredConstructor();
        AnnotatedMethod amDefaultCtor = new AnnotatedMethod(defaultCtor);
        collector.setDefaultCreator(new AnnotatedWithParams(amDefaultCtor));

        // Property-based constructor 1
        Method propertiesCtor1 = MyDuplicatePropertiesClass.class.getDeclaredConstructor(String.class, int.class, boolean.class);
        AnnotatedMethod amPropertiesCtor1 = new AnnotatedMethod(propertiesCtor1);
        CreatorProperty[] propertiesArgs1 = new CreatorProperty[] {
            new CreatorProperty("name1", new AnnotatedParameter(propertiesCtor1, 0, PropertyName.construct("name1"))),
            new CreatorProperty("valueInt1", new AnnotatedParameter(propertiesCtor1, 1, PropertyName.construct("valueInt1"))),
            new CreatorProperty("valueBool1", new AnnotatedParameter(propertiesCtor1, 2, PropertyName.construct("valueBool1")))
        };
        collector.addPropertyCreator(new AnnotatedWithParams(amPropertiesCtor1), false, propertiesArgs1);

        // Property-based constructor 2
        Method propertiesCtor2 = MyDuplicatePropertiesClass.class.getDeclaredConstructor(String.class, int.class, boolean.class);
        AnnotatedMethod amPropertiesCtor2 = new AnnotatedMethod(propertiesCtor2);
        CreatorProperty[] propertiesArgs2 = new CreatorProperty[] {
            new CreatorProperty("name2", new AnnotatedParameter(propertiesCtor2, 0, PropertyName.construct("name2"))),
            new CreatorProperty("valueInt2", new AnnotatedParameter(propertiesCtor2, 1, PropertyName.construct("valueInt2"))),
            new CreatorProperty("valueBool2", new AnnotatedParameter(propertiesCtor2, 2, PropertyName.construct("valueBool2")))
        };
        collector.addPropertyCreator(new AnnotatedWithParams(amPropertiesCtor2), false, propertiesArgs2);

        // Test creation
        collector.constructValueInstantiator(new DeserializationConfig());

        // Should not reach this point
    }

    private static class MySimpleClass {
        private String name;
        private int value;

        @JsonCreator
        public MySimpleClass() {
            this.name = "Default";
            this.value = 42;
        }

        public MySimpleClass(String name) {
            this.name = name;
        }

        public MySimpleClass(int value) {
            this.value = value;
        }

        public MySimpleClass(boolean value) {
            this.value = value ? 1 : 0;
        }
    }

    private static class MyDelegateClass {
        private String name;
        private MySimpleClass simple;

        @JsonCreator
        public MyDelegateClass(MySimpleClass simple) {
            this.name = "SimpleDelegate";
            this.simple = simple;
        }

        public MyDelegateClass() {
            this.name = "DefaultDelegate";
        }
    }

    private static class MyPropertiesClass {
        private String name;
        private String nameProperty;
        private int valueInt;
        private boolean valueBool;

        @JsonCreator
        public MyPropertiesClass(String name, int valueInt, boolean valueBool) {
            this.name = name;
            this.nameProperty = name;
            this.valueInt = valueInt;
            this.valueBool = valueBool;
        }
    }

    private static class MyDuplicateClass {
        private String name;

        @JsonCreator
        public MyDuplicateClass() {
            this.name = "Default";
        }

        public MyDuplicateClass(String name) {
            this.name = name;
        }
    }

    private static class MyDuplicatePropertiesClass {
        private String name1;
        private String name2;
        private int valueInt1;
        private int valueInt2;
        private boolean valueBool1;
        private boolean valueBool2;

        @JsonCreator
        public MyDuplicatePropertiesClass(String name1, int valueInt1, boolean valueBool1) {
            this.name1 = name1;
            this.name2 = name1;
            this.valueInt1 = valueInt1;
            this.valueBool1 = valueBool1;
        }

        public MyDuplicatePropertiesClass(String name2, int valueInt2, boolean valueBool2) {
            this.name1 = name2;
            this.name2 = name2;
            this.valueInt1 = valueInt2;
            this.valueBool1 = valueBool2;
        }
    }
}