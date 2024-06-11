import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    // Test method for serializing an Enum using the default serializer
    @Test
    public void testSerializeEnumDefault() throws IOException {
        EnumSerializer serializer = objectMapper.findValueSerializer(Enum.class);
        Enum<?> enumValue = Enum.values()[0];
        JsonGenerator gen = objectMapper.getFactory().createGenerator();

        serializer.serialize(enumValue, gen, objectMapper.getSerializationConfig(), null);
        gen.flush();

        String expected = "\"RED\"";
        Assert.assertEquals(expected, gen.getOutputBuffer().toString());
    }

    // Test method for serializing an Enum using the index serializer
    @Test
    public void testSerializeEnumIndex() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(Color.class), true);
        Color enumValue = Color.RED;
        JsonGenerator gen = objectMapper.getFactory().createGenerator();

        serializer.serialize(enumValue, gen, objectMapper.getSerializationConfig(), null);
        gen.flush();

        int expected = 0;
        Assert.assertEquals(expected, gen.getOutputBuffer().readInt());
    }

    // Test method for serializing an Enum using custom names
    @Test
    public void testSerializeEnumCustomNames() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode config = objectMapper.getSerializationConfig().with(new JsonSerializer.Serializers.EnumAsStringSerializer());
        EnumSerializer serializer = objectMapper.findValueSerializer(Enum.class, config);
        Enum<?> enumValue = Enum.values()[0];
        JsonGenerator gen = objectMapper.getFactory().createGenerator();

        serializer.serialize(enumValue, gen, objectMapper.getSerializationConfig(), null);
        gen.flush();

        String expected = "ONE";
        Assert.assertEquals(expected, gen.getOutputBuffer().toString());
    }

    // Test method for deserializing an Enum using the default deserializer
    @Test
    public void testDeserializeEnumDefault() throws IOException {
        String json = "\"RED\"";
        EnumSerializer deserializer = new EnumSerializer(Color.class, null);
        Color enumValue = deserializer.deserialize(json, (Type) Color.class, null);

        Assert.assertEquals(Color.RED, enumValue);
    }

    // Test method for deserializing an Enum using the index deserializer
    @Test
    public void testDeserializeEnumIndex() throws IOException, JsonMappingException {
        String json = "0";
        EnumSerializer deserializer = new EnumSerializer(EnumValues.allValues(Color.class), null);
        Color enumValue = deserializer.deserialize(json, (Type) Color.class, null);

        Assert.assertEquals(Color.RED, enumValue);
    }

    // Test method for deserializing an Enum using custom names
    @Test
    public void testDeserializeEnumCustomNames() throws IOException, JsonMappingException {
        String json = "\"ONE\"";
        EnumSerializer deserializer = new EnumSerializer(EnumValues.allValues(Color.class), null);
        Color enumValue = deserializer.deserialize(json, (Type) Color.class, null);

        Assert.assertEquals(Color.ONE, enumValue);
    }

    // Test method for handling missing Enum values
    @Test(expected = JsonMappingException.class)
    public void testDeserializeEnumMissingValue() throws IOException {
        String json = "\"FOO\"";
        EnumSerializer deserializer = new EnumSerializer(EnumValues.allValues(Color.class), null);
        deserializer.deserialize(json, (Type) Color.class, null);
    }

    // Test method for handling an Enum with no serializable values
    @Test
    public void testSerializeEnumEmpty() throws IOException {
        EnumSerializer serializer = new EnumSerializer(Enum.class, null);
        JsonGenerator gen = objectMapper.getFactory().createGenerator();

        serializer.serialize(null, gen, objectMapper.getSerializationConfig(), null);
        gen.flush();

        String expected = "null";
        Assert.assertEquals(expected, gen.getOutputBuffer().toString());
    }

    // Test method for handling an Enum with custom serialization
    // (using the EnumAsStringSerializer)
    @Test
    public void testSerializeEnumCustomSerialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSerializer(Color[].class, new ColorArraySerializer());
        EnumSerializer serializer = objectMapper.findValueSerializer(Color[].class);
        Color[] enumValues = Color.values();
        JsonGenerator gen = objectMapper.getFactory().createGenerator();

        serializer.serialize(enumValues, gen, objectMapper.getSerializationConfig(), null);
        gen.flush();

        String expected = "[\"RED\",\"GREEN\",\"BLUE\"]";
        Assert.assertEquals(expected, gen.getOutputBuffer().toString());
    }
}

enum Color {
    RED,
    GREEN,
    BLUE,
    ONE
}

class ColorArraySerializer extends StdSerializer<Color[]> {
    public ColorArraySerializer() { this(null); }
    public ColorArraySerializer(Class<Color[]> t) { super(t); }

    @Override
    public void serialize(Color[] values, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        ObjectNode root = new ObjectNode(objectMapper.getFactory());
        ArrayNode array = root.putArray("colors");

        for (Color color : values) {
            array.add(new ObjectNode(objectMapper.getFactory())
                    .put("name", color.name())
                    .put("ordinal", color.ordinal()));
        }

        gen.writeTree(root);
    }
}