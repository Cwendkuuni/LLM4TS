import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDateTimeSerializerBaseSerializesDate() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        Date deserializedDate = mapper.readValue(json, Date.class);
        assertEquals(new Date(), deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBaseUsesCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z"));

        String json = mapper.writerWithDefaultPrettyPrinter()
                .withSerializationFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        Date deserializedDate = mapper.readValue(json, Date.class);
        assertEquals(new Date(), deserializedDate);
        assertEquals("dd-MM-yyyy HH:mm:ss z", dateFormat.format(deserializedDate));
    }

    @Test
    public void testDateTimeSerializerBaseUsesDefaultFormatIfCustomFormatIsNull() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        Date deserializedDate = mapper.readValue(json, Date.class);
        assertEquals(new Date(), deserializedDate);

        // Default format is "EEE MMM dd HH:mm:ss zzz yyyy"
        SimpleDateFormat defaultDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        assertEquals("Sun Mar 21 12:13:45 PDT 2023", defaultDateFormat.format(deserializedDate));
    }

    @Test
    public void testDateTimeSerializerBaseUsesThreadLocalCustomFormatInstance() throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        DateTimeSerializerBase<Date> sameSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat firstFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        SimpleDateFormat secondFormat = null;

        // Reuse first format instance
        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        secondFormat = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect the same format instance
        assertEquals(firstFormat, secondFormat);

        // Reuse same format instance again
        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        SimpleDateFormat thirdFormat = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect the same format instance
        assertEquals(firstFormat, thirdFormat);

        // Check that new serializer has null format
        assertEquals(null, sameSerializer._reusedCustomFormat.get());

        // Let's check that new format instance is created when _reusedCustomFormat is null
        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat fourthFormat = null;

        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        fourthFormat = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect a new instance
        assertNull(fourthFormat);
    }

    @Test
    public void testDateTimeSerializerBaseHandlesNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(null);

        assertNull(mapper.readValue(json, Date.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseThrowsJsonMappingExceptionIfCustomFormatIsInvalid()
            throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz"));

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        mapper.readValue(json, Date.class);
    }

    @Test
    public void testDateTimeSerializerBaseUsesThreadLocalCustomFormatInstanceWithLocale() throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        DateTimeSerializerBase<Date> sameSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat firstFormatEN = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat secondFormatDE = null;

        // Reuse first format instance with German locale
        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        secondFormatDE = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect the same format instance
        assertEquals(firstFormatEN, secondFormatDE);

        // Reuse same format instance again
        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        SimpleDateFormat thirdFormatEN = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect the same format instance
        assertEquals(firstFormatEN, thirdFormatEN);

        // Check that new serializer has null format
        assertEquals(null, sameSerializer._reusedCustomFormat.get());

        // Let's check that new format instance is created when _reusedCustomFormat is null
        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat fourthFormatEN = null;

        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        fourthFormatEN = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect a new instance
        assertNull(fourthFormatEN);

        // Let's check that we can use different locale format instances
        DateTimeSerializerBase<Date> dateTimeSerializerBaseWithLocale = new DateTimeSerializerBase<Date>(Date.class, false,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.GERMAN));

        json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat fifthFormatEN = null;
        SimpleDateFormat sixthFormatDE = null;

        dateTimeSerializerBaseWithLocale.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        fifthFormatEN = dateTimeSerializerBaseWithLocale._reusedCustomFormat.get();

        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        sixthFormatDE = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect different instances
        assertNotEquals(fifthFormatEN, sixthFormatDE);
    }

    @Test
    public void testDateTimeSerializerBaseUsesThreadLocalCustomFormatInstanceWithTimeZone() throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        DateTimeSerializerBase<Date> sameSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat firstFormatUTC = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat secondFormatPT = null;

        // Reuse first format instance with Portugal timezone
        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        secondFormatPT = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect the same format instance
        assertEquals(firstFormatUTC, secondFormatPT);

        // Reuse same format instance again
        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        SimpleDateFormat thirdFormatUTC = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect the same format instance
        assertEquals(firstFormatUTC, thirdFormatUTC);

        // Check that new serializer has null format
        assertEquals(null, sameSerializer._reusedCustomFormat.get());

        // Let's check that new format instance is created when _reusedCustomFormat is null
        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat fourthFormatUTC = null;

        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        fourthFormatUTC = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect a new instance
        assertNull(fourthFormatUTC);

        // Let's check that we can use different timezone format instances
        DateTimeSerializerBase<Date> dateTimeSerializerBaseWithTimeZone = new DateTimeSerializerBase<Date>(Date.class, false,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH), TimeZone.getTimeZone("Europe/Paris"));

        json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat fifthFormatUTC = null;
        SimpleDateFormat sixthFormatPT = null;

        dateTimeSerializerBaseWithTimeZone.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        fifthFormatUTC = dateTimeSerializerBaseWithTimeZone._reusedCustomFormat.get();

        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        sixthFormatPT = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect different instances
        assertNotEquals(fifthFormatUTC, sixthFormatPT);
    }

    @Test
    public void testDateTimeSerializerBaseDoesNotUseThreadLocalCustomFormatInstanceWhenNotSerializingToString() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);

        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");

        Date deserializedDate = mapper.readValue(json, Date.class);
        assertEquals(new Date(), deserializedDate);

        SimpleDateFormat serializedFormat = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect null format
        assertNull(serializedFormat);

        // Let's check that new format instance is created when _reusedCustomFormat is null
        dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);
        json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(new Date());

        SimpleDateFormat newFormat = null;

        dateTimeSerializerBase.serialize(new Date(), mapper.getJsonFactory().createGenerator(null), mapper);
        newFormat = dateTimeSerializerBase._reusedCustomFormat.get();

        // Expect a new instance
        assertNull(newFormat);
    }
}