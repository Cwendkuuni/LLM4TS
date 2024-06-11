import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        verify(provider).handlePrimaryContextualization(mapSerializer, property);
        verifyNoMoreInteractions(bean, gen, provider, mapSerializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAndSerializeWithNullBean() throws Exception {
        Object bean = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAndSerializeWithNullGenerator() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = null;
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAndSerializeWithNullProvider() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = null;
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    public void testGetAndSerializeWithNullValue() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object value = null;
        doReturn(value).when(accessor).getValue(bean);

        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        verify(provider, never()).handlePrimaryContextualization(mapSerializer, property);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithValueNotMap() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);

        Object value = new Object();
        doReturn(value).when(accessor).getValue(bean);

        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }
}