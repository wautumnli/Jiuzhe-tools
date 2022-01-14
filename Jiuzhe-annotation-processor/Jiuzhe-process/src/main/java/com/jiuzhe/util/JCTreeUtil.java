package com.jiuzhe.util;


import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

/**
 * JCTree工具
 *
 * @author wanqiuli
 */
public class JCTreeUtil {


    /**
     * 用于创建一系列的语法树节点
     */
    private TreeMaker treeMaker;
    /**
     * 节点名
     */
    private Names names;

    public JCTreeUtil(TreeMaker treeMaker, Names names) {
        this.treeMaker = treeMaker;
        this.names = names;
    }


    /**
     * 作用域方法域访问命名
     * <p>语法树中 example: java.lang.Object,需要通过'.'去访问包下类或类下方法
     * <p>需要通过{@link com.sun.tools.javac.tree.TreeMaker#Select(JCTree.JCExpression, Name)}去连接
     *
     * @param var1 参数 example: java.lang.Object
     * @return 作用域方法域访问命名语法
     */
    public JCTree.JCExpression visit(String var1) {
        // 以'.'分割所有语法节点
        String[] componentArray = var1.split("\\.");
        // 第一关节点需要通过Ident定义出来 example: java
        JCTree.JCExpression expr = treeMaker.Ident(nodeNaming(componentArray[0]));
        // 后面的节点需要通过Select连接 example: java.lang.Object
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, nodeNaming(componentArray[i]));
        }
        return expr;
    }

    /**
     * 节点命名
     * <p>在语法生成树中,每个名字每个操作符都是一个节点,需要通过names转换成语法生成树中的节点
     *
     * @param var1 参数
     * @return 语法树中的节点
     */
    public Name nodeNaming(String var1) {
        return names.fromString(var1);
    }

    /**
     * 方法调用语法
     *
     * @param method 调用方法名,全限定名
     * @param params 参数列表 {@link JCTree.JCExpression}
     * @return 方法调用语法
     */
    public JCTree.JCExpression execMethod(String method, List<JCTree.JCExpression> params) {
        return treeMaker.Exec(
                treeMaker.Apply(
                        // 参数类型列表,可不填
                        List.nil(),
                        // 调用方法名
                        visit(method),
                        // 参数列表
                        params
                )
        ).getExpression();
    }

    /**
     * 方法调用语法
     *
     * @param method 调用方法名,全限定名
     * @param params 参数列表{@link JCTree.JCExpression}
     * @return 方法调用语法 返回{@link JCTree.JCExpressionStatement}
     */
    public JCTree.JCExpressionStatement execMethodToStat(String method, List<JCTree.JCExpression> params) {
        return treeMaker.Exec(
                treeMaker.Apply(
                        // 参数类型列表,可不填
                        List.nil(),
                        // 调用方法名
                        visit(method),
                        // 参数列表
                        params
                )
        );
    }

    /**
     * 方法调用语法
     *
     * @param method 调用方法名,全限定名
     * @param param  参数 {@link JCTree.JCVariableDecl}
     * @return 方法调用语法
     */
    public JCTree.JCExpression execMethod(String method, JCTree.JCVariableDecl param) {
        return treeMaker.Exec(
                treeMaker.Apply(
                        // 参数类型列表,可不填
                        List.nil(),
                        // 调用方法名
                        visit(method),
                        // 参数列表
                        List.of(getParamNode(param))
                )
        ).getExpression();
    }

    /**
     * 将方法参数转为语法树节点
     *
     * @param var1 参数
     * @return 语法树节点
     */
    public JCTree.JCExpression getParamNode(JCTree.JCVariableDecl var1) {
        return visit(getParamName(var1));
    }

    /**
     * 获取参数名称
     *
     * @param var1 参数
     * @return 参数名称
     */
    public String getParamName(JCTree.JCVariableDecl var1) {
        String param = var1.toString();
        String[] names = param.split(" ");
        return names[1];
    }

    /**
     * 定义变量
     *
     * @param var1 变量名称
     * @param var2 变量类型 全限定名
     * @param var3 参数初始值表达式
     * @return 定义参数语法
     */
    public JCTree.JCVariableDecl defVar(String var1, String var2, JCTree.JCExpression var3) {
        return treeMaker.VarDef(
                // 作用域 0为默认,局部变量
                treeMaker.Modifiers(0),
                // 变量名
                nodeNaming(var1),
                // 变量类的全限定名
                visit(var2),
                // 获取变量的表达式
                var3
        );
    }

    /**
     * new Class()语法
     *
     * @param var1 类名 全限定名
     * @param var2 如果有参，需要传初始化参数 {@link String}
     * @return new Class()语法
     */
    public JCTree.JCNewClass newClass(String var1, String var2) {
        return treeMaker.NewClass(
                null,
                List.nil(),
                visit(var1),
                List.of(treeMaker.Literal(var2)),
                null
        );
    }

    /**
     * 抛异常语法
     *
     * @param var1 异常类名 全限定名
     * @param var2 异常信息 {@link String}
     * @return 抛出异常语法
     */
    public JCTree.JCThrow throwEx(String var1, String var2) {
        return treeMaker.Throw(
                newClass(var1, var2)
        );
    }
}