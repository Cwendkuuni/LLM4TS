import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TShortHashTest {

  @Test
  public void testTShortHashEmptyConstruction() {
    TShortHash hash = new TShortHash();
    short noEntryValue = hash.getNoEntryValue();
    assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
  }

  // Test for edge cases and exceptions will be added in the next steps.
}