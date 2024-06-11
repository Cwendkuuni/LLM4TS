import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testIsSynthetic() {
    RecordType recordType = new RecordType(registry, properties, true);
    assertTrue(recordType.isSynthetic());

    recordType = new RecordType(registry, properties, false);
    assertFalse(recordType.isSynthetic());
  }
}