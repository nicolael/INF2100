package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

/* <program> ::= ’program’ <name> ’;’ <block> ’.’ */
// clang -m32 mini.s -o mini

public class Program extends PascalDecl {
	Block progBlock; 
	String label;
	
Program(String id, int lNum) { 
	super(id, lNum);
}

@Override public String identify() {
	return "<program> " + name + " on line " + lineNum;
}

public static Program parse(Scanner s) { 
	enterParser("program");
	System.out.println("Program : "+s.curToken.kind);	
	s.skip(programToken);
	s.test(nameToken);
	Program p = new Program(s.curToken.id, s.curLineNum());
	s.readNextToken();
	s.skip(semicolonToken);
	p.progBlock = Block.parse(s); p.progBlock.context = p; 

	System.out.println("Program : "+s.curToken.kind);	
	s.skip(dotToken);
	System.out.println("Program : "+s.curToken.kind);	
	
	leaveParser("program");
	return p;

}

@Override public void prettyPrint() {
        Main.log.prettyPrint("program ");  //expr.prettyPrint();
        Main.log.prettyPrintLn(name+";");  
        progBlock.prettyPrint();
        Main.log.prettyPrint(".");
        Main.log.prettyIndent();  
        Main.log.prettyOutdent();
}
@Override public void check(Block curScope, Library lib) {

	progBlock.check(curScope,lib);
	
}
@Override public void genCode(CodeFile f){
	label = f.getLabel(name);
	f.genInstr("", ".globl  _main", "", "");
	f.genInstr("", ".globl  main", "", "");
	f.genInstr("_main","","","");
	f.genInstr("main","call","prog$"+label,"Start program");
	f.genInstr("","movl","$0,%eax","Set status 0 and");
	f.genInstr("","ret","","terminate the program");
	progBlock.func_proc_decl(f);
	progBlock.genCode(f);
}

}