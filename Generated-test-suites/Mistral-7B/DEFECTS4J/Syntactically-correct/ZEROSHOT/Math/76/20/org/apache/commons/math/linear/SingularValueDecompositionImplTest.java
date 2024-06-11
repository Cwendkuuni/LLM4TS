import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.SingularValueDecompositionSolver;
import org.junit.Assert;

public class SingularValueDecompositionImplTest {

    @org.junit.Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl singularValueDecomposition =
                new SingularValueDecompositionImpl(matrix, 2);

        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{
                {-0.5464362227121663, -0.8132721600188464, 0.1247492952233645},
                {0.5464362227121663, 0.8132721600188464, 0.1247492952233645},
                {0.8164965809277261, -0.4422234818952366, -0.4082834465366618}
        });

        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{
                {12.15521156277783, 0.0},
                {0.0, 7.56571382611111}
        });

        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{
                {-0.3535533905932737, 0.894427180886596},
                {0.894427180886596, 0.3535533905932737},
                {-0.4082834465366618, -0.442223481895237}
        });

        RealMatrix u = singularValueDecomposition.getU();
        RealMatrix s = singularValueDecomposition.getS();
        RealMatrix v = singularValueDecomposition.getV();

        Assert.assertTrue(u.equals(expectedU, 0.0001));
        Assert.assertTrue(s.equals(expectedS, 0.0001));
        Assert.assertTrue(v.equals(expectedV, 0.0001));

        RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{
                {-0.5464362227121663, 0.5464362227121663, 0.8164965809277261},
                {-0.8132721600188464, 0.8132721600188464, -0.4082834465366618},
                {0.1247492952233645, 0.1247492952233645, -0.4422234818952366}
        });

        RealMatrix ut = singularValueDecomposition.getUT();
        Assert.assertTrue(ut.equals(expectedUT, 0.0001));

        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{
                {-0.3535533905932737, 0.894427180886596, -0.4082834465366618},
                {0.894427180886596, 0.3535533905932737, 0.442223481895237}
        });

        RealMatrix vt = singularValueDecomposition.getVT();
        Assert.assertTrue(vt.equals(expectedVt, 0.0001));

        RealMatrix expectedSquareRootS = new Array2DRowRealMatrix(new double[][]{
                {3.484365217728416, 0.0},
                {0.0, 2.732543397183134}
        });

        RealMatrix sqrtS = new SingularValueDecompositionSolver(singularValueDecomposition).getInverse();
        RealMatrix expectedSInverse = new Array2DRowRealMatrix(new double[][]{
                {0.2928932188134524, -0.396226415093573},
                {-0.396226415093573, 0.2928932188134524},
                {0.806357286172529, 0.4082834465366613}
        });

        RealMatrix inverse = singularValueDecomposition.getInverse();
        Assert.assertTrue(inverse.equals(expectedSInverse, 0.0001));
        RealMatrix expectedMultiply = new Array2DRowRealMatrix(new double[][]{
                {-2.821324661231235, 4.90442284832181},
                {4.90442284832181, -2.821324661231235}
        });

        RealMatrix multiply = inverse.multiply(matrix);
        Assert.assertTrue(multiply.equals(expectedMultiply, 0.0001));

        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{
                {0.1389042034578657, 0.1719165498095386},
                {0.1719165498095386, 0.2373823205017144}
        });

        RealMatrix covariance = singularValueDecomposition.getCovariance(1.0);
        Assert.assertTrue(covariance.equals(expectedCovariance, 0.0001));

        Assert.assertEquals(singularValueDecomposition.getNorm(), 3.484365217728416, 0.0001);
        Assert.assertEquals(singularValueDecomposition.getConditionNumber(), 12.15521156277783 / 0.02732543397183134, 0.0001);
        Assert.assertEquals(singularValueDecomposition.getRank(), 2);

        DecompositionSolver solver = singularValueDecomposition.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector solution = solver.solve(vector);
        Assert.assertEquals(solution.getDimension(), 3);
        Assert.assertEquals(solution.getEntry(0), 1.9999999999999999, 0.0001);
        Assert.assertEquals(solution.getEntry(1), 1.9999999999999999, 0.0001);
        Assert.assertEquals(solution.getEntry(2), 1.9999999999999999, 0.0001);

        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}
        });

        RealMatrix singularValueDecomposition2 =
                new SingularValueDecompositionImpl(matrix2, 2);
        RealMatrix expectedUT2 = new Array2DRowRealMatrix(new double[][]{
                {-0.3420563787315564, 0.901582485911548},
                {0.901582485911548, 0.3420563787315564},
                {-0.528811728680718, -0.818558666793785}
        });

        RealMatrix ut2 = singularValueDecomposition2.getUT();
        Assert.assertTrue(ut2.equals(expectedUT2, 0.0001));

    }

}