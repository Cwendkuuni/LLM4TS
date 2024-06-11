import static org.junit.Assert.*;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {

  @Test
  public void testGetID() {
    AbstractInstance instance = new AbstractInstance(null);
    int expectedID = 0;
    int actualID = instance.getID();
    assertEquals(expectedID, actualID);

    AbstractInstance instanceWithCustomID = new AbstractInstance(new Object()) {
      {
        this.ID = 10;
      }
    };
    int expectedIDWithCustomID = 10;
    int actualIDWithCustomID = instanceWithCustomID.getID();
    assertEquals(expectedIDWithCustomID, actualIDWithCustomID);
  }
}