import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @After
 public void tearDown() {
 _map.clear();
 decorator.clear();
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)null));
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsValue((Byte)0));
 assertFalse(decorator.containsValue((Byte)1));
 assertFalse(decorator.containsValue((Byte)null));
 }

 @Test
 public void testPut() {
 Short key1 = (Short)1;
 Byte value1 = (Byte)1;
 decorator.put(key1, value1);
 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey(key1));
 assertTrue(decorator.containsValue(value1));
 assertEquals(value1, decorator.get(key1));

 Short key2 = (Short)-1;
 Byte value2 = (Byte)-1;
 decorator.put(key2, value2);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey(key2));
 assertTrue(decorator.containsValue(value2));
 assertEquals(value2, decorator.get(key2));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 decorator.put(null, (Byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 decorator.put((Short)1, null);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsKey((Short)-1));
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 decorator.put(new Short((short)1), new Byte((byte)1));
 decorator.put(new Short((short)-1), new Byte((byte)-1));
 assertEquals(2, entrySet.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TShortByteMapDecorator dec = (TShortByteMapDecorator)in.readObject();
 assertEquals(decorator.size(), dec.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator clone = (TShortByteMapDecorator)decorator.clone();
 assertEquals(decorator.size(), clone.size());
 }

 @Test
 public void testContainsValue() {
 Byte value = (Byte)1;
 assertTrue(decorator.containsValue(value));
 assertFalse(decorator.containsValue((Byte)0));
 }

 @Test
 public void testContainsKey() {
 Short key = (Short)1;
 assertTrue(decorator.containsKey(key));
 assertFalse(decorator.containsKey((Short)0));
 }
}