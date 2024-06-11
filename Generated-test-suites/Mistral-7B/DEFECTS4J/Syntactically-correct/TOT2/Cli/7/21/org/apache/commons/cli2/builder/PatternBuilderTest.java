import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Option;

public class PatternBuilderTest {
    // 
    @Test
    public void testCreate() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        Assert.assertNotNull(option);
        // 
    }
}