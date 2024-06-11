import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.SerializerProviders;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private ObjectMapper mapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        mapper = new ObjectMapper(config);
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(PropertyBuilder.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter_SimpleType_EmptyObject() throws Exception {
        SerializerProvider prov = new BaseSerializerProvider();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("defaultInclusion");
        Object ser = mapper.findValueSerializer(javaType);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, javaType, ser, null, null, null, false);

        Assert.assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType_SimpleType_Annotation() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        AnnotatedMember annotatedMember = new AnnotatedMember() {
            @Override
            public Class<?> getRawType() {
                return javaType.getRawClass();
            }

            @Override
            public Method getMember() {
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new JsonSerialize[0];
            }
        };

        JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, false, javaType);

        Assert.assertNotNull(serializationType);
        Assert.assertEquals(JavaType.constructCollectionType(mapper.getTypeFactory().constructTypeCollectionType(String.class), JavaType.constructType(String.class)), serializationType);
    }

    @Test
    public void testFindSerializationType_ComplexType_NoAnnotation() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(MyComplexClass.class);

        JavaType serializationType = propertyBuilder.findSerializationType(null, false, javaType);

        Assert.assertNull(serializationType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_ComplexType_InvalidAnnotation() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(MyComplexClass.class);
        AnnotatedMember annotatedMember = new AnnotatedMember() {
            @Override
            public Class<?> getRawType() {
                return javaType.getRawClass();
            }

            @Override
            public Method getMember() {
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new JsonSerialize[0];
            }
        };

        propertyBuilder.findSerializationType(annotatedMember, false, javaType);
    }

    @Test
    public void testGetClassAnnotations() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(PropertyBuilder.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(javaType));
        Annotations annotations = propertyBuilder.getClassAnnotations();

        Assert.assertNotNull(annotations);
    }

    @Test
    public void testGetDefaultBean() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(PropertyBuilder.class));

        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Assert.assertFalse(defaultBean instanceof PropertyBuilder);
    }

    @Test
    public void testGetPropertyDefaultValue() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(PropertyBuilder.class));
        Object defaultBean = propertyBuilder.getDefaultBean();

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("_defaultInclusion", null, mapper.getTypeFactory().constructType(String.class));
        Assert.assertNull(propertyDefaultValue);

        // Test with default bean value
        String defaultValue = (String) BeanUtil.getRawValue(defaultBean, "defaultExclusion");
        propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("_defaultExclusion", null, mapper.getTypeFactory().constructType(String.class));
        Assert.assertEquals(defaultValue, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_Null() throws IOException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(PropertyBuilder.class));
        Object defaultBean = propertyBuilder.getDefaultBean();

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("_defaultExclusion", null, mapper.getTypeFactory().constructType(String.class));
        Assert.assertNull(propertyDefaultValue);
    }

    private static class BaseSerializerProvider extends SerializerProvider {
        @Override
        public Object getSerializerForProperty(BeanPropertyWriter writer) {
            return null;
        }
    }

    private static class MyComplexClass {
    }
}