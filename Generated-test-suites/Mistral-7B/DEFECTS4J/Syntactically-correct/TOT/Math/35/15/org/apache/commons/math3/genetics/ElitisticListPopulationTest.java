package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(new byte[10]));
        chromosomes.add(new Chromosome(new byte[10]));
        chromosomes.add(new Chromosome(new byte[10]));
        chromosomes.add(new Chromosome(new byte[10]));
        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testNextGenerationWithInitialPopulation() {
        Population nextGeneration = population.nextGeneration();
        assertEquals("Size of next generation should be equal to population limit", 10, nextGeneration.size());
        // check if the first generation chromosomes are present in the next generation
        for (Chromosome chromosome : population.getChromosomes()) {
            boolean found = false;
            for (Chromosome nextGenChromosome : nextGeneration.getChromosomes()) {
                if (nextGenChromosome.equals(chromosome)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Chromosome not found in the next generation", found);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 1.1);
    }

    @Test
    public void testEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        population.nextGeneration();
        assertNull("Next generation should be empty", population.getChromosomes().get(0));
    }

    @Test
    public void testPopulationWithLessThanMaxLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 3, 0.5);
        population.addChromosome(new Chromosome(new byte[10]));
        population.addChromosome(new Chromosome(new byte[10]));
        population.nextGeneration();
        List<Chromosome> nextGeneration = population.nextGeneration().getChromosomes();
        assertEquals("Size of next generation should be equal to population limit", 3, nextGeneration.size());
        // check if the first generation chromosomes are present in the next generation
        for (Chromosome chromosome : population.getChromosomes()) {
            boolean found = false;
            for (Chromosome nextGenChromosome : nextGeneration) {
                if (nextGenChromosome.equals(chromosome)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Chromosome not found in the next generation", found);
        }
    }

    @Test
    public void testPopulationWithMaxLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        for (int i = 0; i < 10; i++) {
            population.addChromosome(new Chromosome(new byte[10]));
        }
        Population nextGeneration = population.nextGeneration();
        assertEquals("Size of next generation should be equal to population limit", 10, nextGeneration.size());
        // check if all chromosomes are present in the next generation
        for (Chromosome chromosome : population.getChromosomes()) {
            boolean found = false;
            for (Chromosome nextGenChromosome : nextGeneration.getChromosomes()) {
                if (nextGenChromosome.equals(chromosome)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Chromosome not found in the next generation", found);
        }
    }

    @Test
    public void testPopulationWithElitismRateBelowOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 15, 0.7);
        population.addChromosome(new Chromosome(new byte[10]));
        population.addChromosome(new Chromosome(new byte[10]));
        population.addChromosome(new Chromosome(new byte[10]));
        population.addChromosome(new Chromosome(new byte[10]));
        population.addChromosome(new Chromosome(new byte[10]));
        population.nextGeneration();
        List<Chromosome> nextGeneration = population.nextGeneration().getChromosomes();
        assertEquals("Size of next generation should be equal to population limit", 10, nextGeneration.size());
        // check if the elitism rate number of chromosomes from the first generation are present in the next generation
        int elitismCount = (int) Math.ceil(population.size() * population.getElitismRate());
        for (int i = 0; i < elitismCount; i++) {
            Chromosome chromosomeToFind = population.getChromosomes().get(i);
            boolean found = false;
            for (Chromosome nextGenChromosome : nextGeneration) {
                if (nextGenChromosome.equals(chromosomeToFind)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Chromosome not found in the next generation", found);
        }
        // check if the remaining chromosomes from the first generation are not present in the next generation
        for (int i = elitismCount; i < population.size(); i++) {
            Chromosome chromosomeToFind = population.getChromosomes().get(i);
            assertNull("Chromosome not found in the next generation", population.nextGeneration().getChromosomes().get(i));
        }
    }
}