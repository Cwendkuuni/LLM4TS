package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_FILE;
            }

            @Override
            protected File _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new File(value);
            }
        };

        JsonParser parser = createParser("\"path/to/file.txt\"");
        DeserializationContext context = createContext();
        assertEquals(new File("path/to/file.txt"), deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeUrl() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_URL;
            }

            @Override
            protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new URL(value);
            }
        };

        JsonParser parser = createParser("\"http://www.example.com\"");
        DeserializationContext context = createContext();
        assertEquals(new URL("http://www.example.com"), deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeUri() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_URI;
            }

            @Override
            protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return URI.create(value);
            }
        };

        JsonParser parser = createParser("\"uri://www.example.com/path/to\"");
        DeserializationContext context = createContext();
        assertEquals(URI.create("uri://www.example.com/path/to"), deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeClass() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_CLASS;
            }

            @Override
            protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Class.forName(value);
            }
        };

        JsonParser parser = createParser("\"java.util.Date\"");
        DeserializationContext context = createContext();
        assertEquals(Date.class, deserializer.deserialize(parser, context).getType());
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_JAVA_TYPE;
            }

            @Override
            protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return ctxt.getTypeFactory().constructType(value);
            }
        };

        JsonParser parser = createParser("\"java.util.List<java.lang.String>\"");
        DeserializationContext context = createContext();
        assertEquals(java.util.List.class, deserializer.deserialize(parser, context).getType().getRawClass());
        assertEquals(java.lang.String.class, deserializer.deserialize(parser, context).getType().getContentType().getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_CURRENCY;
            }

            @Override
            protected Currency _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Currency.getInstance(value);
            }
        };

        JsonParser parser = createParser("\"USD\"");
        DeserializationContext context = createContext();
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializePattern() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_PATTERN;
            }

            @Override
            protected Pattern _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Pattern.compile(value);
            }
        };

        JsonParser parser = createParser("\"\\w+\\.\\w+\"");
        DeserializationContext context = createContext();
        assertEquals(Pattern.compile("\\w+\\.\\w+"), deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_LOCALE;
            }

            @Override
            protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
                String[] parts = value.split("[_]");
                return new Locale(parts[0], parts.length > 1 ? parts[1] : "");
            }
        };

        JsonParser parser = createParser("\"en_US\"");
        DeserializationContext context = createContext();
        assertEquals(Locale.US, deserializer.deserialize(parser, context));

        parser = createParser("\"en_US_POSIX\"");
        context = createContext();
        assertEquals(Locale.US, deserializer.deserialize(parser, context));

        parser = createParser("\"en_GB_POSIX\"");
        context = createContext();
        assertEquals(Locale.UK, deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_CHARSET;
            }

            @Override
            protected Charset _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Charset.forName(value);
            }
        };

        JsonParser parser = createParser("\"UTF-8\"");
        DeserializationContext context = createContext();
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_TIME_ZONE;
            }

            @Override
            protected TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return TimeZone.getTimeZone(value);
            }
        };

        JsonParser parser = createParser("\"America/New_York\"");
        DeserializationContext context = createContext();
        assertEquals(TimeZone.getTimeZone("America/New_York"), deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_INET_ADDRESS;
            }

            @Override
            protected InetAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return InetAddress.getByName(value);
            }
        };

        JsonParser parser = createParser("\"192.168.0.1\"");
        DeserializationContext context = createContext();
        assertEquals(InetAddress.getByName("192.168.0.1"), deserializer.deserialize(parser, context));

        parser = createParser("\"255.255.255.255\"");
        context = createContext();
        assertNull(deserializer.deserialize(parser, context));

        parser = createParser("\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"");
        context = createContext();
        assertEquals(InetAddress.getByName("2001:0db8:85a3:0:0:8a2e:370:7334"), deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected int _kind() {
                return FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS;
            }

            @Override
            protected InetSocketAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
                int index = value.indexOf(':');
                String host = index > 0 ? value.substring(0, index) : value;
                int port = index > 0 ? Integer.parseInt(value.substring(index + 1)) : 0;
                return new InetSocketAddress(InetAddress.getByName(host), port);
            }
        };

        JsonParser parser = createParser("\"192.168.0.1:80\"");
        DeserializationContext context = createContext();
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.0.1"), 80), deserializer.deserialize(parser, context));

        parser = createParser("\"[2001:0db8:85a3:0:0:8a2e:370:7334]:80\"");
        context = createContext();
        assertEquals(new InetSocketAddress(InetAddress.getByName("2001:0db8:85a3:0:0:8a2e:370:7334"), 80), deserializer.deserialize(parser, context));
    }

    @Test
    public void testEmptyStringDeserialization() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserializeFromEmptyString() throws IOException {
                return null;
            }
        };

        JsonParser parser = createParser("\"\"");
        DeserializationContext context = createContext();
        assertNull(deserializer.deserialize(parser, context));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {};

        JsonParser parser = createParser("\"invalid\"");
        DeserializationContext context = createContext();
        deserializer.deserialize(parser, context);
    }

    private JsonParser createParser(String content) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        ObjectReader objectReader = new ObjectMapper().readerFor(Object.class);
        return objectReader.readValueAs(JsonParser.class, inputStream);
    }

    private DeserializationContext createContext() {
        return new TestDeserializationContext();
    }
}