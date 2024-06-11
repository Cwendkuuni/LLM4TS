package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class PropertyBuilderTest {

    private ObjectMapper mapper;
    private SerializationConfig config;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() throws IOException {
        mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
    }

    @Test
    public void testPropertyBuilder() throws Exception {
        Class<?> testClass = TestBean.class;
        BeanDescription beanDesc = Introspector.findBeanDescription(testClass, config);
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertNull(propertyBuilder._defaultBean);
        assertNull(propertyBuilder._defaultInclusion);
        assertFalse(propertyBuilder._useRealPropertyDefaults);

        JsonInclude.Value inclusion = JsonInclude.Value.builder()
                .defaultObjectInclude(JsonInclude.Include.NON_NULL)
                .build();
        JsonInclude.Value inclusionPerType = JsonInclude.Value.builder()
                .defaultObjectInclude(JsonInclude.Include.ALWAYS)
                .build();
        JsonInclude.Value defaultInclusion = JsonInclude.Value.merge(
                config.getDefaultPropertyInclusion(testClass),
                inclusionPerType);
        propertyBuilder._defaultInclusion = defaultInclusion;
        assertTrue(propertyBuilder._useRealPropertyDefaults);
    }

    @Test
    public void testBuildWriter() throws Exception {
        Class<?> testClass = TestBean.class;
        BeanDescription beanDesc = Introspector.findBeanDescription(testClass, config);
        SerializationConfig config = mapper.getSerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = MapperTestUtils.createSimpleValueType(int[].class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(
                Mockito.mock(SerializerProvider.class),
                Mockito.mock(BeanPropertyDefinition.class),
                declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Class<?> testClass = TestBean.class;
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = MapperTestUtils.createSimpleValueType(TestBean.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(declaredType, serializationType);

        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        JsonSerialize.Typing typing = Typing.STATIC;
        when(annotation.getType()).thenReturn(Integer.class);
        when(annotation.getTyping()).thenReturn(typing);
        when(am.getAnnotations()).thenReturn(new JsonSerialize[]{annotation});

        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertTrue(serializationType.isStaticTyping());
        assertEquals(Integer.class, serializationType.getRawClass());

        when(annotation.getType()).thenReturn(TestBean.class);
        when(annotation.getTyping()).thenReturn(JsonSerialize.Typing.DYNAMIC);
        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertFalse(serializationType.isStaticTyping());
        assertEquals(TestBean.class, serializationType.getRawClass());
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        Class<?> testClass = TestBean.class;
        BeanDescription beanDesc = Introspector.findBeanDescription(testClass, config);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        beanDesc = Introspector.findBeanDescription(TestBeanWithDefaultConstructor.class, config);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        Class<?> testClass = TestBean.class;
        BeanDescription beanDesc = Introspector.findBeanDescription(testClass, config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType type = MapperTestUtils.createSimpleValueType(Integer.class);
        Object defaultBean = new TestBean();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("stringProperty", am, type);
        assertNull(defaultValue);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("intProperty", am, type);
        assertEquals(0, propertyDefaultValue.intValue());

        TestBeanWithDefaultConstructor bean = new TestBeanWithDefaultConstructor();
        Object propertyValue = bean.getIntProperty();
        Object propertyDefaultValueWithBean = propertyBuilder.getPropertyDefaultValue("intProperty", am, type);
        assertEquals(propertyValue, propertyDefaultValueWithBean);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);

        Object defaultValueForPrimitive = propertyBuilder.getDefaultValue(MapperTestUtils.createSimpleValueType(int.class));
        assertEquals(0, defaultValueForPrimitive.intValue());

        Object defaultValueForWrapper = propertyBuilder.getDefaultValue(MapperTestUtils.createSimpleValueType(Integer.class));
        assertEquals(0, defaultValueForWrapper.intValue());

        Object defaultValueForString = propertyBuilder.getDefaultValue(MapperTestUtils.createSimpleValueType(String.class));
        assertEquals("", defaultValueForString);

        Object defaultValueForMap = propertyBuilder.getDefaultValue(MapperTestUtils.createSimpleValueType(Map.class));
        assertNotNull(defaultValueForMap);

        Object defaultValueForCollection = propertyBuilder.getDefaultValue(MapperTestUtils.createSimpleValueType(List.class));
        assertNotNull(defaultValueForCollection);

        Object defaultValueForArray = propertyBuilder.getDefaultValue(MapperTestUtils.createSimpleValueType(int[].class));
        assertNotNull(defaultValueForArray);

        Object defaultValueForReferenceType = propertyBuilder.getDefaultValue(MapperTestUtils.createSimpleValueType(TestReferenceType.class));
        assertNotNull(defaultValueForReferenceType);
    }
}

class TestBean {

    private int intProperty;
    private String stringProperty;

    public TestBean() {}

    public int getIntProperty() {
        return intProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public TestBean(int intProperty, String stringProperty) {
        this.intProperty = intProperty;
        this.stringProperty = stringProperty;
    }
}

class TestBeanWithDefaultConstructor {

    private int intProperty;

    public int getIntProperty() {
        return intProperty;
    }
}

class TestReferenceType {
}