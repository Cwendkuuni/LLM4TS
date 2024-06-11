import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), null);
    }

    @Test
    public void testBuildWriter_DefaultValues() throws JsonMappingException {
        // Given
        SerializerProvider prov = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "testProperty", String.class, new AnnotatedMember(null, null));
        BeanDescription beanDesc = new BeanDescription(String.class);
        JavaType declaredType = new TypeReference<String>() {}.getType();
        JsonSerializer<String> ser = objectMapper.serializerFor(String.class);
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        // When
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);

        // Then
        assertEquals("default value", bpw.getValueSerializer());
    }

    @Test
    public void testBuildWriter_NonDefaultValues() throws JsonMappingException {
        // Given
        SerializerProvider prov = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "testProperty", String.class, new AnnotatedMember(null, null));
        BeanDescription beanDesc = new BeanDescription(String.class);
        JavaType declaredType = new TypeReference<String>() {}.getType();
        JsonSerializer<String> ser = objectMapper.serializerFor(String.class);
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        Map<String, Object> defaultValues = new HashMap<>();
        defaultValues.put("testProperty", "nonDefaultValue");

        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), beanDesc) {
            @Override
            protected Object getDefaultBean() {
                return defaultValues;
            }
        };

        // When
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);

        // Then
        assertEquals("nonDefaultValue", bpw.getValueSerializer());
    }

    @Test
    public void testBuildWriter_NullValues() throws JsonMappingException {
        // Given
        SerializerProvider prov = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "testProperty", String.class, new AnnotatedMember(null, null));
        BeanDescription beanDesc = new BeanDescription(String.class);
        JavaType declaredType = new TypeReference<String>() {}.getType();
        JsonSerializer<String> ser = objectMapper.serializerFor(String.class);
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        // When
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);

        // Then
        assertNull(bw.getValueSerializer());
    }

    @Test
    public void testBuildWriter_EmptyCollections() throws JsonMappingException {
        // Given
        SerializerProvider prov = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "testProperty", List.class, new AnnotatedMember(null, null));
        BeanDescription beanDesc = new BeanDescription(List.class);
        JavaType declaredType = new TypeReference<List<String>>() {}.getType();
        JsonSerializer<List<String>> ser = objectMapper.serializerFor(List.class);
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        // When
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);

        // Then
        assertNull(bw.getValueSerializer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriter_IncompatibleTypes() throws JsonMappingException {
        // Given
        SerializerProvider prov = objectMapper.getSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(
                "testProperty", String.class, new AnnotatedMember(null, null));
        BeanDescription beanDesc = new BeanDescription(int.class);
        JavaType declaredType = new TypeReference<String>() {}.getType();
        JsonSerializer<String> ser = objectMapper.serializerFor(String.class);
        TypeSerializer typeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        // When
        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);

        // Then
        // Exception should be thrown
    }
}