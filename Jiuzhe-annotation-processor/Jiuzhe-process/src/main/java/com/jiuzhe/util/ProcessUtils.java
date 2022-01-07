package com.jiuzhe.util;

import com.alibaba.fastjson.JSON;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.text.MessageFormat;

/**
 * @author wanqiuli
 */
public class ProcessUtils {

    public static void error(Messager messager, Object var1) {
        messager.printMessage(Diagnostic.Kind.ERROR, MessageFormat.format("【ERROR: {0}】", JSON.toJSONString(var1)));
    }

    public static void info(Messager messager, Object var1) {
        messager.printMessage(Diagnostic.Kind.NOTE, MessageFormat.format("【INFO: {0}】", JSON.toJSONString(var1)));
    }

    public static Name of(Names names, String var1) {
        return names.fromString(var1);
    }

    public static JCExpression access(TreeMaker treeMaker, Names names, String components) {
        String[] componentArray = components.split("\\.");
        JCExpression expr = treeMaker.Ident(of(names, componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, of(names, componentArray[i]));
        }
        return expr;
    }
}
