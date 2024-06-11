package org.apache.commons.math3.genetics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreationWithListAndLimit() {
        List<Chromosome> initialChromosomes = List.of(new Chromosome(), new Chromosome(), new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 0.5);
        assertEquals(5, population.size());
        assertNotNull(population.getChromosomes());
    }

    @Test
    public void testElitisticListPopulationCreationWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);
        assertEquals(3, population.size());
        assertNotNull(population.getChromosomes());
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> initialChromosomes = List.of(new Chromosome(), new Chromosome(), new Chromosome(),
                new Chromosome(), new Chromosome(), new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 10, 0.5);
        List<Chromosome> sortedChromosomes = List.copyOf(population.getChromosomes());
        Collections.sort(sortedChromosomes);

        ElitisticListPopulation nextGeneration = population.nextGeneration();
        List<Chromosome> nextGenerationChromosomes = List.copyOf(nextGeneration.getChromosomes());

        int boundIndex = (int) Math.ceil((1.0 - population.getElitismRate()) * initialChromosomes.size());
        for (int i = boundIndex; i < initialChromosomes.size(); i++) {
            Chromosome chromosome = initialChromosomes.get(i);
            Chromosome nextChromosome = nextGenerationChromosomes.get(i - boundIndex);
            assertSame(chromosome, nextChromosome);
        }

        for (int i = boundIndex; i < initialChromosomes.size(); i++) {
            Chromosome chromosome = initialChromosomes.get(i);
            assertNull(population.getChromosome(i));
        }

        for (int i = 0; i < boundIndex; i++) {
            Chromosome chromosome = initialChromosomes.get(i);
            Chromosome nextChromosome = nextGenerationChromosomes.get(i);
            assertNull(nextChromosome);
        }
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 0.5);
        assertThrows(IllegalArgumentException.class, population::nextGeneration);
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, -0.2);
        assertThrows(OutOfRangeException.class, population::nextGeneration);
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 1.2);
        assertThrows(OutOfRangeException.class, population::nextGeneration);
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate());
    }

    @Test
    public void testElitisticListPopulationSetElitismRateWithNegativeValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-0.2));
    }

    @Test
    public void testElitisticListPopulationSetElitismRateWithValueGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(1.2));
    }
}