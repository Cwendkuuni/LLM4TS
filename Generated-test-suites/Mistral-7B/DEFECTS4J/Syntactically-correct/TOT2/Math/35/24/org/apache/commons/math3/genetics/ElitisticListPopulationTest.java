import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {
    @Test
    public void testConstructorWithListAndPopulationLimit() {
        // Given
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.9;

        // When
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Then
        Assert.assertEquals(populationLimit, population.size());
        Assert.assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithNegativeElitismRate() {
        // Given
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = -0.1;

        // When & Then (exception should be thrown)
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }
}