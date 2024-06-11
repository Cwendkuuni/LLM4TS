// Combined test file

import org.jdom2.*;
import org.jdom2.util.NamespacesStack;
import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

public class NamespaceStackTest {

    @BeforeClass
    public static void setUpClass() {
        // Static initializations, if any
    }

    @AfterClass
    public static void tearDownClass() {
        // Static tear-down, if any
    }

    @Before
    public void setUp() {
        // Instance initializations, if any
    }

    @After
    public void tearDown() {
        // Instance tear-down, if any
    }

    @Test
    public void testEmptyStack() {
        // Expert 1's test
    }

    @Test
    public void testBasicPushAndPop() {
        // Expert 1's test
    }

    @Test
    public void testPushMultipleNamespaces() {
        // Expert 1's and Expert 2's tests, combined
    }

    @Test
    public void testPushMultipleNamespacesWithSamePrefix() {
        // Expert 3's test
    }

    @Test(expected = IllegalStateException.class)
    public void testOverPop() {
        // Expert 1's test
    }

    @Test(expected = NullPointerException.class)
    public void testPushNullNamespace() {
        // Expert 2's test
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPushDuplicateNamespaces() {
        // Expert 3's test
    }

    @Test(expected = NullPointerException.class)
    public void testPushNullElement() {
        // Expert 2's test
    }
}