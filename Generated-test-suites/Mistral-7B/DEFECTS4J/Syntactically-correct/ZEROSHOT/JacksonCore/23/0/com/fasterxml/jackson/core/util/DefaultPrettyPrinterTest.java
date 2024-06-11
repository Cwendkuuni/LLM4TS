import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    private JsonGenerator mockJsonGenerator;
    private DefaultPrettyPrinter defaultPrettyPrinter;

    @Before
    public void setUp() {
        mockJsonGenerator = mock(JsonGenerator.class);
        defaultPrettyPrinter = new DefaultPrettyPrinter();
    }

    @After
    public void tearDown() {
        reset(mockJsonGenerator);
    }

    @Test
    public void testConstructorWithNullRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNull(prettyPrinter.getRootSeparator());
    }

    @Test
    public void testConstructorWithRootSeparator() {
        String rootSeparator = "RootSeparator";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        assertEquals(rootSeparator, prettyPrinter.getRootSeparator().asString());
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        String rootSeparator = defaultPrettyPrinter.getRootSeparator() != null ? defaultPrettyPrinter.getRootSeparator().asString() : "";
        assertEquals("", rootSeparator);
        defaultPrettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("");
    }

    @Test
    public void testWriteStartObject() throws IOException {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeStartObject(mockJsonGenerator);
        assertEquals("{", defaultPrettyPrinter.getLastWritten());
        verify(mockJsonGenerator).writeRaw("{");
    }

    @Test
    public void testWriteEndObject() throws IOException {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeStartObject(mockJsonGenerator);
        defaultPrettyPrinter.writeEndObject(mockJsonGenerator, 1);
        assertEquals("}", defaultPrettyPrinter.getLastWritten());
        verify(mockJsonGenerator).writeRaw("}");
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        assertEquals("", defaultPrettyPrinter.getLastWritten());
        defaultPrettyPrinter.withRootSeparator(new SerializedString(":"));
        defaultPrettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        assertEquals(":", defaultPrettyPrinter.getLastWritten());
        verify(mockJsonGenerator).writeRaw(":");
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeStartObject(mockJsonGenerator);
        defaultPrettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);
        assertEquals("", defaultPrettyPrinter.getLastWritten());
        defaultPrettyPrinter.withRootSeparator(new SerializedString(" : "));
        defaultPrettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);
        assertEquals(" : ", defaultPrettyPrinter.getLastWritten());
        verify(mockJsonGenerator).writeRaw(" : ");
    }

    @Test
    public void testWriteStartArray() throws IOException {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeStartArray(mockJsonGenerator);
        assertEquals("[", defaultPrettyPrinter.getLastWritten());
        verify(mockJsonGenerator).writeRaw("[");
    }

    @Test
    public void testWriteEndArray() throws IOException {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeStartArray(mockJsonGenerator);
        defaultPrettyPrinter.writeEndArray(mockJsonGenerator, 1);
        assertEquals("]", defaultPrettyPrinter.getLastWritten());
        verify(mockJsonGenerator).writeRaw("]");
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeArrayValueSeparator(mockJsonGenerator);
        assertEquals("", defaultPrettyPrinter.getLastWritten());
        defaultPrettyPrinter.withRootSeparator(new SerializedString(", "));
        defaultPrettyPrinter.writeArrayValueSeparator(mockJsonGenerator);
        assertEquals(", ", defaultPrettyPrinter.getLastWritten());
        verify(mockJsonGenerator).writeRaw(", ");
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = mock(Indenter.class);
        prettyPrinter.indentArraysWith(indenter);
        assertEquals(indenter, prettyPrinter.getArrayIndenter());
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = mock(Indenter.class);
        prettyPrinter.indentObjectsWith(indenter);
        assertEquals(indenter, prettyPrinter.getObjectIndenter());
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withSpacesInObjectEntries();
        assertTrue(prettyPrinter.isSpacesInObjectEntries());
    }

    @Test
    public void testWithoutSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withoutSpacesInObjectEntries();
        assertFalse(prettyPrinter.isSpacesInObjectEntries());
    }

    @Test
    public void testGetLastWritten() {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter.writeStartObject(mockJsonGenerator);
        defaultPrettyPrinter.writeObjectFieldName("fieldName");
        defaultPrettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        defaultPrettyPrinter.writeObjectValue("value");
        defaultPrettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);
        defaultPrettyPrinter.writeEndObject(mockJsonGenerator, 1);
        assertEquals("{\n  \"fieldName\":\n  \"value\"\n}", defaultPrettyPrinter.getLastWritten());
        verify(mockJsonGenerator, times(5)).writeRaw(anyString());
    }

    @Test
    public void testGetArrayValueSeparatorWithSpaces() {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.withSeparators(new Separators(",", " : "));
        assertEquals(", ", defaultPrettyPrinter.getArrayValueSeparator());
    }

    @Test
    public void testGetObjectFieldValueSeparatorWithSpaces() {
        when(mockJsonGenerator.writeRaw(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.withSeparators(new Separators(",", " : "));
        assertEquals(" : ", defaultPrettyPrinter.getObjectFieldValueSeparator());
    }
}