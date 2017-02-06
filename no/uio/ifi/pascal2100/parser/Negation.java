package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class Negation extends Factor{

	Factor factor;
	String not;
	Negation(String not,int lNum){
		super(lNum);
		this.not = not;
	}

	@Override public String identify() {
		return "<negation> on line " + lineNum;
	}

	static Negation parse(Scanner s){
		enterParser("negation");
		s.test(notToken);
		Negation neg = new Negation(s.curToken.id, s.curLineNum());
		s.skip(notToken);
		neg.factor = Factor.parse(s);
		leaveParser("negation");
		return neg;
	}
	
	@Override void prettyPrint() {
	    Main.log.prettyPrint(not); 
	    if(factor!=null){
	    	factor.prettyPrint();
	    } 
	}
	@Override void check(Block curScope, Library lib) {
		factor.check(curScope,lib);
	}
@Override void genCode(CodeFile f){
	System.out.println("Negation genCode "+not);
	factor.genCode(f);
	f.genInstr("","xorl","$0x1,%eax"," not");
}
}