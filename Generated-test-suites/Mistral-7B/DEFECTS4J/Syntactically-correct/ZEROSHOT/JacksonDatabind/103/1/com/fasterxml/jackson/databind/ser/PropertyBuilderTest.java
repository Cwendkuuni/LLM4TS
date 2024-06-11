package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Unit tests for {@link PropertyBuilder}.
 */
public class PropertyBuilderTest {

    private static final Object NO_DEFAULT_MARKER = Boolean.FALSE;

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private AnnotationIntrospector _annotationIntrospector;

    @BeforeClass
    public static void setUpClass() {
        _config = new SerializationConfig();
        _beanDesc = new AnnotatedClass(_TestBean.class);
        _annotationIntrospector = new StandardAnnotationIntrospector();
    }

    @Before
    public void setUp() {
        _config.setAnnotationIntrospector(_annotationIntrospector);
        _config.setDefaultPropertyInclusion(JsonInclude.Value.always());
    }

    @Test
    public void testDefaultConstruction() {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        assertNotNull(pb);
    }

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        AssertAnnotationsEquals("Expected class annotations",
                pb.getClassAnnotations(),
                new JsonInclude(_TestBean.class, JsonInclude.Include.NON_NULL)
        );
    }

    @Test
    public void testBuildWriterEmpty() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        BeanPropertyWriter bpw = pb.buildWriter(null, null, null, null, null, null, null, false);
        assertNull(bpw);
    }

    @Test
    public void testBuildWriterSimpleType() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMethod(_TestBean.class, "getIntValue");
        Object valueToSuppress = null;
        BeanPropertyWriter bpw = pb.buildWriter(null, null, ValueType.LONG, null, null, null, am, false);
        assertNotNull(bpw);
        assertEquals("Unexpected suppressNulls value", false, bpw.isSuppressNulls());
        assertEquals("Unexpected valueToSuppress", null, bpw.getValueToSuppress());
    }

    @Test
    public void testBuildWriterArrayType() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMethod(_TestBean.class, "getIntArrayValue");
        Object valueToSuppress = ArrayBuilders.getArrayComparator(new int[]{1, 2, 3});
        BeanPropertyWriter bpw = pb.buildWriter(null, null, ValueType.INT_ARRAY, null, null, null, am, false);
        assertNotNull(bpw);
        assertEquals("Unexpected suppressNulls value", false, bpw.isSuppressNulls());
        assertEquals("Unexpected valueToSuppress", valueToSuppress, bpw.getValueToSuppress());
    }

    @Test
    public void testBuildWriterWithContentTypeSerializer() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMethod(_TestBean.class, "getBeanValue");
        TypeSerializer contentTypeSer = new TypeSerializerBuilder(null)
                .buildValueSerializer(ValueType.INT, null);
        BeanPropertyWriter bpw = pb.buildWriter(null, null, ValueType.INT, null, null, contentTypeSer, am, false);
        assertNotNull(bpw);
        assertEquals("Unexpected suppressNulls value", false, bpw.isSuppressNulls());
        assertEquals("Unexpected valueToSuppress", null, bpw.getValueToSuppress());
    }

    @Test
    public void testFindSerializationTypeEmpty() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        assertNull(pb.findSerializationType(null, false, null));
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        AnnotatedElement annotatedElement = new AnnotatedMethod(_TestBean.class, "getIntValue");
        JsonInclude includeAnnotation = new JsonInclude(JsonInclude.Include.NON_NULL);
        annotatedElement.addAnnotation(includeAnnotation);
        JavaType declaredType = ValueType.LONG;
        JavaType serializationType = pb.findSerializationType(annotatedElement, false, declaredType);
        assertEquals("Unexpected serializationType", ValueType.LONG.getRawClass(), serializationType.getRawClass());
    }

    @Test
    public void testFindSerializationTypeWithAnnotatedType() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        AnnotatedElement annotatedElement = new AnnotatedMethod(_TestBean.class, "getBeanValue");
        JsonInclude includeAnnotation = new JsonInclude(JsonInclude.Include.NON_DEFAULT);
        annotatedElement.addAnnotation(includeAnnotation);
        JavaType declaredType = ValueType.beanType(MyBean.class);
        JavaType serializationType = pb.findSerializationType(annotatedElement, false, declaredType);
        assertEquals("Unexpected serializationType", ValueType.beanType(MyBean.class).getRawClass(), serializationType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeInvalidType() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        AnnotatedElement annotatedElement = new AnnotatedMethod(_TestBean.class, "getIntValue");
        JavaType declaredType = ValueType.INT;
        pb.findSerializationType(annotatedElement, false, declaredType);
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = pb.getDefaultBean();
        assertNotNull(defaultBean);
    }

    @Test
    public void testGetDefaultBeanNull() {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        pb.setDefaultBean(null);
        Object defaultBean = pb.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValueSimpleType() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = pb.getDefaultBean();
        Method getIntValue = _TestBean.class.getMethod("getIntValue");
        Object propertyDefaultValue = pb.getPropertyDefaultValue("getIntValue", getIntValue, ValueType.LONG);
        assertEquals("Unexpected propertyDefaultValue", 0, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueArrayType() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = pb.getDefaultBean();
        Method getIntArrayValue = _TestBean.class.getMethod("getIntArrayValue");
        Object propertyDefaultValue = pb.getPropertyDefaultValue("getIntArrayValue", getIntArrayValue, ValueType.INT_ARRAY);
        Object[] expectedDefaultValue = new Object[]{0, 0, 0};
        assertArrayEquals("Unexpected propertyDefaultValue", expectedDefaultValue, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithAnnotation() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = pb.getDefaultBean();
        Method getIntValue = _TestBean.class.getMethod("getIntValue");
        Object propertyDefaultValue = pb.getPropertyDefaultValue("getIntValue", getIntValue, ValueType.LONG);
        assertEquals("Unexpected propertyDefaultValue", 1, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithAnnotationNonDefault() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = pb.getDefaultBean();
        Method getIntValue = _TestBean.class.getMethod("getIntValue");
        Object propertyDefaultValue = pb.getPropertyDefaultValue("getIntValue", getIntValue, ValueType.LONG);
        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithAnnotationNonEmpty() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = pb.getDefaultBean();
        Method getIntArrayValue = _TestBean.class.getMethod("getIntArrayValue");
        Object propertyDefaultValue = pb.getPropertyDefaultValue("getIntArrayValue", getIntArrayValue, ValueType.INT_ARRAY);
        Object[] expectedDefaultValue = new Object[]{};
        assertArrayEquals("Unexpected propertyDefaultValue", expectedDefaultValue, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithAnnotationCustom() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = pb.getDefaultBean();
        Method getCustomValue = _TestBean.class.getMethod("getCustomValue");
        Object propertyDefaultValue = pb.getPropertyDefaultValue("getCustomValue", getCustomValue, ValueType.OBJECT);
        assertEquals("Unexpected propertyDefaultValue", "customDefault", propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValueSimpleType() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object propertyDefaultValue = pb.getDefaultValue(ValueType.LONG);
        assertEquals("Unexpected propertyDefaultValue", 0, propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValueArrayType() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object propertyDefaultValue = pb.getDefaultValue(ValueType.INT_ARRAY);
        assertArrayEquals("Unexpected propertyDefaultValue", new Object[]{0, 0, 0}, propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValueWithAnnotation() throws Exception {
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object propertyDefaultValue = pb.getDefaultValue(ValueType.LONG);
        assertEquals("Unexpected propertyDefaultValue", 1, propertyDefaultValue);
    }

    @Test
    public void testThrowWrappedException() throws Exception {
        Exception e = new IOException("Test IOException");
        PropertyBuilder pb = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = pb.getDefaultBean();
        Method getIntValue = _TestBean.class.getMethod("getIntValue");
        Object propertyDefaultValue = pb.getPropertyDefaultValue("getIntValue", getIntValue, ValueType.LONG);
        pb._throwWrapped(e, "getIntValue", defaultBean);
        fail("Expected IllegalArgumentException not thrown");
    }

    private void AssertAnnotationsEquals(String message, Annotations actual, Annotations expected) {
        if (!JsonAnnotation.class.isInstance(actual) || !JsonAnnotation.class.isInstance(expected)) {
            assertEquals(message, actual, expected);
        } else {
            JsonAnnotation actualAnnotation = (JsonAnnotation) actual;
            JsonAnnotation expectedAnnotation = (JsonAnnotation) expected;
            for (Map.Entry<String, Object> entry : actualAnnotation.getAnnotationValues().entrySet()) {
                JsonAnnotation.Standard expectedEntry = expectedAnnotation.findValue(entry.getKey());
                if (expectedEntry == null) {
                    fail(message + ": missing expected annotation: " + entry.getKey());
                }
                assertEquals(message + ": unexpected annotation value for " + entry.getKey() + ": expected=" + expectedEntry.getValue() + ", actual=" + entry.getValue(), expectedEntry.getValue(), entry.getValue());
            }
            for (Map.Entry<String, Object> entry : expectedAnnotation.getAnnotationValues().entrySet()) {
                JsonAnnotation.Standard actualEntry = actualAnnotation.findValue(entry.getKey());
                if (actualEntry == null) {
                    fail(message + ": missing actual annotation: " + entry.getKey());
                }
                assertEquals(message + ": unexpected annotation value for " + entry.getKey() + ": expected=" + entry.getValue() + ", actual=" + actualEntry.getValue(), entry.getValue(), actualEntry.getValue());
            }
        }
    }

    static class _TestBean {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public int getIntValue() {
            return 1;
        }

        public int[] getIntArrayValue() {
            return new int[]{1, 2, 3};
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public int getIntValueWithAnnotation() {
            return 1;
        }

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public int[] getIntArrayValueWithAnnotation() {
            return new int[]{};
        }

        @JsonSerialize(using = CustomSerializer.class, contentAsType = MyBean.class)
        public MyBean getCustomValue() {
            return new MyBean();
        }
    }

    static class MyBean {
        public String name = "customDefault";
    }
}