import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(11, 0.5f, '\u0000');
 }

 @Test
 public void testEmptyHash() {
 assertFalse(charHash.contains('\u0001'));
 assertEquals(-1, charHash.index('\u0001'));
 }

 @Test
 public void testBasicOperations() {
 charHash.put('\u0002', 0);
 assertTrue(charHash.contains('\u0002'));
 assertEquals(0, charHash.indexOf('\u0002'));
 charHash.put('\u0003', 1);
 assertTrue(charHash.contains('\u0003'));
 assertEquals(1, charHash.indexOf('\u0003'));
 charHash.put('\u0002', 2);
 assertFalse(charHash.contains('\u0002'));
 assertEquals(1, charHash.indexOf('\u0003'));
 }

 @Test
 public void testLoadFactor() {
 charHash.put('\u0004', 0);
 charHash.put('\u0005', 1);
 charHash.put('\u0006', 2);
 charHash.put('\u0007', 3);
 charHash.put('\u0008', 4);
 charHash.put('\u0009', 5);
 charHash.put('\u000a', 6);
 charHash.put('\u000b', 7);
 charHash.put('\u000c', 8);
 charHash.put('\u000d', 9);
 charHash.put('\u000e', 10);
 charHash.put('\u000f', 11);

 assertEquals(12, charHash.size());
 assertTrue(charHash.contains('\u0004' - '\u0020'));
 assertEquals(0, charHash.indexOf('\u0004' - '\u0020'));

 charHash.put('\u0010', 12);
 assertEquals(13, charHash.size());
 assertTrue(charHash.contains('\u0010'));
 assertEquals(12, charHash.indexOf('\u0010'));
 }

 @Test
 public void testRemove() {
 charHash.put('\u0002', 0);
 charHash.put('\u0003', 1);
 charHash.put('\u0004', 2);
 charHash.remove('\u0003');
 assertFalse(charHash.contains('\u0003'));
 assertEquals(2, charHash.size());
 assertEquals(1, charHash.indexOf('\u0004'));
 }

 @Test
 public void testRehash() {
 charHash.put('\u0011', 0);
 charHash.put('\u0012', 1);
 charHash.put('\u0013', 2);
 charHash.put('\u0014', 3);
 charHash.put('\u0015', 4);
 charHash.put('\u0016', 5);
 charHash.put('\u0017', 6);
 charHash.put('\u0018', 7);
 charHash.put('\u0019', 8);
 charHash.put('\u001a', 9);
 charHash.put('\u001b', 10);
 charHash.put('\u001c', 11);
 charHash.put('\u001d', 12);
 charHash.put('\u001e', 13);
 charHash.put('\u001f', 14);

 assertEquals(15, charHash.size());
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 if (charValue == '\u0011') {
 charHash.remove(charValue);
 return false;
 }
 return true;
 }
 };
 assertFalse(charHash.forEach(procedure));
 assertEquals(14, charHash.size());
 }
}