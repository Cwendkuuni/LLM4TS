package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.ObjectIdGenerator;
import com.fasterxml.jackson.databind.json.ObjectIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.test.BaseMapTest;
import com.fasterxml.jackson.test.BaseTest;

import org.junit.Assert;
import org.junit.Test;

public class ObjectIdValuePropertyTest extends BaseTest {

    @Test
    public void testObjectIdValuePropertyNormal() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Long.class;
            }

            @Override
            public ObjectIdGenerator generator() {
                return new ObjectIdGenerator() {
                    long id = 0;

                    @Override
                    public String generateId(SerializationContext ctxt) throws IOException {
                        return Long.toString(id++);
                    }
                };
            }

            @Override
            public ObjectIdResolver resolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId idId(String idStr, DeserializationContext ctxt) throws IOException {
                        return new ObjectId(idStr);
                    }

                    @Override
                    public ObjectId idFromValue(Object value, DeserializationContext ctxt) throws IOException {
                        return new ObjectId(value.toString());
                    }
                };
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        StdSerializer<Long> longSerializer = new StdSerializer<Long>(Long.class) {};
        JsonDeserializer<Long> longDeserializer = new JsonDeserializer<Long>(Long.class) {};
        longDeserializer.setContentDeserializer(longSerializer);

        property = new ObjectIdValueProperty(property, longDeserializer, null);

        PropertyName idPropertyName = PropertyName.construct("idProperty");
        SettableBeanProperty idProperty = new StdSettableBeanProperty(idPropertyName, property.getType(), null);

        PropertyName namePropertyName = PropertyName.construct("name");
        SettableBeanProperty nameProperty = new StdSettableBeanProperty(namePropertyName, String.class, null);

        BeanDescription beanDescription = new BeanDescription();
        beanDescription.setName("TestBean");
        beanDescription.addProperty(nameProperty);
        beanDescription.addProperty(idProperty);

        MapperConfig config = new MapperConfig(mapper.getFactory());
        config.setRootValueFactory(JsonFactory.valueFactory(EmptyObjectUtils.EMPTY_BEAN));
        config.setBeanDescription(beanDescription);

        ObjectMapper objectMapper = new ObjectMapper(config);

        TestBean testBean = new TestBean();
        testBean.setName("TestBeanName");
        testBean.setId(null);

        ObjectNode node = mapper.writeValueAsTree(testBean);
        Assert.assertNull(node.get("id"));

        TestBean deserializedBean = objectMapper.readValue(node.toString(), TestBean.class);
        Assert.assertNull(deserializedBean.getId());

        node.put("id", "1");

        TestBean newBean = objectMapper.readValue(node.toString(), TestBean.class);
        Assert.assertEquals(1L, newBean.getId());
    }

    @Test
    public void testObjectIdValuePropertyNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Object.class;
            }

            @Override
            public ObjectIdGenerator generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        PropertyName idPropertyName = PropertyName.construct("idProperty");
        SettableBeanProperty idProperty = new StdSettableBeanProperty(idPropertyName, Object.class, null);

        PropertyName namePropertyName = PropertyName.construct("name");
        SettableBeanProperty nameProperty = new StdSettableBeanProperty(namePropertyName, String.class, null);

        BeanDescription beanDescription = new BeanDescription();
        beanDescription.setName("TestBean");
        beanDescription.addProperty(nameProperty);
        beanDescription.addProperty(idProperty);

        MapperConfig config = new MapperConfig(mapper.getFactory());
        config.setRootValueFactory(JsonFactory.valueFactory(EmptyObjectUtils.EMPTY_BEAN));
        config.setBeanDescription(beanDescription);

        ObjectMapper objectMapper = new ObjectMapper(config);

        TestBean testBean = new TestBean();
        testBean.setName("TestBeanName");
        testBean.setId(null);

        ObjectNode node = mapper.writeValueAsTree(testBean);
        Assert.assertNull(node.get("id"));

        TestBean deserializedBean = objectMapper.readValue(node.toString(), TestBean.class);
        Assert.assertNull(deserializedBean.getId());

        node.put("id", null);

        TestBean newBean = objectMapper.readValue(node.toString(), TestBean.class);
        Assert.assertNull(newBean.getId());
    }

    @Test
    public void testObjectIdValuePropertyInvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<Object> idType() {
                return Long.class;
            }

            @Override
            public ObjectIdGenerator generator() {
                return null;
            }

            @Override
            public ObjectIdResolver resolver() {
                return null;
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>(String.class) {};
        property = new ObjectIdValueProperty(property, stringDeserializer, null);

        PropertyName idPropertyName = PropertyName.construct("idProperty");
        SettableBeanProperty idProperty = new StdSettableBeanProperty(idPropertyName, property.getType(), null);

        PropertyName namePropertyName = PropertyName.construct("name");
        SettableBeanProperty nameProperty = new StdSettableBeanProperty(namePropertyName, String.class, null);

        BeanDescription beanDescription = new BeanDescription();
        beanDescription.setName("TestBean");
        beanDescription.addProperty(nameProperty);
        beanDescription.addProperty(idProperty);

        MapperConfig config = new MapperConfig(mapper.getFactory());
        config.setRootValueFactory(JsonFactory.valueFactory(EmptyObjectUtils.EMPTY_BEAN));
        config.setBeanDescription(beanDescription);

        ObjectMapper objectMapper = new ObjectMapper(config);

        TestBean testBean = new TestBean();
        testBean.setName("TestBeanName");
        testBean.setId(null);

        ObjectNode node = mapper.writeValueAsTree(testBean);
        Assert.assertNull(node.get("id"));

        TestBean deserializedBean = objectMapper.readValue(node.toString(), TestBean.class);
        Assert.assertNull(deserializedBean.getId());

        node.put("id", "TestString");

        try {
            TestBean newBean = objectMapper.readValue(node.toString(), TestBean.class);
            Assert.fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    static class TestBean {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class ObjectId {
        private String id;

        ObjectId(String id) {
            this.id = id;
        }

        public String toString() {
            return id;
        }
    }

    static class EmptyObjectUtils {
        public static Object EMPTY_BEAN = new Object() {};
    }

    static class TestMapperFactory extends SimpleModule {
        @Override
        public void setupModule(SerializationConfig config) {
            config.setRootValueSerializer(new StdSerializer<Object>(Object.class) {});
        }

        @Override
        public void setupModule(DeserializationConfig config) {
            config.addDeserializer(Long.class, new JsonDeserializer<Long>(Long.class) {});
        }
    }
}