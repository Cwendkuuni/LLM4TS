import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSerializationWithDefaultFormat() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        Date date = new Date();
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(dateTimeSerializer).getSerializerProvider().getJsonGenerator();
        dateTimeSerializer.serialize(date, jsonGenerator, null);
        TextNode actual = objectMapper.readValue(jsonGenerator.getOutputStream(), TextNode.class);
        Date parsedDate = objectMapper.readValue(new JsonParser(new StringReader(actual.asText())), Date.class);
        Assert.assertEquals(date, parsedDate);
    }

    @Test
    public void testSerializationWithCustomFormat() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Date date = new Date();
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(dateTimeSerializer).getSerializerProvider().getJsonGenerator();
        dateTimeSerializer.serialize(date, jsonGenerator, null);
        TextNode actual = objectMapper.readValue(jsonGenerator.getOutputStream(), TextNode.class);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = formatter.parse(actual.asText());
        Assert.assertEquals(date, parsedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithCustomFormatButWrongLocale() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Date date = new Date();
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(dateTimeSerializer).getSerializerProvider().getJsonGenerator();
        dateTimeSerializer.serialize(date, jsonGenerator, null);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLocale(Locale.ITALIAN);
        formatter.parse("2022-01-01 12:00:00");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithCustomFormatButWrongTimeZone() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Date date = new Date();
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializer(dateTimeSerializer).getSerializerProvider().getJsonGenerator();
        dateTimeSerializer.serialize(date, jsonGenerator, null);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        formatter.parse("2022-01-01 12:00:00");
    }

    @Test
    public void testDeserializationWithDefaultFormat() throws IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        String dateString = "2022-01-01T12:00:00.000Z";
        Date date = objectMapper.readValue(new TextNode(dateString), Date.class);
        Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString), date);
    }

    @Test
    public void testDeserializationWithCustomFormat() throws IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String dateString = "2022-01-01 12:00:00";
        Date date = objectMapper.readValue(new TextNode(dateString), Date.class);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Assert.assertEquals(formatter.parse(dateString), date);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithCustomFormatButWrongLocale() throws IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String dateString = "2022-01-01 12:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLocale(Locale.ITALIAN);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader(dateString));
        jsonParser.setCodec(objectMapper);
        jsonParser.nextToken();
        dateTimeSerializer.deserialize(JsonToken.VALUE_STRING, jsonParser, null, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithCustomFormatButWrongTimeZone() throws IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String dateString = "2022-01-01 12:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader(dateString));
        jsonParser.setCodec(objectMapper);
        jsonParser.nextToken();
        dateTimeSerializer.deserialize(JsonToken.VALUE_STRING, jsonParser, null, null, null);
    }
}