import org.junit.Assert;
import org.junit.Test;

public class ObjectResultTest {

@Test
public void testObjectResultConstructor() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
assertNull(objectResult.getStatus());
}

@Test
public void testOfMethod() {
ObjectStatus objectStatus = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), null);
ObjectResult objectResult = ObjectResult.of(objectStatus);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("", objectResult.getType());
assertEquals(10, objectResult.getCount().get());
assertEquals(20, objectResult.getTotal().get());
assertNull(objectResult.getStatus());
}

@Test
public void testGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema.name[type]", objectResult.getSummary());
}

}