package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ObjectIdReferencePropertyTest extends BaseMapTest {

    public static class Dto {
        private ObjectIdReferenceProperty id1;
        private ObjectIdReferenceProperty id2;

        public void setId1(ObjectIdReferenceProperty id1) {
            this.id1 = id1;
        }

        public void setId2(ObjectIdReferenceProperty id2) {
            this.id2 = id2;
        }
    }

    public static class JsonDeserializer extends StdSerializer<Dto> {
        @Override
        public Dto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectNode node = p.getCodec().readTree(p);
            return new Dto();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> ObjectIdReferenceProperty createTestProperty(Class<?> type, String name) throws InstantiationException, IllegalAccessException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(type, new AnnotationIntrospector().findIdAnnotation(type, true), null);
        SettableBeanProperty settableBeanProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        Field field = settableBeanProperty.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return (ObjectIdReferenceProperty) field.get(settableBeanProperty);
    }

    @org.junit.Test
    public void testObjectIdReferenceProperty() throws IOException {
        // Test constructor with ObjectIdInfo
        Dto dto = new Dto();
        ObjectIdReferenceProperty id1 = createTestProperty(Dto.class, "id1");
        ObjectIdReferenceProperty id2 = createTestProperty(Dto.class, "id2");
        dto.setId1(id1);
        dto.setId2(id2);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("id1", mapper.valueToTree("value1"));
        rootNode.put("id2", mapper.valueToTree("value2"));
        ObjectNode resultNode = (ObjectNode) mapper.readValue(rootNode.toString(), Dto.class);
        assertSame(dto, resultNode);
        assertEquals("value1", dto.getId1().getValue().getValueAsString());
        assertEquals("value2", dto.getId2().getValue().getValueAsString());

        // Test constructor with SettableBeanProperty
        SettableBeanProperty forward = new ObjectIdReferenceProperty(null, ObjectIdInfo.STD_OBJECT_ID_INFO);
        ObjectIdReferenceProperty id3 = new ObjectIdReferenceProperty(forward, ObjectMapper.getDefaultInstance().getDeserializationConfig());
        dto.setId1(id1);
        dto.setId2(id2);
        dto.setId3(id3);
        ObjectNode rootNode3 = mapper.createObjectNode();
        rootNode3.put("id1", mapper.valueToTree("value1"));
        rootNode3.put("id2", mapper.valueToTree("value2"));
        rootNode3.put("id3", mapper.valueToTree("{@JsonIdentityInfo(generator = ObjectIdGenerators.IntGenerator.class)}"));
        ObjectNode resultNode3 = (ObjectNode) mapper.readValue(rootNode3.toString(), Dto.class);
        assertSame(dto, resultNode3);
        assertEquals("value1", dto.getId1().getValue().getValueAsString());
        assertEquals("value2", dto.getId2().getValue().getValueAsString());
        assertNull(dto.getId3().getValue());

        // Test constructor with SettableBeanProperty and new name
        ObjectIdReferenceProperty id4 = new ObjectIdReferenceProperty(id3, "id4");
        dto.setId3(id3);
        dto.setId4(id4);
        ObjectNode rootNode4 = mapper.createObjectNode();
        rootNode4.put("id3", mapper.valueToTree("value3"));
        rootNode4.put("id4", mapper.valueToTree("value4"));
        ObjectNode resultNode4 = (ObjectNode) mapper.readValue(rootNode4.toString(), Dto.class);
        assertSame(dto, resultNode4);
        assertSame(id3, dto.getId3());
        assertSame(id4, dto.getId4());
        assertEquals("value3", dto.getId3().getValue().getValueAsString());
        assertEquals("value4", dto.getId4().getValue().getValueAsString());

        // Test withJsonDeserializer
        JsonDeserializer deserializer = new JsonDeserializer();
        ObjectIdReferenceProperty id5 = new ObjectIdReferenceProperty(id1, deserializer, ObjectMapper.getDefaultInstance().getNullValueProvider());
        dto.setId1(id1);
        dto.setId5(id5);
        ObjectNode rootNode5 = mapper.createObjectNode();
        rootNode5.put("id1", mapper.valueToTree("value1"));
        ObjectNode id5Node = mapper.createObjectNode();
        id5Node.put("id1", mapper.valueToTree("value5"));
        rootNode5.set("id5", id5Node);
        ObjectNode resultNode5 = (ObjectNode) mapper.readValue(rootNode5.toString(), Dto.class);
        assertSame(dto, resultNode5);
        assertEquals("value1", dto.getId1().getValue().getValueAsString());
        assertEquals("value5", dto.getId5().getValue().getValueAsString());

        // Test withName
        ObjectIdReferenceProperty id6 = new ObjectIdReferenceProperty(id1, ObjectMapper.getDefaultInstance().getDeserializationConfig());
        ObjectIdReferenceProperty id6NewName = id6.withName("id6");
        dto.setId1(id1);
        dto.setId6(id6NewName);
        ObjectNode rootNode6 = mapper.createObjectNode();
        rootNode6.put("id1", mapper.valueToTree("value1"));
        rootNode6.put("id6", mapper.valueToTree("value6"));
        ObjectNode resultNode6 = (ObjectNode) mapper.readValue(rootNode6.toString(), Dto.class);
        assertSame(dto, resultNode6);
        assertSame(id1, dto.getId1());
        assertSame(id6NewName, dto.getId6());
        assertEquals("value1", dto.getId1().getValue().getValueAsString());
        assertEquals("value6", dto.getId6().getValue().getValueAsString());

        // Test fixAccess
        ObjectIdReferenceProperty id7 = new ObjectIdReferenceProperty(null, ObjectIdInfo.STD_OBJECT_ID_INFO);
        id7.fixAccess(ObjectMapper.getDefaultInstance().getDeserializationConfig());
        ObjectNode rootNode7 = mapper.createObjectNode();
        ObjectNode id7Node = mapper.createObjectNode();
        id7Node.put("id7", mapper.valueToTree("value7"));
        rootNode7.set("id7", id7Node);
        ObjectNode resultNode7 = (ObjectNode) mapper.readValue(rootNode7.toString(), Dto.class);
        assertNull(resultNode7);

        // Test getAnnotation
        ObjectIdReferenceProperty id8 = new ObjectIdReferenceProperty(null, ObjectIdInfo.STD_OBJECT_ID_INFO);
        assertEquals(JsonIdentityInfo.class, id8.getAnnotation(JsonIdentityInfo.class));

        // Test getMember
        ObjectIdReferenceProperty id9 = new ObjectIdReferenceProperty(null, ObjectIdInfo.STD_OBJECT_ID_INFO);
        assertSame(Dto.class.getDeclaredField("id1"), id9.getMember());

        // Test getCreatorIndex
        ObjectIdReferenceProperty id10 = new ObjectIdReferenceProperty(null, ObjectIdInfo.STD_OBJECT_ID_INFO);
        assertEquals(-1, id10.getCreatorIndex());

        // Test deserializeAndSet
        ObjectNode rootNode11 = mapper.createObjectNode();
        ObjectNode id11Node = mapper.createObjectNode();
        id11Node.put("id11", mapper.valueToTree("value11"));
        rootNode11.set("id11", id11Node);
        dto.setId1(id1);
        ObjectNode resultNode11 = (ObjectNode) mapper.readValue(rootNode11.toString(), Dto.class);
        assertSame(dto, resultNode11);
        assertEquals("value11", dto.getId1().getValue().getValueAsString());

        // Test deserializeSetAndReturn
        ObjectIdReferenceProperty id12 = new ObjectIdReferenceProperty(null, ObjectIdInfo.STD_OBJECT_ID_INFO);
        ObjectNode rootNode12 = mapper.createObjectNode();
        ObjectNode id12Node = mapper.createObjectNode();
        id12Node.put("id12", mapper.valueToTree("value12"));
        rootNode12.set("id12", id12Node);
        Object node12 = id12.deserializeSetAndReturn(mapper.getFactory().createParser(rootNode12.toString()), ObjectMapper.getDefaultInstance().getDeserializationContext(), dto);
        assertNull(node12);

        // Test set
        ObjectNode rootNode13 = mapper.createObjectNode();
        ObjectNode id13Node = mapper.createObjectNode();
        id13Node.put("id13", mapper.valueToTree("value13"));
        dto.setId1(id1);
        ObjectNode resultNode13 = (ObjectNode) mapper.readValue(rootNode13.toString(), Dto.class);
        assertSame(dto, resultNode13);
        assertEquals("value13", dto.getId1().getValue().getValueAsString());
        id1.set(dto, "newValue");
        ObjectNode resultNode13NewValue = (ObjectNode) mapper.readValue(rootNode13.toString(), Dto.class);
        assertEquals("newValue", resultNode13NewValue.get("id1").getValueAsString());

        // Test setAndReturn
        ObjectNode rootNode14 = mapper.createObjectNode();
        ObjectNode id14Node = mapper.createObjectNode();
        id14Node.put("id14", mapper.valueToTree("value14"));
        ObjectNode resultNode14 = (ObjectNode) mapper.readValue(rootNode14.toString(), Dto.class);
        assertSame(dto, resultNode14);
        assertEquals("value14", dto.getId1().setAndReturn(dto, "newValue").getValue().getValueAsString());

        // Test with UnresolvedForwardReference
        ObjectIdReferenceProperty id15 = new ObjectIdReferenceProperty(null, ObjectIdInfo.STD_OBJECT_ID_INFO);
        ObjectIdReferenceProperty id16 = new ObjectIdReferenceProperty(id15, ObjectMapper.getDefaultInstance().getDeserializationConfig());
        ObjectNode rootNode15 = mapper.createObjectNode();
        ObjectNode id15Node = mapper.createObjectNode();
        id15Node.put("id15", mapper.valueToTree("value15"));
        rootNode15.set("id15", id15Node);
        ObjectNode id16Node = mapper.createObjectNode();
        id16Node.put("id16", mapper.valueToTree("value16"));
        rootNode15.set("id16", id16Node);
        ObjectNode resultNode15 = (ObjectNode) mapper.readValue(rootNode15.toString(), Dto.class);
        assertSame(dto, resultNode15);
        assertEquals("value15", dto.getId15().getValue().getValueAsString());
        assertEquals("value16", dto.getId16().getValue().getValueAsString());
        assertNull(dto.getId1().getValue());
        UnresolvedForwardReference ref = new UnresolvedForwardReference("id1", Object.class, rootNode15);
        Object node15 = id15.deserializeSetAndReturn(mapper.getFactory().createParser(rootNode15.toString()), ObjectMapper.getDefaultInstance().getDeserializationContext(), dto);
        assertSame(dto, node15);
        id15.set(dto, "newValue");
        ObjectNode resultNode15NewValue = (ObjectNode) mapper.readValue(rootNode15.toString(), Dto.class);
        assertEquals("newValue", resultNode15NewValue.get("id15").getValueAsString());
    }
}