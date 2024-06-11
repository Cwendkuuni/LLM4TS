package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import static com.fasterxml.jackson.databind.deser.std.StdValueInstantiator.ValueInstantiationSource.*;

/**
 * Test cases for {@link StdValueInstantiator}.
 */
public class StdValueInstantiatorTest {

    @Test
    public void testCanCreateFromDefault() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
        Assert.assertTrue(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
        String result = instantiator.createUsingDefault(mapper.getDeserializationContext());
        Assert.assertEquals("", result);
    }

    @Test
    public void testDelegateCreation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(MyDelegate.class);
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyDelegate.class);
        Assert.assertTrue(instantiator.canCreateUsingDelegate());
        MyDelegate result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), new Object());
        Assert.assertNotNull(result);
        Assert.assertEquals("delegate", result.s);
    }

    @Test
    public void testArrayDelegateCreation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(MyArrayDelegate.class);
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyArrayDelegate.class);
        Assert.assertTrue(instantiator.canCreateUsingArrayDelegate());
        MyArrayDelegate result = instantiator.createUsingArrayDelegate(mapper.getDeserializationContext(), new Object[0]);
        Assert.assertArrayEquals(new int[] { 1, 2, 3 }, result);
    }

    @Test
    public void testWithArgsCreation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(MyWithArgs.class);
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyWithArgs.class);
        Assert.assertTrue(instantiator.canCreateFromObjectWith());
        ObjectNode node = ObjectMapper.factory.createObjectNode();
        node.put("a", 1);
        node.put("b", "two");
        MyWithArgs result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), new Object[] { node });
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.a);
        Assert.assertEquals("two", result.b);
    }

    @Test
    public void testFromStringCreation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
        Assert.assertTrue(instantiator.canCreateFromString());
        String result = instantiator.createFromString(mapper.getDeserializationContext(), "test");
        Assert.assertEquals("test", result);
    }

    @Test
    public void testFromIntCreation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyFromInt.class);
        Assert.assertTrue(instantiator.canCreateFromInt());
        MyFromInt result = instantiator.createFromInt(mapper.getDeserializationContext(), 42);
        Assert.assertEquals(42, result.i);
    }

    @Test
    public void testFromLongCreation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyFromLong.class);
        Assert.assertTrue(instantiator.canCreateFromLong());
        MyFromLong result = instantiator.createFromLong(mapper.getDeserializationContext(), 42L);
        Assert.assertEquals(42L, result.l);
    }

    @Test
    public void testFromDoubleCreation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyFromDouble.class);
        Assert.assertTrue(instantiator.canCreateFromDouble());
        MyFromDouble result = instantiator.createFromDouble(mapper.getDeserializationContext(), 42.0);
        Assert.assertEquals(42.0, result.d, 0.0);
    }

    @Test
    public void testFromBooleanCreation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdValueInstantiator instantiator = mapper.findValueInstantiator(MyFromBoolean.class);
        Assert.assertTrue(instantiator.canCreateFromBoolean());
        MyFromBoolean result = instantiator.createFromBoolean(mapper.getDeserializationContext(), true);
        Assert.assertTrue(result.b);
    }

    @JsonCreator
    static class MyDelegate {
        private String s;

        @JsonProperty
        public MyDelegate(String s) {
            this.s = s;
        }
    }

    @JsonCreator
    static class MyArrayDelegate {
        int[] a;

        @JsonProperty
        public MyArrayDelegate(int... a) {
            this.a = a;
        }
    }

    @JsonCreator
    static class MyWithArgs {
        int a;
        String b;

        @JsonProperty
        public MyWithArgs(@JsonProperty("a") int a, @JsonProperty("b") String b) {
            this.a = a;
            this.b = b;
        }
    }

    @JsonDeserialize(using = StdSerializer.class)
    static class MyFromInt {
        int i;

        public MyFromInt(int i) {
            this.i = i;
        }
    }

    @JsonDeserialize(using = StdSerializer.class)
    static class MyFromLong {
        long l;

        public MyFromLong(long l) {
            this.l = l;
        }
    }

    @JsonDeserialize(using = StdSerializer.class)
    static class MyFromDouble {
        double d;

        public MyFromDouble(double d) {
            this.d = d;
        }
    }

    @JsonDeserialize(using = StdSerializer.class)
    static class MyFromBoolean {
        boolean b;

        public MyFromBoolean(boolean b) {
            this.b = b;
        }
    }
}