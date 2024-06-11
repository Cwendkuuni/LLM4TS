package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private static ObjectMapper mapper;

    static {
        mapper = new JsonMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testDateTimeSerializerBaseWithTimestamp() throws IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);
        SerializerProvider serializers = mapper.getSerializerProvider();

        Date date = new Date();
        ObjectNode node = mapper.writeValueAsTree(date, serializers);
        Long timestamp = node.get(0).asLong();
        Assert.assertEquals(date.getTime(), timestamp);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() throws IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, getCustomDateFormat());
        SerializerProvider serializers = mapper.getSerializerProvider();

        Date date = new Date();
        ObjectNode node = mapper.writeValueAsTree(date, serializers);
        String dateString = node.get(0).asText();
        Assert.assertEquals("2023-03-25 10:30:00", dateString);
    }

    @Test
    public void testDateTimeSerializerBaseWithJsonFormat() throws IOException {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        SerializerProvider serializers = mapper.getSerializerProvider();

        Date date = new Date();
        ObjectNode node = mapper.writeValueAsTree(date, serializers);
        String dateString = node.get(0).asText();
        Assert.assertEquals("2023-03-25 10:30:00", dateString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBaseWithNullProvider() throws IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        dateTimeSerializerBase.createContextual(null, null);
    }

    private DateFormat getCustomDateFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df;
    }
}