package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test cases for {@link StdTypeResolverBuilder}.
 */
public class StdTypeResolverBuilderTest {

    public static void main(String[] args) throws Exception {
        testStdTypeResolverBuilder();
    }

    private static void testStdTypeResolverBuilder() throws JsonProcessingException, IOException {
        // Test case 1: Simple usage with class inclusion
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder()
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)
                .typeProperty("type");
        MapperFactory mapperFactory = new MapperFactory();
        ObjectMapper mapper = mapperFactory.getSerializerProvider().getObjectMapper();
        ObjectWriter writer = mapper.writerWithTypeResolver(builder.build());

        // Test serialization
        TestBean bean1 = new TestBean(1, "A");
        ObjectNode node1 = writer.writeValueAsTree(bean1);
        String json1 = node1.toString();
        assertEquals("{\"type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestBean\"}", json1);

        TestBean bean2 = mapper.readValue(json1, TestBean.class);
        assertEquals(bean1.getId(), bean2.getId());
        assertEquals(bean1.getName(), bean2.getName());

        // Test case 2: Custom type id resolver
        TestBeanWithCustomId iddedBean = new TestBeanWithCustomId(1, "B");
        ObjectNode node2 = writer.writeValueAsTree(iddedBean);
        String json2 = node2.toString();
        assertEquals("{\"type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestBeanWithCustomId\"}", json2);

        TestBeanWithCustomId iddedBean2 = mapper.readValue(json2, TestBeanWithCustomId.class);
        assertEquals(iddedBean.getId(), iddedBean2.getId());
        assertEquals(iddedBean.getName(), iddedBean2.getName());

        // Test case 3: Error handling
        StdTypeResolverBuilder errorBuilder = StdTypeResolverBuilder.noTypeInfoBuilder()
                .typeProperty("type").defaultImpl(Object.class);
        try {
            ObjectMapper mapperError = mapperFactory.getSerializerProvider().getObjectMapper();
            ObjectWriter writerError = mapperError.writerWithTypeResolver(errorBuilder.build());
            ObjectNode nodeError = writerError.writeValueAsTree(new Object());
            throw new AssertionError("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test case 4: Complex usage with multiple sub-types
        Map<String, ObjectNode> complexJson = new LinkedHashMap<>();
        complexJson.put("item1", writer.writeValueAsTree(new TestBean(2, "C")));
        complexJson.put("item2", writer.writeValueAsTree(new TestBeanWithCustomId(3, "D")));
        complexJson.put("item3", writer.writeValueAsTree(new OtherTestBean(4, "E")));

        ObjectMapper mapperComplex = mapperFactory.getSerializerProvider().getObjectMapper();
        ObjectReader complexReader = mapperComplex.readerWithTypeResolver(builder.build());
        Collection<TestBean> beans = complexReader.readValueAs(new TypeReference<Collection<TestBean>>() {});
        assertEquals(3, beans.size());

        for (TestBean bean : beans) {
            if (bean.getId() == 2) {
                assertEquals("C", bean.getName());
            } else if (bean.getId() == 3) {
                assertEquals("D", bean.getName());
            } else {
                fail("Unexpected bean found: " + bean);
            }
        }

        // Test case 5: Usage with JsonTypeInfo annotation
        TestAnnotationBean annotationBean = new TestAnnotationBean();
        ObjectNode nodeAnnotation1 = writer.writeValueAsTree(annotationBean);
        String jsonAnnotation1 = nodeAnnotation1.toString();
        assertEquals("{\"type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestAnnotationBean\"}", jsonAnnotation1);

        TestAnnotationBean annotationBean2 = mapper.readValue(jsonAnnotation1, TestAnnotationBean.class);
        assertEquals(annotationBean.getId(), annotationBean2.getId());

        // Test case 6: Usage with custom include-as
        TestBeanCustomInclude asIncludeBean = new TestBeanCustomInclude();
        ObjectNode nodeAsInclude1 = writer.writeValueAsTree(asIncludeBean);
        String jsonAsInclude1 = nodeAsInclude1.toString();
        assertEquals("{\"type\":[\"com.fasterxml.jackson.databind.jsontype.impl.TestBeanCustomInclude\"]}", jsonAsInclude1);

        TestBeanCustomInclude asIncludeBean2 = mapper.readValue(jsonAsInclude1, TestBeanCustomInclude[].class)[0];
        assertEquals(asIncludeBean.getId(), asIncludeBean2.getId());

        TestBeanCustomInclude asIncludeBean3 = mapper.readValue(jsonAsInclude1, TestBeanCustomInclude.class);
        assertEquals(asIncludeBean.getId(), asIncludeBean3.getId());

        // Test case 7: Usage with minimal class type id
        TestMinimalClassBean minimalBean = new TestMinimalClassBean();
        ObjectNode nodeMinimal1 = writer.writeValueAsTree(minimalBean);
        String jsonMinimal1 = nodeMinimal1.toString();
        assertEquals("{\"type\":\"com.fasterxml.jackson.databind.jsontype.impl.TestMinimalClassBean\"}", jsonMinimal1);

        TestMinimalClassBean minimalBean2 = mapper.readValue(jsonMinimal1, TestMinimalClassBean.class);
        assertEquals(minimalBean.getId(), minimalBean2.getId());
    }

    /**
     * Base class for test beans.
     */
    static class TestBean {
        private int id;
        private String name;

        public TestBean(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Test bean with custom id.
     */
    static class TestBeanWithCustomId extends TestBean {
        public TestBeanWithCustomId(int id, String name) {
            super(id, name);
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
        static class TestBeanWithCustomIdTypeIdResolver
                extends TypeIdResolverBase<TestBeanWithCustomId> {
            @Override
            public TypeId idFromValue(TestBeanWithCustomId value) {
                return new TypeId("com.fasterxml.jackson.databind.jsontype.impl.TestBeanWithCustomId",
                        TestBeanWithCustomId.class);
            }
        }
    }

    /**
     * Test bean using custom include-as.
     */
    static class TestBeanCustomInclude {
        @JsonDeserialize(using = TestBeanCustomDeserializer.class)
        @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type")
        @JsonTypeName("TestBeanCustomInclude")
        private int id;

        @JsonProperty("type")
        private String type;

        public int getId() {
            return id;
        }
    }

    /**
     * Custom deserializer for {@link TestBeanCustomInclude}.
     */
    static class TestBeanCustomDeserializer
            extends JsonDeserializer<TestBeanCustomInclude> {
        @Override
        public TestBeanCustomInclude deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            TestBeanCustomInclude bean = new TestBeanCustomInclude();
            bean.setId(node.getInt("id"));
            bean.setType(node.getTextValue());
            return bean;
        }
    }

    /**
     * Test bean using minimal class type id.
     */
    static class TestMinimalClassBean {
        @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "")
        private int id;

        public int getId() {
            return id;
        }
    }

    /**
     * Test bean annotated with JsonTypeInfo.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
    static class TestAnnotationBean {
        private int id;

        public TestAnnotationBean() {
            this(0);
        }

        public TestAnnotationBean(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Test bean using other class.
     */
    static class OtherTestBean {
        private int id;
        private String name;

        public OtherTestBean(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}