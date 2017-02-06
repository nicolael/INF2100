package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class ArrayType extends Type{

	Type type;
	Type type2;
	ArrayType(int lNum){
		super(lNum);	
	}

	@Override public String identify() {
		return "<array-type> on line " + lineNum;
	}

	static ArrayType parse(Scanner s){
		enterParser("array-type");
		System.out.println("array type 1: "+s.curToken.kind);
		s.skip(arrayToken);
		ArrayType arrType = new ArrayType(s.curLineNum());
		s.skip(leftBracketToken);
		System.out.println("array type 2: "+s.curToken.kind);
		arrType.type = Type.parse(s);
		System.out.println("array type 3: "+s.curToken.kind);
		s.skip(rightBracketToken);
		s.skip(ofToken);
		arrType.type2 = Type.parse(s);
		leaveParser("array-type");
		return arrType;
	}
	@Override public void prettyPrint() {

	    Main.log.prettyPrint("array [");
	    type.prettyPrint();
	    Main.log.prettyPrint("] of ");
	    type2.prettyPrint();
	}
	@Override void check(Block curScope, Library lib) {
		if(type!=null){
			type.check(curScope,lib);
		}
		if(type2!=null){
			type2.check(curScope,lib);
		}
	}
@Override void genCode(CodeFile f){

	System.out.println("monica "+type.identify());
	System.out.println("monica "+type2.identify());
	if(type2 instanceof TypeName){
		TypeName t = (TypeName)type2;
		System.out.println("-->"+t.type);
		System.out.println("-->"+t.typeRef.name);
	}
}
}