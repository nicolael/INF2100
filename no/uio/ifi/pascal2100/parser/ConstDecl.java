package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class ConstDecl extends PascalDecl{
	Constant konst;

	ConstDecl(String id, int lNum){
		super(id,lNum);
	}
@Override public String identify() {
	if(name.equals("eol")){
		return "<const decl> in the library";
	}
	return "<const decl> on line " + lineNum;
}

public static ConstDecl parse(Scanner s){
	enterParser("const decl");
	ConstDecl decl = new ConstDecl(s.curToken.id, s.curLineNum());
	System.out.println("ConstDecl 1: "+s.curToken.id);
	s.test(nameToken);
	s.skip(nameToken);
	System.out.println("ConstDecl 2: "+s.curToken.kind);
	s.test(equalToken);
	s.skip(equalToken);
	System.out.println("ConstDecl 3: "+s.curToken.kind);
	decl.konst = Constant.parse(s);
	System.out.println("ConstDecl 4: "+s.curToken.kind);
	s.skip(semicolonToken);
	leaveParser("const decl");
	return decl;
}
@Override public void prettyPrint() {
       
        Main.log.prettyPrint(name);
        Main.log.prettyPrint(" = "); konst.prettyPrint(); 
        Main.log.prettyPrint(";"); 
        //Main.log.prettyOutdent();
}
@Override void check(Block curScope, Library lib) {
	System.out.println("ConstDecl");
	konst.check(curScope,lib);
}
@Override void genCode(CodeFile f){
	System.out.println("ConstDecl GENCODE");
	konst.genCode(f);
}
}