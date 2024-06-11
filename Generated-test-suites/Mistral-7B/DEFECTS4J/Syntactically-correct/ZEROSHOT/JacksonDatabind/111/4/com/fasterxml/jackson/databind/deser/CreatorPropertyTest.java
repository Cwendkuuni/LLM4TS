package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTest;

import org.junit.Test;

public class CreatorPropertyTest extends JsonTest {

    @Test
    public void testCreatorProperty() throws Exception {
        // Create test class
        static class TestClass {
            @JsonCreator
            public TestClass(@JsonProperty("name") String name, @JsonProperty("age") int age) {
                this.name = name;
                this.age = age;
            }

            private String name;
            private int age;
        }

        ObjectMapper mapper = new ObjectMapper();

        // Test constructor property
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");

            TestClass testClass = reader.readValue(p);
            assertEquals("John", testClass.name);
            assertEquals(30, testClass.age);
        }

        // Test injectable value
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"inject\":1}");

            TestClass testClass = reader.readValue(p);
            assertEquals("John", testClass.name);
            assertNotNull(testClass.getClass().getField("age"));
        }

        // Test missing setter
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"inject\":1}");

            try {
                reader.readValue(p);
                fail("expected InvalidDefinitionException");
            } catch (InvalidDefinitionException e) {
                String expectedMessage = "No fallback setter/field defined for creator property 'name'";
                assertEquals(expectedMessage, e.getMessage());
            }
        }

        // Test injection with custom injectable value id
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"customId\":\"myId\"}");

            TestClass testClass = reader.readValue(p);
            assertEquals("John", testClass.name);
            assertNotNull(testClass.getClass().getField("age"));

            assertEquals("myId", testClass.getClass().getAnnotation(JsonCreator.class).value()[1]);
        }

        // Test injection with null value
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"inject\":null}");

            TestClass testClass = reader.readValue(p);
            assertEquals("John", testClass.name);
            assertNull(testClass.age);
        }
    }

    @Test
    public void testCreatorPropertyWithAnnotations() throws Exception {
        // Create test class
        static class TestClass {
            @JsonCreator
            @JsonPropertyOrder({"age", "name"})
            public TestClass(@JsonProperty("name") String name, @JsonProperty("age") int age) {
                this.name = name;
                this.age = age;
            }

            @JsonProperty(value = "name", index = 1)
            private String name;
            private int age;
        }

        ObjectMapper mapper = new ObjectMapper();

        // Test constructor property with @JsonPropertyOrder
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"age\":30,\"name\":\"John\"}");

            TestClass testClass = reader.readValue(p);
            assertEquals(30, testClass.age);
            assertEquals("John", testClass.name);
        }

        // Test injection with @JsonProperty(value =, index =)
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");

            TestClass testClass = reader.readValue(p);
            assertEquals("John", testClass.name);
            assertEquals(30, testClass.age);
        }
    }

    @Test
    public void testCreatorPropertyWithCustomDeserializer() throws Exception {
        // Create test class
        static class TestClass {
            @JsonCreator
            public TestClass(@JsonProperty("name") String name, @JsonProperty("age") int age) {
                this.name = name;
                this.age = age;
            }

            private String name;
            private int age;

            @JsonDeserialize(using = MyIntDeserializer.class)
            private int myIntField;
        }

        static class MyIntDeserializer extends JsonDeserializer<Integer> {
            @Override
            public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return jsonParser.getValueAsInt() * 2;
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        // Test constructor property
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":10}");

            TestClass testClass = reader.readValue(p);
            assertEquals("John", testClass.name);
            assertEquals(20, testClass.age);
            assertEquals(20, testClass.myIntField);
        }

        // Test injection
        {
            JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
            ObjectReader reader = mapper.readerFor(javaType);
            JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"inject\":5}");

            TestClass testClass = reader.readValue(p);
            assertEquals("John", testClass.name);
            assertEquals(10, testClass.age);
            assertEquals(12, testClass.myIntField);
        }
    }
}