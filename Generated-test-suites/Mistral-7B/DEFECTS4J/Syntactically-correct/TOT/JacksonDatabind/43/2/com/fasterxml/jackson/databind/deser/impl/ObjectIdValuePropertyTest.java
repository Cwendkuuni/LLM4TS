package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.BeanPropertyDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @BeforeClass
    public void setup() {
        objectMapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader() {
            @Override
            public Class<? extends IdType> getIdTypeClass() {
                return StringId.class;
            }

            @Override
            public ObjectIdGenerator<?> getGenerator() {
                return new ObjectIdGenerator() {
                    long counter = 0;

                    @Override
                    public ObjectId generateId(Serializable id) {
                        return new ObjectId(id.toString(), counter++);
                    }
                };
            }

            @Override
            public ObjectIdResolver<?> getResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idFromString(String id) {
                        return new ObjectId(id, 0);
                    }
                };
            }
        };
    }

    @Test
    public void testDeserializationWithValidId() throws JsonMappingException, JsonProcessingException {
        PropertyName propertyName = new PropertyName("objectIdProperty");
        JsonDeserializer<String> stringDeserializer = new BeanPropertyDeserializer(objectIdReader);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        property.setName(propertyName);
        property.setValueDeserializer(stringDeserializer);

        AnnotatedClass annotatedClass = new AnnotatedClass(TestPojo.class, null);
        BeanPropertyDefinition definition = annotatedClass.findPropertyDefinition("objectIdProperty");
        AnnotatedMember member = definition.getMember();
        property.setMember(member);

        TestPojo pojo = new TestPojo();
        String id = "123";

        ObjectMapper mapper = new ObjectMapper();
        Object newPojo = mapper.readValue("{\"objectIdProperty\":\"" + id + "\"}", TestPojo.class);
        assertEquals(pojo.getId(), newPojo.getId());
    }

    @Test
    public void testDeserializationWithNullId() throws JsonMappingException, JsonProcessingException {
        PropertyName propertyName = new PropertyName("objectIdProperty");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        property.setName(propertyName);

        TestPojo pojo = new TestPojo();

        ObjectMapper mapper = new ObjectMapper();
        Object newPojo = mapper.readValue("{\"objectIdProperty\":null}", TestPojo.class);
        assertNull(newPojo.getId());
        assertEquals(pojo.getId(), newPojo.getId());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSettingValueDirectly() throws IOException {
        TestPojo pojo = new TestPojo();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());
        property.setName(new PropertyName("objectIdProperty"));
        property.setValueDeserializer(new StdSerializer<String>(String.class) {});
        property.setMember(null);

        pojo.setId(123);
        property.set(pojo, "invalidId");
    }

    static class TestPojo {
        private Long id;

        public Long getId() {
            return id;
        }

        @JsonDeserialize(using = ObjectIdValueProperty.class)
        public void setId(Long id) {
            this.id = id;
        }
    }
}