package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

class FactorOperator extends Operator{

	String value;
	FactorOperator(String value, int lNum){
		super(lNum);
		this.value = value;	
	}

@Override public String identify() {
	return "<factor opr> on line " + lineNum;
}

static FactorOperator parse(Scanner s){
	enterParser("factor opr");
	System.out.println("Factor opr : "+s.curToken.id);
	FactorOperator factorOpr = null;
	switch(s.curToken.kind){
		case multiplyToken:
			System.out.println("Factor opr : "+s.curToken.id);
			factorOpr = new FactorOperator(s.curToken.id, s.curLineNum());
			s.skip(multiplyToken);
			break;
		case divToken :
			System.out.println("Factor opr : "+s.curToken.id);
			factorOpr = new FactorOperator(s.curToken.id, s.curLineNum());
			s.skip(divToken);
			break;
		case modToken :
			System.out.println("Factor opr : "+s.curToken.id);
			factorOpr = new FactorOperator(s.curToken.id, s.curLineNum());
			s.skip(modToken);
			break;
		case andToken :
			System.out.println("Factor opr : "+s.curToken.id);
			factorOpr = new FactorOperator(s.curToken.id, s.curLineNum());
			s.skip(andToken);
			break;
	}
	leaveParser("factor opr");
	return factorOpr;
}
@Override void prettyPrint() {
    Main.log.prettyPrint(" "+value+" ");
}
@Override void check(Block curScope, Library lib) {

}
@Override void genCode(CodeFile f){
	System.out.println("FactorOperator genCode "+value);
	f.genInstr("", "movl","%eax,%ecx", "");
	f.genInstr("", "popl","%eax", "");

	switch(value){
	case "div":
		f.genInstr("","cdq","","");
		f.genInstr("","idivl","%ecx"," /");
		break;
	case "mod":
		f.genInstr("","cdq","","");
		f.genInstr("","idivl","%ecx","");
		f.genInstr("", "movl","%edx,%eax", " "+value);
		break;
	case "*":
		f.genInstr("","imull","%ecx,%eax","*");
		break;
	case "and":
		f.genInstr("","andl","%ecx,%eax"," and");
		break;
	}
}
}