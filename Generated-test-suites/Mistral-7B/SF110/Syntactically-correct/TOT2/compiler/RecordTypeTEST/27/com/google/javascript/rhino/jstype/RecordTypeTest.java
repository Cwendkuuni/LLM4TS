import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testIsSynthetic() {
    RecordType recordType = new RecordType(null, null, true);
    assertFalse(recordType.isSynthetic());

    RecordType syntheticRecordType = new RecordType(null, null, false);
    assertTrue(syntheticRecordType.isSynthetic());
  }
}