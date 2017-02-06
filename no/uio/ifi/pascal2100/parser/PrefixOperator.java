package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class PrefixOperator extends Operator{

	String sign;
	PrefixOperator(String sign, int lNum){
		super(lNum);
		this.sign = sign;
	}

@Override public String identify() {
	return "<prefix opr> on line " + lineNum;
}

public static PrefixOperator parse(Scanner s){
	enterParser("prefix opr");

	PrefixOperator prfOpr = null;
	switch(s.curToken.kind){
		case addToken:
		case subtractToken:
			prfOpr = new PrefixOperator(s.curToken.id, s.curLineNum());
			break;

	}
	s.readNextToken();
	leaveParser("prefix opr");
	return prfOpr;
}
@Override void prettyPrint() {
    Main.log.prettyPrint(sign+" ");
}
@Override void check(Block curScope, Library lib) {

}
@Override void genCode(CodeFile f){
	switch(sign){
		case "-":
			f.genInstr("","negl","%eax"," - (prefix)");
			break;	
	}	
}
}