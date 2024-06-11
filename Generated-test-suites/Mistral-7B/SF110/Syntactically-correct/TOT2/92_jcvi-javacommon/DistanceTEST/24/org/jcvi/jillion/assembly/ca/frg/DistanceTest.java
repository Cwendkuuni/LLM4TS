import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {
    @Test
    void testGetMean() {
        Distance distance = new Distance(1, 2, 1.5f, 0.5f);
        assertEquals(1.5f, distance.getMean());
    }
}