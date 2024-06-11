package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class PropertyBuilderTest {

    private ObjectNoIdJsonTestClass objectNoIdJsonTestClass;
    private ObjectIdJsonTestClass objectIdJsonTestClass;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper.DefaultTypingConfig defaultTypingConfig = mapper.getConfiguration().getDefaultTypingConfig();
        defaultTypingConfig.setFailOnEmptyBeans(false);

        this.objectNoIdJsonTestClass = new ObjectNoIdJsonTestClass();
        this.objectIdJsonTestClass = new ObjectIdJsonTestClass(1L);
    }

    @Test
    public void testPropertyBuilder() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig(mapper.getConfiguration());
        BeanDescription beanDesc = BeanDescription.forClass(ObjectNoIdJsonTestClass.class, ObjectMapper.getDefaultInstance());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Assert.assertEquals(JsonInclude.Include.NON_NULL, propertyBuilder._defaultInclusion.getValueInclusion());
        Assert.assertFalse(propertyBuilder._useRealPropertyDefaults);

        beanDesc = BeanDescription.forClass(ObjectIdJsonTestClass.class, ObjectMapper.getDefaultInstance());
        PropertyBuilder propertyBuilder2 = new PropertyBuilder(config, beanDesc);

        Assert.assertEquals(JsonInclude.Include.NON_NULL, propertyBuilder2._defaultInclusion.getValueInclusion());
        Assert.assertTrue(propertyBuilder2._useRealPropertyDefaults);
    }

    @Test
    public void testBuildWriter() throws JsonProcessingException {
        SerializerProvider prov = new StandardSerializerProvider();
        JavaType propType = ObjectTypeFactory.defaultInstance().constructType(Integer.class);
        BeanPropertyDefinition propDef = BeanPropertyDefinition.forName("id")
                .setType(propType)
                .setSerializer(null);

        ObjectIdJsonTestClass objectIdJsonTestClass = this.objectIdJsonTestClass;

        // Test without annotation
        PropertyBuilder propertyBuilder1 = new PropertyBuilder(mapper.getSerializationConfig(), null);
        BeanPropertyWriter writer1 = propertyBuilder1.buildWriter(prov, propDef, propType, null, null, null, null, false);
        Assert.assertNull(writer1.getNullSerializer());
        Assert.assertNull(writer1.getUnwrappingNameTransformer());

        // Test with annotation
        BeanDescription beanDesc = BeanDescription.forClass(ObjectIdJsonTestClass.class, ObjectMapper.getDefaultInstance());
        PropertyBuilder propertyBuilder2 = new PropertyBuilder(mapper.getSerializationConfig(), beanDesc);
        BeanPropertyWriter writer2 = propertyBuilder2.buildWriter(prov, propDef, propType, null, null, null, null, false);

        JsonSerializer<?> nullSerializer = propertyBuilder2._annotationIntrospector.findNullSerializer(propDef);
        Assert.assertNotNull(nullSerializer);
        Assert.assertEquals(ObjectIdJsonTestClass.class, nullSerializer.getClass());

        Method getIdMethod = ObjectIdJsonTestClass.class.getMethod("getId");
        Assert.assertEquals(getIdMethod, writer2.getUnwrappingNameTransformer().getAnnotation());
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithBadType() throws JsonProcessingException {
        SerializerProvider prov = new StandardSerializerProvider();
        JavaType propType = ObjectTypeFactory.defaultInstance().constructType(String.class);
        BeanPropertyDefinition propDef = BeanPropertyDefinition.forName("id")
                .setType(propType)
                .setSerializer(null);

        PropertyBuilder propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(), null);
        propertyBuilder.buildWriter(prov, propDef, ObjectTypeFactory.defaultInstance().constructType(Integer.class), null, null, null, null, false);
    }
}

class ObjectNoIdJsonTestClass {
}

class ObjectIdJsonTestClass {

    private final Long id;

    public ObjectIdJsonTestClass(Long id) {
        this.id = id;
    }

    @JsonSerialize(using = JsonLongSerializer.class)
    public Long getId() {
        return id;
    }
}