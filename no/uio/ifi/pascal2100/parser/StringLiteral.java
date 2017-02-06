package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class StringLiteral extends Constant {

	String value;
	StringLiteral(String value, int lNum){
		super(lNum);
		this.value = value;
	}
@Override public String identify() {
	return "<String literal> " + value + " on line " + lineNum;
}

public static StringLiteral parse(Scanner s){
	enterParser("string literal");
	StringLiteral str = new StringLiteral(s.curToken.strVal, s.curLineNum());
	s.skip(stringValToken);
	leaveParser("string literal");
	return str;
}
@Override public void prettyPrint() {
        Main.log.prettyPrint("'"+value+"'");
}
@Override void check(Block curScope, Library lib) {
	System.out.println("StringLiteral");
}
@Override void genCode(CodeFile f){
	
}
}