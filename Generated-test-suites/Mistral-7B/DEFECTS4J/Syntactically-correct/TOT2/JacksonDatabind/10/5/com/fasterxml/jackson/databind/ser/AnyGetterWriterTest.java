
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializer;

public class AnyGetterWriterTest {
    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);

        when(bean.getClass()).thenReturn(Object.class);
        when(accessor.getValue(bean)).thenReturn(new HashMap<>());

        when(provider.handlePrimaryContextualization(any(StdSerializer.class), any(BeanProperty.class))).thenReturn(mapSerializer);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
        writer.getAndSerialize(bean, gen, provider);

        verify(mapSerializer).serializeFields((Map<?,?>) anyMap(), eq(gen), eq(provider));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNullValue() throws Exception {
        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);

        when(bean.getClass()).thenReturn(Object.class);
        when(accessor.getValue(bean)).thenReturn(null);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);
        writer.getAndSerialize(bean, gen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Object value = new Object();

        when(bean.getClass()).thenReturn(Object.class);
        when(accessor.getValue(bean)).thenReturn(value);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);
        writer.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Map<String, Object> map = new HashMap<>();

        when(bean.getClass()).thenReturn(Object.class);
        when(accessor.getValue(bean)).thenReturn(map);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);
        writer.getAndFilter(bean, gen, provider, filter);

        // Check if _mapSerializer is not null
        verify(writer)._mapSerializer;
    }

    @Test
    public void testGetAndFilterWithNullFilter() throws Exception {
        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Map<String, Object> map = new HashMap<>();

        when(bean.getClass()).thenReturn(Object.class);
        when(accessor.getValue(bean)).thenReturn(map);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);
        writer.getAndFilter(bean, gen, provider, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNullValue() throws Exception {
        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);

        when(bean.getClass()).thenReturn(Object.class);
        when(accessor.getValue(bean)).thenReturn(null);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);
        writer.getAndFilter(bean, gen, provider, filter);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws Exception {
        Object bean = mock(Object.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        Object value = new Object();

        when(bean.getClass()).thenReturn(Object.class);
        when(accessor.getValue(bean)).thenReturn(value);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, null);
        writer.getAndFilter(bean, gen, provider, filter);
    }
}
