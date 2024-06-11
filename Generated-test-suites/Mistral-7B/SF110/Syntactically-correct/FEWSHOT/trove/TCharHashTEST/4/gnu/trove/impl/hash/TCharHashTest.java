package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testConstructors() {
        TCharHash charHash = new TCharHash();
        TCharHash charHashWithInitialCapacity = new TCharHash(10);
        TCharHash charHashWithInitialCapacityAndLoadFactor = new TCharHash(10, 0.75f);
        TCharHash charHashWithInitialCapacityLoadFactorAndNoEntryValue = new TCharHash(10, 0.75f, 'N');

        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        assertEquals(10, charHashWithInitialCapacity.getCapacity());
        assertEquals(0.75f, charHashWithInitialCapacityAndLoadFactor.getLoadFactor(), 0.01);
        assertEquals('N', charHashWithInitialCapacityLoadFactorAndNoEntryValue.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash();
        int capacity = charHash.setUp(11);
        assertArrayEquals(new char[11], charHash._set);
        assertEquals(11, capacity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TCharHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TCharHash(5, -0.5f);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        TCharHash charHashWithCustomNoEntryValue = new TCharHash(5, 'C');
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        assertEquals('C', charHashWithCustomNoEntryValue.getNoEntryValue());
    }

    @Test
    public void testConsumeFreeSlot() {
        TCharHash charHash = new TCharHash(5);
        char addedKey = 'A';
        int index = charHash.insertKey(addedKey);
        assertTrue(charHash.consumeFreeSlot);
        assertEquals(addedKey, charHash._set[index]);

        charHash.consumeFreeSlot = false;
        char addedKeyTwo = 'B';
        assertEquals(-1, charHash.index(addedKeyTwo));
        index = charHash.insertKey(addedKeyTwo);
        assertTrue(charHash.consumeFreeSlot);
        assertEquals(addedKeyTwo, charHash._set[index]);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(5);
        char keyOne = 'A';
        char keyTwo = 'B';

        charHash.insertKey(keyOne);
        assertTrue(charHash.contains(keyOne));
        assertFalse(charHash.contains(keyTwo));

        charHash.insertKey(keyTwo);
        assertTrue(charHash.contains(keyOne));
        assertTrue(charHash.contains(keyTwo));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(5);
        char keyOne = 'A';
        char keyTwo = 'B';
        char keyThree = 'C';

        charHash.insertKey(keyOne);
        charHash.insertKey(keyTwo);
        charHash.insertKey(keyThree);

        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            @Override
            public boolean execute(char charValue) {
                count++;
                return true;
            }
        };

        assertTrue(charHash.forEach(procedure));
        assertEquals(3, procedure.getCount());
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(5);
        char keyOne = 'A';
        char keyTwo = 'B';
        char keyThree = 'C';

        charHash.insertKey(keyOne);
        charHash.insertKey(keyTwo);
        charHash.insertKey(keyThree);

        int indexOne = charHash.indexOf(keyOne);
        int indexTwo = charHash.indexOf(keyTwo);
        int indexThree = charHash.indexOf(keyThree);

        charHash.removeAt(indexOne);
        assertNotNull(charHash.get(indexTwo));
        assertNull(charHash.get(indexOne));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(5);
        char keyOne = 'A';
        char keyTwo = 'B';
        char keyThree = 'C';

        charHash.insertKey(keyOne);
        charHash.insertKey(keyTwo);
        charHash.insertKey(keyThree);

        int indexOne = charHash.indexOf(keyOne);
        int indexTwo = charHash.indexOf(keyTwo);
        int indexThree = charHash.indexOf(keyThree);

        assertEquals(indexOne, charHash.index(keyOne));
        assertEquals(indexTwo, charHash.index(keyTwo));
        assertEquals(indexThree, charHash.index(keyThree));
        assertEquals(-1, charHash.index('D'));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(5);
        char keyOne = 'A';
        char keyTwo = 'B';
        char keyThree = 'C';

        charHash.insertKey(keyOne);
        charHash.insertKey(keyTwo);
        charHash.insertKey(keyThree);

        assertEquals(keyOne, charHash._set[0]);
        assertEquals(keyTwo, charHash._set[1]);
        assertEquals(keyThree, charHash._set[2]);

        char addedKey = 'D';
        charHash.insertKey(addedKey);
        assertEquals(addedKey, charHash._set[3]);
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(5);
        char keyOne = 'A';
        char keyTwo = 'B';
        char keyThree = 'C';
        char keyFour = 'D';
        char keyFive = 'E';
        char keySix = 'F';

        charHash.insertKey(keyOne);
        charHash.insertKey(keyTwo);
        charHash.insertKey(keyThree);
        charHash.insertKey(keyFour);
        charHash.insertKey(keyFive);
        charHash.insertKey(keySix);

        int indexOne = charHash.indexOf(keyOne);
        int indexTwo = charHash.indexOf(keyTwo);
        int indexThree = charHash.indexOf(keyThree);
        int indexFour = charHash.indexOf(keyFour);
        int indexFive = charHash.indexOf(keyFive);
        int indexSix = charHash.indexOf(keySix);

        assertEquals(indexOne, charHash.index(keyOne));
        assertEquals(indexTwo, charHash.index(keyTwo));
        assertEquals(indexThree, charHash.index(keyThree));
        assertEquals(indexFour, charHash.index(keyFour));
        assertEquals(indexFive, charHash.index(keyFive));
        assertEquals(indexSix, charHash.index(keySix));
    }

    @Test
    public void testInsertKeyRehashCollision() {
        TCharHash charHash = new TCharHash(5);
        char keyOne = 'A';
        char keyTwo = 'B';
        char keyThree = 'C';
        char keyFour = 'A';
        char keyFive = 'E';
        char keySix = 'F';

        charHash.insertKey(keyOne);
        charHash.insertKey(keyTwo);
        charHash.insertKey(keyThree);

        charHash.insertKey(keyFour);
        int indexFour = charHash.indexOf(keyFour);
        assertEquals(indexFour, charHash.index(keyFour));

        charHash.insertKey(keyFive);
        int indexFive = charHash.indexOf(keyFive);
        assertEquals(-1, charHash.index(keyFour));
        assertEquals(indexFive, charHash.index(keyFive));
    }

    private int[] getRandomIndexes(int length) {
        Random random = new Random();
        int[] randomIndexes = new int[5];

        for (int i = 0; i < 5; i++) {
            randomIndexes[i] = random.nextInt(length);
        }

        return randomIndexes;
    }

    @Test
    public void testRemoveKeys() {
        TCharHash charHash = new TCharHash(10);
        char keys[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

        for (char key : keys) {
            charHash.insertKey(key);
        }

        int randomIndexes[] = getRandomIndexes(keys.length);

        for (int index : randomIndexes) {
            char removedKey = charHash._set[index];
            charHash.removeAt(index);
            assertNull(charHash.get(index));
            assertEquals(removedKey, charHash.getNoEntryValue());
        }
    }
}