import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.io.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortHashMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @After
 public void tearDown() {
 _map.clear();
 decorator = null;
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsValue((short) 1));
 }

 @Test
 public void testPutGetKeyExists() {
 decorator.put(1.0, (short) 1);
 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey(1.0));
 assertEquals((Short) Short.valueOf((short) 1), decorator.get(1.0));
 }

 @Test
 public void testPutGetKeyDoesNotExist() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 assertNull(decorator.get(3.0));
 assertEquals((Short) Short.valueOf((short) 1), decorator.get(1.0));
 }

 @Test
 public void testPutNullKey() {
 decorator.put(1.0, (short) 1);
 assertNull(decorator.get(null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testRemoveKeyExists() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 decorator.remove(1.0);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test(expected = KeyNotFoundException.class)
 public void testRemoveKeyDoesNotExist() {
 decorator.remove(3.0);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(decorator.size(), entrySet.size());
 for (Entry<Double, Short> entry : entrySet) {
 Double key = entry.getKey();
 Short value = entry.getValue();
 assertEquals(key, decorator.get(key));
 assertEquals(value, decorator.get(key));
 }
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 1);
 assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TDoubleShortMapDecorator decoratorCopy = (TDoubleShortMapDecorator)in.readObject();
 assertEquals(decorator, decoratorCopy);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TDoubleShortMapDecorator original = new TDoubleShortMapDecorator(_map);
 TDoubleShortMapDecorator clone = (TDoubleShortMapDecorator)original.clone();
 assertEquals(original, clone);
 }
}