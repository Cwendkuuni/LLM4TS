import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class ValueInstantiatorTest {

    private ObjectMapper mapper;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        valueInstantiator = new ValueInstantiator.Base(String.class);
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("{ \"foo\": 1 }");
        Object result = valueInstantiator.createUsingDefault(new DeserializationContext(parser, mapper.getFactory()));
        Assert.assertNotNull(result);
        Assert.assertEquals("1", result.toString());
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("{ \"foo\": { \"bar\": 1 } }");
        Object[] arguments = new Object[]{"1"};
        SettableBeanProperty[] properties = new SettableBeanProperty[]{new SettableBeanProperty("bar")};
        Object result = valueInstantiator.createFromObjectWith(new DeserializationContext(parser, mapper.getFactory()), arguments, new PropertyValueBuffer(properties));
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.hashCode());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromObjectWithMissingArgument() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("{ \"foo\": { \"bar\": 1, \"baz\": 2 } }");
        SettableBeanProperty[] properties = new SettableBeanProperty[]{new SettableBeanProperty("bar")};
        Object[] arguments = new Object[]{null};
        Object result = valueInstantiator.createFromObjectWith(new DeserializationContext(parser, mapper.getFactory()), arguments, new PropertyValueBuffer(properties));
        // the test should fail with a JsonMappingException
    }

    @Test
    public void testCreateFromString() throws IOException {
        Object result = valueInstantiator.createFromString(new DeserializationContext(mapper.getFactory().getParser(), mapper), "\"Hello World\"");
        Assert.assertEquals("Hello World", result.toString());
    }

    @Test(expected = IOException.class)
    public void testCreateFromStringInvalid() throws IOException {
        Object result = valueInstantiator.createFromString(new DeserializationContext(mapper.getFactory().getParser(), mapper), "{ invalid json }");
        // the test should fail with an IOException
    }

    @Test
    public void testGetDelegateType() throws Exception {
        JavaType delegateType = valueInstantiator.getDelegateType(new DeserializationContext(mapper.getFactory(), new JsonParser(new StringReader(""))));
        Assert.assertNull(delegateType);
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        Class<?> delegateClass = String.class;
        Constructor<?> constructor = delegateClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object delegate = constructor.newInstance("Delegate");
        Object result = valueInstantiator.createUsingDelegate(new DeserializationContext(mapper.getFactory().getParser(), mapper), delegate);
        Assert.assertNotNull(result);
        Assert.assertEquals("Delegate", result.toString());
    }

    @Test(expected = IOException.class)
    public void testCreateUsingDelegateInvalid() throws Exception {
        Class<?> delegateClass = Integer.class;
        Constructor<?> constructor = delegateClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object delegate = constructor.newInstance("Delegate");
        Object result = valueInstantiator.createUsingDelegate(new DeserializationContext(mapper.getFactory().getParser(), mapper), delegate);
        // the test should fail with an IOException
    }

    @Test
    public void testCanCreateUsingDefault() {
        Assert.assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testCanCreateUsingDelegateFalse() {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class) {
            @Override
            public boolean canCreateUsingDelegate() {
                return false;
            }
        };
        Assert.assertFalse(instantiator.canInstantiate());
    }
}