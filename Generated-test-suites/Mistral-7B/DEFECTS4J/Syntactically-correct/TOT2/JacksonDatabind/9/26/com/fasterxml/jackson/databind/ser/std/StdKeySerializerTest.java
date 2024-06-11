import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StdKeySerializerTest {

    @Test
    public void testSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String strKey = "testString";
        Date dateKey = new Date();

        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), com.fasterxml.jackson.core.util.DefaultPreprendableWriter.None_INSTANCE);
        SerializerProvider provider = mapper.getSerializerProvider();

        serializer.serialize(strKey, jgen, provider);
        jgen.flush();

        byte[] strKeyJson = jgen.getOutputBuffer().toByteArray();
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(strKeyJson));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals(strKey, parser.getText());

        jgen.reset();
        serializer.serialize(dateKey, jgen, provider);
        jgen.flush();

        byte[] dateKeyJson = jgen.getOutputBuffer().toByteArray();
        parser = mapper.getFactory().createParser(new ByteArrayInputStream(dateKeyJson));
        token = parser.nextToken();
        assertTrue(token.isValueInstanceOf(Date.class));
        assertEquals(dateKey.getClass(), parser.getValueAsToken().getValueClass());
    }
}