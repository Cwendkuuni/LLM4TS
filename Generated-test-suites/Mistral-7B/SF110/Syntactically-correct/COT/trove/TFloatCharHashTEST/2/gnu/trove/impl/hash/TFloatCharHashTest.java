package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TFloatCharHashTest {

    @org.junit.Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
    }

    @org.junit.Test
    public void testConstructWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @org.junit.Test
    public void testConstructWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(5, hash.size());
    }

    @org.junit.Test
    public void testConstructWithInitialCapacityLoadFactorAndNoEntryKeyNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(5, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @org.junit.Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey());
    }

    @org.junit.Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.capacity());
        hash.setUp(5);
        assertEquals(5, hash.capacity());
        assertNotNull(hash._set);
    }

    @org.junit.Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(1.0f));
        hash.put(1.0f, 'a');
        assertTrue(hash.contains(1.0f));
        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @org.junit.Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        assertEquals(3, hash.size());
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.containsKey(1.0f));
        assertEquals('b', hash.get('2'));
    }

    @org.junit.Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        assertEquals(2, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashSet() {
        TFloatCharHash hash = new TFloatCharHash(2);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c'); // This should throw an IllegalStateException
    }

    @org.junit.Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        hash.writeExternal(objectOutput);
        objectOutput.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = new ObjectInput(inputStream);
        TFloatCharHash hash2 = new TFloatCharHash(5);
        hash2.readExternal(objectInput);
        objectInput.close();
        assertEquals(hash.size(), hash2.size());
        assertEquals(hash.get(0), hash2.get(0));
    }
}