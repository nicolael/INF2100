package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

//Super klassen til constant, variable, func call,
//inner expr, negation
public abstract class Factor extends PascalSyntax{
	
	Factor(int lNum){
		super(lNum);
	}

@Override public String identify() {
	return "<factor> on line " + lineNum;
}
static Factor parse(Scanner s){
	enterParser("factor");
	Factor factor = null;
	System.out.println("Factor : "+s.curToken.kind+" next : "+s.nextToken.kind);

	switch(s.curToken.kind){
		case nameToken:
			if(s.nextToken.kind==leftParToken){
				System.out.println("Factor func call 1 : "+s.curToken.kind);
				factor = FuncCall.parse(s);
				System.out.println("Factor func call 1 : "+s.curToken.kind);
			}else{
				System.out.println("Factor var 1 : "+s.curToken.kind);
				factor = Variable.parse(s);
				System.out.println("Factor var 2 : "+s.curToken.kind);
			}
			break;
		case notToken:
			factor = Negation.parse(s);
			break;
		case leftParToken:
			System.out.println("leftParToken");
			factor = InnerExpr.parse(s);
			break;
		case stringValToken:
		case intValToken:
			factor=Constant.parse(s);
			break;
	}
	leaveParser("factor");
	return factor;
}

}