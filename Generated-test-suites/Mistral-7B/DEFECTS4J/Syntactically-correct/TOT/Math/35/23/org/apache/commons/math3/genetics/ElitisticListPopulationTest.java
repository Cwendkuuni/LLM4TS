package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.ElitisticListPopulationBuilder;
import org.junit.Test;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with initial list and population limit.
     */
    @Test
    public void testElitisticListPopulationConstructorWithListAndLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome() {
            @Override
            public Double[] getGenes() {
                return new Double[] {1.0, 2.0, 3.0};
            }
        });
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 10);
        assertNotNull(population);
        assertEquals(10, population.size());
    }

    /**
     * Test constructor with population limit.
     */
    @Test
    public void testElitisticListPopulationConstructorWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        assertNotNull(population);
        assertEquals(5, population.size());
    }

    /**
     * Test nextGeneration method with empty list.
     */
    @Test
    public void testNextGenerationWithEmptyList() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5);
        assertThrows(IllegalStateException.class, population::nextGeneration);
    }

    /**
     * Test nextGeneration method with list size smaller than population limit.
     */
    @Test
    public void testNextGenerationWithListSizeSmallerThanPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10);
        population.addAll(Collections.nCopies(5, new Chromosome() {
            @Override
            public Double[] getGenes() {
                return new Double[] {1.0, 2.0, 3.0};
            }
        }));
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(5, nextGeneration.size());
    }

    /**
     * Test nextGeneration method with list size equal to population limit.
     */
    @Test
    public void testNextGenerationWithListSizeEqualToPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10);
        population.addAll(Collections.nCopies(10, new Chromosome() {
            @Override
            public Double[] getGenes() {
                return new Double[] {1.0, 2.0, 3.0};
            }
        }));
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(10, nextGeneration.size());
    }

    /**
     * Test nextGeneration method with list size greater than population limit.
     */
    @Test
    public void testNextGenerationWithListSizeGreaterThanPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10);
        population.addAll(Collections.nCopies(11, new Chromosome() {
            @Override
            public Double[] getGenes() {
                return new Double[] {1.0, 2.0, 3.0};
            }
        }));
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(10, nextGeneration.size());
    }

    /**
     * Test setElitismRate method with valid elitism rate.
     */
    @Test
    public void testSetElitismRateWithValidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        population.setElitismRate(0.95);
        assertEquals(0.95, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10);
        population.setElitismRate(-0.1);
    }

    /**
     * Test ElitisticListPopulationBuilder.
     */
    @Test
    public void testElitisticListPopulationBuilder() {
        ElitisticListPopulationBuilder builder = new ElitisticListPopulationBuilder();
        builder.setElitismRate(0.9);
        ElitisticListPopulation population = builder.build(new ArrayList<>(), 10);
        assertNotNull(population);
        assertEquals(10, population.size());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

}