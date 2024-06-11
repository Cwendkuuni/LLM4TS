import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);
        assertEquals(3, md.getDimension());
        assertEquals(8, md.getSizes()[0]);
        assertEquals(4, md.getSizes()[1]);
        assertEquals(3, md.getSizes()[2]);
        assertEquals(24, md.getSize());

        try {
            new MultidimensionalCounter(new int[]{-1, 4, 3});
            fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new MultidimensionalCounter(new int[]{1, 4, -3});
            fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new MultidimensionalCounter(new int[]{1, 4});
            fail("DimensionMismatchException expected");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = md.iterator();

        int[] counts = new int[3];
        for (int i = 0; i < 24; i++) {
            counts[it.next().getCounts()[last]]++;
        }

        for (int i = 0; i < 3; i++) {
            assertEquals(8, counts[i]);
        }

        it = md.iterator();
        int c = 0;
        while (it.hasNext()) {
            int[] counts2 = it.next().getCounts();
            for (int i = 0; i < 3; i++) {
                assertEquals(counts[i], counts2[i]);
            }
            c++;
        }
        assertEquals(24, c);
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter md = new MultidimensionalCounter(sizes);

        for (int i = 0; i < 24; i++) {
            int[] counts = md.getCounts(md.getCount(i));
            for (int j = 0; j < 3; j++) {
                assertEquals(counts[j], i % sizes[j]);
            }
        }
    }
}