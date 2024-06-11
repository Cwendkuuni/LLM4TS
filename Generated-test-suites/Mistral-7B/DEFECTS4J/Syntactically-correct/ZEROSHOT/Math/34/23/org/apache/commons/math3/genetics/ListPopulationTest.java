package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for ListPopulation.
 */
public class ListPopulationTest {

    /**
     * Test method for {@link ListPopulation#ListPopulation(int)}.
     */
    @Test
    public void testListPopulation() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test method for {@link ListPopulation#ListPopulation(List, int)}.
     */
    @Test
    public void testListPopulationWithList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        Chromosome c3 = new Chromosome(3);
        chromosomes.add(c1);
        chromosomes.add(c2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(c1, population.getChromosomes().get(0));
        assertEquals(c2, population.getChromosomes().get(1));
    }

    /**
     * Test method for {@link ListPopulation#setChromosomes(List)}.
     */
    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        chromosomes.add(c1);
        chromosomes.add(c2);

        population.setChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(c1, population.getChromosomes().get(0));
        assertEquals(c2, population.getChromosomes().get(1));
    }

    /**
     * Test method for {@link ListPopulation#addChromosomes(Collection)}.
     */
    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        chromosomes.add(c1);
        chromosomes.add(c2);

        population.addChromosomes(chromosomes);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(c1, population.getChromosomes().get(0));
        assertEquals(c2, population.getChromosomes().get(1));
    }

    /**
     * Test method for {@link ListPopulation#getChromosomes()}.
     */
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        chromosomes.add(c1);
        chromosomes.add(c2);
        population.setChromosomes(chromosomes);

        List<Chromosome> result = population.getChromosomes();
        assertEquals(2, result.size());
        assertEquals(c1, result.get(0));
        assertEquals(c2, result.get(1));
    }

    /**
     * Test method for {@link ListPopulation#getChromosomeList()}.
     */
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        chromosomes.add(c1);
        chromosomes.add(c2);
        population.setChromosomes(chromosomes);

        List<Chromosome> result = population.getChromosomeList();
        assertEquals(2, result.size());
        assertEquals(c1, result.get(0));
        assertEquals(c2, result.get(1));
    }

    /**
     * Test method for {@link ListPopulation#addChromosome(Chromosome)}.
     */
    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        chromosomes.add(c1);

        population.addChromosome(c2);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(c1, population.getChromosomes().get(0));
        assertEquals(c2, population.getChromosomes().get(1));
    }

    /**
     * Test method for {@link ListPopulation#getFittestChromosome()}.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        Chromosome c3 = new Chromosome(3);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        population.setChromosomes(chromosomes);

        Chromosome result = population.getFittestChromosome();
        assertEquals(c3, result);
    }

    /**
     * Test method for {@link ListPopulation#setPopulationLimit(int)}.
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test method for {@link ListPopulation#setPopulationLimit(int)}.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(-1);
    }

    /**
     * Test method for {@link ListPopulation#setPopulationLimit(int)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        Chromosome c3 = new Chromosome(3);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        population.setChromosomes(chromosomes);

        population.setPopulationLimit(3);
    }

    /**
     * Test method for {@link ListPopulation#getPopulationSize()}.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        Chromosome c3 = new Chromosome(3);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        population.setChromosomes(chromosomes);

        assertEquals(3, population.getPopulationSize());
    }

    /**
     * Test method for {@link ListPopulation#toString()}.
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        Chromosome c3 = new Chromosome(3);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        population.setChromosomes(chromosomes);

        String result = population.toString();
        assertEquals("[Chromosome 1, Chromosome 2, Chromosome 3]", result);
    }

    /**
     * Test method for {@link ListPopulation#iterator()}.
     */
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        Chromosome c3 = new Chromosome(3);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        population.setChromosomes(chromosomes);

        Iterator<Chromosome> iterator = population.iterator();
        Chromosome result = iterator.next();

        assertEquals(c1, result);
        assertFalse(iterator.hasNext());
    }
}