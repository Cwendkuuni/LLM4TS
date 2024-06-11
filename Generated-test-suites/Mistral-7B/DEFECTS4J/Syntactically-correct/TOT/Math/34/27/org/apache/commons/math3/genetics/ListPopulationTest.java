package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ListPopulationTest {

    private static final int SMALL_POPULATION_LIMIT = 5;
    private static final int LARGE_POPULATION_LIMIT = 10;
    private static final int NUM_CHROMOSOMES = 3;

    private ListPopulation population;

    @Parameterized.Parameters(name = "{index}: test population limit={0}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{{SMALL_POPULATION_LIMIT}, {LARGE_POPULATION_LIMIT}};
        return Arrays.asList(data);
    }

    @Parameterized.Parameter
    public int populationLimit;

    @Before
    public void setUp() {
        population = new ListPopulation(populationLimit);
    }

    /**
     * Test constructor with valid population limit.
     */
    @Test
    public void testValidConstructor() {
        assertEquals(populationLimit, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertTrue(population.getChromosomes().isEmpty());
    }

    /**
     * Test constructor with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructor() {
        new ListPopulation(-1);
    }

    /**
     * Test setting list of chromosomes with valid chromosomes.
     */
    @Test
    public void testSetChromosomesValid() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(chromosomes.size(), population.getPopulationSize());
    }

    /**
     * Test setting list of chromosomes with invalid chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesInvalid() {
        List<Chromosome> chromosomes = null;
        population.setChromosomes(chromosomes);
    }

    /**
     * Test setting list of chromosomes with too many chromosomes.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesTooMany() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        population.setChromosomes(chromosomes);
    }

    /**
     * Test adding chromosomes to an empty population.
     */
    @Test
    public void testAddChromosomesToEmpty() {
        Chromosome chromosome = new Chromosome(1);
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test adding a chromosome to a populated population.
     */
    @Test
    public void testAddChromosomesToPopulated() {
        Chromosome chromosome = new Chromosome(1);
        population.addChromosome(new Chromosome(0));
        population.addChromosome(new Chromosome(2));
        population.addChromosome(chromosome);
        assertEquals(4, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    /**
     * Test adding too many chromosomes to a population.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesTooMany() {
        Chromosome chromosome = new Chromosome(1);
        population.addChromosome(new Chromosome(0));
        population.addChromosome(new Chromosome(2));
        population.addChromosome(chromosome);
        population.addChromosome(new Chromosome(3));
    }

    /**
     * Test getting the unmodifiable list of chromosomes.
     */
    @Test
    public void testGetChromosomes() {
        population.addChromosome(new Chromosome(1));
        population.addChromosome(new Chromosome(2));
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(2, chromosomes.size());
        assertFalse(chromosomes.isEmpty());
        assertFalse(chromosomes.contains(null));
    }

    /**
     * Test getting the mutable list of chromosomes.
     */
    @Test
    public void testGetChromosomeList() {
        population.addChromosome(new Chromosome(1));
        population.addChromosome(new Chromosome(2));
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(2, chromosomes.size());
        assertFalse(chromosomes.isEmpty());
        assertFalse(chromosomes.contains(null));
    }

    /**
     * Test getting the fittest chromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(5);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1, fittestChromosome);
    }

    /**
     * Test setting the maximal population size.
     */
    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(LARGE_POPULATION_LIMIT);
        assertEquals(LARGE_POPULATION_LIMIT, population.getPopulationLimit());
    }

    /**
     * Test setting the maximal population size to a smaller number.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        population.setPopulationLimit(NUM_CHROMOSOMES - 1);
    }

    /**
     * Test adding chromosomes to a population with the population limit.
     */
    @Test
    public void testAddChromosomesToPopulationLimit() {
        for (int i = 0; i < populationLimit; i++) {
            population.addChromosome(new Chromosome(i));
        }
        assertEquals(populationLimit, population.getPopulationSize());
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosome(new Chromosome(populationLimit + 1)));
    }
}