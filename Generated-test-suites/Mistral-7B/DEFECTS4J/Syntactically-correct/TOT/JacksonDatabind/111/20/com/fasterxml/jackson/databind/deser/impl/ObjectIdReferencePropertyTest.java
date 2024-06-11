package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdReferencePropertyTest {

    @SuppressWarnings("unchecked")
    static class TestPojo {
        @JsonDeserialize(using = ObjectIdReferenceProperty.class)
        private ObjectIdReferenceProperty idRef;
        private String strValue;

        public ObjectIdReferenceProperty getIdRef() {
            return idRef;
        }

        public void setIdRef(ObjectIdReferenceProperty idRef) {
            this.idRef = idRef;
        }

        public String getStrValue() {
            return strValue;
        }

        public void setStrValue(String strValue) {
            this.strValue = strValue;
        }
    }

    @org.junit.Test
    public void testObjectIdReferenceProperty() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestPojo pojo = new TestPojo();
        ObjectNode node = mapper.createObjectNode();
        node.put("idRef", null);
        node.put("strValue", "test");

        TestPojo deserializedPojo = mapper.readValue(node, TestPojo.class);
        assertEquals(null, deserializedPojo.getIdRef());
        assertEquals("test", deserializedPojo.getStrValue());

        ObjectIdReferenceProperty idRefProp = new ObjectIdReferenceProperty(new ObjectIdReferenceProperty(_forward(TestPojo.class.getDeclaredField("idRef")), mapper.findValueDeserializer(ObjectIdReferenceProperty.class, TestPojo.class), null), mapper.nullValueProvider());
        ObjectNode idRefNode = mapper.createObjectNode();
        idRefNode.put("name", "name");
        idRefNode.put("value", "value");

        AnnotatedMember member = AnnotationIntrospector.findAnnotated(TestPojo.class, ObjectIdReferenceProperty.class);
        ObjectIdInfo objectIdInfo = member.findAnnotation(ObjectIdInfo.class);

        idRefProp = new ObjectIdReferenceProperty(idRefProp, idRefNode, mapper.findDeserializer(String.class, TestPojo.class), null);
        assertEquals("name", idRefProp.getName());
        assertEquals(String.class, idRefProp.getType());
        assertEquals(StringDeserializer.class, idRefProp.getValueDeserializer().getClass());

        JsonParser testParser = new BaseJsonParser();
        ObjectNode testNode = mapper.readTree("{\"idRef\":{\"name\":\"name\",\"value\":\"value\"}}");
        idRefProp.deserializeAndSet(testParser, mapper.getDeserializationContext(), pojo);
        assertEquals("value", pojo.getIdRef().getValue().asText());

        assertEquals("test", pojo.getStrValue());

        testParser = new BaseJsonParser();
        testNode = mapper.readTree("{\"strValue\":\"test\"}");
        idRefProp.deserializeAndSet(testParser, mapper.getDeserializationContext(), pojo);
        assertEquals("value", pojo.getIdRef().getValue().asText());
        assertEquals("test", pojo.getStrValue());

        testParser = new BaseJsonParser();
        testNode = mapper.readTree("{\"idRef\":null}");
        idRefProp.deserializeAndSet(testParser, mapper.getDeserializationContext(), pojo);
        assertNull(pojo.getIdRef());
        assertEquals("test", pojo.getStrValue());

        testParser = new BaseJsonParser();
        ObjectNode testNodeEmpty = mapper.readTree("{}");
        try {
            idRefProp.deserializeAndSet(testParser, mapper.getDeserializationContext(), pojo);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        testParser = new BaseJsonParser();
        ObjectNode testNodeNull = mapper.readTree("{\"idRef\":null}");
        try {
            idRefProp.deserializeAndSet(testParser, mapper.getDeserializationContext(), pojo);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        testParser = new BaseJsonParser();
        ObjectNode testNodeInvalid = mapper.readTree("{\"idRef\":{\"name\":\"invalid\"}}");
        try {
            idRefProp.deserializeAndSet(testParser, mapper.getDeserializationContext(), pojo);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    private static JsonDeserializer<?> _forward(Field field) {
        return (JsonDeserializer<?>) Reflections.getFieldValue(field, ObjectMapper.class, "valueDeserializer");
    }
}