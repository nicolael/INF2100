package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public abstract class Type extends PascalSyntax{
	
	Type(int lNum){
		super(lNum);
	}

@Override public String identify() {
	return "<type> on line " + lineNum;
}
	
	static Type parse(Scanner s){
		enterParser("type");
		Type t = null;
		switch(s.curToken.kind){
			case nameToken: //name
				if(s.nextToken.kind==rangeToken){
					//range constant
					t = RangeType.parse(s);
				}else{
					System.out.println("Type says : "+s.curToken.kind);
					t = TypeName.parse(s);
				}
				break;
			case leftParToken: //enum type
				break;
			case arrayToken: //arraytype
				t = ArrayType.parse(s);
				break;
			case intValToken:
				//range contant
				if(s.nextToken.kind==rangeToken){
					t = RangeType.parse(s);
				}
				break;
			case stringValToken:
				//range contant
				if(s.nextToken.kind==rangeToken){
					t = RangeType.parse(s);
				}
				break;
		}
		leaveParser("type");
		return t;
	}
}
