package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MappingIteratorTest {

    private static final ObjectMapper mapper;
    private static final JsonFactory jsonFactory;

    static {
        JsonFactoryBuilder factoryBuilder = new JsonFactoryBuilder();
        jsonFactory = factoryBuilder.build();
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private static final class TestData {
        int i;
        String s;
        List<TestData> list;
    }

    @BeforeClass
    public void setup() throws IOException {
        JsonParser parser = jsonFactory.createParser("[]");
        MappingIterator<TestData> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(TestData.class),
                parser,
                mapper.getDeserializationContext(),
                mapper.findValueDeserializer(TestData.class),
                true,
                null
        );
        mapper.readerForUpdating(TestData.class).readValue(parser);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyIterator() throws IOException {
        MappingIterator<TestData> iterator = MappingIterator.emptyIterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNextValue() throws IOException {
        ObjectReader reader = mapper.readerForListOf(TestData.class);
        JsonParser parser = jsonFactory.createParser("[]");
        MappingIterator<TestData> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(TestData.class),
                parser,
                mapper.getDeserializationContext(),
                mapper.findValueDeserializer(TestData.class),
                true,
                null
        );
        TestData data = reader.readValue(parser);
        iterator.nextValue();
        TestData nextData = iterator.nextValue();
        Assert.assertEquals(nextData, data);
    }

    @Test
    public void testHasNextValue() throws IOException {
        ObjectReader reader = mapper.readerForListOf(TestData.class);
        JsonParser parser = jsonFactory.createParser("[]");
        MappingIterator<TestData> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(TestData.class),
                parser,
                mapper.getDeserializationContext(),
                mapper.findValueDeserializer(TestData.class),
                true,
                null
        );
        TestData data = reader.readValue(parser);
        parser.nextToken();
        Assert.assertTrue(iterator.hasNextValue());
        TestData nextData = iterator.nextValue();
        Assert.assertEquals(nextData, data);
        Assert.assertFalse(iterator.hasNextValue());

        JsonParser parser2 = jsonFactory.createParser("[]");
        MappingIterator<TestData> iterator2 = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(TestData.class),
                parser2,
                mapper.getDeserializationContext(),
                mapper.findValueDeserializer(TestData.class),
                true,
                null
        );
        Assert.assertFalse(iterator2.hasNextValue());
    }

    @Test
    public void testHasNext() throws IOException {
        ObjectReader reader = mapper.readerForListOf(TestData.class);
        JsonParser parser = jsonFactory.createParser("[]");
        MappingIterator<TestData> iterator = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(TestData.class),
                parser,
                mapper.getDeserializationContext(),
                mapper.findValueDeserializer(TestData.class),
                true,
                null
        );
        TestData data = reader.readValue(parser);
        parser.nextToken();
        Assert.assertTrue(iterator.hasNext());
        TestData nextData = iterator.nextValue();
        Assert.assertEquals(nextData, data);
        Assert.assertFalse(iterator.hasNext());

        JsonParser parser2 = jsonFactory.createParser("[]");
        MappingIterator<TestData> iterator2 = new MappingIterator<>(
                TypeFactory.defaultInstance.constructType(TestData.class),
                parser2,
                mapper.getDeserializationContext(),
                mapper.findValueDeserializer(TestData.class),
                true,
                null
        );
        Assert.assertFalse(iterator2.hasNext());
    }

    @Test
    public void testReadAll() throws IOException {
        ObjectReader reader = mapper.readerForListOf(TestData.class);
        JsonParser parser = jsonFactory.createParser("[]");
        List<TestData> result = new ArrayList<>();
        MappingIterator<TestData> iterator =
                new MappingIterator<>(
                        TypeFactory.defaultInstance.constructType(TestData.class),
                        parser,
                        mapper.getDeserializationContext(),
                        mapper.findValueDeserializer(TestData.class),
                        true,
                        null);
        reader.readValue(parser, result);
        iterator.readAll(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), iterator.nextValue());
        Assert.assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testReadAllList() throws IOException {
        ObjectReader reader = mapper.readerForListOf(TestData.class);
        JsonParser parser = jsonFactory.createParser("[]");
        List<TestData> result = new ArrayList<>();
        MappingIterator<TestData> iterator =
                new MappingIterator<>(
                        TypeFactory.defaultInstance.constructType(TestData.class),
                        parser,
                        mapper.getDeserializationContext(),
                        mapper.findValueDeserializer(TestData.class),
                        true,
                        null);
        reader.readValue(parser, result);
        iterator.readAll(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), iterator.nextValue());
        Assert.assertEquals(result, iterator.readAll(new ArrayList<>()));
        Assert.assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testReadAllCollection() throws IOException {
        ObjectReader reader = mapper.readerForListOf(TestData.class);
        JsonParser parser = jsonFactory.createParser("[]");
        List<TestData> result = new ArrayList<>();
        MappingIterator<TestData> iterator =
                new MappingIterator<>(
                        TypeFactory.defaultInstance.constructType(TestData.class),
                        parser,
                        mapper.getDeserializationContext(),
                        mapper.findValueDeserializer(TestData.class),
                        true,
                        null);
        reader.readValue(parser, result);
        iterator.readAll(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), iterator.nextValue());
        Assert.assertEquals(result, iterator.readAll(Collections.synchronizedList(new ArrayList<>())));
        Assert.assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testReadAllMap() throws IOException {
        ObjectReader reader = mapper.readerForMapOf(String.class, TestData.class);
        JsonParser parser = jsonFactory.createParser("{}");
        Map<String, TestData> result = new HashMap<>();
        MappingIterator<Map.Entry<String, TestData>> iterator =
                new MappingIterator<>(
                        TypeFactory.defaultInstance.constructType(Map.Entry.class),
                        parser,
                        mapper.getDeserializationContext(),
                        mapper.findValueDeserializer(Map.Entry.class),
                        true,
                        null);
        reader.readValue(parser, result);
        iterator.readAll(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(iterator.nextValue().getKey()), iterator.nextValue().getValue());
        Assert.assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testReadAllJsonSchema() throws IOException {
        JsonSchemaFactory schemaFactory = new JsonSchemaFactory();
        JsonParser parser = jsonFactory.createParser("{}");
        JsonSchema schema = schemaFactory.getSchema(parser);
        List<JsonSchema> result = new ArrayList<>();
        MappingIterator<JsonSchema> iterator =
                new MappingIterator<>(
                        TypeFactory.defaultInstance.constructType(JsonSchema.class),
                        parser,
                        mapper.getDeserializationContext(),
                        mapper.findValueDeserializer(JsonSchema.class),
                        true,
                        null);
        reader.readValue(parser, result);
        iterator.readAll(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), iterator.nextValue());
        Assert.assertFalse(iterator.hasNextValue());
    }
}