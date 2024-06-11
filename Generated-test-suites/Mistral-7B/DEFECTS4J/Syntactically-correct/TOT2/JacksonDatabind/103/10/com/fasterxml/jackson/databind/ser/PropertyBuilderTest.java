import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.BeanPropertyWriter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private ObjectNode objectNode;
    private SerializationConfig config;
    private BeanDescription beanDescription;
    private AnnotationIntrospector annotationIntrospector;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws Exception {
        config = mock(SerializationConfig.class);
        when(config.getDefaultPropertyInclusion(any(), any())).thenReturn(JsonInclude.Value.empty());
        beanDescription = mock(BeanDescription.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        when(annotationIntrospector.findPropertySerializer(any(), any(), any(), any())).thenReturn(mock(JsonSerializer.class));
        when(beanDescription.getClassAnnotations()).thenReturn(mock(Annotations.class));
        when(beanDescription.findPropertyDefinition(anyString())).thenReturn(mock(BeanPropertyDefinition.class));
        objectNode = mock(ObjectNode.class);
        propertyBuilder = new PropertyBuilder(config, beanDescription);
    }

    @Test
    public void testBuildWriter_SimpleProperty() throws Exception {
        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        when(beanPropertyDefinition.getName()).thenReturn("name");
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getRawType()).thenReturn(String.class);
        when(beanPropertyDefinition.findMemberSerializers(any(), any(), any())).thenReturn(mock(List.class));
        when(beanPropertyDefinition.getAccessor()).thenReturn(annotatedMember);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        when(typeSerializer.createSerializer(any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(JsonSerializer.class));
        when(beanPropertyDefinition.findViews()).thenReturn(new Class[0]);

        ObjectSerializer objectSerializer = propertyBuilder.buildWriter(mock(SerializerProvider.class), beanPropertyDefinition, new TypeReference<Object>() {}.getType(), null, typeSerializer, null, annotatedMember, true);

        assertNotNull(objectSerializer);
        verify(beanPropertyDefinition, times(1)).getAccessor();
        verify(beanPropertyDefinition, times(1)).getSerializerProvider();
        verify(beanPropertyDefinition, times(1)).findPropertySerializers(any(), any(), any());
        verify(beanPropertyDefinition, times(1)).getBeanDescription();
        verify(beanPropertyDefinition, times(1)).getClassAnnotations();
        verify(beanPropertyDefinition, times(1)).findViews();
    }

    @Test
    public void testBuildWriter_ListProperty() throws Exception {
        BeanPropertyDefinition beanPropertyDefinition = mock(BeanPropertyDefinition.class);
        when(beanPropertyDefinition.getName()).thenReturn("list");
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getRawType()).thenReturn(List.class);
        when(beanPropertyDefinition.getSerializerProvider()).thenReturn(mock(SerializerProvider.class));
        when(beanPropertyDefinition.findMemberSerializers(any(), any(), any())).thenReturn(Arrays.asList(mock(JsonSerializer.class)));
        when(beanPropertyDefinition.getAccessor()).thenReturn(annotatedMember);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        when(typeSerializer.createSerializer(any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(JsonSerializer.class));
        JavaType javaType = mock(JavaType.class);
        when(javaType.getContentType()).thenReturn(String.class);
        when(beanPropertyDefinition.findViews()).thenReturn(new Class[]{javaType});

        ObjectSerializer objectSerializer = propertyBuilder.buildWriter(mock(SerializerProvider.class), beanPropertyDefinition, javaType.getRawClass(), null, typeSerializer, null, annotatedMember, true);

        assertNotNull(objectSerializer);
        verify(beanPropertyDefinition, times(1)).getAccessor();
        verify(beanPropertyDefinition, times(1)).getSerializerProvider();
        verify(beanPropertyDefinition, times(1)).findMemberSerializers(any(), any(), any());
        verify(beanPropertyDefinition, times(1)).getBeanDescription();
        verify(beanPropertyDefinition, times(1)).getClassAnnotations();
        verify(beanPropertyDefinition, times(1)).findViews();
    }

    @Test
    public void testFindSerializationType_SimpleProperty() throws Exception {
        Annotated annotated = mock(Annotated.class);
        when(annotated.getClassInfo()).thenReturn(mock(ClassInfo.class));
        when(annotated.getClassInfo().getAnnotated()).thenReturn(String.class);
        when(annotationIntrospector.refineSerializationType(any(), any(), any())).thenReturn(String.class);

        JavaType javaType = propertyBuilder.findSerializationType(annotated, true, new TypeReference<Object>() {}.getType());

        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testFindSerializationType_AnnotatedProperty() throws Exception {
        Annotated annotated = mock(Annotated.class);
        when(annotated.getClassInfo()).thenReturn(mock(ClassInfo.class));
        when(annotated.getClassInfo().getAnnotated()).thenReturn(String[].class);
        when(annotationIntrospector.refineSerializationType(any(), any(), any())).thenReturn(String[].class);

        JavaType javaType = propertyBuilder.findSerializationType(annotated, true, new TypeReference<Object>() {}.getType());

        assertEquals(String[].class, javaType.getRawClass());
    }

    @Test
    public void testFindSerializationType_NoAnnotation() throws Exception {
        Annotated annotated = mock(Annotated.class);
        when(annotated.getClassInfo()).thenReturn(mock(ClassInfo.class));
        when(annotated.getClassInfo().getAnnotated()).thenReturn(Integer.class);

        JavaType javaType = propertyBuilder.findSerializationType(annotated, true, new TypeReference<Object>() {}.getType());

        assertNull(javaType);
    }

    @Test
    public void testGetPropertyDefaultValue_SimpleProperty() throws Exception {
        Object obj = new Object() {{
            String name = "John Doe";
        }};
        Object defaultBean = new Object() {{
            String name = "Jane Doe";
        }};

        Field field = new Field("name") {
            {
                setAccessible(true);
            }
        };

        Object value = propertyBuilder.getPropertyDefaultValue("name", field, String.class);

        assertEquals("Jane Doe", value);
    }

    @Test
    public void testGetPropertyDefaultValue_NoDefaultValue() throws Exception {
        Object defaultBean = new Object();

        Object value = propertyBuilder.getPropertyDefaultValue("name", null, String.class);

        assertNull(value);
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefaultBean() throws Exception {
        Object defaultBean = null;

        Object value = propertyBuilder.getPropertyDefaultValue("name", null, String.class);

        assertNull(value);
    }

    @Test
    public void testGetPropertyDefaultValue_CustomDefaultValue() throws Exception {
        Object defaultBean = new Object() {{
            @JsonSerialize(using = MyCustomSerializer.class)
            String name;
        }};
        MyCustomSerializer myCustomSerializer = mock(MyCustomSerializer.class);

        Object value = propertyBuilder.getPropertyDefaultValue("name", null, String.class);

        verify(myCustomSerializer, times(1)).serialize(any(Object.class), any());
        assertEquals(myCustomSerializer, propertyBuilder.getDefaultSerializer(String.class));
        assertEquals(myCustomSerializer.getValue(), value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_ThrowsException() throws Exception {
        Object defaultBean = new Object() {{
            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            int age;
        }};

        Object value = propertyBuilder.getPropertyDefaultValue("age", null, int.class);

        verify(defaultBean, never()).getValue(any());
        throw new AssertionError("Expected IllegalArgumentException to be thrown");
    }
}