package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

class EnumLiteral extends PascalDecl{

	String bool;

	EnumLiteral(String bool, int lNum){
		super(bool,lNum);
		this.bool = bool;
	}
@Override public String identify() {

	return "<enum literal> on line " + lineNum;
}
public static EnumLiteral parse(Scanner s){
	enterParser("enum literal");
	EnumLiteral enumLit = new EnumLiteral(s.curToken.id, s.curLineNum());
	leaveParser("enum literal");
	return enumLit;
}
@Override public void prettyPrint() {
       
}
@Override void check(Block curScope, Library lib) {

}
@Override void genCode(CodeFile f){
	switch(bool){
		case "true":
			f.genInstr("","movl","$1,%eax","  enum value true");
			break;
		case "false":
			f.genInstr("","movl","$0,%eax","  enum value false");
			break;
	}
}
}