package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests for the {@link ListPopulation} class.
 */
public class ListPopulationTest {

    /**
     * Tests the creation of a new ListPopulation with a valid population limit.
     */
    @Test
    public void testValidPopulationCreation() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    /**
     * Tests the creation of a new ListPopulation with an invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Tests the initialization of a new ListPopulation with existing chromosomes.
     */
    @Test
    public void testPopulationInitialization() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(0));
        Assert.assertEquals(chromosome2, population.getChromosomes().get(1));
    }

    /**
     * Tests the addition of chromosomes to an existing ListPopulation.
     */
    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        population.addChromosome(chromosome1);
        Assert.assertEquals(1, population.getPopulationSize());

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        population.addChromosomes(chromosomes);
        Assert.assertEquals(3, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(0));
        Assert.assertEquals(chromosome2, population.getChromosomes().get(1));
        Assert.assertEquals(chromosome3, population.getChromosomes().get(2));
    }

    /**
     * Tests the removal of chromosomes from a ListPopulation.
     */
    @Test
    public void testRemoveChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        population.addChromosome(chromosome1);
        Assert.assertEquals(1, population.getPopulationSize());

        population.remove(0);
        Assert.assertEquals(0, population.getPopulationSize());

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        population.addChromosomes(chromosomes);
        Assert.assertEquals(3, population.getPopulationSize());

        population.remove(1);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(chromosome1, population.getChromosomes().get(0));
        Assert.assertEquals(chromosome3, population.getChromosomes().get(1));
    }

    /**
     * Tests the getting of the fittest chromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(10);
        Chromosome chromosome2 = new DummyChromosome(5);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Assert.assertEquals(chromosome2, population.getFittestChromosome());
    }

    /**
     * Tests the setting of the population limit.
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());

        population.setPopulationLimit(10);
        Assert.assertEquals(5, population.getPopulationLimit()); // cannot decrease the limit

        population.addChromosome(new DummyChromosome(6));
        population.addChromosome(new DummyChromosome(7));
        population.setPopulationLimit(7);
        Assert.assertEquals(7, population.getPopulationSize());
    }

    /**
     * Tests the getting of the population size.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(0, population.getPopulationSize());

        population.addChromosome(new DummyChromosome(1));
        Assert.assertEquals(1, population.getPopulationSize());

        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(new DummyChromosome(2));
            add(new DummyChromosome(3));
            add(new DummyChromosome(4));
        }});
        Assert.assertEquals(4, population.getPopulationSize());
    }

    /**
     * Tests the string representation of a ListPopulation.
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        Assert.assertEquals("[Chromosome: 1, Chromosome: 2]", population.toString());
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));

        Iterator<Chromosome> iterator = population.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(population.getChromosomes().get(0), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(population.getChromosomes().get(1), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Dummy chromosome for testing.
     */
    private static class DummyChromosome implements Chromosome {
        private final int fitness;

        public DummyChromosome(int fitness) {
            this.fitness = fitness;
        }

        @Override
        public int getFitness() {
            return fitness;
        }

        @Override
        public String toString() {
            return "Chromosome: " + fitness;
        }

        @Override
        public int compareTo(Chromosome o) {
            return Integer.compare(this.fitness, o.getFitness());
        }
    }
}