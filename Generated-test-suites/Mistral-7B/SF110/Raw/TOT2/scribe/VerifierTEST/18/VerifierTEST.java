import org.junit.Test;
import org.junit.Assert.*;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {
   @Test
   public void testVerifierConstruction() {
      String validValue = "validValue";
      Verifier verifier = new Verifier(validValue);
      String actualValue = verifier.getValue();
      String expectedValue = validValue;
      assertEquals(expectedValue, actualValue);
   }
}