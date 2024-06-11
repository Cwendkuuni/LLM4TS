import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ObjectResultTest {

@Test
public void testObjectResultConstructor() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("type", objectResult.getType());
assertEquals(0, objectResult.getCount().get());
assertEquals(0, objectResult.getTotal().get());
}

@Test
public void testObjectResultOf() {
ObjectStatus that = new ObjectStatus("schema", "name", new AtomicLong(10), new AtomicLong(20), null);
ObjectResult objectResult = ObjectResult.of(that);
assertEquals("schema", objectResult.getSchema());
assertEquals("name", objectResult.getName());
assertEquals("", objectResult.getType());
assertEquals(10, objectResult.getCount().get());
assertEquals(20, objectResult.getTotal().get());
}

@Test
public void testObjectResultGetSummary() {
ObjectResult objectResult = new ObjectResult("schema", "name", "type");
objectResult.setCount(new AtomicLong(10));
objectResult.setTotal(new AtomicLong(20));
assertEquals("schema.name[type]: 10/20", objectResult.getSummary());
}

@Test(expected = NullPointerException.class)
public void testObjectResultNullInputs() {
new ObjectResult(null, "name", "type");
}

}