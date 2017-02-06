package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class NamedConst extends Constant{

	String value;
	PascalDecl d;
	NamedConst(String value, int lNum){
		super(lNum);
		this.value = value;
	}

@Override public String identify() {
	return "<named constant> on line " + lineNum;
}	

public static NamedConst parse(Scanner s){
	enterParser("named constant");
	s.test(nameToken);
	NamedConst nameConst = new NamedConst(s.curToken.id,s.curLineNum());
	s.skip(nameToken);
	leaveParser("named constant");
	return nameConst;
}
@Override void prettyPrint() {

    Main.log.prettyPrint(value);
}
@Override void check(Block curScope, Library lib) {
	System.out.println("NamedConst");
	d = curScope.findDecl(value,this);
}
@Override void genCode(CodeFile f){
	System.out.println("NamedConst GENDCODE -->"+value);
}
}