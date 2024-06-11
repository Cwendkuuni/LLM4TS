package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;
    private AnnotatedMethod annotatedMethod;
    private Object testInstance;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setUp() throws Exception {
        annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method getter = Mockito.mock(Method.class);
        when(annotatedMethod.getAnnotations()).thenReturn(new Annotation[0]);
        when(annotatedMethod.getAnnotated()).thenReturn(annotatedMethod);
        when(annotatedMethod.getName()).thenReturn("testProperty");
        when(annotatedMethod.getMember()).thenReturn(annotatedMethod);
        when(annotatedMethod.getRawType()).thenReturn(Object.class);
        when(annotatedMethod.getDeclaringClass()).thenReturn(Object.class);
        when(annotatedMethod.getMethod()).thenReturn(getter);

        testInstance = Mockito.mock(Object.class);
        jsonParser = Mockito.mock(JsonParser.class);
        deserializationContext = Mockito.mock(DeserializationContext.class);
        typeDeserializer = Mockito.mock(TypeDeserializer.class);
        nullValueProvider = Mockito.mock(NullValueProvider.class);

        Object type = Mockito.mock(Object.class);
        when(typeDeserializer.findDeserializerForEmptyValue(any())).thenReturn(null);

        setterlessProperty = new SetterlessProperty(
                Mockito.mock(BeanPropertyDefinition.class),
                Mockito.mock(JavaType.class),
                typeDeserializer,
                Annotations.empty(),
                annotatedMethod
        );
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(getter.invoke(testInstance, (Object[]) null)).thenReturn(new Object());

        JsonToken jsonToken = JsonToken.VALUE_STRING;
        String jsonValue = "testValue";
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        when(jsonParser.getValueAsString()).thenReturn(jsonValue);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, testInstance);

        verify(jsonParser).nextToken();
        verify(testInstance).getClass();
        verify(getter).invoke(testInstance, (Object[]) null);
        verify(jsonParser).setValue(any());
        verify(deserializationContext).findBeanProperty(any());
        verify(setterlessProperty.getValueDeserializer()).deserialize(jsonParser, deserializationContext, any());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(testInstance, new Object());
    }
}