import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.BeanPropertyWriter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PropertyBuilderTest extends BaseMapTest {

    private BeanSerializerFactory _factory;
    private PropertyBuilder _propertyBuilder;
    private SerializationConfig _config;
    private BeanDescription _beanDesc;

    @BeforeMethod
    public void setUp() throws Exception {
        _config = new SerializationConfig(new AnnotationIntrospector());
        _beanDesc = new BeanDescription(MyPojo.class, _config);
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        _factory = new BeanSerializerFactory(_config, _propertyBuilder);
    }

    @Test
    public void testBuildWriterBasic() throws IOException {
        ObjectWriter writer = _factory.findValueSerializer(null, MyPojo.class);
        BeanPropertyWriter bpw = _propertyBuilder.buildWriter(
                _factory.getSerializerProvider(),
                _beanDesc.findPropertyDefinition("name"),
                _beanDesc.getType(),
                writer.getValueSerializer(null, "name"),
                null,
                null,
                null,
                true);
        assertNotNull(bpw);
    }

    @Test
    public void testFindSerializationType() throws IOException {
        JavaType javaType = _propertyBuilder.findSerializationType(null, false, _beanDesc.getType());
        assertEquals(javaType, _beanDesc.getType());

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(Map.class);
        JavaType javaTypeMap = _propertyBuilder.findSerializationType(am, false, _beanDesc.getType());
        assertEquals(javaTypeMap, Map.class);

        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        when(annotation.type()).thenReturn(String.class);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotation);
        JavaType javaTypeString = _propertyBuilder.findSerializationType(am, false, _beanDesc.getType());
        assertEquals(javaTypeString, String.class);

        JsonInclude inclusionAnnotation = Mockito.mock(JsonInclude.class);
        when(inclusionAnnotation.include()).thenReturn(JsonInclude.Include.NON_EMPTY);
        when(am.getAnnotation(JsonInclude.class)).thenReturn(inclusionAnnotation);
        JavaType javaTypeEmpty = _propertyBuilder.findSerializationType(am, false, _beanDesc.getType());
        assertEquals(javaTypeEmpty, BeanPropertyWriter.MARKER_FOR_EMPTY);
    }

    @Test
    public void testGetDefaultBean() throws IOException, InstantiationException, IllegalAccessException {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean1 = _propertyBuilder.getDefaultBean();
        assertNull(defaultBean1);

        Constructor<MyPojo> constructor = MyPojo.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object defaultBean2 = _propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean2);
        assertEquals(defaultBean2, constructor.newInstance());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFindSerializationTypeWithNullType() {
        _propertyBuilder.findSerializationType(null, false, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindSerializationTypeWithIncompatibleTypes() throws IOException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getGenericType()).thenReturn(Map.class);
        _propertyBuilder.findSerializationType(am, false, String.class);
    }

    @Test
    public void testGetPropertyDefaultValue() throws IOException {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        assertEquals("", _propertyBuilder.getPropertyDefaultValue("field1", null, String.class));
        assertEquals(1, _propertyBuilder.getPropertyDefaultValue("field2", null, Integer.TYPE));
        assertEquals(0L, _propertyBuilder.getPropertyDefaultValue("field3", null, Long.TYPE));
        assertEquals(new HashMap<>(), _propertyBuilder.getPropertyDefaultValue("field4", null, Map.class));
        assertNull(_propertyBuilder.getPropertyDefaultValue("field5", null, MyPojo.class));

        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        MyPojo defaultBean1 = _propertyBuilder.getDefaultBean();
        assertEquals("", _propertyBuilder.getPropertyDefaultValue("field1", null, String.class));
        assertEquals(1, _propertyBuilder.getPropertyDefaultValue("field2", null, Integer.TYPE));
        assertEquals(0L, _propertyBuilder.getPropertyDefaultValue("field3", null, Long.TYPE));
        assertNotNull(_propertyBuilder.getPropertyDefaultValue("field4", null, MyPojo.class));
        assertEquals(defaultBean1, _propertyBuilder.getPropertyDefaultValue("field4", null, MyPojo.class));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithNullName() {
        _propertyBuilder.getPropertyDefaultValue(null, null, String.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithNullMember() {
        _propertyBuilder.getPropertyDefaultValue("name", null, String.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithInvalidType() {
        _propertyBuilder.getPropertyDefaultValue("name", null, MyPojo[].class);
    }

    @Test
    public void testGetPropertDefValueWithDefaultBean() throws IOException {
        MyPojo defaultBean = _propertyBuilder.getDefaultBean();
        assertEquals("defaultName", _propertyBuilder.getPropertyDefaultValue("name", null, MyPojo.class));
        assertEquals("defaultField1", _propertyBuilder.getPropertyDefaultValue("field1", null, MyPojo.class));
        assertEquals(1, _propertyBuilder.getPropertyDefaultValue("field2", null, Integer.TYPE));
        assertEquals(new HashMap<>(), _propertyBuilder.getPropertyDefaultValue("field3", null, Map.class));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetPropertDefValueWithInvalidName() {
        _propertyBuilder.getPropertyDefaultValue("", null, MyPojo.class);
    }

    @Test
    public void test_throwWrapped() {
        Throwable e = new RuntimeException("Test");
        Throwable wrapped = _propertyBuilder._throwWrapped("name", null, new Object());
        assertEquals(e.getClass(), wrapped.getClass());
        assertEquals(e.getMessage(), wrapped.getMessage());
        assertEquals(e.getCause(), wrapped.getCause());
    }

    @Test
    public void test_throwWrappedWithError() {
        Throwable e = new IllegalStateException("Test");
        Throwable wrapped = _propertyBuilder._throwWrapped("name", null, new Object());
        assertEquals(e.getClass(), wrapped.getClass());
        assertEquals(e.getMessage(), wrapped.getMessage());
        assertNull(wrapped.getCause());
    }

    @Test
    public void test_throwWrappedWithNull() {
        Throwable e = null;
        Throwable wrapped = _propertyBuilder._throwWrapped("name", null, new Object());
        assertNotNull(wrapped);
        assertEquals(IllegalArgumentException.class, wrapped.getClass());
        assertEquals("Failed to get property 'name' of default Object@0 object", wrapped.getMessage());
    }

    private static class MyPojo {
        private String name;
        private int field2;
        private Long field3;
        private Map<String, String> field4;

        public MyPojo() {
        }

        public MyPojo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getField2() {
            return field2;
        }

        public void setField2(int field2) {
            this.field2 = field2;
        }

        public Long getField3() {
            return field3;
        }

        public void setField3(Long field3) {
            this.field3 = field3;
        }

        public Map<String, String> getField4() {
            return field4;
        }

        public void setField4(Map<String, String> field4) {
            this.field4 = field4;
        }
    }

}