import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    @Test
    public void testParse() {
        final String cacheBuilderSpecification = "initialCapacity=5, maximumSize=10, concurrencyLevel=2";
        final CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertEquals(5, spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertEquals(2, spec.concurrencyLevel);
    }
}