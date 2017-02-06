package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
import java.util.Collections;
class ConstDeclPart extends PascalSyntax {
	ConstDecl konst;
	LinkedList<ConstDecl> constList;
	ConstDeclPart(int lNum){
		super(lNum);
		constList = new LinkedList<ConstDecl>();
	}
	@Override public String identify() {
		return "<const decl part> on line " + lineNum;
	}

	static ConstDeclPart parse(Scanner s){
		enterParser("const decl part");
		ConstDeclPart constant = new ConstDeclPart(s.curLineNum());
		System.out.println("const decl part 1: "+s.curToken.kind);
		s.test(constToken);
		s.skip(constToken);
		System.out.println("const decl part 2: "+s.curToken.kind);
		constant.konst = ConstDecl.parse(s);
		constant.constList.add(constant.konst);
		//så lenge neste token er et nameToken så må vi gjenta
		
		System.out.println("const decl part 3: "+s.curToken.kind);
		while(s.curToken.kind==nameToken){
			constant.konst = ConstDecl.parse(s);
			constant.constList.add(constant.konst);
		}
		leaveParser("const decl part");
		return constant;
	}
	@Override void prettyPrint() {
	    Main.log.prettyPrint("const ");
	    Main.log.prettyPrintLn(); 
	  
	    for(ConstDecl cdecl : constList){
	    	Main.log.prettyIndent();
	    	cdecl.prettyPrint();
	    	Main.log.prettyOutdent();
	    	Main.log.prettyPrintLn();
	    }
	   
	}
@Override void check(Block curScope, Library lib) {
	System.out.println(" ConstDeclpart check");
	for (ConstDecl cd: constList) {
          	cd.check(curScope,lib);
    }
}
@Override void genCode(CodeFile f){
	System.out.println("ConstDeclPart genCode");
	Collections.reverse(constList);

	for(ConstDecl c : constList){
		c.genCode(f);
	}
}
}