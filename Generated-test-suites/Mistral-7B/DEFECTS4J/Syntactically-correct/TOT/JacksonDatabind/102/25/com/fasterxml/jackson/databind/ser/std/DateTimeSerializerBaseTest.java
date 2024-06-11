import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseWithDefaultSettings() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);
        Date date = new Date();

        // Serialize using default settings
        JsonNode serializedDefault = mapper.writeValueAsTree(date);
        Assertions.assertEquals("\"\\/Date(<?= date.getTime() >)\"", serializedDefault.asText());

        // Deserialize using default settings
        Date deserializedDefault = mapper.readValue(serializedDefault.asText(), Date.class);
        Assertions.assertEquals(date, deserializedDefault);

        // Serialize and deserialize using custom settings (custom format, locale, and timezone)
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        ObjectNode serializedCustom = mapper.writeValueAsTree(date, customFormat);
        Date deserializedCustom = mapper.readValue(serializedCustom, Date.class);
        Assertions.assertEquals(date, deserializedCustom);
        Assertions.assertEquals("2023-03-22 14:30:21 UTC", customFormat.format(deserializedCustom));
    }

    @Test
    public void testDateTimeSerializerBaseWithUseTimestampFlag() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);

        // Serialize using useTimestamp flag
        DateTimeSerializerBase<Date> withUseTimestamp = dateTimeSerializerBase.withFormat(true, null);
        Date date = new Date();
        JsonNode serializedWithUseTimestamp = mapper.writeValueAsTree(date, withUseTimestamp);
        Assertions.assertEquals("\"\\/Date(<?= date.getTime() >)\"", serializedWithUseTimestamp.asText());

        // Deserialize using useTimestamp flag
        Date deserializedWithUseTimestamp = mapper.readValue(serializedWithUseTimestamp.asText(), Date.class);
        Assertions.assertEquals(date, deserializedWithUseTimestamp);

        // Verify that the customFormat is not used when useTimestamp is true
        DateTimeSerializerBase<Date> withCustomFormat = dateTimeSerializerBase.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US));
        ObjectNode serializedWithCustomFormat = mapper.writeValueAsTree(date, withCustomFormat);
        Assertions.assertEquals("\"\\/Date(<?= date.getTime() >)\"", serializedWithCustomFormat.asText());
    }

    @Test
    public void testDateTimeSerializerBaseWithNullCustomFormat() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);

        // Serialize using null customFormat
        DateTimeSerializerBase<Date> withNullCustomFormat = dateTimeSerializerBase.withFormat(false, null);
        Date date = new Date();
        JsonNode serializedWithNullCustomFormat = mapper.writeValueAsTree(date, withNullCustomFormat);
        Assertions.assertEquals("\"\\/Date(<?= date.getTime() >)\"", serializedWithNullCustomFormat.asText());

        // Deserialize using null customFormat
        Date deserializedWithNullCustomFormat = mapper.readValue(serializedWithNullCustomFormat.asText(), Date.class);
        Assertions.assertEquals(date, deserializedWithNullCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithInvalidCustomFormat() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);

        // Serialize and deserialize using invalid custom format (pattern does not match the date format)
        SimpleDateFormat invalidFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        ObjectNode serializedWithInvalidFormat = mapper.writeValueAsTree(new Date(), dateTimeSerializerBase.withFormat(false, invalidFormat));
        Assertions.assertThrows(JsonMappingException.class, () -> mapper.readValue(serializedWithInvalidFormat.asText(), Date.class));
    }

    @Test
    public void testDateTimeSerializerBaseWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);

        // Serialize and deserialize null value
        JsonNode serializedNull = mapper.writeValueAsTree(null);
        Date deserializedNull = mapper.readValue(serializedNull.asText(), Date.class);
        Assertions.assertNull(deserializedNull);
    }

    @Test
    public void testDateTimeSerializerBaseWithEmptyValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<String> stringSerializerBase = mapper.findValueSerializer(String.class);
        String emptyString = "";

        // Serialize and deserialize empty string value
        JsonNode serializedEmptyString = mapper.writeValueAsTree(emptyString, stringSerializerBase);
        String deserializedEmptyString = mapper.readValue(serializedEmptyString.asText(), String.class);
        Assertions.assertEquals(emptyString, deserializedEmptyString);
    }
}