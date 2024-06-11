import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {
   // Test for normal usage
   @Test
   public void testgetValueNormal() {
      BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(5);
      int actual = testObject.getValue();
      int expected = 5;
      assertEquals(expected, actual);
   }

   // Test for negative value
   @Test(expected = IndexOutOfBoundsException.class)
   public void testgetValueNegative() {
      BaseSingleFieldPeriod testObject = new MySingleFieldPeriod(5);
      testObject.setValue(-1);
      int actual = testObject.getValue();
   }
}