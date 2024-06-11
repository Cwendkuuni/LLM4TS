import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private AnnotationIntrospector _annotationIntrospector;
    private Object _defaultBean;

    @Before
    public void setUp() {
        _config = mock(SerializationConfig.class);
        _beanDesc = mock(BeanDescription.class);
        _annotationIntrospector = mock(AnnotationIntrospector.class);

        when(_config.getDefaultPropertyInclusion(BeanPropertyWriter.Any.class, JsonInclude.Value.EMPTY)).thenReturn(JsonInclude.Value.builder().build());
        when(_config.getDefaultPropertyInclusion(String.class, JsonInclude.Value.EMPTY)).thenReturn(JsonInclude.Value.NON_DEFAULT);
    }

    @Test
    public void testBuilderConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(_config, propertyBuilder._config);
        assertEquals(_beanDesc, propertyBuilder._beanDesc);
        assertEquals(_annotationIntrospector, propertyBuilder._annotationIntrospector);
        assertNull(propertyBuilder._defaultBean);
        assertNull(propertyBuilder._defaultInclusion);
        assertFalse(propertyBuilder._useRealPropertyDefaults);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = mock(Annotations.class);
        when(_beanDesc.getClassAnnotations()).thenReturn(annotations);
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(annotations, propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriterWithDefaultSerializationType() throws JsonMappingException {
        JavaType declaredType = TypeFactory.defaultInstance();
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getDeclaredType()).thenReturn(declaredType);
        when(propDef.getName()).thenReturn("testProperty");
        TypeSerializer typeSer = mock(TypeSerializer.class);
        when(typeSer.serializeValue(_config, "value")).thenReturn("serializedValue");
        Object ser = mock(JsonSerializer.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        BeanPropertyWriter builder = propertyBuilder.buildWriter(_config.getProvider(), propDef, declaredType, ser, typeSer, null, null, true);
        assertEquals(ser, builder.getSerializer());
        assertEquals(typeSer, builder.getTypeSerializer());
        assertEquals(declaredType, builder.getDeclaredType());
        assertEquals("testProperty", builder.getName());
        assertEquals("serializedValue", builder.getValueSerializer().serializeValue(_config.getProvider(), null));
    }

    @Test
    public void testFindSerializationTypeWithDefaultAnnotations() throws JsonMappingException {
        AnnotatedMember am = mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance();
        when(am.getAnnotations()).thenReturn(new JsonSerialize.With());
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(declaredType, serializationType);
    }

    @Test
    public void testFindSerializationTypeWithJsonSerializeAnnotation() throws JsonMappingException {
        AnnotatedMember am = mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance();
        when(am.getAnnotations()).thenReturn(new JsonSerialize());
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(serializationType);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeWithInvalidAnnotations() throws JsonMappingException {
        AnnotatedMember am = mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance();
        when(am.getAnnotations()).thenReturn(new Object());
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        propertyBuilder.findSerializationType(am, true, declaredType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        when(_beanDesc.instantiateBean(_config.canOverrideAccessModifiers())).thenReturn(new TestBean("defaultValue"));
        assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue("name", null, String.class));
    }

    @Test
    public void testGetPropertyDefaultValueWithNullBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(null, propertyBuilder.getPropertyDefaultValue("name", null, String.class));
    }

    @Test
    public void testGetPropertyDefaultValueWithNonExistentProperty() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(null, propertyBuilder.getPropertyDefaultValue("nonExistentProperty", null, String.class));
    }

    @Test
    public void testGetDefaultValueForPrimitiveTypes() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(0, propertyBuilder.getDefaultValue(IntType.INSTANCE));
        assertEquals(0, propertyBuilder.getDefaultValue(IntegerType.INSTANCE));
        assertEquals(false, propertyBuilder.getDefaultValue(BooleanType.INSTANCE));
        assertEquals(0L, propertyBuilder.getDefaultValue(LongType.INSTANCE));
        assertEquals(0.0f, propertyBuilder.getDefaultValue(FloatType.INSTANCE));
        assertEquals(0.0d, propertyBuilder.getDefaultValue(DoubleType.INSTANCE));
        assertEquals(0.0f, propertyBuilder.getDefaultValue(NumberType.INSTANCE));
    }

    @Test
    public void testGetDefaultValueForStringType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals("", propertyBuilder.getDefaultValue(StringType.INSTANCE));
    }

    @Test
    public void testGetDefaultValueForContainerType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(ListType.construct(StringType.INSTANCE, null)));
    }

    @Test
    public void testGetDefaultValueForReferenceType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, propertyBuilder.getDefaultValue(ObjectType.INSTANCE));
    }

    @Test
    public void testGetDefaultValueForUnknownType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        assertEquals(null, propertyBuilder.getDefaultValue(TypeFactory.unknownType()));
    }

    private static class TestBean {
        private final String name;

        public TestBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}