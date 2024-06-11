import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CacheBuilderSpecTest {

  @Test
  public void testParse() {
    String cacheBuilderSpecification = "initialCapacity=5, maximumSize=10, concurrencyLevel=2";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

    // Check if CacheBuilderSpec object is initialized correctly
    assertEquals(5, spec.initialCapacity);
    assertEquals(10, spec.maximumSize);
    assertEquals(2, spec.concurrencyLevel);
    assertNull(spec.recordStats);
    assertNull(spec.keyStrength);
    assertNull(spec.valueStrength);
    assertNull(spec.writeExpirationDuration);
    assertNull(spec.writeExpirationTimeUnit);
    assertNull(spec.accessExpirationDuration);
    assertNull(spec.accessExpirationTimeUnit);
    assertNull(spec.refreshDuration);
    assertNull(spec.refreshTimeUnit);
  }
}