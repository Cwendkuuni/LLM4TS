package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private ObjectNode node;

    @BeforeClass
    public static void setupClass() {
        // Initialize ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        objectMapper = mapper;
    }

    @Before
    public void setup() throws JsonProcessingException {
        // Initialize test object
        TestObject testObject = new TestObject();
        node = (ObjectNode) objectMapper.valueToTree(testObject);
    }

    @Test
    public void testDefaultInclusion() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = config.findBeanDescription(TestObject.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JsonInclude.Value defaultInclusion = propertyBuilder.getDefaultInclusion();
        JsonInclude.Include inclusion = defaultInclusion.getValueInclusion();
        Assert.assertEquals(JsonInclude.Include.ALWAYS, inclusion);
    }

    @Test
    public void testUseRealPropertyDefaults() throws JsonMappingException, IOException {
        TestObject testObject = new TestObject("Default Value");
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = config.findBeanDescription(TestObject.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder.setDefaultBean(testObject);
        boolean useRealPropertyDefaults = propertyBuilder.isUseRealPropertyDefaults();
        Assert.assertTrue(useRealPropertyDefaults);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException, IOException {
        TestObject testObject = new TestObject("Default Value");
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = config.findBeanDescription(TestObject.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = BeanProperty.stdGetterFor("name");
        TypeSerializer typeSerializer = null;
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
                TypeFactory.defaultInstance().constructType(TestObject.class), null, typeSerializer, null, am, false);
        Assert.assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException, IOException {
        TestObject testObject = new TestObject();
        TestObjectSubClass testObjectSubClass = new TestObjectSubClass();
        TestObjectWithJsonSerialize testObjectWithJsonSerialize = new TestObjectWithJsonSerialize();

        SerializationConfig config = objectMapper.getSerializationConfig();
        AnnotatedMember am = BeanProperty.stdGetterFor("name");

        JavaType declaredType = TypeFactory.defaultInstance().constructType(TestObject.class);
        JavaType actualType = JavaType.construct(TestObjectSubClass.class, Object.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNull(serializationType);

        am = BeanProperty.stdGetterFor("name");
        am.setAnnotation(new JsonSerialize(using = TestObjectSerializer.class));
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNotNull(serializationType);
        Assert.assertEquals(TestObjectSubClass.class, serializationType.getRawClass());

        am = BeanProperty.stdGetterFor("name");
        am.setAnnotation(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        propertyBuilder = new PropertyBuilder(config, null);
        Object defaultBean = new TestObject("Default Value");
        propertyBuilder.setDefaultBean(defaultBean);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNull(serializationType);

        am = BeanProperty.stdGetterFor("name");
        am.setAnnotation(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        propertyBuilder = new PropertyBuilder(config, null);
        propertyBuilder.setDefaultBean(testObject);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNotNull(serializationType);
        Assert.assertEquals(TestObject.class, serializationType.getRawClass());

        am = BeanProperty.stdGetterFor("name");
        am.setAnnotation(new JsonSerialize(using = TestObjectSerializer.class));
        propertyBuilder = new PropertyBuilder(config, null);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNotNull(serializationType);
        Assert.assertEquals(TestObjectSubClass.class, serializationType.getRawClass());

        am = BeanProperty.stdGetterFor("name");
        am.setAnnotation(new JsonSerialize(using = TestObjectSerializer.class));
        am.setAnnotation(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        propertyBuilder = new PropertyBuilder(config, null);
        propertyBuilder.setDefaultBean(testObject);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNull(serializationType);

        am = BeanProperty.stdGetterFor("name");
        am.setAnnotation(new JsonInclude(JsonInclude.Include.NON_DEFAULT));
        propertyBuilder = new PropertyBuilder(config, null);
        propertyBuilder.setDefaultBean(testObjectWithJsonSerialize);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertNotNull(serializationType);
        Assert.assertEquals(TestObjectWithJsonSerialize.class, serializationType.getRawClass());
    }

    @Test
    public void testGetClassAnnotations() throws JsonMappingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = config.findBeanDescription(TestObject.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(annotations);
        Assert.assertEquals(0, annotations.size());
    }

    @Test
    public void testGetProperties() throws JsonMappingException, IOException {
        TestObject testObject = new TestObject();
        TestObject testObject2 = new TestObject();
        SerializationConfig config = objectMapper.getSerializationConfig();
        BeanDescription beanDesc = config.findBeanDescription(TestObject.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = BeanProperty.stdGetterFor("name");
        ObjectWriter objectWriter = config.getFactory().createObjectWriter(null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(objectMapper.getSerializerProvider(), null,
                TypeFactory.defaultInstance().constructType(TestObject.class), null, null, null, am, false);
        ObjectWriter writer = objectMapper.writerWithView(MyCustomSerializerView.class);
        writer.writeValue(System.out, testObject);
        writer.writeValue(System.out, testObject2);

        ObjectReader objectReader = config.getFactory().createObjectReader(node.traverse());
        TestObject readObject1 = objectReader.readValueAs(TestObject.class);
        TestObject readObject2 = objectReader.readValueAs(TestObject.class);
        Assert.assertNotSame(readObject1, readObject2);
        Assert.assertEquals(testObject.getName(), readObject1.getName());
        Assert.assertEquals(testObject2.getName(), readObject2.getName());
    }

    static class TestObject {
        @JsonProperty("name")
        private String name;

        public TestObject() {
            this("Default Value");
        }

        public TestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class TestObjectSubClass extends TestObject {
        @JsonProperty("subName")
        private String subName;

        public String getSubName() {
            return subName;
        }

        public void setSubName(String subName) {
            this.subName = subName;
        }
    }

    static class TestObjectWithJsonSerialize extends TestObject {
        @JsonSerialize
        private Integer integer;

        public TestObjectWithJsonSerialize() {
            this("Default Value");
        }

        public TestObjectWithJsonSerialize(String name) {
            super(name);
        }

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }
    }

    static class MyCustomSerializerView extends StandardSerializer<Object> {
        public MyCustomSerializerView() {
            this(null);
        }

        public MyCustomSerializerView(Class<?> t) {
            this(null, null);
        }

        public MyCustomSerializerView(Class<?> t, AnnotationMetadata metadata) {
            this(null);
        }

        public MyCustomSerializerView(Class<?> t, AnnotationMirror[] anns) {
            this(null);
        }

        public MyCustomSerializerView(Class<?> t, AnnotationInstance instance) {
            this(null);
        }

        public MyCustomSerializerView(Class<?> t, JsonSerializer<?> _valueSerializer) {
            this(null, null);
        }

        public MyCustomSerializerView(Class<?> t, JsonSerializer<?> _valueSerializer, JsonSerializerProvider provider) {
            super(t, _valueSerializer, provider);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value instanceof TestObject) {
                gen.writeString(((TestObject) value).getName());
            }
        }
    }
}

class HashMapTestObjectSerializer extends StdSerializer<HashMap<String, Object>> {

    public HashMapTestObjectSerializer() {
        this(null);
    }

    public HashMapTestObjectSerializer(Class<HashMap<String, Object>> t, AnnotationMetadata metadata) {
        this(null);
    }

    public HashMapTestObjectSerializer(Class<HashMap<String, Object>> t, AnnotationMirror[] anns) {
        this(null);
    }

    public HashMapTestObjectSerializer(Class<HashMap<String, Object>> t, AnnotationInstance instance) {
        this(null);
    }

    public HashMapTestObjectSerializer(Class<HashMap<String, Object>> t, JsonSerializer<?> valueSerializer) {
        this(null);
    }

    public HashMapTestObjectSerializer(Class<HashMap<String, Object>> t, JsonSerializer<?> valueSerializer, JsonSerializerProvider p) {
        super(t, valueSerializer, p);
    }

    @Override
    public void serialize(HashMap<String, Object> value, JsonGenerator gen, SerializerProvider p) throws IOException {
        gen.writeStartObject();

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String key = entry.getKey();
            Object valueToWrite = entry.getValue();
            gen.writeFieldName(key);
            this._valueSerializer.serialize(valueToWrite, gen, p);
        }

        gen.writeEndObject();
    }
}