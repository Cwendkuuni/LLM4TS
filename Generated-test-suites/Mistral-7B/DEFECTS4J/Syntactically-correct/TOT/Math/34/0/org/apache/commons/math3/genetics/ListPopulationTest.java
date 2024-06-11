package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationWithEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
        assertTrue(population.isEmpty());
    }

    @Test
    public void testListPopulationWithValidListOfChromosomes() {
        ListPopulation population = new ListPopulation(Arrays.asList(
                new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3)), 5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(3, population.getPopulationSize());
        assertEquals(population.getFittestChromosome(), population.getChromosomes().get(0));
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = Arrays.asList(
                new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3));
        population.setChromosomes(chromosomes);
        assertEquals(3, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetNullChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = Arrays.asList(
                new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4));
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = Arrays.asList(
                new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3));
        population.addChromosomes(chromosomes);
        assertEquals(3, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationAddNullChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = Arrays.asList(
                new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4));
        population.addChromosomes(chromosomes);
    }

    private static class DummyChromosome implements Chromosome {
        private final int value;

        public DummyChromosome(int value) {
            this.value = value;
        }

        @Override
        public int getFitness() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DummyChromosome that = (DummyChromosome) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }
}