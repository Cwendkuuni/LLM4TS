import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ElitisticListPopulationTest {

 @Test
 public void testElitisticListPopulationCreation() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 0.5);
 assertEquals(10, population.getPopulationLimit());
 assertEquals(0.5, population.getElitismRate(), 0.01);
 }

 @Test(expected = OutOfRangeException.class)
 public void testElitisticListPopulationCreationOutOfRangeElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(Collections.emptyList(), 10, 1.2);
 }

 @Test
 public void testElitisticListPopulationAddChromosome() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
 Chromosome chromosome = new Chromosome() {
 @Override
 public int size() {
 return 1;
 }
 };
 population.addChromosome(chromosome);
 assertEquals(1, population.size());
 }

 @Test
 public void testElitisticListPopulationSort() {
 List<Chromosome> chromosomes = new java.util.ArrayList<>();
 Chromosome best = new Chromosome() {
 @Override
 public int size() {
 return 1;
 }
 };
 Chromosome secondBest = new Chromosome() {
 @Override
 public int size() {
 return 1;
 }
 };
 Chromosome worst = new Chromosome() {
 @Override
 public int size() {
 return 1;
 }
 };
 chromosomes.add(best);
 chromosomes.add(secondBest);
 chromosomes.add(worst);
 ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
 Collections.sort(population.getChromosomes());
 assertSame(best, population.getChromosomes().get(0));
 assertSame(secondBest, population.getChromosomes().get(1));
 }

 @Test
 public void testElitisticListPopulationNextGeneration() {
 List<Chromosome> chromosomes = new java.util.ArrayList<>();
 Chromosome best = new Chromosome() {
 @Override
 public int size() {
 return 1;
 }
 };
 Chromosome secondBest = new Chromosome() {
 @Override
 public int size() {
 return 1;
 }
 };
 Chromosome worst = new Chromosome() {
 @Override
 public int size() {
 return 1;
 }
 };
 chromosomes.add(best);
 chromosomes.add(secondBest);
 chromosomes.add(worst);
 ElitisticListPopulation parentPopulation = new ElitisticListPopulation(chromosomes, 10, 0.5);
 ElitisticListPopulation nextGeneration = parentPopulation.nextGeneration();
 assertEquals(10, nextGeneration.size());
 Chromosome elitism = best;
 for (int i = 0; i < nextGeneration.size() - 5; i++) {
 Chromosome chromosome = nextGeneration.getChromosome(i);
 assertNotSame(elitism, chromosome);
 }
 assertSame(best, nextGeneration.getChromosome(nextGeneration.size() - 5));
 }

 @Test
 public void testElitisticListPopulationSetElitismRate() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
 population.setElitismRate(0.6);
 assertEquals(0.6, population.getElitismRate(), 0.01);
 }

 @Test(expected = OutOfRangeException.class)
 public void testElitisticListPopulationSetElitismRateOutOfRange() {
 ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
 population.setElitismRate(-0.1);
 }
}