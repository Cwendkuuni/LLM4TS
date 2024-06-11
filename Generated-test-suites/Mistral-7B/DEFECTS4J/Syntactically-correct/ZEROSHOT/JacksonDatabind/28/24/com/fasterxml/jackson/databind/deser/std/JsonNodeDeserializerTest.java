package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonNodeDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Before
    public void setup() throws IOException {
        MAPPER.registerModule(new SimpleModule()
                .addDeserializer(JsonNode.class, JsonNodeDeserializer.getInstance()));
    }

    @Test
    public void testNullValue() throws IOException {
        JsonNode expected = JsonNodeFactory.instance.nullNode();
        JsonParser p = MAPPER.getFactory().createParser("null");
        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.textNode("John"))
                .put("age", JsonNodeFactory.instance.numberNode(30));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.arrayNode(Arrays.asList(
                JsonNodeFactory.instance.numberNode(1),
                JsonNodeFactory.instance.numberNode(2),
                JsonNodeFactory.instance.numberNode(3)
        ));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeAny() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"boolean\":true}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);

        JsonNode expectedObject = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.textNode("John"))
                .put("age", JsonNodeFactory.instance.numberNode(30));
        JsonNode expectedBoolean = JsonNodeFactory.instance.booleanNode(true);

        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", expectedObject)
                .put("age", expectedObject)
                .put("boolean", expectedBoolean);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithDuplicateFields() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"name\":\"Jane\"}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        deserializer.setFailOnReadingDuplicateTreeKeys(true);

        JsonNode actual = deserializer.deserialize(p, null);

        Assert.assertThrows(JsonMappingException.class, () -> {
            JsonNode expected = JsonNodeFactory.instance.objectNode()
                    .put("name", JsonNodeFactory.instance.textNode("John"))
                    .put("age", JsonNodeFactory.instance.numberNode(30));
            deserializer.deserialize(p, null, expected);
        });

        Assert.assertEquals(actual, JsonNodeFactory.instance.nullNode());
    }

    @Test
    public void testDeserializeObjectWithEmptyValue() throws IOException {
        String json = "{}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.arrayNode();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeArrayWithEmptyValue() throws IOException {
        String json = "[{}]";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.arrayNode(Collections.singletonList(JsonNodeFactory.instance.objectNode()));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedObjects() throws IOException {
        String json = "{\"name\":{\"firstName\":\"John\",\"lastName\":\"Doe\"}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.textNode("John"))
                        .put("lastName", JsonNodeFactory.instance.textNode("Doe")));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedArrays() throws IOException {
        String json = "{\"numbers\":[1, [2, 3, 4], [5, 6, 7]]}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("numbers", JsonNodeFactory.instance.arrayNode(
                        Arrays.asList(
                                JsonNodeFactory.instance.numberNode(1),
                                JsonNodeFactory.instance.arrayNode(Arrays.asList(
                                        JsonNodeFactory.instance.numberNode(2),
                                        JsonNodeFactory.instance.numberNode(3),
                                        JsonNodeFactory.instance.numberNode(4)
                                )),
                                JsonNodeFactory.instance.arrayNode(Arrays.asList(
                                        JsonNodeFactory.instance.numberNode(5),
                                        JsonNodeFactory.instance.numberNode(6),
                                        JsonNodeFactory.instance.numberNode(7)
                                ))
                        )));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedObjectsAndArrays() throws IOException {
        String json = "{\"name\":{\"firstName\":\"John\",\"lastName\":[\"Doe\", \"Smith\"]}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.textNode("John"))
                        .put("lastName", JsonNodeFactory.instance.arrayNode(Arrays.asList(
                                JsonNodeFactory.instance.textNode("Doe"),
                                JsonNodeFactory.instance.textNode("Smith")
                        )))
                );

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithEmptyNestedObjects() throws IOException {
        String json = "{\"name\":{\"firstName\":{}, \"lastName\":{}}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.objectNode())
                        .put("lastName", JsonNodeFactory.instance.objectNode())
                );

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedObjectsAndNullValues() throws IOException {
        String json = "{\"name\":{\"firstName\":null, \"lastName\":null}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.nullNode())
                        .put("lastName", JsonNodeFactory.instance.nullNode())
                );

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithEmptyStringValues() throws IOException {
        String json = "{\"name\":{\"firstName\":\"\", \"lastName\":\"\"}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.textNode(""))
                        .put("lastName", JsonNodeFactory.instance.textNode(""))
                );

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithEmptyNestedArrays() throws IOException {
        String json = "{\"name\":{\"firstName\":[], \"lastName\":[]}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.arrayNode())
                        .put("lastName", JsonNodeFactory.instance.arrayNode())
                );

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedArraysAndNullValues() throws IOException {
        String json = "{\"name\":{\"firstName\":[null], \"lastName\":[null, null]}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.arrayNode(Collections.singletonList(JsonNodeFactory.instance.nullNode())))
                        .put("lastName", JsonNodeFactory.instance.arrayNode(Arrays.asList(JsonNodeFactory.instance.nullNode(), JsonNodeFactory.instance.nullNode())))
                );

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedArraysAndEmptyValues() throws IOException {
        String json = "{\"name\":{\"firstName\":[], \"lastName\":[\"\", \"\"]}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.arrayNode())
                        .put("lastName", JsonNodeFactory.instance.arrayNode(Arrays.asList(JsonNodeFactory.instance.textNode(""), JsonNodeFactory.instance.textNode(""))))
                );

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedObjectsAndSimpleValues() throws IOException {
        String json = "{\"name\":{\"firstName\":\"John\", \"lastName\":123}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.textNode("John"))
                        .put("lastName", JsonNodeFactory.instance.numberNode(123))
                );

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedObjectsAndComplexValues() throws IOException {
        String json = "{\"name\":{\"firstName\":{\"name\":\"John\"}, \"lastName\":[{\"name\":\"Doe\"}, {\"name\":\"Smith\"}]}}";
        JsonParser p = MAPPER.getFactory().createParser(json);

        JsonNode actual = JsonNodeDeserializer.getInstance().deserialize(p, null);
        JsonNode expected = JsonNodeFactory.instance.objectNode()
                .put("name", JsonNodeFactory.instance.objectNode()
                        .put("firstName", JsonNodeFactory.instance.objectNode()
                                .put("name", JsonNodeFactory.instance.textNode("John")))
                        .put("lastName", JsonNodeFactory.instance.arrayNode(
                                Arrays.asList(
                                        JsonNodeFactory.instance.objectNode()
                                                .put("name", JsonNodeFactory.instance.textNode("Doe")),
                                        JsonNodeFactory.instance.objectNode()
                                                .put("name", JsonNodeFactory.instance.textNode("Smith"))
                                )
                        ))
                );

        Assert.assertEquals(expected, actual);
    }
}