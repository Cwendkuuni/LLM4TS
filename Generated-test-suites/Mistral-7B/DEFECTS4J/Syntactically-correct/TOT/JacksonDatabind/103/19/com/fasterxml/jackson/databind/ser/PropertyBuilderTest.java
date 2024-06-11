package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.BeanPropertyWriterBase;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.ObjectMap;

import static org.testng.Assert.assertArrayEquals;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private ObjectMapper mapperWithCustomSerializer;
    private ObjectMapper mapperWithAnnotation;
    private ObjectMapper mapperWithDefaultValue;

    @BeforeClass
    public void beforeClassSetup() {
        ObjectMapper.setDefaultUseWrapper(false);
        mapper = new ObjectMapper();
        mapperWithCustomSerializer = new ObjectMapper();
        mapperWithCustomSerializer.registerSerializer(String[].class, new StringArraySerializer());
        mapperWithAnnotation = new ObjectMapper();
        mapperWithAnnotation.findAndRegisterModules();
        mapperWithDefaultValue = new ObjectMapper();
        mapperWithDefaultValue.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    }

    @Test
    public void testSimplePropertyWithNoAnnotations() throws IOException {
        SimpleBean simpleBean = new SimpleBean();
        simpleBean.setName("Test");

        String json = mapper.writeValueAsString(simpleBean);
        ObjectNode jsonNode = mapper.readValue(json, ObjectNode.class);

        assertEquals(jsonNode.get("name").asText(), "Test");
    }

    @Test
    public void testSerializationTypeAnnotations() throws IOException {
        AnnotatedForSerializationBean annotatedBean = new AnnotatedForSerializationBean();
        annotatedBean.setType("String");
        annotatedBean.setName("Test");

        String json = mapperWithCustomSerializer.writeValueAsString(annotatedBean);
        ObjectNode jsonNode = mapperWithCustomSerializer.readValue(json, ObjectNode.class);

        assertEquals(jsonNode.get("type").asText(), "String");
        assertEquals(jsonNode.get("name").asText(), "Test");
    }

    @Test
    public void testSerializationTypeAnnotationWithDefaultValue() throws IOException {
        AnnotatedForSerializationBeanWithDefaultValue annotatedBean = new AnnotatedForSerializationBeanWithDefaultValue();
        String json = mapperWithDefaultValue.writeValueAsString(annotatedBean);
        ObjectNode jsonNode = mapperWithDefaultValue.readValue(json, ObjectNode.class);

        // By default, "type" should not be included in the output since it has a value of "NON_DEFAULT"
        assertNull(jsonNode.get("type"));
        assertEquals(jsonNode.get("name").asText(), "Test");
    }

    @Test
    public void testSerializationTypeAnnotationWithDefaultValueAndInclude() throws IOException {
        AnnotatedForSerializationBeanWithDefaultValue annotatedBean = new AnnotatedForSerializationBeanWithDefaultValue();
        ObjectMapper mapperWithInclude = new ObjectMapper();
        mapperWithInclude.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        String json = mapperWithInclude.writeValueAsString(annotatedBean);
        ObjectNode jsonNode = mapperWithInclude.readValue(json, ObjectNode.class);

        assertEquals(jsonNode.get("type").asText(), "String");
        assertEquals(jsonNode.get("name").asText(), "Test");
    }

    @Test
    public void testCustomIncludeExcludeAnnotations() throws IOException {
        CustomIncludeExcludeBean customIncludeExcludeBean = new CustomIncludeExcludeBean();
        ObjectMapper mapperWithInclude = new ObjectMapper();
        mapperWithInclude.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapperWithInclude.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        String json = mapperWithInclude.writeValueAsString(customIncludeExcludeBean);
        ObjectNode jsonNode = mapperWithInclude.readValue(json, ObjectNode.class);

        // By default, "name" should be excluded because it has a value of "NULL"
        assertNull(jsonNode.get("name"));
        // "list" should be included because it has a value of "NON_EMPTY"
        assertNotNull(jsonNode.get("list"));
        ObjectNode listNode = jsonNode.get("list");
        assertNotNull(listNode);
        Object[] list = mapperWithInclude.readValue(listNode.traverse(), Object[].class);
        assertArrayEquals(new Object[]{1, 2, 3}, list);
        // "array" should be excluded because it has a value of "NULL"
        assertNull(jsonNode.get("array"));
    }

    @Test
    public void testUseRealPropertyDefaults() throws IOException {
        UseRealPropertyDefaultsBean useRealPropertyDefaultsBean = new UseRealPropertyDefaultsBean();
        ObjectMapper mapperWithDefaultValue = new ObjectMapper();
        mapperWithDefaultValue.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

        String json = mapperWithDefaultValue.writeValueAsString(useRealPropertyDefaultsBean);
        ObjectNode jsonNode = mapperWithDefaultValue.readValue(json, ObjectNode.class);

        // "defaultInt" should be included since it has a value of "NON_DEFAULT"
        assertEquals(jsonNode.get("defaultInt").asInt(), 3);
        // "defaultBoolean" should be excluded since it has a value of "false"
        assertNull(jsonNode.get("defaultBoolean"));
        // "defaultString" should be excluded since it has a value of "null"
        assertNull(jsonNode.get("defaultString"));
    }

    @Test
    public void testCustomNullSerializer() throws IOException {
        CustomNullSerializerBean customNullSerializerBean = new CustomNullSerializerBean();
        ObjectMapper mapperWithCustomSerializer = new ObjectMapper();

        String json = mapperWithCustomSerializer.writeValueAsString(customNullSerializerBean);
        ObjectNode jsonNode = mapperWithCustomSerializer.readValue(json, ObjectNode.class);

        // "value" should be excluded because it has a value of "null"
        assertNull(jsonNode.get("value"));
    }

    @Test
    public void testUnwrapping() throws IOException {
        UnwrappingBean unwrappingBean = new UnwrappingBean();
        unwrappingBean.setName("Test");
        unwrappingBean.setWrappedName("WrappedTest");

        String json = mapperWithAnnotation.writeValueAsString(unwrappingBean);
        ObjectNode jsonNode = mapperWithAnnotation.readValue(json, ObjectNode.class);

        assertEquals(jsonNode.get("name").asText(), "Test");
        assertEquals(jsonNode.get("@unwrapped").asText(), "WrappedTest");
    }

    static class SimpleBean {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class AnnotatedForSerializationBean {
        @JsonProperty("type")
        private String type;
        private String name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class AnnotatedForSerializationBeanWithDefaultValue {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        @JsonProperty("type")
        private String type;
        private String name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class CustomIncludeExcludeBean {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String name;
        private int defaultInt = 3;
        private boolean defaultBoolean = false;
        private String defaultString = null;
        private Object[] list = new Object[]{1, 2, 3};
        private Object[] array = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDefaultInt() {
            return defaultInt;
        }

        public boolean getDefaultBoolean() {
            return defaultBoolean;
        }

        public String getDefaultString() {
            return defaultString;
        }
    }

    static class UseRealPropertyDefaultsBean {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int defaultInt = 3;
        private boolean defaultBoolean = false;
        private String defaultString = null;

        public int getDefaultInt() {
            return defaultInt;
        }

        public boolean getDefaultBoolean() {
            return defaultBoolean;
        }

        public String getDefaultString() {
            return defaultString;
        }
    }

    static class CustomNullSerializerBean {
        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    static class StringArraySerializer extends StdSerializer<String[]> {
        public StringArraySerializer() {
            this(null);
        }

        public StringArraySerializer(Class<String[]> t) {
            this(null);
        }

        public StringArraySerializer(Class<String[]> t, BeanPropertyWriterBase w) {
            super(t, w);
        }

        @Override
        public void serializeValues(ObjectWriter ow, JsonSerializerProvider p, String[] v, Object bean) throws IOException {
            ObjectNode node = ow.createObjectNode();
            ObjectMapper mapper = (ObjectMapper) ow.getSerializerProvider();
            BeanPropertyWriter writer = mapper.findBeanPropertyWriter(bean.getClass(), getPropertyName());
            writer.serializeAsField(bean, v, ow, p, node);
            ow.writeValue(node, "value");
        }
    }

    static class UnwrappingBean {
        @JsonSerialize(using = WrapperNameTransformer.class)
        private String name;
        private String wrappedName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWrappedName() {
            return wrappedName;
        }

        public void setWrappedName(String wrappedName) {
            this.wrappedName = wrappedName;
        }
    }

    static class WrapperNameTransformer extends NameTransformerBase {
        @Override
        public String transformPropertyName(MapperConfig config, String name) {
            return "@unwrapped";
        }
    }
}