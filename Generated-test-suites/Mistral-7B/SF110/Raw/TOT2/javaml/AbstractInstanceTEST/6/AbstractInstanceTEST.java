import static org.junit.Assert.*;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {
   @Test
   public void testGetID() {
      AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
      assertEquals(0, instance.getID());
      instance = new DenseInstance(new double[]{4.0, 5.0, 6.0});
      assertEquals(1, instance.getID());
   }
}