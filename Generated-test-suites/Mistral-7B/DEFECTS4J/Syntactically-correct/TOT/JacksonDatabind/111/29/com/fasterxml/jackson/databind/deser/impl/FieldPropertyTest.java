package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FieldPropertyTest extends BaseTest {

    public static class TestBean {
        @JsonProperty(value = "field1")
        private int field1;

        @JsonProperty(value = "field2")
        private String field2;

        public int getField1() {
            return field1;
        }

        public void setField1(int field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        @JsonProperty(value = "field3")
        private transient Field field3;
    }

    @SuppressWarnings("unchecked")
    public void testFieldProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString(bean));

        // Test deserialization with valid values
        p.nextToken(); // [START]
        FieldProperty fp = p.readField("field1");
        assertEquals(0, fp.getValueAsInt(p, null, null));

        p.nextToken(); // "value"
        fp = p.readField("field1");
        fp.setValueAsInt(p, bean, null);
        assertEquals(1, bean.getField1());

        p.nextToken(); // null
        fp = p.readField("field1");
        if (fp.getValueAsInt(p, null, null) == null) {
            assertNull(bean.getField1());
        } else {
            assertEquals(0, bean.getField1());
        }

        // Test deserialization with TypeDeserializer
        p.nextToken(); // [START]
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("field1", IntType.INSTANCE, null, null, null, null, null);
        FieldProperty fpWithTypeDeserializer = new FieldProperty(propDef, IntType.INSTANCE, null, null, null);

        fp = p.readField("field1");
        fp.setValueAsInt(p, bean, fpWithTypeDeserializer);
        assertEquals(2, bean.getField1());

        p.nextToken(); // "value"
        fp = p.readField("field1");
        fp.setValueAsInt(p, bean, fpWithTypeDeserializer);
        assertEquals(3, bean.getField1());

        // Test deserialization with NullValueProvider
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        p.nextToken(); // [START]
        FieldProperty fpWithNullValueProvider = new FieldProperty(propDef, IntType.INSTANCE, null, null, null);
        fpWithNullValueProvider = new FieldProperty(fpWithNullValueProvider, null, nva);

        fp = p.readField("field1");
        fp.setValueAsInt(p, bean, fpWithNullValueProvider);
        assertNull(bean.getField1());

        p.nextToken(); // null
        fp = p.readField("field1");
        fp.setValueAsInt(p, bean, fpWithNullValueProvider);
        assertNull(bean.getField1());

        // Test deserialization with ValueDeserializer
        JsonDeserializer<String> valueDeserializer = new StandardDeserializer<String>(String.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return "value";
            }
        };

        p.nextToken(); // [START]
        FieldProperty fpWithValueDeserializer = new FieldProperty(propDef, IntType.INSTANCE, valueDeserializer, null, null);

        fp = p.readField("field1");
        try {
            fp.setValueAsInt(p, bean, fpWithValueDeserializer);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test deserialization with TypeDeserializer and ValueDeserializer
        TypeDeserializer typeDeserializer = new StdTypeDeserializer();

        p.nextToken(); // [START]
        FieldProperty fpWithTypeAndValueDeserializer = new FieldProperty(propDef, IntType.INSTANCE, valueDeserializer, null, null);
        fpWithTypeAndValueDeserializer = new FieldProperty(fpWithTypeAndValueDeserializer, null, null, typeDeserializer);

        try {
            fp = p.readField("field1");
            fp.setValueAsInt(p, bean, fpWithTypeAndValueDeserializer);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test serialization and deserialization
        ObjectWriter ow = mapper.writer();
        JsonNode node = ow.writeValueAsTree(bean);
        JsonParser parser = mapper.getFactory().createParser(node.toString());

        FieldProperty fpFromString = parser.readField("field1");
        assertEquals(0, fpFromString.getValueAsInt(parser, null, null));

        fpFromString.setValueAsInt(parser, bean, null);
        JsonNode nodeAfterDeserialization = mapper.writeValueAsTree(bean);
        assertEquals(node.toString(), nodeAfterDeserialization.toString());

        // Test access modification
        TestBean testBean = new TestBean();
        TestBean.Field f = TestBean.class.getDeclaredField("field3");
        f.setAccessible(true);
        FieldProperty property = new FieldProperty(null, null, null, null, f);
        property.fixAccess(mapper.getConfig());
        try {
            f.setAccessible(false);
            f.set(testBean, "value");
            fail("Expected AccessControlException");
        } catch (AccessControlException e) {
            // Expected
        }

        // Test deserialization with annotations
        TestBeanWithAnnotation beanWithAnnotation = new TestBeanWithAnnotation();
        p = mapper.getFactory().createParser(mapper.writeValueAsString(beanWithAnnotation));

        FieldProperty fpWithAnnotation = p.readField("annotationField");
        assertEquals(1, fpWithAnnotation.getValueAsInt(p, null, null));

        p.nextToken(); // "value2"
        fpWithAnnotation = p.readField("annotationField");
        fpWithAnnotation.setValueAsInt(p, beanWithAnnotation, null);
        assertEquals(2, beanWithAnnotation.getAnnotationField());

        p.nextToken(); // null
        fpWithAnnotation = p.readField("annotationField");
        if (fpWithAnnotation.getValueAsInt(p, null, null) == null) {
            assertNull(beanWithAnnotation.getAnnotationField());
        } else {
            assertEquals(1, beanWithAnnotation.getAnnotationField());
        }

        // Test accessing annotated members
        TestBeanAnnotatedWithJsonProperty beanAnnotated = new TestBeanAnnotatedWithJsonProperty();
        p = mapper.getFactory().createParser(mapper.writeValueAsString(beanAnnotated));

        FieldProperty fpWithAnnotatedMembers = p.readField("annotatedField");
        assertEquals("value1", fpWithAnnotatedMembers.getValueAsString(p, null, null));

        p.nextToken(); // "value2"
        fpWithAnnotatedMembers = p.readField("annotatedField");
        fpWithAnnotatedMembers.setValueAsString(p, beanAnnotated, null);
        assertEquals("value2", beanAnnotated.getAnnotatedField());

        p.nextToken(); // null
        fpWithAnnotatedMembers = p.readField("annotatedField");
        if (fpWithAnnotatedMembers.getValueAsString(p, null, null) == null) {
            assertNull(beanAnnotated.getAnnotatedField());
        } else {
            assertEquals("value1", beanAnnotated.getAnnotatedField());
        }
    }

    public static class TestBeanWithAnnotation {
        @JsonProperty(value = "annotationField")
        @JsonValue
        private int annotationField;

        public int getAnnotationField() {
            return annotationField;
        }

        public void setAnnotationField(int annotationField) {
            this.annotationField = annotationField;
        }
    }

    public static class TestBeanAnnotatedWithJsonProperty {
        @JsonProperty(value = "annotatedField")
        private String annotatedField;

        public String getAnnotatedField() {
            return annotatedField;
        }

        public void setAnnotatedField(String annotatedField) {
            this.annotatedField = annotatedField;
        }
    }
}