import static org.junit.Assert.assertNotNull;

public class RecordTypeBuilderTest {
  @Test
  public void testRecordTypeBuilderConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    assertNotNull(recordTypeBuilder);
  }
}