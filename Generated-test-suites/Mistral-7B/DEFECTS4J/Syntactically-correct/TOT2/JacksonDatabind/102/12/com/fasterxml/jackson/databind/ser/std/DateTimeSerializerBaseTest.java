import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> dateTimeSerializer;
    private DateTimeSerializerBase<String> dateTimeSerializerString;
    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        serializerProvider = new SerializerProvider();
        objectMapper = new ObjectMapper();
        dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        dateTimeSerializerString = new DateTimeSerializerBase<String>(String.class, false, null);
    }

    // Test for basic functionality of the DateTimeSerializerBase with default settings
    @Test
    public void testSerializeDate() throws JsonGenerationException, JsonMappingException, IOException {
        Date date = new Date();
        ObjectNode result = objectMapper.writeValueAsTree(date, serializerProvider);
        Assert.assertEquals(formatDate(date), result.get("@json.special.value").asText());
    }

    // Test for serializing as timestamp with default settings
    @Test
    public void testSerializeTimestamp() throws JsonGenerationException, JsonMappingException, IOException {
        Long timestamp = Instant.now().getEpochSecond();
        ObjectNode result = objectMapper.writeValueAsTree(timestamp, serializerProvider);
        Assert.assertEquals(Long.toString(timestamp), result.get("@json.special.value").asText());
    }

    // Test for serializing with custom DateFormat
    @Test
    public void testSerializeCustomDateFormat() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        DateTimeSerializerBase<Date> dateTimeSerializerWithFormat = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        Date date = new Date();
        ObjectNode result = objectMapper.writeValueAsTree(date, serializerProvider);
        Assert.assertEquals(customFormat.format(date), result.get("@json.special.value").asText());
    }

    // Test for serializing with Locale and TimeZone
    @Test
    public void testSerializeCustomLocaleAndTimeZone() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        customFormat.setLocale(Locale.FRENCH);
        customFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        DateTimeSerializerBase<Date> dateTimeSerializerWithFormat = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        Date date = new Date();
        ObjectNode result = objectMapper.writeValueAsTree(date, serializerProvider);
        Assert.assertEquals(customFormat.format(date), result.get("@json.special.value").asText());
        Assert.assertEquals("Europe/Paris", result.get("@json.special.timezone").asText());
    }

    // Test for deserializing with default settings
    @Test
    public void testDeserializeDate() throws JsonGenerationException, JsonMappingException, IOException {
        Date date = new Date();
        String jsonString = formatDate(date);
        ObjectNode input = objectMapper.readTree(jsonString);
        Date deserializedDate = objectMapper.treeToValue(input, Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    // Test for deserializing with custom DateFormat
    @Test
    public void testDeserializeCustomDateFormat() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        DateTimeSerializerBase<Date> dateTimeSerializerWithFormat = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        DateFormat jsonFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        String jsonString = jsonFormat.format(new Date());
        ObjectNode input = objectMapper.readTree(jsonString);
        Date deserializedDate = objectMapper.treeToValue(input, Date.class);
        Assert.assertEquals(new Date(), deserializedDate);
    }

    // Test for deserializing with Locale and TimeZone
    @Test
    public void testDeserializeCustomLocaleAndTimeZone() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        customFormat.setLocale(Locale.FRENCH);
        customFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        DateTimeSerializerBase<Date> dateTimeSerializerWithFormat = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        DateFormat jsonFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        jsonFormat.setLocale(Locale.FRENCH);
        jsonFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        String jsonString = jsonFormat.format(new Date());
        ObjectNode input = objectMapper.readTree(jsonString);
        Date deserializedDate = objectMapper.treeToValue(input, Date.class);
        Assert.assertEquals(new Date(), deserializedDate);
    }

    // Test for deserializing with custom DateFormat and handling missing timezone info
    @Test
    public void testDeserializeCustomDateFormatWithMissingTimezone() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Date> dateTimeSerializerWithFormat = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        String jsonString = "2023-03-01 12:30:45";
        ObjectNode input = objectMapper.readTree(jsonString);
        Date deserializedDate = objectMapper.treeToValue(input, Date.class);
        Assert.assertNotNull(deserializedDate);
    }

    // Test for deserializing with custom DateFormat and handling null input
    @Test(expected = JsonMappingException.class)
    public void testDeserializeCustomDateFormatWithNullInput() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        DateTimeSerializerBase<Date> dateTimeSerializerWithFormat = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        ObjectNode input = objectMapper.createObjectNode();
        objectMapper.treeToValue(input, Date.class);
    }

    // Test for serializing and deserializing with empty value
    @Test
    public void testSerializeAndDeserializeEmptyValue() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectNode emptyValue = objectMapper.createObjectNode();
        ObjectNode result = objectMapper.writeValueAsTree(emptyValue, serializerProvider);
        ObjectNode deserializedValue = objectMapper.readTree(result.get("@json.special.value").asText());
        Assert.assertEquals(emptyValue, deserializedValue);
    }

    private static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        return formatter.format(date);
    }

    private static ZonedDateTime toZonedDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault());
    }
}