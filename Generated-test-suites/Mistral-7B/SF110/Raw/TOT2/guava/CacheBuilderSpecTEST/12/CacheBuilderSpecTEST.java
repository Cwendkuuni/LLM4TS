import static org.junit.Assert.assertEquals;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100, concurrencyLevel=2, weakKeys, expireAfterWrite=5m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(10, spec.initialCapacity);
        assertEquals(100, spec.maximumSize);
        assertEquals(2, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(true, spec.recordStats);
        assertEquals(5, spec.writeExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }
}