package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    @Test
    public void testFit() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}, {1.3, 2.3}, {1.4, 2.4}, {1.5, 2.5}, {1.6, 2.6}, {1.7, 2.7}, {1.8, 2.8}, {1.9, 2.9}};
        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(0.1, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2))), new Pair<>(0.9, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2)))));
        emm.fit(initialMixture);
        
        double expectedLogLikelihood = -5.582753481932251;
        double actualLogLikelihood = emm.getLogLikelihood();
        assertEquals("Unexpected log likelihood after fitting", expectedLogLikelihood, actualLogLikelihood, 1e-6);
        
        MixtureMultivariateNormalDistribution fittedModel = emm.getFittedModel();
        List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();
        assertEquals("Unexpected number of components", 2, components.size());
        
        MultivariateNormalDistribution component1 = components.get(0).getSecond();
        MultivariateNormalDistribution component2 = components.get(1).getSecond();
        double[][] means1 = component1.getMeans();
        double[][] means2 = component2.getMeans();
        RealMatrix covMat1 = component1.getCovarianceMatrix();
        RealMatrix covMat2 = component2.getCovarianceMatrix();
        
        // Component 1 means
        assertEquals("Unexpected mean for component 1 on dimension 0", 1.0, means1[0][0], 1e-6);
        assertEquals("Unexpected mean for component 1 on dimension 1", 2.0, means1[0][1], 1e-6);
        
        // Component 1 covariance matrix
        RealMatrix expectedCovMat1 = new Array2DRowRealMatrix(new double[][]{{0.69827549, 0.30133643}, {0.30133643, 0.70362693}});
        RealMatrix actualCovMat1 = covMat1;
        assertArrayEquals("Unexpected covariance matrix for component 1", expectedCovMat1.getData(), actualCovMat1.getData());
        
        // Component 2 means
        assertEquals("Unexpected mean for component 2 on dimension 0", 1.5, means2[0][0], 1e-6);
        assertEquals("Unexpected mean for component 2 on dimension 1", 2.5, means2[0][1], 1e-6);
        
        // Component 2 covariance matrix
        RealMatrix expectedCovMat2 = new Array2DRowRealMatrix(new double[][]{{0.34338885, 0.13985139}, {0.13985139, 0.39353675}});
        RealMatrix actualCovMat2 = covMat2;
        assertArrayEquals("Unexpected covariance matrix for component 2", expectedCovMat2.getData(), actualCovMat2.getData());
        
        // Weights
        double[] weights = new double[2];
        for (int i = 0; i < 2; ++i) {
            weights[i] = components.get(i).getFirst();
        }
        assertEquals("Unexpected weight for component 1", 0.9, weights[1], 1e-6);
        assertEquals("Unexpected weight for component 2", 0.1, weights[0], 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testFit_Unconverged() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}, {1.3, 2.3}, {1.4, 2.4}, {1.5, 2.5}, {1.6, 2.6}, {1.7, 2.7}, {1.8, 2.8}};
        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(0.1, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2))), new Pair<>(0.9, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2)))));
        emm.fit(initialMixture, 1, 1e-6);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFit_WrongSizeData() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}};
        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(0.1, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2))), new Pair<>(0.9, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2)))));
        emm.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_WrongSizeInitialMixture() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}, {1.3, 2.3}, {1.4, 2.4}, {1.5, 2.5}, {1.6, 2.6}, {1.7, 2.7}, {1.8, 2.8}};
        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(-1.0, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2))), new Pair<>(0.9, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2)))));
        emm.fit(initialMixture);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFit_SingularCovarianceMatrix() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}, {1.3, 2.3}, {1.4, 2.4}, {1.5, 2.5}, {1.6, 2.6}, {1.7, 2.7}, {1.8, 2.8}, {1.9, 2.9}, {1.95, 2.95}};
        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(0.1, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new SingularValueDecomposition(new Array2DRowRealMatrix(2, 2)).getSolution())), new Pair<>(0.9, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2)))));
        emm.fit(initialMixture);
    }

    @Test
    public void testEstimate() throws Exception {
        double[][] data = {{1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2}, {1.3, 2.3}, {1.4, 2.4}, {1.5, 2.5}, {1.6, 2.6}, {1.7, 2.7}, {1.8, 2.8}, {1.9, 2.9}};
        MixtureMultivariateNormalDistribution expectedModel = new MultivariateNormalMixtureExpectationMaximization().estimate(data, 2);
        MixtureMultivariateNormalDistribution estimatedModel = new MultivariateNormalMixtureExpectationMaximization().estimate(data, 2);
        assertEquals("Unexpected estimated model", expectedModel, estimatedModel);
    }
}