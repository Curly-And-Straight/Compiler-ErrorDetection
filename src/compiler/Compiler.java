package compiler;
import gen.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.io.IOException;

public class Compiler {
    public static void main(String[] args) throws IOException{
        CharStream stream = CharStreams.fromFileName("./sample/test2.cool");
        CoolLexer lexer = new CoolLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        CoolParser parser = new CoolParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        CoolListener listener = new ScopeChecker();
        walker.walk(listener,tree);
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < ScopeChecker.nodes.size(); i++) {
            System.out.println(ScopeChecker.nodes.get(i).name + " : " + ScopeChecker.nodes.get(i).symbolTable.keySet() + ScopeChecker.nodes.get(i).symbolTable.values().toString());
        }


    }


}
