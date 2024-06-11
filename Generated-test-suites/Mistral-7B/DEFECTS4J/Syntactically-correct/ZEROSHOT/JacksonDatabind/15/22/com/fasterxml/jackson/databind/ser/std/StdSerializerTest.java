package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class StdSerializerTest {

    private static final ObjectNode EMPTY_NODE = JsonNodeFactory.instance.objectNode();
    private StdSerializer<Object> serializer;
    private ObjectNode schemaNode;
    private JsonFormatVisitorWrapper formatVisitor;

    @Before
    public void setup() {
        serializer = new StdSerializer<Object>(Object.class) {};
        schemaNode = EMPTY_NODE;
        formatVisitor = JsonMapping.StdSerializerProvider.stdSerializers().getSerializerProvider().getJsonFactory()
                .getJsonFormatVisitorWrapper(new TypeIdentifier("Object", null));
    }

    @Test
    public void testHandledType() {
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws Exception {
        // Test case for empty serialize method
        Object obj = new Object();
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(obj, generator, new JsonSerializer.DefaultSerializers.BaseSerializerProvider());
        generator.close();
        assertEquals("", writer.toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        schemaNode = serializer.getSchema(new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), null);
        assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(formatVisitor, Object.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaWithOptionalFalse() throws JsonMappingException {
        schemaNode = serializer.getSchema(new JsonSerializer.DefaultSerializers.BaseSerializerProvider(), Object.class, false);
        assertNotNull(schemaNode);
        assertFalse(schemaNode.get("required").asBoolean());
    }

    @Test
    public void testCreateSchemaNode() {
        ObjectNode expectedSchemaNode = createSchemaNode("string");
        ObjectNode actualSchemaNode = serializer.createSchemaNode("string");
        assertEquals(expectedSchemaNode, actualSchemaNode);
    }

    @Test
    public void testIsDefaultSerializer() {
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertFalse(StdSerializerTest.class.isInstance(serializer));
        assertTrue(StdSerializer.isDefaultSerializer(serializer));
    }
}

class StdDelegatingSerializer extends StdSerializer<Object> {

    private final Converter<Object, Object> converter;
    private final JsonSerializer<Object> delegateSerializer;

    public StdDelegatingSerializer(Converter<Object, Object> converter, JavaType delegateType,
            JsonSerializer<Object> existingSerializer) {
        super(Object.class);
        this.converter = converter;
        this.delegateSerializer = existingSerializer;
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        Object convertedValue = converter.convertValue(value, provider.getTypeFactory().constructType(delegateSerializer.handledType()));
        delegateSerializer.serialize(convertedValue, jgen, provider);
    }
}

class StdSerializerProvider extends JsonSerializer.DefaultSerializers.BaseSerializerProvider {
}

class StringWriter extends StringWriterBase {

    @Override
    public void write(String s, int offset, int length) throws IOException {
        super.write(s, offset, length);
    }
}

class TypeIdentifier {

    private final Class<?> rawType;
    private final Class<?>[] actualTypeArguments;

    public TypeIdentifier(Class<?> rawType, Class<?>[] actualTypeArguments) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(rawType.getName());
        if (actualTypeArguments != null) {
            builder.append("<");
            for (Class<?> typeArgument : actualTypeArguments) {
                builder.append(typeArgument.getName());
                if (typeArgument != actualTypeArguments[actualTypeArguments.length - 1]) {
                    builder.append(", ");
                }
            }
            builder.append(">");
        }
        return builder.toString();
    }
}