import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new DateTimeSerializerBase.DateLocalDateTimeSerializer(false, null));
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSerializingLocalDateTime() throws JsonGenerationException, JsonMappingException {
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        String json = mapper.writeValueAsString(dateTime);
        JsonNode node = mapper.readTree(json);
        Assertions.assertEquals("\"2022-01-01T12:00:00.000Z\"", node.asText());
    }

    @Test
    public void testSerializingLocalDateTimeWithCustomFormat() throws JsonGenerationException, JsonMappingException {
        TimeZone timeZone = TimeZone.getTimeZone("UTC+1");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .withLocale(Locale.ENGLISH)
                .withZone(timeZone);

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 13, 0);
        String json = mapper.writeValueAsString(dateTime);
        JsonNode node = mapper.readTree(json);
        Assertions.assertEquals(new TextNode("\"2022-01-01 14:00:00\""), node.get("value"));

        // check serializer uses custom format
        DateTimeSerializerBase serializer = mapper.findValueSerializer(LocalDateTime.class);
        Assertions.assertInstanceOf(DateTimeSerializerBase.DateLocalDateTimeSerializer.class, serializer);
        DateTimeSerializerBase.DateLocalDateTimeSerializer customSerializer = (DateTimeSerializerBase.DateLocalDateTimeSerializer) serializer;
        Assertions.assertEquals(timeZone, customSerializer._customFormat.getTimeZone());
        Assertions.assertEquals(formatter.toPattern(), customSerializer._customFormat.toPattern());
    }

    @Test
    public void testSerializingNullLocalDateTime() throws JsonGenerationException, JsonMappingException {
        String json = mapper.writeValueAsString(null);
        JsonNode node = mapper.readTree(json);
        Assertions.assertEquals(JsonNode.ValueType.NULL, node.getValueType());
    }
}