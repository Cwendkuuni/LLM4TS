import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.core.SerializableString;

public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstructor() {
        ObjectIdGenerator generator = new ObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
    }

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator generator = new ObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        when(w.alwaysAsId).thenReturn(false);
        when(gen.canWriteObjectId()).thenReturn(true);

        assertTrue(writableObjectId.writeAsId(gen, provider, w));

        // Test with null id and idWritten as true
        writableObjectId.idWritten = true;
        writableObjectId.id = null;
        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator generator = new ObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();
        Object id = writableObjectId.generateId(forPojo);
        assertNotNull(id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator generator = new ObjectIdGenerator();
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);

        writableObjectId.writeAsField(gen, provider, w);
        verify(gen, times(1)).writeFieldName(anyString());
        verify(w, times(1)).serializer.serialize(writableObjectId.id, gen, provider);
    }
}