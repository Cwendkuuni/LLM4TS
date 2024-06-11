import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private PropertyBuilder propertyBuilder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        _config = mock(SerializationConfig.class);
        _beanDesc = mock(BeanDescription.class);
        propertyBuilder = new PropertyBuilder(_config, _beanDesc);
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        when(_beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        assertEquals(new Annotations(), propertyBuilder.getClassAnnotations());
        verify(_beanDesc, times(1)).getClassAnnotations();
    }

    @Test
    public void testBuildWriter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        PropertyWriter propertyWriter = mock(PropertyWriter.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);

        when(_beanDesc.getType()).thenReturn(JavaType.valueOf(Map.class));

        propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, javaType, new StdSerializer<>(),
                null, null, null, false, null);

        verify(_beanDesc, times(1)).getType();
    }

    @Test
    public void testFindSerializationType() throws Exception {
        Annotated a = mock(Annotated.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);

        when(_annotationIntrospector.findSerializationType(_config, a, javaType)).thenReturn(javaType);

        assertEquals(javaType, propertyBuilder.findSerializationType(a, false, javaType));
        verify(_annotationIntrospector, times(1)).findSerializationType(_config, a, javaType);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String propName = "testProperty";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object defaultBean = new Object();

        when(member.getValue(defaultBean)).thenReturn("defaultValue");

        assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue(propName, member, javaType));
        verify(member, times(1)).getValue(defaultBean);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals(Collections.emptyList(), propertyBuilder.getDefaultValue(javaType));
        verify(_config, never()).getAnnotationIntrospector();
    }

    @Test
    public void testFindNullSerializer() throws Exception {
        Annotated a = mock(Annotated.class);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);

        when(_annotationIntrospector.findNullSerializer(a)).thenReturn(jsonSerializer);

        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertNotNull(propertyBuilder.buildWriter(mock(SerializerProvider.class), mock(BeanPropertyDefinition.class), javaType, jsonSerializer, null, null, null, false, null));
        verify(_annotationIntrospector, times(1)).findNullSerializer(a);
    }

    @Test
    public void testFindUnwrappingNameTransformer() throws Exception {
        Annotated a = mock(Annotated.class);
        NameTransformer nameTransformer = mock(NameTransformer.class);

        when(_annotationIntrospector.findUnwrappingNameTransformer(a)).thenReturn(nameTransformer);

        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        PropertyBuilder propertyBuilderWithNameTransformer = propertyBuilder.unwrappingWriter(nameTransformer);
        assertEquals(propertyBuilderWithNameTransformer, propertyBuilder.buildWriter(mock(SerializerProvider.class), mock(BeanPropertyDefinition.class), javaType, null, null, null, null, false, null).unwrappingWriter(nameTransformer));
        verify(_annotationIntrospector, times(1)).findUnwrappingNameTransformer(a);
    }

    @Test
    public void testThrowWrapped() throws Exception {
        Exception e = new IOException("Test IOException");
        assertEquals(e.getCause(), propertyBuilder._throwWrapped(e, "testProperty", new Object()));
        verify(propertyBuilder, times(1))._throwWrapped(e, "testProperty", new Object());
    }

    @Test
    public void testFindSerializationTyping() throws Exception {
        Annotated a = mock(Annotated.class);
        JsonSerialize.Typing jsonSerializeTyping = JsonSerialize.Typing.STATIC;

        when(_annotationIntrospector.findSerializationTyping(a)).thenReturn(jsonSerializeTyping);

        assertTrue(propertyBuilder.useStaticTyping(a));
        verify(_annotationIntrospector, times(1)).findSerializationTyping(a);
    }
}