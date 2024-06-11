package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {

    private DeserializationContext ctxt;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer typeDeserializer;
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        ctxt = mock(DeserializationContext.class);
        valueDeserializer = mock(JsonDeserializer.class);
        typeDeserializer = mock(TypeDeserializer.class);
        mapper = new ObjectMapper();
    }

    @Test
    public void testNormalDeserialization() throws IOException {
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InnerClassProperty property = new InnerClassProperty(new StdDelegate(), constructor);

        Object bean = new Object();
        String json = "{\"innerClass\":{\"name\":\"TestInnerClass\"}}";
        JsonParser jp = mapper.getFactory().createParser(json);

        when(valueDeserializer.deserialize(jp, eq(ctxt), any())).thenReturn(new TestInnerClass("TestInnerClass"));

        property.deserializeAndSet(jp, ctxt, bean);

        Assertions.assertEquals(new TestInnerClass("TestInnerClass"), property.getBean().getProperty("innerClass"));
        verify(valueDeserializer, times(1)).deserialize(jp, eq(ctxt), eq(bean.getClass()));
        verifyNoMoreInteractions(ctxt);
    }

    @Test
    public void testNullValueDeserialization() throws IOException {
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InnerClassProperty property = new InnerClassProperty(new StdDelegate(), constructor);

        Object bean = new Object();
        String json = "{\"innerClass\":null}";
        JsonParser jp = mapper.getFactory().createParser(json);

        when(valueDeserializer.getNullValue(eq(ctxt))).thenReturn(new TestInnerClass(null));

        property.deserializeAndSet(jp, ctxt, bean);

        Assertions.assertNull(property.getBean().getProperty("innerClass"));
        verify(valueDeserializer, times(1)).getNullValue(eq(ctxt));
        verifyNoMoreInteractions(ctxt);
    }

    @Test
    public void testDeserializationWithType() throws IOException {
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InnerClassProperty property = new InnerClassProperty(new StdDelegate(), constructor);

        Object bean = new Object();
        String json = "{\"innerClass\":{\"name\":\"TestInnerClass\"}}";
        JsonParser jp = mapper.getFactory().createParser(json);

        TestInnerClassTypeDeserializer typeDeserializerMock = mock(TestInnerClassTypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromAny(eq(ctxt), any(), any()))
                .thenReturn(typeDeserializerMock);
        when(typeDeserializerMock.deserializeTypedFromObject(eq(ctxt), any()))
                .thenReturn(new TestInnerClass("TestInnerClassType"));

        when(valueDeserializer.deserializeWithType(jp, eq(ctxt), eq(typeDeserializer)))
                .thenReturn(new TestInnerClass("TestInnerClassType"));

        property.deserializeAndSet(jp, ctxt, bean);

        Assertions.assertEquals(new TestInnerClass("TestInnerClassType"), property.getBean().getProperty("innerClass"));
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(eq(ctxt), eq(jp), any());
        verify(valueDeserializer, times(1)).deserializeWithType(jp, eq(ctxt), eq(typeDeserializer));
        verifyNoMoreInteractions(ctxt);
    }

    @Test
    public void testExceptionDuringDeserialization() throws IOException {
        Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InnerClassProperty property = new InnerClassProperty(new StdDelegate(), constructor);

        Object bean = new Object();
        String json = "{\"innerClass\":null}";
        JsonParser jp = mapper.getFactory().createParser(json);

        when(valueDeserializer.getNullValue(eq(ctxt))).thenThrow(new IOException("Null value error"));

        Exception exception = assertThrows(IOException.class, () -> property.deserializeAndSet(jp, ctxt, bean));

        Assertions.assertNull(property.getBean().getProperty("innerClass"));
        verify(valueDeserializer, times(1)).getNullValue(eq(ctxt));
        verifyNoMoreInteractions(ctxt);
        Assertions.assertEquals("Null value error", exception.getMessage());
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        Object bean = new Object();
        InnerClassProperty property = new InnerClassProperty(new StdDelegate(), null) {
            @Override
            public Object instantiate(Object bean, DeserializationContext ctxt) throws IOException {
                return new TestInnerClass("TestInnerClass");
            }
        };

        String json = mapper.writeValueAsString(bean);
        JsonParser jp = mapper.getFactory().createParser(json);

        Assertions.assertEquals("TestInnerClass", jp.getValueAsString());
        verifyNoMoreInteractions(ctxt);
    }
}

class InnerClass {
    @JsonDeserialize(using = TestInnerClassDeserializer.class)
    private TestInnerClass innerClass;

    public TestInnerClass getInnerClass() {
        return innerClass;
    }

    public void setInnerClass(TestInnerClass innerClass) {
        this.innerClass = innerClass;
    }
}

class TestInnerClass {
    private String name;

    public TestInnerClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class TestInnerClassDeserializer extends JsonDeserializer<TestInnerClass> {
    @Override
    public TestInnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new TestInnerClass("TestInnerClassDeserializer");
    }
}

class TestInnerClassTypeDeserializer extends JsonDeserializer<TestInnerClass> {
    @Override
    public TestInnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new TestInnerClass("TestInnerClassTypeDeserializer");
    }

    @Override
    public final TypeDeserializer getTypeDeserializer() {
        return null;
    }
}

class StdDelegate extends SettableBeanProperty.StdDelegate {
}