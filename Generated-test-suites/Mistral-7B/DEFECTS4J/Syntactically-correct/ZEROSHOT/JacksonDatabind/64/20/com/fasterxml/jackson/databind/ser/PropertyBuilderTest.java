package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;

/**
 * PropertyBuilderTest class to test all methods in PropertyBuilder class
 */
public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        config = new SerializationConfig(new AnnotationIntrospector(), new ObjectMapper.DefaultSerializers.BaseAnnotationSerializerFinder());
        JavaClass javaClass = new JavaClass(TestBean.class);
        beanDesc = new BeanDescription(javaClass, null, null, null, null, null, null, null, null);
        annotationIntrospector = new StandardAnnotationIntrospector();
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations actualAnnotations = new PropertyBuilder(config, beanDesc).getClassAnnotations();
        Annotations expectedAnnotations = beanDesc.getClassAnnotations();
        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        JavaType declaredType = TypeFactory.uncheckedType(TestBean.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new BeanProperty("name", TestBean.class, "name"), null, null, null, null, null);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        JavaType serializationType = TypeFactory.uncheckedType(TestBean.class);
        Object valueToSuppress = propertyBuilder.getDefaultBean();

        // Test if the expected serialization type is used
        assertEquals(serializationType, actualWriter.getSerializationType());

        // Test if the expected valueToSuppress is used
        assertEquals(valueToSuppress, actualWriter.getValueToSuppress());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(TestBean.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType actualType = propertyBuilder.findSerializationType(a, true, declaredType);

        // Test if the correct type is returned
        assertEquals(declaredType, actualType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.uncheckedType(TestBean.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object actualValue = propertyBuilder.getPropertyDefaultValue("name", member, type);

        // Test if the correct default value is returned
        fail("Method getPropertyDefaultValue is deprecated since 2.8.5");
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = TypeFactory.uncheckedType(TestBean.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object actualValue = propertyBuilder.getDefaultValue(type);

        // Test if the correct default value is returned
        if (type.isContainerType() || type.isReferenceType()) {
            assertEquals(JsonInclude.Include.NON_EMPTY, actualValue);
        } else {
            assertNull(actualValue);
        }
    }
}

class TestBean {
    private String name;

    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int age;

    @JsonTypeName("testType")
    public static class InnerBean {
        private String innerName;

        public InnerBean() {
            innerName = "InnerBean";
        }
    }
}