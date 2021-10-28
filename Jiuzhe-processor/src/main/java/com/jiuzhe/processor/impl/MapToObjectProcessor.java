package com.jiuzhe.processor.impl;

import com.jiuzhe.processor.annotation.MapToObject;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author wautumnli
 */
@SupportedAnnotationTypes("com.jiuzhe.processor.annotation.MapToObject")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MapToObjectProcessor extends AbstractProcessor {

    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        // 初始化注入
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 获取当前
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(MapToObject.class);
        set.forEach(element -> {
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
                    for (JCTree tree : jcClassDecl.defs) {
                        if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                            JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                            jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                        }
                    }
                    JCTree.JCMethodDecl jcMethodDecl = makeGetterMethodDecl(jcVariableDeclList);
                    jcClassDecl.defs = jcClassDecl.defs.prepend(jcMethodDecl);
                    super.visitClassDef(jcClassDecl);
                }

            });
        });
        return true;
    }

    private JCTree.JCMethodDecl makeGetterMethodDecl(List<JCTree.JCVariableDecl> jcVariableDeclList) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        JCTree.JCNewClass jcNewClass = treeMaker.NewClass(
                null,
                List.nil(),
                memberAccess("java.util.HashMap"),
                List.nil(),
                null);
        statements.append(makeVarDef(treeMaker.Modifiers(0), "myResultMap", memberAccess("java.util.Map"), jcNewClass));
        for (JCTree.JCVariableDecl jcVariableDecl : jcVariableDeclList) {
            statements.append(getStat(jcVariableDecl));
        }
        statements.append(treeMaker.Return(treeMaker.Ident(getNameFromString("myResultMap"))));
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC),
                getNameFromString("objectToMap"),
                memberAccess("java.util.Map"),
                List.nil(),
                List.nil(),
                List.nil(),
                body,
                null);
    }

    private JCTree.JCExpressionStatement getStat(JCTree.JCVariableDecl jvd) {
        return treeMaker.Exec(
                treeMaker.Apply(
                        List.of(memberAccess("java.lang.String"), memberAccess("java.lang.Object")),
                        memberAccess("myResultMap.put"),
                        List.of(treeMaker.Literal(jvd.getName().toString()), treeMaker.Select(treeMaker.Ident(getNameFromString("this")), jvd.getName()))
                )
        );
    }

    private JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    private JCTree.JCVariableDecl makeVarDef(JCTree.JCModifiers modifiers, String name, JCTree.JCExpression vartype, JCTree.JCExpression init) {
        return treeMaker.VarDef(
                modifiers,
                // 名字
                getNameFromString(name),
                // 类型
                vartype,
                // 初始化语句
                init
        );
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }
}