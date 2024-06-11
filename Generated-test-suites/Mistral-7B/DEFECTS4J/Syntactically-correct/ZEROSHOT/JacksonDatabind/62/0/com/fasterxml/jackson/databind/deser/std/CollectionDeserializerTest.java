package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test class for {@link CollectionDeserializer}
 */
public class CollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private CollectionDeserializer collectionDeserializer;
    private List<Integer> list;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        collectionDeserializer = objectMapper.getTypeFactory().constructValueType(List.class, Integer.class);
        list = new ArrayList<>();
    }

    /**
     * Test deserialization of empty Collection.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testEmptyCollectionDeserialization() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ ]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationConfig());
        Assert.assertEquals(Collections.emptyList(), result);
    }

    /**
     * Test deserialization of Collection with single element.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testSingleElementCollectionDeserialization() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ 1 ]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationConfig());
        Assert.assertEquals(Arrays.asList(1), result);
    }

    /**
     * Test deserialization of Collection with multiple elements.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testMultipleElementsCollectionDeserialization() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ 1, 2, 3 ]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationConfig());
        Assert.assertEquals(Arrays.asList(1, 2, 3), result);
    }

    /**
     * Test deserialization of Collection with null value.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testNullValueCollectionDeserialization() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[ null ]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationConfig());
        Assert.assertEquals(Collections.singleton(null), result);
    }

    /**
     * Test deserialization of Collection with empty String value.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testEmptyStringValueCollectionDeserialization() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"\"]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationConfig());
        Assert.assertEquals(Collections.emptyList(), result);
    }

    /**
     * Test deserialization of Collection with non-array value.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test(expected = JsonMappingException.class)
    public void testNonArrayValueCollectionDeserialization() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"1\"");
        collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationConfig(), list);
    }

    /**
     * Test deserialization of Collection with polymorphic elements.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testPolymorphicElementsCollectionDeserialization() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2.5]");
        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationConfig());
        Assert.assertEquals(Arrays.asList(1, 2.5), result);
    }

    /**
     * Test deserialization of Collection with circular references.
     * @throws JsonProcessingException if deserialization fails
     */
    @Test
    public void testCircularReferenceCollectionDeserialization() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        JsonFactory factory = objectMapper.getFactory();
        JsonGenerator jsonGenerator = factory.createGenerator(new ByteArrayOutputStream());
        ObjectIdReader idReader = objectMapper.getPolymorphicObjectIdReader();

        Object id1 = idReader.readValue(jsonParser, Object.class);
        Object id2 = idReader.readValue(jsonParser, Object.class);

        jsonGenerator.writeStartArray();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeValue(id1);
        jsonGenerator.writeFieldName("next");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeValue(id2);
        jsonGenerator.writeEnd();
        jsonGenerator.writeEnd();
        jsonGenerator.flush();

        Collection<Object> result = collectionDeserializer.deserialize(factory.createParser(jsonGenerator.getBufferedStream()), objectMapper.getDeserializationConfig());
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
    }
}