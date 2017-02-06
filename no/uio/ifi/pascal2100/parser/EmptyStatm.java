package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class EmptyStatm extends Statement{

	EmptyStatm(int lNum){
		super(lNum);
	}
@Override public String identify() {
	return "<empty statm> on line " + lineNum;

}
static EmptyStatm parse(Scanner s){
	enterParser("empty statm");
	EmptyStatm empt=new EmptyStatm(s.curLineNum());

	leaveParser("empty statm");
	return empt;
}
@Override void prettyPrint() {
    //Main.log.prettyPrint("");
}
@Override void check(Block curScope, Library lib) {
	System.out.println("empty statm check");
}
@Override void genCode(CodeFile f){
	
}
}