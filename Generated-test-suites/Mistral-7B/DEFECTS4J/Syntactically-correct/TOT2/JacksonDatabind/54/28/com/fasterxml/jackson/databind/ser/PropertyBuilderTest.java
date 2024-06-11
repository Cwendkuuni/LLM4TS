import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.SerializationFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        config = mapper.getSerializationConfig();
        beanDesc = mapper.findBeanDescription(ObjectNode.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(annotations);
        Assert.assertEquals(1, annotations.size());
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        JavaType type = ObjectTypeFactory.defaultInstance().getRawClassType(String.class);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, type, null, null, null, null, false);
        Assert.assertNotNull(writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithInvalidContentTypeSerializer() throws JsonMappingException {
        JavaType type = ObjectTypeFactory.defaultInstance().getRawClassType(String.class);
        TypeSerializer contentTypeSer = null;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, type, null, null, contentTypeSer, null, false);
    }

    @Test
    public void testFindSerializationType() {
        AnnotatedMember am = new AnnotatedMember(null, new Field("field", Integer.class, this.getClass()), null);
        JavaType declaredType = ObjectTypeFactory.defaultInstance().getRawClassType(Integer.class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertEquals(Integer.TYPE, serializationType.getRawClass());
    }

    @Test
    public void testFindSerializationTypeWithAnnotation() {
        AnnotatedMember am = new AnnotatedMember(null, new Field("field", Double.class, this.getClass()), JsonSerialize.class);
        JavaType declaredType = ObjectTypeFactory.defaultInstance().getRawClassType(Double.class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        Assert.assertEquals(Double.TYPE, serializationType.getRawClass());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Field field = this.getClass().getDeclaredField("defaultValue");
        field.setAccessible(true);
        Object defaultValue = field.get(this);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("defaultValue", null, ObjectTypeFactory.defaultInstance().getRawClassType(this.getClass()));

        if (defaultValue != null) {
            Assert.assertEquals(defaultValue, propertyDefaultValue);
        } else {
            Assert.assertNull(propertyDefaultValue);
        }
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = ObjectTypeFactory.defaultInstance().getRawClassType(String[].class);
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Assert.assertFalse(defaultBean instanceof PropertyBuilder);
    }

    @Test
    public void testThrowWrapped() {
        IllegalAccessException e = new IllegalAccessException("test");
        Object defaultBean = new Object();
        Object thrown = propertyBuilder._throwWrapped(e, "property", defaultBean);
        Assert.assertNotNull(thrown);
        Assert.assertEquals("Failed to get property 'property' of default Object@4e1a9e85 object instances", thrown.getClass().getName());
    }

    private static class TestClass {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int field;
    }

    private static int defaultValue = 42;

    @Test
    public void testNonDefaultInclusion() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        ObjectNode node = mapper.writeValueAsObject(new TestClass());
        Assert.assertFalse(node.isMissingNode("field"));
        int defaultValue = mapper.readerForUpdating(new TestClass()).getField();
        Assert.assertNotEquals(0, defaultValue);
    }
}