package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

/**
 * Unit tests for {@link PropertyBuilder}.
 */
public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() throws Exception {
        ObjectNode node;
        JsonFactory factory = new JsonFactory();
        SerializationConfig config = new SerializationConfig(factory);
        BeanDescription beanDesc = new BeanDescription(MyPojo.class, config);
        PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

        assertNull(pb._defaultBean);
        assertEquals(JsonInclude.Value.empty(), pb._defaultInclusion);
        assertFalse(pb._useRealPropertyDefaults);

        // test constructor
        SerializerProvider prov = new SerializerProvider();
        ObjectWriter writer = config.builder()
                .withSerializerProvider(prov)
                .build();
        MyPojo pojo = new MyPojo();
        MyPojo otherPojo = new MyPojo();
        ObjectWriter ow = writer.forType(MyPojo.class);

        // test getClassAnnotations()
        Annotations annotations = pb.getClassAnnotations();
        assertNotNull(annotations);
        assertEquals(1, annotations.size());
        JsonInclude annotation = annotations.get(JsonInclude.class);
        assertEquals(JsonInclude.Include.NON_DEFAULT, annotation.value());

        // test buildWriter()
        BeanPropertyWriter bpw = pb.buildWriter(prov, null, null, null, null, null, null, false);
        assertNotNull(bpw);

        // test findSerializationType()
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getRawType()).thenReturn(MyClass.class);
        MyClass myClassAnnotation = Mockito.mock(MyClass.class);
        when(am.findAnnotation(MyClass.class)).thenReturn(myClassAnnotation);
        when(myClassAnnotation.value()).thenReturn("MyClassAnnotationValue");

        MyPojoProperty pojoProperty = new MyPojoProperty();
        JavaType declaredType = TypeFactory.defaultInstance()
                .constructType(MyPojoProperty.class);
        JavaType serializationType = pb.findSerializationType(am, false, declaredType);
        assertEquals(MyClassAnnotationValue.class, serializationType.getRawClass());

        // test getDefaultBean()
        Object defaultBean = pb.getDefaultBean();
        assertNull(defaultBean);

        // test getPropertyDefaultValue()
        MyPojoProperty property = new MyPojoProperty();
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("myProperty");
        when(propDef.getAccessor()).thenReturn(am);

        Object defaultValue = pb.getPropertyDefaultValue("myProperty", am, declaredType);
        assertNull(defaultValue);

        // test getDefaultValue()
        defaultValue = pb.getDefaultValue(declaredType);
        assertEquals(MyDefaultValue.class, defaultValue.getClass());

        // test _throwWrapped()
        Exception exception = new Exception("Exception occurred");
        String propertyName = "myProperty";
        Object defaultBeanInstance = new MyPojo();
        Object thrown = pb._throwWrapped(exception, propertyName, defaultBeanInstance);
        fail("_throwWrapped() should not be called");
    }
}

class MyPojo {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    static class MyClass {
        String value;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = MyPojoSerializer.class)
    public MyPojoProperty myProperty;
}

class MyPojoProperty {
}

class MyPojoSerializer extends StdSerializer<MyPojoProperty> {
    public MyPojoSerializer() {
        this(null);
    }

    public MyPojoSerializer(Class<MyPojoProperty> t) {
        super(t);
    }

    @Override
    public void serialize(MyPojoProperty value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString("MyPojoPropertyValue");
    }
}

class MyDefaultValue {
}