import com.fasterxml.jackson.core.base.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdValueInstantiatorTest {

    private StdValueInstantiator instantiator;

    @Test
    public void testDefaultConstructor() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        assertEquals("UNKNOWN TYPE", instantiator.getValueTypeDesc());
        assertNull(instantiator.getValueClass());
    }

    @Test
    public void testConstructorWithJavaType() throws Exception {
        instantiator = new StdValueInstantiator(null, new ClassType(String.class));
        assertEquals("java.lang.String", instantiator.getValueTypeDesc());
        assertEquals(String.class, instantiator.getValueClass());
    }

    @Test
    public void testCopyConstructor() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        StdValueInstantiator copy = new StdValueInstantiator(instantiator);
        assertEquals(instantiator.getValueTypeDesc(), copy.getValueTypeDesc());
        assertEquals(instantiator.getValueClass(), copy.getValueClass());
    }

    @Test
    public void testCanCreateFromString() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromStringCreator(new AnnotatedWithParams(StringCreator.class));
        assertEquals(true, instantiator.canCreateFromString());

        Object obj = instantiator.createFromString(new DeserializationContext(), "test");
        assertNotNull(obj);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateFromStringThrowsException() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromStringCreator(new AnnotatedWithParams(UnknownStringCreator.class));
        instantiator.createFromString(new DeserializationContext(), "test");
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromIntCreator(new AnnotatedWithParams(IntCreator.class));
        assertEquals(true, instantiator.canCreateFromInt());

        Object obj = instantiator.createFromInt(new DeserializationContext(), 1);
        assertNotNull(obj);
    }

    @Test
    public void testCreateFromIntWhenNoCreator() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        assertEquals(false, instantiator.canCreateFromInt());

        Object obj = instantiator.createFromInt(new DeserializationContext(), 1);
        assertNull(obj);
    }

    @Test
    public void testCanCreateFromLong() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromLongCreator(new AnnotatedWithParams(LongCreator.class));
        assertEquals(true, instantiator.canCreateFromLong());

        Object obj = instantiator.createFromLong(new DeserializationContext(), 1L);
        assertNotNull(obj);
    }

    @Test
    public void testCreateFromLongWhenNoCreator() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        assertEquals(false, instantiator.canCreateFromLong());

        Object obj = instantiator.createFromLong(new DeserializationContext(), 1L);
        assertNull(obj);
    }

    @Test
    public void testCanCreateFromDouble() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromDoubleCreator(new AnnotatedWithParams(DoubleCreator.class));
        assertEquals(true, instantiator.canCreateFromDouble());

        Object obj = instantiator.createFromDouble(new DeserializationContext(), 1.0D);
        assertNotNull(obj);
    }

    @Test
    public void testCreateFromDoubleWhenNoCreator() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        assertEquals(false, instantiator.canCreateFromDouble());

        Object obj = instantiator.createFromDouble(new DeserializationContext(), 1.0D);
        assertNull(obj);
    }

    @Test
    public void testCanCreateFromBoolean() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromBooleanCreator(new AnnotatedWithParams(BooleanCreator.class));
        assertEquals(true, instantiator.canCreateFromBoolean());

        Object obj = instantiator.createFromBoolean(new DeserializationContext(), true);
        assertNotNull(obj);
    }

    @Test
    public void testCreateFromBooleanWhenNoCreator() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        assertEquals(false, instantiator.canCreateFromBoolean());

        Object obj = instantiator.createFromBoolean(new DeserializationContext(), true);
        assertNull(obj);
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromObjectSettings(new AnnotatedWithParams(DefaultCreator.class), null, null, null, null, null);
        assertEquals(true, instantiator.canCreateUsingDefault());

        Object obj = instantiator.createUsingDefault(new DeserializationContext());
        assertNotNull(obj);
    }

    @Test
    public void testCanCreateUsingDelegate() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromObjectSettings(null, new AnnotatedWithParams(DelegateCreator.class), new ClassType(String.class), null, null, null);
        assertEquals(true, instantiator.canCreateUsingDelegate());

        Object obj = instantiator.createUsingDelegate(new DeserializationContext(), new Object());
        assertNotNull(obj);
    }

    @Test
    public void testCanCreateUsingArrayDelegate() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromObjectSettings(null, null, null, new AnnotatedWithParams(ArrayDelegateCreator.class), null, null);
        assertEquals(true, instantiator.canCreateUsingArrayDelegate());

        Object[] args = new Object[]{new Object()};
        Object obj = instantiator.createUsingArrayDelegate(new DeserializationContext(), new Object());
        assertNotNull(obj);
        assertEquals(args[0].getClass().getComponentType(), obj.getClass().getComponentType());
    }

    @Test
    public void testCreateUsingDelegateThrowsException() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromObjectSettings(null, new AnnotatedWithParams(UnknownDelegateCreator.class), null, null, null, null);
        try {
            Object obj = instantiator.createUsingDelegate(new DeserializationContext(), new Object());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testCreateUsingArrayDelegateThrowsException() throws Exception {
        instantiator = new StdValueInstantiator(null, null);
        instantiator.configureFromObjectSettings(null, null, null, new AnnotatedWithParams(UnknownArrayDelegateCreator.class), null, null);
        try {
            Object[] args = new Object[]{new Object()};
            Object obj = instantiator.createUsingArrayDelegate(new DeserializationContext(), new Object());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}