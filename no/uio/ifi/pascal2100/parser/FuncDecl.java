package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class FuncDecl extends ProcDecl{
	ParamDeclList prmList;
	TypeName typeName;
	Block block;
	String name;
	String label;
	int offsett = 4;
	FuncDecl(String name, int lNum){
		super(name,lNum);
		this.name = name;
	}
@Override public String identify() {
	return "<func decl> on line " + lineNum;
}

public static FuncDecl parse(Scanner s){
	enterParser("func decl");
	System.out.println("func decl 1: "+s.curToken.id);
	s.skip(functionToken);
	s.test(nameToken);
	System.out.println("func decl 2: "+s.curToken.id);
	FuncDecl fdecl = new FuncDecl(s.curToken.id, s.curLineNum());
	s.skip(nameToken);
	System.out.println("func decl 3: "+s.curToken.id);
	
	if(s.curToken.kind!=colonToken){
		fdecl.prmList = ParamDeclList.parse(s);

	}
	System.out.println("func decl 4: "+s.curToken.id);
	s.skip(colonToken);
	System.out.println("func decl 5: "+s.curToken.kind);
	fdecl.typeName = TypeName.parse(s);

	System.out.println("func decl 6: "+s.curToken.kind);
	s.skip(semicolonToken);
	fdecl.block = Block.parse(s);

	if(fdecl.prmList!=null){
		for(ParamDecl p : fdecl.prmList.declList){
			fdecl.offsett += 4;
			p.declOffset = fdecl.offsett;
			fdecl.block.addDecl(p.name,p);
		}
	}
	s.skip(semicolonToken);
	System.out.println("func decl 7: "+s.curToken.kind);
	leaveParser("func decl");
	return fdecl;

}
@Override public void prettyPrint() {
        //Example : function gcd (m: integer; n: integer): integer;
        Main.log.prettyPrint("function ");
        Main.log.prettyPrint(name+" ");
        if(prmList!=null){
        	prmList.prettyPrint();
        }
        Main.log.prettyPrint(": ");
        typeName.prettyPrint();
 		Main.log.prettyPrint(";");
 		Main.log.prettyPrintLn();
 		block.prettyPrint();
 		Main.log.prettyPrint(";");
}
@Override void check(Block curScope, Library lib) {

	if(prmList!=null){
		prmList.check(curScope,lib);
	}
	typeName.check(curScope,lib);
	block.check(curScope,lib);
}
@Override void genCode(CodeFile f){
	label = f.getLabel(name);
	//32 bytes + lokale variabler
	int localVars = block.declarations();
	
	f.genInstr("func$"+label,"","","");
	f.genInstr("","enter","$"+localVars+",$"+block.level,"Start of "+name);
	this.block.genCode(f);
	f.genInstr("","movl","-32(%ebp),%eax","Fetch return value");
	f.genInstr("","leave","","End of "+name);
	f.genInstr("","ret","","");
}



}