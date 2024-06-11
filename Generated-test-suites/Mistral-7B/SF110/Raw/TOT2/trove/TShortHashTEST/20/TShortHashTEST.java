import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    // Methods 1-12 from Expert 1

    // Method 13 from Expert 2
    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash(3);
        hash.insert((short) 1);
        hash.insert((short) 2);
        hash.insert((short) 3);
        hash.insert((short) 4);
        int index = hash.index((short) 4);
        assertEquals(-1, index);
    }

    // Methods 14 and 15 from Expert 3
    @Test
    public void testInsertKeyDuplicateKey() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) 1);
        hash.insertKey((short) 1);
        assertEquals(2, hash.size());
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyNegativeKey() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) -1);
    }

    @Test
    public void testTShortHashFull() {
        TShortHash hash = new TShortHash(3);
        for (short i = 0; i <= 3; i++) {
            hash.insertKey(i);
        }
        assertEquals(3, hash.size());
        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertFalse(hash.insertKey((short) 4));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains((short) 2));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 3));
    }

    @Test
    public void testInsertKeyRehashWithCollision() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 1);
        int index = hash.index((short) 1);
        assertEquals(2, index);
    }

    @Test
    public void testInsertKeyRehashWithRemoveSlot() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.removeAt(1);
        hash.insertKey((short) 4);
        int index = hash.index((short) 4);
        assertEquals(2, index);
    }

    @Test
    public void testConsumeFreeSlot() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.consumeFreeSlot = true;
        hash.insertKey((short) 4);
        int index = hash.index((short) 4);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        hash.insertKeyAt(1, (short) 5);
        assertEquals(4, hash.size());
        assertTrue(hash.contains((short) 5));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
    }

    @Test
    public void testTShortHashEmptyRehash() {
        TShortHash hash = new TShortHash(3);
        hash.insertKey((short) 1);
        assertEquals(1, hash.size());
        assertTrue(hash.contains((short) 1));
        hash.setUp(5);
        assertEquals(1, hash.size());
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testTShortHashFullRehash() {
        TShortHash hash = new TShortHash(3);
        for (short i = 0; i <= 3; i++) {
            hash.insertKey(i);
        }
        assertEquals(3, hash.size());
        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        hash.setUp(5);
        assertEquals(4, hash.size());
        assertTrue(hash.contains((short) 0));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertTrue(hash.contains((short) 4));
    }
}