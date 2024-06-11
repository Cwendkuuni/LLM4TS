import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        int[] mapping = new int[40];
        Object[] newLocals = new Object[20];
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitorAdapter(new ClassReader(LocalVariablesSorter.class.getBytes()))) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, 0); // Call the parent implementation
                int expected = 0;
                switch (opcode) {
                    case 22:
                    case 55: {
                        expected = 0;
                        break;
                    }
                    case 24:
                    case 57: {
                        expected = 1;
                        break;
                    }
                    case 23:
                    case 56: {
                        expected = 2;
                        break;
                    }
                    case 21:
                    case 54: {
                        expected = 3;
                        break;
                    }
                    default: {
                        expected = 4;
                        break;
                    }
                }
                assertEquals("Unexpected remapped index for opcode " + opcode, expected, sorter.remap(var, Type.getType("I")));
            }
        };

        for (int i = 0; i < 10; ++i) {
            sorter.visitVarInsn(i % 5 + 20, Type.getType("I"));
        }
    }
}