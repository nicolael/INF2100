package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

class CompoundStatm extends Statement{
	StatmList statmList;
	
	CompoundStatm(int lNum) { 
		super(lNum);
	}

	@Override public String identify() {
	return "<compound statm> on line " + lineNum;
	}
	static CompoundStatm parse(Scanner s) {
		enterParser("compound statm");
		CompoundStatm cmpSt = new CompoundStatm(s.curLineNum());
		System.out.println("compound says : "+s.curToken.kind);
		s.skip(beginToken);
		 
		cmpSt.statmList = StatmList.parse(s);
		System.out.println("compound says : "+s.curToken.kind);
		s.test(endToken);
		s.skip(endToken);

		System.out.println("compound says : "+s.curToken.kind);
		leaveParser("compound statm");
		return cmpSt;
	}
	@Override void prettyPrint() {
	    Main.log.prettyPrint("begin "); 
	    Main.log.prettyPrintLn();  //Main.log.prettyIndent();
	    Main.log.prettyIndent();
	    statmList.prettyPrint();
	    Main.log.prettyOutdent();
	    Main.log.prettyPrint("end");
	    //Main.log.prettyOutdent();
	}
	@Override void check(Block curScope, Library lib) {
		System.out.println("CompoundStatm check");
		statmList.check(curScope,lib);
	}
@Override void genCode(CodeFile f){
	System.out.println("CompoundStatm GENCODE");
	statmList.genCode(f);
}
}