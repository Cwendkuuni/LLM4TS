import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testTCharHashConstructor() {
 // Test default constructor
 TCharHash defaultCharHash = new TCharHash();
 assertEquals(0, defaultCharHash.size());
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultCharHash.getNoEntryValue());

 // Test constructor with initialCapacity
 TCharHash initialCapacityCharHash = new TCharHash(3);
 assertEquals(0, initialCapacityCharHash.size());
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, initialCapacityCharHash.getNoEntryValue());

 // Test constructor with initialCapacity and loadFactor
 TCharHash initialCapacityAndLoadFactorCharHash = new TCharHash(5, 0.75f);
 assertEquals(0, initialCapacityAndLoadFactorCharHash.size());
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, initialCapacityAndLoadFactorCharHash.getNoEntryValue());

 // Test constructor with initialCapacity, loadFactor, and no_entry_value
 TCharHash initialCapacityLoadFactorAndNoEntryValueCharHash = new TCharHash(5, 0.75f, 'X');
 assertEquals(0, initialCapacityLoadFactorAndNoEntryValueCharHash.size());
 assertEquals('X', initialCapacityLoadFactorAndNoEntryValueCharHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash.put('A', 'A');
 char c = charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, c);
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');

 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));

 assertFalse(charHash.contains('D'));
 }

 @Test
 public void testForEach() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');

 TCharProcedure charProcedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals(charValue, charHash.get(charValue));
 return true;
 }
 };

 assertTrue(charHash.forEach(charProcedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtWithInvalidIndex() {
 charHash.removeAt(-1);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');

 int index = charHash.indexOf('B');
 charHash.removeAt(index);
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testIndex() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');

 assertEquals(0, charHash.index('A'));
 assertEquals(1, charHash.index('B'));
 assertEquals(2, charHash.index('C'));

 assertEquals(-1, charHash.index('D'));
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');

 assertEquals(-1, charHash.indexOf('E'));
 assertEquals(5, charHash.size());

 charHash.put('E', 'E');
 assertEquals(6, charHash.size());
 assertEquals(5, charHash.indexOf('E'));
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.put('E', 'E');
 charHash.put('F', 'F');
 charHash.put('G', 'G');
 charHash.put('H', 'H');
 charHash.put('I', 'I');
 charHash.put('J', 'J');
 charHash.put('K', 'K');
 charHash.put('L', 'L');
 charHash.put('M', 'M');
 charHash.put('N', 'N');
 charHash.put('O', 'O');
 charHash.put('P', 'P');
 charHash.put('Q', 'Q');
 charHash.put('R', 'R');
 charHash.put('S', 'S');
 charHash.put('T', 'T');
 charHash.put('U', 'U');
 charHash.put('V', 'V');
 charHash.put('W', 'W');
 charHash.put('X', 'X');
 charHash.put('Y', 'Y');
 charHash.put('Z', 'Z');
 charHash.put('AA', 'AA');
 charHash.put('AB', 'AB');
 charHash.put('AC', 'AC');
 charHash.put('AD', 'AD');
 charHash.put('AE', 'AE');
 charHash.put('AF', 'AF');
 charHash.put('AG', 'AG');
 charHash.put('AH', 'AH');
 charHash.put('AI', 'AI');
 charHash.put('AJ', 'AJ');
 charHash.put('AK', 'AK');
 charHash.put('AL', 'AL');
 charHash.put('AM', 'AM');
 charHash.put('AN', 'AN');
 charHash.put('AO', 'AO');
 charHash.put('AP', 'AP');
 charHash.put('AQ', 'AQ');
 charHash.put('AR', 'AR');
 charHash.put('AS', 'AS');
 charHash.put('AT', 'AT');
 charHash.put('AU', 'AU');
 charHash.put('AV', 'AV');
 charHash.put('AW', 'AW');
 charHash.put('AX', 'AX');
 charHash.put('AY', 'AY');
 charHash.put('AZ', 'AZ');
 charHash.put('AAA', 'AAA');
 charHash.put('AAB', 'AAB');
 charHash.put('AAC', 'AAC');
 charHash.put('AAD', 'AAD');
 charHash.put('AAE', 'AAE');
 charHash.put('AAF', 'AAF');
 charHash.put('AAG', 'AAG');
 charHash.put('AAH', 'AAH');
 charHash.put('AAI', 'AAI');
 charHash.put('AAJ', 'AAJ');
 charHash.put('AAK', 'AAK');
 charHash.put('AAL', 'AAL');
 charHash.put('AAM', 'AAM');
 charHash.put('AAN', 'AAN');
 charHash.put('AOA', 'AOA');
 charHash.put('AOB', 'AOB');
 charHash.put('AOC', 'AOC');
 charHash.put('AOD', 'AOD');
 charHash.put('AOE', 'AOE');
 charHash.put('AOF', 'AOF');
 charHash.put('AOG', 'AOG');
 charHash.put('AOH', 'AOH');
 charHash.put('AOI', 'AOI');
 charHash.put('AOJ', 'AOJ');
 charHash.put('AOK', 'AOK');
 charHash.put('AOL', 'AOL');
 charHash.put('AOM', 'AOM');
 charHash.put('AON', 'AON');
 charHash.put('AOP', 'AOP');
 charHash.put('AOQ', 'AOQ');
 charHash.put('AOR', 'AOR');
 charHash.put('AOS', 'AOS');
 charHash.put('AOT', 'AOT');
 charHash.put('AUA', 'AUA');
 charHash.put('AUB', 'AUB');
 charHash.put('AUC', 'AUC');
 charHash.put('AUD', 'AUD');
 charHash.put('AUA', 'AUA');
 charHash.put('AUG', 'AUG');
 charHash.put('AUH', 'AUH');
 charHash.put('AUJ', 'AUJ');
 charHash.put('AUK', 'AUK');
 charHash.put('AUL', 'AUL');
 charHash.put('AUM', 'AUM');
 charHash.put('AUN', 'AUN');
 charHash.put('AUP', 'AUP');
 charHash.put('AUQ', 'AUQ');
 charHash.put('AUR', 'AUR');
 charHash.put('AUS', 'AUS');
 charHash.put('AUT', 'AUT');
 charHash.put('AUU', 'AUU');
 charHash.put('AUV', 'AUV');
 charHash.put('AUW', 'AUW');
 charHash.put('AUX', 'AUX');
 charHash.put('AUY', 'AUY');
 charHash.put('AUZ', 'AUZ');
 charHash.put('AAUA', 'AAUA');
 charHash.put('AAUB', 'AAUB');
 charHash.put('AAUC', 'AAUC');
 charHash.put('AAUD', 'AAUD');
 charHash.put('AAUE', 'AAUE');
 charHash.put('AAUF', 'AAUF');
 charHash.put('AAUG', 'AAUG');
 charHash.put('AAUH', 'AAUH');
 charHash.put('AAUI', 'AAUI');
 charHash.put('AAUJ', 'AAUJ');
 charHash.put('AAUK', 'AAUK');
 charHash.put('AAUL', 'AAUL');
 charHash.put('AAUM', 'AAUM');
 charHash.put('AAUN', 'AAUN');
 charHash.put('AAOP', 'AAOP');
 charHash.put('AAOQ', 'AAOQ');
 charHash.put('AAOR', 'AAOR');
 charHash.put('AAOS', 'AAOS');
 charHash.put('AAOT', 'AAOT');
 charHash.put('AAMX', 'AAMX');
 charHash.put('AAMY', 'AAMY');
 charHash.put('AAMZ', 'AAMZ');
 charHash.put('AAMA', 'AAMA');
 charHash.put('AAMB', 'AAMB');
 charHash.put('AAMC', 'AAMC');
 charHash.put('AAMD', 'AAMD');
 charHash.put('AAME', 'AAME');
 charHash.put('AAMF', 'AAMF');
 charHash.put('AAMG', 'AAMG');
 charHash.put('AAMH', 'AAMH');
 charHash.put('AAMI', 'AAMI');
 charHash.put('AAMJ', 'AAMJ');
 charHash.put('AAMK', 'AAMK');
 charHash.put('AAML', 'AAML');
 charHash.put('AAMM', 'AAMM');
 charHash.put('AAMN', 'AAMN');
 charHash.put('AAMO', 'AAMO');
 charHash.put('AAMQ', 'AAMQ');
 charHash.put('AAMR', 'AAMR');
 charHash.put('AAMS', 'AAMS');
 charHash.put('AAMT', 'AAMT');
 charHash.put('AAMU', 'AAMU');
 charHash.put('AAMV', 'AAMV');
 charHash.put('AAMW', 'AAMW');
 charHash.put('AAMX', 'AAMX');
 charHash.put('AAMY', 'AAMY');
 charHash.put('AAMZ', 'AAMZ');
 charHash.put('AAMA', 'AAMA');
 charHash.put('AAMA', 'AAMA'); // Duplicate key test

 assertEquals(67, charHash.size());
 }

}