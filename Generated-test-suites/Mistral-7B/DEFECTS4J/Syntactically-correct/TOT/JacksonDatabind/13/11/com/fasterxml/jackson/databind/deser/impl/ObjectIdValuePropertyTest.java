import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.lang.reflect.Field;

public class ObjectIdValuePropertyTest {

    @Test
    public void testValidObjectIdDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new FromStringDeserializer(Long.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long id = 1L;
                    @Override
                    public ObjectId generateId(DeserializationContext ctxt, JsonParser jp) throws IOException {
                        return new ReadableObjectId() {
                            @Override
                            public long id() {
                                return id++;
                            }
                        };
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idForValue(DeserializationContext ctxt, Object idValue) throws IOException {
                        return new ReadableObjectId() {
                            @Override
                            public long id() {
                                return ((Long) idValue).longValue();
                            }
                        };
                    }
                };
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null));

        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return 1L;
            }
        };

        ObjectIdValueProperty propertyWithValueDeserializer = new ObjectIdValueProperty(objectIdReader, propertyMetadata -> new PropertyMetadata(null, valueDeserializer), valueDeserializer);

        ObjectMapper mapperWithSettableBeanProperty = new ObjectMapper();
        mapperWithSettableBeanProperty.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializer(ObjectIdValueProperty.class, propertyWithValueDeserializer);
            }
        });

        MyPojo pojo = mapperWithSettableBeanProperty.readValue(
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        new ObjectMapper().writeValueAsString(new MyPojo())
                ), MyPojo.class);

        Assert.assertEquals(1L, pojo.getIdValue());

        Field idField = MyPojo.class.getDeclaredField("idValue");
        idField.setAccessible(true);
        idField.set(pojo, 2L);

        String jsonWithNewId = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);

        MyPojo pojoWithNewId = mapperWithSettableBeanProperty.readValue(jsonWithNewId, MyPojo.class);

        Assert.assertEquals(2L, pojoWithNewId.getIdValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCannotSetObjectIdPropertyDirectly() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new FromStringDeserializer(Long.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long id = 1L;
                    @Override
                    public ObjectId generateId(DeserializationContext ctxt, JsonParser jp) throws IOException {
                        return new ReadableObjectId() {
                            @Override
                            public long id() {
                                return id++;
                            }
                        };
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idForValue(DeserializationContext ctxt, Object idValue) throws IOException {
                        return new ReadableObjectId() {
                            @Override
                            public long id() {
                                return ((Long) idValue).longValue();
                            }
                        };
                    }
                };
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null));

        ObjectMapper mapperWithSettableBeanProperty = new ObjectMapper();
        mapperWithSettableBeanProperty.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializer(ObjectIdValueProperty.class, objectIdProperty);
            }
        });

        MyPojo pojo = mapperWithSettableBeanProperty.readValue(
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        new ObjectMapper().writeValueAsString(new MyPojo())
                ), MyPojo.class);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata -> new PropertyMetadata(null, null), null);

        try {
            property.set(pojo, 2L);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("Cannot set ObjectIdValueProperty directly.", e);
        }

        Assert.assertEquals(1L, pojo.getIdValue());
    }

    @Test
    public void testNullObjectIdValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new FromStringDeserializer(Long.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long id = 1L;
                    @Override
                    public ObjectId generateId(DeserializationContext ctxt, JsonParser jp) throws IOException {
                        return new ReadableObjectId() {
                            @Override
                            public long id() {
                                return id++;
                            }
                        };
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idForValue(DeserializationContext ctxt, Object idValue) throws IOException {
                        return new ReadableObjectId() {
                            @Override
                            public long id() {
                                return ((Long) idValue).longValue();
                            }
                        };
                    }
                };
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null));

        JsonDeserializer<Object> valueDeserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        ObjectIdValueProperty propertyWithValueDeserializer = new ObjectIdValueProperty(objectIdReader, propertyMetadata -> new PropertyMetadata(null, valueDeserializer), valueDeserializer);

        ObjectMapper mapperWithSettableBeanProperty = new ObjectMapper();
        mapperWithSettableBeanProperty.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializer(ObjectIdValueProperty.class, propertyWithValueDeserializer);
            }
        });

        MyPojo pojo = mapperWithSettableBeanProperty.readValue(
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        new ObjectMapper().writeValueAsString(new MyPojo())
                ), MyPojo.class);

        String jsonWithNullId = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);

        MyPojo pojoWithNullId = mapperWithSettableBeanProperty.readValue(jsonWithNullId, MyPojo.class);

        Assert.assertNull(pojoWithNullId.getIdValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidObjectIdDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("objectId");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<?> idDeserializer() {
                return new FromStringDeserializer(Long.class);
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    long id = 1L;
                    @Override
                    public ObjectId generateId(DeserializationContext ctxt, JsonParser jp) throws IOException {
                        return new ReadableObjectId() {
                            @Override
                            public long id() {
                                return id++;
                            }
                        };
                    }
                };
            }

            @Override
            public ObjectIdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idForValue(DeserializationContext ctxt, Object idValue) throws IOException {
                        return new ReadableObjectId() {
                            @Override
                            public long id() {
                                throw new IllegalArgumentException("Invalid ObjectId.");
                            }
                        };
                    }
                };
            }
        };

        ObjectIdValueProperty objectIdProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null));

        ObjectMapper mapperWithSettableBeanProperty = new ObjectMapper();
        mapperWithSettableBeanProperty.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializer(ObjectIdValueProperty.class, objectIdProperty);
            }
        });

        String jsonWithInvalidId = "{\"objectId\": \"invalidId\"}";

        try {
            MyPojo pojo = mapperWithSettableBeanProperty.readValue(jsonWithInvalidId, MyPojo.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid ObjectId deserialization should fail.", e);
        }
    }

    static class MyPojo {
        @JsonDeserialize(using = ObjectIdValueProperty.class)
        private long idValue;

        public long getIdValue() {
            return idValue;
        }

        public void setIdValue(long idValue) {
            this.idValue = idValue;
        }
    }
}