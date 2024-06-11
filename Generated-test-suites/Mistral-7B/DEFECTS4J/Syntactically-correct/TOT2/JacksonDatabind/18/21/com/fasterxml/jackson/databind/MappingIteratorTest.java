package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.io.Serials;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;

    @Before
    public void setup() throws IOException {
        jsonFactory = new JsonFactory();
        mapper = new ObjectMapper(jsonFactory);
    }

    @Test
    public void testHasNextAndNextWithEmptyIterator() {
        MappingIterator<Integer> emptyIterator = MappingIterator.emptyIterator();
        Assert.assertFalse(emptyIterator.hasNext());
        Assert.assertNull(emptyIterator.next());
    }

    @Test
    public void testHasNextAndNextWithIterable() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonReader jsonReader = jsonFactory.createReader(new ByteArrayInputStream(Serials.asBytes("[]")));
        ObjectReader objectReader = mapper.readerFor(javaType);
        MappingIterator<String> mappingIterator = objectReader.readValues(jsonReader);

        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals("", mappingIterator.next());
        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertEquals("", mappingIterator.next());
        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextAndNextWithNullValue() throws IOException {
        ObjectReader objectReader = mapper.readerForValue(String.class);
        MappingIterator<String> mappingIterator = objectReader.readValues(new ByteArrayInputStream("null".getBytes()));

        Assert.assertTrue(mappingIterator.hasNext());
        Assert.assertNull(mappingIterator.next());
        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextAndNextWithException() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream("{\"name\":null}".getBytes()));
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ObjectReader objectReader = objectMapper.readerForValue(String.class);
            MappingIterator<String> mappingIterator = objectReader.readValues(jsonParser);
            mappingIterator.hasNext(); // Should not throw an exception here
            mappingIterator.next(); // Should throw an exception here
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextAndNextWithNegativeIndex() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream("[]".getBytes()));
        ObjectReader objectReader = mapper.readerFor(mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        MappingIterator<String> mappingIterator = objectReader.readValues(jsonParser);

        Assert.assertFalse(mappingIterator.hasNext(-1));
        mappingIterator.next();
        mappingIterator.next();
        Assert.assertFalse(mappingIterator.hasNext(-1));
    }

    @Test
    public void testCloseParser() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream("[]".getBytes()));
        ObjectReader objectReader = objectMapper.readerFor(mapper.getTypeFactory().constructCollectionType(List.class, String.class));

        MappingIterator<String> mappingIterator1 = objectReader.readValues(jsonParser);
        Assert.assertEquals("[]", mapper.writeValueAsString(mappingIterator1));

        jsonParser.close();

        MappingIterator<String> mappingIterator2 = objectReader.readValues(jsonParser);
        Assert.assertEquals("[]", mapper.writeValueAsString(mappingIterator2));
        Assert.assertFalse(mappingIterator2.hasNext());
    }

    @Test
    public void testCloseParserWithManagedParser() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream("[]".getBytes()));
        ObjectReader objectReader = objectMapper.readerFor(mapper.getTypeFactory().constructCollectionType(List.class, String.class));

        MappingIterator<String> mappingIterator1 = objectReader.readValues(jsonParser);
        Assert.assertEquals("[]", mapper.writeValueAsString(mappingIterator1));
        jsonParser.close();

        MappingIterator<String> mappingIterator2 = objectReader.readValues(jsonParser);
        Assert.assertEquals("[]", mapper.writeValueAsString(mappingIterator2));
        Assert.assertFalse(mappingIterator2.hasNext());

        jsonParser = jsonFactory.createParser(new ByteArrayInputStream("[]".getBytes()));
        MappingIterator<String> mappingIterator3 = objectReader.readValues(jsonParser);
        Assert.assertEquals("[]", mapper.writeValueAsString(mappingIterator3));
        Assert.assertTrue(mappingIterator3.hasNext());
        Assert.assertEquals("", mappingIterator3.next());
    }

    @Test
    public void testReadAll() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream("[\n" +
                "  {\"name\":\"John\", \"age\":30,\"city\":\"New York\"},\n" +
                "  {\"name\":\"Jane\", \"age\":28,\"city\":\"London\"},\n" +
                "  {\"name\":\"Doe\", \"age\":22,\"city\":\"Paris\"}\n" +
                "]"));
        ObjectReader objectReader = objectMapper.readerFor(mapper.getTypeFactory().constructCollectionType(List.class, Object.class));

        List<Object> resultList = Arrays.asList(new Object[0]);
        List<Object> resultListDeserialized = objectReader.readAll(resultList);

        Assert.assertEquals(3, resultListDeserialized.size());
        Assert.assertTrue(resultListDeserialized.get(0) instanceof MapperTestBean);
        Assert.assertTrue(resultListDeserialized.get(1) instanceof MapperTestBean);
        Assert.assertTrue(resultListDeserialized.get(2) instanceof MapperTestBean);

        MapperTestBean mapperTestBean1 = (MapperTestBean) resultListDeserialized.get(0);
        Assert.assertEquals("John", mapperTestBean1.getName());
        Assert.assertEquals(30, mapperTestBean1.getAge());
        Assert.assertEquals("New York", mapperTestBean1.getCity());

        MapperTestBean mapperTestBean2 = (MapperTestBean) resultListDeserialized.get(1);
        Assert.assertEquals("Jane", mapperTestBean2.getName());
        Assert.assertEquals(28, mapperTestBean2.getAge());
        Assert.assertEquals("London", mapperTestBean2.getCity());

        MapperTestBean mapperTestBean3 = (MapperTestBean) resultListDeserialized.get(2);
        Assert.assertEquals("Doe", mapperTestBean3.getName());
        Assert.assertEquals(22, mapperTestBean3.getAge());
        Assert.assertEquals("Paris", mapperTestBean3.getCity());
    }

    @Test
    public void testReadAllWithEmptyIterator() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream("[]".getBytes()));
        ObjectReader objectReader = objectMapper.readerFor(mapper.getTypeFactory().constructCollectionType(List.class, Object.class));

        List<Object> resultList = Collections.emptyList();
        List<Object> resultListDeserialized = objectReader.readAll(resultList);

        Assert.assertEquals(0, resultListDeserialized.size());
        Assert.assertFalse(resultListDeserialized.isEmpty());
    }

    @Test
    public void testReadAllWithNullList() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream("[]".getBytes()));
        ObjectReader objectReader = objectMapper.readerFor(mapper.getTypeFactory().constructCollectionType(List.class, Object.class));

        List<Object> resultList = null;
        List<Object> resultListDeserialized = objectReader.readAll(resultList);

        Assert.assertNull(resultListDeserialized);
    }

    @Test
    public void testReadAllWithNullValue() throws IOException {
        ObjectReader objectReader = mapper.readerForValue(String.class);
        List<String> resultList = Arrays.asList(new String[0]);
        List<String> resultListDeserialized = objectReader.readAll(resultList);

        Assert.assertEquals(0, resultListDeserialized.size());
        Assert.assertNull(resultListDeserialized.get(0));
        Assert.assertArrayEquals(new String[0], resultList.toArray());
    }

    static class MapperTestBean {
        private String name;
        private int age;
        private String city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}