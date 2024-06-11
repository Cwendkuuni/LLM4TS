import org.apache.commons.math3.ExceptionEasyBase;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        // Test constructor with list and population limit
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Assert.assertEquals("Size of population should be 10", 10, population.size());

        // Test constructor with population limit and elitism rate
        ElitisticListPopulation population1 = new ElitisticListPopulation(10, 0.9);
        Assert.assertEquals("Size of population should be 10", 10, population1.size());

        // Test nextGeneration method with empty list
        ElitisticListPopulation population2 = new ElitisticListPopulation(Collections.emptyList(), 10, 0.9);
        List<Chromosome> nextGeneration = population2.nextGeneration();
        Assert.assertEquals("Size of next generation should be 10", 10, nextGeneration.size());

        // Test nextGeneration method with list
        List<Chromosome> oldChromosomes = new ArrayList<>();
        oldChromosomes.add(new Chromosome() {
            @Override
            public Object getAllele(int geneIndex) {
                return 1.0;
            }

            @Override
            public void setAllele(int geneIndex, Object newAllele) {

            }

            @Override
            public double fitness() {
                return 0.5;
            }
        });
        oldChromosomes.add(new Chromosome() {
            @Override
            public Object getAllele(int geneIndex) {
                return 0.0;
            }

            @Override
            public void setAllele(int geneIndex, Object newAllele) {

            }

            @Override
            public double fitness() {
                return 0.2;
            }
        });
        oldChromosomes.add(new Chromosome() {
            @Override
            public Object getAllele(int geneIndex) {
                return 1.0;
            }

            @Override
            public void setAllele(int geneIndex, Object newAllele) {

            }

            @Override
            public double fitness() {
                return 0.8;
            }
        });

        ElitisticListPopulation population3 = new ElitisticListPopulation(oldChromosomes, 10, 0.9);
        List<Chromosome> nextGeneration1 = population3.nextGeneration();
        int boundIndex = (int) FastMath.ceil((1.0 - 0.9) * oldChromosomes.size());

        Assert.assertEquals("Size of next generation should be 10", 10, nextGeneration1.size());
        Assert.assertEquals("First chromosome in next generation should be the best chromosome",
                oldChromosomes.get(oldChromosomes.size() - 1), nextGeneration1.get(0));

        for (int i = 1; i < boundIndex; i++) {
            Assert.assertEquals("Chromosomes in next generation should be the same as in old generation",
                    oldChromosomes.get(oldChromosomes.size() - i - 1), nextGeneration1.get(i));
        }

        List<Chromosome> oldChromosomes2 = new ArrayList<>();
        oldChromosomes2.add(new Chromosome() {
            @Override
            public Object getAllele(int geneIndex) {
                return 1.0;
            }

            @Override
            public void setAllele(int geneIndex, Object newAllele) {

            }

            @Override
            public double fitness() {
                return 0.4;
            }
        });
        oldChromosomes2.add(new Chromosome() {
            @Override
            public Object getAllele(int geneIndex) {
                return 0.0;
            }

            @Override
            public void setAllele(int geneIndex, Object newAllele) {

            }

            @Override
            public double fitness() {
                return 0.1;
            }
        });
        oldChromosomes2.add(new Chromosome() {
            @Override
            public Object getAllele(int geneIndex) {
                return 1.0;
            }

            @Override
            public void setAllele(int geneIndex, Object newAllele) {

            }

            @Override
            public double fitness() {
                return 0.7;
            }
        });

        ElitisticListPopulation population4 = new ElitisticListPopulation(oldChromosomes2, 10, 0.9);
        List<Chromosome> nextGeneration2 = population4.nextGeneration();

        Assert.assertEquals("Size of next generation should be 10", 10, nextGeneration2.size());
        Assert.assertEquals("First chromosome in next generation should be the best chromosome",
                oldChromosomes2.get(oldChromosomes2.size() - 1), nextGeneration2.get(0));

        for (int i = 1; i < boundIndex; i++) {
            Assert.assertEquals("Chromosomes in next generation should be the same as in old generation",
                    oldChromosomes2.get(oldChromosomes2.size() - i - 1), nextGeneration2.get(i));
        }

        // Test setElitismRate method
        ElitisticListPopulation population5 = new ElitisticListPopulation(10, 0.5);
        population5.setElitismRate(0.7);
        Assert.assertEquals("Elitism rate should be 0.7", 0.7, population5.getElitismRate(), 0.001);

        // Test out of range exception
        try {
            ElitisticListPopulation population6 = new ElitisticListPopulation(10, 1.1);
        } catch (ExceptionEasyBase e) {
            Assert.assertEquals("Message of exception should be 'Elitism rate should be between 0.0 and 1.0'",
                    "Elitism rate should be between 0.0 and 1.0", e.getMessage());
        }
    }

}