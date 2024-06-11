package com.example.junit; // replace with your package name if necessary

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash(5);
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\u0000', hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\u0000', hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\u0000', hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyNoEntryValue() {
        assertEquals(0, hash.size());
        assertNotNull(hash.getNoEntryKey());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001f);
        assertEquals('\u0000', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        assertEquals(0, hash.size());
    }

    @Test
    public void testContains() {
        hash.put(1.0f, 'a');
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value > 0.5f;
            }
        };
        hash.put(1.0f, 'a');
        hash.put(0.5f, 'b');
        hash.put(2.0f, 'c');

        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        hash.put(1.0f, 'a');
        hash.put(0.5f, 'b');
        hash.put(2.0f, 'c');

        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testPut() {
        hash.put(1.0f, 'a');
        hash.put(0.5f, 'b');
        hash.put(2.0f, 'c');

        assertEquals(3, hash.size());
        assertEquals(1, hash.index(1.0f));
        assertEquals(0.5f, hash.keyAt(0), 0.001f);
        assertEquals('b', hash.valueAt(0));
        assertEquals(2, hash.index(2.0f));
        assertEquals(2.0f, hash.keyAt(1), 0.001f);
        assertEquals('c', hash.valueAt(1));
        assertNull(hash.keyAt(2));
        assertEquals('\u0000', hash.valueAt(2));
    }

    @Test
    public void testPutOverCapacity() {
        hash = new TFloatCharHash(3);
        hash.put(1.0f, 'a');
        hash.put(1.5f, 'b');
        hash.put(2.0f, 'c');

        assertEquals(3, hash.size());
        assertEquals(2, hash.index(1.5f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();

        TFloatCharHash hashCopy = new TFloatCharHash(4);
        hashCopy.readExternal(new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray())));

        assertEquals(3, hash.size());
        assertEquals(3, hashCopy.size());
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();

        TFloatCharHash hashCopy = new TFloatCharHash(4);
        hashCopy.readExternal(new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray())));

        assertEquals(3, hash.size());
        assertEquals(3, hashCopy.size());
    }
}