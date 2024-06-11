import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator _decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 _decorator = new TDoubleShortMapDecorator(_map);
 }

 @After
 public void tearDown() {
 _map.clear();
 _decorator = null;
 }

 @Test
 public void testEmptyMap() {
 assertTrue(_decorator.isEmpty());
 assertNull(_decorator.get(_map.getNoEntryKey()));
 }

 @Test
 public void testPut() {
 _decorator.put(1.0, (short)1);
 _decorator.put(2.0, (short)2);
 _decorator.put(3.0, (short)3);

 assertEquals((short)1, _decorator.get(1.0));
 assertEquals((short)2, _decorator.get(2.0));
 assertEquals((short)3, _decorator.get(3.0));

 assertNull(_decorator.get(4.0));

 _decorator.clear();
 assertTrue(_decorator.isEmpty());
 }

 @Test
 public void testPutNullKey() {
 _decorator.put(null, (short)5);
 _decorator.put(1.0, (short)1);

 assertNull(_decorator.get(null));
 assertEquals((short)1, _decorator.get(1.0));
 }

 @Test
 public void testRemove() {
 _decorator.put(1.0, (short)1);
 _decorator.put(2.0, (short)2);
 _decorator.put(3.0, (short)3);

 assertEquals((short)1, _decorator.remove(1.0));
 assertNull(_decorator.get(1.0));

 assertEquals((short)2, _decorator.get(2.0));
 assertEquals((short)3, _decorator.get(3.0));
 }

 @Test
 public void testClear() {
 _decorator.put(1.0, (short)1);
 _decorator.put(2.0, (short)2);
 _decorator.put(3.0, (short)3);

 _decorator.clear();

 assertTrue(_decorator.isEmpty());
 assertNull(_decorator.get(1.0));
 assertNull(_decorator.get(2.0));
 assertNull(_decorator.get(3.0));
 }

 @Test
 public void testSize() {
 _decorator.put(1.0, (short)1);
 _decorator.put(2.0, (short)2);
 _decorator.put(3.0, (short)3);

 assertEquals(3, _decorator.size());
 }

 @Test
 public void testContainsValue() {
 _decorator.put(1.0, (short)1);
 _decorator.put(2.0, (short)2);
 _decorator.put(3.0, (short)3);

 assertTrue(_decorator.containsValue((short)1));
 assertTrue(_decorator.containsValue((short)2));
 assertTrue(_decorator.containsValue((short)3));

 assertFalse(_decorator.containsValue(Byte.MIN_VALUE));
 }

 @Test
 public void testEntrySet() {
 TMap<Double, Short> entrySet = _decorator.entrySet();

 assertEquals(3, entrySet.size());

 for (Object entry : entrySet) {
 Entry<Double, Short> e = (Entry<Double, Short>)entry;
 assertSame(_decorator, e.getMap());
 assertEquals(1.0, e.getKey());
 assertEquals((short)1, e.getValue());
 }
 }

 @Test
 public void testContainsKey() {
 _decorator.put(1.0, (short)1);
 _decorator.put(2.0, (short)2);
 _decorator.put(3.0, (short)3);

 assertTrue(_decorator.containsKey(1.0));
 assertTrue(_decorator.containsKey(2.0));
 assertTrue(_decorator.containsKey(3.0));

 assertFalse(_decorator.containsKey(4.0));
 }

 @Test
 public void testEquals() {
 TDoubleShortMapDecorator d1 = new TDoubleShortMapDecorator(_map);
 TDoubleShortMapDecorator d2 = new TDoubleShortMapDecorator(_map);
 TDoubleShortMapDecorator d3 = new TDoubleShortMapDecorator(_map);

 TDoubleShortMapDecorator d4 = new TDoubleShortMapDecorator(new TDoubleShortMap());

 assertEquals(d1, d2);
 assertEquals(d1, d3);

 assertNotEquals(d1, d4);
 }

 @Test
 public void testClone() {
 TDoubleShortMapDecorator original = new TDoubleShortMapDecorator(_map);
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)original.clone();

 assertEquals(original, cloned);
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 _decorator.writeExternal(out);

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)in.readObject();

 assertEquals(_map, cloned.getMap());
 }
}