package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHashConstruction() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
        TFloatCharHash hashWithInitialCapacity = new TFloatCharHash(10);
        assertNotNull(hashWithInitialCapacity);
        TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(10, 0.75f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);
        TFloatCharHash hashWithInitialCapacityLoadFactorAndValues = new TFloatCharHash(10, 0.75f, 0.0f, '\0');
        assertNotNull(hashWithInitialCapacityLoadFactorAndValues);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.001f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char noEntryValue = hash.getNoEntryValue();
        assertEquals('\0', noEntryValue);
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        int capacity = hash.setUp(5);
        assertEquals(5, capacity);
        hash = new TFloatCharHash(10);
        capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));

        assertFalse(hash.contains(4.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);
        assertEquals(3, count);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtWithIndexOutOfBounds() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.removeAt(4);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(2, hash.index(3.0f));

        assertEqual(-1, hash.index(4.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);

        assertEquals(2, hash.indexRehashed(4.0f, 0, Integer.MAX_VALUE, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.insertKey(4.0f);
        assertEquals(4, hash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithKeyAlreadyPresent() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.insertKey(2.0f);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);

        int index = hash.insertKeyRehash(5.0f, 0, Integer.MAX_VALUE, (byte) 0);
        assertEquals(2, index);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        hash.writeExternal(objStream);

        TFloatCharHash hashFromByteStream = new TFloatCharHash();
        hashFromByteStream.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));

        assertEquals(3, hashFromByteStream.size());
        assertEquals(1.0f, hashFromByteStream.get(0), 0.001f);
        assertEquals(2.0f, hashFromByteStream.get(1), 0.001f);
        assertEquals(3.0f, hashFromByteStream.get(2), 0.001f);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        hash.writeExternal(objStream);
        objStream.close();
        byteStream.close();

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objInputStream = new ObjectInputStream(byteInputStream);
        TFloatCharHash hashFromByteStream = (TFloatCharHash) objInputStream.readObject();
        objInputStream.close();
        byteInputStream.close();

        assertEquals(0, hashFromByteStream.size());

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        byteStream = new ByteArrayOutputStream();
        objStream = new ObjectOutputStream(byteStream);
        hash.writeExternal(objStream);
        objStream.close();
        byteStream.close();

        byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
        objInputStream = new ObjectInputStream(byteInputStream);
        hashFromByteStream = (TFloatCharHash) objInputStream.readObject();
        objInputStream.close();
        byteInputStream.close();

        assertEquals(3, hashFromByteStream.size());
        assertEquals(1.0f, hashFromByteStream.get(0), 0.001f);
        assertEquals(2.0f, hashFromByteStream.get(1), 0.001f);
        assertEquals(3.0f, hashFromByteStream.get(2), 0.001f);
    }
}