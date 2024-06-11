package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StandardSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

/**
 * Test class for XmlSerializerProvider
 */
public class XmlSerializerProviderTest {

    private static final ObjectMapper MAPPER = new XmlMapper();

    @Test
    public void testXmlSerializerProvider() throws IOException {
        // Test default constructor
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        assertNotNull(provider);

        // Test constructor with rootNames
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider providerWithRootNames = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(providerWithRootNames);

        // Test constructor with src, config, and factory
        XmlSerializerProvider providerWithConfig = new XmlSerializerProvider(
                new XmlSerializerProvider(), new SerializationConfig(MAPPER.getDefaultConfig()), null);
        assertNotNull(providerWithConfig);

        // Test serializing a null value
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator gen = MAPPER.getFactory()
                .createXmlGenerator(outputStream, null, null, null, new StandardSerializer(String.class));
        provider.serializeValue(gen, null);
        gen.close();
        String xml = outputStream.toString();
        assertEquals("<null/>", xml);

        // Test serializing a simple String value
        String value = "Hello, World!";
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        JsonGenerator gen2 = MAPPER.getFactory()
                .createXmlGenerator(outputStream2, null, null, null, new StandardSerializer(String.class));
        provider.serializeValue(gen2, value);
        gen2.close();
        String xml2 = outputStream2.toString();
        assertEquals("<string>Hello, World!</string>", xml2);

        // Test serializing a custom JavaBean with a valid root name
        JavaType javaType = MAPPER.getTypeFactory().constructType(TestBean.class);
        ByteArrayOutputStream outputStream3 = new ByteArrayOutputStream();
        JsonGenerator gen3 = MAPPER.getFactory()
                .createXmlGenerator(outputStream3, null, null, null, new StandardSerializer(TestBean.class));
        provider.serializeValue(gen3, new TestBean("Test"));
        gen3.close();
        String xml3 = outputStream3.toString();
        assertEquals("<test>Test</test>", xml3);

        // Test serializing a custom JavaBean with an invalid root name
        SerializationConfig config = MAPPER.getDefaultConfig();
        config.setRootValueConstructor(TestBean.class);
        config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        config.setSerializationFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SerializerProviderFactory factory = new SerializerProviders.BaseSerializerProviderFactory();
        XmlSerializerProvider providerWithSerializers = new XmlSerializerProvider(
                new XmlSerializerProvider(), config, factory);
        ByteArrayOutputStream outputStream4 = new ByteArrayOutputStream();
        JsonGenerator gen4 = MAPPER.getFactory()
                .createXmlGenerator(outputStream4, null, null, null, new StandardSerializer(TestBean.class));
        providerWithSerializers.serializeValue(gen4, new TestBean());
        gen4.close();
        String xml4 = outputStream4.toString();
        assertEquals("<test/>", xml4);

        // Test serializing a custom JavaBean with a custom root name
        SerializerProviderFactory factory2 = new SerializerProviders.BaseSerializerProviderFactory();
        XmlRootNameLookup rootNameLookup2 = new XmlRootNameLookup();
        rootNameLookup2.putRootName("test2", TestBean.class);
        XmlSerializerProvider providerWithCustomRootName = new XmlSerializerProvider(
                new XmlSerializerProvider(), config, factory2, rootNameLookup2);
        ByteArrayOutputStream outputStream5 = new ByteArrayOutputStream();
        JsonGenerator gen5 = MAPPER.getFactory()
                .createXmlGenerator(outputStream5, null, null, null, new StandardSerializer(TestBean.class));
        providerWithCustomRootName.serializeValue(gen5, new TestBean());
        gen5.close();
        String xml5 = outputStream5.toString();
        assertEquals("<test2>Test</test2>", xml5);

        // Test serializing a custom JavaBean as an array
        JavaType javaTypeArray = MAPPER.getTypeFactory().constructType(TestBean[].class);
        ByteArrayOutputStream outputStream6 = new ByteArrayOutputStream();
        JsonGenerator gen6 = MAPPER.getFactory()
                .createXmlGenerator(outputStream6, null, null, null, null);
        provider.serializeValue(gen6, new TestBean[2]);
        gen6.close();
        String xml6 = outputStream6.toString();
        assertEquals("<test>[Test, Test]</test>", xml6);

        // Test serializing a custom JavaBean as an array with a custom root name
        SerializerProviderFactory factory3 = new SerializerProviders.BaseSerializerProviderFactory();
        XmlRootNameLookup rootNameLookup3 = new XmlRootNameLookup();
        rootNameLookup3.putRootName("test3", TestBean[].class);
        XmlSerializerProvider providerWithCustomRootNameArray = new XmlSerializerProvider(
                new XmlSerializerProvider(), config, factory3, rootNameLookup3);
        ByteArrayOutputStream outputStream7 = new ByteArrayOutputStream();
        JsonGenerator gen7 = MAPPER.getFactory()
                .createXmlGenerator(outputStream7, null, null, null, null);
        providerWithCustomRootNameArray.serializeValue(gen7, new TestBean[2]);
        gen7.close();
        String xml7 = outputStream7.toString();
        assertEquals("<test3>[Test, Test]</test3>", xml7);

        // Test serializing a custom JavaBean with a custom root name and a custom serializer
        StandardSerializer customSerializer = new StandardSerializer(TestBean.class) {
            @Override
            public void serialize(TestBean value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("name");
                gen.writeValue(value.getName());
                gen.writeEndObject();
            }
        };
        XmlSerializerProvider providerWithCustomSerializer = new XmlSerializerProvider(
                new XmlSerializerProvider(), config, null, rootNameLookup2);
        providerWithCustomSerializer.setSerializerInstance(TestBean.class, customSerializer);
        ByteArrayOutputStream outputStream8 = new ByteArrayOutputStream();
        JsonGenerator gen8 = MAPPER.getFactory()
                .createXmlGenerator(outputStream8, null, null, null, new StandardSerializer(TestBean.class));
        providerWithCustomSerializer.serializeValue(gen8, new TestBean());
        gen8.close();
        String xml8 = outputStream8.toString();
        assertEquals("<test><name>Test</name></test>", xml8);

        // Test serializing a custom JavaBean with a custom root name and a custom deserializer
        Converter<String, TestBean> converter = new Converter<String, TestBean>() {
            @Override
            public TestBean convert(String text) throws IOException {
                return new TestBean(text);
            }

            @Override
            public String convertAndWrite(String text, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("name");
                gen.writeValue(text);
                gen.writeEndObject();
                return null;
            }
        };
        TypeDeserializer customDeserializer = new CustomTypeDeserializer(converter);
        XmlSerializerProvider providerWithCustomDeserializer = new XmlSerializerProvider(
                new XmlSerializerProvider(), config, null, rootNameLookup2);
        providerWithCustomDeserializer.setDefaultUseWrapper(false);
        ByteArrayOutputStream outputStream9 = new ByteArrayOutputStream();
        JsonParser parser = MAPPER.getFactory().createParser(new ByteArrayInputStream(xml5.getBytes()));
        providerWithCustomDeserializer.setRootValueFactory(
                ClassUtil.handleMappedName("test2", TestBean.class, null, null, null, null, null, null, null));
        TestBean valueFromXml = parser.readValueAs(TestBean.class, providerWithCustomDeserializer);
        parser.close();
        assertEquals("Test2", valueFromXml.getName());

        // Test serializing a custom JavaBean with a custom root name and a custom serializer and deserializer
        XmlSerializerProvider providerWithCustomSerializerAndDeserializer = new XmlSerializerProvider(
                new XmlSerializerProvider(), config, null, rootNameLookup2);
        providerWithCustomSerializerAndDeserializer.setSerializerInstance(TestBean.class, customSerializer);
        providerWithCustomSerializerAndDeserializer.setDefaultUseWrapper(false);
        providerWithCustomSerializerAndDeserializer.setDefaultSerializerInstance(TestBean.class, customSerializer);
        providerWithCustomSerializerAndDeserializer.setDefaultDeserializerInstance(TestBean.class, customDeserializer);
        ByteArrayOutputStream outputStream10 = new ByteArrayOutputStream();
        JsonGenerator gen10 = MAPPER.getFactory()
                .createXmlGenerator(outputStream10, null, null, null, new StandardSerializer(TestBean.class));
        providerWithCustomSerializerAndDeserializer.serializeValue(gen10, new TestBean());
        gen10.close();
        String xml10 = outputStream10.toString();
        assertEquals("<test><name>Test</name></test>", xml10);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml10.getBytes());
        TestBean valueFromXml10 = MAPPER.readValue(inputStream, TestBean.class);
        assertEquals("Test", valueFromXml10.getName());
    }

    private static class CustomTypeDeserializer extends StaxXmlDeserializer<TestBean> {
        private final Converter<String, TestBean> _converter;

        public CustomTypeDeserializer(Converter<String, TestBean> converter) {
            super(TestBean.class, new StandardDeserializer<>(String.class));
            _converter = converter;
        }

        @Override
        protected TestBean _parseRootValue(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            String text = p.getText();
            return _converter.convert(text);
        }
    }

    private static class TestBean {
        private final String name;

        public TestBean() {
            this(null);
        }

        public TestBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}