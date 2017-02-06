package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
//java -jar pascal2100.jar -testparser easter.pas
//diff -iwy easter.log easterOrig.log
class IfStatm extends Statement{
	Expression expr;
	Statement body;
	Statement body2; //else body
	LinkedList<Statement> bodyList;
	
	IfStatm(int lNum){
		super(lNum);
		bodyList = new LinkedList<Statement>();
	
	}
@Override public String identify() {
	return "<if-statm> on line " + lineNum;
}

static IfStatm parse(Scanner s){
	enterParser("if-statm");
	IfStatm ifstm = new IfStatm(s.curLineNum());
	s.skip(ifToken); //skipper ifToken
	System.out.println("IfStatm 1: "+s.curToken.kind);
	ifstm.expr = Expression.parse(s); //parserer etter ifToken
	System.out.println("IfStatm 2: "+s.curToken.kind);
	s.skip(thenToken); //skipper thenToken
	System.out.println("IfStatm 3: "+s.curToken.kind);
	ifstm.body = Statement.parse(s); //parserer etter thenToken
	System.out.println("IfStatm 4: "+s.curToken.kind);
	
	if(s.curToken.kind==elseToken){
	
		s.skip(elseToken);
		System.out.println("IfStatm 5: "+s.curToken.kind);
		ifstm.body2 = Statement.parse(s);
		ifstm.bodyList.add(ifstm.body2);
		System.out.println("IfStatm 6: "+s.curToken.kind);
	}

	leaveParser("if-statm");
	return ifstm;

}
@Override void prettyPrint() {

    Main.log.prettyPrint("if ");
    expr.prettyPrint();
    Main.log.prettyPrintLn(" then");
    Main.log.prettyIndent();
    body.prettyPrint();  
    Main.log.prettyOutdent();
    for(Statement st : bodyList){
    	
    	Main.log.prettyPrintLn();
    	Main.log.prettyPrintLn("else ");
    	Main.log.prettyIndent();
    	st.prettyPrint();
    	Main.log.prettyOutdent();
    }
}
@Override void check(Block curScope, Library lib) {
	System.out.println("IfStatm check");
	expr.check(curScope,lib);
	body.check(curScope,lib);
	for(Statement st : bodyList){
		st.check(curScope,lib);
	}
}
@Override void genCode(CodeFile f){
	System.out.println("if statm GENCODE");
	String testLabel = f.getLocalLabel();
	String endLabel  = "";//f.getLocalLabel();
	
	if(this.body instanceof CompoundStatm){
		f.genInstr("","","","Start if-statement");
		expr.genCode(f);
		f.genInstr("","cmpl","$0,%eax","");
		f.genInstr("","je",testLabel,"");
		body.genCode(f);

		if(body2!=null){
			endLabel  = f.getLocalLabel();
			f.genInstr("", "jmp", endLabel, "");
			f.genInstr(testLabel,"","","");
			body2.genCode(f);
			f.genInstr(endLabel, "", "", "End if-statement");
		}else{
			f.genInstr(testLabel, "", "", "End if-statement");
		}
	}else{
		endLabel  = f.getLocalLabel();
		f.genInstr("","","","Start if-statement");
		expr.genCode(f);
		f.genInstr("","cmpl","$0,%eax","");
		f.genInstr("","je",testLabel,"");
		body.genCode(f);
		if(body2!=null){
			f.genInstr("", "jmp", endLabel, "");
			f.genInstr(testLabel,"","","");
			body2.genCode(f);
			f.genInstr(endLabel, "", "", "End if-statement");
		}else{
			f.genInstr(testLabel, "", "", "End if-statement");
		}
	}
}

}