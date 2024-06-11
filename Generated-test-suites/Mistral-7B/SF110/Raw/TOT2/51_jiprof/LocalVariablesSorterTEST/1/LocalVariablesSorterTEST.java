import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.Opcodes;
import static org.objectweb.asm.jip.Opcodes.*;

public class LocalVariablesSorterTest {
    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            int local;

            @Override
            public void visitVarInsn(int opcode, int var) {
                this.local = sorter.remap(var, Type.INT_TYPE);
            }
        });

        // Test int variable
        sorter.visitVarInsn(OPCODE_ILOAD, 1);
        assertEquals(2, sorter.local);

        // Test long variable
        sorter.visitVarInsn(OPCODE_LLOAD, 2);
        assertEquals(5, sorter.local);

        // Test float variable
        sorter.visitVarInsn(OPCODE_FLOAD, 3);
        assertEquals(7, sorter.local);

        // Test double variable
        sorter.visitVarInsn(OPCODE_DLOAD, 4);
        assertEquals(9, sorter.local);

        // Test object variable
        sorter.visitVarInsn(OPCODE_ALOAD, 5);
        assertEquals(11, sorter.local);
    }
}