package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DateTimeSerializerBaseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateTimeSerializerBase<Date>(Date.class, true, null));
        mapper.registerModule(module);
    }

    @Test
    public void testTimestampSerializers() throws JsonGenerationException, JsonMappingException, IOException {
        Date date1 = new Date(System.currentTimeMillis());
        JsonNode node = mapper.writeValueAsTree(date1);
        assertEquals("Expected timestamp to be serialized as number", JsonNode.Value.numberValue(System.currentTimeMillis()), node);

        Date date2 = new Date(0L);
        node = mapper.writeValueAsTree(date2);
        assertTrue("Expected empty (null date) to be serialized as number 0", node.isNumber());
        assertEquals("Expected empty (null date) to be serialized as number 0", 0L, node.longValue());

        // Test empty serialization with provider
        SerializerProvider provider = mapper.getSerializationConfig().getProvider();
        Date emptyDate = null;
        node = mapper.writeValueAsTree(emptyDate, provider);
        assertTrue("Expected empty (null date) to be serialized as number 0", node.isNumber());
        assertEquals("Expected empty (null date) to be serialized as number 0", 0L, node.longValue());
    }

    @Test
    public void testCustomFormatSerializers() throws JsonGenerationException, JsonMappingException, IOException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS zzz", Locale.ENGLISH);
        customFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }
        };

        SimpleModule customModule = new SimpleModule();
        customModule.addSerializer(Date.class, customSerializer);
        mapper.registerModule(customModule);

        Date customDate = new Date(System.currentTimeMillis());
        ObjectNode node = mapper.writeValueAsTree(customDate, customSerializer, mapper.getSerializationConfig().getProvider());
        String serializedDate = node.textValue();
        Date deserializedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS zzz").parse(serializedDate);
        assertEquals("Expected custom formatted date to be deserialized correctly", customDate, deserializedDate);
    }

    @Test
    public void testNullSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<String> nullSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
        SerializerProvider provider = mapper.getSerializationConfig().getProvider();
        String nullValue = null;
        JsonNode node = nullSerializer.serialize(nullValue, new JsonGenerator(provider.getOutputFactory()), provider);
        assertNull("Expected null to return null node", node);
    }

    @Test
    public void testUnregisteredSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<String> unregisteredSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
        SerializerProvider provider = mapper.getSerializationConfig().getProvider();
        String unregisteredValue = "test";
        JsonNode node = unregisteredSerializer.serialize(unregisteredValue, new JsonGenerator(provider.getOutputFactory()), provider);
        assertSame("Expected unregistered serializer to return the default string serializer", mapper.getSerializerProvider().findValueSerializer(String.class), node.getSerializer());
    }

    @Test
    public void testTimestampFormatSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                if (_customFormat != null) {
                    throw new UnsupportedOperationException("Cannot change format once set");
                }
                return new DateTimeSerializerBase<Date>(Date.class, false, timestamp != null ? new StdDateFormat(JsonFormat.Value.datetimeFormat(new JsonFormat.Shape.STRING(), null, null, null)) : null) {
                    @Override
                    protected long _timestamp(Date value) {
                        return value.getTime();
                    }
                };
            }
        });
        mapper.registerModule(module);

        JsonFormat.Value format = new JsonFormat.Value() {
            @JsonFormat.SerializedName("shape")
            private JsonFormat.Shape shape = JsonFormat.Shape.STRING;

            @JsonFormat.SerializedName("format")
            private String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        };

        DateTimeSerializerBase<Date> timestampSerializer = mapper.findValueSerializer(Date.class, mapper.getSerializationConfig(), null, format);

        Date date = new Date(System.currentTimeMillis());
        JsonNode node = timestampSerializer.serialize(date, new JsonGenerator(mapper.getOutputFactory()), mapper.getSerializationConfig().getProvider());
        String serializedDate = node.textValue();
        Date deserializedDate = new SimpleDateFormat(format.getFormat()).parse(serializedDate);
        assertEquals("Expected serialized date in custom format to be deserialized correctly", date, deserializedDate);
    }

    @Test
    public void testCustomFormatDeserialization() throws JsonProcessingException, JsonMappingException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new DateTimeDeserializer() {
            @Override
            public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS zzz");
                try {
                    return customFormat.parse(p.getText());
                } catch (ParseException e) {
                    throw ctxt.wrappedException(e);
                }
            }
        });
        mapper.registerModule(module);

        String jsonString = "\"25-Jun-2017 15:45:34.567 EDT\"";
        Date deserializedDate = mapper.readValue(jsonString, Date.class);
        assertEquals("Expected custom formatted date to be deserialized correctly", new Date(System.currentTimeMillis()), deserializedDate);
    }
}