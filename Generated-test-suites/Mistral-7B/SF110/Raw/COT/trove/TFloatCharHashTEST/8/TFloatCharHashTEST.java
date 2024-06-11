package tests; // replace with your test package, if needed

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
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
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(1.0f, key, 0.001f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtNegativeIndex() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.removeAt(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtIndexOutOfBounds() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(3);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(1.0f);
        int index1 = hash.index(1.0f);
        assertEquals(0, index1);
        int index2 = hash.index(2.0f);
        assertEquals(1, index2);
        int index3 = hash.index(3.0f);
        assertEquals(-1, index3);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(1.0f);
        assertEquals(3, hash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyDuplicateKey() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(1.0f);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.XinsertKey(1.0f);
        hash.XinsertKey(2.0f);
        hash.XinsertKey(1.0f);
        assertEquals(3, hash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testXinsertKeyDuplicateKey() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.XinsertKey(1.0f);
        hash.XinsertKey(1.0f);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.writeExternal(new ByteArrayOutputStream());
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        TFloatCharHash original = new TFloatCharHash(3);
        original.insertKey(1.0f);
        original.writeExternal(byteStream);
        TFloatCharHash read = new TFloatCharHash(3);
        read.readExternal(new ByteArrayInputStream(byteStream.toByteArray()));
        assertEquals(original.getNoEntryKey(), read.getNoEntryKey(), 0.001f);
        assertEquals(original.getNoEntryValue(), read.getNoEntryValue());
        assertTrue(original.contains(1.0f));
        assertTrue(read.contains(1.0f));
    }

}