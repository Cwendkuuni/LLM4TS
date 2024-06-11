package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    @Test
    public void testCalcDependencyGraph() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[icssa.maxn() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        // Add asserts here to test the behavior of the calcDependencyGraph method
        // You can check the values of edges and sepsets arrays after the method call
    }

    @Test
    public void testExistsSepSet() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[icssa.maxn() + 1][];
        SeparationSet oSepSet = icssa.existsSepSet(0, 1, 2, edges);
        // Add asserts here to test the behavior of the existsSepSet method
        // You can check the value of oSepSet object after the method call
    }

    @Test
    public void testNext() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        int x = icssa.next(0, 0, new boolean[][]{{}}, new boolean[][]{{}});
        // Add asserts here to test the behavior of the next method
        // You can check the value of x variable after the method call
    }

    @Test
    public void testCalcVeeNodes() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[icssa.maxn() + 1][];
        boolean[][] arrows = new boolean[icssa.maxn() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
        icssa.calcVeeNodes(edges, arrows, sepsets);
        // Add asserts here to test the behavior of the calcVeeNodes method
        // You can check the values of edges, arrows and sepsets arrays after the method call
    }

    @Test
    public void testCalcArcDirections() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[icssa.maxn() + 1][];
        boolean[][] arrows = new boolean[icssa.maxn() + 1][];
        icssa.calcArcDirections(edges, arrows);
        // Add asserts here to test the behavior of the calcArcDirections method
        // You can check the values of edges and arrows arrays after the method call
    }
}