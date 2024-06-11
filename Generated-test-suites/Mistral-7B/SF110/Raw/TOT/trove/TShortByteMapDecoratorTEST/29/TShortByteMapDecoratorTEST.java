import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.hash.TShortByteHash;
import java.util.NoSuchElementException;
import java.util.Collections;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMapImpl(new TShortByteHash(11));
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testPutNullKey() {
 decorator.put((Short) null, (Byte) 0);
 assertNull(decorator.get((Short) null));
 }

 @Test
 public void testPutValidKeyValue() {
 decorator.put((Short) 1, (Byte) 2);
 assertEquals((Byte) 2, decorator.get((Short) 1));
 }

 @Test
 public void testPutValidKeyValueAgain() {
 decorator.put((Short) 1, (Byte) 3);
 assertEquals((Byte) 3, decorator.get((Short) 1));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put((Short) 1, null);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testSize() {
 decorator.put((Short) 1, (Byte) 1);
 decorator.put((Short) 2, (Byte) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> set = decorator.entrySet();
 assertTrue(set.isEmpty());
 decorator.put((Short) 1, (Byte) 1);
 decorator.put((Short) 2, (Byte) 2);
 set = decorator.entrySet();
 assertEquals(2, set.size());
 }

 @Test
 public void testContainsKey() {
 decorator.put((Short) 1, (Byte) 1);
 decorator.put((Short) 2, (Byte) 2);
 assertTrue(decorator.containsKey((Short) 1));
 assertTrue(decorator.containsKey((Short) 2));
 assertFalse(decorator.containsKey((Short) 3));
 }

 @Test
 public void testContainsValue() {
 decorator.put((Short) 1, (Byte) 1);
 decorator.put((Short) 1, (Byte) 2);
 assertTrue(decorator.containsValue((Byte) 1));
 assertTrue(decorator.containsValue((Byte) 2));
 assertFalse(decorator.containsValue((Byte) 3));
 }

 @Test
 public void testRemoveKey() {
 decorator.put((Short) 1, (Byte) 1);
 decorator.put((Short) 1, (Byte) 2);
 assertTrue(decorator.containsKey((Short) 1));
 decorator.remove((Short) 1);
 assertFalse(decorator.containsKey((Short) 1));
 }

 @Test
 public void testRemoveKeyNull() {
 decorator.put((Short) 1, (Byte) 1);
 decorator.put((Short) 1, (Byte) 2);
 assertFalse(decorator.remove((Short) null));
 }

 @Test
 public void testRemoveValue() {
 decorator.put((Short) 1, (Byte) 1);
 decorator.put((Short) 1, (Byte) 2);
 byte valueToRemove = decorator.get((Short) 1);
 decorator.remove((Byte) valueToRemove);
 assertFalse(decorator.containsValue(valueToRemove));
 }

 @Test
 public void testRemoveKeyValueNotPresent() {
 decorator.put((Short) 1, (Byte) 1);
 decorator.remove((Byte) 3);
 assertFalse(decorator.containsValue(3));
 }

 @Test
 public void testGetNoEntryKey() {
 Short key = decorator.getMap().getNoEntryKey();
 assertNull(decorator.get(key));
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetNoEntryValue() {
 assertEquals(null, decorator.get((Short) null));
 assertEquals(null, decorator.get((Short) 0));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 decorator.put((Short) 1, (Byte) 1);
 assertEquals(null, decorator.get(null));
 }

 @Test
 public void testIteration() {
 decorator.put((Short) 1, (Byte) 1);
 decorator.put((Short) 2, (Byte) 2);
 decorator.put((Short) 3, (Byte) 3);
 TShortByteIterator it = decorator.getMap().iterator();
 assertEquals((Short) 1, it.key());
 assertEquals((Byte) 1, it.value());
 it.advance();
 assertEquals((Short) 2, it.key());
 assertEquals((Byte) 2, it.value());
 it.advance();
 assertEquals((Short) 3, it.key());
 assertEquals((Byte) 3, it.value());
 it.advance();
 assertFalse(it.hasNext());
 }

 @Test
 public void testClone() {
 TShortByteMap DecoratorClone = decorator.clone();
 decorator.put((Short) 1, (Byte) 1);
 assertFalse(decorator.equals(DecoratorClone));
 DecoratorClone.put((Short) 1, (Byte) 1);
 assertTrue(decorator.equals(DecoratorClone));
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator newDecorator = (TShortByteMapDecorator) in.readObject();
 assertTrue(decorator.equals(newDecorator));
 }

 @Test
 public void testExternalizableWithEmptyMap() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 TShortByteMapDecorator emptyDecorator = new TShortByteMapDecorator();
 emptyDecorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator newEmptyDecorator = (TShortByteMapDecorator) in.readObject();
 TShortByteMap emptyMap = new TShortByteMapImpl(new TShortByteHash(11));
 assertTrue(newEmptyDecorator.getMap().equals(emptyMap));
 }
}