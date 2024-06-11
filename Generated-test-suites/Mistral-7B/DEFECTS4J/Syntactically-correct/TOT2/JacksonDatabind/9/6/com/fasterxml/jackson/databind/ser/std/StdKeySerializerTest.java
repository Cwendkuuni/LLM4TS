import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        Date date = new Date();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(date, jgen, provider);
        verify(jgen).writeFieldName(date.toString());
        verify(jgen).defaultSerializeDateKey(date, jgen);
    }

    @Test
    public void testSerializeString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        String value = "test";

        serializer.serialize(value, jgen, provider);
        verify(jgen).writeFieldName(value);
    }
}