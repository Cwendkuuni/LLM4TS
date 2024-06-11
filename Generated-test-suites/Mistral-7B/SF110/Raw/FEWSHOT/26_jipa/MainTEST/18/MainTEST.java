package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_mov() {
        Main.TOTAL_INSTRUCTIONS = 1;
        Main.instruction = new String[] {"mov A 0"};
        Variable varA = new Variable("A");

        Main.processInstruction(Main.instruction[0]);

        assertEquals(0, varA.getValue());
    }

    @Test
    public void testProcessInstruction_set() {
        Main.TOTAL_INSTRUCTIONS = 2;
        Main.instruction = new String[] {"set A 5", "mov B A"};
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        Main.processInstruction(Main.instruction[0]);

        assertEquals(5, varA.getValue());
        assertEquals(5, varB.getValue());
    }

    @Test
    public void testProcessInstruction_inc() {
        Main.TOTAL_INSTRUCTIONS = 2;
        Main.instruction = new String[] {"set A 0", "inc A"};
        Variable varA = new Variable("A");

        Main.processInstruction(Main.instruction[0]);
        Main.processInstruction(Main.instruction[1]);

        assertEquals(1, varA.getValue());
    }

    @Test
    public void testProcessInstruction_add() {
        Main.TOTAL_INSTRUCTIONS = 3;
        Main.instruction = new String[] {"set A 5", "set B 3", "add A B"};
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        Main.processInstruction(Main.instruction[0]);
        Main.processInstruction(Main.instruction[1]);
        Main.processInstruction(Main.instruction[2]);

        assertEquals(8, varA.getValue());
    }

    @Test
    public void testProcessInstruction_jmp() {
        Main.TOTAL_INSTRUCTIONS = 4;
        Main.instruction = new String[] {"set A 0", "jmp label1", "label1: mov A 5", "mov B A"};
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[0]);
        Main.processInstruction(Main.instruction[1]);

        assertEquals(5, varA.getValue());
        assertEquals(5, varB.getValue());
    }

    @Test
    public void testProcessInstruction_jnz() {
        Main.TOTAL_INSTRUCTIONS = 5;
        Main.instruction = new String[] {"set A 0", "set B 1", "jnz label1", "mov A 5", "label1: mov B 2"};
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        Main.iPtr = 0;
        Main.processInstruction(Main.instruction[0]);
        Main.processInstruction(Main.instruction[1]);

        assertEquals(0, varA.getValue());
        assertEquals(1, varB.getValue());

        Main.iPtr = 3;
        Main.processInstruction(Main.instruction[2]);
        Main.processInstruction(Main.instruction[3]);

        assertEquals(5, varA.getValue());
        assertEquals(2, varB.getValue());
    }

    @Test
    public void testProcessInstruction_in() {
        Main.TOTAL_INSTRUCTIONS = 2;
        Main.instruction = new String[] {"set A 0", "in A"};
        Variable varA = new Variable("A");

        Main.processInstruction(Main.instruction[0]);
        Main.processInstruction(Main.instruction[1]);

        assertEquals(65, varA.getValue()); // assume input is 'A'
    }

    @Test
    public void testProcessInstruction_out() {
        Main.TOTAL_INSTRUCTIONS = 2;
        Main.instruction = new String[] {"set A 123", "out A"};
        Variable varA = new Variable("A");

        System.out.flush();
        System.setIn(new ByteArrayInputStream("".getBytes()));
        System.setOut(new ByteArrayOutputStream());

        Main.processInstruction(Main.instruction[0]);
        Main.processInstruction(Main.instruction[1]);

        assertEquals("123", System.out.getOutputStream().toString().trim());
    }
}