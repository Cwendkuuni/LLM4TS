import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;
    private AnnotatedClass annotatedClass;
    private BeanDescription beanDescription;

    @Before
    public void setup() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        this.objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        this.annotatedClass = Mockito.mock(AnnotatedClass.class);
        this.beanDescription = Mockito.mock(BeanDescription.class);
        Mockito.when(annotatedClass.getAnnotated(JsonIdentityInfo.class)).thenReturn(this.objectIdInfo);
        Mockito.when(beanDescription.getAnnotatedClass()).thenReturn(this.annotatedClass);
        Mockito.when(beanDescription.getBeanDescription()).thenReturn(this.beanDescription);
    }

    @Test
    public void testDefaultConstructor() {
        ObjectIdInfo defaultObjectIdInfo = new ObjectIdInfo(null, null, null, false, null);
        assertEquals("ObjectIdInfo: propName=null, scope=null, generatorType=null, alwaysAsId=false", defaultObjectIdInfo.toString());
    }

    @Test
    public void testConstructorWithNameAndScope() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false, null);
        assertEquals("ObjectIdInfo: propName=name, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=null, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
        assertEquals("ObjectIdInfo: propName=name, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGeneratorAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        boolean alwaysAsId = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, null);
        assertEquals("ObjectIdInfo: propName=name, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
    }

    @Test
    public void testConstructorWithNameAndScopeAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("name");
        Class<ObjectIdInfo> scope = ObjectIdInfo.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals("ObjectIdInfo: propName=name, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("name", objectIdInfo.getPropertyName().getShortName());
    }

    @Test
    public void testGetScope() {
        assertEquals(ObjectIdInfo.class, objectIdInfo.getScope());
    }

    @Test
    public void testGetGeneratorType() {
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
    }

    @Test
    public void testGetResolverType() {
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testGetAlwaysAsId() {
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
        assertEquals(objectIdInfo, newObjectIdInfo.getPropertyName());
        assertEquals(objectIdInfo.getScope(), newObjectIdInfo.getScope());
        assertEquals(objectIdInfo.getGeneratorType(), newObjectIdInfo.getGeneratorType());
        assertEquals(objectIdInfo.getResolverType(), newObjectIdInfo.getResolverType());
    }

    @Test
    public void testToString() {
        String expectedString = "ObjectIdInfo: propName=name, scope=class com.fasterxml.jackson.databind.introspect.ObjectIdInfo, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }

    @Test
    public void testGetAnnotatedClass() {
        assertEquals(this.annotatedClass, objectIdInfo.getAnnotatedClass());
    }

    @Test
    public void testGetBeanDescription() {
        assertEquals(this.beanDescription, objectIdInfo.getBeanDescription());
    }

    @Test
    public void testGetAnnotatedField() throws NoSuchFieldException, IllegalAccessException {
        Field field = this.beanDescription.getBeanClass().getDeclaredField("objectIdInfo");
        field.setAccessible(true);
        AnnotatedField annotatedField = Mockito.mock(AnnotatedField.class);
        Mockito.when(annotatedField.getAnnotatedClass()).thenReturn(this.annotatedClass);
        Mockito.when(annotatedField.getName()).thenReturn("name");
        Mockito.when(annotatedField.getParent()).thenReturn(this.beanDescription);
        Mockito.when(annotatedField.getValueType()).thenReturn(JavaType.constructType(String.class));
        Mockito.when(beanDescription.findAnnotation(JsonIdentityInfo.class)).thenReturn(null);
        Mockito.when(beanDescription.findFieldAnnotation(JsonIdentityInfo.class)).thenReturn(null);
        Mockito.when(beanDescription.findMethodAnnotation(JsonIdentityInfo.class, "getObjectIdInfo")).thenReturn(null);
        ObjectIdInfo returnedObjectIdInfo = (ObjectIdInfo) objectIdInfo.getAnnotated(ObjectIdInfo.class, NopAnnotationIntrospector.instance);
        assertEquals(this.objectIdInfo, returnedObjectIdInfo);
        assertEquals(this.beanDescription, returnedObjectIdInfo.getBeanDescription());
        assertEquals(this.annotatedClass, returnedObjectIdInfo.getAnnotatedClass());
        assertEquals("name", returnedObjectIdInfo.getPropertyName().getShortName());
        assertEquals(ObjectIdInfo.class, returnedObjectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, returnedObjectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, returnedObjectIdInfo.getResolverType());
        assertFalse(returnedObjectIdInfo.getAlwaysAsId());
        Mockito.verify(annotatedField).getParent();
        Mockito.verifyNoMoreInteractions(annotatedField);
    }

    @Test
    public void testGetAnnotatedMethod() throws NoSuchMethodException, IllegalAccessException {
        JavaType javaType = JavaType.constructType(List.class, String.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Mockito.when(annotatedMethod.getAnnotatedClass()).thenReturn(this.annotatedClass);
        Mockito.when(annotatedMethod.getName()).thenReturn("getList");
        Mockito.when(annotatedMethod.getParameterType(0)).thenReturn(JavaType.constructType(String.class));
        Mockito.when(annotatedMethod.getParameterTypes()).thenReturn(new JavaType[] { JavaType.constructType(String.class) });
        Mockito.when(annotatedMethod.getParameterNames()).thenReturn(new String[] {"name"});
        Mockito.when(annotatedMethod.getParent()).thenReturn(this.beanDescription);
        ObjectIdInfo returnedObjectIdInfo = (ObjectIdInfo) objectIdInfo.getAnnotated(ObjectIdInfo.class, NopAnnotationIntrospector.instance);
        Mockito.when(beanDescription.findAnnotation(JsonIdentityInfo.class)).thenReturn(null);
        Mockito.when(beanDescription.findFieldAnnotation(JsonIdentityInfo.class)).thenReturn(null);
        Mockito.when(beanDescription.findMethodAnnotation(JsonIdentityInfo.class, "getObjectIdInfo")).thenReturn(null);
        Mockito.when(beanDescription.findMethodAnnotation(ObjectIdInfo.class, "getObjectIdInfo")).thenReturn(null);
        Mockito.when(beanDescription.findMethodsAnnotatedWith(JsonIdentityInfo.class)).thenReturn(Arrays.asList(annotatedMethod));
        ObjectIdInfo methodObjectIdInfo = (ObjectIdInfo) returnedObjectIdInfo.getAnnotated(ObjectIdInfo.class, NopAnnotationIntrospector.instance);
        assertEquals(this.objectIdInfo, methodObjectIdInfo);
        assertEquals(this.beanDescription, methodObjectIdInfo.getBeanDescription());
        assertEquals(this.annotatedClass, methodObjectIdInfo.getAnnotatedClass());
        assertEquals("name", methodObjectIdInfo.getPropertyName().getShortName());
        assertEquals(ObjectIdInfo.class, methodObjectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, methodObjectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, methodObjectIdInfo.getResolverType());
        assertFalse(methodObjectIdInfo.getAlwaysAsId());
        Mockito.verify(annotatedMethod).getParent();
        Mockito.verifyNoMoreInteractions(annotatedMethod);
        Mockito.verifyNoMoreInteractions(beanDescription);
    }
}