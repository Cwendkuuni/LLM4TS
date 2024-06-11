import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.getSerializationConfig();
        this.beanDesc = mapper.getTypeFactory().constructType(DummyClass.class);
        this.propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    // Test for getClassAnnotations() method
    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertEquals(2, annotations.size());
    }

    // Test for buildWriter() method with default parameters
    @Test
    public void testBuildWriterDefault() throws JsonMappingException {
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertNull(writer);
    }

    // Test for buildWriter() method with custom parameters
    @Test
    public void testBuildWriterCustom() throws JsonMappingException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Add more assertions here to test the behavior of the writer
    }

    // Test for findSerializationType() method
    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);

        // Test when there is no annotation
        JavaType result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(result);

        // Test when there is an annotation
        when(a.getMember()).thenReturn(new AnnotatedMember() {
            @JsonSerialize(using = JsonStringSerializer.class)
            public Class<?> getRawType() {
                return String.class;
            }
        });
        result = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(String.class, result.getRawClass());
    }

    // Test for getDefaultBean() method
    @Test
    public void testGetDefaultBean() {
        // Test when defaultBean is not set
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        // Test when defaultBean is set
        Object defaultBean2 = new String[]{"test"};
        propertyBuilder._defaultBean = defaultBean2;
        Object result = propertyBuilder.getDefaultBean();
        assertArrayEquals(defaultBean2, result);
    }

    // Test for getPropertyDefaultValue() method
    @Test
    public void testGetPropertyDefaultValue() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Integer.class);

        Object result = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertEquals(0, result);

        type = TypeFactory.defaultInstance().constructType(String.class);
        result = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertEquals("", result);

        type = TypeFactory.defaultInstance().constructType(DummyClass.class);
        result = propertyBuilder.getPropertyDefaultValue("test", member, type);
        assertNull(result);
    }

    // Test for getDefaultValue() method
    @Test
    public void testGetDefaultValue() {
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        Object result = propertyBuilder.getDefaultValue(type);
        assertEquals("", result);

        type = TypeFactory.defaultInstance().constructType(Integer.class);
        result = propertyBuilder.getDefaultValue(type);
        assertEquals(0, result);

        type = TypeFactory.defaultInstance().constructType(DummyClass.class);
        result = propertyBuilder.getDefaultValue(type);
        assertNull(result);
    }

    // Test for _throwWrapped() method
    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String propName = "test";
        Object defaultBean = new DummyClass();
        throw new IllegalArgumentException("Failed to get property '"+propName+"' of default "+defaultBean.getClass().getName()+" instance");
    }
}

class DummyClass {

}