package org.objectweb.asm.jip.commons;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Type;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    private ClassReader reader;
    private MethodVisitor mv;
    private LocalVariablesSorter sorter;

    @Before
    public void setUp() {
        String className = "TestClass";
        String methodName = "<init>";
        String methodDesc = "()V";
        reader = new ClassReader(getClass().getResourceAsStream("/" + className + ".class"));
        mv = new MethodVisitor(0, reader, access -> new LocalVariablesSorter(access, methodName, methodDesc, mv));
        sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, methodName, methodDesc, mv);
    }

    @After
    public void tearDown() {
        sorter = null;
        mv = null;
        reader.setReader(null);
    }

    @Test
    public void testVisitVarInsn() {
        int[] expectedMappings = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        for (int i = 0; i < expectedMappings.length; i++) {
            Type type;
            int opcode;
            int var;
            switch (i) {
                case 0:
                    opcode = Opcodes.ALOAD_0;
                    type = Type.getType("Ljava/lang/Object;");
                    break;
                case 1:
                    opcode = Opcodes.ILOAD;
                    type = Type.INT_TYPE;
                    var = 0;
                    break;
                case 2:
                    opcode = Opcodes.ILOAD;
                    type = Type.INT_TYPE;
                    var = 1;
                    break;
                case 3:
                    opcode = Opcodes.ILOAD;
                    type = Type.INT_TYPE;
                    var = 2;
                    break;
                case 4:
                    opcode = Opcodes.LLOAD;
                    type = Type.LONG_TYPE;
                    var = 3;
                    break;
                case 5:
                    opcode = Opcodes.LLOAD;
                    type = Type.LONG_TYPE;
                    var = 4;
                    break;
                case 6:
                    opcode = Opcodes.FLOAD;
                    type = Type.FLOAT_TYPE;
                    var = 5;
                    break;
                case 7:
                    opcode = Opcodes.FLOAD;
                    type = Type.FLOAT_TYPE;
                    var = 6;
                    break;
                case 8:
                    opcode = Opcodes.DLOAD;
                    type = Type.DOUBLE_TYPE;
                    var = 7;
                    break;
                case 9:
                    opcode = Opcodes.DLOAD;
                    type = Type.DOUBLE_TYPE;
                    var = 8;
                    break;
                case 10:
                    opcode = Opcodes.ALOAD;
                    type = Type.getType("Ljava/lang/String;");
                    var = 9;
                    break;
                case 11:
                    opcode = Opcodes.ALOAD;
                    type = Type.getType("Ljava/lang/String;");
                    var = 10;
                    break;
                case 12:
                    opcode = Opcodes.ILOAD;
                    type = Type.INT_TYPE;
                    var = 11;
                    break;
                case 13:
                    opcode = Opcodes.ILOAD;
                    type = Type.INT_TYPE;
                    var = 12;
                    break;
                case 14:
                    opcode = Opcodes.LLOAD;
                    type = Type.LONG_TYPE;
                    var = 13;
                    break;
                case 15:
                    opcode = Opcodes.LLOAD;
                    type = Type.LONG_TYPE;
                    var = 14;
                    break;
                case 16:
                    opcode = Opcodes.FLOAD;
                    type = Type.FLOAT_TYPE;
                    var = 15;
                    break;
                case 17:
                    opcode = Opcodes.FLOAD;
                    type = Type.FLOAT_TYPE;
                    var = 16;
                    break;
                case 18:
                    opcode = Opcodes.DLOAD;
                    type = Type.DOUBLE_TYPE;
                    var = 17;
                    break;
                case 19:
                    opcode = Opcodes.DLOAD;
                    type = Type.DOUBLE_TYPE;
                    var = 18;
                    break;
                case 20:
                    opcode = Opcodes.ALOAD;
                    type = Type.getType("Ljava/lang/String;");
                    var = 19;
                    break;
                default:
                    opcode = -1;
            }
            sorter.visitVarInsn(opcode, var);
            assertEquals(expectedMappings[i], sorter.mv.stackMapTable[0].localVariables[i].index);
        }
    }

    @Test
    public void testVisitIincInsn() {
        int[] expectedMappings = {1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        for (int i = 0; i < expectedMappings.length; i++) {
            int index = i;
            int increment = 1;
            if (i == 10) {
                increment = 2;
                index = 11;
            }
            sorter.visitIincInsn(index, increment);
            assertEquals(expectedMappings[i], sorter.mv.stackMapTable[0].localVariables[i].index);
        }
    }

    @Test
    public void testVisitMaxs() {
        int maxLocals = 21;
        int maxStack = 5;
        sorter.visitMaxs(maxStack, maxLocals);
        assertEquals(maxLocals, sorter.mv.stackMapTable[0].maxLocals);
        assertEquals(maxStack, sorter.mv.stackMapTable[0].maxStack);
    }

    @Test
    public void testVisitLocalVariable() {
        String[] names = {"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "str1", "str2"};
        String[] signatures = {"I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "Ljava/lang/String;", "Ljava/lang/String;"};
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String signature = signatures[i];
            Label start = new Label();
            Label end = new Label();
            int index = i;
            sorter.visitLocalVariable(name, signature, null, start, end, index);
            assertEquals(index, sorter.mv.stackMapTable[0].localVariables[i].index);
            assertEquals(name, sorter.mv.stackMapTable[0].localVariables[i].name);
            assertEquals(signature, sorter.mv.stackMapTable[0].localVariables[i].signature);
            assertEquals(start, sorter.mv.stackMapTable[0].localVariables[i].start);
            assertEquals(end, sorter.mv.stackMapTable[0].localVariables[i].end);
        }
    }

    @Test
    public void testVisitFrame() {
        Object[] local = new Object[22];
        for (int i = 0; i < 22; i++) {
            if (i < 11) {
                local[i] = Opcodes.INTEGER;
            } else {
                local[i] = Opcodes.TOP;
            }
        }
        int maxLocals = 21;
        int maxStack = 5;
        sorter.visitFrame(Opcodes.FRAME_TYPE_FULL, maxLocals, local, maxStack, null);
        assertEquals(maxLocals, sorter.mv.stackMapTable[0].localVariables.length);
        for (int i = 0; i < maxLocals; i++) {
            assertEquals(i, sorter.mv.stackMapTable[0].localVariables[i].index);
            assertEquals(local[i] == Opcodes.TOP ? Opcodes.TOP : local[i], sorter.newLocals[i]);
        }
        assertEquals(maxStack, sorter.mv.stackMapTable[0].stackSize);
        assertEquals(maxLocals, sorter.mv.stackMapTable[0].localSize);
    }

    @Test
    public void testNewLocal() {
        Type intType = Type.INT_TYPE;
        Type floatType = Type.FLOAT_TYPE;
        Type longType = Type.LONG_TYPE;
        Type doubleType = Type.DOUBLE_TYPE;
        Type stringType = Type.getType("Ljava/lang/String;");
        for (Type type : new Type[]{intType, floatType, longType, doubleType, stringType}) {
            int local = sorter.newLocal(type);
            assertEquals(sorter.nextLocal - type.getSize(), local);
            assertEquals(type, sorter.mv.stackMapTable[0].localVariables[local - 1].type);
        }
    }
}