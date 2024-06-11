import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

  @Test
  public void testDefaultConstructor() {
    DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
    assertEquals(Date.class, adapter.dateType);
  }

}