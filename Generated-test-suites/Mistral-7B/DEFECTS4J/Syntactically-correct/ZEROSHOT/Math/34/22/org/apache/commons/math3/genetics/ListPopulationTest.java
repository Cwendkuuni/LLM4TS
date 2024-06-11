package org.apache.commons.math3.genetics;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.math3.genetics.ListPopulation.NotPositiveException;
import static org.apache.commons.math3.genetics.ListPopulation.NumberIsTooLargeException;
import static org.apache.commons.math3.genetics.ListPopulation.NumberIsTooSmallException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test file for ListPopulation class.
 */
public class ListPopulationTest {
    private static final int POPULATION_LIMIT = 5;

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(POPULATION_LIMIT);
    }

    @Test
    public void testListPopulation_EmptyConstructor() {
        assertEquals(Collections.emptyList(), population.getChromosomes());
        assertEquals(POPULATION_LIMIT, population.getPopulationLimit());
    }

    @Test
    public void testListPopulation_WithInitialChromosomes() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        }, new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });

        population = new ListPopulation(initialChromosomes, POPULATION_LIMIT);

        assertEquals(initialChromosomes, population.getChromosomes());
        assertEquals(POPULATION_LIMIT, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulation_NegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulation_GetPopulationSize() {
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulation_AddMoreChromosomesThanLimit() {
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        });
    }

    @Test
    public void testListPopulation_AddChromosomes() {
        List<Chromosome> testChromosomes = new ArrayList<>();
        testChromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        population.addChromosomes(testChromosomes);
        assertEquals(testChromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulation_NullChromosomes() {
        new ListPopulation(null, POPULATION_LIMIT);
    }

    @Test
    public void testListPopulation_SetChromosomes() {
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        }, new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });

        population.setChromosomes(newChromosomes);
        assertEquals(newChromosomes, population.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulation_SetChromosomes_TooManyChromosomes() {
        List<Chromosome> newChromosomes = Arrays.asList(
                new Chromosome() {
                    @Override
                    public double getFitness() {
                        return 1.0;
                    }
                }, new Chromosome() {
                    @Override
                    public double getFitness() {
                        return 2.0;
                    }
                }, new Chromosome() {
                    @Override
                    public double getFitness() {
                        return 3.0;
                    }
                }
        );

        population.setChromosomes(newChromosomes);
    }

    @Test
    public void testListPopulation_AddChromosome() {
        Chromosome testChromosome = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };

        population.addChromosome(testChromosome);
        assertTrue(population.getChromosomes().contains(testChromosome));
    }

    @Test
    public void testListPopulation_GetFittestChromosome() {
        List<Chromosome> testChromosomes = Arrays.asList(
                new Chromosome() {
                    @Override
                    public double getFitness() {
                        return 3.0;
                    }
                }, new Chromosome() {
                    @Override
                    public double getFitness() {
                        return 1.0;
                    }
                }, new Chromosome() {
                    @Override
                    public double getFitness() {
                        return 2.0;
                    }
                }
        );
        population.setChromosomes(testChromosomes);
        Chromosome expected = testChromosomes.get(0);
        Chromosome actual = population.getFittestChromosome();
        assertSame(expected, actual);
    }

    @Test
    public void testListPopulation_GetPopulationLimit() {
        assertEquals(POPULATION_LIMIT, population.getPopulationLimit());
    }

    @Test
    public void testListPopulation_SetPopulationLimit_NotPositive() {
        assertThrows(NotPositiveException.class, () -> population.setPopulationLimit(-1));
    }

    @Test
    public void testListPopulation_SetPopulationLimit_SmallerThanCurrentSize() {
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        assertThrows(NumberIsTooSmallException.class, () -> population.setPopulationLimit(0));
    }

    @Test
    public void testListPopulation_SetPopulationLimit() {
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testListPopulation_ToString() {
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        });
        String expected = "[" +
                "org.apache.commons.math3.genetics.Chromosome@78a6b130," +
                "org.apache.commons.math3.genetics.Chromosome@15db9742]";
        String actual = population.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testListPopulation_Iterator() {
        Chromosome testChromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        };
        Chromosome testChromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        population.addChromosome(testChromosome1);
        population.addChromosome(testChromosome2);

        Iterator<Chromosome> iterator = population.iterator();
        List<Chromosome> testList = new ArrayList<>();
        testList.add(testChromosome1);
        testList.add(testChromosome2);

        Chromosome expected = testChromosome1;
        Chromosome actual = iterator.next();
        assertSame(expected, actual);

        assertFalse(iterator.hasNext());
    }
}