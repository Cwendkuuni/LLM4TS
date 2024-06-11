
package org.jcvi.jillion.assembly.ca.frg;

import org.junit.*;
import static org.junit.Assert.*;

public class DistanceTest {

    @BeforeClass
    public static void setUpClass() {
        // Set up class level environment if needed
    }

    @AfterClass
    public static void tearDownClass() {
        // Tear down class level environment if needed
    }

    @Before
    public void setUp() {
        // Set up per test environment if needed
    }

    @After
    public void tearDown() {
        // Tear down per test environment if needed
    }

    @Test
    public void testBuildDistance() {
        int min = 1;
        int max = 10;
        Distance distance = Distance.buildDistance(min, max);
        float expectedMean = (min + max) / 2.0f;
        float expectedStdDev = (max - min) / 6.0f;
        assertEquals(expectedMean, distance.getMean(), 0.01); // typical use case
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01); // typical use case
        min = 0;
        max = 5;
        distance = Distance.buildDistance(min, max);
        expectedMean = (min + max) / 2.0f;
        expectedStdDev = (max - min) / 6.0f;
        assertEquals(expectedMean, distance.getMean(), 0.01); // edge case
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01); // edge case
        min = -5;
        max = 5;
        distance = Distance.buildDistance(min, max);
        expectedMean = (min + max) / 2.0f;
        expectedStdDev = (max - min) / 6.0f;
        assertEquals(expectedMean, distance.getMean(), 0.01); // error scenario
        assertEquals(expectedStdDev, distance.getStdDev(), 0.01); // error scenario
    }

    @Test
    public void testBuildDistance2() {
        float mean = 5.5f;
        float stdDev = 2.5f;
        Distance distance = Distance.buildDistance(mean, stdDev);
        int expectedMin = (int) (mean - 3.0f * stdDev);
        int expectedMax = (int) (mean + 3.0f * stdDev);
        assertEquals(expectedMin, distance.getMin()); // typical use case
        assertEquals(expectedMax, distance.getMax()); // typical use case
        mean = 0.0f;
        stdDev = 1.0f;
        distance = Distance.buildDistance(mean, stdDev);
        expectedMin = 0;
        expectedMax = 1;
        assertEquals(expectedMin, distance.getMin()); // edge case
        assertEquals(expectedMax, distance.getMax()); // edge case
        mean = 5.0f;
        stdDev = -2.5f;
        distance = Distance.buildDistance(mean, stdDev);
        expectedMin = 2;
        expectedMax = 8;
        assertEquals(expectedMin, distance.getMin()); // error scenario
        assertEquals(expectedMax, distance.getMax()); // error scenario
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance originalDistance = Distance.buildDistance(1, 10);
        Distance expectedDistance = Distance.buildDistance(1.55f, 1.25f);
        float mean = originalDistance.getMean();
        float stdDev = originalDistance.getStdDev();
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(originalDistance);
        assertEquals(expectedDistance.getMean(), transformedDistance.getMean(), 0.01); // typical use case
        assertEquals(expectedDistance.getStdDev(), transformedDistance.getStdDev(), 0.01); // typical use case
        Distance invalidDistance = Distance.buildDistance(2, 3);
        transformedDistance = Distance.transformIntoCeleraAssemblerDistance(invalidDistance);
        assertEquals(invalidDistance, transformedDistance); // error scenario
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10);
        Distance distance2 = Distance.buildDistance(1, 10);
        assertEquals(distance1.hashCode(), distance2.hashCode()); // equal distances have the same hash codes
        distance1 = Distance.buildDistance(1, 11);
        assertNotEquals(distance1.hashCode(), distance2.hashCode()); // different distances have different hash codes
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10);
        Distance distance2 = Distance.buildDistance(1, 10);
        assertTrue(distance1.equals(distance2)); // equal distances are equal
        distance2 = Distance.buildDistance(1, 11);
        assertFalse(distance1.equals(distance2)); // different distances are not equal
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(1, 10);
        String expectedString = String.format("%.3f, std: %.3f", distance.getMean(), distance.getStdDev());
        assertEquals(expectedString, distance.toString()); // test basic formatting
        distance = Distance.buildDistance(0, 0);
        expectedString = "0.000, std: 0.000";
        assertEquals(expectedString, distance.toString()); // test zero distances
    }
}