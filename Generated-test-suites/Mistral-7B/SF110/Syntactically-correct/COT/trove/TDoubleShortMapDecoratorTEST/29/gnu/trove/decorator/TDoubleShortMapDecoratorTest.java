import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     Assert.assertSame(map, decorator.getMap());
 }

 @Test
 public void testPutNullKey() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(decorator.put(null, (short) 0));
 }

 @Test
 public void testPutNullValue() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNull(decorator.put((double) 1.0, null));
 }

 @Test
 public void testPut() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     Assert.assertEquals((short) 0, decorator.put((double) 1.0, (short) 1));
     Assert.assertEquals((short) 1, decorator.get((double) 1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.get(null);
 }

 @Test
 public void testGetWrappedKey() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertEquals((Double) 1.0, decorator.wrapKey(1.0));
 }

 @Test
 public void testGetWrappedValue() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertEquals((Short) (short) 1, decorator.wrapValue((short) 1));
 }

 @Test
 public void testClear() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.put((double) 1.0, (short) 1);
     decorator.clear();
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemoveNullKey() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.put((double) 1.0, (short) 1);
     Assert.assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveWrappedKey() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.put((double) 1.0, (short) 1);
     Assert.assertEquals((short) 1, decorator.remove((Double) 1.0));
 }

 @Test
 public void testEntrySetSize() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);
     Assert.assertEquals(2, decorator.entrySet().size());
 }

 @Test
 public void testEntrySetContains() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);
     Entry<Double, Short> entry = new HashMap.SimpleEntry<>((double) 1.0, (short) 1);
     Assert.assertTrue(decorator.entrySet().contains(entry));
 }

 @Test
 public void testContainsValue() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.put((double) 1.0, (short) 1);
     Assert.assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.put((double) 1.0, (short) 1);
     Assert.assertTrue(decorator.containsKey((Double) 1.0));
 }

 @Test
 public void testSize() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);
     Assert.assertTrue(decorator.isEmpty());
     decorator.put((double) 1.0, (short) 1);
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     Map<Double, Short> mapToPut = new HashMap<>();
     mapToPut.put((double) 1.0, (short) 1);
     mapToPut.put((double) 2.0, (short) 2);
     decorator = new TDoubleShortMapDecorator();
     decorator.putAll(mapToPut);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(byteStream);
     TDoubleShortMap mapToSave = new TDoubleShortMap();
     mapToSave.put((double) 1.0, (short) 1);
     TDoubleShortMapDecorator decoratorToSave = new TDoubleShortMapDecorator(mapToSave);
     out.writeObject(decoratorToSave);
     out.close();
     byte[] bytes = byteStream.toByteArray();
     ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
     ObjectInput in = new ObjectInputStream(byteInput);
     TDoubleShortMapDecorator decoratorLoaded = (TDoubleShortMapDecorator)in.readObject();
     in.close();
     Assert.assertEquals(decoratorToSave.size(), decoratorLoaded.size());
 }

 @Test
 public void testWriteExternal() throws IOException {
     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(byteStream);
     TDoubleShortMapDecorator decoratorToSave = new TDoubleShortMapDecorator();
     decoratorToSave.put((double) 1.0, (short) 1);
     decoratorToSave.put((double) 2.0, (short) 2);
     out.writeObject(decoratorToSave);
     out.close();
     byte[] bytes = byteStream.toByteArray();
     ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
     ObjectInput in = new ObjectInputStream(byteInput);
     TDoubleShortMapDecorator decoratorLoaded = (TDoubleShortMapDecorator)in.readObject();
     in.close();
     Assert.assertEquals(decoratorToSave.size(), decoratorLoaded.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithDuplicateKey() {
     decorator = new TDoubleShortMapDecorator();
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 1.0, (short) 2);
 }
}