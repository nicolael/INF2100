package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class NumberLiteral extends Constant{

	int value;
	NumberLiteral(int value, int lNum){
		super(lNum);
		this.value= value;
	}

@Override public String identify() {
	return "<number literal> on line " + lineNum;
}

public static NumberLiteral parse(Scanner s){
	enterParser("number literal");
	System.out.println("NumberLit says : "+s.curToken.intVal);
	NumberLiteral number = new NumberLiteral(s.curToken.intVal, s.curLineNum());
	s.readNextToken();
	leaveParser("number literal");
	return number;
}
@Override void prettyPrint() {

    Main.log.prettyPrint(Integer.toString(value));  //expr.prettyPrint();

}

@Override void check(Block curScope, Library lib) {
	System.out.println("NumberLiteral");
}
@Override void genCode(CodeFile f){
	System.out.println("NUmberLiteral genCode "+value);
	f.genInstr("","movl","$"+value+",%eax"," "+value);
}
}