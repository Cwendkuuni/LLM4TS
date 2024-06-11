import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdValueInstantiatorTest {

    private ObjectMapper objectMapper = new JsonMapper();

    @Test
    public void testCreateUsingDefault() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, String.class);
        String result = valueInstantiator.createUsingDefault(this.objectMapper.getDeserializationContext());
        assertEquals("Unknown TYPE", result);
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, String.class);
        SettableBeanProperty[] args = new SettableBeanProperty[]{new SettableBeanProperty.Std("test", String.class)};
        String result = valueInstantiator.createFromObjectWith(this.objectMapper.getDeserializationContext(), new Object[]{"test"});
        assertEquals("test", result);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, String.class);
        JavaType delegateType = TypeFactory.defaultInstance().constructType(String[].class);
        AnnotatedWithParams delegateCreator = this.objectMapper.findValueInstancer(delegateType, null, null);
        SettableBeanProperty[] delegateArguments = new SettableBeanProperty[]{new SettableBeanProperty.Std("test", String.class)};
        valueInstantiator.configureFromDelegateSettings(delegateCreator, delegateArguments);
        String[] result = (String[]) valueInstantiator.createUsingDelegate(this.objectMapper.getDeserializationContext(), new Object[]{"test"});
        assertEquals("test", result[0]);
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, String[].class);
        JavaType arrayDelegateType = TypeFactory.defaultInstance().constructType(String[].class);
        AnnotatedWithParams arrayDelegateCreator = this.objectMapper.findValueInstancer(arrayDelegateType, null, null);
        SettableBeanProperty[] arrayDelegateArguments = new SettableBeanProperty[]{new SettableBeanProperty.Std("test", String.class)};
        valueInstantiator.configureFromArraySettings(arrayDelegateCreator, arrayDelegateType, arrayDelegateArguments);
        String[] result = (String[]) valueInstantiator.createUsingArrayDelegate(this.objectMapper.getDeserializationContext(), new Object[]{new String[]{"test"}});
        assertEquals("test", result[0]);
    }

    @Test(expected = IOException.class)
    public void testCreateFromStringFailed() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, String.class);
        String result = valueInstantiator.createFromString(this.objectMapper.getDeserializationContext(), "test");
        fail("Should have thrown IOException");
    }

    @Test
    public void testCreateFromInt() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, Integer.class);
        int result = valueInstantiator.createFromInt(this.objectMapper.getDeserializationContext(), 1);
        assertEquals(1, result.intValue());
    }

    @Test
    public void testCreateFromLong() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, Long.class);
        long result = valueInstantiator.createFromLong(this.objectMapper.getDeserializationContext(), 1L);
        assertEquals(1L, result.longValue());
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, Double.class);
        double result = valueInstantiator.createFromDouble(this.objectMapper.getDeserializationContext(), 1.0);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        StdValueInstantiator valueInstantiator = new StdValueInstantiator(null, Boolean.class);
        boolean result = valueInstantiator.createFromBoolean(this.objectMapper.getDeserializationContext(), true);
        assertTrue(result);
    }
}