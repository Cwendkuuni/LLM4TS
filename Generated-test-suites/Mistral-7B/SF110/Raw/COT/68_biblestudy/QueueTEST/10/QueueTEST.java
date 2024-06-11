import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithCapacity = new Queue(5);
 }

 @Test
 public void testConstructor() {
 Queue q = new Queue();
 assertNotNull(q);
 }

 @Test
 public void testConstructorWithCapacity() {
 Queue q = new Queue(5);
 assertNotNull(q);
 assertEquals(5, q.getMaxCapacity());
 }

 @Test
 public void testIsEmpty() {
 Queue q = new Queue();
 assertTrue(q.isEmpty());
 }

 @Test
 public void testIsEmptyAfterEnqueue() {
 Queue q = new Queue();
 q.enqueue("test");
 assertFalse(q.isEmpty());
 }

 @Test
 public void testEnqueueNullObject() {
 Queue q = new Queue();
 assertFalse(q.isEmpty());
 try {
 q.enqueue(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected, continue with test
 }
 assertTrue(q.isEmpty());
 }

 @Test
 public void testEnqueueObject() {
 Queue q = new Queue();
 q.enqueue("test");
 assertEquals("test", q.dequeue().toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueWhenEmpty() {
 Queue q = new Queue();
 q.dequeue();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testDequeue() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.enqueue("test2");
 assertEquals("test1", q.dequeue().toString());
 assertEquals("test2", q.dequeue().toString());
 }

 @Test
 public void testRemoveObject() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.enqueue("test2");
 q.remove("test1");
 assertFalse(q.contains("test1"));
 }

 @Test
 public void testRemoveObjectDoesNotExist() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.remove("test2");
 assertFalse(q.isEmpty());
 assertEqual("test1", q.dequeue().toString());
 }

 @Test
 public void testRefreshElement() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.refreshElement("test1");
 assertTrue(q.contains("test1"));
 }

 @Test
 public void testGetNumberItems() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.enqueue("test2");
 assertEquals(2, q.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.enqueue("test2");
 q.enqueue("test3");
 assertEquals(3, q.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.enqueue("test2");
 Vector v = q.getObjects();
 assertEquals(2, v.size());
 assertEquals("test1", v.get(0));
 assertEquals("test2", v.get(1));
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue q = new Queue(5);
 q.enqueue("test1");
 q.enqueue("test2");
 q.enqueue("test3");
 q.enqueue("test4");
 assertTrue(q.maxCapacityExceeded());
 }

 @Test
 public void testMaxCapacityNotExceeded() {
 Queue q = new Queue(5);
 q.enqueue("test1");
 q.enqueue("test2");
 assertFalse(q.maxCapacityExceeded());
 }

 @Test
 public void testMaxCapacityExceededWithNoMaxCapacity() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.enqueue("test2");
 q.enqueue("test3");
 q.enqueue("test4");
 q.enqueue("test5");
 assertFalse(q.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 Queue q = new Queue();
 q.enqueue("test1");
 q.enqueue("test2");
 String toString = q.toString();
 assertTrue(toString.contains("numItems=2"));
 assertTrue(toString.contains("maxNumItems=2"));
 assertTrue(toString.contains("maxCapacity=-1"));
 assertTrue(toString.contains("getObjects()=[test1, test2]"));
 }

 @After
 public void tearDown() {
 queue = null;
 queueWithCapacity = null;
 }
}