package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private static final int TEST_DATA_LENGTH = 10;
    private static final int TEST_SEED = 12345;

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        // empty test
        assertNull(generator);
    }

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }
        };
        generator.setSeed(TEST_SEED);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final boolean expected = Math.random() > 0.5;
            final boolean actual = generator.nextBoolean();
            assertEquals(expected, actual);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }

            @Override
            public void clear() {
                nextGaussian = Double.NaN;
            }
        };
        generator.setSeed(TEST_SEED);
        final byte[] expected = new byte[TEST_DATA_LENGTH];
        final byte[] actual   = new byte[TEST_DATA_LENGTH];
        generator.nextBytes(expected);
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }

            @Override
            protected double nextGaussian() {
                return Math.random();
            }
        };
        generator.setSeed(TEST_SEED);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final double expected = Math.random();
            final double actual = generator.nextDouble();
            assertEquals(expected, actual, 0.0001);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }

            @Override
            protected float nextGaussian() {
                return (float) Math.random();
            }
        };
        generator.setSeed(TEST_SEED);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final float expected = (float) Math.random();
            final float actual = generator.nextFloat();
            assertEquals(expected, actual, 0.0001f);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextGaussianWithNegativeSeed() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }

            @Override
            protected double nextGaussian() {
                return Math.sqrt(-Math.log(Math.random())) * Math.cos(2 * Math.PI * Math.random());
            }
        };
        generator.setSeed(-1);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * (1 << bits));
            }
        };
        generator.setSeed(TEST_SEED);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final int expected = (int) (Math.random() * Integer.MAX_VALUE);
            final int actual = generator.nextInt();
            assertEquals(expected, actual);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimit() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }
        };
        generator.setSeed(TEST_SEED);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final int limit = 10;
            final int expected = (int) (Math.random() * limit);
            final int actual = generator.nextInt(limit);
            assertEquals(expected, actual);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }

            @Override
            protected long nextLong() {
                final long high = ((long) next(32)) << 32;
                final int low = next(32);
                return high | low;
            }
        };
        generator.setSeed(TEST_SEED);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final long expected = (long) (Math.random() * Long.MAX_VALUE);
            final long actual = generator.nextLong();
            assertEquals(expected, actual);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        final BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                this.seed = seed;
            }

            @Override
            public void setSeed(int[] seed) {
                this.seed = IntStream.of(seed).sum();
            }

            @Override
            public void setSeed(long seed) {
                this.seed = seed;
            }

            @Override
            protected int next(int bits) {
                return (int) (Math.random() * 256);
            }

            @Override
            protected double nextGaussian() {
                return Math.sqrt(-Math.log(Math.random())) * Math.cos(2 * Math.PI * Math.random());
            }

            @Override
            public void clear() {
                nextGaussian = Double.NaN;
            }
        };
        generator.setSeed(TEST_SEED);
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isFinite(generator.nextGaussian()));
    }
}