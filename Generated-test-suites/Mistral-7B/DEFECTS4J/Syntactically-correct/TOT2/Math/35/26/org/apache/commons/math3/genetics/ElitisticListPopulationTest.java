import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.chromosomes.AbstractChromosome;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithListAndPopulationLimit() {
        // Given
        List<AbstractChromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.9;

        // When
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Then
        assertEquals(populationLimit, population.size());
    }
}