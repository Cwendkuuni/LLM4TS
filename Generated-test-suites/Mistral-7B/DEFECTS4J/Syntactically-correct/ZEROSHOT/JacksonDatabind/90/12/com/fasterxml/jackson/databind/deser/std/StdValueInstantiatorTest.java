package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test class for com.fasterxml.jackson.databind.deser.std.StdValueInstantiator
 */
@JacksonStdImpl
public class StdValueInstantiatorTest {

    @Test
    public void testStdValueInstantiator() throws IOException, JsonProcessingException {
        StdValueInstantiator stdValueInstantiator = new StdValueInstantiator(null, null);

        // Test default constructor
        assertEquals("UNKNOWN TYPE", stdValueInstantiator.getValueTypeDesc());
        assertNull(stdValueInstantiator.getDefaultCreator());

        // Test constructor with JavaType
        AnnotatedClass ac = AnnotatedClass.construct(TestClassWithDefaultConstructor.class);
        JavaType valueType = ac.getConstructors().get(0).getGenericParameterTypes()[0];
        StdValueInstantiator stdValueInstantiator1 = new StdValueInstantiator(null, valueType);
        assertEquals(valueType.toString(), stdValueInstantiator1.getValueTypeDesc());
        assertNotNull(stdValueInstantiator1.getDefaultCreator());

        // Test copy constructor
        AnnotatedConstructor constructor = ac.getConstructors().get(0);
        AnnotatedParameter[] parameters = constructor.getParameters();
        ObjectNode node = ObjectMapper.getDefaultMapper().createObjectNode();
        node.put("param1", "value1");
        node.put("param2", 1);
        ObjectNode node2 = ObjectMapper.getDefaultMapper().createObjectNode();
        node2.put("param1", "value2");
        node2.put("param2", 2);
        TestClassWithDefaultConstructor testClassWithDefaultConstructor1 = stdValueInstantiator1.createFromObjectWith(
                ObjectMapper.getDefaultMapper().getDeserializationContext().forDeserialization(node), new Object[]{});
        TestClassWithDefaultConstructor testClassWithDefaultConstructor2 = stdValueInstantiator1.createFromObjectWith(
                ObjectMapper.getDefaultMapper().getDeserializationContext().forDeserialization(node2), new Object[]{});
        assertEquals(testClassWithDefaultConstructor1.getParam1(), testClassWithDefaultConstructor2.getParam1());
        assertEquals(testClassWithDefaultConstructor1.getParam2(), testClassWithDefaultConstructor2.getParam2());

        // Test configureFromObjectSettings
        AnnotatedClass ac2 = AnnotatedClass.construct(TestClassWithParamsConstructor.class);
        JavaType valueType1 = ac2.getConstructors().get(0).getParameterTypes()[0];
        SettableBeanProperty[] constructorArguments = new SettableBeanProperty[2];
        constructorArguments[0] = new SettableBeanProperty(ac2.getMember("param1"), null, null);
        constructorArguments[1] = new SettableBeanProperty(ac2.getMember("param2"), null, null);
        StdValueInstantiator stdValueInstantiator2 = new StdValueInstantiator(null, valueType1);
        stdValueInstantiator2.configureFromObjectSettings(null, null, null, null, null, constructorArguments);
        TestClassWithParamsConstructor testClassWithParamsConstructor = stdValueInstantiator2.createFromObjectWith(
                ObjectMapper.getDefaultMapper().getDeserializationContext().forDeserialization(node),
                new Object[]{ "value1", 1 });
        assertEquals("value1", testClassWithParamsConstructor.getParam1());
        assertEquals(1, testClassWithParamsConstructor.getParam2());

        // Test configureFromArraySettings
        JavaType arrayType = ObjectMapper.getDefaultMapper().getTypeFactory().constructArrayType(TestClassWithParamsConstructor.class);
        StdValueInstantiator stdValueInstantiator3 = new StdValueInstantiator(null, valueType1);
        stdValueInstantiator3.configureFromArraySettings(null, arrayType, constructorArguments);
        TestClassWithParamsConstructor[] testClassWithParamsConstructors = stdValueInstantiator3.createUsingArrayDelegate(
                ObjectMapper.getDefaultMapper().getDeserializationContext(), new Object[]{"value1", 1});
        assertEquals("value1", testClassWithParamsConstructors[0].getParam1());
        assertEquals(1, testClassWithParamsConstructors[0].getParam2());

        // Test configureFromStringCreator
        TestClassWithStringConstructor testClassWithStringConstructor = new TestClassWithStringConstructor();
        AnnotatedMethod stringConstructor = testClassWithStringConstructor.getClass().getMethod("fromString");
        AnnotatedWithParams annotatedWithParams = AnnotatedWithParams.construct(stringConstructor);
        StdValueInstantiator stdValueInstantiator4 = new StdValueInstantiator(null, null);
        stdValueInstantiator4.configureFromStringCreator(annotatedWithParams);
        Object obj = stdValueInstantiator4.createFromString(ObjectMapper.getDefaultMapper().getDeserializationContext(), "test");
        assertEquals(testClassWithStringConstructor.getClass(), obj.getClass());

        // Test configureFromIntCreator
        testClassWithIntConstructor = new TestClassWithIntConstructor();
        AnnotatedMethod intConstructor = testClassWithIntConstructor.getClass().getMethod("fromInt");
        AnnotatedWithParams annotatedWithParams1 = AnnotatedWithParams.construct(intConstructor);
        StdValueInstantiator stdValueInstantiator5 = new StdValueInstantiator(null, null);
        stdValueInstantiator5.configureFromIntCreator(annotatedWithParams1);
        Object intObj = stdValueInstantiator5.createFromInt(ObjectMapper.getDefaultMapper().getDeserializationContext(), 1);
        assertEquals(testClassWithIntConstructor.getClass(), intObj.getClass());

        // Test configureFromLongCreator
        testClassWithLongConstructor = new TestClassWithLongConstructor();
        AnnotatedMethod longConstructor = testClassWithLongConstructor.getClass().getMethod("fromLong");
        AnnotatedWithParams annotatedWithParams2 = AnnotatedWithParams.construct(longConstructor);
        StdValueInstantiator stdValueInstantiator6 = new StdValueInstantiator(null, null);
        stdValueInstantiator6.configureFromLongCreator(annotatedWithParams2);
        Object longObj = stdValueInstantiator6.createFromLong(ObjectMapper.getDefaultMapper().getDeserializationContext(), 1L);
        assertEquals(testClassWithLongConstructor.getClass(), longObj.getClass());

        // Test configureFromDoubleCreator
        testClassWithDoubleConstructor = new TestClassWithDoubleConstructor();
        AnnotatedMethod doubleConstructor = testClassWithDoubleConstructor.getClass().getMethod("fromDouble");
        AnnotatedWithParams annotatedWithParams3 = AnnotatedWithParams.construct(doubleConstructor);
        StdValueInstantiator stdValueInstantiator7 = new StdValueInstantiator(null, null);
        stdValueInstantiator7.configureFromDoubleCreator(annotatedWithParams3);
        Object doubleObj = stdValueInstantiator7.createFromDouble(ObjectMapper.getDefaultMapper().getDeserializationContext(), 1.0d);
        assertEquals(testClassWithDoubleConstructor.getClass(), doubleObj.getClass());

        // Test configureFromBooleanCreator
        testClassWithBooleanConstructor = new TestClassWithBooleanConstructor();
        AnnotatedMethod booleanConstructor = testClassWithBooleanConstructor.getClass().getMethod("fromBoolean");
        AnnotatedWithParams annotatedWithParams4 = AnnotatedWithParams.construct(booleanConstructor);
        StdValueInstantiator stdValueInstantiator8 = new StdValueInstantiator(null, null);
        stdValueInstantiator8.configureFromBooleanCreator(annotatedWithParams4);
        Object booleanObj = stdValueInstantiator8.createFromBoolean(ObjectMapper.getDefaultMapper().getDeserializationContext(), true);
        assertEquals(testClassWithBooleanConstructor.getClass(), booleanObj.getClass());

        // Test configureIncompleteParameter
        TestClassWithIncompleteConstructor testClassWithIncompleteConstructor = new TestClassWithIncompleteConstructor();
        AnnotatedMember incompleteMember = testClassWithIncompleteConstructor.getClass().getMember("incompleteField");
        AnnotatedParameter incompleteParameter = incompleteMember.getAnnotations().findValue(JsonCreator.class).getParameters()[0];
        StdValueInstantiator stdValueInstantiator9 = new StdValueInstantiator(null, null);
        stdValueInstantiator9.configureIncompleteParameter(incompleteParameter);
        Object incompleteObj = stdValueInstantiator9.createUsingDelegate(
                ObjectMapper.getDefaultMapper().getDeserializationContext(), testClassWithIncompleteConstructor);
        assertEquals(testClassWithIncompleteConstructor.getClass(), incompleteObj.getClass());
    }
}

class TestClassWithDefaultConstructor {
    String param1;
    int param2;

    public TestClassWithDefaultConstructor() {
    }

    public String getParam1() {
        return param1;
    }

    public int getParam2() {
        return param2;
    }
}

class TestClassWithParamsConstructor {
    String param1;
    int param2;

    @JsonCreator
    public TestClassWithParamsConstructor(@JsonProperty("param1") String param1, @JsonProperty("param2") int param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    public String getParam1() {
        return param1;
    }

    public int getParam2() {
        return param2;
    }
}

class TestClassWithStringConstructor {
    String param;

    @JsonCreator
    public TestClassWithStringConstructor(@JsonProperty("param") String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public static TestClassWithStringConstructor fromString(String param) {
        return new TestClassWithStringConstructor(param);
    }
}

class TestClassWithIntConstructor {
    int param;

    @JsonCreator
    public TestClassWithIntConstructor(@JsonProperty("param") int param) {
        this.param = param;
    }

    public int getParam() {
        return param;
    }

    public static TestClassWithIntConstructor fromInt(int param) {
        return new TestClassWithIntConstructor(param);
    }
}

class TestClassWithLongConstructor {
    long param;

    @JsonCreator
    public TestClassWithLongConstructor(@JsonProperty("param") long param) {
        this.param = param;
    }

    public long getParam() {
        return param;
    }

    public static TestClassWithLongConstructor fromLong(long param) {
        return new TestClassWithLongConstructor(param);
    }
}

class TestClassWithDoubleConstructor {
    double param;

    @JsonCreator
    public TestClassWithDoubleConstructor(@JsonProperty("param") double param) {
        this.param = param;
    }

    public double getParam() {
        return param;
    }

    public static TestClassWithDoubleConstructor fromDouble(double param) {
        return new TestClassWithDoubleConstructor(param);
    }
}

class TestClassWithBooleanConstructor {
    boolean param;

    @JsonCreator
    public TestClassWithBooleanConstructor(@JsonProperty("param") boolean param) {
        this.param = param;
    }

    public boolean getParam() {
        return param;
    }

    public static TestClassWithBooleanConstructor fromBoolean(boolean param) {
        return new TestClassWithBooleanConstructor(param);
    }
}

class TestClassWithIncompleteConstructor {
    @JsonCreator
    public TestClassWithIncompleteConstructor(@JsonProperty int param) {
        this.param = param;
    }

    int param;
}