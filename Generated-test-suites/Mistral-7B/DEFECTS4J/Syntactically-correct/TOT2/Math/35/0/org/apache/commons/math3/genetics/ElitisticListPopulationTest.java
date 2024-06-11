package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.IllegalStateException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithListAndInt() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationWithIntAndDouble() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.size());
    }

    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> oldChromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5));
        ElitisticListPopulation population = new ElitisticListPopulation(oldChromosomes, 10, 0.9);
        Collections.sort(oldChromosomes);
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        // Check if the correct number of chromosomes are copied from the old population
        int copiedChromosomes = 0;
        for (Chromosome chromosome : oldChromosomes) {
            if (nextGeneration.contains(chromosome)) {
                copiedChromosomes++;
            }
        }
        assertEquals(Math.ceil(oldChromosomes.size() * 0.9), copiedChromosomes);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
        population.nextGeneration();
    }

    @Test(expected = OutOfRangeException.class)
    public void testNextGenerationWithNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, -0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeSize() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, -1, 0.9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeElitismRateButValidSize() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, -0.1);
    }
}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double[] getGenes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFitness() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}