package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class SimpleExpr extends PascalSyntax{
	Term term;
	TermOperator termOpr;
	PrefixOperator prfOpr;
	LinkedList<TermOperator> oprList;
	LinkedList<Term> termList;
	
	SimpleExpr(int lNum){
		super(lNum);
		termList = new LinkedList<Term>();
		oprList = new LinkedList<TermOperator>();
	}

@Override public String identify() {
	return "<simple expr> on line " + lineNum;
}

static SimpleExpr parse(Scanner s){
	enterParser("simple expr");
	SimpleExpr expr = new SimpleExpr(s.curLineNum());
	System.out.println("simple xpr says 1: "+s.curToken.id);

	//sjekker for prefix
	switch(s.curToken.kind){
		case addToken:
		case subtractToken:
			expr.prfOpr = PrefixOperator.parse(s);
	}

	expr.term = Term.parse(s); //parser term
	expr.termList.add(expr.term);
	
	System.out.println("simple xpr says 2: "+s.curToken.kind);

	while(s.curToken.kind==addToken||s.curToken.kind==subtractToken||s.curToken.kind==orToken){
		expr.termOpr = TermOperator.parse(s);
		expr.oprList.add(expr.termOpr);
		
		System.out.println("simple xpr says 4: "+s.curToken.kind);
		expr.term = Term.parse(s);
		expr.termList.add(expr.term);
		System.out.println("simple xpr says 5: "+s.curToken.kind);
	}
	System.out.println("simple xpr says 6: "+s.curToken.kind);
	leaveParser("simple expr");
	return expr;
}
@Override void prettyPrint() {
     
    ListIterator<Term> termIt = termList.listIterator();
    ListIterator<TermOperator> oprIt = oprList.listIterator();
 
	while (termIt.hasNext()) {
		Term t = termIt.next();
		if(prfOpr!=null){
			prfOpr.prettyPrint();
		}
		t.prettyPrint();

		if(oprIt.hasNext()){
			TermOperator opr = oprIt.next();
			opr.prettyPrint();
		} 
	}
}
@Override void check(Block curScope, Library lib) {
	System.out.println("SimpleExpr check");
	for(Term t : termList){
		t.check(curScope,lib);
	}
}
@Override void genCode(CodeFile f){
	System.out.println("SimpleExpr genCode");
	ListIterator<Term> termIt = termList.listIterator();
    ListIterator<TermOperator> oprIt = oprList.listIterator();
	Term t = termIt.next();
	t.genCode(f);
	if(prfOpr!=null){
		prfOpr.genCode(f);
	}	
	while (oprIt.hasNext()) {

			f.genInstr("","pushl","%eax","");
			termIt.next().genCode(f);
			TermOperator opr = oprIt.next();
			opr.genCode(f);
	
	}	
}
}