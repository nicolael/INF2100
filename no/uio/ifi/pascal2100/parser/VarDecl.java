package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class VarDecl extends PascalDecl{
	Type type;
	int level;
	VarDecl(String id, int lNum){
		super(id,lNum);
	}

@Override public String identify() {
	return "<var decl> on line " + lineNum;
}

public static VarDecl parse(Scanner s){
	enterParser("var decl");
	s.test(nameToken);
	System.out.println("var decl 1: "+s.curToken.kind);
	VarDecl vardecl = new VarDecl(s.curToken.id, s.curLineNum());
	s.skip(nameToken); 
	//over til type
	System.out.println("var decl 2: "+s.curToken.id);
	s.skip(colonToken);
	System.out.println("var decl 3: "+s.curToken.kind);

	vardecl.type = Type.parse(s);
	s.test(semicolonToken);
	System.out.println("var decl 4: "+s.curToken.kind);
	s.skip(semicolonToken);
	leaveParser("var decl");
	return vardecl;
}
@Override public void prettyPrint() {
        Main.log.prettyPrint(name);
        Main.log.prettyPrint(" : "); type.prettyPrint();
        Main.log.prettyPrint(";"); 
       
}
@Override void check(Block curScope, Library lib) {
	System.out.println("ConstDecl");
	type.check(curScope,lib);
	level = curScope.level;

}
@Override void genCode(CodeFile f){
	
}

}