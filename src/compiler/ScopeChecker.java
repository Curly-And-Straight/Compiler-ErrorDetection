package compiler;

import gen.CoolListener;
import gen.CoolParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ScopeChecker implements CoolListener {

    static ArrayList<Node> nodes = new ArrayList();
    Stack<Node> parents = new Stack<>();
    static int error_number = 0;
    ArrayList<List> new_objects = new ArrayList<>();
    ArrayList<List> method_called = new ArrayList<>();
    boolean skipClass = false;
    boolean skipMethod = false;
    boolean skipField = false;

    public Node duplicateError(String typeID, String name, int line , int column){
        if(parents.peek().lookup(name) != null){
            Scopes scope = parents.peek().lookup(name);
            if(scope.id.equals(typeID)){
                if(scope.id == "class"){
                    skipClass = true;
                }
                else if(scope.id == "method"){
                    skipMethod = true;
                }
                else if(scope.id == "var" || scope.id == "let"){
                    skipField = true;
                }

                error_number ++;
                System.out.println("Error" + error_number + " : in line [" + line + ":" + column + "], " + scope.id + " [" + scope.name + "] has been defined already");
                Node cl = new Node(parents.peek(),name + "_" + line + "_" + column);
                 return cl;
            }
        }
        return new Node(parents.peek(),name);
    }

    public void notExistError(List<List> list,String scope){
        switch (scope){
            case "class":
                for (int i = 0; i < list.size(); i++) {
                    if(!nodes.get(0).symbolTable.containsKey(list.get(i).get(0))){
                        error_number++;
                        System.out.println("Error" + error_number + " : " + "in line [" + list.get(i).get(1) + ":" + list.get(i).get(2) + "], cannot find class [" + list.get(i).get(0) + "]");
                    }
                }
                break;

            case "var":
                for (int i = 0; i < list.size(); i++) {
                    if(!parents.peek().symbolTable.containsKey(list.get(i).get(0))){
                        error_number++;
                        System.out.println("Error" + error_number + " : " + "in line [" + list.get(i).get(1) + ":" + list.get(i).get(2) + "], cannot find variable [" + list.get(i).get(0) + "]");
                    }
                }
                break;


        }

    }

    public ClassObj checkInheritance(CoolParser.ClassDefContext ctx){
        ClassObj obj;
        if(ctx.TYPE(1) == null){
            obj = new ClassObj("class",ctx.TYPE(0).getText());

        }
        else{
            obj = new ClassObj("class",ctx.TYPE(0).getText(),ctx.TYPE(1).getText());
        }

        return obj;
    }


    @Override
    public void enterStart(CoolParser.StartContext ctx) {
        Node global = new Node("global");
        nodes.add(global);
        parents.push(global);
    }

    @Override
    public void exitStart(CoolParser.StartContext ctx) {
        notExistError(new_objects,"class");
//        notExistError(method_called,"method");
    }

    @Override
    public void enterClassDef(CoolParser.ClassDefContext ctx) {
            int line = ctx.getStart().getLine();
            int column =  ctx.getStart().getCharPositionInLine();
            Node cl = duplicateError("class",ctx.TYPE(0).getText(),line,column);
            parents.peek().insert(cl.name,checkInheritance(ctx));
            parents.push(cl);
            if(!skipClass){
                nodes.add(cl);
            }
    }

    @Override
    public void exitClassDef(CoolParser.ClassDefContext ctx) {
            parents.pop();
            skipClass = false;

    }

    @Override
    public void enterFunction(CoolParser.FunctionContext ctx) {
        if(!skipClass){
            int line = ctx.getStart().getLine();
            int column =  ctx.getStart().getCharPositionInLine();
            Node method = duplicateError("method",ctx.ID().getText(),line,column);
            parents.peek().insert(method.name,new MethodObj("method",ctx.ID().getText(),ctx.TYPE().getText(),ctx.parameter()));
            parents.push(method);
            if(!skipMethod){
                nodes.add(method);
            }
        }
    }

    @Override
    public void exitFunction(CoolParser.FunctionContext ctx) {
        parents.pop();
        skipMethod = false;
    }

    @Override
    public void enterVarDef(CoolParser.VarDefContext ctx) {
        if((!skipClass) || (!skipMethod)){
            int line = ctx.getStart().getLine();
            int column =  ctx.getStart().getCharPositionInLine();
            Node field = duplicateError("var",ctx.ID().getText(),line,column);
            if (!skipField){
                nodes.add(field);
            }

            parents.peek().insert(field.name,new FieldObj("var",ctx.ID().getText(),ctx.TYPE().getText()));
        }
    }

    @Override
    public void exitVarDef(CoolParser.VarDefContext ctx) {
        skipField = false;
    }

    @Override
    public void enterParam(CoolParser.ParamContext ctx) {

    }

    @Override
    public void exitParam(CoolParser.ParamContext ctx) {

    }

    @Override
    public void enterSub(CoolParser.SubContext ctx) {
        List list1 = new ArrayList<>();
        List list2 = new ArrayList<>();

        List<List> list3 = new ArrayList<>();

        list1.add(ctx.expr().get(0).getText());
        list1.add(ctx.expr().get(0).getStart().getLine());
        list1.add(ctx.expr().get(0).getStart().getCharPositionInLine());

        list2.add(ctx.expr().get(1).getText());
        list2.add(ctx.expr().get(1).getStart().getLine());
        list2.add(ctx.expr().get(1).getStart().getCharPositionInLine());

        list3.add(list1);
        list3.add(list2);

        notExistError(list3, "var");
    }

    @Override
    public void exitSub(CoolParser.SubContext ctx) {

    }

    @Override
    public void enterString(CoolParser.StringContext ctx) {

    }

    @Override
    public void exitString(CoolParser.StringContext ctx) {

    }

    @Override
    public void enterMul(CoolParser.MulContext ctx) {

        List list1 = new ArrayList<>();
        List list2 = new ArrayList<>();

        List<List> list3 = new ArrayList<>();

        list1.add(ctx.expr().get(0).getText());
        list1.add(ctx.expr().get(0).getStart().getLine());
        list1.add(ctx.expr().get(0).getStart().getCharPositionInLine());

        list2.add(ctx.expr().get(1).getText());
        list2.add(ctx.expr().get(1).getStart().getLine());
        list2.add(ctx.expr().get(1).getStart().getCharPositionInLine());

        list3.add(list1);
        list3.add(list2);

        notExistError(list3, "var");
    }

    @Override
    public void exitMul(CoolParser.MulContext ctx) {

    }

    @Override
    public void enterLteq(CoolParser.LteqContext ctx) {

        List list1 = new ArrayList<>();
        List list2 = new ArrayList<>();

        List<List> list3 = new ArrayList<>();

        list1.add(ctx.expr().get(0).getText());
        list1.add(ctx.expr().get(0).getStart().getLine());
        list1.add(ctx.expr().get(0).getStart().getCharPositionInLine());

        list2.add(ctx.expr().get(1).getText());
        list2.add(ctx.expr().get(1).getStart().getLine());
        list2.add(ctx.expr().get(1).getStart().getCharPositionInLine());

        list3.add(list1);
        list3.add(list2);

        notExistError(list3, "var");
    }

    @Override
    public void exitLteq(CoolParser.LteqContext ctx) {

    }

    @Override
    public void enterNum(CoolParser.NumContext ctx) {

    }

    @Override
    public void exitNum(CoolParser.NumContext ctx) {

    }

    @Override
    public void enterStaticCall(CoolParser.StaticCallContext ctx) {
        System.out.println("static call : " + ctx.getText());
    }

    @Override
    public void exitStaticCall(CoolParser.StaticCallContext ctx) {

    }

    @Override
    public void enterLt(CoolParser.LtContext ctx) {

        List list1 = new ArrayList<>();
        List list2 = new ArrayList<>();

        List<List> list3 = new ArrayList<>();

        list1.add(ctx.expr().get(0).getText());
        list1.add(ctx.expr().get(0).getStart().getLine());
        list1.add(ctx.expr().get(0).getStart().getCharPositionInLine());

        list2.add(ctx.expr().get(1).getText());
        list2.add(ctx.expr().get(1).getStart().getLine());
        list2.add(ctx.expr().get(1).getStart().getCharPositionInLine());

        list3.add(list1);
        list3.add(list2);

        notExistError(list3, "var");
    }

    @Override
    public void exitLt(CoolParser.LtContext ctx) {

    }

    @Override
    public void enterWhile(CoolParser.WhileContext ctx) {
        Node while_scope = new Node(parents.peek(),"while");
        nodes.add(while_scope);
        parents.peek().insert(while_scope.name,new NestedObj("while"));
        parents.push(while_scope);
    }

    @Override
    public void exitWhile(CoolParser.WhileContext ctx) {
        parents.pop();
    }

    @Override
    public void enterSwitch(CoolParser.SwitchContext ctx) {
        Node switch_scope = new Node(parents.peek(),"switch");
        nodes.add(switch_scope);
        parents.peek().insert(switch_scope.name,new NestedObj("switch"));
        parents.push(switch_scope);
    }

    @Override
    public void exitSwitch(CoolParser.SwitchContext ctx) {
        parents.pop();
    }

    @Override
    public void enterDiv(CoolParser.DivContext ctx) {

        List list1 = new ArrayList<>();
        List list2 = new ArrayList<>();

        List<List> list3 = new ArrayList<>();

        list1.add(ctx.expr().get(0).getText());
        list1.add(ctx.expr().get(0).getStart().getLine());
        list1.add(ctx.expr().get(0).getStart().getCharPositionInLine());

        list2.add(ctx.expr().get(1).getText());
        list2.add(ctx.expr().get(1).getStart().getLine());
        list2.add(ctx.expr().get(1).getStart().getCharPositionInLine());

        list3.add(list1);
        list3.add(list2);

        notExistError(list3, "var");
    }

    @Override
    public void exitDiv(CoolParser.DivContext ctx) {

    }

    @Override
    public void enterNot(CoolParser.NotContext ctx) {

        //TODO
    }

    @Override
    public void exitNot(CoolParser.NotContext ctx) {

    }

    @Override
    public void enterNewObject(CoolParser.NewObjectContext ctx) {
        List name = new ArrayList();
        name.add(ctx.TYPE().getText());
        name.add(ctx.getStart().getLine());
        name.add(ctx.getStart().getCharPositionInLine());
        new_objects.add(name);
    }

    @Override
    public void exitNewObject(CoolParser.NewObjectContext ctx) {

    }

    @Override
    public void enterBlock(CoolParser.BlockContext ctx) {
        Node block_scope = new Node(parents.peek(),"block");
        nodes.add(block_scope);
        parents.peek().insert(block_scope.name,new NestedObj("block"));
        parents.push(block_scope);
    }

    @Override
    public void exitBlock(CoolParser.BlockContext ctx) {
        parents.pop();
    }

    @Override
    public void enterLet(CoolParser.LetContext ctx) {
        int line = ctx.getStart().getLine();
        int column = ctx.getStart().getCharPositionInLine();
        Node field = duplicateError("let",ctx.ID(0).getText(),line,column);
        if (!skipField){
            nodes.add(field);
        }
        parents.peek().insert(field.name,new FieldObj("let",ctx.ID(0).getText(),ctx.TYPE(0).getText()));
    }

    @Override
    public void exitLet(CoolParser.LetContext ctx) {
        skipField = false;
    }

    @Override
    public void enterId(CoolParser.IdContext ctx) {

    }

    @Override
    public void exitId(CoolParser.IdContext ctx) {

    }

    @Override
    public void enterIf(CoolParser.IfContext ctx) {
        Node if_scope = new Node(parents.peek(),"if");
        nodes.add(if_scope);
        parents.peek().insert(if_scope.name,new NestedObj("if"));
        parents.push(if_scope);
    }

    @Override
    public void exitIf(CoolParser.IfContext ctx) {
        parents.pop();
    }

    @Override
    public void enterAdd(CoolParser.AddContext ctx) {

        List list1 = new ArrayList<>();
        List list2 = new ArrayList<>();

        List<List> list3 = new ArrayList<>();

        list1.add(ctx.expr().get(0).getText());
        list1.add(ctx.expr().get(0).getStart().getLine());
        list1.add(ctx.expr().get(0).getStart().getCharPositionInLine());

        list2.add(ctx.expr().get(1).getText());
        list2.add(ctx.expr().get(1).getStart().getLine());
        list2.add(ctx.expr().get(1).getStart().getCharPositionInLine());

        list3.add(list1);
        list3.add(list2);

        notExistError(list3, "var");
    }

    @Override
    public void exitAdd(CoolParser.AddContext ctx) {

    }

    @Override
    public void enterVoid(CoolParser.VoidContext ctx) {

    }

    @Override
    public void exitVoid(CoolParser.VoidContext ctx) {

    }

    @Override
    public void enterInvert(CoolParser.InvertContext ctx) {

    }

    @Override
    public void exitInvert(CoolParser.InvertContext ctx) {

    }

    @Override
    public void enterFactExpr(CoolParser.FactExprContext ctx) {

    }

    @Override
    public void exitFactExpr(CoolParser.FactExprContext ctx) {

    }

    @Override
    public void enterFalse(CoolParser.FalseContext ctx) {

    }

    @Override
    public void exitFalse(CoolParser.FalseContext ctx) {

    }

    @Override
    public void enterEqual(CoolParser.EqualContext ctx) {

    }

    @Override
    public void exitEqual(CoolParser.EqualContext ctx) {

        List list1 = new ArrayList<>();
        List list2 = new ArrayList<>();

        List<List> list3 = new ArrayList<>();

        list1.add(ctx.expr().get(0).getText());
        list1.add(ctx.expr().get(0).getStart().getLine());
        list1.add(ctx.expr().get(0).getStart().getCharPositionInLine());

        list2.add(ctx.expr().get(1).getText());
        list2.add(ctx.expr().get(1).getStart().getLine());
        list2.add(ctx.expr().get(1).getStart().getCharPositionInLine());

        list3.add(list1);
        list3.add(list2);

        notExistError(list3, "var");
    }

    @Override
    public void enterObjectCall(CoolParser.ObjectCallContext ctx) {

    }

    @Override
    public void exitObjectCall(CoolParser.ObjectCallContext ctx) {

    }

    @Override
    public void enterTrue(CoolParser.TrueContext ctx) {

    }

    @Override
    public void exitTrue(CoolParser.TrueContext ctx) {

    }

    @Override
    public void enterAssign(CoolParser.AssignContext ctx) {

        List list1 = new ArrayList<>();
        List list2 = new ArrayList<>();

        List<List> list3 = new ArrayList<>();

        list1.add(ctx.expr().getText());
        list1.add(ctx.expr().getStart().getLine());
        list1.add(ctx.expr().getStart().getCharPositionInLine());

        list2.add(ctx.ID().getText());
        list2.add(ctx.expr().getStart().getLine());
        list2.add(ctx.expr().getStart().getCharPositionInLine());

        list3.add(list1);
        list3.add(list2);

        notExistError(list3, "var");
    }

    @Override
    public void exitAssign(CoolParser.AssignContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
