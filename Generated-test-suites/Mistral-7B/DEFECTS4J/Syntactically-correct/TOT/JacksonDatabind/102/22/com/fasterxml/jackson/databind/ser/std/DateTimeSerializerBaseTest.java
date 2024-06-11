package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseBasic() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        Date date = new Date(System.currentTimeMillis());
        String json = "";

        SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out);

        serializer.serialize(date, gen, serializers);
        gen.flush();

        json = mapper.writeValueAsString(date);
        JsonParser parser = mapper.getFactory().createParser(json);

        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBaseCustomFormat() throws IOException, JsonMappingException, ParseException {
        ObjectMapper mapper = JsonMapperBuilder.builder()
                .serializationFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();

        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        Date date = new Date(System.currentTimeMillis());
        String json = "";

        SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out);

        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        TimeZone customTimeZone = TimeZone.getTimeZone("UTC");
        customFormat.setTimeZone(customTimeZone);
        serializer = serializer.withFormat(Boolean.FALSE, customFormat);

        serializer.serialize(date, gen, serializers);
        gen.flush();

        json = mapper.writeValueAsString(date);
        JsonParser parser = mapper.getFactory().createParser(json);

        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseInvalidCustomFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        Date date = new Date(System.currentTimeMillis());

        SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();

        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        customFormat.setTimeZone(null);

        serializer = serializer.withFormat(Boolean.FALSE, customFormat);

        try {
            serializer.serialize(date, null, serializers);
            fail("Expected JsonMappingException");
        } catch (JsonGenerator.Exception e) {
            throw new JsonMappingException(e.getLocalizedMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseSerializationSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), Date.class);

        assertEquals(JsonNodeType.STRING, schema.getType());
    }
}