package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        try {
            population = new ListPopulation(5);
        } catch (NotPositiveException e) {
            Assert.fail("Population limit should be positive");
        }
    }

    @Test
    public void testPopulationLimit() {
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testInitializationWithEmptyList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testInitializationWithNullList() {
        try {
            new ListPopulation(null, 10);
            Assert.fail("Initialization with null list should fail");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInitializationWithListBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomes.add(new FakeChromosome(i));
        }
        try {
            new ListPopulation(chromosomes, 5);
            Assert.fail("Initialization with list bigger than population limit should fail");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testSettingPopulationWithEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 10);
        population.setChromosomes(Collections.emptyList());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSettingPopulationWithNullList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 10);
        population.setChromosomes(null);
    }

    @Test
    public void testSettingPopulationWithListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomes.add(new FakeChromosome(i));
        }
        population.setChromosomes(chromosomes);
        Assert.assertEquals(6, population.getPopulationSize());
    }

    @Test
    public void testAddingChromosomeWithNullChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        try {
            population.addChromosome(null);
            Assert.fail("Adding null chromosome should fail");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddingChromosomeWithPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome = new FakeChromosome(5);
        population.addChromosome(chromosome);
        population.addChromosome(new FakeChromosome(6));
        Assert.assertEquals(5, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddingChromosomeWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome = new FakeChromosome(5);
        population.addChromosome(chromosome);
        population.addChromosome(new FakeChromosome(6));
        population.addChromosome(new FakeChromosome(7));
    }

    @Test
    public void testAddingChromosomesWithEmptyCollection() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(Collections.emptyList());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testAddingChromosomesWithCollectionBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomes.add(new FakeChromosome(i));
        }
        population.addChromosomes(chromosomes);
        Assert.assertEquals(6, population.getPopulationSize());
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new FakeChromosome(1);
        Chromosome chromosome2 = new FakeChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        List<Chromosome> chromosomes = population.getChromosomes();
        Assert.assertEquals(chromosome1, chromosomes.get(0));
        Assert.assertEquals(chromosome2, chromosomes.get(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingGetChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = population.getChromosomes();
        chromosomes.set(0, new FakeChromosome(6));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 3);
        Chromosome chromosome1 = new FakeChromosome(1);
        Chromosome chromosome2 = new FakeChromosome(2);
        Chromosome chromosome3 = new FakeChromosome(3);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        Chromosome fittestChromosome = population.getFittestChromosome();
        Assert.assertEquals(chromosome3, fittestChromosome);
    }

    @Test
    public void testSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new FakeChromosome(1));
        population.addChromosome(new FakeChromosome(2));
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome1 = new FakeChromosome(1);
        Chromosome chromosome2 = new FakeChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        String expected = "[" + chromosome1 + ", " + chromosome2 + "]";
        Assert.assertEquals(expected, population.toString());
    }

    private static class FakeChromosome implements Chromosome {
        private final int id;

        public FakeChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }
}