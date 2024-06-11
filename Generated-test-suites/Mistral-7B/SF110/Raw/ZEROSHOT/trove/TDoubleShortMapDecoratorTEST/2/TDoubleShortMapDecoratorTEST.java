import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleShortMapView;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyDecorator() {
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(1.0)));
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(new Double(1.0)));
 }

 @Test
 public void testDecoratorWithEntries() {
 _map.put(1.0, (short)1);
 _map.put(2.0, (short)2);

 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertEquals(2, decorator.size());
 assertFalse(decorator.isEmpty());
 assertSame(new Short((short)1), decorator.get(new Double(1.0)));
 assertSame(new Short((short)2), decorator.get(new Double(2.0)));
 assertSame(new Double(1.0), decorator.keySet().iterator().next().getKey());
 assertSame(new Double(2.0), decorator.keySet().iterator().next().getKey());

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertFalse(decorator.containsKey(null));

 TDoubleShortMapView view = _map.view();
 Iterator<Entry<Double, Short>> decoratorIterator = decorator.entrySet().iterator();
 Iterator<Entry<Double, Short>> mapIterator = view.entrySet().iterator();

 while (decoratorIterator.hasNext() && mapIterator.hasNext()) {
 Entry<Double, Short> decoratorEntry = decoratorIterator.next();
 Entry<Double, Short> mapEntry = mapIterator.next();

 assertEquals(decoratorEntry.getKey(), mapEntry.getKey());
 assertEquals(decoratorEntry.getValue(), mapEntry.getValue());
 }
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
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.clear();

 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(new Double(1.0)));
 assertNull(decorator.get(new Double(2.0)));
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertSame(new Short((short)1), decorator.remove(new Double(1.0)));
 assertSame(new Short((short)2), decorator.get(new Double(2.0)));

 assertEquals(1, decorator.size());
 assertFalse(decorator.isEmpty());
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 Iterator<Entry<Double, Short>> entrySetIterator = decorator.entrySet().iterator();
 Entry<Double, Short> entry = entrySetIterator.next();

 assertEquals(entry.getValue(), new Short((short)1));
 assertEquals(entry.getKey(), new Double(1.0));
 assertFalse(entrySetIterator.hasNext());
 }

 @Test
 public void testContainsValue() {
 _map.put(1.0, (short)1);
 _map.put(1.0, (short)2);
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(1.0), (short)2);

 assertTrue(decorator.containsValue(new Short((short)1)));
 assertTrue(decorator.containsValue(new Short((short)2)));
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();

 ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();

 assertEquals(decorator.size(), deserializedDecorator.size());
 }

 @After
 public void tearDown() {
 _map = null;
 decorator = null;
 }
}