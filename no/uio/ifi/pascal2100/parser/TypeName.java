package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class TypeName extends Type{

	String type;
	TypeDecl typeRef;
	PascalDecl dh;

	TypeName(String type,int lNum){
		super(lNum);
		this.type = type;
	}

	@Override public String identify() {
		return "<type name> on line " + lineNum;
	}

	static TypeName parse(Scanner s){
		enterParser("type name");
		System.out.println("Type name says : "+s.curToken.id);
		s.test(nameToken);
		TypeName typ = new TypeName(s.curToken.id,s.curLineNum());
		s.skip(nameToken);
		System.out.println("Type name says : "+s.curToken.kind);
		leaveParser("type name");
		return typ;
	}

	@Override public void prettyPrint() {
	    Main.log.prettyPrint(type);
	  
	}
@Override void check(Block curScope, Library lib) {
	System.out.println("TypeName");
	System.out.println("'"+type+"'");
	switch(type){
		case "write":
		case "integer":
		case "boolean":
		case "char":
		case "eol":
			PascalDecl d = lib.findDecl(type,this);
			typeRef = (TypeDecl)d;
			break;
		default :
			PascalDecl t = curScope.findDecl(type,this);
			typeRef = (TypeDecl)t;
			break;
	}
}
@Override void genCode(CodeFile f){
	System.out.println("TypeName genCode "+type);
		
		switch(type){
			case "integer":
				f.genInstr("","call","write_int","");
				break;
			case "bool":
				f.genInstr("","call","write_int","");
				break;
			case "char":
				f.genInstr("","call","write_char","");
				break;
			case "eol":
				f.genInstr("","call","write_char","");
				break;
		}

		//i tillfelle vi intreffer en typeDecl int = integer;
		if(typeRef.typeName!=null){
			switch(typeRef.typeName.type){
				case "integer":
					f.genInstr("","call","write_int","");
					break;
				case "bool":
					f.genInstr("","call","write_int","");
					break;
				case "char":
					f.genInstr("","call","write_char","");
					break;
				case "eol":
					f.genInstr("","call","write_char","");
					break;
			}
		}
}

}