package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public abstract class Operator extends PascalSyntax{
	 
	 Operator(int lNum){
	 	super(lNum);
	 }

	static Operator parse(Scanner s){
		//Operator opr = null;
		switch (s.curToken.kind) {
			case lessToken:
				System.out.println("Operator says : "+s.curToken.kind);
			case lessEqualToken:
			case notEqualToken:
			case greaterEqualToken:
			case greaterToken:
			case equalToken:
				break;
			case addToken:
				break;
			case subtractToken:
				break;
			case orToken:
				break;
		}

		return null;
	}

}