package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderImpl;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ValueInMapDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.PropertyName;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier.BeanSerializerModifierDef;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.SerializerProviderDecorator;
import com.fasterxml.jackson.databind.ser.EmptyBeanSerializerBase;
import com.fasterxml.jackson.databind.ser.EmptyObjectSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil.TypeIdentifier;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.datatype.jdk8.JavaType;
import com.fasterxml.jackson.test.BaseTest;

public class XmlSerializerProviderTest extends BaseTest {

    @Mock
    private SerializerProviderImpl baseProvider;

    @Mock
    private XmlRootNameLookup rootNameLookup;

    @InjectMocks
    private XmlSerializerProvider xmlSerializerProvider;

    @Mock
    private ToXmlGenerator xmlGenerator;

    @Mock
    private JsonSerializer<Object> jsonSerializer;

    @Mock
    private SerializerFactory serializerFactory;

    private BeanSerializerModifierDef beanSerializerModifierDef;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(baseProvider.findValueSerializer(any(Class.class), anyBoolean(), anyBoolean(), any(BeanSerializerModifier.class)))
                .thenCallRealMethod();
        when(baseProvider.findBeanSerializer(any(AnnotatedClass.class), anyBoolean(), anyBoolean()))
                .thenCallRealMethod();
        when(baseProvider.findBeanSerializer(any(AnnotatedMethod.class), anyBoolean(), anyBoolean()))
                .thenCallRealMethod();
        when(baseProvider.findBeanSerializer(any(PropertyName.class), anyBoolean(), anyBoolean()))
                .thenCallRealMethod();
        when(baseProvider.getSerializerProviders()).thenReturn(Arrays.asList(baseProvider, xmlSerializerProvider));
        when(baseProvider.findSerializer(any(Class.class), any(BeanSerializerFactory.class)))
                .thenCallRealMethod();
        when(baseProvider.findValueSerializer(eq(String.class), anyBoolean()))
                .thenReturn(Serializers.stringSerializer(this.objectMapper));
        when(baseProvider.findValueSerializer(eq(int[].class), anyBoolean()))
                .thenReturn(Serializers.intArraySerializer(this.objectMapper));
        when(baseProvider.findValueSerializer(eq(Boolean[].class), anyBoolean()))
                .thenReturn(Serializers.booleanArraySerializer(this.objectMapper));

        beanSerializerModifierDef = new BeanSerializerModifierDef() {
            @Override
            public BeanSerializerModifier createInstance(
                    SerializerProvider serializers,
                    BeanSerializerFactory beanSerializerFactory,
                    BeanSerializerModifier.BeanSerializerModifierProvider modProvider) {
                return new MyBeanSerializerModifier(xmlSerializerProvider);
            }
        };

        when(baseProvider.getBeanSerializerModifier()).thenReturn(beanSerializerModifierDef);

        when(rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class)))
                .thenReturn(new QName("rootName"));
        when(rootNameLookup.findRootName(any(JavaType.class), any(SerializationConfig.class)))
                .thenReturn(new QName("rootName"));

        when(serializerFactory.createSerializerInstance(any(Class.class))).thenReturn(jsonSerializer);
    }

    @Test
    public void testSerializeValue() throws Exception {
        Object value = new Object();
        when(baseProvider.findValueSerializer(eq(value.getClass()), anyBoolean(), anyBoolean(), any(BeanSerializerModifier.class)))
                .thenReturn(jsonSerializer);

        TokenBuffer tokenBuffer = new TokenBuffer();
        xmlGenerator = mock(ToXmlGenerator.class);
        when(xmlGenerator.getTokenBuffer()).thenReturn(tokenBuffer);

        _serializeValueTest(value);

        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeFieldName("item");
        verify(jsonSerializer).serialize(value, xmlGenerator, xmlSerializerProvider);
        verify(xmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() throws Exception {
        Object value = new Object();
        JavaType rootType = objectMapper.getTypeFactory().constructType(Object[].class);

        when(baseProvider.findValueSerializer(eq(value.getClass()), anyBoolean(), anyBoolean(), any(BeanSerializerModifier.class)))
                .thenReturn(null);
        when(baseProvider.findValueSerializer(eq(rootType), anyBoolean(), anyBoolean()))
                .thenReturn(jsonSerializer);

        _serializeValueTest(value, rootType);

        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeFieldName("item");
        verify(jsonSerializer).serialize(value, xmlGenerator, xmlSerializerProvider);
        verify(xmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValueRootNull() throws Exception {
        Object value = null;

        _serializeValueTestWithRootName(value);

        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeFieldName("null");
        verify(xmlSerializerProvider)._serializeXmlNull(xmlGenerator);
        verify(xmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValueRootConfigured() throws Exception {
        Object value = new Object();
        QName rootName = new QName("rootName");

        when(baseProvider.findValueSerializer(eq(value.getClass()), anyBoolean(), anyBoolean(), any(BeanSerializerModifier.class)))
                .thenReturn(jsonSerializer);

        _serializeValueTestWithRootName(value, rootName);

        verify(xmlGenerator).writeStartObject(rootName);
        verify(xmlGenerator).writeFieldName("item");
        verify(jsonSerializer).serialize(value, xmlGenerator, xmlSerializerProvider);
        verify(xmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValueRootNullConfigured() throws Exception {
        Object value = null;
        QName rootName = new QName("rootName");

        _serializeValueTestWithRootName(value, rootName);

        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeFieldName("null");
        verify(xmlSerializerProvider)._serializeXmlNull(xmlGenerator);
        verify(xmlGenerator).writeEndObject();
    }

    @Test
    public void testSerializeValueArray() throws Exception {
        Object[] value = new Object[0];
        JavaType rootType = objectMapper.getTypeFactory().constructType(Object[].class);

        when(baseProvider.findValueSerializer(eq(value.getClass()), anyBoolean(), anyBoolean(), any(BeanSerializerModifier.class)))
                .thenReturn(jsonSerializer);

        _serializeValueTest(value, rootType);

        verify(xmlGenerator).writeStartObject();
        verify(xmlGenerator).writeFieldName("item");
        verify(xmlGenerator).writeStartArray();
        verifyNoMoreInteractions(xmlGenerator);
    }

    private void _serializeValueTest(Object value) throws Exception {
        _serializeValueTest(value, null);
    }

    private void _serializeValueTest(Object value, JavaType rootType) throws Exception {
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(xmlGenerator.getJsonGenerator()).thenReturn(gen);

        _serializeValue(value, rootType);
    }

    private void _serializeValueTestWithRootName(Object value, QName rootName) throws Exception {
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        JsonGenerator gen = mock(JsonGenerator.class);
        when(xmlGenerator.getJsonGenerator()).thenReturn(gen);

        _serializeValue(value, rootName);
    }

    private void _serializeValue(Object value, QName rootName) throws Exception {
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
        xmlSerializerProvider._initWithRootName(xmlGenerator, rootName);

        xmlSerializerProvider.serializeValue(xmlGenerator, value);
    }

    private static class MyBeanSerializerModifier extends BeanSerializerModifier {

        private final XmlSerializerProvider provider;

        public MyBeanSerializerModifier(XmlSerializerProvider provider) {
            this.provider = provider;
        }

        @Override
        public BeanSerializerFactory createBeanSerializerFactory() {
            return new MyBeanSerializerFactory(provider);
        }

        private class MyBeanSerializerFactory extends BeanSerializerFactory {

            private final XmlSerializerProvider provider;

            public MyBeanSerializerFactory(XmlSerializerProvider provider) {
                this.provider = provider;
            }

            @Override
            public BeanSerializerModifier getModifier() {
                return this;
            }

            @Override
            public BeanSerializer findBeanSerializer(AnnotatedClass aClass, boolean isEmpty) {
                return new EmptyBeanSerializerBase();
            }

            @Override
            public BeanSerializer findBeanSerializer(AnnotatedMethod method, boolean isEmpty) {
                return new EmptyObjectSerializer();
            }

            @Override
            public BeanSerializer findBeanSerializer(PropertyName propertyName, boolean isEmpty) {
                return new ValueInMapDeserializer() {
                    @Override
                    public Object deserializeWithType(StreamContext context, DeserializationContext ctxt, TypeDeserializer vd,
                            JavaType type) throws IOException {
                        String value = context.getText();
                        String key = context.getName();

                        if (value.equals("null")) {
                            return null;
                        }

                        if (type.isContainerType()) {
                            // Container types are usually Arrays or Lists
                            // and we need to create them using StdKeyDeserializer
                            JavaType containerType = type.getContainedType(0);
                            TypeIdentifier keyId = TypeUtil.typeIdentifierFromValue(value, ContainerUtil.class, ctxt);
                            return new StdKeyDeserializer(vd, keyId, ctxt, null, null, null, null, null);
                        }

                        // Assume that it's a POJO
                        ContextualDeserializer cd = ctxt.findContextualValueDeserializer(type, null);
                        if (cd != null) {
                            return cd.deserializeWithType(context, ctxt, this, type);
                        }

                        // If not, we need to find a deserializer for the type
                        return vd.deserializeWithType(context, ctxt, this, type);
                    }
                };
            }
        }
    }
}