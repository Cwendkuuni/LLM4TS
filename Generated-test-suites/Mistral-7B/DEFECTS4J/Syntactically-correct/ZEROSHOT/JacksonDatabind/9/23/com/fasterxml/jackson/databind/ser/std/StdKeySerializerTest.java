import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.Types;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException, IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

        serializer.serialize(date, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(Mockito.anyString());
        verify(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
    }

    @Test
    public void testSerializeString() throws JsonProcessingException, IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        ObjectMapper mapper = new ObjectMapper();
        String key = "testKey";

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        serializer.serialize(key, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(key);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        JavaType javaType = Mockito.mock(JavaType.class);

        serializer.getSchema(Mockito.mock(SerializerProvider.class), Types.NULL);

        verify(visitor).expectStringFormat(javaType);
    }
}