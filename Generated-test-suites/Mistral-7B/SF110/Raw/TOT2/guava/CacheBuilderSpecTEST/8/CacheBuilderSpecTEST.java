import static org.junit.Assert.*;
import com.google.common.cache.CacheBuilderSpec;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=3600, writeExpirationTimeUnit=SECONDS, accessExpirationDuration=1800, accessExpirationTimeUnit=MINUTES, refreshDuration=10800, refreshTimeUnit=HOURS";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(10, spec.initialCapacity);
        assertEquals(20, spec.maximumSize);
        assertEquals(30, spec.maximumWeight);
        assertEquals(5, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(3600, spec.writeExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
        assertEquals(1800, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
        assertEquals(10800, spec.refreshDuration);
        assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpec() {
        String cacheBuilderSpecification = "invalidKey=value";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("Expected an IllegalArgumentException to be thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptySpec() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("Expected an IllegalArgumentException to be thrown");
    }
}