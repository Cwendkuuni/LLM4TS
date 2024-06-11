package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer.EnumValues;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Type;
import static org.junit.Assert.*;

@Test(desc = "Test for EnumSerializer class")
public class EnumSerializerTest {

    @TestData
    public static Object[][] testData() {
        return new Object[][]{
                {MyEnum.ONE, "ONE"},
                {MyEnum.TWO, "TWO"},
                {MyEnum.THREE, "THREE"},
                {MyEnum.FOUR, "FOUR"},
                {MyEnum.FIVE, "FIVE"},
                {MyEnum.SIX, "SIX"},
                {MyEnum.SEVEN, "SEVEN"},
                {MyEnum.EIGHT, "EIGHT"},
                {MyEnum.NINE, "NINE"}
        };
    }

    @DataProvider
    public Object[][] dataProvider() {
        return testData();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Set up any static variables or classes here
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clean up any static variables or classes here
    }

    @BeforeMethod
    public void setUp() {
        // Set up any instance variables or objects here
    }

    @AfterMethod
    public void tearDown() {
        // Clean up any instance variables or objects here
    }

    @Test(dataProvider = "dataProvider")
    public void testSerializeDeserialize(Enum<?> enumValue, String expectedString)
            throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class));
        ObjectNode node = mapper.valueToTree(serializer.serializeWithOutValue(enumValue, null));
        String actualString = node.get(0).asText();
        assertEquals(expectedString, actualString);

        EnumDeserializer deserializer = mapper.findValue(new ObjectNode(mapper.createObjectNode()),
                MyEnum[].class);
        Enum<?> deserializedEnum = deserializer.deserialize(node, null);
        assertEquals(enumValue, deserializedEnum);
    }

    @Test
    public void testSerializeWithShapeAnnotation()
            throws JsonGenerationException, JsonMappingException, IOException {
        JsonFormat shapeAnnotation = new JsonFormat.Builder()
                .shape(JsonFormat.Shape.STRING)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), false);
        ObjectNode node = mapper.valueToTree(serializer.serializeWithShapeAnnotation(MyEnum.ONE, null, shapeAnnotation));
        String actualString = node.get(0).asText();
        assertEquals("ONE", actualString);
    }

    @Test
    public void testSerializeWithIndex()
            throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class), true);
        int[] indexes = new int[MyEnum.values().length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        ArrayNode node = mapper.valueToTree(serializer.serializeWithOutValue(MyEnum.values(), null));
        int[] actualIndexes = mapper.convertValue(node, int[].class);
        assertArrayEquals(indexes, actualIndexes);
    }

    @Test
    public void testSerializeWithToStringEnabled()
            throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class));
        ObjectNode node = mapper.valueToTree(serializer.serializeWithOutValue(MyEnum.ONE, null));
        String actualString = node.get(0).asText();
        assertEquals("ONE.ONE", actualString);
    }

    @Test
    public void testSerializeWithUnknownValue()
            throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(new EnumValues(MyEnum.class));
        ObjectNode node = mapper.valueToTree(serializer.serializeWithOutValue(new MyUnknownEnum(), null));
        String actualString = node.get(0).asText();
        assertEquals("null", actualString);
    }

    @Test
    public void testDeserializeWithUnknownValue()
            throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("empty", "");
        EnumDeserializer deserializer = mapper.findValue(node, MyEnum[].class);
        Enum<?> deserializedEnum = deserializer.deserialize(node, null);
        assertNull(deserializedEnum);
    }

    enum MyEnum {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE
    }

    enum MyUnknownEnum {
        UNKNOWN
    }
}