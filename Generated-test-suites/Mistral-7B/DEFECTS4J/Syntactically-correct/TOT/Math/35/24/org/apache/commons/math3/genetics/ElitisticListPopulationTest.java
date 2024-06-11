package org.apache.commons.math3.genetics;

import static org.junit.jupiter.api.Assertions.*;
import static org.apache.commons.math3.util.Arrays.isSame;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.FitnessFunction;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.PopulationFactory;
import org.apache.commons.math3.genetics.SimpleChromosome;
import org.apache.commons.math3.random.RandomGenerator;

class ElitisticListPopulationTest {

    @org.junit.jupiter.api.Test
    void testElitisticPopulationCreation() {
        // Preconditions
        List<Chromosome> initialPopulation = List.of(new SimpleChromosome(10), new SimpleChromosome(11));
        int populationLimit = 5;
        double elitismRate = 0.5;

        // Test
        ElitisticListPopulation population = new ElitisticListPopulation(initialPopulation, populationLimit, elitismRate);

        // Verify
        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);

        // Cleanup
        population = null;
    }

    @org.junit.jupiter.api.Test
    void testElitisticPopulationNextGeneration() throws OutOfRangeException {
        // Preconditions
        List<Chromosome> initialPopulation = List.of(
                new SimpleChromosome(10, 5.0),
                new SimpleChromosome(11, 3.5),
                new SimpleChromosome(12, 2.0),
                new SimpleChromosome(13, 7.5),
                new SimpleChromosome(14, 8.0),
                new SimpleChromosome(15, 1.0),
                new SimpleChromosome(16, 4.0)
        );
        int populationLimit = 5;
        double elitismRate = 0.5;

        // Test
        ElitisticListPopulation population = new ElitisticListPopulation(initialPopulation, populationLimit, elitismRate);
        Population nextGeneration = population.nextGeneration();

        // Verify
        assertEquals(populationLimit, nextGeneration.size());
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0.001);

        List<Chromosome> oldPopulation = population.getChromosomes();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();

        // Check that the best chromosomes are copied to the next generation
        for (int i = 0; i < (int) Math.ceil(populationLimit * elitismRate); i++) {
            Chromosome oldChromosome = oldPopulation.get(i);
            Chromosome nextGenerationChromosome = nextGenerationChromosomes.get(i);
            assertTrue(isSame(oldChromosome, nextGenerationChromosome));
        }

        // Check that the worst chromosomes are replaced
        for (int i = oldPopulation.size() - 1; i >= oldPopulation.size() - populationLimit; i--) {
            Chromosome oldChromosome = oldPopulation.get(i);
            Chromosome nextGenerationChromosome = nextGenerationChromosomes.get(i);
            assertNotSame(oldChromosome, nextGenerationChromosome);
        }

        // Cleanup
        population = null;
        nextGeneration = null;
    }

    @org.junit.jupiter.api.Test
    void testElitisticPopulationNextGenerationWithFitnessFunction() throws OutOfRangeException {
        // Preconditions
        List<Chromosome> initialPopulation = List.of(
                new SimpleChromosome(10, 5.0),
                new SimpleChromosome(11, 3.5),
                new SimpleChromosome(12, 2.0),
                new SimpleChromosome(13, 7.5),
                new SimpleChromosome(14, 8.0),
                new SimpleChromosome(15, 1.0),
                new SimpleChromosome(16, 4.0)
        );
        int populationLimit = 5;
        double elitismRate = 0.5;

        FitnessFunction fitnessFunction = chromosome -> chromosome.getFitness();

        // Test
        ElitisticListPopulation population = new ElitisticListPopulation(initialPopulation, populationLimit, elitismRate, fitnessFunction);
        Population nextGeneration = population.nextGeneration();

        // Verify
        assertEquals(populationLimit, nextGeneration.size());
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0.001);

        List<Chromosome> oldPopulation = population.getChromosomes();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();

        // Check that the best chromosomes are copied to the next generation
        for (int i = 0; i < (int) Math.ceil(populationLimit * elitismRate); i++) {
            Chromosome oldChromosome = oldPopulation.get(i);
            Chromosome nextGenerationChromosome = nextGenerationChromosomes.get(i);
            assertTrue(isSame(oldChromosome, nextGenerationChromosome));
            assertEquals(oldChromosome.getFitness(), nextGenerationChromosome.getFitness());
        }

        // Check that the worst chromosomes are replaced
        for (int i = oldPopulation.size() - 1; i >= oldPopulation.size() - populationLimit; i--) {
            Chromosome oldChromosome = oldPopulation.get(i);
            Chromosome nextGenerationChromosome = nextGenerationChromosomes.get(i);
            assertNotSame(oldChromosome, nextGenerationChromosome);
            assertNotEquals(oldChromosome.getFitness(), nextGenerationChromosome.getFitness());
        }

        // Cleanup
        population = null;
        nextGeneration = null;
    }

    @org.junit.jupiter.api.Test
    void testElitisticPopulationNextGenerationWithRandomGenerator() throws OutOfRangeException {
        // Preconditions
        List<Chromosome> initialPopulation = List.of(
                new SimpleChromosome(10, 5.0),
                new SimpleChromosome(11, 3.5),
                new SimpleChromosome(12, 2.0),
                new SimpleChromosome(13, 7.5),
                new SimpleChromosome(14, 8.0),
                new SimpleChromosome(15, 1.0),
                new SimpleChromosome(16, 4.0)
        );
        int populationLimit = 5;
        double elitismRate = 0.5;
        RandomGenerator random = new RandomGenerator();

        // Test
        ElitisticListPopulation population = new ElitisticListPopulation(initialPopulation, populationLimit, elitismRate, null, random);
        Population nextGeneration = population.nextGeneration();

        // Verify
        assertEquals(populationLimit, nextGeneration.size());
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0.001);

        List<Chromosome> oldPopulation = population.getChromosomes();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();

        // Check that the best chromosomes are copied to the next generation
        for (int i = 0; i < (int) Math.ceil(populationLimit * elitismRate); i++) {
            Chromosome oldChromosome = oldPopulation.get(i);
            Chromosome nextGenerationChromosome = nextGenerationChromosomes.get(i);
            assertTrue(isSame(oldChromosome, nextGenerationChromosome));
            assertEquals(oldChromosome.getFitness(), nextGenerationChromosome.getFitness());
        }

        // Check that the worst chromosomes are replaced
        for (int i = oldPopulation.size() - 1; i >= oldPopulation.size() - populationLimit; i--) {
            Chromosome oldChromosome = oldPopulation.get(i);
            Chromosome nextGenerationChromosome = nextGenerationChromosomes.get(i);
            assertNotSame(oldChromosome, nextGenerationChromosome);
            assertNotEquals(oldChromosome.getFitness(), nextGenerationChromosome.getFitness());
        }

        // Check that the new chromosomes have different fitness values
        for (int i = oldPopulation.size(); i < nextGeneration.size(); i++) {
            Chromosome newChromosome = nextGenerationChromosomes.get(i);
            List<Double> newGenes = newChromosome.getGenes();
            double newFitness = newChromosome.getFitness();

            boolean fitnessFound = false;
            for (Chromosome oldChromosome : oldPopulation) {
                if (isSame(oldChromosome.getGenes(), newGenes)) {
                    fail("New chromosome fitness must be different from any old chromosome fitness.");
                    fitnessFound = true;
                    break;
                }
                double oldFitness = oldChromosome.getFitness();
                if (Math.abs(newFitness - oldFitness) < 0.001) {
                    fail("New chromosome fitness must be different from any old chromosome fitness.");
                    fitnessFound = true;
                    break;
                }
            }
            if (!fitnessFound) {
                fail("Test failed: no matching fitness found for the new chromosome.");
            }
        }

        // Cleanup
        population = null;
        nextGeneration = null;
    }
}