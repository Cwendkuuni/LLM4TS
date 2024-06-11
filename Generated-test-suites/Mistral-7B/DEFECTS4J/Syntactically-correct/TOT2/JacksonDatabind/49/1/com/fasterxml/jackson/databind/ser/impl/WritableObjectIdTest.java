
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.SerializableStringSerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase() {};
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
    }

    @Test
    public void testWriteAsIdHappyPath() throws IOException {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(any())).thenReturn("happy-path-id");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        ObjectIdWriter w = new ObjectIdWriter() {};
        writableObjectId.writeAsId(gen, mock(SerializerProvider.class), w);
        verify(gen, times(1)).writeObjectId("happy-path-id");
    }

    @Test
    public void testWriteAsIdJsonGeneratorCannotWriteObjectIds() throws IOException {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(any())).thenReturn("happy-path-id");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(false);
        ObjectIdWriter w = new ObjectIdWriter() {};
        writableObjectId.writeAsId(gen, mock(SerializerProvider.class), w);
        verify(gen, never()).writeObjectId(anyString());
        verify(w, never()).serializer;
    }

    @Test
    public void testGenerateIdObjectHasExistingId() throws IOException {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(any())).thenReturn("generated-id");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object obj = new Object() {
            public Object getId() {
                return "existing-id";
            }
        };
        Object id = writableObjectId.generateId(obj);
        assertEquals("existing-id", id);
    }

    @Test
    public void testGenerateIdObjectHasNoId() throws IOException {
        ObjectIdGenerator<Object> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(any())).thenReturn("generated-id");
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object id = writableObjectId.generateId(new Object());
        assertNotNull(id);
    }

    @Test
    public void testWriteAsFieldJsonGeneratorCanWriteObjectIds() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase() {};
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(true);
        ObjectIdWriter w = new ObjectIdWriter() {};
        writableObjectId.writeAsField(gen, mock(SerializerProvider.class), w);
        verify(gen, times(1)).writeObjectId(anyString());
    }

    @Test
    public void testWriteAsFieldJsonGeneratorCannotWriteObjectIds() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase() {};
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.canWriteObjectId()).thenReturn(false);
        ObjectIdWriter w = new ObjectIdWriter() {};
        writableObjectId.writeAsField(gen, mock(SerializerProvider.class), w);
        verify(gen, never()).writeObjectId(anyString());
        verify(w, never()).serializer;
    }

    @Test
    public void testWriteAsFieldWithPropertyName() throws IOException {
        ObjectIdGenerator<Object> generator = new ObjectIdGeneratorBase() {};
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        JsonGenerator gen = mock(JsonGenerator.class);
        ObjectIdWriter w = new ObjectIdWriter() {};
        when(w.propertyName).thenReturn("propertyName");
        writableObjectId.writeAsField(gen, mock(SerializerProvider.class), w);
        verify(gen, times(1)).writeFieldName("propertyName");
    }
}