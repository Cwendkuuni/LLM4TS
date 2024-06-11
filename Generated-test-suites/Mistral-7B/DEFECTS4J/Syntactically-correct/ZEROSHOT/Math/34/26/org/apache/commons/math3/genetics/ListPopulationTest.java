import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithInitialChromosomesAndLimit() {
        ListPopulation population = new ListPopulation(10);
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());

        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        };
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        population = new ListPopulation(population.getChromosomes(), 10);
        assertEquals(initialChromosomes, population.getChromosomes());
        assertEquals(2, population.getPopulationSize());

        Chromosome fittest = population.getFittestChromosome();
        assertTrue(chromosome2.getFitness() > fittest.getFitness());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativeLimit() {
        new ListPopulation(-1);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithZeroLimit() {
        new ListPopulation(0);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomesList() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithChromosomeListBiggerThanLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        };
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }

            @Override
            public String toString() {
                return "Chromosome3";
            }
        });

        new ListPopulation(chromosomes, 2);
    }

    @Test
    public void testListPopulationWithAddingChromosomes() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());

        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        };
        population.addChromosomes(Collections.singletonList(chromosome1));
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));

        population.addChromosomes(Collections.singletonList(chromosome2));
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome2, population.getChromosomes().get(1));

        population.addChromosomes(Collections.singletonList(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }

            @Override
            public String toString() {
                return "Chromosome3";
            }
        }));
        assertEquals(3, population.getPopulationSize());

        List<Chromosome> addedChromosomes = new ArrayList<>();
        addedChromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 4.0;
            }

            @Override
            public String toString() {
                return "Chromosome4";
            }
        });
        addedChromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 5.0;
            }

            @Override
            public String toString() {
                return "Chromosome5";
            }
        });

        population.addChromosomes(addedChromosomes);
        assertEquals(5, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(addedChromosomes));

        Chromosome fittest = population.getFittestChromosome();
        assertTrue(addedChromosomes.get(1).getFitness() > fittest.getFitness());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithTooManyAddedChromosomes() {
        ListPopulation population = new ListPopulation(10);

        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        };
        population.addChromosomes(Collections.singletonList(chromosome1));

        List<Chromosome> additionalChromosomes = new ArrayList<>();
        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }

            @Override
            public String toString() {
                return "Chromosome3";
            }
        };
        Chromosome chromosome4 = new Chromosome() {
            @Override
            public double getFitness() {
                return 4.0;
            }

            @Override
            public String toString() {
                return "Chromosome4";
            }
        };
        Chromosome chromosome5 = new Chromosome() {
            @Override
            public double getFitness() {
                return 5.0;
            }

            @Override
            public String toString() {
                return "Chromosome5";
            }
        };
        additionalChromosomes.add(chromosome3);
        additionalChromosomes.add(chromosome4);
        additionalChromosomes.add(chromosome5);

        population.addChromosomes(additionalChromosomes);
    }

    @Test
    public void testListPopulationWithSettingChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(Collections.singletonList(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        }));
        assertEquals(1, population.getPopulationSize());

        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        };
        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }

            @Override
            public String toString() {
                return "Chromosome3";
            }
        };

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        population.setChromosomes(chromosomes);
        assertEquals(3, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(chromosomes));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithTooManySetChromosomes() {
        ListPopulation population = new ListPopulation(10);

        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome1";
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome2";
            }
        };
        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }

            @Override
            public String toString() {
                return "Chromosome3";
            }
        };
        Chromosome chromosome4 = new Chromosome() {
            @Override
            public double getFitness() {
                return 4.0;
            }

            @Override
            public String toString() {
                return "Chromosome4";
            }
        };
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);

        population.setChromosomes(chromosomes);
    }
}