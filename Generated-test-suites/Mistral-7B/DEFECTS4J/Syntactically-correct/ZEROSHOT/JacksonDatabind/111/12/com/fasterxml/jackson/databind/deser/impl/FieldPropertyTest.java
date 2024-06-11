package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class FieldPropertyTest {

    @Test
    public void testFieldProperty() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testField", String.class, null, null, null, null);
        JavaType type = new TypeReference<String>() {}.getType();
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new StandardAnnotations();
        AnnotatedField field = new AnnotatedField("testField", String.class, null, null, null, null);
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        // Test constructor with Name
        PropertyName newName = new PropertyName("testField2");
        FieldProperty newFieldProperty = fieldProperty.withName(newName);
        assertEquals(newName, newFieldProperty.getName());

        // Test constructor with ValueDeserializer
        JsonDeserializer<Integer> valueDeserializer = new StandardDeserializer<Integer>(Integer.class);
        FieldProperty newFieldProperty2 = newFieldProperty.withValueDeserializer(valueDeserializer);
        assertEquals(valueDeserializer, newFieldProperty2.getValueDeserializer());

        // Test constructor with NullValueProvider
        NullValueProvider nva = new StandardNullValueProvider();
        FieldProperty newFieldProperty3 = newFieldProperty2.withNullProvider(nva);
        assertEquals(nva, newFieldProperty3.getNullProvider());

        // Test deserializeAndSet method
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testField\": null}");
        Object instance = new Object();
        fieldProperty.deserializeAndSet(jsonParser, null, instance);
        assertEquals(null, fieldProperty.getField().get(instance));

        jsonParser = objectMapper.getFactory().createParser("{\"testField\": 1}");
        fieldProperty.deserializeAndSet(jsonParser, null, instance);
        assertEquals(1, fieldProperty.getField().get(instance));

        jsonParser = objectMapper.getFactory().createParser("{\"testField\": null}");
        if (fieldProperty._skipNulls) {
            assertEquals(instance, fieldProperty.deserializeAndSet(jsonParser, null, instance));
        } else {
            assertEquals(null, fieldProperty.deserializeAndSet(jsonParser, null, instance));
        }

        // Test deserializeSetAndReturn method
        assertEquals(instance, fieldProperty.deserializeSetAndReturn(jsonParser, null, instance));

        // Test set method
        String value = "test";
        fieldProperty.set(instance, value);
        assertEquals(value, fieldProperty.getField().get(instance));

        // Test setAndReturn method
        assertEquals(instance, fieldProperty.setAndReturn(instance, value));

        // Test JDK serialization handling
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("test.ser"));
        objectOutputStream.writeObject(fieldProperty);
        Object input = objectOutputStream.readObject();
        assertEquals(fieldProperty, input);
    }
}