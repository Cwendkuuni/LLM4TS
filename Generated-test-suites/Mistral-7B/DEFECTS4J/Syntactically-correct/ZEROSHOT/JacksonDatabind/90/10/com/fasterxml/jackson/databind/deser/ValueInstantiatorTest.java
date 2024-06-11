package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParserArgs;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ValueInstantiatorTest {

    @InjectMocks
    private TestValueInstantiator instantiator;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultCreator() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.handleMissingInstantiator(any(Class.class), any(Parser.class), anyString())).thenCallRealMethod();

        ObjectNode node = ObjectMapperTestUtils.createTestNode("{\"class\":\"com.test.DefaultConstructorClass\"}", new ParserArgs());
        Object result = instantiator.createUsingDefault(context, node);

        verify(context, times(1)).handleMissingInstantiator(eq(DefaultConstructorClass.class), any(Parser.class), anyString());
        assertEquals(DefaultConstructorClass.class, result.getClass());
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectNode node = ObjectMapperTestUtils.createTestNode("{\"value\":1,\"anotherValue\":\"test\"}", new ParserArgs());
        SettableBeanProperty[] properties = {
                new SettableBeanProperty("value", ValueTypeWithObjectConstructor.class, ValueTypeWithObjectConstructorArg1.class),
                new SettableBeanProperty("anotherValue", ValueTypeWithObjectConstructor.class, ValueTypeWithObjectConstructorArg2.class)
        };

        when(context.findBeanProperty(eq("value"), any(JavaType.class))).thenReturn(properties[0]);
        when(context.findBeanProperty(eq("anotherValue"), any(JavaType.class))).thenReturn(properties[1]);

        Object[] args = new Object[]{1L, "test"};
        Object result = instantiator.createFromObjectWith(context, node, properties);

        verify(context, times(1)).handleMissingInstantiator(eq(ValueTypeWithObjectConstructor.class), any(Parser.class), anyString());
        assertEquals(ValueTypeWithObjectConstructor.class, result.getClass());
        assertEquals(args[0], result.getClass().getField("value").get(result));
        assertEquals(args[1], result.getClass().getField("anotherValue").get(result));
    }

    @Test
    public void testCreateFromInt() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.handleMissingInstantiator(any(Class.class), any(Parser.class), anyString())).thenCallRealMethod();

        ObjectNode node = ObjectMapperTestUtils.createTestNode("42", new ParserArgs());

        Object result = instantiator.createFromInt(context, node.getLongValue());

        verify(context, times(1)).handleMissingInstantiator(eq(ValueTypeWithIntConstructor.class), any(Parser.class), anyString());
        assertEquals(ValueTypeWithIntConstructor.class, result.getClass());
        assertEquals(new Integer(42), result);
    }

    @Test
    public void testCanInstantiate() {
        assertTrue(instantiator.canInstantiate());
    }

    @Test
    public void testGetDelegateType() {
        JavaType delegateType = objectMapper.getTypeFactory().constructType(ValueTypeWithDelegate.class);

        assertEquals(delegateType, instantiator.getDelegateType(null));
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectNode node = ObjectMapperTestUtils.createTestNode("\"test\"", new ParserArgs());
        Object delegate = new Object();

        when(context.handleMissingInstantiator(any(Class.class), any(Parser.class), anyString())).thenCallRealMethod();

        Object result = instantiator.createUsingDelegate(context, delegate);

        verify(context, times(1)).handleMissingInstantiator(eq(ValueTypeWithDelegate.class), any(Parser.class), anyString());
        assertEquals(ValueTypeWithDelegate.class, result.getClass());
    }

    @Test
    public void testCreateFromString() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectNode node = ObjectMapperTestUtils.createTestNode("\"true\"", new ParserArgs());

        when(context.handleMissingInstantiator(any(Class.class), any(Parser.class), anyString())).thenCallRealMethod();

        Object result = instantiator.createFromString(context, node.getTextValue());

        verify(context, times(1)).handleMissingInstantiator(eq(ValueTypeWithBooleanFromString.class), any(Parser.class), anyString());
        assertEquals(ValueTypeWithBooleanFromString.class, result.getClass());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCanCreateFromIntAndCreateFromLong() {
        assertFalse(instantiator.canCreateFromInt());
        assertFalse(instantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() {
        assertFalse(instantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() {
        assertFalse(instantiator.canCreateFromBoolean());
    }

    @Test
    public void testCanCreateFromObjectWithEmptyArguments() {
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectNode node = ObjectMapperTestUtils.createTestNode("{}", new ParserArgs());

        SettableBeanProperty[] properties = instantiator.getFromObjectArguments(context);

        assertEquals(0, properties.length);
    }

    @Test
    public void testGetFromObjectArguments() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType valueType = objectMapper.getTypeFactory().constructType(ValueTypeWithObjectArguments.class);
        SettableBeanProperty[] properties = instantiator.getFromObjectArguments(context);

        assertEquals(2, properties.length);
        assertEquals("firstValue", properties[0].getName());
        assertEquals(ValueTypeWithObjectArgumentsArg1.class, properties[0].getType());
        assertEquals("secondValue", properties[1].getName());
        assertEquals(ValueTypeWithObjectArgumentsArg2.class, properties[1].getType());
        assertEquals(valueType, context.getCurrentValueType(node));
    }

    private static class DefaultConstructorClass {

    }

    private static class ValueTypeWithObjectConstructor {

        private Long value;
        private String anotherValue;

        public ValueTypeWithObjectConstructor(Long value, String anotherValue) {
            this.value = value;
            this.anotherValue = anotherValue;
        }

    }

    private static class ValueTypeWithObjectConstructorArg1 {

    }

    private static class ValueTypeWithObjectConstructorArg2 {

    }

    private static class ValueTypeWithIntConstructor {

        private Integer value;

        public ValueTypeWithIntConstructor(int value) {
            this.value = value;
        }

    }

    private static class ValueTypeWithDelegate {

        private String value;

        public ValueTypeWithDelegate(String value) {
            this.value = value;
        }

    }

    private static class ValueTypeWithBooleanFromString {

        private Boolean value;

        public ValueTypeWithBooleanFromString() {
            this.value = Boolean.FALSE;
        }

    }

    private static class ValueTypeWithObjectArguments {

        private String firstValue;
        private int secondValue;

        public ValueTypeWithObjectArguments(String firstValue, int secondValue) {
            this.firstValue = firstValue;
            this.secondValue = secondValue;
        }

    }

    private static class ValueTypeWithObjectArgumentsArg1 {

    }

    private static class ValueTypeWithObjectArgumentsArg2 {

    }

    private static class TestValueInstantiator extends ValueInstantiator.Base {

        @Override
        public Object createUsingDefault(DeserializationContext ctxt, Object node) throws IOException {
            return super.createUsingDefault(ctxt, node);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return super.createFromObjectWith(ctxt, args);
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return super.createUsingDelegate(ctxt, delegate);
        }

        @Override
        public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
            return super.getFromObjectArguments(config);
        }

        @Override
        public JavaType getDelegateType(DeserializationConfig config) {
            return null;
        }

        @Override
        public JavaType getArrayDelegateType(DeserializationConfig config) {
            return null;
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return super.createFromInt(ctxt, value);
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return super.createFromLong(ctxt, value);
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return super.createFromDouble(ctxt, value);
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return super.createFromBoolean(ctxt, value);
        }

        @Override
        public boolean canCreateFromString() {
            return true;
        }

        @Override
        public boolean canCreateFromInt() {
            return true;
        }

        @Override
        public boolean canCreateFromLong() {
            return true;
        }

        @Override
        public boolean canCreateFromDouble() {
            return true;
        }

        @Override
        public boolean canCreateFromBoolean() {
            return true;
        }

        @Override
        public boolean canCreateUsingDefault() {
            return true;
        }

        @Override
        public boolean canCreateUsingDelegate() {
            return true;
        }

        @Override
        public boolean canCreateFromObjectWith() {
            return true;
        }

        @Override
        public AnnotatedWithParams getDefaultCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getDelegateCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getArrayDelegateCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getWithArgsCreator() {
            return null;
        }

        @Override
        public AnnotatedParameter getIncompleteParameter() {
            return null;
        }

        @Override
        public Object createUsingDefault(DeserializationContext context) throws IOException {
            return null;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext context, Object[] args) throws IOException {
            return null;
        }

        @Override
        public Object createFromObjectWith(DeserializationContext context, SettableBeanProperty[] properties, PropertyValueBuffer buffer) throws IOException {
            return null;
        }

        @Override
        public Object createUsingDelegate(DeserializationContext context, Object delegate) throws IOException {
            return null;
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext context, Object delegate) throws IOException {
            return null;
        }

        @Override
        public Object _createFromStringFallbacks(DeserializationContext context, String value) throws IOException {
            return null;
        }

        @Override
        protected Class<?> _valueType;

        public TestValueInstantiator() {
            super(objectMapper.getTypeFactory().constructType(getClass()));
        }

    }
}