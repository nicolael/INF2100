package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class ProcDecl extends PascalDecl{
	Block block;
	ParamDeclList prmList;
	String value;
	String label;
	int offsett = 4;

	ProcDecl(String id, int lNum){
		super(id,lNum);
		value = id;
	}
@Override public String identify() {
	if(name.equals("write")){
		return "<proc decl> in the library";
	}
	return "<proc decl> on line " + lineNum;
}

public static ProcDecl parse(Scanner s){
	enterParser("proc decl");
	System.out.println("Proc decl 1: "+s.curToken.kind);
	s.skip(procedureToken); // procedureToken
	s.test(nameToken); // tester nameToken
	ProcDecl proc = new ProcDecl(s.curToken.id, s.curLineNum());
	s.skip(nameToken); //skipper procedureToken
	
	//Sjekker om det kommer parameter
	if(s.curToken.kind==leftParToken){
		System.out.println("Proc decl 2: "+s.curToken.kind);
		proc.prmList = ParamDeclList.parse(s);
		System.out.println("Proc decl 3: "+s.curToken.kind);
	}
	s.skip(semicolonToken); //skipper semicolonToken
	proc.block = Block.parse(s); //Parser block
	
	if(proc.prmList!=null){
		for(ParamDecl p : proc.prmList.declList){
			proc.offsett += 4;
			p.declOffset = proc.offsett;
			proc.block.addDecl(p.name,p);
		}
	}

	System.out.println("Proc decl 4: "+s.curToken.kind);
	s.skip(semicolonToken); //skipper semicoloToken
	System.out.println("Proc decl 5: "+s.curToken.kind);
	leaveParser("proc decl");
	return proc;
}
@Override public void prettyPrint() {
        Main.log.prettyPrint("procedure ");
        Main.log.prettyPrint(value+" ");
        if(prmList!=null){
        	prmList.prettyPrint();
        }
        Main.log.prettyPrintLn(";"); 
        block.prettyPrint();
        Main.log.prettyPrint(";"); 
}
@Override void check(Block curScope, Library lib) {
	System.out.println("proc decl check");

	if(prmList!=null){
		prmList.check(curScope,lib);
	}
	block.check(curScope,lib);
}
@Override void genCode(CodeFile f){
	System.out.println("ProcDecl genCode "+value);
	label = f.getLabel(name);
	int localVars = block.declarations();
	this.block.func_proc_decl(f);
	f.genInstr("proc$"+label,"","","");
	f.genInstr("","enter","$"+localVars+",$"+block.level,"Start of "+name);
	this.block.genCode(f);
	f.genInstr("","leave","","End of "+name);
	f.genInstr("","ret","","");

}
}


