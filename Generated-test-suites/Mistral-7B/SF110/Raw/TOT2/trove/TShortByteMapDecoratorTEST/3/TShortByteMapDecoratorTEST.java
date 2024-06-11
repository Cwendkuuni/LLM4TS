import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

    TShortByteMap map;
    TShortByteMapDecorator decorator;

    @Before
    public void setup() {
        map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void testPut() {
        Byte value = new Byte((byte) 10);
        Short key = new Short((short) 5);

        Byte putValue = decorator.put(key, value);
        assertEquals(value, putValue);
        assertTrue(decorator.containsKey(key));
        assertEquals(value, decorator.get(key));

        value = new Byte((byte) 20);
        putValue = decorator.put(key, value);
        assertNotEquals(value, putValue); // old value should be returned if key already exists
        assertEquals(value, decorator.get(key));

        Byte nullValue = null;
        Short nullKey = null;
        Byte putNullValue = decorator.put(nullKey, nullValue);
        assertNull(putNullValue); // null value should be ignored
        assertFalse(decorator.containsKey(nullKey));

        Byte noEntryValue = decorator.getNoEntryValue();
        Byte putNoEntryValue = decorator.put(new Short((short) 0), noEntryValue);
        assertEquals(noEntryValue, putNoEntryValue);
        assertTrue(decorator.containsKey((Short) 0));
        assertEquals(noEntryValue, decorator.get((Short) 0));
    }

    @Test
    public void testGet() {
        Byte value = new Byte((byte) 10);
        Short key = new Short((short) 5);
        decorator.put(key, value);

        Byte getValue = decorator.get(key);
        assertEquals(value, getValue);

        value = new Byte((byte) 20);
        decorator.put(key, value);
        getValue = decorator.get(key);
        assertEquals(value, getValue);

        Short nullKey = null;
        Byte getNullValue = decorator.get(nullKey);
        assertNull(getNullValue);

        Byte noEntryValue = decorator.getNoEntryValue();
        getValue = decorator.get(new Short((short) 0));
        assertEquals(noEntryValue, getValue);

        try {
            decorator.get(new Object());
            fail();
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testClear() {
        Byte value = new Byte((byte) 10);
        Short key = new Short((short) 5);
        decorator.put(key, value);

        decorator.clear();

        assertFalse(decorator.containsKey(key));
        assertNull(decorator.get(key));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        Byte value = new Byte((byte) 10);
        Short key = new Short((short) 5);
        decorator.put(key, value);

        Byte removedValue = decorator.remove(key);
        assertEquals(value, removedValue);
        assertFalse(decorator.containsKey(key));
        assertEquals(0, decorator.size());

        Short nullKey = null;
        Byte removedNullValue = decorator.remove(nullKey);
        assertNull(removedNullValue);
        assertEquals(1, decorator.size()); // no entry should not be removed
    }

    // other test cases for entrySet(), containsValue(), containsKey(), size(), isEmpty(), putAll(), readExternal(), writeExternal()
}