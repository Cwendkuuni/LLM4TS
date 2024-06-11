import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;

public class HelpFormatterTest {
  @Test
  public void testHelpFormatterConstructor() {
    HelpFormatter formatter = new HelpFormatter();
    int width = 50;
    formatter.setWidth(width);
    int actualWidth = formatter.getWidth();
    assert (actualWidth == width);
  }
}