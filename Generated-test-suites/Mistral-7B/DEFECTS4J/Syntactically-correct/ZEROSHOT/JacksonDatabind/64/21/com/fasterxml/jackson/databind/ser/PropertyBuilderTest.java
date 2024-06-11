package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * Test for {@link PropertyBuilder} class.
 */
public class PropertyBuilderTest {

    private static final Object NO_DEFAULT_MARKER = Boolean.FALSE;

    private ObjectMapper mapper;
    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.findBeanDescription(TestPropertyBuilder.class);
        annotationIntrospector = config.getDefaultAnnotationIntrospector();
    }

    @Test
    public void testConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
        assertEquals(config, propertyBuilder._config);
        assertEquals(beanDesc, propertyBuilder._beanDesc);
        assertEquals(annotationIntrospector, propertyBuilder._annotationIntrospector);
        assertNull(propertyBuilder._defaultBean);
        assertEquals(JsonInclude.Include.NON_DEFAULT, propertyBuilder._defaultInclusion);
        assertFalse(propertyBuilder._useRealPropertyDefaults);
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        Annotations annotations = propertyBuilder("TestPropertyBuilder").getClassAnnotations();
        assertNotNull(annotations);
        assertEquals(1, annotations.size());
        assertEquals(JsonInclude.Include.NON_DEFAULT, annotations.get(0).annotationType());
    }

    @Test
    public void testBuildWriter() throws Exception {
        // Test case with no custom serializer, type serializer, or content type serializer.
        SerializerProvider prov = mapper.getSerializerProvider();
        BeanPropertyDefinition propDef = beanDesc.findPropertyDefinition("testProperty");
        JavaType declaredType = TypeFactory.defaultInstance().constructType(int[].class);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = createAnnotatedMember("testProperty");
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter bpw = propertyBuilder(propertyBuilder("TestPropertyBuilder"), prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).buildWriter();

        assertEquals(propDef, bpw.getPropertyDefinition());
        assertEquals(am, bpw.getMember());
        assertArrayEquals(getPropertyDefaultValue(propDef.getName(), am, declaredType.getComponentType()), bpw.getValueToSuppress());
        assertFalse(bpw.isSuppressNulls());
        assertNull(bpw.getNullSerializer());
        assertNull(bpw.getUnwrappingNameTransformer());

        // Test case with custom serializer.
        ser = mapper.getSerializerProvider().findSerializer(int[].class);
        bpw = propertyBuilder(propertyBuilder("TestPropertyBuilder"), prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).buildWriter();
        assertEquals(ser, bpw.getSerializer());

        // Test case with custom type serializer.
        typeSer = mapper.getTypeSerializer(int[].class);
        bpw = propertyBuilder(propertyBuilder("TestPropertyBuilder"), prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).buildWriter();
        assertEquals(typeSer, bpw.getTypeSerializer());

        // Test case with custom content type serializer.
        contentTypeSer = mapper.getTypeSerializer(int.class);
        bpw = propertyBuilder(propertyBuilder("TestPropertyBuilder"), prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).buildWriter();
        assertEquals(contentTypeSer, bpw.getContentTypeSerializer());

        // Test case with annotated member.
        am = createAnnotatedMember("testPropertyAnnotated");
        int value = 42;
        setAccessible(am.getAnnotated());
        setInt(am.getAnnotated(), "testValue", value);

        bpw = propertyBuilder(propertyBuilder("TestPropertyBuilder"), prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).buildWriter();
        assertEquals(value, bpw.getAnnotationIntrospector().findValueAnnotation(am).getValue());
    }

    @Test
    public void testFindSerializationType() throws Exception {
        JsonTypeSerializer serializer = mapper.getTypeSerializer(int[].class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(int[].class);
        Annotated member = createAnnotatedMember("testProperty");

        JavaType serializationType1 = propertyBuilder(propertyBuilder("TestPropertyBuilder")).findSerializationType(member, false, declaredType);
        assertNull(serializationType1);

        JavaType serializationType2 = propertyBuilder(propertyBuilder("TestPropertyBuilder")).findSerializationType(member, true, declaredType);
        assertEquals(declaredType, serializationType2);

        JsonSerialize.Typing typing = JsonSerialize.Typing.STATIC;
        JavaType serializationType3 = propertyBuilder(propertyBuilder("TestPropertyBuilder")).findSerializationType(member, true, declaredType);
        assertEquals(declaredType.withStaticTyping(), serializationType3);

        // Test case with custom serialization type.
        AnnotationIntrospector introspector = mapper.getDefaultAnnotationIntrospector();
        JsonSerialize annotation = introspector.findJsonSerializeAnnotation(member.getAnnotated());
        if (annotation != null) {
            typing = annotation.typing();
        }
        JavaType serializationType4 = propertyBuilder(propertyBuilder("TestPropertyBuilder")).findSerializationType(member, true, declaredType);
        assertEquals(declaredType.withStaticTyping().withTypeSerializer(serializer), serializationType4);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = propertyBuilder("TestPropertyBuilder");
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", createAnnotatedMember("testProperty"), int[].class);
        assertNull(propertyDefaultValue);

        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);

        Object propertyValue = defaultBean.getClass().getMethod("testProperty").invoke(defaultBean);
        Object propertyDefaultValue2 = propertyBuilder.getPropertyDefaultValue("testProperty", createAnnotatedMember("testProperty"), int[].class);
        assertEquals(propertyValue, propertyDefaultValue2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithInvalidDefaultBean() throws Exception {
        PropertyBuilder propertyBuilder = propertyBuilder("TestPropertyBuilderWithNoDefaultConstructor");
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", createAnnotatedMember("testProperty"), int[].class);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = propertyBuilder("TestPropertyBuilder");

        Object defaultValue1 = propertyBuilder.getDefaultValue(int.class);
        assertEquals(0, defaultValue1);

        Object defaultValue2 = propertyBuilder.getDefaultValue(int[].class);
        assertArrayEquals(new int[0], defaultValue2);

        Object defaultValue3 = propertyBuilder.getDefaultValue(String.class);
        assertEquals("", defaultValue3);

        Object defaultValue4 = propertyBuilder.getDefaultValue(TestNestedClass.class);
        assertNull(defaultValue4);
    }

    private PropertyBuilder propertyBuilder(String className) {
        return new PropertyBuilder(config, beanDesc);
    }

    private PropertyBuilder propertyBuilder(PropertyBuilder propertyBuilder, SerializerProvider prov, BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser,
            TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping) {
        return new PropertyBuilder(propertyBuilder._config, propertyBuilder._beanDesc, propertyBuilder._annotationIntrospector, prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    private AnnotatedMember createAnnotatedMember(String name) throws IllegalAccessException, InstantiationException {
        Object instance = beanDesc.getBeanDescriptionFor(TestPropertyBuilder.class).getInstance();
        Method method = TestPropertyBuilder.class.getDeclaredMethod(name);
        method.setAccessible(true);
        return new AnnotatedMethod(method, createTestPropertyBuilderInstance());
    }

    private Object createTestPropertyBuilderInstance() throws InstantiationException, IllegalAccessException {
        return beanDesc.getBeanDescriptionFor(TestPropertyBuilder.class).instantiateBean(config.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
    }

    private void setAccessible(Object obj) {
        if (obj instanceof AccessibleObject) {
            ((AccessibleObject) obj).setAccessible(true);
        }
    }

    private void setInt(Object obj, String fieldName, int value) throws IllegalAccessException {
        Method method = obj.getClass().getDeclaredMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
        method.setAccessible(true);
        method.invoke(obj, value);
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
        } else if (expected.getClass().isArray() && actual.getClass().isArray()) {
            Object[] expectedArray = (Object[]) expected;
            Object[] actualArray = (Object[]) actual;
            Assert.assertArrayEquals("Arrays do not match", expectedArray, actualArray);
        } else {
            assertEquals("Values do not match", expected, actual);
        }
    }

    private static class TestPropertyBuilder {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int testProperty;

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int[] testPropertyAnnotated;

        private static class TestNestedClass {}
    }
}