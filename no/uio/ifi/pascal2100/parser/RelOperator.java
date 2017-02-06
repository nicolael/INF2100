package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

class RelOperator extends Operator{

	String value;
	RelOperator(String value, int lNum){
		super(lNum);
		this.value = value;
	}
@Override public String identify() {
	return "<rel opr> on line " + lineNum;
}

static RelOperator parse(Scanner s){
	enterParser("rel opr");
	String token = s.curToken.kind.toString();
	RelOperator relOp=new RelOperator(token,s.curLineNum());
	leaveParser("rel opr");
	return relOp;
}
@Override void prettyPrint() {
    Main.log.prettyPrint(" "+value+" ");
}
@Override void check(Block curScope, Library lib) {

}
@Override void genCode(CodeFile f){
	switch(value){
		case "=" :
			f.genInstr("","sete","%al","Test =");
			break;
		case "<" :
			f.genInstr("","setl","%al","Test <");
			break;
		case "<=":
			f.genInstr("","setle","%al","Test <=");
			break;
		case ">=":
			f.genInstr("","setge","%al","Test >=");
			break;
		case "<>":
			f.genInstr("","setne","%al","Test <>");
			break;
		case ">":
			f.genInstr("","setg","%al","Test >");
			break;
	}
}

}