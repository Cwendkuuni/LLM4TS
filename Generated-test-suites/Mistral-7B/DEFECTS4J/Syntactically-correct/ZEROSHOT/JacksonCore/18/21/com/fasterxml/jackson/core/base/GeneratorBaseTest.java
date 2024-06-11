package com.fasterxml.jackson.core.base;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    private static class TestGenerator extends GeneratorBase {
        public TestGenerator() {
            super(JsonGenerator.Feature.AUTO_CLOSE_TARGET.getMask() | JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        }

        @Override
        protected void _releaseBuffers() {
            // no-op
        }

        @Override
        protected void _verifyValueWrite(String typeMsg) throws IOException {
            // no-op
        }

        @Override
        protected PrettyPrinter _constructDefaultPrettyPrinter() {
            return null;
        }

        @Override
        public JsonGenerator writeNumber(int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeNumber(long l) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeNumber(double d) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeNumber(float f) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeNumber(BigDecimal dec) throws IOException {
            // no-op
        }

        @Override
        public void writeBoolean(boolean state) throws IOException {
            // no-op
        }

        @Override
        public void writeNull() throws IOException {
            // no-op
        }

        @Override
        public void writeObject(Object value) throws IOException {
            // no-op
        }

        @Override
        public void writeTree(TreeNode rootNode) throws IOException {
            // no-op
        }

        @Override
        public void flush() throws IOException {
            // no-op
        }

        @Override
        public void close() throws IOException {
            // no-op
        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public ObjectCodec getCodec() {
            return null;
        }

        @Override
        public JsonWriteContext getOutputContext() {
            return null;
        }
    }

    @Test
    public void testIsEnabled() throws IOException, JsonProcessingException {
        TestGenerator gen = new TestGenerator();

        assertTrue("Features should be disabled by default", !gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue("Features should be disabled by default", !gen.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue("Features should be disabled by default", !gen.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        assertTrue("Features should be disabled by default", !gen.isEnabled(Feature.AUTO_CLOSE_TARGET));

        gen.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue("Features should be enabled after enable call", gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        gen.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse("Features should be disabled after disable call", gen.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testSetFeatureMask() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), JsonGenerator.Feature.AUTO_CLOSE_TARGET, null);

        int initialFeatures = gen.getFeatureMask();

        gen.setFeatureMask(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        gen.setFeatureMask(JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask());
        gen.setFeatureMask(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.getMask());

        int features = gen.getFeatureMask();
        assertTrue("Features should have been set as expected", (features & JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask()) != 0);
        assertTrue("Features should have been set as expected", (features & JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask()) != 0);
        assertTrue("Features should have been set as expected", (features & JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.getMask()) != 0);

        // check that initial state is restored
        assertTrue("Initial state should be restored after setFeatureMask call", (gen.getFeatureMask() & initialFeatures) == initialFeatures);
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws IOException {
        TestGenerator gen = new TestGenerator();

        assertNull("UseDefaultPrettyPrinter should return null by default", gen.getPrettyPrinter());

        gen.useDefaultPrettyPrinter();
        assertNull("UseDefaultPrettyPrinter should still return null after first call", gen.getPrettyPrinter());

        gen.useDefaultPrettyPrinter();
        assertNotNull("UseDefaultPrettyPrinter should return a DefaultPrettyPrinter instance after second call", gen.getPrettyPrinter());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedWriteMethods() throws IOException {
        TestGenerator gen = new TestGenerator();

        gen.writeNumber(1);
        gen.writeNumber(1L);
        gen.writeNumber(1.0);
        gen.writeNumber(1.0f);
        gen.writeNumber(new BigDecimal("1"));
        gen.writeBoolean(true);
        gen.writeNull();
    }

    @Test
    public void testWriteString() throws IOException {
        TestGenerator gen = new TestGenerator();

        gen.writeString("Hello World");
        assertEquals("Hello World", gen.getCurrentValue());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        TestGenerator gen = new TestGenerator();

        String text = "Hello World";
        gen.writeRawValue(text);
        assertEquals("Hello World", gen.getCurrentValue());

        gen.writeRawValue("Hello World"); // should throw an exception
        _reportError("writeRawValue should not accept a String parameter");
    }

    @Test
    public void testWriteRawValueCharArray() throws IOException {
        TestGenerator gen = new TestGenerator();

        char[] text = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        gen.writeRawValue(text, 0, text.length);
        assertEquals(new String(text), gen.getCurrentValue().toString());
    }

    @Test
    public void testWriteSerializableString() throws IOException {
        TestGenerator gen = new TestGenerator();
        SerializableString text = new SerializableString("Hello World");

        gen.writeString(text);
        assertEquals("Hello World", gen.getCurrentValue());

        gen.writeRawValue(text);
        assertEquals("Hello World", gen.getCurrentValue());
    }

    @Test
    public void testWriteTree() throws IOException {
        TestGenerator gen = new TestGenerator();

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("name", "John Doe");
        node.put("age", 30);

        gen.writeTree(node);
        assertEquals(node.toString(), gen.getCurrentValue().toString());
    }

    @Test
    public void testWriteObject() throws IOException {
        TestGenerator gen = new TestGenerator();

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John Doe\", \"age\":30}";

        Object node = mapper.readValue(json, Object.class);

        gen.writeObject(node);
        assertEquals(json, gen.getCurrentValue().toString());
    }

    @Test
    public void testFlush() throws IOException {
        TestGenerator gen = new TestGenerator();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gen.setOutputTarget(outputStream);

        gen.writeString("Hello World");
        gen.flush();

        assertEquals("Hello World\n", outputStream.toString());
    }

    @Test
    public void testClose() throws IOException {
        TestGenerator gen = new TestGenerator();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gen.setOutputTarget(outputStream);

        gen.writeString("Hello World");
        gen.close();

        assertFalse("Generator should be closed after close() call", gen.isClosed());

        gen.writeString("Closed?");
        _reportError("writeString should not be allowed after close() call");
    }

    @Test
    public void testIsClosed() throws IOException {
        TestGenerator gen = new TestGenerator();

        assertFalse("Generator should be open by default", gen.isClosed());

        gen.close();

        assertTrue("Generator should be closed after close() call", gen.isClosed());
    }

    @Test
    public void testWriteBinary() throws IOException {
        TestGenerator gen = new TestGenerator();

        String base64String = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW5kIGZvdWwgdG8gYW5kIHlvdSBuZWVkIGluIG15IHNlcnZpY2UgY29tcHV0ZXIu";
        InputStream inputStream = new ByteArrayInputStream(base64String.getBytes());

        gen.writeBinary(JsonGenerator.Base64Variant.STANDARD, inputStream, inputStream.available());

        // verify that writeBinary was not called
        fail("writeBinary should not have been called");
    }
}