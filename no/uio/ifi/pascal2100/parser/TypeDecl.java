package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class TypeDecl extends PascalDecl{

	TypeName typeName;
	Type type;
	String name;
	TypeDecl(String id, int lNum){
		super(id, lNum);
		name = id;
	}

@Override public String identify() {
	switch(name){
		case "integer":
		case "boolean":
		case "char":
			return "<type decl> in the library";
	}
	return "<type decl> on line " + lineNum;
}

public static TypeDecl parse(Scanner s){
	enterParser("type decl");
	s.test(nameToken); //sjekker navn;
	System.out.println("type decl 1: "+s.curToken.id);
	TypeDecl typeDecl = new TypeDecl(s.curToken.id, s.curLineNum());
	
	s.skip(nameToken);
	System.out.println("type decl 2: "+s.curToken.id);
	s.skip(equalToken); //skipper equaltoken

	typeDecl.type = Type.parse(s); //parser til Type
	System.out.println("type decl 3: "+s.curToken.id);

	s.skip(semicolonToken);//skippe semikolon;

	leaveParser("type decl");
	return typeDecl;

}
@Override public void prettyPrint() {

	Main.log.prettyPrint(name);
	Main.log.prettyPrint(" = ");
	type.prettyPrint();
	Main.log.prettyPrint(";");

}
@Override void check(Block curScope, Library lib) {
	System.out.println(name+"TypeDecl check");
	if(type instanceof TypeName){
		typeName = (TypeName)type;
		System.out.println("250788"+typeName.type+" = "+name);
	}
	//TypeName tp = (TypeName)t;
	type.check(curScope,lib);
}
@Override void genCode(CodeFile f){
	System.out.println("TypeDecl genCode");
}
}