package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.StdUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootElement;
import com.fasterxml.jackson.dataformat.xml.util.DefaultPrettyPrinter;

import org.junit.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

@SuppressWarnings("unchecked")
public class XmlSerializerProviderTest {

    private static final ObjectMapper mapper = new XmlMapper();
    private static final SerializationConfig config = mapper.getSerializationConfig();

    @BeforeClass
    public static void setUp() {
        config.setRootValueConverter(StdUtil.nullValue(QName.class), new QNameSerializer());
    }

    @Test
    public void testSerializeNullValue() throws IOException, XMLStreamException {
        ObjectWriter writer = mapper.writer().with(new XmlSerializerProvider());
        String xml = writer.writeValueAsString(null);
        JsonNode json = mapper.readTree(xml);
        assertEquals("null", json.asText());
    }

    @Test
    public void testSerializeObject() throws IOException, XMLStreamException {
        @JsonRootElement(namespace = "ns1")
        static class TestObject {
            private String name;

            public TestObject(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        TestObject testObject = new TestObject("Test");
        ObjectWriter writer = mapper.writer().with(new XmlSerializerProvider());
        String xml = writer.writeValueAsString(testObject);
        JsonNode json = mapper.readTree(xml);

        assertEquals("ns1:Test", json.getName());
        assertEquals("Test", json.path("name").asText());
    }

    @Test
    public void testSerializeArray() throws IOException, XMLStreamException {
        static class TestArray {
            private int[] ints;

            public TestArray(int[] ints) {
                this.ints = ints;
            }

            public int[] getInts() {
                return ints;
            }
        }

        TestArray testArray = new TestArray(new int[] {1, 2, 3});
        ObjectWriter writer = mapper.writer().with(new XmlSerializerProvider());
        String xml = writer.writeValueAsString(testArray);
        JsonNode json = mapper.readTree(xml);

        assertEquals("item", json.get("item").get("item").fieldNames().nextValue().asText());
        assertEquals(1, json.path("item[0]/@value").asInt());
        assertEquals(2, json.path("item[1]/@value").asInt());
        assertEquals(3, json.path("item[2]/@value").asInt());
    }

    @Test
    public void testSerializeObjectWithCustomRootElement() throws IOException, XMLStreamException {
        static class CustomRoot {
            @JsonSerialize(using = CustomRootSerializer.class)
            private TestObject testObject;

            public CustomRoot(TestObject testObject) {
                this.testObject = testObject;
            }
        }

        static class TestObject {
            private String name;

            public TestObject(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        TestObject testObject = new TestObject("Test");
        CustomRoot customRoot = new CustomRoot(testObject);

        ObjectWriter writer = mapper.writer().with(new XmlSerializerProvider());
        String xml = writer.writeValueAsString(customRoot);
        JsonNode json = mapper.readTree(xml);

        assertEquals("customRoot", json.getName());
        assertEquals("Test", json.path("testObject/name").asText());
    }

    @Test
    public void testSerializeObjectWithEmptyNamespace() throws IOException, XMLStreamException {
        static class TestObject {
            private String name;

            public TestObject(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        TestObject testObject = new TestObject("Test");
        ObjectWriter writer = mapper.writer().with(new XmlSerializerProvider());
        String xml = writer.writeValueAsString(testObject);
        JsonNode json = mapper.readTree(xml);

        assertEquals("", json.getName());
        assertEquals("Test", json.path("name").asText());
    }

    @Test
    public void testSerializeObjectWithCustomRootName() throws IOException, XMLStreamException {
        static class TestObject {
            private String name;

            public TestObject(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        TestObject testObject = new TestObject("Test");
        ObjectMapper mapper = new XmlMapper();
        mapper.setRootValueConverter(JsonNode.class, new QNameSerializer());
        mapper.setSerializationConfig(mapper.getSerializationConfig().withRootName("customRoot"));
        ObjectWriter writer = mapper.writer().with(new XmlSerializerProvider());
        String xml = writer.writeValueAsString(testObject);
        JsonNode json = mapper.readTree(xml);

        assertEquals("customRoot", json.getName());
        assertEquals("Test", json.path("name").asText());
    }

    static class QNameSerializer extends StdSerializer<QName> {
        public QNameSerializer() {
            this(null);
        }

        public QNameSerializer(Class<QName> t) {
            this(null);
        }

        public QNameSerializer(Class<QName> t, PropertyName pn) {
            super(t, pn);
        }

        @Override
        public void serialize(QName qname, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (qname == null) {
                gen.writeNull();
                return;
            }

            String ns = qname.getNamespaceURI();
            if (ns == null || ns.isEmpty()) {
                gen.writeString(qname.getLocalPart());
            } else {
                gen.writeStartObject();
                gen.writeFieldName("namespaceURI");
                gen.writeString(ns);
                gen.writeFieldName("localPart");
                gen.writeString(qname.getLocalPart());
                gen.writeEndObject();
            }
        }
    }

    static class CustomRootSerializer extends StdSerializer<CustomRoot> {
        public CustomRootSerializer() {
            this(null);
        }

        public CustomRootSerializer(Class<CustomRoot> t) {
            this(null);
        }

        public CustomRootSerializer(Class<CustomRoot> t, PropertyName pn) {
            super(t, pn);
        }

        @Override
        public void serialize(CustomRoot customRoot, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("testObject");
            ObjectWriter objectWriter = mapper.writer().with(new XmlSerializerProvider());
            objectWriter.writeValue(gen, customRoot.getTestObject());
            gen.writeEndObject();
        }
    }

}