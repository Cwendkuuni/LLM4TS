import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.view.TDoubleMapView;
import gnu.trove.map.view.TObjectMapView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
 assertEquals(1, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(new Double(1.0), null);
 }

 @Test
 public void testPutExistingKey() {
 decorator.put(new Double(1.0), (short)2);
 assertEquals((short)2, decorator.get(new Double(1.0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutDuplicateKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(1.0), (short)2);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testIteration() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 Iterator<Entry<Double, Short>> it = decorator.entrySet().iterator();
 assertEquals(new Double(1.0), it.next().getKey());
 assertEquals((short)1, it.next().getValue());
 assertEquals(new Double(2.0), it.next().getKey());
 assertEquals((short)2, it.next().getValue());
 assertEquals(new Double(3.0), it.next().getKey());
 assertEquals((short)3, it.next().getValue());
 assertFalse(it.hasNext());
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 assertTrue(decorator.containsKey(new Double(1.0)));
 assertFalse(decorator.containsKey(new Double(2.0)));
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 assertTrue(decorator.containsValue((short)1));
 assertFalse(decorator.containsValue((short)2));
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 assertEquals((short)1, decorator.remove(new Double(1.0)));
 assertEquals(1, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveNonExistentKey() {
 decorator.remove(new Double(1.0));
 }

 @Test
 public void testClone() {
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)decorator.clone();
 decorator.put(new Double(1.0), (short)1);
 assertNotSame(decorator, cloned);
 assertEquals(decorator.size(), cloned.size());
 }

 @Test
 public void testExternalizable() {
 TObjectDoubleMap map = new HashMap<>();
 map.put(new Double(1.0), new Short((byte)1));
 map.put(new Double(2.0), new Short((byte)2));
 TDoubleShortMap decoratedMap = new TDoubleShortMapDecorator(new TDoubleShortMap(map));
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 decoratedMap.writeExternal(objOut);
 ObjectInput objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator((TDoubleShortMap)objIn.readObject());
 assertEquals(decoratedMap.size(), readDecorator.size());
 }
}