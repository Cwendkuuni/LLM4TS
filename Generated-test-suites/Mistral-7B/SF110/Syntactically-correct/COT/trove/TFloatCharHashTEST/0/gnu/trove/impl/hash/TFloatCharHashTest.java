package gnu.trove.impl.hash.test;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash(10);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructorsWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash1 = new TFloatCharHash(10, 0.75f);
        assertEquals(10, hash1.capacity());
        assertEquals(0.75f, hash1.loadFactor(), 0.01f);

        TFloatCharHash hash2 = new TFloatCharHash(20, 0.5f);
        assertEquals(20, hash2.capacity());
        assertEquals(0.5f, hash2.loadFactor(), 0.01f);
    }

    @Test
    public void testConstructorsWithInitialCapacityLoadFactorAndNoEntryKeyNoEntryValue() {
        TFloatCharHash hash1 = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
        assertEquals(10, hash1.capacity());
        assertEquals(0.75f, hash1.loadFactor(), 0.01f);
        assertEquals(1.0f, hash1.getNoEntryKey());
        assertEquals('A', hash1.getNoEntryValue());

        TFloatCharHash hash2 = new TFloatCharHash(20, 0.5f, 2.0f, 'B');
        assertEquals(20, hash2.capacity());
        assertEquals(0.5f, hash2.loadFactor(), 0.01f);
        assertEquals(2.0f, hash2.getNoEntryKey());
        assertEquals('B', hash2.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int capacity = hash.setUp(5);
        assertEquals(5, capacity);
        assertEquals(5, hash.capacity());
        assertNull(hash._set);
    }

    @Test
    public void testContains() {
        hash.put(1.0f, 'A');
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);
        assertEquals(2, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        hash.put(1.0f, 'A');
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');

        int index = hash.index(1.0f);
        assertEquals(0, index);

        index = hash.index(2.0f);
        assertEquals(1, index);

        index = hash.index(3.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');

        int index = hash.insertKey(3.0f);
        assertEquals(2, index);
        assertEquals(3.0f, hash.get(2));

        index = hash.insertKey(1.0f); // should not insert key, as the hash is full
        assertEquals(-1, index);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(out);
        hash.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);
        TFloatCharHash newHash = new TFloatCharHash();
        newHash.readExternal(objectInput);
        objectInput.close();

        assertEquals(hash.size(), newHash.size());
        assertEquals(hash.getNoEntryKey(), newHash.getNoEntryKey());
        assertEquals(hash.getNoEntryValue(), newHash.getNoEntryValue());
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(out);
        hash.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);
        TFloatCharHash newHash = new TFloatCharHash();
        newHash.readExternal(objectInput);
        objectInput.close();

        assertEquals(hash.size(), newHash.size());
        assertEquals(hash.getNoEntryKey(), newHash.getNoEntryKey());
        assertEquals(hash.getNoEntryValue(), newHash.getNoEntryValue());

        for (float key : hash.keySet()) {
            assertEquals(hash.get(hash.indexOfKey(key)), newHash.get(newHash.indexOfKey(key)));
        }
    }
}