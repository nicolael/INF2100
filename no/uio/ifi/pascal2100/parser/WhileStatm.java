package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

/* <while-statm> ::= ’while’ <expression> ’do’ <statement> */

class WhileStatm extends Statement {
	Expression expr; //ma lage denne klassen!!
	Statement body;

WhileStatm(int lNum) { 
	super(lNum);
}

@Override public String identify() {
	return "<while-statm> on line " + lineNum;
}

static WhileStatm parse(Scanner s) {
	enterParser("while-statm");
	WhileStatm ws = new WhileStatm(s.curLineNum());
	s.skip(whileToken);
	System.out.println("while says : "+s.curToken.id);
	ws.expr = Expression.parse(s);
	s.skip(doToken);
	ws.body = Statement.parse(s);
	leaveParser("while-statm");
	return ws;
}

@Override void prettyPrint() {
    Main.log.prettyPrint("while ");  expr.prettyPrint();
    Main.log.prettyPrintLn(" do");  Main.log.prettyIndent();
    body.prettyPrint();  Main.log.prettyOutdent();
}
@Override void check(Block curScope, Library lib) {
	System.out.println("while-statm check");
	expr.check(curScope,lib);
	body.check(curScope,lib);
}
@Override void genCode(CodeFile f){
	System.out.println("WhileStatm genCode");
	String testLabel = f.getLocalLabel(),
		   endLabel  = f.getLocalLabel();

	f.genInstr(testLabel, "", "", "Start while-statement");
	expr.genCode(f);
	f.genInstr("", "cmpl", "$0,%eax", "");
	f.genInstr("", "je", endLabel, "");
	body.genCode(f);
	System.out.println(body.identify());
	f.genInstr("", "jmp", testLabel, "");
	f.genInstr(endLabel, "", "", "End while-statement");

}
}