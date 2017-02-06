package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class ParamDecl extends PascalDecl{

	TypeName typeName;
	String name;
	
	ParamDecl(String id, int lNum){
		super(id,lNum);
		this.name = id;
	}

@Override public String identify() {
	return "<param decl> on line " + lineNum;
}

static ParamDecl parse(Scanner s){
	enterParser("param decl");
	s.test(nameToken);
	System.out.println("param decl 1: "+s.curToken.id);
	ParamDecl paramDcl = new ParamDecl(s.curToken.id, s.curLineNum());
	s.skip(nameToken);
	System.out.println("param decl 2: "+s.curToken.id);
	s.skip(colonToken);
	System.out.println("param decl 3: "+s.curToken.id);
	paramDcl.typeName = TypeName.parse(s);
	System.out.println("param decl 4: "+s.curToken.id);
	leaveParser("param decl");
	return paramDcl;
}
@Override public void prettyPrint() {
	Main.log.prettyPrint(name);
	Main.log.prettyPrint(": ");
	typeName.prettyPrint();
}
@Override void check(Block curScope, Library lib) {
	typeName.check(curScope,lib);
}
@Override void genCode(CodeFile f){
	typeName.genCode(f);
}
}