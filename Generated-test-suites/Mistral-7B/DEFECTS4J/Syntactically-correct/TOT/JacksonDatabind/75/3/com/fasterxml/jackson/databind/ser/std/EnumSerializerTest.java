import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testStandardEnumSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class), false);
        ObjectNode node = mapper.writeValueAsTree(MyEnum.A);
        Assert.assertEquals("\"A\"", node.get(0).asText());

        SerializerProvider serializers = mapper.getSerializerProvider();
        EnumSerializer serializerWithIndex = serializer.createContextual(null, null);
        node = mapper.writeValueAsTree(MyEnum.A, serializers, null);
        Assert.assertEquals(0, node.get(0).asInt());

        node = mapper.writeValueAsTree(MyEnum.B);
        Assert.assertEquals("\"B\"", node.get(0).asText());

        EnumSet<MyEnum> values = EnumSet.allOf(MyEnum.class);
        ObjectNode enumNode = mapper.writeValueAsTree(values, serializers, null);
        Assert.assertEquals(3, enumNode.size());
        Assert.assertEquals("\"A\"", enumNode.get(0).get("enum").get(0).asText());
        Assert.assertEquals("\"B\"", enumNode.get(0).get("enum").get(1).asText());
        Assert.assertEquals("\"C\"", enumNode.get(0).get("enum").get(2).asText());
    }

    @Test
    public void testCustomSerializationViaAnnotation() throws JsonGenerationException, JsonMappingException, IOException {
        @JsonFormat(shape = JsonFormat.Shape.INT)
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyAnnotatedEnum.class), false);
        ObjectNode node = mapper.writeValueAsTree(MyAnnotatedEnum.A);
        Assert.assertEquals(0, node.get(0).asInt());

        EnumSerializer serializerWithIndex = serializer.createContextual(null, null);
        node = mapper.writeValueAsTree(MyAnnotatedEnum.A, mapper.getSerializerProvider(), null);
        Assert.assertEquals(0, node.get(0).asInt());

        node = mapper.writeValueAsTree(MyAnnotatedEnum.B);
        Assert.assertEquals(1, node.get(0).asInt());

        EnumSet<MyAnnotatedEnum> values = EnumSet.allOf(MyAnnotatedEnum.class);
        ObjectNode enumNode = mapper.writeValueAsTree(values, mapper.getSerializerProvider(), null);
        Assert.assertEquals(3, enumNode.size());
        Assert.assertEquals(0, enumNode.get(0).get("enum").get(0).asInt());
        Assert.assertEquals(1, enumNode.get(0).get("enum").get(1).asInt());
        Assert.assertEquals(2, enumNode.get(0).get("enum").get(2).asInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testCustomSerializationViaAnnotationWithInvalidShape() throws JsonGenerationException, JsonMappingException, IOException {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(MyAnnotatedEnum.class), false);
        ObjectNode node = mapper.writeValueAsTree(MyAnnotatedEnum.A);
        Assert.assertEquals("\"A\"", node.get(0).asText());
    }

    @Test
    public void testToStringSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromValues(ToStringEnum.class), false);
        ObjectNode node = mapper.writeValueAsTree(ToStringEnum.A);
        Assert.assertEquals("\"A_TO_STRING\"", node.get(0).asText());

        SerializerProvider serializers = mapper.getSerializerProvider();
        EnumSerializer serializerWithIndex = serializer.createContextual(null, null);
        node = mapper.writeValueAsTree(ToStringEnum.A, serializers, null);
        Assert.assertEquals(0, node.get(0).asInt());

        node = mapper.writeValueAsTree(ToStringEnum.B);
        Assert.assertEquals("\"B_TO_STRING\"", node.get(0).asText());

        EnumSet<ToStringEnum> values = EnumSet.allOf(ToStringEnum.class);
        ObjectNode enumNode = mapper.writeValueAsTree(values, serializers, null);
        Assert.assertEquals(3, enumNode.size());
        Assert.assertEquals("\"A_TO_STRING\"", enumNode.get(0).get("enum").get(0).asText());
        Assert.assertEquals("\"B_TO_STRING\"", enumNode.get(0).get("enum").get(1).asText());
        Assert.assertEquals("\"C_TO_STRING\"", enumNode.get(0).get("enum").get(2).asText());
    }
}

enum MyEnum {
    A,
    B,
    C
}

enum MyAnnotatedEnum {
    @JsonFormat(shape = JsonFormat.Shape.INT)
    A,
    @JsonFormat(shape = JsonFormat.Shape.INT)
    B,
    @JsonFormat(shape = JsonFormat.Shape.INT)
    C
}

enum ToStringEnum {
    A_TO_STRING("A_TO_STRING"),
    B_TO_STRING("B_TO_STRING"),
    C_TO_STRING("C_TO_STRING")
}