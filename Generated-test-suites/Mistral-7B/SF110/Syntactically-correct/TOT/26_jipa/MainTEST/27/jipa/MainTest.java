package jipa;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainTest {

    @Test
    public void testLoadInstructionsAndRun() {
        String fileName = "testInstructions.txt";
        Main.loadInstructions(fileName);
        Main.processInstruction("mov a 0");
        Main.processInstruction("inc a");
        Main.processInstruction("mov b a");
        Main.processInstruction("inc b");
        Main.processInstruction("mov a b");
        Main.processInstruction("dec a");
        Main.processInstruction("out a"); // should print "0"

        // test loading an empty file
        Main.loadInstructions("");
        Main.processInstruction("halt"); // should exit the program
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadInstructionsInvalidFile() {
        Main.loadInstructions("nonExistentFile.txt");
        Main.processInstruction("halt"); // should never reach this line
    }

    @Test
    public void testProcessInstructionsWithInvalidOpcode() {
        String fileName = "testInstructionsWithInvalidOpcode.txt";
        Main.loadInstructions(fileName);
        Main.iPtr = 0;

        Main.processInstruction("invalidOpcode");
    }

    @Test
    public void testProcessInstructionsWithInvalidArgument() {
        String fileName = "testInstructionsWithInvalidArgument.txt";
        Main.loadInstructions(fileName);
        Main.iPtr = 0;

        Main.processInstruction("mov a b"); // should throw an exception
    }

    @Test
    public void testProcessInstructionsWithJumpInstructions() {
        String fileName = "testInstructionsWithJumpInstructions.txt";
        Main.loadInstructions(fileName);

        Main.processInstruction("jmp label1");
        Main.processInstruction("label1: mov a 5");
        Main.processInstruction("jmp label2");
        Main.processInstruction("label2: mov b a");
        Main.processInstruction("jmp label3") ;
        Main.processInstruction("label3: halt");

        Main.iPtr = 0;
        String expectedOutput = "5";
        String output = "";

        while (Main.instruction[Main.iPtr].charAt(0) != 'o' || Main.instruction[Main.iPtr].charAt(1) != 'u' || Main.instruction[Main.iPtr].charAt(2) != 't') {
            if (Main.instruction[Main.iPtr].length() > 0) {
                output += Main.instruction[Main.iPtr] + "\n";
            }
            Main.processInstruction(Main.instruction[Main.iPtr]);
            ++Main.iPtr;
        }

        Assert.assertEquals(expectedOutput, output);
    }

    private String getTestDataFile(String fileName) {
        return new File("testData/" + fileName + ".txt").getAbsolutePath();
    }

    @Test
    public void testProcessInstructionsWithVarInstructions() {
        String fileName = "testInstructionsWithVarInstructions.txt";
        Main.loadInstructions(getTestDataFile(fileName));

        String expectedOutput = "1 2 3 4";
        String output = "";

        Main.processInstruction("arr a 4");
        for (String instruction : Arrays.asList("mov a 0", "mov b a", "mov a 1", "mov c a", "mov a 2", "mov d a", "mov e a", "mov a 3", "mov a b", "mov a c", "mov a d", "mov a e", "inc a", "inc a", "mov a 0", "mov a b", "mov b a", "out a")) {
            Main.processInstruction(instruction);
            output += Main.instruction[Main.iPtr] + "\n";
            ++Main.iPtr;
        }

        Assert.assertEquals(expectedOutput, output);
    }
}