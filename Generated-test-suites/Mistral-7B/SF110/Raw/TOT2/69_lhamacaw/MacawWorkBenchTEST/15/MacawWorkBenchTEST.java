// MacawWorkBenchTest.java - Basic tests

import org.junit.Test;
import static org.junit.Assert.*;
import macaw.presentationLayer.MacawWorkBench;

public class MacawWorkBenchTest {

  @Test
  public void testMacawWorkBenchConstructor() {
    new MacawWorkBench(new SessionProperties());
  }

  // Add test cases for other methods here
}