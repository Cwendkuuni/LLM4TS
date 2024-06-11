import com.fasterxml.jackson.core.SerializationConfig;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.BeanPropertyWriterFactory;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PropertyBuilderTest {

    private BeanSerializerFactory _beanSerializerFactory;
    private SerializationConfig _serializationConfig;
    private AnnotationIntrospector _annotationIntrospector;

    @Before
    public void setup() {
        _annotationIntrospector = mock(AnnotationIntrospector.class);
        _serializationConfig = new SerializationConfig();
        _beanSerializerFactory = new BeanSerializerFactory(_serializationConfig, null);
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        AnnotatedElement element = mock(AnnotatedElement.class);
        Object classAnnotations = _beanSerializerFactory.getClassAnnotations(element);
        assertEquals("Expected class annotations", classAnnotations.getClass(), Annotations.class);
    }

    @Test
    public void testBuildWriter_NullPropertyDefinition() throws Exception {
        BeanPropertyDefinition propertyDefinition = null;
        // Call the method under test
        BeanPropertyWriter beanPropertyWriter = _beanSerializerFactory.buildWriter(
                new DefaultSerializerProvider(), propertyDefinition, null, null, null, null, null, false);
        // Verify the result
        assertEquals("Expected null", null, beanPropertyWriter);
    }

    @Test
    public void testBuildWriter_ValidArguments() throws Exception {
        BeanPropertyDefinition propertyDefinition = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // Call the method under test
        BeanPropertyWriter beanPropertyWriter = _beanSerializerFactory.buildWriter(
                new DefaultSerializerProvider(), propertyDefinition, declaredType, ser,
                typeSer, contentTypeSer, am, defaultUseStaticTyping);
        // Verify the result
        assertEquals("Expected non-null", beanPropertyWriter, beanPropertyWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_ExceptionThrown() throws Exception {
        BeanPropertyDefinition propertyDefinition = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        doThrow(new JsonMappingException("Test Exception")).when(am).getValue(anyObject());

        // Call the method under test
        _beanSerializerFactory.buildWriter(
                new DefaultSerializerProvider(), propertyDefinition, declaredType, ser,
                typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    @Test
    public void testFindSerializationType_NoAnnotations() throws Exception {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);

        // Call the method under test and verify the result
        JavaType result = _beanSerializerFactory.findSerializationType(a, false, declaredType);
        assertEquals("Expected null", null, result);
    }

    @Test
    public void testFindSerializationType_SuperTypeAnnotations() throws Exception {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        JavaType secondary = mock(JavaType.class);

        when(a.getType()).thenReturn(secondary);
        when(secondary.getRawClass()).thenReturn(declaredType.getRawClass().getSuperclass());

        // Call the method under test and verify the result
        JavaType result = _beanSerializerFactory.findSerializationType(a, false, declaredType);
        assertEquals("Expected super-type", declaredType, result);
    }

    @Test
    public void testFindSerializationType_SameTypeAnnotations() throws Exception {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        JavaType secondary = mock(JavaType.class);

        when(a.getType()).thenReturn(secondary);
        when(secondary.getRawClass()).thenReturn(declaredType.getRawClass());

        // Call the method under test and verify the result
        JavaType result = _beanSerializerFactory.findSerializationType(a, false, declaredType);
        assertEquals("Expected same type", declaredType, result);
    }

    @Test
    public void testFindSerializationType_SubTypeAnnotations() throws Exception {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        JavaType secondary = mock(JavaType.class);

        when(a.getType()).thenReturn(secondary);
        when(secondary.getRawClass()).thenReturn(declaredType.getRawClass().getSuperclass());

        // Call the method under test and verify the result
        JavaType result = _beanSerializerFactory.findSerializationType(a, false, declaredType);
        assertEquals("Expected null", null, result);
    }

    @Test
    public void testGetDefaultBean() throws Exception {
        BeanDescription beanDesc = mock(BeanDescription.class);

        // Call the method under test and verify the result
        Object result = _beanSerializerFactory.getDefaultBean();
        assertEquals("Expected null", null, result);
    }

    @Test
    public void testGetPropertyDefaultValue_StringProperty() throws Exception {
        BeanDescription beanDesc = mock(BeanDescription.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = new String("Test String");

        when(beanDesc.getClassInfo()).thenReturn(beanDesc);
        when(beanDesc.instantiateBean(true)).thenReturn(defaultBean);
        when(member.getValue(defaultBean)).thenReturn("Expected Value");

        // Call the method under test and verify the result
        Object result = _beanSerializerFactory.getPropertyDefaultValue("test", member, type);
        assertEquals("Expected 'Expected Value'", "Expected Value", result);
    }

    @Test
    public void testGetPropertyDefaultValue_NullProperty() throws Exception {
        BeanDescription beanDesc = mock(BeanDescription.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        when(beanDesc.getClassInfo()).thenReturn(beanDesc);
        when(beanDesc.instantiateBean(true)).thenReturn(null);

        // Call the method under test and verify the result
        Object result = _beanSerializerFactory.getPropertyDefaultValue("test", member, type);
        assertEquals("Expected null", null, result);
    }

    @Test
    public void testGetPropertyDefaultValue_NonStringProperty() throws Exception {
        BeanDescription beanDesc = mock(BeanDescription.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = new Integer(1);

        when(beanDesc.getClassInfo()).thenReturn(beanDesc);
        when(beanDesc.instantiateBean(true)).thenReturn(defaultBean);
        when(member.getValue(defaultBean)).thenReturn(1);

        // Call the method under test and verify the result
        Object result = _beanSerializerFactory.getPropertyDefaultValue("test", member, type);
        assertEquals("Expected 1", 1, result);
    }

    @Test
    public void testGetPropertyDefaultValue_EmptyCollection() throws Exception {
        BeanDescription beanDesc = mock(BeanDescription.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = mock(Object.class);

        when(beanDesc.getClassInfo()).thenReturn(beanDesc);
        when(beanDesc.instantiateBean(true)).thenReturn(defaultBean);
        when(member.getValue(defaultBean)).thenReturn(BeanPropertyWriter.MARKER_FOR_EMPTY);

        // Call the method under test and verify the result
        Object result = _beanSerializerFactory.getPropertyDefaultValue("test", member, type);
        assertEquals("Expected BeanPropertyWriter.MARKER_FOR_EMPTY", BeanPropertyWriter.MARKER_FOR_EMPTY, result);
    }

    @Test
    public void testGetPropertyDefaultValue_EmptyMap() throws Exception {
        BeanDescription beanDesc = mock(BeanDescription.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = mock(Object.class);

        when(beanDesc.getClassInfo()).thenReturn(beanDesc);
        when(beanDesc.instantiateBean(true)).thenReturn(defaultBean);
        when(member.getValue(defaultBean)).thenReturn(BeanUtil.emptyMap());

        // Call the method under test and verify the result
        Object result = _beanSerializerFactory.getPropertyDefaultValue("test", member, type);
        assertEquals("Expected BeanUtil.emptyMap()", BeanUtil.emptyMap(), result);
    }

    @Test
    public void testGetPropertyDefaultValue_ClassWithoutDefaultConstructor() throws Exception {
        BeanDescription beanDesc = mock(BeanDescription.class);

        // Call the method under test and verify the result
        Object result = _beanSerializerFactory.getPropertyDefaultValue("test", null, null);
        assertEquals("Expected null", null, result);
    }

    @Test
    public void testGetPropertyDefaultValue_ClassWithDefaultConstructor() throws Exception {
        BeanDescription beanDesc = mock(BeanDescription.class);
        Object defaultBean = new Object();

        when(beanDesc.getClassInfo()).thenReturn(beanDesc);
        when(beanDesc.instantiateBean(true)).thenReturn(defaultBean);

        // Call the method under test and verify the result
        Object result = _beanSerializerFactory.getPropertyDefaultValue("test", null, null);
        assertEquals("Expected defaultBean", defaultBean, result);
    }
}