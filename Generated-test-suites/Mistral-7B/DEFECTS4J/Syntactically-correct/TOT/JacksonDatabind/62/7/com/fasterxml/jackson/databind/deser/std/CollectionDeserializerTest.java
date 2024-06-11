package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @JsonFormat(features = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    static class TestBean {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[]");
        TestBean bean = mapper.readValue(node.traverse(), TestBean.class);
        assertEquals(Collections.emptyList(), bean.getList());
    }

    @Test
    public void testEmptyArrayWithSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1]");
        TestBean bean = mapper.readValue(node.traverse(), TestBean.class);
        assertEquals(Collections.singletonList("1"), bean.getList());
    }

    @Test
    public void testArrayWithMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[\"apple\", \"banana\", \"orange\"]");
        TestBean bean = mapper.readValue(node.traverse(), TestBean.class);
        List<String> expected = Arrays.asList("apple", "banana", "orange");
        assertEquals(expected, bean.getList());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("invalidJson");
        TestBean bean = mapper.readValue(node.traverse(), TestBean.class);
        fail("Should have thrown JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TestBean bean = mapper.readValue(node.traverse(), TestBean.class);
        fail("Should have thrown JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testUnresolvedReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode();
        node.setPointer("/name", ObjectMapper.ValueType.OBJECT);
        node.setPointer("/name/ref", ObjectMapper.ValueType.Reference);
        node.setPointer("/ref", ObjectMapper.ValueType.OBJECT);

        TestBean bean = mapper.readValue(node.traverse(), TestBean.class);
        fail("Should have thrown JsonMappingException with message: Unresolved forward reference: /name/ref");
    }

    @Test
    public void testCollectionDeserializerWithValueTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[{\"name\": \"John\", \"age\": 30}]");
        final JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class,
                mapper.getTypeFactory().constructType(TestPerson.class));
        JsonDeserializer<Object> valueDeserializer = mapper.deserializerForClass(TestPerson.class);
        TypeDeserializer valueTypeDeserializer = mapper.deserializerForType(TestPerson.class);

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, valueDeserializer, valueTypeDeserializer, null, null);
        Collection<TestPerson> result = deserializer.deserialize(mapper.createParser(node), mapper.getDeserializationContext());

        List<TestPerson> expected = Arrays.asList(new TestPerson("John", 30));
        assertEquals(expected, result);
    }

    @Test
    public void testCollectionDeserializerWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1, 2, 3]");
        ValueInstantiator valueInstantiator = mapper.getValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = mapper.deserializerForClass(Integer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(mapper.getTypeFactory().constructCollectionType(List.class, Object.class), null, null, valueInstantiator, delegateDeserializer, null);

        Collection<Object> result = deserializer.deserialize(mapper.createParser(node), mapper.getDeserializationContext());
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, result);
    }

    static class TestPerson {
        private String name;
        private int age;

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

        public TestPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}