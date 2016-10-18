package com.github.angelndevil2.dsee;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

/**
 *
 * Created by k on 16. 10. 17.
 */
@Slf4j
public class ClassInspector extends ClassVisitor implements JSONAware {

    public class InspectedMethod implements JSONAware {

        public InspectedMethod() {}
        public InspectedMethod(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        @Getter @Setter
        private String name;
        @Getter @Setter
        private String desc;

        @Override
        public String toJSONString() {
            return toJSONObject().toJSONString();
        }

        @SuppressWarnings("unchecked")
        public JSONArray toJSONArray() {
            JSONArray ret = new JSONArray();
            ret.add(name);
            ret.add(desc);

            return ret;
        }

        @SuppressWarnings("unchecked")
        public JSONObject toJSONObject() {
            JSONObject ret = new JSONObject();
            ret.put("name", name);
            ret.put("desc", desc);

            return ret;
        }
    }

    public ClassInspector(byte[] classBytes) {
        this(Opcodes.ASM5);
        ClassReader classReader = new ClassReader(classBytes);
        classReader.accept(this, 0);
    }

    @SuppressWarnings("unchecked")
    public String toJSONString() {
        return toJSONObject().toJSONString();
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {

        JSONObject ret = new JSONObject();
        ret.put(name, methods);

        return ret;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        methods.add(new InspectedMethod(name, desc));

        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    private ClassInspector(int api) {
        super(api);
    }


    @Getter
    private String name;
    private ArrayList<InspectedMethod> methods = new ArrayList<InspectedMethod>();
}
