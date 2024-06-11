import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PropertyBuilderTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private BeanDescription beanDesc;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private TypeSerializer contentTypeSerializer;

    @Mock
    private JsonSerializer<Object> jsonSerializer;

    @Mock
    private AnnotatedMember am;

    @InjectMocks
    private PropertyBuilder propertyBuilder;

    private BeanPropertyDefinition propDef;

    @Before
    public void setUp() throws Exception {
        when(config.getDefaultPropertyInclusion(Mockito.any(Class.class), Mockito.eq(JsonInclude.Value.empty())))
                .thenReturn(JsonInclude.Value.include(JsonInclude.Include.NON_NULL));
        when(config.getDefaultPropertyInclusion(Mockito.any(JavaType.class), Mockito.eq(JsonInclude.Value.empty())))
                .thenReturn(JsonInclude.Value.include(JsonInclude.Include.NON_NULL));
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        when(beanDesc.getBeanClass()).thenReturn(Object.class);
        when(beanDesc.getClassInfo()).thenReturn(new BeanPropertyDefinition.BeanPropertyInfo(Object.class, "testProperty"));
        when(beanDesc.findPropertyInclusion(Mockito.any(JsonInclude.Value.class))).thenReturn(JsonInclude.Value.empty());
        when(beanDesc.findPropertyAnnotation(Mockito.any(JsonSerialize.class))).thenReturn(null);
        when(beanDesc.findPropertySerializationType(Mockito.any(JavaType.class), Mockito.eq(defaultUseStaticTyping -> false)))
                .thenReturn(null);
        when(beanDesc.getType()).thenReturn(new TypeReference<Object>() {}.getType());
        when(annotationIntrospector.findNullSerializer(Mockito.any(AnnotatedMember.class)))
                .thenReturn(null);
        when(annotationIntrospector.findUnwrappingNameTransformer(Mockito.any(AnnotatedMember.class)))
                .thenReturn(null);
        when(config.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
        when(config.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)).thenReturn(false);
        when(config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)).thenReturn(false);

        propDef = new BeanPropertyDefinition(new BeanPropertyInfo(Object.class, "testProperty"), new AnnotatedClass(Object.class), null);
    }

    @Test
    public void testBuildWriter_NullValue() throws Exception {
        Object valueToSuppress = null;
        when(typeSerializer.serializeValue(any(), any(), any(), any()))
                .thenReturn(null);
        when(beanBuilder -> buildWriter(any(), any(), eq(Object.class), any(), eq(typeSerializer), eq(contentTypeSerializer), eq(am), anyBoolean()))
                .thenReturn(null);

        BeanSerializerFactory beanSerializerFactory = mock(BeanSerializerFactory.class);
        BeanSerializerModifier beanSerializerModifier = mock(BeanSerializerModifier.class);
        DefaultSerializerProvider serializerProvider = new DefaultSerializerProvider();
        SerializerProvider provider = new DefaultSerializerProvider();

        Object result = propertyBuilder.buildWriter(provider, propDef, Object.class, jsonSerializer, typeSerializer, contentTypeSerializer, am, false);

        assertNotNull(result);
        verify(beanSerializerFactory, times(1)).createBeanSerializer(propDef, jsonSerializer, beanSerializerModifier, typeSerializer, contentTypeSerializer, serializerProvider);
        verifyNoMoreInteractions(beanSerializerFactory);
    }

    @Test
    public void testBuildWriter_DefaultValue() throws Exception {
        Object valueToSuppress = getDefaultValue(Object.class);
        when(typeSerializer.serializeValue(any(), any(), any(), any()))
                .thenReturn(valueToSuppress);

        BeanSerializerFactory beanSerializerFactory = mock(BeanSerializerFactory.class);
        BeanSerializerModifier beanSerializerModifier = mock(BeanSerializerModifier.class);
        DefaultSerializerProvider serializerProvider = new DefaultSerializerProvider();
        SerializerProvider provider = new DefaultSerializerProvider();

        Object result = propertyBuilder.buildWriter(provider, propDef, Object.class, jsonSerializer, typeSerializer, contentTypeSerializer, am, false);

        assertNotNull(result);
        verify(beanSerializerFactory, times(1)).createBeanSerializer(propDef, jsonSerializer, beanSerializerModifier, typeSerializer, contentTypeSerializer, serializerProvider);
        verifyNoMoreInteractions(beanSerializerFactory);
    }

    @Test
    public void testBuildWriter_SerializerDefinedValue() throws Exception {
        Object valueToSuppress = "SerializerDefinedValue";
        when(jsonSerializer.serializeValues())
                .thenReturn(valueToSuppress);

        BeanSerializerFactory beanSerializerFactory = mock(BeanSerializerFactory.class);
        BeanSerializerModifier beanSerializerModifier = mock(BeanSerializerModifier.class);
        DefaultSerializerProvider serializerProvider = new DefaultSerializerProvider();
        SerializerProvider provider = new DefaultSerializerProvider();

        Object result = propertyBuilder.buildWriter(provider, propDef, Object.class, jsonSerializer, typeSerializer, contentTypeSerializer, am, false);

        assertNotNull(result);
        verify(beanSerializerFactory, times(1)).createBeanSerializer(propDef, jsonSerializer, beanSerializerModifier, typeSerializer, contentTypeSerializer, serializerProvider);
        verifyNoMoreInteractions(beanSerializerFactory);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_InvalidValue() throws Exception {
        when(typeSerializer.serializeValue(any(), any(), any(), any()))
                .thenThrow(new JsonMappingException("Invalid Value"));

        BeanSerializerFactory beanSerializerFactory = mock(BeanSerializerFactory.class);
        BeanSerializerModifier beanSerializerModifier = mock(BeanSerializerModifier.class);
        DefaultSerializerProvider serializerProvider = new DefaultSerializerProvider();
        SerializerProvider provider = new DefaultSerializerProvider();

        propertyBuilder.buildWriter(provider, propDef, Object.class, jsonSerializer, typeSerializer, contentTypeSerializer, am, false);

        fail("Expected JsonMappingException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_InvalidValue() throws Exception {
        when(annotationIntrospector.refineSerializationType(config, am, Object.class))
                .thenThrow(new IllegalArgumentException("Invalid Value"));

        JavaType result = propertyBuilder.findSerializationType(am, false, Object.class);

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFindSerializationType_NullValue() throws Exception {
        JavaType result = propertyBuilder.findSerializationType(am, false, Object.class);

        assertEquals(Object.class, result.getRawClass());
    }

    @Test
    public void testGetPropertyDefaultValue_NullValue() throws Exception {
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", am, Object.class);

        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue_DefaultValue() throws Exception {
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", am, new TypeReference<List<Integer>>() {}.getType());

        assertEquals("[],[]", result.toString());
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefaultBean() throws Exception {
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", am, Object.class);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_InvalidBean() throws Exception {
        when(am.getValue(anyObject()))
                .thenThrow(new IllegalArgumentException("Invalid Bean Value"));

        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", am, Object.class);

        fail("Expected IllegalArgumentException");
    }

    private Object getDefaultValue(Type type) throws IOException {
        ObjectWriter ow = new ObjectMapper().writerForType(type);
        return ow.writeValueAsString(ow.readValue(ow.writeValueAsString(""), type));
    }
}