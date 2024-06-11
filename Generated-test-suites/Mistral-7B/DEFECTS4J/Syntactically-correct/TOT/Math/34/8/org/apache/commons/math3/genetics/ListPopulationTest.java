package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;

/**
 * Tests for the {@link ListPopulation} class.
 */
public class ListPopulationTest {

    /**
     * Test construction with population limit.
     */
    @org.junit.Test
    public void testConstructionWithPopulationLimit() {
        ListPopulation pop = new ListPopulation(3);
        assertEquals(3, pop.getPopulationSize());
        assertEquals(3, pop.getPopulationLimit());
    }

    /**
     * Test construction with empty list of chromosomes.
     */
    @org.junit.Test
    public void testConstructionWithEmptyChromosomeList() {
        ListPopulation pop = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(0, pop.getPopulationSize());
        assertEquals(5, pop.getPopulationLimit());
    }

    /**
     * Test construction with list of chromosomes.
     */
    @org.junit.Test
    public void testConstructionWithChromosomeList() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Arrays.asList(c1, c2), 3);
        assertEquals(2, pop.getPopulationSize());
        assertEquals(3, pop.getPopulationLimit());
        assertEquals(c1, pop.getChromosome(0));
        assertEquals(c2, pop.getChromosome(1));
    }

    /**
     * Test setting chromosomes.
     */
    @org.junit.Test
    public void testSettingChromosomes() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 3);
        pop.setChromosomes(Arrays.asList(c1, c2));
        assertEquals(2, pop.getPopulationSize());
        assertEquals(3, pop.getPopulationLimit());
        assertEquals(c1, pop.getChromosome(0));
        assertEquals(c2, pop.getChromosome(1));
    }

    /**
     * Test adding chromosomes.
     */
    @org.junit.Test
    public void testAddingChromosomes() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 3);
        pop.addChromosomes(Arrays.asList(c1, c2));
        assertEquals(2, pop.getPopulationSize());
        assertEquals(3, pop.getPopulationLimit());
        assertEquals(c1, pop.getChromosome(0));
        assertEquals(c2, pop.getChromosome(1));
    }

    /**
     * Test adding chromosomes when population limit is exceeded.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testAddingChromosomesWhenPopulationLimitIsExceeded() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 3);
        pop.addChromosomes(Arrays.asList(c1, c2, new DummyChromosome(3)));
    }

    /**
     * Test removing chromosomes.
     */
    @org.junit.Test
    public void testRemovingChromosomes() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Arrays.asList(c1, c2), 3);
        pop.removeChromosome(1);
        assertEquals(1, pop.getPopulationSize());
        assertEquals(3, pop.getPopulationLimit());
        assertNull(pop.getChromosome(1));
        assertEquals(c1, pop.getChromosome(0));
    }

    /**
     * Test getting fittest chromosome.
     */
    @org.junit.Test
    public void testGettingFittestChromosome() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Arrays.asList(c1, c2), 3);
        assertEquals(c2, pop.getFittestChromosome());
    }

    /**
     * Test setting population limit.
     */
    @org.junit.Test
    public void testSettingPopulationLimit() {
        ListPopulation pop = new ListPopulation(Arrays.asList(new DummyChromosome(1), new DummyChromosome(2)), 3);
        pop.setPopulationLimit(5);
        assertEquals(2, pop.getPopulationSize());
        assertEquals(5, pop.getPopulationLimit());
    }

    /**
     * Test setting population limit when current population size is smaller.
     */
    @org.junit.Test
    public void testSettingPopulationLimitWhenCurrentSizeIsSmaller() {
        ListPopulation pop = new ListPopulation(new ArrayList<>(), 3);
        pop.setPopulationLimit(5);
        assertEquals(0, pop.getPopulationSize());
        assertEquals(5, pop.getPopulationLimit());
    }

    /**
     * Test setting population limit when current population size is equal.
     */
    @org.junit.Test
    public void testSettingPopulationLimitWhenCurrentSizeIsEqual() {
        ListPopulation pop = new ListPopulation(Arrays.asList(new DummyChromosome(1), new DummyChromosome(2)), 3);
        pop.setPopulationLimit(3);
        assertEquals(2, pop.getPopulationSize());
        assertEquals(3, pop.getPopulationLimit());
    }

    /**
     * Test setting population limit when current population size is greater.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testSettingPopulationLimitWhenCurrentSizeIsGreater() {
        ListPopulation pop = new ListPopulation(Arrays.asList(new DummyChromosome(1), new DummyChromosome(2)), 3);
        pop.setPopulationLimit(1);
    }

    /**
     * Test getting current population size.
     */
    @org.junit.Test
    public void testGettingCurrentPopulationSize() {
        ListPopulation pop = new ListPopulation(Arrays.asList(new DummyChromosome(1), new DummyChromosome(2)), 3);
        assertEquals(2, pop.getPopulationSize());
    }

    /**
     * Test getting maximum population size.
     */
    @org.junit.Test
    public void testGettingMaximumPopulationSize() {
        ListPopulation pop = new ListPopulation(Arrays.asList(new DummyChromosome(1), new DummyChromosome(2)), 3);
        assertEquals(3, pop.getPopulationLimit());
    }

    /**
     * Test constructor with negative population limit.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructionWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test constructor with null list of chromosomes.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testConstructionWithNullChromosomeList() {
        new ListPopulation(null, 3);
    }

    /**
     * Test getting list of chromosomes.
     */
    @org.junit.Test
    public void testGettingListOfChromosomes() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Arrays.asList(c1, c2), 3);
        List<Chromosome> list = pop.getChromosomes();
        assertEquals(2, list.size());
        assertEquals(c1, list.get(0));
        assertEquals(c2, list.get(1));
    }

    /**
     * Test getting list of chromosomes when population is empty.
     */
    @org.junit.Test
    public void testGettingListOfChromosomesWhenPopulationIsEmpty() {
        ListPopulation pop = new ListPopulation(new ArrayList<>(), 3);
        List<Chromosome> list = pop.getChromosomes();
        assertEquals(0, list.size());
    }

    /**
     * Test getting list of chromosomes when population limit is smaller than current population size.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testGettingListOfChromosomesWhenPopulationLimitIsSmallerThanCurrentPopulationSize() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Arrays.asList(c1, c2, new DummyChromosome(3)), 2);
        List<Chromosome> list = pop.getChromosomes();
        assertEquals(2, list.size());
        assertEquals(c1, list.get(0));
        assertEquals(c2, list.get(1));
        pop.setPopulationLimit(1);
    }

    /**
     * Test iterator.
     */
    @org.junit.Test
    public void testIterator() {
        Chromosome c1 = new DummyChromosome(1);
        Chromosome c2 = new DummyChromosome(2);
        ListPopulation pop = new ListPopulation(Arrays.asList(c1, c2), 3);
        Iterator<Chromosome> it = pop.iterator();
        assertEquals(c1, it.next());
        assertTrue(it.hasNext());
        assertEquals(c2, it.next());
        assertFalse(it.hasNext());
    }

    /**
     * Test constructor with illegal population limit.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructionWithIllegalPopulationLimit() {
        new ListPopulation(Integer.MAX_VALUE);
    }

    /**
     * Dummy chromosome for testing.
     */
    private static class DummyChromosome implements Chromosome {

        private final int id;

        /**
         * Constructor.
         * @param id chromosome identifier
         */
        public DummyChromosome(final int id) {
            super();
            this.id = id;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(final Object o) {
            if (o == this) {
                return 0;
            }
            if (o instanceof Chromosome) {
                Chromosome other = (Chromosome) o;
                return Integer.compare(id, other.getId());
            }
            throw new ClassCastException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getId() {
            return id;
        }

    }

}