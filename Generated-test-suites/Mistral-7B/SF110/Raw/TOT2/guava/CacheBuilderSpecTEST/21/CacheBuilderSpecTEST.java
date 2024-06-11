import static org.junit.Assert.*;
import static com.google.common.cache.CacheBuilderSpec.*;

public class CacheBuilderSpecTest {
    @Test
    public void testParse_EmptySpec() {
        CacheBuilderSpec spec = parse("");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_InitialCapacity() {
        CacheBuilderSpec spec = parse("initialCapacity=5");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertEquals(5, spec.initialCapacity);
        assertEquals(null, spec.maximumSize);
        assertEquals(null, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidIntegerValue() {
        parse("initialCapacity=abc");
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=15,concurrencyLevel=3," +
                "keyStrength=softValues,valueStrength=weakValues,recordStats=true,writeExpirationDuration=3600," +
                "writeExpirationTimeUnit=SECONDS,accessExpirationDuration=1800,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1000,refreshTimeUnit=MILLISECONDS");
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        // Additional tests for the cacheBuilder object
    }

    // Add other test cases for other methods and edge cases
}