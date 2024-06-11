import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private ObjectMapper objectMapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        config = mock(SerializationConfig.class);
        beanDesc = mock(BeanDescription.class);
        objectMapper = mock(ObjectMapper.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() throws JsonProcessingException {
        when(beanDesc.getClassAnnotations()).thenReturn(new Annotations());
        Assert.assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_Default() throws JsonMappingException {
        // Given
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // When
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping);

        // Then
        Assert.assertNotNull(bpw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriter_IllegalConcreteTypeAnnotation() throws JsonMappingException {
        // Given
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);

        // When
        propertyBuilder.findSerializationType(a, false, declaredType);

        // Then
        // An exception should be thrown
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue_String() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        Object defaultBean = new Object();
        when(member.getValue(defaultBean)).thenReturn("defaultValue");

        propertyBuilder._defaultBean = defaultBean;

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals("defaultValue", propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue_String() {
        JavaType type = mock(JavaType.class);
        when(type.isReferenceType()).thenReturn(true);
        when(type.isContainerType()).thenReturn(false);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultValue_Number() {
        JavaType type = mock(JavaType.class);
        Class<?> cls = mock(Class.class);
        when(type.getRawClass()).thenReturn(cls);
        when(ClassUtil.primitiveType(cls)).thenReturn(int.class);
        when(ClassUtil.defaultValue(int.class)).thenReturn((int) 0);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(0, defaultValue);
    }
}