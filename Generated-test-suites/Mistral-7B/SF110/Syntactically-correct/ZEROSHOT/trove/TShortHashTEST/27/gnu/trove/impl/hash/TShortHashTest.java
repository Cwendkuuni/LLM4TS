import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(5, hash.size());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hashWithInitialCapacity = new TShortHash(10);
 assertEquals(10, hashWithInitialCapacity.size());
 assertNull(hashWithInitialCapacity.getNoEntryValue());
 }

 @Test
 public void testConstructorWithLoadFactor() {
 TShortHash hashWithLoadFactor = new TShortHash(5, 0.75f);
 assertEquals(4, hashWithLoadFactor.size());
 assertNull(hashWithLoadFactor.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hashWithInitialCapacityAndLoadFactor = new TShortHash(10, 0.75f);
 assertEquals(7, hashWithInitialCapacityAndLoadFactor.size());
 assertNull(hashWithInitialCapacityAndLoadFactor.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hashWithInitialCapacityLoadFactorAndNoEntryValue = new TShortHash(10, 0.75f, 1);
 assertEquals(7, hashWithInitialCapacityLoadFactorAndNoEntryValue.size());
 assertEquals(1, hashWithInitialCapacityLoadFactorAndNoEntryValue.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 assertEquals(shortValue, hash.get((short) shortValue));
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.removeAt(0);
 assertEquals(4, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 assertEquals(2, hash.index((short) 2));
 assertEquals(-1, hash.index((short) 4));
 }
}