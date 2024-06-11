package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ListPopulation
 */
public class ListPopulationTest {

    /**
     * Test constructor ListPopulation(int populationLimit)
     */
    @Test
    public void testConstructorPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test constructor ListPopulation(List<Chromosome> chromosomes, int populationLimit)
     */
    @Test
    public void testConstructorChromosomesPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
        assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    /**
     * Test method setChromosomes
     */
    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
        assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    /**
     * Test method addChromosomes
     */
    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(10);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        chromosomes.add(chromosome1);

        population.addChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));

        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosome(chromosome2);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
        assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    /**
     * Test method getFittestChromosome
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(10);

        Chromosome chromosome1 = new DummyChromosome(100);
        Chromosome chromosome2 = new DummyChromosome(99);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Chromosome fittest = population.getFittestChromosome();
        assertEquals(chromosome1, fittest);
    }

    /**
     * Test method getPopulationLimit
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test method setPopulationLimit
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);

        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(10, population.getPopulationSize());

        population.setPopulationLimit(15);
        assertEquals(15, population.getPopulationLimit());
    }

    /**
     * Test method getPopulationSize
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());

        population.addChromosome(new DummyChromosome(1));
        assertEquals(1, population.getPopulationSize());
    }

    /**
     * Test method toString
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(10);

        Chromosome chromosome1 = new DummyChromosome(1);
        population.addChromosome(chromosome1);

        String result = population.toString();
        String expected = "[Chromosome [fitness=0.0, genes=[], id=0], Chromosome [fitness=0.0, genes=[], id=1]]";
        assertEquals(expected, result);
    }

    /**
     * Test method iterator
     */
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(10);

        Chromosome chromosome1 = new DummyChromosome(1);
        population.addChromosome(chromosome1);

        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosome1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test constructor with negative populationLimit
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorPopulationLimitNegative() {
        new ListPopulation(-5);
    }

    /**
     * Test constructor with empty list of chromosomes
     */
    @Test(expected = NullArgumentException.class)
    public void testConstructorChromosomesNull() {
        new ListPopulation(null, 10);
    }

    /**
     * Test constructor with list of chromosomes larger than populationLimit
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorChromosomesTooLarge() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new DummyChromosome(3));

        new ListPopulation(chromosomes, 2);
    }

    /**
     * Test method setChromosomes with null list
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNull() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    /**
     * Test method setChromosomes with list of chromosomes larger than populationLimit
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesTooLarge() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        population.setChromosomes(chromosomes);
    }

    /**
     * Test method addChromosomes with null collection
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNull() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(null);
    }

    /**
     * Test method addChromosomes with collection of chromosomes larger than populationLimit
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesTooLarge() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        population.addChromosomes(chromosomes);
    }

    /**
     * Test method addChromosome with null chromosome
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomeNull() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(null);
    }

    /**
     * Test method addChromosome when population limit is reached
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomePopulationLimitReached() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(4));
        population.addChromosome(new DummyChromosome(5));

        Chromosome chromosome6 = new DummyChromosome(6);
        population.addChromosome(chromosome6);
    }

    /**
     * Test method getFittestChromosome when population is empty
     */
    @Test
    public void testGetFittestChromosomeEmptyPopulation() {
        ListPopulation population = new ListPopulation(10);
        assertNull(population.getFittestChromosome());
    }

    /**
     * Test method setPopulationLimit with negative populationLimit
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-5);
    }

    /**
     * Test method setPopulationLimit with population size greater than new population limit
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new DummyChromosome(1));

        population.setPopulationLimit(5);
    }

    /**
     * Dummy implementation of {@link Chromosome} for testing purposes
     */
    private static class DummyChromosome implements Chromosome {

        private int id;
        private double fitness;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return fitness;
        }

        @Override
        public void setFitness(double fitness) {
            this.fitness = fitness;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Chromosome [fitness=" + fitness + ", genes=[" + id + "], id=" + id + "]";
        }

        @Override
        public int compareTo(Chromosome o) {
            if (o instanceof DummyChromosome) {
                DummyChromosome other = (DummyChromosome) o;
                if (fitness > other.fitness) {
                    return 1;
                } else if (fitness < other.fitness) {
                    return -1;
                } else {
                    return 0;
                }
            }
            fail("Unexpected type for comparison: " + o.getClass().getName());
            return 0;
        }
    }
}