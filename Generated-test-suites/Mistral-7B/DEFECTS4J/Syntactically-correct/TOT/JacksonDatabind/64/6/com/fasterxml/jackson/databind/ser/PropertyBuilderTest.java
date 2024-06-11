import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig config;
    private PropertyBuilder propertyBuilder;
    private BeanDescription beanDescription;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        config = objectMapper.getSerializationConfig();
        beanDescription = objectMapper.introspect(DummyClass.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuildWriter_SimpleProperty() throws Exception {
        SerializerProvider provider = objectMapper.getSerializerProvider();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(provider, null, TypeFactory.defaultInstance.constructType(String.class), null, null, null, null, false);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testBuildWriter_ComplexType() throws Exception {
        SerializerProvider provider = objectMapper.getSerializerProvider();
        JavaType javaType = TypeFactory.defaultInstance.constructType(DummyClassWithNested.class);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(provider, null, javaType, null, null, null, null, false);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testBuildWriter_NullValuesSuppressed() throws Exception {
        SerializerProvider provider = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", null, TypeFactory.defaultInstance.constructType(String.class), null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(provider, propDef, TypeFactory.defaultInstance.constructType(String.class), null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertNull(beanPropertyWriter.getValueToSuppress());

        beanPropertyWriter = propertyBuilder.buildWriter(provider, propDef, TypeFactory.defaultInstance.constructType(String[].class), null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertArrayEquals(new Object[]{}, beanPropertyWriter.getValueToSuppress());

        beanPropertyWriter = propertyBuilder.buildWriter(provider, propDef, TypeFactory.defaultInstance.constructType(DummyClassWithEmptyList.class), null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, beanPropertyWriter.getValueToSuppress());

        beanPropertyWriter = propertyBuilder.buildWriter(provider, propDef, TypeFactory.defaultInstance.constructType(DummyClassWithNullList.class), null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertNull(beanPropertyWriter.getValueToSuppress());
    }

    @Test
    public void testBuildWriter_NullSerializationType() throws Exception {
        SerializerProvider provider = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", null, TypeFactory.defaultInstance.constructType(String.class), null, null);
        try {
            propertyBuilder.buildWriter(provider, propDef, null, null, null, null, null, false);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Property 'name' of type [null] has no serialization type", e.getMessage());
        }
    }

    @Test
    public void testBuildWriter_InvalidSerializationType() throws Exception {
        SerializerProvider provider = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", null, TypeFactory.defaultInstance.constructType(String.class), null, null);
        try {
            propertyBuilder.buildWriter(provider, propDef, TypeFactory.defaultInstance.constructType(Integer.class), null, null, null, null, false);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Failed to deserialize value of type [simple type, class int] from String 'Integer'", e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType_NoAnnotations() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance.constructType(DummyClassWithNoAnnotations.class);
        JavaType result = propertyBuilder.findSerializationType(null, false, javaType);
        assertNull(result);
    }

    @Test
    public void testFindSerializationType_CustomSerializationType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance.constructType(DummyClassWithCustomSerialization.class);
        JavaType result = propertyBuilder.findSerializationType(null, false, javaType);
        assertNotNull(result);
        assertEquals(TypeFactory.defaultInstance.constructType(String.class), result);
    }

    @Test
    public void testFindSerializationType_CustomSerializationTypeWithSuperclass() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance.constructType(DummyClassWithCustomSerializationSuperclass.class);
        JavaType result = propertyBuilder.findSerializationType(null, false, javaType);
        assertNotNull(result);
        assertEquals(TypeFactory.defaultInstance.constructType(String.class), result);
    }

    @Test
    public void testGetPropertyDefaultValue_SimpleProperty() throws Exception {
        Object defaultBean = propertyBuilder.getDefaultBean();
        String result = (String) propertyBuilder.getPropertyDefaultValue("name", null, TypeFactory.defaultInstance.constructType(String.class));
        assertNull(result);
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());

        propertyBuilder = new PropertyBuilder(config, beanDescription);
        defaultBean = propertyBuilder.getDefaultBean();
        result = (String) propertyBuilder.getPropertyDefaultValue("name", null, TypeFactory.defaultInstance.constructType(String.class));
        assertEquals("", result);
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue_ComplexType() throws Exception {
        Object defaultBean = propertyBuilder.getDefaultBean();
        DummyClassWithNested result = (DummyClassWithNested) propertyBuilder.getPropertyDefaultValue("nested", null, TypeFactory.defaultInstance.constructType(DummyClassWithNested.class));
        assertNull(result);
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());

        propertyBuilder = new PropertyBuilder(config, beanDescription);
        defaultBean = propertyBuilder.getDefaultBean();
        result = (DummyClassWithNested) propertyBuilder.getPropertyDefaultValue("nested", null, TypeFactory.defaultInstance.constructType(DummyClassWithNested.class));
        assertNotNull(result);
        assertEquals(0, result.getIntField());
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue_ListProperty() throws Exception {
        Object defaultBean = propertyBuilder.getDefaultBean();
        DummyClassWithEmptyList result = (DummyClassWithEmptyList) propertyBuilder.getPropertyDefaultValue("list", null, TypeFactory.defaultInstance.constructType(DummyClassWithEmptyList.class));
        assertNotNull(result);
        assertArrayEquals(new Object[]{}, result.getListField());
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());

        propertyBuilder = new PropertyBuilder(config, beanDescription);
        defaultBean = propertyBuilder.getDefaultBean();
        result = (DummyClassWithEmptyList) propertyBuilder.getPropertyDefaultValue("list", null, TypeFactory.defaultInstance.constructType(DummyClassWithEmptyList.class));
        assertNotNull(result);
        assertArrayEquals(new Object[]{}, result.getListField());
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());

        propertyBuilder = new PropertyBuilder(config, beanDescription);
        defaultBean = propertyBuilder.getDefaultBean();
        result = (DummyClassWithNullList) propertyBuilder.getPropertyDefaultValue("list", null, TypeFactory.defaultInstance.constructType(DummyClassWithNullList.class));
        assertNotNull(result);
        assertNull(result.getListField());
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue_ArrayProperty() throws Exception {
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object[] result = (Object[]) propertyBuilder.getPropertyDefaultValue("array", null, TypeFactory.defaultInstance.constructType(Object[].class));
        assertNotNull(result);
        assertArrayEquals(new Object[]{}, result);
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());

        propertyBuilder = new PropertyBuilder(config, beanDescription);
        defaultBean = propertyBuilder.getDefaultBean();
        result = (Object[]) propertyBuilder.getPropertyDefaultValue("array", null, TypeFactory.defaultInstance.constructType(Object[].class));
        assertNotNull(result);
        assertArrayEquals(new Object[]{}, result);
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetPropertyDefaultValue_NullBean() throws Exception {
        propertyBuilder = new PropertyBuilder(config, null);
        try {
            propertyBuilder.getPropertyDefaultValue("name", null, TypeFactory.defaultInstance.constructType(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Default bean not available", e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValue_NoDefaultConstructor() throws Exception {
        propertyBuilder = new PropertyBuilder(config, null);
        try {
            propertyBuilder.getPropertyDefaultValue("name", null, TypeFactory.defaultInstance.constructType(DummyClassWithNoDefaultConstructor.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Class com.example.DummyClassWithNoDefaultConstructor has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation", e.getMessage());
        }
    }

    public static class DummyClass {
    }

    public static class DummyClassWithNested {
        private int intField;

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }
    }

    public static class DummyClassWithEmptyList {
        private List<String> list = new ArrayList<>();

        public List<String> getListField() {
            return list;
        }

        public void setListField(List<String> listField) {
            this.list = listField;
        }
    }

    public static class DummyClassWithNullList {
        private List<String> list;

        public List<String> getListField() {
            return list;
        }

        public void setListField(List<String> listField) {
            this.list = listField;
        }
    }

    public static class DummyClassWithCustomSerialization {
        private String name;
    }

    public static class DummyClassWithCustomSerializationSuperclass extends DummyClass {
        private String name;
    }

    public static class DummyClassWithNoAnnotations {
    }
}