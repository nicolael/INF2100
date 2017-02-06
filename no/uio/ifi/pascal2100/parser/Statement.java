package no.uio.ifi.pascal2100.parser;
//import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;


public abstract class Statement extends PascalSyntax {
	
	Statement(int lNum){
		super(lNum);
	}

	static Statement parse(Scanner s){
		enterParser("statement");
		Statement st =null;
		System.out.println("Statement says : "+s.curToken.id);
		switch(s.curToken.kind){
			case beginToken:
				st = CompoundStatm.parse(s); break; 
			case ifToken:
				st = IfStatm.parse(s); break;
			case nameToken:
				switch (s.nextToken.kind) {
					case assignToken:
					case leftBracketToken:
						System.out.println("Statement -> assign: "+s.curToken.kind);
						st = AssignStatm.parse(s); break;
					default:
					System.out.println("Statement -> Proc call: "+s.curToken.kind);
					st= ProcCallStatm.parse(s); break;
				}break;
			case whileToken:
			 	st = WhileStatm.parse(s); break;
			default :
				st = EmptyStatm.parse(s); break;
		}
		leaveParser("statement");
		return st;
	}
}