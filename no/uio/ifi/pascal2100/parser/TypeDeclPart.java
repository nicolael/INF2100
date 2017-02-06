package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;

public class TypeDeclPart extends PascalSyntax{
	TypeDecl type;
	LinkedList<TypeDecl> typeList;
	TypeDeclPart(int lNum){
		super(lNum);
		typeList = new LinkedList<TypeDecl>();
	}

	@Override public String identify() {
		return "<type decl part> on line " + lineNum;
	}

static TypeDeclPart parse(Scanner s){
	enterParser("type decl part");
	System.out.println("Type dcl part : "+s.curToken.id);
	s.skip(typeToken);
	TypeDeclPart typDeclPart = new TypeDeclPart(s.curLineNum());
	typDeclPart.type = TypeDecl.parse(s);
	typDeclPart.typeList.add(typDeclPart.type);
	
	while(s.curToken.kind == nameToken){
		typDeclPart.type = TypeDecl.parse(s);
		typDeclPart.typeList.add(typDeclPart.type);
	}
	System.out.println("Type dcl part : "+s.curToken.id);
	leaveParser("type decl part");
	return typDeclPart;
}
@Override void prettyPrint() {
	Main.log.prettyPrint("type ");
	Main.log.prettyPrintLn();
	for(TypeDecl t: typeList){
		t.prettyPrint();
		Main.log.prettyPrintLn();
	}

}
@Override void check(Block curScope, Library lib) {
	System.out.println("TypeDeclPart check");
	for(TypeDecl tpDcl : typeList ){
		tpDcl.check(curScope,lib);
	}
}
@Override void genCode(CodeFile f){
	
}
}