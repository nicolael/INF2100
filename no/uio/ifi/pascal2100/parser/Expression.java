package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class Expression extends PascalSyntax{
	SimpleExpr simpExpr;
	RelOperator relOp;
	SimpleExpr simpExpr2;

	Expression(int lNum){
		super(lNum);
	}

@Override public String identify() {
	return "<expression> on line " + lineNum;
}

static Expression parse(Scanner s){
	enterParser("expression");
	System.out.println("Expression 1: "+s.curToken.kind);
	Expression expr = new Expression(s.curLineNum());
	expr.simpExpr = SimpleExpr.parse(s);
	System.out.println("Expression 2: "+s.curToken.kind);
	
	switch(s.curToken.kind){
		case lessToken:
		case lessEqualToken:
		case notEqualToken:
		case greaterEqualToken:
		case greaterToken:
		case equalToken:
			System.out.println("Expression 3: "+s.curToken.kind);
			expr.relOp = RelOperator.parse(s);
			s.readNextToken();
			System.out.println("Expression says 4: "+s.curToken.kind);
			expr.simpExpr2 = SimpleExpr.parse(s);
			System.out.println("Expression says 5: "+s.curToken.kind);
			break;
	}
	System.out.println("Expression says 6 : "+s.curToken.kind);

	leaveParser("expression");
	return expr;
}

@Override void prettyPrint() {
    simpExpr.prettyPrint();
   	if(relOp!=null){
   		relOp.prettyPrint();
   		simpExpr2.prettyPrint();
   	}  
}
@Override void check(Block curScope, Library lib) {
	System.out.println("Expression check");
	simpExpr.check(curScope,lib);
	
	if(simpExpr2!=null){
		simpExpr2.check(curScope,lib);
	}
}
@Override void genCode(CodeFile f){
	System.out.println("expression genCode");
	simpExpr.genCode(f);

	if(relOp!=null){
		System.out.println("teeeeeeeeeeestt!!!!!!!");
		//putter 1.expr saa vi ikke overskriver 
		f.genInstr("","pushl","%eax","");
   		simpExpr2.genCode(f);
   		f.genInstr("","popl","%ecx","");
   		f.genInstr("","cmpl","%eax,%ecx","");
   		f.genInstr("","movl","$0,%eax","");
   		relOp.genCode(f);
   		
   	} 
}
}