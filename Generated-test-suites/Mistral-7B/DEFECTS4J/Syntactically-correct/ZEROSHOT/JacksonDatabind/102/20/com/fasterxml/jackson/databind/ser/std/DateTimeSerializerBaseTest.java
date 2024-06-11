import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateTimeSerializerBaseTest {

    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private JsonGenerator jsonGenerator;

    @Before
    public void setUp() throws Exception {
        serializerProvider = new SerializerProvider();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializerProvider(serializerProvider);
        jsonParser = objectMapper.getFactory().createParser("{}");
        jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setLocale(locale);
        dateFormat.setTimeZone(timeZone);

        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, dateFormat) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<Date>(Date.class, timestamp, customFormat) {
                    @Override
                    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        _serializeAsString(value, gen, serializers);
                    }
                };
            }
        };

        JsonFormat jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(shape);
        jsonFormat.setLocale(locale);
        jsonFormat.setTimeZone(timeZone);
        when(serializerProvider.findFormatOverrides(eq(Date.class), any(), any())).thenReturn(jsonFormat);

        JsonSerializer<Date> jsonSerializer = dateTimeSerializerBase.createContextual(serializerProvider, null);

        assertEquals(dateTimeSerializerBase.getClass(), jsonSerializer.getClass());
        verify(serializerProvider, times(1)).findFormatOverrides(eq(Date.class), any(), any());
    }

    @Test
    public void testSerialize() throws Exception {
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setLocale(locale);
        dateFormat.setTimeZone(timeZone);

        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, dateFormat) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<Date>(Date.class, timestamp, customFormat) {
                    @Override
                    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        _serializeAsString(value, gen, serializers);
                    }
                };
            }
        };

        Date date = new Date(1562981600000L);
        String expectedOutput = "2019-06-13T00:00:00.000Z";

        dateTimeSerializerBase.serialize(date, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String actualOutput = jsonParser.getValueAsString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIsEmpty() throws Exception {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public boolean isEmpty(SerializerProvider serializers, Long value) {
                return value == null;
            }
        };

        assertEquals(false, dateTimeSerializerBase.isEmpty(serializerProvider, null));
        assertEquals(true, dateTimeSerializerBase.isEmpty(serializerProvider, nullL()));
    }

    @Test
    public void testGetSchema() throws Exception {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return createSchemaNode("string", true);
            }
        };

        JsonNode jsonNode = dateTimeSerializerBase.getSchema(serializerProvider, Date.class);

        assertEquals("string", jsonNode.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                visitor.visitStringFormat(null, JsonValueFormat.DATE_TIME);
            }
        };

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, Date.class);

        verify(jsonFormatVisitorWrapper, times(1)).visitStringFormat(eq(null), eq(JsonValueFormat.DATE_TIME));
    }

    @Test
    public void testCreateSerializerWithDefaultSerializer() throws Exception {
        DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }
        };

        assertEquals(StdSerializer.Instance.class, dateTimeSerializerBase.getSerializerProvider().findValueSerializer(String.class).getClass());
    }

    @Test
    public void testCreateSerializerWithCustomSerializer() throws Exception {
        class CustomSerializer extends StdSerializer<String> {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("custom");
            }
        }
        DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<String>(String.class, timestamp, customFormat) {
                    @Override
                    public JsonSerializer<String> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                        return new CustomSerializer();
                    }
                };
            }
        };

        assertEquals(CustomSerializer.class, dateTimeSerializerBase.createContextual(serializerProvider, null).getClass());
    }

    @Test
    public void testCreateSerializerWithJsonFormatAnnotation() throws Exception {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        class TestClass {
            private Date date;
        }

        DateTimeSerializerBase<Date> dateTimeSerializerBase
                = new DateTimeSerializerBase<Date>(Date.class, false, null);

        TestClass testClass = new TestClass();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Date> jsonSerializer = objectMapper.findValueSerializer(Date.class);

        ObjectWriter objectWriter = objectMapper.writerWithView(new SimpleBeanPropertyView(TestClass.class));

        String expectedOutput = "13-06-2019 00:00:00";
        String actualOutput = objectWriter.writeValueAsString(testClass);

        assertEquals(expectedOutput, actualOutput);

        JsonFormat jsonFormat = objectMapper.getSerializationConfig().findValueSerializers(Date.class, null)[0].getSerializerConfig().getFormatOverrides();
        assertEquals("dd-MM-yyyy HH:mm:ss", jsonFormat.getPattern());
    }

    @Test
    public void testCreateSerializerWithCustomFormatAnnotation() throws Exception {
        @JsonFormat(shape = JsonFormat.Shape.STRING, location = JsonFormat.Location.ABSOLUTE, timezone = "UTC")
        class TestClass {
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            private Date date;
        }

        DateTimeSerializerBase<Date> dateTimeSerializerBase
                = new DateTimeSerializerBase<Date>(Date.class, false, null);

        TestClass testClass = new TestClass();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Date> jsonSerializer = objectMapper.findValueSerializer(Date.class);

        ObjectWriter objectWriter = objectMapper.writerWithView(new SimpleBeanPropertyView(TestClass.class));

        String expectedOutput = "2019-06-13T00:00:00.000Z";
        String actualOutput = objectWriter.writeValueAsString(testClass);

        assertEquals(expectedOutput, actualOutput);

        JsonFormat jsonFormat = objectMapper.getSerializationConfig().findValueSerializers(Date.class, null)[0].getSerializerConfig().getFormatOverrides();
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", jsonFormat.getPattern());
        assertEquals(StdDateFormat.CloseStyle.UTC, jsonFormat.getCloseStyle());
    }
}