package com.jiuzhe.process;

import com.alibaba.fastjson.JSON;
import com.google.auto.service.AutoService;
import com.jiuzhe.annotation.BaseInfo;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Objects;
import java.util.Set;

/**
 * @author wanqiuli
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.jiuzhe.annotation.BaseInfo")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BaseInfoProcess extends AbstractProcessor {

    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 获取所有带BaseInfo注解的方法
        Set<? extends Element> elementList = roundEnv.getElementsAnnotatedWith(BaseInfo.class);
        // 循环处理每个方法
        for (Element element : elementList) {
            // 当前方法的语法树
            JCTree tree = trees.getTree(element);
            // 对当前方法语法树进行解析处理
            tree.accept(new TreeTranslator() {
                @Override
                public void visitMethodDef(JCMethodDecl jcMethodDecl) {
                    List<JCVariableDecl> parameters = jcMethodDecl.params;
                    info(parameters.size());
                    // 1. example: BaseInfo baseInfo = BasicUtil.getUserInfo(request);
                    JCTree.JCExpression methodName = access("com.ql.test.util.BaseUtil.getUserInfo");
                    JCTree.JCExpression typeName = access("com.ql.test.baseinfo.HttpServletRequest");
                    JCTree.JCMethodInvocation callMethod = treeMaker.Apply(List.of(typeName), methodName, List.of(access(parameters.get(0))));
                    JCTree.JCExpressionStatement exec = treeMaker.Exec(callMethod);
                    JCVariableDecl var1 = treeMaker.VarDef(treeMaker.Modifiers(0), of("var1"), access("com.ql.test.baseinfo.UserInfo"), exec.getExpression());
                    info(var1.toString());
                    // 2. example: a.setTenantId(userInfo.getTenantId());
                    JCTree.JCExpression access = access("var1.getTenantId");
                    JCTree.JCMethodInvocation apply = treeMaker.Apply(List.nil(), access, List.nil());
                    JCTree.JCExpressionStatement exec1 = treeMaker.Exec(apply);
                    JCTree.JCExpression access1 = access(vName(parameters.get(1)) + "." + "setTenantId");
                    JCTree.JCMethodInvocation apply1 = treeMaker.Apply(List.of(typeName), access1, List.of(exec1.getExpression()));
                    JCTree.JCExpressionStatement exec2 = treeMaker.Exec(apply1);
                    // 3. example: a.setWarehouse()..
                    JCTree.JCExpression access2 = access("var1.getWarehouseNo");
                    JCTree.JCMethodInvocation apply2 = treeMaker.Apply(List.nil(), access2, List.nil());
                    JCTree.JCExpressionStatement exec3 = treeMaker.Exec(apply2);
                    JCTree.JCExpression access3 = access(vName(parameters.get(1)) + "." + "setWarehouseNo");
                    JCTree.JCMethodInvocation apply3 = treeMaker.Apply(List.of(typeName), access3, List.of(exec3.getExpression()));
                    JCTree.JCExpressionStatement exec4 = treeMaker.Exec(apply3);
                    // if (va1 == null) throw Exception
                    JCTree.JCExpression access4 = access("com.ql.test.baseinfo.UserInfo");
                    JCTree.JCMethodInvocation var11 = treeMaker.Apply(List.of(access4), access("java.util.Objects.isNull"), List.of(access("var1")));
                    JCTree.JCExpressionStatement exec5 = treeMaker.Exec(var11);
                    JCTree.JCNewClass jcNewClass = treeMaker.NewClass(
                            null,
                            List.nil(),
                            access("java.lang.Exception"),
                            List.nil(),
                            null
                    );
                    JCTree.JCThrow exception = treeMaker.Throw(jcNewClass);
                    JCTree.JCIf anIf = treeMaker.If(exec5.getExpression(), exception, null);
                    info(anIf.toString());
                    jcMethodDecl.body = treeMaker.Block(0, List.of(var1, anIf, exec2, exec4, jcMethodDecl.body));
                    super.visitMethodDef(jcMethodDecl);
                }
            });
        }
        return true;
    }

    public void info(Object var1) {
        messager.printMessage(Diagnostic.Kind.NOTE, "@:" + JSON.toJSONString(var1));
    }

    public Name of(String var1) {
        return names.fromString(var1);
    }

    public JCTree.JCExpression access(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(of(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, of(componentArray[i]));
        }
        return expr;
    }

    public JCIdent iDent(String var1) {
        return treeMaker.Ident(of(var1));
    }

    public JCTree.JCExpression access(JCVariableDecl jcVariableDecl) {
        String name = jcVariableDecl.toString();
        String[] names = name.split(" ");
        return access(names[1]);
    }

    public String vName(JCVariableDecl jcVariableDecl) {
        String name = jcVariableDecl.toString();
        String[] names = name.split(" ");
        return names[1];
    }
}
