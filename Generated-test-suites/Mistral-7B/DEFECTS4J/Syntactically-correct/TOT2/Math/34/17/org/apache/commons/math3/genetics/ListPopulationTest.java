import org.junit.Test;
import org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        // Add more assertions here to check the functionality of the constructor
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithInvalidPopulationLimit() {
        try {
            ListPopulation population = new ListPopulation(-1);
        } catch (NotPositiveException e) {
            // Expected exception, do nothing
        }
        // Add more assertions here to check the exception handling
    }
}