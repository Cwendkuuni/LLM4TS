package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link ListPopulation}.
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        // Create a new ListPopulation instance with a population limit of 5
        ListPopulation pop = new ListPopulation(5);

        // Check that the population size is initially 0
        assertEquals(0, pop.getPopulationSize());

        // Add 5 chromosomes to the population
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new FakeChromosome(i);
            pop.addChromosome(chromosome);
            chromosomes.add(chromosome);
        }

        // Check that the population size is now 5
        assertEquals(5, pop.getPopulationSize());

        // Check that the list of chromosomes is equal to the one added
        List<Chromosome> population = pop.getChromosomes();
        assertEquals(chromosomes, population);
    }

    /**
     * Test constructor with invalid arguments: population limit is not positive.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test constructor with invalid arguments: population limit is 0.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionWithZeroPopulationLimit() {
        new ListPopulation(0);
    }

    /**
     * Test method setChromosomes with valid arguments.
     */
    @Test
    public void testSetChromosomesWithValidArguments() {
        // Create a new ListPopulation instance with a population limit of 5
        ListPopulation pop = new ListPopulation(5);

        // Add 6 chromosomes to the population
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Chromosome chromosome = new FakeChromosome(i);
            pop.addChromosome(chromosome);
            chromosomes.add(chromosome);
        }

        // Test setting the chromosomes list with a smaller list
        List<Chromosome> smallerList = new ArrayList<>();
        smallerList.addAll(chromosomes.subList(0, 5));
        pop.setChromosomes(smallerList);

        // Check that the population size is now 5
        assertEquals(5, pop.getPopulationSize());

        // Check that the list of chromosomes is equal to the smaller one set
        List<Chromosome> population = pop.getChromosomes();
        assertEquals(smallerList, population);

        // Test setting the chromosomes list with a larger list
        List<Chromosome> largerList = new ArrayList<>();
        largerList.addAll(chromosomes);
        pop.setChromosomes(largerList);

        // Check that the population size remains 5
        assertEquals(5, pop.getPopulationSize());

        // Check that the list of chromosomes is equal to the larger one set
        population = pop.getChromosomes();
        assertEquals(largerList, population);
    }

    /**
     * Test method setChromosomes with invalid arguments: null list.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        ListPopulation pop = new ListPopulation(5);
        pop.setChromosomes(null);
    }

    /**
     * Test method setChromosomes with invalid arguments: list size is greater than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithTooManyChromosomes() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Chromosome chromosome = new FakeChromosome(i);
            pop.addChromosome(chromosome);
            chromosomes.add(chromosome);
        }
        pop.setChromosomes(chromosomes);
    }

    /**
     * Test method addChromosomes with valid arguments.
     */
    @Test
    public void testAddChromosomesWithValidArguments() {
        // Create a new ListPopulation instance with a population limit of 5
        ListPopulation pop = new ListPopulation(5);

        // Add 5 chromosomes to the population
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new FakeChromosome(i);
            pop.addChromosome(chromosome);
            chromosomes.add(chromosome);
        }

        // Add 2 more chromosomes to the population
        List<Chromosome> additionalChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new FakeChromosome(5);
        Chromosome chromosome2 = new FakeChromosome(6);
        pop.addChromosomes(additionalChromosomes);
        additionalChromosomes.add(chromosome1);
        additionalChromosomes.add(chromosome2);

        // Check that the population size is now 7
        assertEquals(7, pop.getPopulationSize());

        // Check that the list of chromosomes is equal to the one added
        List<Chromosome> population = pop.getChromosomes();
        assertEquals(7, population.size());
        for (int i = 0; i < 7; i++) {
            assertEquals(chromosomes.get(i), population.get(i));
        }
    }

    /**
     * Test method addChromosomes with invalid arguments: null collection.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesWithNullCollection() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosomes(null);
    }

    /**
     * Test method addChromosomes with invalid arguments: collection size is greater than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithTooManyChromosomes() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Chromosome chromosome = new FakeChromosome(i);
            pop.addChromosome(chromosome);
            chromosomes.add(chromosome);
        }
        pop.addChromosomes(chromosomes);
    }

    /**
     * Test method getFittestChromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        // Create a new ListPopulation instance with a population limit of 5 and add 5 chromosomes
        ListPopulation pop = new ListPopulation(5);
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new FakeChromosome(i);
            pop.addChromosome(chromosome);
        }

        // Check that the fittest chromosome is the one with the highest fitness
        Chromosome expected = pop.getChromosomes().get(0);
        Chromosome fittest = pop.getFittestChromosome();
        assertEquals(expected, fittest);
    }

    /**
     * Test method getPopulationLimit.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(5, pop.getPopulationLimit());
    }

    /**
     * Test method setPopulationLimit.
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation pop = new ListPopulation(5);
        pop.setPopulationLimit(10);
        assertEquals(10, pop.getPopulationLimit());
    }

    /**
     * Test method setPopulationLimit with population size greater than new limit.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithPopulationSizeGreaterThanNewLimit() {
        ListPopulation pop = new ListPopulation(5);
        pop.setPopulationLimit(3);
    }

    /**
     * Test method getPopulationSize.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation pop = new ListPopulation(5);
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new FakeChromosome(i);
            pop.addChromosome(chromosome);
        }
        assertEquals(5, pop.getPopulationSize());
    }

    /**
     * Test method toString.
     */
    @Test
    public void testToString() {
        ListPopulation pop = new ListPopulation(5);
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new FakeChromosome(i);
            pop.addChromosome(chromosome);
        }
        assertEquals("[Chromosome: 0, Chromosome: 1, Chromosome: 2, Chromosome: 3, Chromosome: 4]", pop.toString());
    }
}