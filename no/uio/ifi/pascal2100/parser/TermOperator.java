package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class TermOperator extends Operator{

	String value;
	TermOperator(String value, int lNum){
		super(lNum);
		this.value = value;
	}

@Override public String identify() {
	return "<term opr> on line " + lineNum;
}
static TermOperator parse(Scanner s){
	enterParser("term opr");
	System.out.println("Term opr says : "+s.curToken.kind);
	TermOperator termOpr=null;
	switch(s.curToken.kind){
		case addToken:
			System.out.println("Term opr says : "+s.curToken.kind);
			termOpr = new TermOperator(s.curToken.id,s.curLineNum());
			s.readNextToken();
			break;
		case subtractToken:
			System.out.println("Term opr says : "+s.curToken.kind);
			termOpr = new TermOperator(s.curToken.id,s.curLineNum());
			s.readNextToken();
			break;
		case orToken:
			System.out.println("Term opr says : "+s.curToken.kind);
			termOpr = new TermOperator(s.curToken.id,s.curLineNum());
			s.readNextToken();
			break;
	}

	leaveParser("term opr");
	return termOpr;
}
@Override void prettyPrint() {
    Main.log.prettyPrint(" "+value+" ");
}
@Override void check(Block curScope, Library lib) {

}
@Override void genCode(CodeFile f){
	//f.genInstr("","pushl","%eax","");
	System.out.println("TermOperator GENCODE "+value);
	switch(value){
		case "+":
			f.genInstr("","movl","%eax,%ecx","");
			f.genInstr("","popl","%eax","");
			f.genInstr("","addl","%ecx,%eax","+");
			break;
		case "-":
			f.genInstr("","movl","%eax,%ecx","");
			f.genInstr("","popl","%eax","");
			f.genInstr("","subl","%ecx,%eax","-");
			break;
		case "or":
			f.genInstr("","movl","%eax,%ecx","");
			f.genInstr("","popl","%eax","");
			f.genInstr("","orl","%ecx,%eax"," or");
			break;
	}

}
}


