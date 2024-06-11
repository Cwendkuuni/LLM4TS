package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotationMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ObjectIdValueProperty}
 */
public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader() {
            @Override
            public String getPropertyName() {
                return "objectId";
            }

            @Override
            public Class<Object> getIdType() {
                return Object.class;
            }

            @Override
            public JsonDeserializer<Object> getDeserializer() {
                return new StdDeserializer<Object>(Object.class) {};
            }

            @Override
            public ObjectIdGenerator getGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver getResolver() {
                return null;
            }

            @Override
            public SettableBeanProperty getIdProperty() {
                return new ObjectIdValueProperty(this, new PropertyMetadata());
            }
        };
    }

    @Test
    public void testObjectIdValuePropertyCreation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNotNull(property);
        assertEquals("objectId", property.getName());
        assertEquals(Object.class, property.getType());
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testObjectIdValuePropertyGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testObjectIdValuePropertyDeserializeAndSet() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializer(Object.class, new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsNumber().longValue();
                    }
                });
            }
        });

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser parser = mapper.getFactory().createParser("123");
        Object instance = new Object();

        property.deserializeAndSet(parser, new TestContext(), instance);

        assertEquals(123L, ((Object) instance).getClass().getDeclaredField("objectId").get(instance));
    }

    @Test
    public void testObjectIdValuePropertyDeserializeSetAndReturn() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializer(Object.class, new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsNumber().longValue();
                    }
                });
            }
        });

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonParser parser = mapper.getFactory().createParser("123");
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(parser, new TestContext(), instance);

        assertEquals(123L, result);
        assertEquals(123L, ((Object) instance).getClass().getDeclaredField("objectId").get(instance));
    }

    @Test
    public void testObjectIdValuePropertySet() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializer(Object.class, new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsNumber().longValue();
                    }
                });
            }
        });

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object() {
            private long objectId;

            public long getObjectId() {
                return objectId;
            }

            public void setObjectId(long id) {
                this.objectId = id;
            }
        };

        property.set(instance, 123L);

        assertEquals(123L, ((Object) instance).getClass().getDeclaredField("objectId").get(instance));
    }

    @Test
    public void testObjectIdValuePropertySetAndReturn() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializer(Object.class, new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsNumber().longValue();
                    }
                });
            }
        });

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object() {
            private long objectId;

            public long getObjectId() {
                return objectId;
            }

            public void setObjectId(long id) {
                this.objectId = id;
            }
        };

        Object result = property.setAndReturn(instance, 123L);

        assertEquals(123L, result);
        assertEquals(123L, ((Object) instance).getClass().getDeclaredField("objectId").get(instance));
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));

        assertEquals("newName", newProperty.getName());
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {};
        ObjectIdValueProperty newProperty = property.withValueDeserializer(stringDeserializer);

        assertEquals(stringDeserializer, newProperty._valueDeserializer);
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyWithNullProvider() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        NullValueProvider nullValueProvider = new BaseNullValueProvider() {};
        ObjectIdValueProperty newProperty = property.withNullProvider(nullValueProvider);

        assertEquals(nullValueProvider, newProperty._nullProvider);
        assertEquals(property._objectIdReader, newProperty._objectIdReader);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectReader getCameraReader(DeserializationConfig config, BeanDescription beanDesc, Type type) {
            return null;
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return null;
        }

        @Override
        public ObjectReader getAnnotationIntrospector() {
            return new AnnotationIntrospector() {
                @Override
                public AnnotatedMember findMember(BeanPropertyDefinition propDef, Object value) {
                    return new AnnotatedMember() {
                        @Override
                        public BeanPropertyDefinition getMemberDefinition() {
                            return propDef;
                        }
                    };
                }
            };
        }
    }
}