package com.jiuzhe.process;

import com.google.auto.service.AutoService;
import com.jiuzhe.annotation.FieldAnalysis;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static com.jiuzhe.util.ProcessUtils.*;
import static com.sun.tools.javac.tree.JCTree.JCStatement;

/**
 * @author wanqiuli
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.jiuzhe.annotation.FieldAnalysis")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FieldAnalysisProcess extends AbstractProcessor {

    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    private static List<JCVariableDecl> jcVariableDeclList = List.nil();


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
        // 获取所有添加该注解的
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(FieldAnalysis.class);
        for (Element element : elements) {
            JCTree tree = trees.getTree(element);
            tree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCClassDecl jcClassDecl) {
                    // 用来存储所有成员变量
                    for (JCTree jcTree : jcClassDecl.defs) {
                        // 判断是否是变量，如果是变量存储到list里
                        if (jcTree.getKind().equals(Tree.Kind.VARIABLE)) {
                            jcVariableDeclList = jcVariableDeclList.append((JCVariableDecl) jcTree);
                        }
                    }
                    JCExpression extending = jcClassDecl.getExtendsClause();
                    extending.getTree().accept(new TreeTranslator() {
                        @Override
                        public void visitClassDef(JCClassDecl jcClassDecl) {
                            info(messager, jcClassDecl.getSimpleName());
                            for (JCTree def : jcClassDecl.defs) {
                                if (def.getKind().equals(Tree.Kind.VARIABLE)) {
                                    jcVariableDeclList = jcVariableDeclList.append((JCVariableDecl) def);
                                }
                            }
                        }
                    });
                    info(messager, "数量:" + jcVariableDeclList.size());
                    // 生成新的方法语法树
                    JCMethodDecl jcMethodDecl = generateMethodCode(jcVariableDeclList);
                    // 将语法树加入到当前语法树中
                    jcClassDecl.defs = jcClassDecl.defs.prepend(jcMethodDecl);
                    super.visitClassDef(jcClassDecl);
                }
            });
        }
        return true;
    }

    public JCMethodDecl generateMethodCode(List<JCVariableDecl> jcVariableDeclList) {
        return treeMaker.MethodDef(
                // public
                treeMaker.Modifiers(Flags.PUBLIC),
                of(names, "getFields"),
                access(treeMaker, names, "java.util.List"),
                List.nil(),
                List.nil(),
                List.nil(),
                body(jcVariableDeclList),
                null);
    }

    private JCBlock body(List<JCVariableDecl> jcVariableDeclList) {
        ListBuffer<JCStatement> statements = new ListBuffer<>();
        JCVariableDecl fieldListExtend = treeMaker.VarDef(treeMaker.Modifiers(0), of(names, "fieldList"), access(treeMaker, names, "java.util.List"),
                treeMaker.Exec(
                        treeMaker.Apply(List.nil(),
                                access(treeMaker, names, "super.getFields"), List.nil())
                ).getExpression());
        // 判断
        JCIf jcIf = treeMaker.If()
        JCNewClass arrayListClass = treeMaker.NewClass(null, List.nil(),
                access(treeMaker, names, "java.util.ArrayList"),
                List.nil(), null);
        JCVariableDecl fieldList = treeMaker.VarDef(treeMaker.Modifiers(0), of(names, "fieldList"), access(treeMaker, names, "java.util.List"), arrayListClass);
        // List fieldList = new ArrayList();
        statements.append(fieldList);
        for (JCVariableDecl jcVariableDecl : jcVariableDeclList) {
            // fieldList.add(fieldName);
            statements.append(
                    treeMaker.Exec(
                            treeMaker.Apply(
                                    List.of(access(treeMaker, names, "java.lang.String")),
                                    access(treeMaker, names, "fieldList.add"),
                                    List.of(treeMaker.Literal(jcVariableDecl.getName().toString()))
                            )
                    )
            );
        }
        statements.append(treeMaker.Return(treeMaker.Ident(of(names, "fieldList"))));
        return treeMaker.Block(0, statements.toList());
    }

}
