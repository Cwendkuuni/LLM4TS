package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class StdValueInstantiatorTest {

    private ObjectMapper mapper;
    private ValueInstantiator instantiator;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), String.class);
    }

    @Test
    public void testCanCreateUsingDefault() {
        Assert.assertTrue(instantiator.canCreateUsingDefault());
    }

    @Test
    public void testCreateUsingDefault() throws JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object result = instantiator.createUsingDefault(context);
        Assert.assertNotNull(result);
        verify(context, never()).findInjectableValue(any(), any(), any());
        verify(context, never()).handleInstantiationProblem(any(), any(), any());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateUsingDefaultThrowsException() throws JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.handleInstantiationProblem(eq(String.class), any(), any()))
                .thenThrow(new JsonMappingException("Expected exception"));
        Object result = instantiator.createUsingDefault(context);
        verify(context, times(1)).handleInstantiationProblem(eq(String.class), any(), any());
    }

    @Test
    public void testCanCreateUsingDelegate() {
        instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), String[].class);
        Assert.assertTrue(instantiator.canCreateUsingDelegate());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateUsingDelegateThrowsException() throws JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object delegate = mock(String.class);
        when(context.handleInstantiationProblem(eq(String[].class), any(), any()))
                .thenThrow(new JsonMappingException("Expected exception"));
        instantiator.createUsingDelegate(context, delegate);
        verify(context, times(1)).handleInstantiationProblem(eq(String[].class), any(), any());
    }

    @Test
    public void testCanCreateFromString() {
        Assert.assertTrue(instantiator.canCreateFromString());
    }

    @Test
    public void testCreateFromString() throws JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        String value = "test";
        Object result = instantiator.createFromString(context, value);
        Assert.assertNotNull(result);
        verify(context, never()).handleInstantiationProblem(any(), any(), any());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromStringThrowsException() throws JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.handleInstantiationProblem(eq(String.class), any(), any()))
                .thenThrow(new JsonMappingException("Expected exception"));
        instantiator.createFromString(context, "test");
        verify(context, times(1)).handleInstantiationProblem(eq(String.class), any(), any());
    }

    @Test
    public void testCanCreateFromInt() {
        Assert.assertTrue(instantiator.canCreateFromInt());
    }

    @Test
    public void testCreateFromIntSuccess() throws JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        int value = 42;
        Object result = instantiator.createFromInt(context, value);
        Assert.assertNotNull(result);
        verify(context, never()).handleInstantiationProblem(any(), any(), any());
    }

    @Test
    public void testCreateFromIntFailure() throws JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.handleInstantiationProblem(eq(String.class), any(), any()))
                .thenThrow(new JsonMappingException("Expected exception"));
        instantiator = new StdValueInstantiator(mapper.getDeserializationConfig(), Integer.class);
        int value = 42;
        instantiator.createFromInt(context, value);
        verify(context, times(1)).handleInstantiationProblem(eq(Integer.class), any(), any());
    }
}