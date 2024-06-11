package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.TestBase;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import junit.framework.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest extends TestBase {

    private static ObjectMapper mapper;

    static {
        mapper = JsonMapperFactory.builder().build();
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testDeserialize() throws IOException, JsonProcessingException {
        String json = "\"file.txt\"";
        Class<?>[] supportedTypes = FromStringDeserializer.types();

        for (Class<?> clazz : supportedTypes) {
            FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(clazz);
            if (deserializer != null) {
                Object result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
                if (clazz == File.class) {
                    Assert.assertInstanceOf(File.class, result);
                } else if (clazz == URL.class) {
                    Assert.assertInstanceOf(URL.class, result);
                    Assert.assertEquals("file.txt", ((URL) result).getFile());
                } else if (clazz == URI.class) {
                    Assert.assertInstanceOf(URI.class, result);
                    Assert.assertEquals("file.txt", ((URI) result).getPath());
                } else if (clazz == Class.class) {
                    Assert.assertInstanceOf(Class.class, result);
                    Assert.assertEquals(String.class, ((Class<?>) result).getName());
                } else if (clazz == JavaType.class) {
                    Assert.assertInstanceOf(JavaType.class, result);
                } else if (clazz == Currency.class) {
                    Assert.assertInstanceOf(Currency.class, result);
                    Assert.assertEquals(Currency.getInstance("USD"), result);
                } else if (clazz == Pattern.class) {
                    Assert.assertInstanceOf(Pattern.class, result);
                    Assert.assertEquals(Pattern.compile(".*"), result);
                } else if (clazz == Locale.class) {
                    Assert.assertInstanceOf(Locale.class, result);
                    Assert.assertEquals(Locale.ENGLISH, result);
                } else if (clazz == Charset.class) {
                    Assert.assertInstanceOf(Charset.class, result);
                    Assert.assertEquals(StandardCharsets.UTF_8, result);
                } else if (clazz == TimeZone.class) {
                    Assert.assertInstanceOf(TimeZone.class, result);
                    Assert.assertEquals(TimeZone.getDefault(), result);
                } else if (clazz == InetAddress.class) {
                    Assert.assertInstanceOf(InetAddress.class, result);
                    InetAddress address = InetAddress.getByName("127.0.0.1");
                    Assert.assertEquals(address.getHostAddress(), result.getHostAddress());
                } else if (clazz == InetSocketAddress.class) {
                    Assert.assertInstanceOf(InetSocketAddress.class, result);
                    InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
                    Assert.assertEquals(expected.getAddress().getHostAddress(), ((InetSocketAddress) result).getAddress().getHostAddress());
                    Assert.assertEquals(expected.getPort(), ((InetSocketAddress) result).getPort());
                } else if (clazz == StringBuilder.class) {
                    Assert.assertInstanceOf(StringBuilder.class, result);
                    StringBuilder expected = new StringBuilder("expected");
                    Assert.assertEquals(expected, result);
                }
            }
        }
    }

    @org.junit.Test
    public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
        String json = "\"\"";

        for (Class<?> supportedType : FromStringDeserializer.types()) {
            FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(supportedType);
            if (deserializer != null) {
                Object result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
                if (supportedType == File.class) {
                    Assert.assertNull(result);
                } else if (supportedType == URL.class) {
                    Assert.assertNull(result);
                } else if (supportedType == URI.class) {
                    Assert.assertInstanceOf(URI.class, result);
                    Assert.assertEquals(URI.create(""), result);
                } else if (supportedType == Class.class) {
                    Assert.assertNull(result);
                } else if (supportedType == JavaType.class) {
                    Assert.assertNull(result);
                } else if (supportedType == Currency.class) {
                    Assert.assertNull(result);
                } else if (supportedType == Pattern.class) {
                    Assert.assertNull(result);
                } else if (supportedType == Locale.class) {
                    Assert.assertEquals(Locale.ROOT, result);
                } else if (supportedType == Charset.class) {
                    Assert.assertNull(result);
                } else if (supportedType == TimeZone.class) {
                    Assert.assertEquals(TimeZone.getDefault(), result);
                } else if (supportedType == InetAddress.class) {
                    Assert.assertNull(result);
                } else if (supportedType == InetSocketAddress.class) {
                    Assert.assertNull(result);
                } else if (supportedType == StringBuilder.class) {
                    Assert.assertEquals(StringBuilder.class, result.getClass());
                    StringBuilder expected = new StringBuilder();
                    Assert.assertEquals(expected, result);
                }
            }
        }
    }

    @org.junit.Test
    public void testDeserializeNull() throws IOException, JsonProcessingException {
        String json = "null";

        for (Class<?> supportedType : FromStringDeserializer.types()) {
            FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(supportedType);
            if (deserializer != null) {
                Object result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
                if (supportedType == File.class) {
                    Assert.assertNull(result);
                } else if (supportedType == URL.class) {
                    Assert.assertNull(result);
                } else if (supportedType == URI.class) {
                    Assert.assertNull(result);
                } else if (supportedType == Class.class) {
                    Assert.assertNull(result);
                } else if (supportedType == JavaType.class) {
                    Assert.assertNull(result);
                } else if (supportedType == Currency.class) {
                    Assert.assertNull(result);
                } else if (supportedType == Pattern.class) {
                    Assert.assertNull(result);
                } else if (supportedType == Locale.class) {
                    Assert.assertNull(result);
                } else if (supportedType == Charset.class) {
                    Assert.assertNull(result);
                } else if (supportedType == TimeZone.class) {
                    Assert.assertNull(result);
                } else if (supportedType == InetAddress.class) {
                    Assert.assertNull(result);
                } else if (supportedType == InetSocketAddress.class) {
                    Assert.assertNull(result);
                } else if (supportedType == StringBuilder.class) {
                    Assert.assertNull(result);
                }
            }
        }
    }

    @org.junit.Test
    public void testDeserializeInvalid() throws IOException, JsonProcessingException {
        String json = "invalid";

        for (Class<?> supportedType : FromStringDeserializer.types()) {
            FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(supportedType);
            if (deserializer != null) {
                try {
                    deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getContext());
                    Assert.fail("Expected JsonMappingException");
                } catch (JsonMappingException expected) {
                    // Expected
                }
            } else {
                Assert.assertEquals(supportedType, null);
            }
        }
    }

    @org.junit.Test
    public void testDeserializeArray() throws IOException, JsonProcessingException {
        String json = "[\"file.txt\",\"url.com\"]";

        for (Class<?> supportedType : FromStringDeserializer.types()) {
            FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(supportedType);
            if (deserializer != null) {
                Object[] result = deserializer.deserializeFromArray(mapper.getFactory().createParser(json), mapper.getContext());
                if (supportedType == File[].class) {
                    Assert.assertArrayEquals(new File[]{new File("file.txt"), new File("url.com")}, result);
                } else if (supportedType == URL[].class) {
                    Assert.assertArrayEquals(new URL[]{new URL("file.txt"), new URL("url.com")}, result);
                } else if (supportedType == URI[].class) {
                    Assert.assertArrayEquals(new URI[]{URI.create("file.txt"), URI.create("url.com")}, result);
                } else {
                    Assert.fail("Unexpected type: " + supportedType);
                }
            }
        }
    }

    @org.junit.Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonProcessingException {
        String json = "{\"file\":\"file.txt\",\"url\":\"url.com\"}";

        for (Class<?> supportedType : FromStringDeserializer.types()) {
            FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(supportedType);
            if (deserializer != null) {
                Object result = deserializer.deserializeEmbedded(mapper.readValue(json, Object.class), mapper.getContext());
                if (supportedType == File[].class) {
                    Assert.assertNull(result);
                } else if (supportedType == URL[].class) {
                    Assert.assertNull(result);
                } else if (supportedType == URI[].class) {
                    Assert.assertNull(result);
                } else if (supportedType == Object[].class) {
                    Object obj = mapper.readValue(json, Object.class);
                    Assert.assertNotNull(obj);
                    Assert.assertEquals(2, obj.getClass().getDeclaredFields().length);
                    Object[] expected = new Object[]{new File("file.txt"), new URL("url.com")};
                    Assert.assertArrayEquals(expected, ((Object[]) result));
                } else {
                    Assert.fail("Unexpected type: " + supportedType);
                }
            }
        }
    }
}