package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class CharLiteral extends Constant{
	
	String c;
	CharLiteral(String c, int lNum){
		super(lNum);
		this.c=c;
	}
@Override public String identify() {
	return "<char literal> on line " + lineNum;
}
static CharLiteral parse(Scanner s){
	enterParser("char literal");
	CharLiteral ch = new CharLiteral(s.curToken.strVal,s.curLineNum());
	System.out.println("char says : "+s.curToken.kind);
	s.skip(stringValToken);
	leaveParser("char literal");
	return ch;
}
@Override void prettyPrint() {
    Main.log.prettyPrint("'"+c+"'");
}
@Override void check(Block curScope, Library lib) {
	System.out.println("CharLiteral");
}
@Override void genCode(CodeFile f){
	System.out.println("CharLiteral genCode");
	String test = Integer.toString((int)c.charAt(0));
	f.genInstr("","movl","$"+test+",%eax","  char "+test);
}
}