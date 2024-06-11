package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(config.getRootValueSerializer());

        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, null);
        gen.close();

        String xml = ((TokenBuffer) gen).asXml();
        Assert.assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializeSimpleBean() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new RootNameSerializeTestSerializer());
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(config.getRootValueSerializer());

        SimpleBean bean = new SimpleBean("Test");
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, bean);
        gen.close();

        String xml = ((TokenBuffer) gen).asXml();
        Assert.assertEquals("<SimpleBean><name>Test</name></SimpleBean>", xml);
    }

    @Test
    public void testSerializeArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new RootNameSerializeTestSerializer());
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(config.getRootValueSerializer());

        SimpleBean[] beans = {new SimpleBean("Test1"), new SimpleBean("Test2")};
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, beans);
        gen.close();

        String xml = ((TokenBuffer) gen).asXml();
        Assert.assertEquals("<SimpleBean xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\">" +
                "<item><name>Test1</name></item>" +
                "<item><name>Test2</name></item>" +
                "</SimpleBean>", xml);
    }

    @Test
    public void testSerializeArrayWithCustomRootName() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new RootNameSerializeTestSerializer("MyArray"));
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(config.getRootValueSerializer());

        SimpleBean[] beans = {new SimpleBean("Test1"), new SimpleBean("Test2")};
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, beans);
        gen.close();

        String xml = ((TokenBuffer) gen).asXml();
        Assert.assertEquals("<MyArray xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\">" +
                "<item><name>Test1</name></item>" +
                "<item><name>Test2</name></item>" +
                "</MyArray>", xml);
    }

    @Test
    public void testSerializeEmptyArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new RootNameSerializeTestSerializer("MyArray"));
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(config.getRootValueSerializer());

        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, new Object[0]);
        gen.close();

        String xml = ((TokenBuffer) gen).asXml();
        Assert.assertEquals("<MyArray xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", xml);
    }

    @Test
    public void testSerializeEmptyObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new RootNameSerializeTestSerializer());
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(config.getRootValueSerializer());

        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, new Object());
        gen.close();

        String xml = ((TokenBuffer) gen).asXml();
        Assert.assertEquals("<Object xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", xml);
    }

    @Test
    public void testSerializeCustomRootNameForObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSerializer(new RootNameSerializeTestSerializer("MyObject"));
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(config.getRootValueSerializer());

        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, new Object());
        gen.close();

        String xml = ((TokenBuffer) gen).asXml();
        Assert.assertEquals("<MyObject xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", xml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullConfig() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(null);
        JsonGenerator gen = new TokenBuffer();

        xmlProvider.serializeValue(gen, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(null);

        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, new Object());
    }

    static class RootNameSerializeTestSerializer extends JsonSerializer<Object> {
        private final String rootName;

        public RootNameSerializeTestSerializer() {
            this(null);
        }

        public RootNameSerializeTestSerializer(String rootName) {
            this.rootName = rootName;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            XmlSerializerProvider xmlProvider = (XmlSerializerProvider) serializers;
            if (rootName != null) {
                xmlProvider._initWithRootName(gen, new QName(null, rootName));
            }
            gen.writeStartObject();
            gen.writeEndObject();
        }
    }

    static class SimpleBean {
        private String name;

        public SimpleBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}