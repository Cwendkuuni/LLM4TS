package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserializerConstructor() {
        // Add constructor test cases here
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeser = mock(JsonDeserializer.class);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(_fullType, _valueInstantiator, typeDeser, valueDeser);
        AtomicReferenceDeserializer newAtomicReferenceDeserializer = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        Assert.assertEquals(atomicReferenceDeserializer, newAtomicReferenceDeserializer);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext context = mock(DeserializationContext.class);
        Assert.assertNotNull(new AtomicReferenceDeserializer(_fullType, _valueInstantiator, null, null).getNullValue(context));
        DeserializationContext contextWithNullConfig = mock(DeserializationContext.class);
        Mockito.when(contextWithNullConfig.getContextualValue(ArgumentMatchers.any(DeserializationConfig.class)))
                .thenReturn(null);
        Assert.assertThrows(JsonMappingException.class, () -> new AtomicReferenceDeserializer(_fullType, _valueInstantiator, null, null).getNullValue(contextWithNullConfig));
    }

    @Test
    public void testGetEmptyValue() {
        Assert.assertNotNull(new AtomicReferenceDeserializer(_fullType, _valueInstantiator, null, null).getEmptyValue(null));
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);
        AtomicReference<Object> result = new AtomicReferenceDeserializer(_fullType, _valueInstantiator, null, null).referenceValue(contents);
        Assert.assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object result = new AtomicReferenceDeserializer(_fullType, _valueInstantiator, null, null).getReferenced(reference);
        Assert.assertEquals(result, reference.get());
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newValue = new Object();
        AtomicReference<Object> updatedReference = new AtomicReferenceDeserializer(_fullType, _valueInstantiator, null, null).updateReference(reference, newValue);
        Assert.assertEquals(newValue, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        Assert.assertTrue(new AtomicReferenceDeserializer(_fullType, _valueInstantiator, null, null).supportsUpdate(config));
    }

}