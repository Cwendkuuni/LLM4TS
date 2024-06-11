import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

 @BeforeClass
 public static void setupClass() {
 System.out.println("--- Queue Testing Started ---");
 }

 @AfterClass
 public static void teardownClass() {
 System.out.println("--- Queue Testing Completed ---");
 }

 @Before
 public void setup() {
 Queue queue = new Queue();
 }

 @Test
 public void testEmptyQueue() {
 Queue queue = new Queue();
 assertTrue(queue.isEmpty());
 assertEquals(0, queue.getNumberItems());
 assertNull(queue.dequeue());
 }

 @Test
 public void testBasicQueueOperations() {
 Queue queue = new Queue();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");

 assertEquals(3, queue.getNumberItems());
 assertEquals("A", queue.dequeue().toString());
 assertEquals("B", queue.dequeue().toString());
 assertEquals("C", queue.dequeue().toString());
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testRemoveObjectFromQueue() {
 Queue queue = new Queue();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");

 assertEquals(3, queue.getNumberItems());
 queue.remove("B");
 queue.remove("C");
 assertEquals(1, queue.getNumberItems());
 assertEquals("A", queue.dequeue().toString());
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testRefreshElement() {
 Queue queue = new Queue();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.refreshElement("B");

 assertEquals(2, queue.getNumberItems());
 assertEquals("B", queue.dequeue().toString());
 assertEquals("A", queue.dequeue().toString());
 assertTrue(queue.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullObjectInQueueOperations() {
 Queue queue = new Queue();
 queue.enqueue(null);
 queue.dequeue();
 queue.remove(null);
 queue.refreshElement(null);
 }

 @Test
 public void testMaxCapacityQueue() {
 Queue queue = new Queue(5);
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");
 queue.enqueue("D");
 queue.enqueue("E");
 queue.enqueue("F");
 queue.enqueue("G"); // This should not be allowed as we have reached the max capacity of 5

 assertEquals(5, queue.getNumberItems());
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testGetPeakNumberItems() {
 Queue queue = new Queue();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");

 assertEquals(3, queue.getNumberItems());
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 Queue queue = new Queue();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");

 Vector vector = queue.getObjects();
 assertEquals(3, vector.size());
 assertTrue(vector.contains("A"));
 assertTrue(vector.contains("B"));
 assertTrue(vector.contains("C"));
 }
}