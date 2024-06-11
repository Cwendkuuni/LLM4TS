package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected File _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new File(value);
            }
        };

        File result = mapper.readValue("\"/path/to/file.txt\"", deserializer.getType());
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new URL(value);
            }
        };

        URL result = mapper.readValue("\"http://example.com\"", deserializer.getType());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testURIDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return URI.create(value);
            }
        };

        URI result = mapper.readValue("\"uri://example.com/path/to/resource\"", deserializer.getType());
        assertEquals("uri://example.com/path/to/resource", result.toString());
    }

    @Test
    public void testClassDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
                try {
                    return Class.forName(value);
                } catch (ClassNotFoundException e) {
                    throw new JsonMappingException(String.format("Unable to find class '%s'", value), e);
                }
            }
        };

        Class<String> result = mapper.readValue("java.lang.String", deserializer.getType());
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return mapper.getTypeFactory().constructFromCanonical(value);
            }
        };

        JavaType<String> result = mapper.readValue("java.util.List<java.lang.String>", deserializer.getType());
        assertEquals( complicateMap().getType(), result );
    }

    @Test
    public void testCurrencyDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Currency _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Currency.getInstance(value);
            }
        };

        Currency result = mapper.readValue("\"USD\"", deserializer.getType());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Pattern _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Pattern.compile(value);
            }
        };

        Pattern result = mapper.readValue("\"[a-z]+\\.\\w+\"", deserializer.getType());
        assertEquals("[a-z]+\\.\\w+", result.pattern().toString());
    }

    @Test
    public void testLocaleDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
                int index = value.indexOf('_');
                String language;
                String country;
                if (index > 0) {
                    language = value.substring(0, index);
                    country = value.substring(index + 1);
                } else {
                    language = value;
                    country = "";
                }
                return new Locale(language, country);
            }
        };

        Locale result = mapper.readValue("\"en_US\"", deserializer.getType());
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCharsetDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Charset _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Charset.forName(value);
            }
        };

        Charset result = mapper.readValue("\"UTF-8\"", deserializer.getType());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return TimeZone.getTimeZone(value);
            }
        };

        TimeZone result = mapper.readValue("\"UTC\"", deserializer.getType());
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected InetAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
                try {
                    return InetAddress.getByName(value);
                } catch (UnknownHostException e) {
                    throw new JsonMappingException("Invalid IP address: " + value, e);
                }
            }
        };

        InetAddress result = mapper.readValue("\"192.168.1.1\"", deserializer.getType());
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected InetSocketAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
                int index = value.indexOf(':');
                String host;
                int port;

                if (index > 0) {
                    host = value.substring(0, index);
                    port = Integer.parseInt(value.substring(index + 1));
                } else {
                    host = value;
                    port = 0;
                }

                return new InetSocketAddress(InetAddress.getByName(host), port);
            }
        };

        InetSocketAddress result = mapper.readValue("\"192.168.1.1:8080\"", deserializer.getType());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 8080), result);
    }

    @Test
    public void testNullDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        assertNull(mapper.readValue("\"null\"", deserializer.getType()));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                throw new IllegalArgumentException("Invalid value: " + value);
            }
        };

        mapper.readValue("\"invalid\"", deserializer.getType());
    }

    private JavaType<List<String>> complicateMap() {
        return mapper.getTypeFactory()
                .constructCollectionType(List.class, String.class);
    }
}