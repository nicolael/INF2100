package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;

public class InnerExpr extends Factor{

	Expression expr;
	InnerExpr(int lNum){
		super(lNum);
	}
@Override public String identify() {
	return "<inner expr> on line " + lineNum;
}

public static InnerExpr parse(Scanner s){
	enterParser("inner expr");
	s.skip(leftParToken);
	InnerExpr inExpr = new InnerExpr(s.curLineNum());
	inExpr.expr = Expression.parse(s);
	s.skip(rightParToken);
	leaveParser("inner expr");
	return inExpr;
}

@Override void prettyPrint() {
	Main.log.prettyPrint("(");
	if(expr!=null){
		expr.prettyPrint();
	}
	Main.log.prettyPrint(")");
}
@Override void check(Block curScope, Library lib) {
	expr.check(curScope,lib);
}
@Override void genCode(CodeFile f){
	System.out.println("InnerExpr genCode");
	expr.genCode(f);
}
}