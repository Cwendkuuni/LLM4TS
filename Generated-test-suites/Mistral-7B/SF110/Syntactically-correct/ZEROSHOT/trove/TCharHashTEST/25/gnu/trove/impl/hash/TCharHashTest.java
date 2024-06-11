import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 }

 @Test
 public void testDefaultConstructor() {
 assertNotNull(charHash.getNoEntryValue());
 assertEquals(11, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 charHash = new TCharHash(17);
 assertEquals(17, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(23, 0.75f);
 assertEquals(23, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 charHash = new TCharHash(29, 0.8f, 'Z');
 assertEquals(29, charHash.size());
 assertSame('Z', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash();
 assertNotNull(charHash.getNoEntryValue());
 charHash = new TCharHash(5);
 assertNotNull(charHash.getNoEntryValue());
 charHash = new TCharHash(5, 'X');
 assertSame('X', charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(3);
 assertEquals(3, charHash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInitialCapacityThrowsIAE() {
 charHash = new TCharHash(0);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testContains() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);

 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 assertTrue(charHash.contains('D'));
 assertTrue(charHash.contains('E'));

 assertFalse(charHash.contains('F'));
 }

 @Test
 public void testForEach() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);

 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 int value = charHash.get(charValue);
 assertEquals(value, 1 + value);
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);

 int indexToRemove = 2;
 charHash.removeAt(indexToRemove);
 assertEquals(4, charHash.size());
 assertFalse(charHash.contains('C'));
 }

 @Test
 public void testIndex() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);

 int indexOfA = charHash.index('A');
 int indexOfB = charHash.index('B');
 int indexOfC = charHash.index('C');
 int indexOfD = charHash.index('D');
 int indexOfE = charHash.index('E');
 int indexOfF = charHash.index('F');

 assertEquals(0, indexOfA);
 assertEquals(1, indexOfB);
 assertEquals(2, indexOfC);
 assertEquals(3, indexOfD);
 assertEquals(4, indexOfE);
 assertEquals(-1, indexOfF);
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 charHash.put('F', 6);
 charHash.put('G', 7);
 charHash.put('H', 8);
 charHash.put('I', 9);
 charHash.put('J', 10);
 charHash.put('K', 11);
 charHash.put('L', 12);
 charHash.put('M', 13);
 charHash.put('N', 14);
 charHash.put('O', 15);
 charHash.put('P', 16);
 charHash.put('Q', 17);
 charHash.put('R', 18);
 charHash.put('S', 19);
 charHash.put('T', 20);
 charHash.put('U', 21);
 charHash.put('V', 22);
 charHash.put('W', 23);
 charHash.put('X', 24);
 charHash.put('Y', 25);
 charHash.put('Z', 26);

 int indexOfA = charHash.index('A');
 int indexOfB = charHash.index('B');
 int indexOfC = charHash.index('C');
 int indexOfD = charHash.index('D');
 int indexOfE = charHash.index('E');
 int indexOfF = charHash.index('F');
 int indexOfG = charHash.index('G');
 int indexOfH = charHash.index('H');
 int indexOfI = charHash.index('I');
 int indexOfJ = charHash.index('J');
 int indexOfK = charHash.index('K');
 int indexOfL = charHash.index('L');
 int indexOfM = charHash.index('M');
 int indexOfN = charHash.index('N');
 int indexOfO = charHash.index('O');
 int indexOfP = charHash.index('P');
 int indexOfQ = charHash.index('Q');
 int indexOfR = charHash.index('R');
 int indexOfS = charHash.index('S');
 int indexOfT = charHash.index('T');
 int indexOfU = charHash.index('U');
 int indexOfV = charHash.index('V');
 int indexOfW = charHash.index('W');
 int indexOfX = charHash.index('X');
 int indexOfY = charHash.index('Y');
 int indexOfZ = charHash.index('Z');

 assertEquals(0, indexOfA);
 assertEquals(1, indexOfB);
 assertEquals(2, indexOfC);
 assertEquals(3, indexOfD);
 assertEquals(4, indexOfE);
 assertEquals(-1, indexOfF);
 assertEquals(25, indexOfG);
 assertEquals(26, indexOfH);
 assertEquals(27, indexOfI);
 assertEquals(28, indexOfJ);
 assertEquals(29, indexOfK);
 assertEquals(30, indexOfL);
 assertEquals(31, indexOfM);
 assertEquals(32, indexOfN);
 assertEquals(33, indexOfO);
 assertEquals(34, indexOfP);
 assertEquals(35, indexOfQ);
 assertEquals(36, indexOfR);
 assertEquals(37, indexOfS);
 assertEquals(38, indexOfT);
 assertEquals(39, indexOfU);
 assertEquals(40, indexOfV);
 assertEquals(41, indexOfW);
 assertEquals(42, indexOfX);
 assertEquals(43, indexOfY);
 assertEquals(44, indexOfZ);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 charHash.put('F', 6);
 charHash.put('G', 7);
 charHash.put('H', 8);
 charHash.put('I', 9);
 charHash.put('J', 10);
 charHash.put('K', 11);
 charHash.put('L', 12);
 charHash.put('M', 13);
 charHash.put('N', 14);
 charHash.put('O', 15);
 charHash.put('P', 16);
 charHash.put('Q', 17);
 charHash.put('R', 18);
 charHash.put('S', 19);
 charHash.put('T', 20);
 charHash.put('U', 21);
 charHash.put('V', 22);
 charHash.put('W', 23);
 charHash.put('X', 24);
 charHash.put('Y', 25);
 charHash.put('Z', 26);

 int indexOfA = charHash.index('A');
 int indexOfB = charHash.index('B');
 int indexOfC = charHash.index('C');
 int indexOfD = charHash.index('D');
 int indexOfE = charHash.index('E');
 int indexOfF = charHash.index('F');
 int indexOfG = charHash.index('G');
 int indexOfH = charHash.index('H');
 int indexOfI = charHash.index('I');
 int indexOfJ = charHash.index('J');
 int indexOfK = charHash.index('K');
 int indexOfL = charHash.index('L');
 int indexOfM = charHash.index('M');
 int indexOfN = charHash.index('N');
 int indexOfO = charHash.index('O');
 int indexOfP = charHash.index('P');
 int indexOfQ = charHash.index('Q');
 int indexOfR = charHash.index('R');
 int indexOfS = charHash.index('S');
 int indexOfT = charHash.index('T');
 int indexOfU = charHash.index('U');
 int indexOfV = charHash.index('V');
 int indexOfW = charHash.index('W');
 int indexOfX = charHash.index('X');
 int indexOfY = charHash.index('Y');
 int indexOfZ = charHash.index('Z');

 int indexToInsert = 45;
 char newKey = 'Z';
 charHash.put(newKey, indexToInsert);

 assertEquals(45, charHash.size());
 assertEquals(45, charHash.capacity());
 assertEquals(indexToInsert, charHash.index(newKey));
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 charHash.put('F', 6);
 charHash.put('G', 7);
 charHash.put('H', 8);
 charHash.put('I', 9);
 charHash.put('J', 10);
 charHash.put('K', 11);
 charHash.put('L', 12);
 charHash.put('M', 13);
 charHash.put('N', 14);
 charHash.put('O', 15);
 charHash.put('P', 16);
 charHash.put('Q', 17);
 charHash.put('R', 18);
 charHash.put('S', 19);
 charHash.put('T', 20);
 charHash.put('U', 21);
 charHash.put('V', 22);
 charHash.put('W', 23);
 charHash.put('X', 24);
 charHash.put('Y', 25);
 charHash.put('Z', 26);

 int indexOfA = charHash.index('A');
 int indexOfB = charHash.index('B');
 int indexOfC = charHash.index('C');
 int indexOfD = charHash.index('D');
 int indexOfE = charHash.index('E');
 int indexOfF = charHash.index('F');
 int indexOfG = charHash.index('G');
 int indexOfH = charHash.index('H');
 int indexOfI = charHash.index('I');
 int indexOfJ = charHash.index('J');
 int indexOfK = charHash.index('K');
 int indexOfL = charHash.index('L');
 int indexOfM = charHash.index('M');
 int indexOfN = charHash.index('N');
 int indexOfO = charHash.index('O');
 int indexOfP = charHash.index('P');
 int indexOfQ = charHash.index('Q');
 int indexOfR = charHash.index('R');
 int indexOfS = charHash.index('S');
 int indexOfT = charHash.index('T');
 int indexOfU = charHash.index('U');
 int indexOfV = charHash.index('V');
 int indexOfW = charHash.index('W');
 int indexOfX = charHash.index('X');
 int indexOfY = charHash.index('Y');
 int indexOfZ = charHash.index('Z');

 int indexToInsert = 45;
 char newKey = 'Z';
 charHash.put(newKey, indexToInsert);

 assertEquals(45, charHash.size());
 assertEquals(45, charHash.capacity());
 assertEquals(indexToInsert, charHash.index(newKey));
 }

 @After
 public void tearDown() {
 charHash = null;
 }
}