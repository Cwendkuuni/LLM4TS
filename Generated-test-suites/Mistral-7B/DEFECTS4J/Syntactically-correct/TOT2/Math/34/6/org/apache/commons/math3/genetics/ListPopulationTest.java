import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }
}