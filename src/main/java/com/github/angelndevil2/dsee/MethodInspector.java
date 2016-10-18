package com.github.angelndevil2.dsee;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 *
 * Created by k on 16. 10. 18.
 */
public class MethodInspector extends MethodVisitor {

    public MethodInspector() { this(Opcodes.ASM5); }

    private MethodInspector(int api) { super(api); }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) { super.visitMaxs(maxStack, maxLocals); }
}
