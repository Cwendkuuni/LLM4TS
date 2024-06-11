package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private JsonDeserializer<Object> idDeserializer;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        idDeserializer = Mockito.mock(JsonDeserializer.class);
        objectIdReader = new ObjectIdReader(_ -> idDeserializer) {
            @Override
            public String getPropertyName() {
                return "id";
            }

            @Override
            public Class<?> getIdType() {
                return Long.class;
            }
        };
    }

    @Test
    public void testNullId() throws JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectReader reader = mapper.readerForUpdating(Object.class).forProperty("idProperty");

        JsonNode node = mapper.createObjectNode();
        node.putNull("id");

        Object instance = reader.readValue(node);
        assertThat(instance, is(notNullValue()));
        // null ID should not be set
        JsonNode resultNode = mapper.writeValueAsTree(instance);
        JsonNode idNode = resultNode.get("idProperty");
        assertThat(idNode, is(nullValue()));
    }

    @Test
    public void testValidId() throws JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        ObjectReader reader = mapper.readerForUpdating(Object.class).forProperty("idProperty");

        ObjectIdReader idReader = Mockito.mock(ObjectIdReader.class);
        ObjectIdGenerator generator = Mockito.mock(ObjectIdGenerator.class);
        ObjectIdResolver resolver = Mockito.mock(ObjectIdResolver.class);

        MapperConfig config = mapper.getConfig();
        config.setAnnotationIntrospector(Mockito.mock(AnnotationIntrospector.class));
        ObjectIdReader mockedObjectIdReader = Mockito.mock(ObjectIdReader.class);
        when(mockedObjectIdReader.idProperty).thenReturn(new PropertyName("idProperty"));
        when(objectIdReader.generator).thenReturn(generator);
        when(objectIdReader.resolver).thenReturn(resolver);
        when(idDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(1L);

        when(idReader.getDeserializer()).thenReturn(idDeserializer);
        property = new ObjectIdValueProperty(idReader, property);

        JsonNode node = mapper.createObjectNode();
        node.putNumber("id", 1L);

        Object instance = reader.readValue(node);
        assertThat(instance, is(notNullValue()));
        // ID should be set
        JsonNode resultNode = mapper.writeValueAsTree(instance);
        JsonNode idNode = resultNode.get("idProperty");
        JsonNode idValueNode = idNode.get("$");
        assertThat(idValueNode.longValue(), is(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSettingId() throws JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, null) {
            @Override
            public SettableBeanProperty idProperty() {
                return property;
            }
        };
        when(idProperty.idProperty).thenReturn(property);

        property.setAndReturn(instance, "id");
    }
}