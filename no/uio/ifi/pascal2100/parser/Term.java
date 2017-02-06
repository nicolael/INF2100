package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class Term extends PascalSyntax{
	Factor factor;
	FactorOperator opr;
	LinkedList<TokenKind> tokens;
	LinkedList<Factor> factorList;
	LinkedList<FactorOperator> oprList;

	Term(int lNum){
		super(lNum);
		tokens = new LinkedList<TokenKind>();
		factorList = new LinkedList<Factor>();
		oprList = new LinkedList<FactorOperator>();
		tokens.add(multiplyToken);
		tokens.add(divToken);
		tokens.add(modToken);
		tokens.add(andToken);
	}

@Override public String identify() {
	return "<expression> on line " + lineNum;
}
static Term parse(Scanner s){
	enterParser("term");
	Term term = new Term(s.curLineNum());
	System.out.println("Term class 1: "+s.curToken.kind);
	
	term.factor = Factor.parse(s);
	term.factorList.add(term.factor);
	System.out.println("Term class 2: "+s.curToken.kind);

	while(term.tokens.contains(s.curToken.kind)){
		term.opr = FactorOperator.parse(s);
		term.oprList.add(term.opr);
		System.out.println("Term class 3: "+s.curToken.kind);
		term.factor = Factor.parse(s);
		term.factorList.add(term.factor);
		System.out.println("Term class 4: "+s.curToken.kind);
	}
	
	leaveParser("term");
	return term;
}

@Override void prettyPrint() {
	ListIterator<Factor> factorIt = factorList.listIterator();
	ListIterator<FactorOperator> oprIt = oprList.listIterator();
    Main.log.prettyPrint("");  //factor.prettyPrint();
    while(factorIt.hasNext()){
    	Factor f = factorIt.next();
    	f.prettyPrint();
    	if(oprIt.hasNext()){
    		FactorOperator op = oprIt.next();
    		op.prettyPrint();
    	}
    }
}
@Override void check(Block curScope, Library lib) {
	System.out.println("Term check");
	for(Factor f : factorList){
		f.check(curScope,lib);
	}
}
@Override void genCode(CodeFile f){
	System.out.println("Term genCode");
	ListIterator<Factor> factorIt = factorList.listIterator();
	ListIterator<FactorOperator> oprIt = oprList.listIterator();
   	Factor fa = factorIt.next();
   	fa.genCode(f);

    while(oprIt.hasNext()){
    	f.genInstr("", "pushl","%eax", "");
    	factorIt.next().genCode(f);
    	oprIt.next().genCode(f);
    }
   
}

}