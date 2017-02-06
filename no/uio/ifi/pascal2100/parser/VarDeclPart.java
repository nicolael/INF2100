package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;

public class VarDeclPart extends PascalSyntax{
	VarDecl vardecl;
	LinkedList<VarDecl> varList;

	VarDeclPart(int lNum){
		super(lNum);
		varList = new LinkedList<VarDecl>();
	}

	@Override public String identify() {
		return "<var decl part> on line " + lineNum;
	}

	static VarDeclPart parse(Scanner s){
		enterParser("var decl part");
		VarDeclPart decl = new VarDeclPart(s.curLineNum());
		s.test(varToken);
		System.out.println("var decl part 1: "+s.curToken.kind);
		s.skip(varToken);
		decl.vardecl = VarDecl.parse(s);
		decl.varList.add(decl.vardecl);

		while(s.curToken.kind==nameToken){

			decl.vardecl = VarDecl.parse(s);
			decl.varList.add(decl.vardecl);
		}
		System.out.println("var decl part 2: "+s.curToken.kind);
		leaveParser("var decl part");
		return decl;
	}
	@Override void prettyPrint() {
	    Main.log.prettyPrint("var ");
	    Main.log.prettyPrintLn();

	    for(VarDecl v : varList){

	    	Main.log.prettyIndent();
	    	v.prettyPrint();
	    	Main.log.prettyOutdent();
	    	Main.log.prettyPrintLn();
	    }
	}
	@Override void check(Block curScope, Library lib) {
		System.out.println(" VarDeclPart check");
		for (VarDecl vd: varList) {
	          System.out.println("var decl");
	          vd.check(curScope,lib);
	    }
	}
@Override void genCode(CodeFile f){
	
}
}