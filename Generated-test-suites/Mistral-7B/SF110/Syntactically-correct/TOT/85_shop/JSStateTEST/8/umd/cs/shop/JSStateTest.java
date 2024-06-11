package umd.cs.shop.test;

import org.junit.*;
import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSStateTest {
    @BeforeClass
    public static void setUpClass() {
        // Set up any class level setup here
    }

    @AfterClass
    public static void tearDownClass() {
        // Tear down any class level teardown here
    }

    @Before
    public void setUp() {
        // Set up any test level setup here
    }

    @After
    public void tearDown() {
        // Tear down any test level teardown here
    }

    @Test
    public void testJSStateEmptyConstructor() {
        JSState jsState = new JSState();
        assertNotNull(jsState);
    }

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        s.addElement(new JSPredicateForm("p2"));
        JSState jsState = new JSState();
        jsState.addElementsToState(s);
        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        expected.addElement(new JSPredicateForm("p1"));
        expected.addElement(new JSPredicateForm("p2"));
        assertEquals(expected, jsState);
    }

    @Test
    public void testApplyOp() {
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("q1"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("p1"));
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1"));
        jsState.addElement(new JSPredicateForm("p2"));
        jsState.addElement(new JSPredicateForm("p3"));
        JSState result = jsState.applyOp(new JSOperator(new JSPredicateForm(":Operator op"), addL, delL), new JSSubstitution(), jsState, jsState);
        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        expected.addElement(new JSPredicateForm("p2"));
        expected.addElement(new JSPredicateForm("p3"));
        expected.addElement(new JSPredicateForm("q1"));
        assertEquals(expected, result.getState());
    }

    @Test
    public void testSatisfies() {
        // Test code here
    }

    @Test
    public void testSatisfiesAll() {
        // Test code here
    }

    @Test
    public void testSatisfiesTAm() {
        // Test code here
    }

    @Test
    public void testApply() {
        // Test code here
    }

}