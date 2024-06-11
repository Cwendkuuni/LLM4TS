package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EnumSerializerTest {

    @Test
    public void testEnumSerializer() throws IOException, JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.valuesFromClass(MyEnum.class), null);
        ObjectMapper mapper = new ObjectMapper();
        
        // Serialize an Enum value using the default serializer
        MyEnum enumValue = MyEnum.VALUE_1;
        String json = serializer.serialize(enumValue, new JsonFactory(), null);
        assertEquals(enumValue.name(), json);

        // Serialize an Enum value using an index-based serialization
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(json.getBytes()));
        SerializationConfig config = mapper.getSerializationConfig();
        ObjectNode tree = mapper.readTree(parser);
        config.setSerializationFeature(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        EnumSerializer indexSerializer = EnumSerializer.construct(MyEnum.class, config, null, null);
        json = indexSerializer.serialize(enumValue, new JsonFactory(), null);
        byte[] bytes = json.getBytes();
        JsonParser indexParser = factory.createParser(new ByteArrayInputStream(bytes));
        ObjectNode indexTree = mapper.readTree(indexParser);
        assertEquals(Integer.toString(enumValue.ordinal()), indexTree.get("0").asText());
        
        // Deserialize an Enum value using the default deserializer
        MyEnum deserializedEnum = mapper.readValue(json, MyEnum.class);
        assertEquals(enumValue, deserializedEnum);

        // Deserialize an Enum value using an index-based deserializer
        config.setSerializationFeature(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);
        ObjectNode indexTreeForDeserialization = mapper.readTree(indexTree);
        JsonNode indexEnumNode = indexTreeForDeserialization.get("0");
        ObjectMapper indexMapper = JsonMapperFactory.builder()
                .findAndRegisterModules()
                .build();
        EnumSerializer indexDeserializer = new EnumSerializer(MyEnum.class, null);
        MyEnum deserializedEnumIndex = indexDeserializer.deserialize(indexEnumNode, new JsonFactory(), null);
        assertEquals(enumValue, deserializedEnumIndex);
    }

    enum MyEnum {
        VALUE_1, VALUE_2, VALUE_3;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        public String getName() {
            return name();
        }
    }
}