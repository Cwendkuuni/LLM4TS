import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DistanceTest {

    @Test
    public void testBuildDistance() {
        int min = 5;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = 7.5f;
        float expectedStdDev = (float) (max - min) / 6.0f;
        assertEquals(expectedMean, distance.getMean(), 0.001f);
        assertEquals(expectedStdDev, distance.getStdDev(), 0.001f);
    }
}