package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link ListPopulation} class.
 */
public class ListPopulationTest {

    /**
     * Test initialization with positive population limit.
     */
    @Test
    public void testListPopulationInitPositiveLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test initialization with empty list of chromosomes and positive population limit.
     */
    @Test
    public void testListPopulationInitEmptyListAndPositiveLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test initialization with non-empty list of chromosomes and positive population limit.
     */
    @Test
    public void testListPopulationInitNonEmptyListAndPositiveLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        initialChromosomes.add(chromosome);
        ListPopulation population = new ListPopulation(initialChromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test initialization with null list of chromosomes and positive population limit.
     */
    @Test(expected = NullPointerException.class)
    public void testListPopulationInitNullListAndPositiveLimit() {
        new ListPopulation(null, 5);
    }

    /**
     * Test initialization with positive population limit, which is smaller than the size of provided list.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationInitTooLargeListAndPositiveLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            initialChromosomes.add(new DummyChromosome(i));
        }
        new ListPopulation(initialChromosomes, 5);
    }

    /**
     * Test setting list of chromosomes with empty list.
     */
    @Test
    public void testListPopulationSetEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setChromosomes(Collections.emptyList());
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test setting list of chromosomes with non-empty list.
     */
    @Test
    public void testListPopulationSetNonEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        initialChromosomes.add(chromosome);
        population.setChromosomes(initialChromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test setting list of chromosomes with null list.
     */
    @Test(expected = NullPointerException.class)
    public void testListPopulationSetNullList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setChromosomes(null);
    }

    /**
     * Test setting list of chromosomes with list size larger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetTooLargeList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            initialChromosomes.add(new DummyChromosome(i));
        }
        population.setChromosomes(initialChromosomes);
    }

    /**
     * Test adding chromosomes to population with size smaller than population limit.
     */
    @Test
    public void testListPopulationAddChromosomesToSmallPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome = new DummyChromosome(1);
        population.addChromosome(chromosome);
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test adding chromosomes to population with size equal to population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesToFullPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            initialChromosomes.add(new DummyChromosome(i));
            population.addChromosome(initialChromosomes.get(i));
        }
    }

    /**
     * Test adding chromosomes to population with size larger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesToOverPopulatedPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            initialChromosomes.add(new DummyChromosome(i));
            population.addChromosomes(initialChromosomes.subList(i, 10));
        }
    }

    /**
     * Test adding collection of chromosomes to population with size smaller than population limit.
     */
    @Test
    public void testListPopulationAddChromosomesCollectionToSmallPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome = new DummyChromosome(1);
        initialChromosomes.add(chromosome);
        population.addChromosomes(initialChromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test adding collection of chromosomes to population with size equal to population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesCollectionToFullPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            initialChromosomes.add(new DummyChromosome(i));
            population.addChromosomes(initialChromosomes);
        }
    }

    /**
     * Test adding collection of chromosomes to population with size larger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesCollectionToOverPopulatedPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            initialChromosomes.add(new DummyChromosome(i));
        }
        population.addChromosomes(initialChromosomes);
    }

    /**
     * Test getting fittest chromosome from empty population.
     */
    @Test
    public void testListPopulationGetFittestChromosomeFromEmptyPopulation() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertNull(population.getFittestChromosome());
    }

    /**
     * Test getting fittest chromosome from population with one chromosome.
     */
    @Test
    public void testListPopulationGetFittestChromosomeFromPopulationWithOneChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome = new DummyChromosome(1);
        population.addChromosome(chromosome);
        assertEquals(chromosome, population.getFittestChromosome());
    }

    /**
     * Test getting fittest chromosome from population with multiple chromosomes.
     */
    @Test
    public void testListPopulationGetFittestChromosomeFromPopulationWithMultipleChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome bestChromosome = new DummyChromosome(1);
        population.addChromosome(bestChromosome);
        Chromosome worstChromosome = new DummyChromosome(5);
        population.addChromosome(worstChromosome);
        assertEquals(bestChromosome, population.getFittestChromosome());
    }

    /**
     * Test getting maximal population size.
     */
    @Test
    public void testListPopulationGetMaxPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test setting maximal population size.
     */
    @Test
    public void testListPopulationSetMaxPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setting maximal population size to value smaller than current population size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetMaxPopulationSizeToSmallerValue() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new DummyChromosome(1));
        population.setPopulationLimit(4);
    }

    /**
     * Test getting current population size.
     */
    @Test
    public void testListPopulationGetCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new DummyChromosome(1));
        assertEquals(1, population.getPopulationSize());
    }
}

class DummyChromosome implements Chromosome {

    private int fitness;

    public DummyChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(Chromosome o) {
        DummyChromosome other = (DummyChromosome) o;
        return Double.compare(this.getFitness(), other.getFitness());
    }

    @Override
    public String toString() {
        return "DummyChromosome{" +
                "fitness=" + fitness +
                '}';
    }
}