package compiler;

import gen.CoolListener;
import gen.CoolParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class ScopeChecker implements CoolListener {
    static ArrayList<Node> nodes = new ArrayList();
    Stack<Node> parents = new Stack<>();
    static int error_number = 1;
    public void detectError(String typeID,String name,int line , int column){
        if(parents.peek().lookup(name) != null){
            Scopes scope = parents.peek().lookup(name);
            System.out.println("****************************************************");
            System.out.println("obji.id = " + scope.id + " , typeID = " + typeID);
            if(scope.id.equals(typeID)){
                System.out.println("Error" + error_number + " : in line [" + line + ":" + column + "], " + scope.id + " [" + scope.name + "] has been defined already");
            }
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
    }

    @Override
    public void enterClassDef(CoolParser.ClassDefContext ctx) {
        int line = ctx.getStart().getLine();
        int column =  ctx.getText().indexOf(ctx.TYPE(0).getText());
        detectError("class",ctx.TYPE(0).getText(),line,column);
        Node cl = new Node(parents.peek(),ctx.TYPE(0).getText());
        nodes.add(cl);
        parents.peek().insert(cl.name,checkInheritance(ctx));
        parents.push(cl);
    }

    @Override
    public void exitClassDef(CoolParser.ClassDefContext ctx) {
        parents.pop();
    }

    @Override
    public void enterFunction(CoolParser.FunctionContext ctx) {
        int line = ctx.getStart().getLine();
        int column =  ctx.getText().indexOf(ctx.ID().getText());
        detectError("method",ctx.ID().getText(),line,column);
        Node method = new Node(parents.peek(),ctx.ID().getText());
        nodes.add(method);
        parents.peek().insert(method.name,new MethodObj("method",ctx.ID().getText(),ctx.TYPE().getText(),ctx.parameter()));
        parents.push(method);
    }

    @Override
    public void exitFunction(CoolParser.FunctionContext ctx) {
        parents.pop();
    }

    @Override
    public void enterVarDef(CoolParser.VarDefContext ctx) {
        int line = ctx.getStart().getLine();
        int column =  ctx.getText().indexOf(ctx.ID().getText());
        detectError("var",ctx.ID().getText(),line,column);
        Node field = new Node(parents.peek(),ctx.ID().getText());
        nodes.add(field);
        parents.peek().insert(field.name,new FieldObj("var",ctx.ID().getText(),ctx.TYPE().getText()));
    }

    @Override
    public void exitVarDef(CoolParser.VarDefContext ctx) {

    }

    @Override
    public void enterParam(CoolParser.ParamContext ctx) {

    }

    @Override
    public void exitParam(CoolParser.ParamContext ctx) {

    }

    @Override
    public void enterSub(CoolParser.SubContext ctx) {

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

    }

    @Override
    public void exitMul(CoolParser.MulContext ctx) {

    }

    @Override
    public void enterLteq(CoolParser.LteqContext ctx) {

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

    }

    @Override
    public void exitStaticCall(CoolParser.StaticCallContext ctx) {

    }

    @Override
    public void enterLt(CoolParser.LtContext ctx) {

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

    }

    @Override
    public void exitDiv(CoolParser.DivContext ctx) {

    }

    @Override
    public void enterNot(CoolParser.NotContext ctx) {

    }

    @Override
    public void exitNot(CoolParser.NotContext ctx) {

    }

    @Override
    public void enterNewObject(CoolParser.NewObjectContext ctx) {

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
        int column =  ctx.getText().indexOf(ctx.ID(0).getText());
        detectError("let",ctx.ID(0).getText(),line,column);
        Node field = new Node(parents.peek(),ctx.ID(0).getText());
        nodes.add(field);
        parents.peek().insert(field.name,new FieldObj("let",ctx.ID(0).getText(),ctx.TYPE(0).getText()));
    }

    @Override
    public void exitLet(CoolParser.LetContext ctx) {

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
