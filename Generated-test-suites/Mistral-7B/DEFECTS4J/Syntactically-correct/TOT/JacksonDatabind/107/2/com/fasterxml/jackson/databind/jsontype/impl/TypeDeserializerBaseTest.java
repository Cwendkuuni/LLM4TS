package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.deser.std.NullDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test with no type information
        TypeDeserializerBase deserializer = mapper.getTypeDeserializer(Map.class, null, null, false, null);
        assertEquals("typeDeserializerBase is instance of TypeDeserializer", deserializer instanceof TypeDeserializerBase);
        assertNull("property is null", deserializer.getProperty());
        assertNull("defaultImpl is null", deserializer.getDefaultImpl());
        assertNotNull("deserializers is not null", deserializer.getDeserializers());

        // Test with type information
        BeanProperty property = new BeanProperty("typeProperty", String.class);
        TypeDeserializerBase deserializerForProperty = deserializer.forProperty(property);
        assertEquals("deserializerForProperty is instance of TypeDeserializerBase", deserializerForProperty instanceof TypeDeserializerBase);
        assertEquals("property is not null", deserializerForProperty.getProperty(), property);
        assertSame("defaultImpl is the same", deserializer.getDefaultImpl(), deserializerForProperty.getDefaultImpl());
        assertSame("deserializers is the same", deserializer.getDeserializers(), deserializerForProperty.getDeserializers());

        // Test with type id
        TypeIdResolver idResolver = new TypeIdResolverBase() {};
        TypeDeserializerBase deserializerWithTypeId = mapper.getTypeDeserializer(Map.class, idResolver, "Map", false, null);
        assertEquals("deserializerWithTypeId is instance of TypeDeserializerBase", deserializerWithTypeId instanceof TypeDeserializerBase);
        assertEquals("baseType is Map.class", deserializerWithTypeId.getBaseType().equals(Map.class));
        assertEquals("typeIdResolver is not null", deserializerWithTypeId.getTypeIdResolver(), idResolver);
        assertEquals("typePropertyName is 'Map'", deserializerWithTypeId.getTypePropertyName(), "Map");
        assertEquals("typeIdVisible is false", deserializerWithTypeId.isTypeIdVisible(), false);
        assertNotNull("deserializers is not null", deserializerWithTypeId.getDeserializers());

        // Test with unknown type id
        ObjectNode node = mapper.createObjectNode();
        node.put("typeId", "UnknownTypeId");
        JsonParser parser = mapper.getFactory().createParser(node);
        JsonDeserializer<?> deserializerWithUnknownTypeId = deserializerWithTypeId._findDeserializer(parser.getContext(), "UnknownTypeId");
        assertNull("deserializerWithUnknownTypeId is null", deserializerWithUnknownTypeId);

        // Test with null type id
        JsonDeserializer<?> deserializerWithNullTypeId = deserializerWithTypeId._findDeserializer(parser.getContext(), null);
        assertInstanceOf("deserializerWithNullTypeId is instance of NullDeserializer", deserializerWithNullTypeId, NullDeserializer.class);

        // Test with default type id
        JsonDeserializer<?> deserializerWithDefaultTypeId = deserializerWithTypeId._findDefaultImplDeserializer(parser.getContext());
        assertInstanceOf("deserializerWithDefaultTypeId is instance of NullDeserializer", deserializerWithDefaultTypeId, NullDeserializer.class);
    }
}