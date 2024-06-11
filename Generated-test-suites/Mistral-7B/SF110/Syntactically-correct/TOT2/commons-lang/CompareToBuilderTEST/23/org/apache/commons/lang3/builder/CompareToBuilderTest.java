import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Assert;
import org.junit.Test;

public class CompareToBuilderTest {
    @Test
    public void testDefaultConstructorAndToComparison() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        int comparison = compareToBuilder.toComparison();
        Assert.assertEquals(0, comparison);
    }
}