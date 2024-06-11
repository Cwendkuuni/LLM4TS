package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector.AnnotatedWithParams;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector.Vanilla;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for CreatorCollector
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        BeanDescription beanDesc = new BeanDescription(new AnnotatedClass(TestClass.class), ObjectMapper.getDefaultInstance());
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        // Test setting default creator
        AnnotatedConstructor defaultConstructor = TestClass.class.getDeclaredConstructor();
        Method defaultMethod = defaultConstructor.getJavaMember();
        creatorCollector.setDefaultCreator(new AnnotatedWithParams(defaultMethod, null, null, null, null));

        // Test setting string creator
        Method stringMethod = TestClass.class.getDeclaredMethod("stringCreator");
        creatorCollector.addStringCreator(new AnnotatedWithParams(stringMethod, null, null, null, null), false);

        // Test setting int creator
        Method intMethod = TestClass.class.getDeclaredMethod("intCreator");
        creatorCollector.addIntCreator(new AnnotatedWithParams(intMethod, null, null, null, null), false);

        // Test setting long creator
        Method longMethod = TestClass.class.getDeclaredMethod("longCreator");
        creatorCollector.addLongCreator(new AnnotatedWithParams(longMethod, null, null, null, null), false);

        // Test setting double creator
        Method doubleMethod = TestClass.class.getDeclaredMethod("doubleCreator");
        creatorCollector.addDoubleCreator(new AnnotatedWithParams(doubleMethod, null, null, null, null), false);

        // Test setting boolean creator
        Method booleanMethod = TestClass.class.getDeclaredMethod("booleanCreator");
        creatorCollector.addBooleanCreator(new AnnotatedWithParams(booleanMethod, null, null, null, null), false);

        // Test setting delegate creator
        AnnotatedConstructor delegateConstructor = TestClass.class.getDeclaredConstructor(TestClass.class);
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(delegateConstructor, null, new CreatorProperty[0]), false, new CreatorProperty[0]);

        // Test setting property creator
        BeanProperty property = TestClass.class.findProperty("propertyName");
        AnnotatedMethod propertyMethod = property.getMethod();
        creatorCollector.addPropertyCreator(new AnnotatedWithParams(propertyMethod, null, new CreatorProperty[]{new CreatorProperty(property, null)}), false, new CreatorProperty[0]);

        // Test setting incomplete parameter
        AnnotatedParameter incompleteParameter = TestClass.class.getDeclaredParameter(1);
        creatorCollector.addIncompleteParameter(incompleteParameter);

        // Test creating ValueInstantiator
        DeserializationConfig config = ObjectMapper.getDefaultInstance().getConfig();
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);

        // Test getValueTypeDesc
        assertEquals(Vanilla.TYPE_COLLECTION, valueInstantiator.getClass().getField("_type").getInt(valueInstantiator));
        assertEquals(ArrayList.class.getName(), valueInstantiator.getValueTypeDesc());

        // Test canInstantiate
        assertTrue(valueInstantiator.canInstantiate());

        // Test canCreateUsingDefault
        assertTrue(valueInstantiator.canCreateUsingDefault());

        // Test createUsingDefault
        Object defaultInstance = valueInstantiator.createUsingDefault(null);
        assertNotNull(defaultInstance);

        // Test hasDefaultCreator
        assertTrue(creatorCollector.hasDefaultCreator());

        // Test toString
        String toString = creatorCollector.toString();
        assertNotNull(toString);

        // Test verifyNonDup
        Method stringMethod2 = TestClass.class.getDeclaredMethod("stringCreator2");
        creatorCollector.addStringCreator(new AnnotatedWithParams(stringMethod2, null, null, null, null), false);
        try {
            creatorCollector.verifyNonDup(new AnnotatedWithParams(stringMethod, null, null, null, null), C_STRING, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test hasNonDefaultCreator
        assertFalse(creatorCollector.hasNonDefaultCreator());

        // Test setDelegatingCreator
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(delegateConstructor, null, new CreatorProperty[0]), true, new CreatorProperty[0]);
        assertTrue(creatorCollector.hasNonDefaultCreator());

        // Test setPropertyCreator
        creatorCollector.addPropertyCreator(new AnnotatedWithParams(propertyMethod, null, new CreatorProperty[]{new CreatorProperty(property, null)}), true, new CreatorProperty[0]);
        assertTrue(creatorCollector.hasNonDefaultCreator());

        // Test deprecated methods
        AnnotatedWithParams stringMethod3 = new AnnotatedWithParams(stringMethod, null, null, null, null);
        creatorCollector.addStringCreator(stringMethod3);
        creatorCollector.addIntCreator(intMethod);
        creatorCollector.addLongCreator(longMethod);
        creatorCollector.addDoubleCreator(doubleMethod);
        creatorCollector.addBooleanCreator(booleanMethod);
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(delegateConstructor, null), new CreatorProperty[0]);
        creatorCollector.addPropertyCreator(new AnnotatedWithParams(propertyMethod, null), new CreatorProperty[0]);
        try {
            creatorCollector.verifyNonDup(new AnnotatedWithParams(stringMethod, null), C_STRING);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test getExplicitCreators
        int explicitCreators = creatorCollector.getExplicitCreators();
        assertEquals(7, explicitCreators);

        // Test getCreators
        AnnotatedWithParams[] creators = creatorCollector.getCreators();
        assertEquals(8, creators.length);

        // Test configureFromObjectSettings
        JavaType delegateType = TypeBindings.constructType(TestClass.class, new Class[]{TestClass.class});
        creatorCollector.constructValueInstantiator(config);
        assertEquals(delegateType, creatorCollector.getValueInstantiator().getDelegateType());
        assertEquals(creatorCollector._delegateArgs, creatorCollector.getValueInstantiator()._delegateArgs);

        // Test configureFromStringCreator
        String stringValue = "stringValue";
        creatorCollector.constructValueInstantiator(config);
        assertEquals(stringValue, creatorCollector.getValueInstantiator().getValueInstantiator().getValueFromString(null, stringValue));

        // Test configureFromIntCreator
        int intValue = 1;
        creatorCollector.constructValueInstantiator(config);
        assertEquals(intValue, creatorCollector.getValueInstantiator().getValueInstantiator().getValueFromInt(null, intValue));

        // Test configureFromLongCreator
        long longValue = 2L;
        creatorCollector.constructValueInstantiator(config);
        assertEquals(longValue, creatorCollector.getValueInstantiator().getValueInstantiator().getValueFromLong(null, longValue));

        // Test configureFromDoubleCreator
        double doubleValue = 3.0;
        creatorCollector.constructValueInstantiator(config);
        assertEquals(doubleValue, creatorCollector.getValueInstantiator().getValueInstantiator().getValueFromNumber(null, doubleValue));

        // Test configureFromBooleanCreator
        boolean booleanValue = true;
        creatorCollector.constructValueInstantiator(config);
        assertEquals(booleanValue, creatorCollector.getValueInstantiator().getValueInstantiator().getValueFromBoolean(null, booleanValue));

        // Test configureIncompleteParameter
        String incompleteParameterValue = "incompleteParameterValue";
        creatorCollector.constructValueInstantiator(config);
        assertEquals(incompleteParameterValue, creatorCollector.getValueInstantiator().getValueInstantiator().getValueFromIncomplete(null, creatorCollector._incompleteParameter));
    }

    @JsonTypeName("TestClass")
    static class TestClass {
        private TestClass() {}

        @JsonCreator
        public TestClass(@JsonProperty("stringValue") String stringValue) {
            this.stringValue = stringValue;
        }

        @JsonProperty
        private String stringValue;

        @JsonCreator
        public TestClass(@JsonProperty("intValue") int intValue) {
            this.intValue = intValue;
        }

        @JsonProperty
        private int intValue;

        @JsonCreator
        public TestClass(@JsonProperty("longValue") long longValue) {
            this.longValue = longValue;
        }

        @JsonProperty
        private long longValue;

        @JsonCreator
        public TestClass(@JsonProperty("doubleValue") double doubleValue) {
            this.doubleValue = doubleValue;
        }

        @JsonProperty
        private double doubleValue;

        @JsonCreator
        public TestClass(@JsonProperty("booleanValue") boolean booleanValue) {
            this.booleanValue = booleanValue;
        }

        @JsonProperty
        private boolean booleanValue;

        @JsonCreator
        public TestClass(@JsonProperty("delegate") TestClass delegate) {
            this.delegate = delegate;
        }

        @JsonProperty
        private TestClass delegate;

        @JsonCreator
        public TestClass(@JsonProperty("propertyName") String propertyName) {
            this.propertyName = propertyName;
        }

        @JsonProperty
        private String propertyName;

        @JsonProperty
        public String getStringValue() {
            return stringValue;
        }

        @JsonProperty
        public int getIntValue() {
            return intValue;
        }

        @JsonProperty
        public long getLongValue() {
            return longValue;
        }

        @JsonProperty
        public double getDoubleValue() {
            return doubleValue;
        }

        @JsonProperty
        public boolean isBooleanValue() {
            return booleanValue;
        }

        @JsonProperty
        public TestClass getDelegate() {
            return delegate;
        }

        @JsonProperty
        public String getPropertyName() {
            return propertyName;
        }

        @JsonProperty(value = "stringCreator")
        static String stringCreator() {
            return "stringCreatorValue";
        }

        @JsonProperty(value = "intCreator")
        static int intCreator(@JsonParameter int i) {
            return i * 2;
        }

        @JsonProperty(value = "longCreator")
        static long longCreator(@JsonParameter long l) {
            return l * 2;
        }

        @JsonProperty(value = "doubleCreator")
        static double doubleCreator(@JsonParameter double d) {
            return d * 2;
        }

        @JsonProperty(value = "booleanCreator")
        static boolean booleanCreator(@JsonParameter boolean b) {
            return !b;
        }

        @JsonProperty(value = "propertyCreator")
        static TestClass propertyCreator(@JsonProperty("propertyName") String propertyName) {
            return new TestClass(propertyName);
        }
    }
}