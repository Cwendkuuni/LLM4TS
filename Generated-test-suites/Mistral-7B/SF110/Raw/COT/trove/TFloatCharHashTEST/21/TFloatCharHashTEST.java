import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TFloatCharHash(10);
 }

 @Test
 public void testConstructor() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash();
 assertNotNull(tFloatCharHash);

 TFloatCharHash tFloatCharHash1 = new TFloatCharHash(5);
 assertNotNull(tFloatCharHash1);

 TFloatCharHash tFloatCharHash2 = new TFloatCharHash(5, 0.5f);
 assertNotNull(tFloatCharHash2);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash();
 assertEquals(0.0f, tFloatCharHash.getNoEntryKey(), 0.01f);

 TFloatCharHash tFloatCharHash1 = new TFloatCharHash(5);
 assertEquals(0.0f, tFloatCharHash1.getNoEntryKey(), 0.01f);

 TFloatCharHash tFloatCharHash2 = new TFloatCharHash(5, 1.0f);
 assertEquals(1.0f, tFloatCharHash2.getNoEntryKey(), 0.01f);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash();
 assertEquals('\u0000', tFloatCharHash.getNoEntryValue());

 TFloatCharHash tFloatCharHash1 = new TFloatCharHash(5);
 assertEquals('\u0000', tFloatCharHash1.getNoEntryValue());

 TFloatCharHash tFloatCharHash2 = new TFloatCharHash(5, 'A');
 assertEquals('A', tFloatCharHash2.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash(10);
 assertEquals(10, tFloatCharHash.size());

 float[] expectedSet = new float[10];
 assertArrayEquals(expectedSet, tFloatCharHash._set);
 }

 @Test
 public void testContains() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash();
 assertFalse(tFloatCharHash.contains(1.0f));

 TFloatCharHash tFloatCharHash1 = new TFloatCharHash(5);
 tFloatCharHash1.insert(2.0f);
 assertFalse(tFloatCharHash1.contains(1.0f));
 tFloatCharHash1.insert(2.0f); // duplicate
 assertFalse(tFloatCharHash1.contains(1.0f));

 TFloatCharHash tFloatCharHash2 = new TFloatCharHash(5);
 tFloatCharHash2.insert(2.0f);
 assertTrue(tFloatCharHash2.contains(2.0f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash();
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 0.0f, 0.01f);
 return true;
 }
 };
 assertFalse(tFloatCharHash.forEach(procedure));

 TFloatCharHash tFloatCharHash1 = new TFloatCharHash(5);
 tFloatCharHash1.insert(1.0f);
 tFloatCharHash1.insert(2.0f);
 tFloatCharHash1.insert(3.0f);
 assertTrue(tFloatCharHash1.forEach(procedure));
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtNegativeIndex() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash(5);
 tFloatCharHash.removeAt(-1);
 fail("Expected IllegalStateException");
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtIndexOutOfBounds() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash(5);
 tFloatCharHash.insert(1.0f);
 tFloatCharHash.insert(2.0f);
 tFloatCharHash.insert(3.0f);
 tFloatCharHash.removeAt(5);
 fail("Expected IllegalStateException");
 }

 @Test
 public void testIndex() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash();
 assertFalse(tFloatCharHash.contains(1.0f));
 assertEquals(-1, tFloatCharHash.index(1.0f));

 TFloatCharHash tFloatCharHash1 = new TFloatCharHash(5);
 tFloatCharHash1.insert(1.0f);
 tFloatCharHash1.insert(2.0f);
 tFloatCharHash1.insert(3.0f);
 assertEquals(1, tFloatCharHash1.index(1.0f));
 assertEquals(-1, tFloatCharHash1.index(4.0f));
 }

 @Test
 public void testIndexRehashed() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash(5);
 tFloatCharHash.insert(1.0f);
 tFloatCharHash.insert(2.0f);
 tFloatCharHash.insert(3.0f);
 tFloatCharHash.insert(4.0f);
 tFloatCharHash.insert(5.0f);

 int index = tFloatCharHash.index(6.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash(5);
 int index = tFloatCharHash.insertKey(1.0f);
 assertEquals(1, index);
 assertEquals(1.0f, tFloatCharHash.get(index), 0.01f);

 TFloatCharHash tFloatCharHash1 = new TFloatCharHash(5);
 tFloatCharHash1.insertKey(1.0f);
 tFloatCharHash1.insertKey(1.0f); // duplicate
 assertEquals(1, tFloatCharHash1.size());
 assertEquals(1.0f, tFloatCharHash1.get(0), 0.01f);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 TFloatCharHash tFloatCharHash = new TFloatCharHash(5);
 for (float i = 0.0f; i < 6.0f; ++i) {
 tFloatCharHash.insertKey(i);
 }
 fail("Expected IllegalStateException");
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatCharHash tFloatCharHash = new TFloatCharHash(5);
 tFloatCharHash.insert(1.0f);
 tFloatCharHash.insert(2.0f);
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(output);
 tFloatCharHash.writeExternal(out);
 out.close();

 ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
 ObjectInput in = new ObjectInput(input);
 TFloatCharHash readTFloatCharHash = new TFloatCharHash();
 readTFloatCharHash.readExternal(in);

 assertEquals(2, readTFloatCharHash.size());
 assertEquals(1.0f, readTFloatCharHash.get(0), 0.01f);
 assertEquals(2.0f, readTFloatCharHash.get(1), 0.01f);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(output);
 TFloatCharHash tFloatCharHash = new TFloatCharHash();
 out.writeByte(0);
 out.writeInt(5);
 out.writeByte(1);
 out.writeFloat(1.0f);
 out.writeChar('A');
 out.writeByte(1);
 out.writeFloat(2.0f);
 out.writeChar('B');
 out.writeByte(0);
 out.writeFloat(0.0f);
 out.writeChar('\u0000');
 out.close();

 ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
 ObjectInput in = new ObjectInput(input);
 TFloatCharHash readTFloatCharHash = new TFloatCharHash();
 readTFloatCharHash.readExternal(in);

 assertEquals(2, readTFloatCharHash.size());
 assertEquals(1.0f, readTFloatCharHash.get(0), 0.01f);
 assertEquals('A', readTFloatCharHash.getNoEntryValue());
 assertEquals(2.0f, readTFloatCharHash.get(1), 0.01f);
 assertEquals('B', readTFloatCharHash.getNoEntryValue());
 }
}