package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

/*Super klasse for CharLiteral, NamedConst, NumberLiteral
 StringLiteral*/
public abstract class Constant extends Factor{
	
	Constant(int lNum){
		super(lNum);
	}

@Override public String identify() {
	return "<constant> on line " + lineNum;
}

static Constant parse(Scanner s){
	enterParser("constant");
	Constant konst = null;

	switch(s.curToken.kind){
		case nameToken:
			konst = NamedConst.parse(s);
			break;
		case intValToken:
			konst = NumberLiteral.parse(s);
			break;
		case stringValToken:
			String value=s.curToken.strVal;
			switch(value.length()){
				case 1 :
				konst = CharLiteral.parse(s);
				break;
				default:
				konst = StringLiteral.parse(s);
				break;
			}	
			break;
		default:
	}

	leaveParser("constant");
	return konst;
}	

}

