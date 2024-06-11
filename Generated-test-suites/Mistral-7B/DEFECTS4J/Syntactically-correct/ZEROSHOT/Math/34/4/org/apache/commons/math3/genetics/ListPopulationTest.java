import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidParameters() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());

        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome 1";
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome 2";
            }
        });

        assertEquals(2, population.getPopulationSize());
        List<Chromosome> chromosomes = population.getChromosomes();
        assertTrue(chromosomes.contains(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome 1";
            }
        }));
        assertTrue(chromosomes.contains(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome 2";
            }
        }));
        assertEquals(population.getFittestChromosome(), chromosomes.get(1));
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomesList() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithChromosomesListBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 1.0;
                }

                @Override
                public String toString() {
                    return "Chromosome " + i;
                }
            });
        }
        new ListPopulation(10, chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithAddChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(new ArrayList<Chromosome>() {{
            add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 1.0;
                }

                @Override
                public String toString() {
                    return "Chromosome 1";
                }
            });
            add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 2.0;
                }

                @Override
                public String toString() {
                    return "Chromosome 2";
                }
            });
            add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 3.0;
                }

                @Override
                public String toString() {
                    return "Chromosome 3";
                }
            });
        }});
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationWithSetChromosomesSmallerThanCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "Chromosome 1";
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "Chromosome 2";
            }
        });

        List<Chromosome> smallerList = new ArrayList<>();
        smallerList.add(population.getChromosomes().get(0));

        population.setChromosomes(smallerList);
    }
}