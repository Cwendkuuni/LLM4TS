package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;
    private XmlMapper xmlMapper;
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        xmlMapper = new XmlMapper();
        xmlMapper.setSerializerProvider(new DefaultSerializerProvider());
        xmlSerializerProvider = new XmlSerializerProvider(xmlMapper.getRootValueSerializer(), xmlMapper.getSerializationConfig(), xmlMapper.getSerializerFactory());
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, JsonMappingException, IOException {
        QName rootName = XmlSerializerProvider.ROOT_NAME_FOR_NULL;
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        try {
            xmlSerializerProvider.serializeValue(gen, null);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"\" />";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializeBoolean() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        boolean value = true;
        QName rootName = new QName("root");
        try {
            xmlSerializerProvider.serializeValue(gen, value, rootName);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<root><boolean>true</boolean></root>";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializeInt() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        int value = 123;
        QName rootName = new QName("root");
        try {
            xmlSerializerProvider.serializeValue(gen, value, rootName);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<root><integer>123</integer></root>";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        String value = "abc";
        QName rootName = new QName("root");
        try {
            xmlSerializerProvider.serializeValue(gen, value, rootName);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<root><string>abc</string></root>";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializeCustomClass() throws JsonGenerationException, JsonMappingException, IOException {
        class CustomClass {
            private String name;

            public CustomClass(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        CustomClass value = new CustomClass("custom");
        QName rootName = new QName("root");
        try {
            StdSerializer<CustomClass> serializer = new StdSerializer<CustomClass>(CustomClass.class) {
                @Override
                public void serialize(CustomClass value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                    gen.writeStartObject();
                    gen.writeFieldName("name");
                    gen.writeString(value.getName());
                    gen.writeEndObject();
                }
            };
            xmlMapper.registerSerializer(CustomClass.class, serializer);
            xmlSerializerProvider = new XmlSerializerProvider(xmlMapper.getRootValueSerializer(), xmlMapper.getSerializationConfig(), xmlMapper.getSerializerFactory());
            xmlSerializerProvider.setSerializerInstance(CustomClass.class, serializer);
            xmlSerializerProvider.serializeValue(gen, value, rootName);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<root><name>custom</name></root>";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullRootName() throws JsonProcessingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        Object value = Arrays.asList(new Object[]{null, "abc"});
        JavaType rootType = objectMapper.getTypeFactory().constructType(Arrays.class);
        try {
            xmlSerializerProvider.serializeValue(gen, value, rootType);
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializeArray() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        Object value = Arrays.asList(new Object[]{true, 123, "abc"});
        JavaType rootType = objectMapper.getTypeFactory().constructType(Arrays.class);
        QName rootName = new QName("root");
        try {
            xmlSerializerProvider.serializeValue(gen, value, rootName);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<root><item><boolean>true</boolean></item>" +
                              "<item><integer>123</integer></item>" +
                              "<item><string>abc</string></item></root>";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializeCustomRootName() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        Object value = Arrays.asList(new Object[]{new Object(){}, "abc"});
        JavaType rootType = objectMapper.getTypeFactory().constructType(Arrays.class);
        QName rootName = new QName("root", "prefix");
        try {
            xmlSerializerProvider._initWithRootName(gen, rootName);
            xmlSerializerProvider.serializeValue(gen, value, rootType);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<root xmlns:prefix=\"prefix\"><item><xsi:type>java.lang.Object</xsi:type></item><item><string>abc</string></item></root>";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializeNullRootNameFromConfig() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        Object value = Arrays.asList(new Object[]{new Object(){}, "abc"});
        JavaType rootType = objectMapper.getTypeFactory().constructType(Arrays.class);
        try {
            xmlSerializerProvider._rootNameLookup = new XmlRootNameLookup() {
                @Override
                public QName findRootName(Class<?> clazz, SerializationConfig config) {
                    return null;
                }
            };
            xmlSerializerProvider._initWithRootName(gen, null);
            xmlSerializerProvider.serializeValue(gen, value, rootType);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item><xsi:type>java.lang.Object</xsi:type></item><item><string>abc</string></item></root>";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeCustomRootNameNotFound() throws JsonProcessingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        Object value = Arrays.asList(new Object[]{new Object(){}, "abc"});
        JavaType rootType = objectMapper.getTypeFactory().constructType(Arrays.class);
        QName rootName = new QName("root", "prefix");
        try {
            xmlSerializerProvider._rootNameLookup = new XmlRootNameLookup() {
                @Override
                public QName findRootName(Class<?> clazz, SerializationConfig config) {
                    return null;
                }
            };
            xmlSerializerProvider._initWithRootName(gen, rootName);
            xmlSerializerProvider.serializeValue(gen, value, rootType);
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializeEmptyArray() throws JsonGenerationException, JsonMappingException, IOException {
        JsonGenerator gen = StaxUtil.createJsonGenerator(true, "none", null);
        Object value = new Object[0];
        JavaType rootType = objectMapper.getTypeFactory().constructType(Object[].class);
        try {
            xmlSerializerProvider.serializeValue(gen, value, rootType);
            gen.flush();
            String actual = gen.getBuffer().asString();
            String expected = "<root><item></item></root>";
            assertEquals(expected, actual);
        } finally {
            gen.close();
        }
    }
}