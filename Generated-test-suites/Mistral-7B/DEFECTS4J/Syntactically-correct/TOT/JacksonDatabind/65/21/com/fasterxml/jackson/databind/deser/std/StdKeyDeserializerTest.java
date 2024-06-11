package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializableString;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer _stringKD;
    private StdKeyDeserializer _stringCtorKD;
    private StdKeyDeserializer _intKD;
    private StdKeyDeserializer _longKD;
    private StdKeyDeserializer _dateKD;
    private StdKeyDeserializer _uriKD;
    private StdKeyDeserializer _urlKD;
    private StdKeyDeserializer _classKD;
    private StdKeyDeserializer _enumKD;
    private StdKeyDeserializer _localeKD;
    private StdKeyDeserializer _currencyKD;

    @BeforeMethod
    public void setup() {
        _stringKD = StdKeyDeserializer.forType(String.class);
        _stringCtorKD = StdKeyDeserializer.forType(String.class);
        _intKD = StdKeyDeserializer.forType(Integer.class);
        _longKD = StdKeyDeserializer.forType(Long.class);
        _dateKD = StdKeyDeserializer.forType(Date.class);
        _uriKD = StdKeyDeserializer.forType(URI.class);
        _urlKD = StdKeyDeserializer.forType(URL.class);
        _classKD = StdKeyDeserializer.forType(Class.class);
        _enumKD = new EnumKD(mock(EnumResolver.class), mock(AnnotatedMethod.class));
        _localeKD = new DelegatingKD(Locale.class, mock(JsonDeserializer.class));
        _currencyKD = new DelegatingKD(Currency.class, mock(JsonDeserializer.class));
    }

    @Test
    public void testStringKey() throws Exception {
        Object key = _stringKD.deserializeKey("key123", null);
        assertEquals("key123", key);
    }

    @Test
    public void testStringKeyDeserializeUsingDelegate() throws Exception {
        Object key = _localeKD.deserializeKey("en_US", null);
        assertNotNull(key);
        verify(mock(JsonDeserializer.class), times(1)).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testBooleanKey() throws Exception {
        Object key = _intKD.deserializeKey("true", null);
        assertTrue((Boolean) key);
        key = _intKD.deserializeKey("false", null);
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteKey() throws Exception {
        Object key = _intKD.deserializeKey("66", null);
        assertEquals((Byte) (byte) 66, key);
    }

    @Test
    public void testShortKey() throws Exception {
        Object key = _intKD.deserializeKey("123", null);
        assertEquals((Short) 123, key);
    }

    @Test
    public void testCharKey() throws Exception {
        Object key = _intKD.deserializeKey("A", null);
        assertEquals((Character) 'A', key);
    }

    @Test
    public void testIntKey() throws Exception {
        Object key = _intKD.deserializeKey("123", null);
        assertEquals(123, key);
    }

    @Test
    public void testLongKey() throws Exception {
        Object key = _longKD.deserializeKey("12345678901234567890", null);
        assertEquals(12345678901234567890L, key);
    }

    @Test
    public void testFloatKey() throws Exception {
        Object key = _doubleKD.deserializeKey("1.23", null);
        assertEquals(1.23f, key);
    }

    @Test
    public void testDoubleKey() throws Exception {
        Object key = _doubleKD.deserializeKey("1.23", null);
        assertEquals(1.23, key);
    }

    @Test
    public void testDateKey() throws Exception {
        Object key = _dateKD.deserializeKey("2022-01-01", null);
        assertInstanceOf(Date.class, key);
        SerializableString dateString = new SerializableString("2022-01-01");
        assertEquals(dateString.stringValue(), _dateKD.getKeySerializer().serialize(_dateKD.deserializeKey(dateString, null)));
    }

    @Test
    public void testURIKey() throws Exception {
        Object key = _uriKD.deserializeKey("http://www.example.com", null);
        assertInstanceOf(URI.class, key);
        SerializableString uriString = new SerializableString("http://www.example.com");
        assertEquals(uriString.stringValue(), _uriKD.getKeySerializer().serialize(_uriKD.deserializeKey(uriString, null)));
    }

    @Test
    public void testURLKey() throws Exception {
        Object key = _urlKD.deserializeKey("http://www.example.com", null);
        assertInstanceOf(URL.class, key);
        SerializableString urlString = new SerializableString("http://www.example.com");
        assertEquals(urlString.stringValue(), _urlKD.getKeySerializer().serialize(_urlKD.deserializeKey(urlString, null)));
    }

    @Test
    public void testClassKey() throws Exception {
        Object key = _classKD.deserializeKey("java.lang.String", null);
        assertInstanceOf(Class.class, key);
        assertEquals(String.class, _classKD.getKeyClass());
        assertEquals(String.class, key.getClass());
    }

    @Test
    public void testEnumKey() throws Exception {
        EnumResolver enumResolver = mock(EnumResolver.class);
        Object key = _enumKD.deserializeKey("FOO", null);
        assertInstanceOf(Enum.class, key);

        when(enumResolver.findEnum(eq("FOO"))).thenReturn(Enum.valueOf(String.class, "FOO"));
        Object key2 = _enumKD.deserializeKey("FOO", null);
        assertEquals(key, key2);

        when(enumResolver.findEnum(eq("BAR"))).thenReturn(null);
        assertThrows(JsonProcessingException.class, () -> _enumKD.deserializeKey("BAR", null));

        when(enumResolver.getEnumIds()).thenReturn(new String[]{"FOO", "BAR", "BAZ"});
        Object key3 = _enumKD.deserializeKey("BAZ", null);
        assertNull(key3);

        when(enumResolver.findEnum(anyString())).thenThrow(new IllegalArgumentException("boom"));
        assertThrows(JsonProcessingException.class, () -> _enumKD.deserializeKey("BOOM", null));
    }

    @Test
    public void testLocaleKey() throws Exception {
        Object key = _localeKD.deserializeKey("en_US", null);
        assertInstanceOf(Locale.class, key);
        assertEquals("en_US", key.toString());
    }

    @Test
    public void testCurrencyKey() throws Exception {
        Object key = _currencyKD.deserializeKey("USD", null);
        assertInstanceOf(Currency.class, key);
        assertEquals("USD", key.toString());
    }

    @Test
    public void testNullKey() throws Exception {
        Object key = _intKD.deserializeKey(null, null);
        assertNull(key);
    }

    @Test
    public void testInvalidKey() throws Exception {
        assertThrows(JsonProcessingException.class, () -> _intKD.deserializeKey("invalidKey", null));
    }

    @Test
    public void testInvalidKeyWithAnnotation() throws Exception {
        AnnotatedClass annotatedClass = mock(AnnotatedClass.class);
        when(annotatedClass.getName()).thenReturn(Object.class.getName());
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        JsonDeserializer<?> annotatedDeserializer = mock(JsonDeserializer.class);
        when(annotatedDeserializer.getClass()).thenReturn(JsonDeserializer.class);
        when(annotatedDeserializer.getAnnotationObjects()).thenReturn(new Object[]{new JacksonAnnotation(true)});
        doAnswer(invocation -> invocation.getArgument(0)).when(deserializer).deserialize(any(JsonParser.class), any(DeserializationContext.class));
        when(annotatedClass.findMethod(eq("findDeserializer"))).thenReturn(new AnnotatedMethod(deserializer, "findDeserializer", new Class[0]));
        AnnotatedMethod annotatedFactoryMethod = mock(AnnotatedMethod.class);
        when(annotatedFactoryMethod.getName()).thenReturn("valueOf");
        when(annotatedFactoryMethod.getParameterTypes()).thenReturn(new Class[]{String.class});
        AnnotatedConstructor constructor = mock(AnnotatedConstructor.class);
        when(constructor.getParameterTypes()).thenReturn(new Class[]{String.class});
        when(annotatedClass.findConstructor(any(AnnotatedMethod.class), any(AnnotatedConstructor.class))).thenReturn(constructor);

        Object key = _stringCtorKD.deserializeKey("key123", null);
        assertInstanceOf(String.class, key);
        assertEquals("key123", key);

        Object key2 = _stringFactoryKeyDeserializer.deserializeKey("key2", null);
        assertInstanceOf(String.class, key2);
        assertEquals("key2", key2);

        Object key3 = _stringFactoryKeyDeserializer.deserializeKey("key3", null);
        assertInstanceOf(String.class, key3);
        assertEquals("key3", key3);

        Object key4 = _stringFactoryKeyDeserializer.deserializeKey("key4", null);
        assertInstanceOf(String.class, key4);
        assertEquals("key4", key4);

        assertThrows(JsonProcessingException.class, () -> _stringFactoryKeyDeserializer.deserializeKey("invalidKey", null));
    }

    private StdKeyDeserializer _stringFactoryKeyDeserializer;

    @Test
    public void testStringFactoryDeserializer() throws Exception {
        _stringFactoryKeyDeserializer = new StringFactoryKeyDeserializer(mock(Method.class));

        Object key1 = _stringFactoryKeyDeserializer.deserializeKey("key1", null);
        assertInstanceOf(String.class, key1);
        assertEquals("key1", key1);

        Object key2 = _stringFactoryKeyDeserializer.deserializeKey("key2", null);
        assertInstanceOf(String.class, key2);
        assertEquals("key2", key2);

        Object key3 = _stringFactoryKeyDeserializer.deserializeKey("key3", null);
        assertInstanceOf(String.class, key3);
        assertEquals("key3", key3);

        Object key4 = _stringFactoryKeyDeserializer.deserializeKey("key4", null);
        assertInstanceOf(String.class, key4);
        assertEquals("key4", key4);

        Object key5 = _stringFactoryKeyDeserializer.deserializeKey("key5", null);
        assertInstanceOf(String.class, key5);
        assertEquals("key5", key5);

        assertThrows(JsonProcessingException.class, () -> _stringFactoryKeyDeserializer.deserializeKey("invalidKey", null));
    }
}