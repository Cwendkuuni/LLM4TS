package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ListPopulation} class.
 */
public class ListPopulationTest {

    private ListPopulation population;

    /**
     * Sets up the test environment.
     * @throws Exception if test initialization fails
     */
    @Before
    public void setUp() throws Exception {
        population = new ListPopulation(10);
    }

    /**
     * Test constructor with population limit.
     */
    @Test
    public void testListPopulationConstructor() {
        assertEquals(Collections.emptyList(), population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test constructor with initial list of chromosomes and population limit.
     */
    @Test
    public void testListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {};
            }
        }, new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {};
            }
        });

        population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setChromosomes method.
     */
    @Test
    public void testSetChromosomesMethod() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {};
            }
        }, new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {};
            }
        });

        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.setChromosomes(chromosomes);
        assertEquals(chromosomes, pop.getChromosomes());
        assertEquals(5, pop.getPopulationLimit());
    }

    /**
     * Test constructor with negative population limit.
     */
    @Test
    public void testListPopulationConstructorNegativePopulationLimit() {
        assertThrows(NotPositiveException.class, () -> new ListPopulation(-5));
    }

    /**
     * Test adding chromosomes to the population.
     */
    @Test
    public void testAddChromosomeToPopulation() {
        Chromosome chromosome = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {};
            }
        };
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(11, population.getPopulationLimit());
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    /**
     * Test adding too many chromosomes to the population.
     */
    @Test
    public void testAddTooManyChromosomesToPopulation() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {};
            }
        });
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosomes(chromosomes));
    }

    /**
     * Test accessing the fittest chromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {1.0};
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {0.5};
            }
        });
        Chromosome fittest = population.getFittestChromosome();
        assertEquals(population.getChromosomes().get(0), fittest);
    }

    /**
     * Test getting the population limit.
     */
    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setting the population limit.
     */
    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    /**
     * Test setting the population limit to a value smaller than the current population size.
     */
    @Test
    public void testSetPopulationLimitTooSmall() {
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {};
            }
        });
        assertThrows(NumberIsTooSmallException.class, () -> population.setPopulationLimit(5));
    }

    /**
     * Test getting the population size.
     */
    @Test
    public void testGetPopulationSize() {
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {};
            }
        });
        assertEquals(1, population.getPopulationSize());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {1.0};
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {0.5};
            }
        });
        String result = population.toString();
        String expected = "[Chromosome[0], Chromosome[1]]";
        assertEquals(expected, result);
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {1.0};
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[] {0.5};
            }
        });

        Iterator<Chromosome> it = population.iterator();
        assertTrue(it.hasNext());
        Chromosome first = it.next();
        assertNotNull(first);
        assertTrue(population.getChromosomes().contains(first));
        assertTrue(it.hasNext());
        Chromosome second = it.next();
        assertNotNull(second);
        assertTrue(population.getChromosomes().contains(second));
        assertFalse(it.hasNext());
    }

    /**
     * Test constructor with empty list of chromosomes and population limit.
     */
    @Test
    public void testListPopulationConstructorWithEmptyChromosomesList() {
        population = new ListPopulation(Collections.emptyList(), 10);
        assertEquals(Collections.emptyList(), population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setting the chromosomes list with null argument.
     */
    @Test
    public void testSetChromosomesWithNullArgument() {
        assertThrows(NullArgumentException.class, () -> population.setChromosomes(null));
    }

    /**
     * Test setting the chromosomes list with a list containing null chromosomes.
     */
    @Test
    public void testSetChromosomesWithNullChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(null);
        assertThrows(NullArgumentException.class, () -> population.setChromosomes(chromosomes));
    }
}