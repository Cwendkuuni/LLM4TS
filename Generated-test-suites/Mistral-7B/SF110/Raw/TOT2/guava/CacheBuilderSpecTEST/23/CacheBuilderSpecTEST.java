import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Test;

public class CacheBuilderSpecTest {

  @Test
  public void testParse() {
    final String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=10";
    final CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

    assertEquals("initialCapacity=10, maximumSize=50, concurrencyLevel=10", spec.toParsableString());
    assertNotNull(spec.initialCapacity);
    assertEquals(10, spec.initialCapacity.intValue());
    assertNotNull(spec.maximumSize);
    assertEquals(50, spec.maximumSize.longValue());
    assertNotNull(spec.concurrencyLevel);
    assertEquals(10, spec.concurrencyLevel.intValue());
  }
}