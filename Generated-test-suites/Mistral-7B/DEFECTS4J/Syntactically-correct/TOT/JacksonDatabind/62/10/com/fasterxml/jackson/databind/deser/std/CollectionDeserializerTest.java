package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.*;

@Test
public class CollectionDeserializerTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[]");
        CollectionDeserializer deserializer = mapper.readValue(parser, CollectionDeserializer.class);
        Assert.assertNotNull(deserializer, "Deserializer should not be null");
        Assert.assertEquals(deserializer.getContentType().getRawClass(), Collection.class, "Incorrect content type");
        Assert.assertNull(deserializer.getDelegateDeserializer(), "Delegate deserializer should be null");
        Assert.assertNull(deserializer.getValueDeserializer(), "Value deserializer should be null");
        Assert.assertNull(deserializer.getValueTypeDeserializer(), "Value type deserializer should be null");
        Assert.assertNull(deserializer.getValueInstantiator(), "Value instantiation deserializer should be null");
        Assert.assertNull(deserializer.unwrapSingle(), "unwrapSingle should be null");

        Collection<Object> collection = deserializer.deserialize(mapper.getFactory().createParser("[]"), null);
        Assert.assertNotNull(collection, "Collection should not be null");
        Assert.assertEquals(collection.size(), 0, "Collection should be empty");
    }

    @Test
    public void testDeserializationWithValueDeserializer() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithView(MyView.class);

        JsonParser parser = factory.createParser(writer.writeValueAsString(new Person("John")));

        JavaType collectionType = TypeFactory.defaultInstance().uncheckedCollectionType(String.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class, null);
        deserializer = deserializer.withResolved(stringDeserializer, null, null, null);

        Collection<Object> collection = deserializer.deserialize(parser, null);
        Assert.assertNotNull(collection, "Collection should not be null");
        Assert.assertEquals(collection.size(), 1, "Collection should have one element");
        Assert.assertEquals(collection.get(0), "John", "Incorrect value");
    }

    @Test
    public void testDeserializationWithValueDeserializerAndTypeDeserializer() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithView(MyView.class);

        JsonParser parser = factory.createParser(writer.writeValueAsString(new Person("John")));

        JavaType collectionType = TypeFactory.defaultInstance().constructType(new TypeReference<List<MyType>>(){}.getType());

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class, null);
        TypeDeserializer myTypeDeserializer = new MyTypeDeserializer();
        deserializer = deserializer.withResolved(stringDeserializer, null, myTypeDeserializer, null);

        Collection<Object> collection = deserializer.deserialize(parser, null);
        Assert.assertNotNull(collection, "Collection should not be null");
        Assert.assertEquals(collection.size(), 1, "Collection should have one element");
        MyType myType = (MyType) collection.get(0);
        Assert.assertEquals(myType.getName(), "John", "Incorrect value");
    }

    @Test
    public void testDeserializationWithUnwrappedSingleValue() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithView(MyView.class);

        JsonParser parser = factory.createParser(writer.writeValueAsString(new Person("John")));

        JavaType collectionType = TypeFactory.defaultInstance().uncheckedCollectionType(Person.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, Boolean.TRUE);

        Collection<Object> collection = deserializer.deserialize(parser, null);
        Assert.assertNotNull(collection, "Collection should not be null");
        Assert.assertEquals(collection.size(), 1, "Collection should have one element");
        Assert.assertEquals(collection.get(0), new Person("John"), "Incorrect value");
    }

    @Test
    public void testDeserializationWithDelegateDeserializer() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithView(MyView.class);

        JsonParser parser = factory.createParser(writer.writeValueAsString(new Person("John")));

        JavaType collectionType = TypeFactory.defaultInstance().uncheckedCollectionType(Person.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, new PersonDeserializer(), null);

        Collection<Object> collection = deserializer.deserialize(parser, null);
        Assert.assertNotNull(collection, "Collection should not be null");
        Assert.assertEquals(collection.size(), 1, "Collection should have one element");
        Assert.assertEquals(collection.get(0), new Person("John"), "Incorrect value");
    }

    @Test
    public void testDeserializationWithValueInstantiator() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithView(MyView.class);

        JsonParser parser = factory.createParser(writer.writeValueAsString("[1,2,3]"));

        JavaType collectionType = TypeFactory.defaultInstance().uncheckedCollectionType(Integer.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, new IntValueInstantiator(), null, null);

        Collection<Object> collection = deserializer.deserialize(parser, null);
        Assert.assertNotNull(collection, "Collection should not be null");
        Assert.assertEquals(collection.size(), 3, "Collection should have three elements");
        for (Object obj : collection) {
            Assert.assertTrue(obj instanceof Integer, "Incorrect type");
            Assert.assertNotNull(((Integer) obj).intValue(), "Null value");
        }
    }

    @Test
    public void testDeserializationWithValueDeserializerAndValueInstantiator() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithView(MyView.class);

        JsonParser parser = factory.createParser(writer.writeValueAsString("[1,null,3]"));

        JavaType collectionType = TypeFactory.defaultInstance().uncheckedCollectionType(Integer.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, new IntValueInstantiator(), new IntDeserializer(), null);

        Collection<Object> collection = deserializer.deserialize(parser, null);
        Assert.assertNotNull(collection, "Collection should not be null");
        Assert.assertEquals(collection.size(), 3, "Collection should have three elements");
        for (Object obj : collection) {
            if (obj == null) {
                continue;
            }
            Assert.assertTrue(obj instanceof Integer, "Incorrect type");
            Assert.assertNotNull(((Integer) obj).intValue(), "Null value");
        }
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializationWithNonArray() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectParser parser = factory.parserForObject("{\"name\":\"John\"}");
        CollectionDeserializer deserializer = mapper.readValue(parser, CollectionDeserializer.class);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializationWithObjectIdReader() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectParser parser = factory.parserForObject("{\"id\":1, \"items\":[1,2,3]}");

        JavaType collectionType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, new IntValueInstantiator(), null, new IntObjectIdReader());

        deserializer.deserialize(parser, null);
    }

    @Test(expectedExceptions = IOException.class)
    public void testDeserializationWithUnsupportedType() throws IOException {
        JsonFactory factory = mapper.getFactory();
        ObjectParser parser = factory.parserForObject("{\"items\":[\"one\", \"two\", \"three\"]}");

        JavaType collectionType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);

        deserializer.deserialize(parser, null);
    }

    static class Person {
        private final String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class PersonDeserializer extends StdDeserializer<Person> {
        public PersonDeserializer() {
            this(null);
        }

        public PersonDeserializer(Class<Person> t) {
            super(t);
        }

        @Override
        public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String name = p.getValueAsString();
            return new Person(name);
        }
    }

    static class MyTypeDeserializer extends StdDeserializer<MyType> {
        @Override
        public MyType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String name = p.getValueAsString();
            return new MyType(name);
        }
    }

    static class MyType {
        private final String name;

        public MyType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class IntDeserializer extends StdDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            int value = p.getValueAsInt();
            return value;
        }
    }

    static class IntObjectIdReader extends JsonDeserializer.BaseJsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            int id = p.getValueAsInt();
            return id;
        }

        @Override
        public Class<Object> getInputClass() {
            return Object.class;
        }
    }

    static class IntValueInstantiator implements ValueInstantiator {
        @Override
        public Object instantiateValue(SerializationConfig config, Type type, Object value) {
            if (value == null) {
                return null;
            }
            return new Integer((Integer) value);
        }
    }

    static class IntDeserializerWithType extends StdDeserializer<Integer> {
        @Override
        public Integer deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer vtd) throws IOException {
            int value = jp.getValueAsInt();
            return value;
        }
    }
}