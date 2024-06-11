package Newzgrabber.tests;

import org.junit.*;
import static org.junit.Assert.*;
import Newzgrabber.BatchDriver;
import static Newzgrabber.MockNNTP.mockNNTP;

public class BatchDriverTest {

    @BeforeClass
    public static void setup() {
        Newzgrabber.IsBatch = true;
    }

    @AfterClass
    public static void tearDown() {
        Newzgrabber.IsBatch = false;
    }

    @Before
    public void setupTest() {
        Newzgrabber.nf = new Newzgrabber.NewzFactory();
    }

    // Method tests
    //...

    // Edge case tests
    //...

}