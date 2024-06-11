import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @After
 public void tearDown() {
 _map.clear();
 decorator = null;
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 assertNotNull(decorator);
 assertNotNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortMap());
 assertNotNull(decorator);
 assertNotNull(decorator.getMap());
 }

 @Test
 public void testPut() {
 Double key = 1.0;
 Short value = (short) 1;
 decorator.put(key, value);
 assertNotNull(decorator.get(key));
 assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short) 1);
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGet() {
 Double key = 1.0;
 Short value = (short) 1;
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));
 }

 @Test
 public void testGetNullKey() {
 assertEquals(null, decorator.get(null));
 }

 @Test(expected = ClassCastException.class)
 public void testGetWrongTypeKey() {
 decorator.put(1, (short) 1);
 assertEquals(null, decorator.get(new Object()));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short) 1);
 Short removedValue = decorator.remove(1.0);
 assertEquals(Short.valueOf((short) 1), removedValue);
 assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullValue() {
 decorator.put(1.0, (short) 1);
 decorator.remove(1.0, null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testEntrySet() {
 Iterator<Entry<Double, Short>> iterator = decorator.entrySet().iterator();
 assertFalse(iterator.hasNext());
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 iterator = decorator.entrySet().iterator();
 assertTrue(iterator.hasNext());
 Entry<Double, Short> entry = iterator.next();
 assertEquals((Double)1.0, entry.getKey());
 assertEquals((Short)Short.valueOf((short) 1), entry.getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
 decorator.entrySet().add(new Entry<Double, Short>(null, (short) 1));
 fail("Should throw UnsupportedOperationException");
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(true, decorator.isEmpty());
 decorator.put(1.0, (short) 1);
 assertEquals(false, decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 TDoubleShortMapDecorator decoratorToWrite = new TDoubleShortMapDecorator(new TDoubleShortMap());
 decoratorToWrite.put(1.0, (short) 1);
 oos.writeObject(decoratorToWrite);
 oos.writeByte(0);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 TDoubleShortMapDecorator readDecorator = (TDoubleShortMapDecorator)ois.readObject();
 ois.close();
 assertEquals(decoratorToWrite.get(1.0), readDecorator.get(1.0));
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 TDoubleShortMapDecorator decoratorToWrite = new TDoubleShortMapDecorator(new TDoubleShortMap());
 decoratorToWrite.put(1.0, (short) 1);
 oos.writeObject(decoratorToWrite);
 oos.writeByte(0);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(bais);
 bais.close();
 assertEquals(decoratorToWrite.get(1.0), readDecorator.get(1.0));
 }
}