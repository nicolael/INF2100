package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Collections;

public class FuncCall extends Factor{

	Expression expr;
	LinkedList<Expression> exprList;
	String name;
	FuncDecl funcRef;

	FuncCall(String name,int lNum){
		super(lNum);
		this.name = name;
		exprList = new LinkedList<Expression>();
	}

	@Override public String identify() {
		return "<func call> on line " + lineNum;
	}

static FuncCall parse(Scanner s){
	enterParser("func call");
	//tester om det som kommer inn er et navn
	s.test(nameToken);
	System.out.println("func call 1: "+s.curToken.kind);
	FuncCall funcCall = new FuncCall(s.curToken.id, s.curLineNum());
	s.skip(nameToken);
	System.out.println("func call 2: "+s.curToken.id);

	if(s.curToken.kind == leftParToken){
		s.skip(leftParToken);
		System.out.println("func call 3: "+s.curToken.kind);
		funcCall.expr = Expression.parse(s);
		funcCall.exprList.add(funcCall.expr);
		System.out.println("func call 4: "+s.curToken.kind);
		while(s.curToken.kind == commaToken){
			s.skip(commaToken);
			System.out.println("func call 5: "+s.curToken.id);
			funcCall.expr = Expression.parse(s);
			funcCall.exprList.add(funcCall.expr);
		}
		s.skip(rightParToken);
	}
	leaveParser("func call");
	return funcCall;
}

	@Override void prettyPrint() {
		ListIterator<Expression> xprIt = exprList.listIterator();
	    Main.log.prettyPrint(name);
	    Main.log.prettyPrint("(");  
	    while(xprIt.hasNext()){
	    	Expression xp = xprIt.next();
	    	xp.prettyPrint();
	    	if(xprIt.hasNext()){
	    		Main.log.prettyPrint(", ");
	    	} 
	    }
	    Main.log.prettyPrint(")");
	}
	@Override void check(Block curScope, Library lib) {
		System.out.println("funcCall check");
		PascalDecl d = curScope.findDecl(name,this);
	
		for(Expression xpr :exprList){
			xpr.check(curScope,lib);
		}
		funcRef = (FuncDecl)d;
	}
@Override void genCode(CodeFile f){
	System.out.println("func call genCode");
	Collections.reverse(exprList);
	ListIterator<Expression> xprIt = exprList.listIterator();
	int bytes_pushed=0;
	int pushedParam= exprList.size();	
	while(xprIt.hasNext()){
		
	    Expression xp = xprIt.next();
	    xp.genCode(f);
	    f.genInstr("","pushl","%eax"," Push param #"+pushedParam);
	    bytes_pushed+=4;
	    pushedParam--;
	}
	f.genInstr("", "call","func$"+funcRef.label, "");
	f.genInstr("", "addl","$"+bytes_pushed+",%esp", "Pop parameters");
}

}