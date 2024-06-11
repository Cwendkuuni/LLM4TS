import static org.junit.Assert.assertEquals;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {
   @Test
   public void testTimeOfDayConstructionWithValidArguments() {
      TimeOfDay time = new TimeOfDay(13, 30);
      assertEquals("13:30", time.toString());
   }
}