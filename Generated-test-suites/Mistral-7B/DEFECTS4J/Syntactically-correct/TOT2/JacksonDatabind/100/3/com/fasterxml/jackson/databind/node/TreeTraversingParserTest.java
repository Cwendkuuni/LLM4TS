package com.fasterxml.jackson.databind.node; // import statements go here

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyJson() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{}"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testSimpleString() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"name\": \"John\"}"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());
        parser.close();
    }

    @Test
    public void testNestedObjects() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("first", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());
        parser.closeCurrentObject();
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("last", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Doe", parser.getText());
        parser.close();
    }

    @Test
    public void testNestedArrays() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"arr\": [1, [2, [3, 4], 5], 6], 7]}"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("arr", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(3, parser.getIntValue());
        parser.closeCurrentArray();
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(4, parser.getIntValue());
        parser.closeCurrentArray();
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(5, parser.getIntValue());
        parser.closeCurrentArray();
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(6, parser.getIntValue());
        parser.closeCurrentArray();
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(7, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testNullValue() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"name\": null}"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NULL, token);
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testUnexpectedToken() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"name\": 1}"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        token = parser.nextToken(); // should be VALUE_STRING, but it's VALUE_NUMBER_INT
        fail("Unexpected token: "+token);
    }

    @Test
    public void testEmptyArray() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("[]"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test
    public void testCloseParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{}"));
        parser.close();
    }

    private JsonNode createJsonNode(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        return factory.readValue(new JsonNodeReader(new StringReader(json)), JsonNode.class);
    }

    private static class JsonNodeReader extends BaseJsonParser {

        private final StringReader _sr;

        public JsonNodeReader(Reader r) {
            super(new JsonFactory().getCodec(), r);
            _sr = (StringReader) r;
        }

        @Override
        protected void _parseStartDocument() throws IOException {
            // no-op
        }

        @Override
        protected void _parseEndDocument() throws IOException {
            // no-op
        }

        @Override
        public void close() throws IOException {
            // no-op
        }
    }

    private static class StringReader extends InputStreamReader {

        private final String _content;

        public StringReader(String content) {
            super(new ByteArrayInputStream(content.getBytes()), "UTF-8");
            _content = content;
        }

        @Override
        public int read() throws IOException {
            return _content == null ? -1 : _content.charAt(0++) & 0xFF;
        }

        @Override
        public int read(char[] cbuf, int off, int length) throws IOException {
            String text = _content;
            if (text == null) {
                return -1;
            }
            int index = off;
            if (length == 0) {
                return 0;
            }
            int len = text.length();
            int numToRead = Math.min(len - index, length);
            for (int i = 0; i < numToRead; i++) {
                cbuf[index++] = text.charAt(off + i);
            }
            return numToRead;
        }

        @Override
        public long skip(long n) throws IOException {
            if (n > Integer.MAX_VALUE) {
                throw new IOException("Skip value is too large for a string reader");
            }
            int remaining = (int) n;
            String text = _content;
            while (remaining > 0 && text != null) {
                int length = text.length();
                int numToSkip = Math.min(length, remaining);
                text = text.substring(numToSkip);
                remaining -= numToSkip;
            }
            return n;
        }

        @Override
        public boolean markSupported(boolean readable, boolean writable) {
            return false;
        }

        @Override
        public void mark(int readAheadLimit) {
            // no-op
        }

        @Override
        public void reset() throws IOException {
            // no-op
        }

        @Override
        public boolean ready() throws IOException {
            return _content != null;
        }
    }
}