import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties.put("b", new RecordProperty("b", registry.getType(String.class)));

    RecordType recordType = new RecordType(registry, recordProperties);
    JSType recordTypeInstance = recordType.getInstanceType();

    assertEquals("{a: int, b: String}", recordType.getCanonicalSignature());
    assertEquals(recordTypeInstance.getClass(), recordTypeInstance.getClass());
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties1.put("b", new RecordProperty("b", registry.getType(String.class)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties2.put("b", new RecordProperty("b", registry.getType(String.class)));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties1.put("b", new RecordProperty("b", registry.getType(String.class)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("b", new RecordProperty("b", registry.getType(String.class)));
    recordProperties2.put("a", new RecordProperty("a", registry.getType(int.class)));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties.put("b", new RecordProperty("b", registry.getType(String.class)));

    RecordType recordType = new RecordType(registry, recordProperties);
    recordType.freeze();

    try {
      recordType.defineProperty("c", registry.getType(double.class), false, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeGetLeastSupertype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties1.put("b", new RecordProperty("b", registry.getType(String.class)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("b", new RecordProperty("b", registry.getType(String.class)));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
    assertEquals("{b: String}", leastSupertype.getCanonicalSignature());
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties1.put("b", new RecordProperty("b", registry.getType(String.class)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("b", new RecordProperty("b", registry.getType(String.class)));
    recordProperties2.put("c", new RecordProperty("c", registry.getType(int.class)));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals("{a: int, b: String}", greatestSubtype.getCanonicalSignature());
  }

  @Test
  public void testRecordTypeIsEquivalentTo() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties1.put("b", new RecordProperty("b", registry.getType(String.class)));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", registry.getType(int.class)));
    recordProperties2.put("c", new RecordProperty("c", registry.getType(String.class)));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }
}