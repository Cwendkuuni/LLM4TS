package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValueInstantiatorTest {

    private static final ObjectMapper TEST_MAPPER = new ObjectMapper();

    @Mock
    private ValueInstantiator.ValueInstantiatorContext context;

    private ValueInstantiator valueInstantiator;

    @Test
    public void testGetValueClass() {
        valueInstantiator = new ValueInstantiator.Base(String.class);
        assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() {
        valueInstantiator = new ValueInstantiator.Base(String.class);
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() {
        valueInstantiator = new ValueInstantiator.Base(String.class);
        assertFalse(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCanCreateFromString() {
        valueInstantiator = new StringValueInstantiator();
        assertTrue(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() {
        valueInstantiator = new IntValueInstantiator();
        assertTrue(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() {
        valueInstantiator = new LongValueInstantiator();
        assertTrue(valueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        valueInstantiator = new DoubleValueInstantiator();
        assertTrue(valueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        valueInstantiator = new BooleanValueInstantiator();
        assertTrue(valueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testGetDefaultCreator() {
        valueInstantiator = new DefaultConstructorValueInstantiator();
        AnnotatedMember defaultCreator = valueInstantiator.getDefaultCreator();
        assertNotNull(defaultCreator);
    }

    @Test
    public void testGetDelegateCreator() {
        valueInstantiator = new DelegatorValueInstantiator();
        AnnotatedMember delegateCreator = valueInstantiator.getDelegateCreator();
        assertNotNull(delegateCreator);
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        valueInstantiator = new WithArgsValueInstantiator();

        Object obj = TEST_MAPPER.readValue("{\"a\":1,\"b\":2}", valueInstantiator.getValueClass());
        assertEquals(1, obj.getClass().getField("a").get(obj));
        assertEquals(2, obj.getClass().getField("b").get(obj));
    }

    @Test
    public void testCreateFromObjectWithEmptyArgs() throws IOException {
        valueInstantiator = new WithEmptyArgsValueInstantiator();
        Object obj = TEST_MAPPER.readValue("{}", valueInstantiator.getValueClass());
        assertNull(obj);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        valueInstantiator = new DefaultConstructorValueInstantiator();
        Object obj = valueInstantiator.createUsingDefault(context);
        assertNotNull(obj);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        valueInstantiator = new DelegatorValueInstantiator();
        Object delegate = mock(String.class);
        Object obj = valueInstantiator.createUsingDelegate(context, delegate);
        assertNotNull(obj);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws IOException {
        valueInstantiator = new ArrayDelegateValueInstantiator();
        Object delegate = new Object[]{1, 2};
        Object obj = valueInstantiator.createUsingArrayDelegate(context, delegate);
        assertNotNull(obj);
    }

    @Test
    public void testCreateFromString() throws IOException, JsonProcessingException {
        valueInstantiator = new StringValueInstantiator();
        String str = "\"Hello world\"";
        Object obj = valueInstantiator.createFromString(context, str);
        assertEquals("Hello world", obj);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        valueInstantiator = new IntValueInstantiator();
        int intValue = 42;
        Object obj = valueInstantiator.createFromInt(context, intValue);
        assertEquals(intValue, obj.intValue());
    }

    @Test
    public void testCreateFromLong() throws IOException {
        valueInstantiator = new LongValueInstantiator();
        long longValue = 9L;
        Object obj = valueInstantiator.createFromLong(context, longValue);
        assertEquals(longValue, obj.longValue());
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        valueInstantiator = new DoubleValueInstantiator();
        double doubleValue = 3.14;
        Object obj = valueInstantiator.createFromDouble(context, doubleValue);
        assertEquals(doubleValue, obj.doubleValue(), 0.0);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        valueInstantiator = new BooleanValueInstantiator();
        boolean boolValue = true;
        Object obj = valueInstantiator.createFromBoolean(context, boolValue);
        assertEquals(boolValue, obj.booleanValue());
    }

    @Test
    public void testCanCreateUsingArrayDelegate() {
        valueInstantiator = new ArrayDelegateValueInstantiator();
        assertTrue(valueInstantiator.canCreateUsingArrayDelegate());
    }

    private static class DefaultConstructorValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateUsingDefault() {
            return true;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }

    private static class DelegatorValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateUsingDelegate() {
            return true;
        }

        @Override
        public AnnotatedMember getDelegateCreator() {
            AnnotatedClass ac = mock(AnnotatedClass.class);
            AnnotatedMember member = mock(AnnotatedMember.class);
            when(ac.findMember("delegateMethod")).thenReturn(member);
            return member;
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return delegate;
        }
    }

    private static class WithArgsValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            BeanPropertyDefinition p1 = mock(BeanPropertyDefinition.class);
            BeanPropertyDefinition p2 = mock(BeanPropertyDefinition.class);
            SettableBeanProperty[] args = {p1, p2};
            return args;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            Object obj = new Object();
            BeanPropertyDefinition p1 = (BeanPropertyDefinition) args[0];
            Field f1 = p1.getMember();
            f1.set(obj, 1);
            BeanPropertyDefinition p2 = (BeanPropertyDefinition) args[1];
            Field f2 = p2.getMember();
            f2.set(obj, 2);
            return obj;
        }
    }

    private static class WithEmptyArgsValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public Set<BeanPropertyDefinition> getFromObjectArguments(DeserializationConfig config) {
            return Arrays.asList(BeanPropertyDefinition.emptyCreatorList()).iterator().next();
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return null;
        }
    }

    private static class StringValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromString() {
            return true;
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return new String(value);
        }
    }

    private static class IntValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return new Integer(value);
        }
    }

    private static class LongValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromLong() {
            return true;
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return new Long(value);
        }
    }

    private static class DoubleValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromDouble() {
            return true;
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return new Double(value);
        }
    }

    private static class BooleanValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return new Boolean(value);
        }
    }

    private static class ArrayDelegateValueInstantiator extends ValueInstantiator {
        @Override
        public boolean canCreateUsingArrayDelegate() {
            return true;
        }

        @Override
        public JavaType getArrayDelegateType(DeserializationConfig config) {
            return JavaType.constructUnboundType(int[].class);
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            int[] ints = (int[]) delegate;
            Object obj = new Object();
            Field field = ClassUtil.findCreatorProperty(obj.getClass(), "ints");
            field.set(obj, ints);
            return obj;
        }
    }
}

class NopValueInstantiatorContext extends ValueInstantiator.ValueInstantiatorContext {
    @Override
    public Object findValueHandler(DeserializationConfig config, BeanPropertyDefinition prop) {
        return null;
    }

    @Override
    public Object findValueHandler(DeserializationConfig config, PropertyName name) {
        return null;
    }

    @Override
    public Object findValueHandler(DeserializationConfig config, AnnotatedMember member) {
        return null;
    }

    @Override
    public Object findValueHandler(DeserializationConfig config, AnnotatedMethod method) {
        return null;
    }

    @Override
    public Object findValueHandler(DeserializationConfig config, AnnotatedField field) {
        return null;
    }

    @Override
    public Object findValueHandler(DeserializationConfig config, ValueDeserializer deserializer) {
        return null;
    }

    @Override
    public Object findValueHandler(DeserializationConfig config, Converter<?> converter) {
        return null;
    }

    @Override
    public Object findValueHandler(DeserializationConfig config, Object valueHandler) {
        return null;
    }
}