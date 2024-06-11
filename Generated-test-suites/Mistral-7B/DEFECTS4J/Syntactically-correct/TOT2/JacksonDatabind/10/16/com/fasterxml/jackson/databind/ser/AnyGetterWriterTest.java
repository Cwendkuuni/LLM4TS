import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.io.Tests.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnyGetterWriter anyGetterWriter = mapper.getSerializerProvider().findValueSerializer(Map.class);

        BeanProperty property = new BeanProperty("propertyName", null, null, null);
        AnnotatedMember accessor = new AnnotatedMember("accessorName", null);
        MapSerializer mapSerializer = new MapSerializer(null, new StandardConverter.NullValueSerializer(null), null);
        AnyGetterWriter anyWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = new HashMap<>();
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonEncoding.UTF8);
        SerializerProvider provider = mapper.getSerializerProvider();

        anyWriter.getAndSerialize(bean, gen, provider);
        gen.flush();

        String json = new String(gen.getOutputStream().toByteArray());
        JsonNode actual = mapper.readTree(json);
        JsonNode expected = mapper.readTree("{propertyName: []}");

        assertEquals(expected, actual);
    }
}