package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

public class EnumSerializerTest extends BaseMapTest {

    private static enum TestEnum {
        ONE("One"),
        TWO("Two"),
        THREE("Three"),
        FOUR("Four");

        private final String name;

        TestEnum(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private static class TestBean {
        private Enum<TestEnum> enumField;

        public Enum<TestEnum> getEnumField() {
            return enumField;
        }

        public void setEnumField(Enum<TestEnum> enumField) {
            this.enumField = enumField;
        }
    }

    @org.junit.Test
    public void testSerializeEnum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        bean.setEnumField(TestEnum.ONE);

        String json = mapper.writeValueAsString(bean);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());

        EnumSerializer serializer = new EnumSerializer(TestEnum.class, null);
        serializer.serialize(TestEnum.ONE, gen, null);
        String expected = TestEnum.ONE.getName();

        assertEquals("Check serialization of enum using name", expected, node.get("enumField").asText());
        assertEquals("Check serialization of enum using serializer", expected, gen.getOutputBuffer().toString().split("\\s+")[0]);
    }

    @org.junit.Test
    public void testSerializeEnumWithCustomSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("serializeAsIndex", true);
        SimpleBeanPropertyFilter filter = SimpleFilterProvider.filterEmptyBeans().withSerializedName("testBean");
        ObjectWriter writer = mapper.writerWithView(JsonView.class, filter);

        TestBean bean = new TestBean();
        bean.setEnumField(TestEnum.ONE);

        String json = writer.writeValueAsString(bean);
        JsonParser parser = mapper.getFactory().createParser(json);
        parser.nextToken();

        JsonNode nodeFromJson = parser.readValueAsTree();
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());

        EnumSerializer serializer = new EnumSerializer(TestEnum.class, true);
        serializer.serialize(TestEnum.ONE, gen, null);

        assertEquals("Check serialization of enum using index", Integer.valueOf(TestEnum.ONE.ordinal()), nodeFromJson.get("enumField"));
        assertEquals("Check serialization of enum using serializer", TestEnum.ONE.ordinal(), gen.getOutputBuffer().asInt());
    }

    @org.junit.Test
    public void testDeserializeEnum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"enumField\":\"Two\"}";
        TestBean bean = mapper.readValue(json, TestBean.class);

        assertEquals("Check deserialization of enum using name", TestEnum.TWO, bean.getEnumField());
    }

    @org.junit.Test
    public void testDeserializeEnumWithCustomSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"serializeAsIndex\":true,\"enumField\":1}";
        TestBean bean = mapper.readerForUpdating(TestBean.class).readValue(json);

        assertEquals("Check deserialization of enum using index", TestEnum.TWO, bean.getEnumField());
    }

    @org.junit.Test
    public void testSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(TestEnum.class, null);

        ObjectNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), null);
        String schemaString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaNode);

        String expectedSchema = "{\"type\":\"object\",\"properties\":{\"enum\":{\"type\":\"array\",\"items\":[{\"type\":\"string\",\"enum\":[" +
                TestEnum.ONE.getName() + ",\" " + TestEnum.TWO.getName() + "\",\" " + TestEnum.THREE.getName() + "\",\" " + TestEnum.FOUR.getName() + "]}}}}";

        assertEquals("Check schema for enum serializer", expectedSchema, schemaString);
    }
}