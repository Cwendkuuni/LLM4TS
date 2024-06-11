package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private PropertyBuilder _propertyBuilder;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        _config = mapper.getSerializationConfig();
        _beanDesc = mapper.getIntrospector().findBeanDescription(DummyBean.class, null);
        _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
    }

    @Test
    public void testDefaultInclusion() throws IOException {
        JsonInclude.Value inclusion = _propertyBuilder.getDefaultInclusion();
        Assert.assertEquals(JsonInclude.Include.NON_DEFAULT, inclusion.getDefaultValueInclusion());
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, inclusion.getDefaultContentInclusion());
    }

    @Test
    public void testDefaultValueForPublicFields() throws IOException {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        DummyBean dummyBean = (DummyBean) _beanDesc.instantiateBean(_config.canOverrideAccessModifiers());

        Field publicField = DummyBean.class.getField("publicField");
        publicField.setAccessible(true);
        Object publicFieldValue = publicField.get(dummyBean);

        Object defaultValue = _propertyBuilder.getPropertyDefaultValue("publicField", AccessorUtil.getAnnotatedMember(publicField), _beanDesc.getType());
        Assert.assertEquals(publicFieldValue, defaultValue);
    }

    @Test
    public void testDefaultValueForPrivateFields() throws IOException {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        DummyBean dummyBean = (DummyBean) _beanDesc.instantiateBean(_config.canOverrideAccessModifiers());

        Field privateField = DummyBean.class.getDeclaredField("privateField");
        privateField.setAccessible(true);
        Object privateFieldValue = privateField.get(dummyBean);

        Object defaultValue = _propertyBuilder.getPropertyDefaultValue("privateField", AccessorUtil.getAnnotatedMember(privateField), _beanDesc.getType());
        if (defaultValue == null) {
            Assert.assertNull(privateFieldValue);
        } else {
            Assert.assertEquals(privateFieldValue, defaultValue);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailedToGetDefaultValueForNullBean() throws IOException {
        Object defaultValue = _propertyBuilder.getPropertyDefaultValue("nullBean", null, _beanDesc.getType());
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalAccessException.class)
    public void testFailedToAccessDefaultBeanField() throws IOException {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        DummyBean dummyBean = (DummyBean) _beanDesc.instantiateBean(_config.canOverrideAccessModifiers());

        Field privateField = DummyBean.class.getDeclaredField("privateField");
        privateField.setAccessible(true);
        privateField.set(defaultBean, new Object());

        try {
            Object defaultValue = _propertyBuilder.getPropertyDefaultValue("privateField", AccessorUtil.getAnnotatedMember(privateField), _beanDesc.getType());
            Assert.fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailedToGetDefaultBeanInstance() throws IOException {
        Object defaultBean = _propertyBuilder.getDefaultBean();
        if (defaultBean != null) {
            _propertyBuilder._defaultBean = null;
            Object defaultValue = _propertyBuilder.getPropertyDefaultValue("publicField", AccessorUtil.getAnnotatedMember(new Field("publicField") {
                public int getModifiers() {
                    return AccessibleObject.PROTECTED;
                }
            }), _beanDesc.getType());
            Assert.fail("Expected IllegalArgumentException");
        }
    }

    @Test
    public void testGetSerializationType() throws IOException {
        AnnotatedMember member = new AnnotatedField("testField", new Field("testField") {
            public Class<?> getType() {
                return String.class;
            }
        }, DummyBean.class);
        JavaType declaredType = _beanDesc.getType();
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String[].class);

        JavaType serializationType = _propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(expectedType, serializationType);

        expectedType = null;
        serializationType = _propertyBuilder.findSerializationType(member, false, null);
        Assert.assertNull(serializationType);

        expectedType = TypeFactory.defaultInstance().constructType(DummyBean.class);
        serializationType = _propertyBuilder.findSerializationType(member, true, declaredType);
        Assert.assertEquals(expectedType, serializationType);
    }

    @Test(expected = JsonMappingException.class)
    public void testFailedToFindSerializationType() throws IOException {
        AnnotatedMember member = new AnnotatedField("nonExistentField", new Field("nonExistentField") {
        }, DummyBean.class);
        _propertyBuilder.findSerializationType(member, false, _beanDesc.getType());
        Assert.fail("Expected JsonMappingException");
    }

    @Test
    public void testBuildWriter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        DummyBean dummyBean = new DummyBean();
        dummyBean.setPublicField("publicValue");

        String serializedString = mapper.writeValueAsString(dummyBean);
        JsonNode serializedNode = mapper.readTree(serializedString);

        BeanPropertyWriter writer = _propertyBuilder.buildWriter(serializerProvider, null, _beanDesc.getType(), null, null, null, null, false);
        Assert.assertNotNull(writer);

        writer.serializeAsField(_config, null, _beanDesc, null, null, null, null);
        Assert.assertEquals("publicValue", writer.getValue());

        writer = _propertyBuilder.buildWriter(serializerProvider, null, _beanDesc.getType(), null, null, null, null, true);
        Assert.assertNotNull(writer);

        writer.serializeAsField(_config, null, _beanDesc, null, null, null, null);
        Assert.assertNull(writer.getValue());

        writer = _propertyBuilder.buildWriter(serializerProvider, null, _beanDesc.getType(), null, null, null, null, false);
        Assert.assertNotNull(writer);

        writer.serializeAsField(_config, null, _beanDesc, serializedNode, null, null, null);
        Assert.assertEquals("publicValue", writer.getValue());

        writer = _propertyBuilder.buildWriter(serializerProvider, null, _beanDesc.getType(), null, null, null, null, true);
        Assert.assertNotNull(writer);

        writer.serializeAsField(_config, null, _beanDesc, serializedNode, null, null, null);
        Assert.assertNull(writer.getValue());

        JavaType secondaryType = TypeFactory.defaultInstance().constructType(Integer[].class);
        writer = _propertyBuilder.buildWriter(serializerProvider, null, secondaryType, null, null, null, null, false);
        Assert.assertNotNull(writer);

        writer.serializeAsField(_config, null, _beanDesc, null, null, null, null);
        Assert.assertNull(writer.getValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testFailedToBuildWriter() throws IOException {
        SerializerProvider serializerProvider = new SerializerProvider();
        _propertyBuilder.buildWriter(serializerProvider, null, _beanDesc.getType(), null, null, null, null, false);
        Assert.fail("Expected JsonMappingException");
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = _propertyBuilder.getClassAnnotations();
        Assert.assertEquals(Arrays.asList(new JsonInclude(JsonInclude.Include.NON_DEFAULT), new JsonAutoDetect()), annotations.annotations());
    }

    static class DummyBean {
        private String privateField;
        public String publicField;

        public String getPublicField() {
            return publicField;
        }

        public void setPublicField(String publicField) {
            this.publicField = publicField;
        }

        private String getPrivateField() {
            return privateField;
        }

        public void setPrivateField(String privateField) {
            this.privateField = privateField;
        }

        public DummyBean() {
        }
    }

    static class AnnotatedField extends AnnotatedMember {
        private Field field;

        public AnnotatedField(String name, Field field, Class<?> annotatedClass) {
            super(name, null, null, null, null, annotatedClass, null);
            this.field = field;
        }

        @Override
        public AnnotatedElement getElement() {
            return field;
        }

        @Override
        public Class<?> getAnnotatedType() {
            return field.getType();
        }
    }
}