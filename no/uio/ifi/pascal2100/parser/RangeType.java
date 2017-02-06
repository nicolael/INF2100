package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class RangeType extends Type{

	Constant constant1;
	Constant constant2;
	
	NumberLiteral nmb;
	int start;
	int limit;
	RangeType(int lNum){
		super(lNum);
	}
@Override public String identify() {
	return "<range-type> on line " + lineNum;
}

static RangeType parse(Scanner s){
	enterParser("range-type");
	System.out.println("range type 1: "+s.curToken.kind);
	RangeType range = new RangeType(s.curLineNum());
	range.constant1 = Constant.parse(s);
	System.out.println("range type 2: "+s.curToken.kind);
	s.skip(rangeToken);
	System.out.println("range type 3: "+s.curToken.id);
	range.constant2 = Constant.parse(s);	
	leaveParser("range-type");
	return range;
}
@Override public void prettyPrint() {
	
	constant1.prettyPrint();
	Main.log.prettyPrint("..");
	constant2.prettyPrint();
	
}
@Override void check(Block curScope, Library lib) {
	if(constant2 instanceof NamedConst){
		NamedConst nmd = (NamedConst)constant2;
		PascalDecl d = curScope.findDecl(nmd.value,this);
		if(constant1 instanceof NumberLiteral){
			NumberLiteral nr = (NumberLiteral)constant1;
			start = nr.value;
		}
		if(d!=null){
			if(d instanceof ConstDecl){
				ConstDecl cdecl = (ConstDecl)d;
				if(cdecl.konst instanceof NumberLiteral){
					nmb = (NumberLiteral)cdecl.konst;
					limit = nmb.value;
				}
			}
		}
	}
	
	constant1.check(curScope,lib);
	constant2.check(curScope,lib);
}
@Override void genCode(CodeFile f){

}
}