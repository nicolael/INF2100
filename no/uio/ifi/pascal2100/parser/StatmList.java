package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
import java.util.ListIterator;

class StatmList extends PascalSyntax{

	Statement st;
	LinkedList<Statement> stList; 

	StatmList(int lNum){
		super(lNum);
		stList = new LinkedList<Statement>();
	}

	@Override public String identify() {
		return "<statm list> on line " + lineNum;
	}

public static StatmList parse(Scanner s){
	enterParser("statm list");
	
	StatmList statmList = new StatmList(s.curLineNum());

	statmList.st = Statement.parse(s);
	statmList.stList.add(statmList.st);
	
	while(s.curToken.kind==semicolonToken){
		s.skip(semicolonToken);
		statmList.st = Statement.parse(s);
		statmList.stList.add(statmList.st); //lagrer i LinkedList
	}

	leaveParser("statm list");
	return statmList;
}
@Override void prettyPrint() {
	ListIterator<Statement> st = stList.listIterator();
	while(st.hasNext()){
		Statement s = st.next();
		s.prettyPrint();
		if(st.hasNext()){
			Main.log.prettyPrint(";");
			Main.log.prettyPrintLn();
		}
	}
	Main.log.prettyPrintLn();
	
}
@Override void check(Block curScope, Library lib) {
	System.out.println("statmList check");
	for(Statement s : stList){
		s.check(curScope,lib);
	}
}
@Override void genCode(CodeFile f){
	
	for(Statement s : stList){
		s.genCode(f);
	}
}	
}



