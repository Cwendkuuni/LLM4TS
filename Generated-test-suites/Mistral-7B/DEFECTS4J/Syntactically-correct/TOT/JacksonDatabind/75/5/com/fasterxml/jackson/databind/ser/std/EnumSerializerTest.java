import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;

public class EnumSerializerTest {

    private ObjectMapper mapper;

    public EnumSerializerTest() {
        mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeEnumAsString() throws JsonMappingException, JsonGenerationException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class), null);
        ObjectNode node = mapper.valueToTree(MyEnum.FOO);
        Assert.assertEquals("FOO", node.get("value").asText());
    }

    @Test
    public void testSerializeEnumAsIndex() throws JsonMappingException, JsonGenerationException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class), true);
        ObjectNode node = mapper.valueToTree(MyEnum.BAR);
        Assert.assertEquals(1, node.get("value").asInt());

        mapper.configure(WRITE_ENUMS_USING_TO_STRING, false);
        node = mapper.valueToTree(MyEnum.BAR);
        Assert.assertEquals(1, node.get("value").asInt());

        mapper.configure(WRITE_ENUMS_USING_TO_STRING, true);
        node = mapper.valueToTree(MyEnum.BAR);
        Assert.assertEquals("BAR", node.get("value").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeEnumUnknownValue() throws JsonMappingException, JsonGenerationException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valuesOf(MyEnum.class), null);
        ObjectNode node = mapper.valueToTree(new EnumValue("UNKNOWN"));
        mapper.writeValueAsString(node);
    }

    @Test
    public void testDeserializeEnumAsString() throws JsonMappingException, JsonGenerationException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "FOO");
        MyEnum enumValue = mapper.treeToValue(node, MyEnum.class);
        Assert.assertEquals(MyEnum.FOO, enumValue);
    }

    @Test
    public void testDeserializeEnumAsIndex() throws JsonMappingException, JsonGenerationException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", 0);
        MyEnum enumValue = mapper.treeToValue(node, MyEnum.class);
        Assert.assertEquals(MyEnum.FOO, enumValue);

        node.put("value", 1);
        enumValue = mapper.treeToValue(node, MyEnum.class);
        Assert.assertEquals(MyEnum.BAR, enumValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEnumUnknownJson() throws JsonMappingException, JsonGenerationException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "UNKNOWN_VALUE");
        mapper.treeToValue(node, MyEnum.class);
    }

    @Test
    public void testDeserializeEnumUnknownValueWithToString() throws JsonMappingException, JsonGenerationException {
        mapper.configure(WRITE_ENUMS_USING_TO_STRING, true);
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "UNKNOWN_VALUE");
        MyEnum enumValue = mapper.treeToValue(node, MyEnum.class);
        Assert.assertNull(enumValue);
    }

    @Test
    public void testDeserializeEnumUnknownValueWithoutToString() throws JsonMappingException, JsonGenerationException {
        mapper.configure(WRITE_ENUMS_USING_TO_STRING, false);
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "UNKNOWN_VALUE");
        MyEnum enumValue = mapper.treeToValue(node, MyEnum.class);
        Assert.assertNull(enumValue);
    }

    private enum MyEnum {
        FOO, BAR,
        @JsonFormat(shape = Shape.STRING, serializableAs = MyCustomEnum.class)
        CUSTOM
    }

    private static class EnumValue {
        private final String name;

        public EnumValue(String name) {
            this.name = name;
        }

        public String getValue() {
            return name;
        }
    }
}