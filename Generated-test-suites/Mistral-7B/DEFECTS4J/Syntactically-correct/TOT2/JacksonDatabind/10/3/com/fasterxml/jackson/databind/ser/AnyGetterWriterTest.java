
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;

    @Mock
    private BeanProperty property;

    @Mock
    private AnnotatedMember accessor;

    @Mock
    private MapSerializer mapSerializer;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private PropertyFilter filter;

    @Before
    public void setUp() {
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testConstructor() {
        // ... (test case from earlier)
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        // ... (test case from earlier)
    }

    @Test
    public void testGetAndFilter() throws Exception {
        // ... (test case from earlier)
    }

    @Test
    public void testResolve() throws JsonMappingException {
        // ... (test case from earlier)
    }

    @Test
    public void testNullBean() throws Exception {
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object bean = null;

        anyGetterWriter.getAndSerialize(bean, gen, provider);
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // No exception should be thrown
    }

    @Test
    public void testEmptyMap() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedMember(null, null, null, null, null), mapSerializer);

        Map<String, Object> map = new HashMap<>();

        when(_accessor.getValue(mock(Object.class))).thenReturn(map);

        anyGetterWriter.getAndSerialize(mock(Object.class), gen, provider);
        anyGetterWriter.getAndFilter(mock(Object.class), gen, provider, filter);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testMapWithNullValues() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedMember(null, null, null, null, null), mapSerializer);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", null);
        map.put("key2", "value2");

        when(_accessor.getValue(mock(Object.class))).thenReturn(map);

        anyGetterWriter.getAndSerialize(mock(Object.class), gen, provider);
        anyGetterWriter.getAndFilter(mock(Object.class), gen, provider, filter);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeNull("key1");
        verify(gen, times(1)).writeFieldName("key2");
        verify(gen, times(1)).writeString("value2");
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testMapWithNonStringKeys() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedMember(null, null, null, null, null), mapSerializer);

        Map<Number, Object> map = new HashMap<>();
        map.put(1, "value1");
        map.put(2, "value2");

        when(_accessor.getValue(mock(Object.class))).thenReturn(map);

        anyGetterWriter.getAndSerialize(mock(Object.class), gen, provider);
        anyGetterWriter.getAndFilter(mock(Object.class), gen, provider, filter);

        verify(gen, times(1)).writeStartObject();
        for (Map.Entry<Number, Object> entry : map.entrySet()) {
            verify(gen, times(1)).writeNumberFieldName(eq(entry.getKey().toString()));
            verify(gen, times(1)).writeString(entry.getValue().toString());
        }
        verify(gen, times(1)).writeEndObject();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedMember(null, null, null, null, null), mapSerializer);

        when(gen.writeStartObject()).thenThrow(new IOException());

        anyGetterWriter.getAndSerialize(mock(Object.class), gen, provider);
        anyGetterWriter.getAndFilter(mock(Object.class), gen, provider, filter);

        // IOException should be thrown
    }

    @Test
    public void testJsonMappingException() throws Exception {
        Object bean = new Object();

        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(new BeanProperty(null, null, null), new AnnotatedMember(null, null, null, null, null), mapSerializer);

        when(_accessor.getValue(bean)).thenReturn(new Object());

        try {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
            fail("An exception should have been thrown.");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
            fail("An exception should have been thrown.");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}
