import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Type.*;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        int[] mapping = new int[40];
        Object[] newLocals = new Object[20];
        LocalVariablesSorter lvs = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, mapping[var]);
            }
        });

        lvs.visitVarInsn(Opcodes.ILOAD, 0);
        int expected = 0;
        int actual = lvs.mapping[0];
        assertEquals(expected, actual);

        // Test negative index
        lvs = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, mapping[var]);
            }
        });
        lvs.visitVarInsn(-1, 0);
        int errorIndex = -1;
        try {
            int actualIndex = lvs.mapping[errorIndex];
            fail("Expected IllegalArgumentException when index is negative.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null as input for Type.getType(desc)
        LocalVariablesSorter lvsNull = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                super.visitLocalVariable(name, desc, null, mapping[index], mapping[index + 1], index);
            }
        });
        String expectedNull = null;
        String actualNull = lvsNull.mapping[0];
        assertEquals(expectedNull, actualNull);
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitIincInsn(int var, int increment) {
                super.visitIincInsn(mapping[var], increment);
            }
        });

        lvs.visitIincInsn(0, 1);
        int expected = 1;
        int actual = lvs.mapping[0] - lvs.firstLocal;
        assertEquals(expected, actual);

        // Test negative index
        LocalVariablesSorter lvsNegative = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitIincInsn(int var, int increment) {
                super.visitIincInsn(mapping[var], increment);
            }
        });
        lvsNegative.visitIincInsn(-1, 0);
        int errorIndexNegative = -1;
        try {
            lvsNegative.mapping[errorIndexNegative];
            fail("Expected IllegalArgumentException when index is negative.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null as input for Type.getArgumentTypes(desc)
        LocalVariablesSorter lvsNullInput = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitIincInsn(int var, int increment) {
                super.visitIincInsn(mapping[var], increment);
            }
        });
        LocalVariablesSorter.Type[] emptyArgs = null;
        try {
            int expectedSize = emptyArgs.length;
            int actualSize = lvsNullInput.nextLocal - lvsNullInput.firstLocal;
            assertEquals(expectedSize, actualSize);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testVisitLocalVariable() {
        int[] mapping = new int[40];
        Object[] newLocals = new Object[20];
        LocalVariablesSorter lvs = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                super.visitLocalVariable(name, desc, signature, mapping[index], mapping[index + 1], index);
            }
        });

        lvs.visitLocalVariable("test", "I", null, new Label(), new Label(), 0);
        String expected = "test";
        String actual = lvs.mapping[0];
        assertEquals(expected, actual);

        // Test negative index
        LocalVariablesSorter lvsNegative = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                super.visitLocalVariable(name, desc, signature, mapping[index], mapping[index + 1], index);
            }
        });
        lvsNegative.visitLocalVariable("test", "I", null, new Label(), new Label(), -1);
        String errorName = "test";
        try {
            String actualName = lvsNegative.mapping[0];
            fail("Expected IllegalArgumentException when index is negative.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null as input for Type.getType(desc)
        LocalVariablesSorter lvsNullDesc = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                super.visitLocalVariable(name, null, signature, mapping[index], mapping[index + 1], index);
            }
        });
        String expectedNullDesc = null;
        String actualNullDesc = lvsNullDesc.mapping[0];
        assertEquals(expectedNullDesc, actualNullDesc);
    }

    @Test
    public void testEmptyConstructor() {
        try {
            new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())));
            fail("Expected IllegalStateException when descriptor is empty.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidOpcode() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "(Ljava/lang/String;)V", new MethodVisitor(1, new ClassReader(LocalVariablesSorter.class.getBytes())) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(99, mapping[var]);
            }
        });

        int errorOpcode = 99;
        try {
            lvs.visitVarInsn(errorOpcode, 0);
            fail("Expected IllegalArgumentException when opcode is invalid.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}