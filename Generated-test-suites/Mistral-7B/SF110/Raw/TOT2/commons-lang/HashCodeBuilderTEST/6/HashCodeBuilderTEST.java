import org.junit.Test;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HashCodeBuilderTest {
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int expected = 37; // iConstant value of HashCodeBuilder
        int actual = hcb.toHashCode();
        assert(expected == actual);
    }
}