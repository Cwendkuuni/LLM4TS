import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class ValueInstantiatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefaultInstantiator() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Map.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object result = instantiator.createUsingDefault(context);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testCreateFromStringNull() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(String.class);
        DeserializationContext context = mock(DeserializationContext.class);
        instantiator.createFromString(context, null);
    }

    @Test
    public void testCreateFromStringEmpty() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(String.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String value = "";
        assertEquals("", instantiator.createFromString(context, value));
    }

    @Test
    public void testCreateFromStringValid() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(String.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String value = "test";
        assertEquals(value, instantiator.createFromString(context, value));
    }

    @Test(expected = IOException.class)
    public void testCreateFromIntNegative() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Integer.class);
        DeserializationContext context = mock(DeserializationContext.class);
        instantiator.createFromInt(context, -1);
    }

    @Test
    public void testCreateFromIntZero() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Integer.class);
        DeserializationContext context = mock(DeserializationContext.class);
        int value = 0;
        assertEquals(value, instantiator.createFromInt(context, value));
    }

    @Test
    public void testCreateFromIntPositive() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Integer.class);
        DeserializationContext context = mock(DeserialiationContext.class); // typo in DeserializationContext
        int value = 1;
        assertEquals(value, instantiator.createFromInt(context, value));
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromLongNegative() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Long.class);
        DeserializationContext context = mock(DeserializationContext.class);
        instantiator.createFromLong(context, -1L);
    }

    @Test
    public void testCreateFromLongZero() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Long.class);
        DeserializationContext context = mock(DeserializationContext.class);
        long value = 0L;
        assertEquals(value, instantiator.createFromLong(context, value));
    }

    @Test
    public void testCreateFromLongPositive() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Long.class);
        DeserializationContext context = mock(DeserializationContext.class);
        long value = 1L;
        assertEquals(value, instantiator.createFromLong(context, value));
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromDoubleNegative() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Double.class);
        DeserializationContext context = mock(DeserializationContext.class);
        instantiator.createFromDouble(context, -1.0);
    }

    @Test
    public void testCreateFromDoubleZero() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Double.class);
        DeserializationContext context = mock(DeserializationContext.class);
        double value = 0.0;
        assertEquals(value, instantiator.createFromDouble(context, value), 0.0);
    }

    @Test
    public void testCreateFromDoublePositive() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Double.class);
        DeserializationContext context = mock(DeserializationContext.class);
        double value = 1.0;
        assertEquals(value, instantiator.createFromDouble(context, value), 0.0);
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromBooleanFalse() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Boolean.class);
        DeserializationContext context = mock(DeserializationContext.class);
        instantiator.createFromBoolean(context, false);
    }

    @Test
    public void testCreateFromBooleanTrue() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Boolean.class);
        DeserializationContext context = mock(DeserializationContext.class);
        boolean value = true;
        assertEquals(value, instantiator.createFromBoolean(context, value));
    }

    @Test
    public void testCreateFromObjectWithNull() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Map.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object[] arguments = new Object[0];
        instantiator.createFromObjectWith(context, arguments);
        assertNull(instantiator.createFromObjectWith(context, arguments));
    }

    @Test(expected = JsonProcessingException.class)
    public void testCreateFromObjectWithInvalidType() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Map.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object[] arguments = {new Object() {}};
        instantiator.createFromObjectWith(context, arguments);
    }

    @Test
    public void testCreateFromObjectWithValid() throws Exception {
        ValueInstantiator instantiator = MAPPER.findValueInstantiator(Map.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object[] arguments = {"key", "value"};
        Map map = new HashMap();
        map.put("key", "value");
        assertEquals(map, instantiator.createFromObjectWith(context, arguments));
    }
}