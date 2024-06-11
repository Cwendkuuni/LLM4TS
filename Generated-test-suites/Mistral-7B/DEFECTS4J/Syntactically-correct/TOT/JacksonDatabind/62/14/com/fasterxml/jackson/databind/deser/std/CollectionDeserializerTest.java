package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getDeserializationContext(), result);
        Assert.assertTrue("Expected empty collection", result.isEmpty());
    }

    @Test
    public void testDeserializeSingleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[42]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getDeserializationContext(), result);
        Assert.assertEquals("Expected single element in collection", 1, result.size());
        Assert.assertEquals("Expected value to be 42", 42, result.iterator().next());
    }

    @Test
    public void testDeserializeArrayWithNullElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[null, 42, null]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getDeserializationContext(), result);
        Assert.assertEquals("Expected size 3", 3, result.size());
        Assert.assertTrue("Expected one null element", result.contains(null));
        Assert.assertEquals("Expected second element to be 42", 42, result.get(1));
    }

    @Test
    public void testDeserializeArrayWithPolymorphicElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1, \"two\", 3.5]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getDeserializationContext(), result);
        Assert.assertEquals("Expected size 3", 3, result.size());
        Assert.assertTrue("Expected first element to be Number", result.get(0) instanceof Number);
        Assert.assertTrue("Expected second element to be String", result.get(1) instanceof String);
        Assert.assertTrue("Expected third element to be Number", result.get(2) instanceof Number);
    }

    @Test
    public void testDeserializeArrayWithUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Setup a forward reference
        JsonFactory factory = mapper.getFactory();
        JsonGenerator generator = factory.createGenerator(new ByteArrayOutputStream(), null);
        generator.writeStartArray();
        generator.writeValue(1);
        generator.writeStartArray();
        generator.writeValue("second");
        generator.writeEndArray();
        generator.writeEndArray();
        generator.close();
        BytesReference bytesReference = new BytesReference(generator.getOutputBuffer().toByteArray());

        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        deserializer.deserialize(factory.createParser(bytesReference), mapper.getDeserializationContext(), result);

        // Resolve forward reference
        ObjectMapper mapper2 = new ObjectMapper();
        CollectionDeserializer deserializer2 = mapper2.findValueDeserializer(Collection.class, null);
        Collection<Object> resolvedResult = deserializer2.createContextual(mapper2.getDeserializationContext(), null);
        deserializer2.deserialize(factory.createParser(bytesReference), mapper2.getDeserializationContext(), resolvedResult);

        // Verify that the forward reference was resolved correctly
        Assert.assertEquals("Expected size 2", 2, result.size());
        Assert.assertEquals("Expected the first element to be the same as the resolved first element", result.get(0), resolvedResult.get(0));
        Assert.assertEquals("Expected the second element to be the same as the resolved second element", result.get(1), resolvedResult.get(1));
    }

    @Test
    public void testDeserializeArrayWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[{\"foo\":1}, {\"foo\":2}]");
        MapperFeature.DEFAULT_USE_ANNOTATIONS = false;
        ObjectReader reader = mapper.readerForMap();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        deserializer.deserialize(reader.readValue(node), mapper.getDeserializationContext(), result);
        Assert.assertEquals("Expected size 2", 2, result.size());
        Assert.assertTrue(result.get(0) instanceof Map);
        Assert.assertTrue(result.get(1) instanceof Map);
        for (Object obj : result) {
            Assert.assertNotNull(obj);
            Map map = (Map) obj;
            Assert.assertEquals("Expected key to be 'foo'", "foo", map.keySet().iterator().next());
            Assert.assertEquals("Expected value to be Number", 1, map.get("foo"));
        }
    }

    @Test
    public void testDeserializeArrayWithCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"apple\", \"banana\"]");
        CollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        Collection<Object> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getDeserializationContext(), result);
        Assert.assertEquals("Expected size 2", 2, result.size());
        for (Object obj : result) {
            Assert.assertEquals("Expected value to be String", "apple", obj);
        }
    }

    @Test
    public void testDeserializeArrayWithCustomValueDeserializerWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"apple\", \"banana\"]");
        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionDeserializer deserializer = mapper.findValueDeserializer(typeFactory.constructCollectionType(ArrayList.class), null);
        Collection<String> result = deserializer.createContextual(mapper.getDeserializationContext(), null);
        deserializer.deserialize(mapper.getFactory().createParser(node), mapper.getDeserializationContext(), result);
        Assert.assertEquals("Expected size 2", 2, result.size());
        for (Object obj : result) {
            Assert.assertEquals("Expected value to be String", "apple", obj);
        }
    }
}