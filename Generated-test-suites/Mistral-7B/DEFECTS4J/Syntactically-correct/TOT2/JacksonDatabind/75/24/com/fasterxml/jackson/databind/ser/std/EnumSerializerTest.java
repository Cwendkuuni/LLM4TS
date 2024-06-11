import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeEnumUsingIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), true);
        JsonGenerator gen = objectMapper.getPolymorphicSerializer(MyEnum.class).getSerializerProvider().getGenerator();

        MyEnum enumValue = MyEnum.RED;
        serializer.serialize(enumValue, gen, null);

        Assert.assertEquals(0, gen.getCurrentPosition().get());
        Assert.assertEquals(Integer.toString(enumValue.ordinal()), gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeEnumUsingToString() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);
        JsonGenerator gen = objectMapper.getPolymorphicSerializer(MyEnum.class).getSerializerProvider().getGenerator();

        MyEnum enumValue = MyEnum.RED;
        serializer.serialize(enumValue, gen, null);

        Assert.assertEquals(5, gen.getCurrentPosition().get());
        Assert.assertEquals(enumValue.toString(), gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeEnumUsingName() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(objectMapper.getSerializationConfig(), MyEnum.class), null);
        JsonGenerator gen = objectMapper.getPolymorphicSerializer(MyEnum.class).getSerializerProvider().getGenerator();

        MyEnum enumValue = MyEnum.RED;
        serializer.serialize(enumValue, gen, null);

        Assert.assertEquals(5, gen.getCurrentPosition().get());
        Assert.assertEquals(enumValue.name(), gen.getOutputContent().toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullEnum() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), false);
        JsonGenerator gen = objectMapper.getPolymorphicSerializer(MyEnum.class).getSerializerProvider().getGenerator();

        serializer.serialize(null, gen, null);
    }

    @Test
    public void testDeserializeEnumUsingIndex() throws Exception {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("type", "MyEnum");
        rootNode.put("value", 1);

        MyEnum deserializedEnum = objectMapper.readValue(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode), MyEnum.class);

        Assert.assertEquals(MyEnum.GREEN, deserializedEnum);
    }

    @Test
    public void testDeserializeEnumUsingToString() throws Exception {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("type", "MyEnum");
        rootNode.put("value", "GREEN");

        MyEnum deserializedEnum = objectMapper.readValue(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode), MyEnum.class);

        Assert.assertEquals(MyEnum.GREEN, deserializedEnum);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEnumUsingInvalidValue() throws Exception {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("type", "MyEnum");
        rootNode.put("value", "InvalidValue");

        objectMapper.readValue(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode), MyEnum.class);
    }

    @Test
    public void testGetSchema() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), null);
        JsonNode schema = serializer.getSchema(objectMapper.getSerializationConfig(), null);

        Assert.assertTrue(schema.isObject());
        Assert.assertEquals("string", schema.path("type").asText());
        Assert.assertTrue(schema.path("properties").isObject());
        Assert.assertEquals("enum", schema.path("properties").get(MyEnum.class.getName()).getName());
        Assert.assertTrue(schema.path("properties").get(MyEnum.class.getName()).isArray());
        Assert.assertFalse(schema.path("properties").isDefined("value"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.values(MyEnum.class), null);

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonFormatVisitorWrapper visitor = factory.constructSchemaVisitor(null);
        SerializerProvider serializers = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> nullValueSerializer) {
                return serializer;
            }
        };

        Type typeHint = MyEnum.class;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        JsonNode schema = visitor.getSchemaProvider().getSchema(typeHint);

        Assert.assertTrue(schema.isObject());
        Assert.assertEquals("string", schema.path("type").asText());
        Assert.assertTrue(schema.path("properties").isObject());
        Assert.assertEquals("enum", schema.path("properties").get(MyEnum.class.getName()).getName());
        Assert.assertTrue(schema.path("properties").get(MyEnum.class.getName()).isArray());
        Assert.assertFalse(schema.path("properties").isDefined("value"));
    }
}

enum MyEnum {
    RED,
    GREEN,
    BLUE
}