import static org.junit.Assert.*;
import com.google.common.cache.*;
import org.junit.Test;
import org.junit.experimental.annotations.ExpectedException;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test empty string
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        // ...
    }

    @Test
    public void testParseInvalidKeyValuePair() {
        // Test invalid key-value pair
    }

    @Test
    public void testParseMissingKeyValuePair() {
        // Test missing required key-value pair
    }

    @Test
    public void testParseMissingTimeUnit() {
        // Test missing time unit for duration
    }

    @Test
    public void testParseUnknownKey() {
        // Test unknown key
    }

    @Test
    public void testToCacheBuilder() {
        // Test toCacheBuilder method
    }

    @Test
    public void testToString() {
        // Test toString method
    }

    @Test
    public void testEquals() {
        // Test equals method
    }

    @Test
    public void testHashCode() {
        // Test hashCode method
    }

    // Add more test cases as needed

    @ExpectedException(IllegalArgumentException.class)
    @Test
    public void testParseInvalidFormat() {
        String specification = "initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=1, writeExpirationTimeUnit=HOURS, accessExpirationDuration=2m, accessExpirationTimeUnit=MINUTES, refreshDuration=3d, unknownKey=value";
        CacheBuilderSpec.parse(specification);
    }
}