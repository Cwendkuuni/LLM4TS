import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class TShortByteMapDecoratorTest {
  @Test
  public void testTShortByteMapDecoratorEmptyConstructor() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    assertNotNull(map);
  }
}