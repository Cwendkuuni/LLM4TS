import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.util.MultidimensionalCounter;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorValidDimensions() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(dimensions);
        Assert.assertEquals(3, counter.getDimension());
        
    }

    // Test methods for getCounts(), getCount(), etc. will follow here
}