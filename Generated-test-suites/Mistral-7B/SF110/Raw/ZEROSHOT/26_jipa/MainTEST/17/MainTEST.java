package jipa;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @BeforeClass
    public static void setup() {
        // Initialize the Main class with a sample input file
        Main.loadInstructions("sample.txt");
    }

    @Test
    public void testHalt() {
        Main.processInstruction("halt");
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testOut() {
        // Set up some variables for the test
        Variable.setVariableValue("message", "Hello, World!");
        
        Main.processInstruction("out message");
        assertEquals("Hello, World!\n", System.out.getHistogram().toString());
    }

    @Test
    public void testIn() {
        // Set up some variables for the test
        Variable.setVariableValue("number", 42);

        Main.processInstruction("in x");
        int input = 7;
        Main.processInstruction("set x 0");
        Main.processInstruction("in x");
        assertEquals(input, Variable.getVariableValue("x"));
    }

    @Test
    public void testMov() {
        // Set up some variables for the test
        Variable.setVariableValue("a", 10);
        Variable.setVariableValue("b", 0);

        Main.processInstruction("mov a b");
        assertEquals(10, Variable.getVariableValue("b"));
    }

    @Test
    public void testCpy() {
        // Set up some variables for the test
        Variable.setVariableValue("a", 10);
        Variable.setVariableValue("b", 5);
        Variable.setVariableValue("c", 0);

        Main.processInstruction("cpy b a");
        assertEquals(5, Variable.getVariableValue("a"));
        assertEquals(10, Variable.getVariableValue("b"));
        
        Main.processInstruction("cpy a c");
        assertEquals(10, Variable.getVariableValue("c"));
    }

    @Test
    public void testSet() {
        Variable.setVariableValue("a", 0);

        Main.processInstruction("set a 42");
        assertEquals(42, Variable.getVariableValue("a"));
    }

    @Test
    public void testDel() {
        Variable.addVariable("x");
        Variable.addVariable("y");

        Main.processInstruction("del x");
        assertFalse(Variable.hasVariable("x"));
        assertTrue(Variable.hasVariable("y"));
    }

    @Test
    public void testArr() {
        Variable.addVariable("arr");

        Main.processInstruction("arr arr 3");
        assertTrue(Variable.hasVariable("arr"));
        assertTrue(Variable.hasVariable("arr0"));
        assertTrue(Variable.hasVariable("arr1"));
        assertTrue(Variable.hasVariable("arr2"));
    }

    @Test
    public void testVar() {
        Variable.addVariable("x");

        Main.processInstruction("var x");
        assertEquals(Variable.getVariableValue("x"), Variable.getVariableValue("x"));
    }

    @Test
    public void testInc() {
        Variable.setVariableValue("a", 5);

        Main.processInstruction("inc a");
        assertEquals(6, Variable.getVariableValue("a"));
    }

    @Test
    public void testDec() {
        Variable.setVariableValue("a", 10);

        Main.processInstruction("dec a");
        assertEquals(9, Variable.getVariableValue("a"));
    }

    @Test
    public void testAdd() {
        Variable.setVariableValue("a", 5);
        Variable.setVariableValue("b", 3);

        Main.processInstruction("add a b");
        assertEquals(8, Variable.getVariableValue("a"));
    }

    @Test
    public void testSub() {
        Variable.setVariableValue("a", 10);
        Variable.setVariableValue("b", 3);

        Main.processInstruction("sub a b");
        assertEquals(7, Variable.getVariableValue("a"));
    }

    @Test
    public void testMul() {
        Variable.setVariableValue("a", 5);
        Variable.setVariableValue("b", 3);

        Main.processInstruction("mul a b");
        assertEquals(15, Variable.getVariableValue("a"));
    }

    @Test
    public void testDiv() {
        Variable.setVariableValue("a", 10);
        Variable.setVariableValue("b", 3);

        Main.processInstruction("div a b");
        assertEquals(3, Variable.getVariableValue("a"));
        assertEquals(3, Variable.getVariableValue("b"));
    }

    @Test
    public void testMod() {
        Variable.setVariableValue("a", 10);
        Variable.setVariableValue("b", 3);

        Main.processInstruction("mod a b");
        assertEquals(1, Variable.getVariableValue("a"));
        assertEquals(3, Variable.getVariableValue("b"));
    }

    @Test
    public void testXor() {
        Variable.setVariableValue("a", 5);
        Variable.setVariableValue("b", 3);

        Main.processInstruction("xor a b");
        assertEquals(2, Variable.getVariableValue("a"));
    }

    @Test
    public void testOr() {
        Variable.setVariableValue("a", 5);
        Variable.setVariableValue("b", 3);

        Main.processInstruction("or a b");
        assertEquals(6, Variable.getVariableValue("a"));
    }

    @Test
    public void testAnd() {
        Variable.setVariableValue("a", 5);
        Variable.setVariableValue("b", 3);

        Main.processInstruction("and a b");
        assertEquals(1, Variable.getVariableValue("a"));
    }

    @Test
    public void testJmp() {
        Main.processInstruction("jmp 10");
        assertEquals(10, Main.iPtr);
    }

    @Test
    public void testJnz() {
        Variable.setVariableValue("a", 42);

        Main.processInstruction("jnz a 10");
        assertEquals(11, Main.iPtr);

        Variable.setVariableValue("a", 0);

        Main.processInstruction("jnz a 10");
        assertEquals(12, Main.iPtr);
    }

    @Test
    public void testJz() {
        Variable.setVariableValue("a", 42);

        Main.processInstruction("jz a 10");
        assertEquals(12, Main.iPtr);

        Variable.setVariableValue("a", 0);

        Main.processInstruction("jz a 10");
        assertEquals(11, Main.iPtr);
    }

    @Test
    public void testJe() {
        Variable.setVariableValue("a", 42);
        Variable.setVariableValue("b", 42);

        Main.processInstruction("je a b 10");
        assertEquals(11, Main.iPtr);

        Variable.setVariableValue("b", 43);

        Main.processInstruction("je a b 10");
        assertEquals(12, Main.iPtr);
    }

    @Test
    public void testJne() {
        Variable.setVariableValue("a", 42);
        Variable.setVariableValue("b", 42);

        Main.processInstruction("jne a b 10");
        assertEquals(12, Main.iPtr);

        Variable.setVariableValue("b", 43);

        Main.processInstruction("jne a b 10");
        assertEquals(11, Main.iPtr);
    }

    @Test
    public void testCmp() {
        Variable.setVariableValue("a", 42);
        Variable.setVariableValue("b", 43);

        Main.processInstruction("cmp a b");
        assertEquals(0, Variable.getVariableValue("z"));

        Variable.setVariableValue("b", 42);

        Main.processInstruction("cmp a b");
        assertEquals(1, Variable.getVariableValue("z"));
    }
}