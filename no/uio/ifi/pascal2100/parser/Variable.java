package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class Variable extends Factor{

	Expression expr;
	String value;
	int level;
	Block bl;
	PascalDecl d;
	

	Variable(String value,int lNum){
		super(lNum);
		this.value = value;
	}
	@Override public String identify() {
		return "<variable> on line " + lineNum;
	}

	static Variable parse(Scanner s){
		enterParser("variable");
		Variable var = new Variable(s.curToken.id,s.curLineNum());
		System.out.println("Variable says : "+s.curToken.id);
		s.skip(nameToken);

		if(s.curToken.kind==leftBracketToken){
			s.skip(leftBracketToken);
			var.expr = Expression.parse(s);
			s.skip(rightBracketToken);
		}

		System.out.println("Variable says %: "+s.curToken.kind);
		leaveParser("variable");
		return var;
	}

	@Override void prettyPrint() {
	    Main.log.prettyPrint(value);
	    if(expr!=null){
	    	Main.log.prettyPrint("[");
	    	expr.prettyPrint();
	    	Main.log.prettyPrint("]");
	    }  
	}
@Override void check(Block curScope, Library lib) {
	System.out.println("variable check");
	bl = curScope;
	level = curScope.level;
	d=null;
	if(value.equals("eol")||value.equals("true")||value.equals("false")){
		d = lib.findDecl(value,this);
	}else{
		d = curScope.findDecl(value,this);
	}
	if(expr!=null){
		expr.check(curScope,lib);
	}

}
@Override void genCode(CodeFile f){
	System.out.println("Variable GENCODE "+value);
	int result = -4*level;
	
	if(!value.equals("eol")&&!value.equals("true")&&!value.equals("false")){
		System.out.println(value);
		if(d instanceof ParamDecl){
			ParamDecl param = (ParamDecl)d;
			f.genInstr("","movl",result+"(%ebp),%edx","");
			f.genInstr("","movl",param.declOffset+"(%edx),%eax"," "+value);
		}else if(d instanceof VarDecl){
			VarDecl v = (VarDecl)d;
			if(v.type instanceof ArrayType){
				ArrayType arr = (ArrayType)v.type;
				int lev=-4*v.level;
				expr.genCode(f);
				f.genInstr("","subl","$2,%eax","");
				f.genInstr("","movl",lev+"(%ebp),%edx","");
				f.genInstr("","leal","-"+(v.declOffset-4)+"(%edx),%edx","");
				f.genInstr("","movl","0(%edx,%eax,4),%eax",v.name+"[...]");
			}else{
				f.genInstr("","movl",result+"(%ebp),%edx","");
				f.genInstr("","movl",v.declOffset+"(%edx),%eax",""+value);
			}

		}else if(d instanceof ConstDecl){
			System.out.println("Constant");
			ConstDecl c = (ConstDecl)d;
			c.konst.genCode(f);
		}else if(d instanceof FuncDecl){
			System.out.println("FuncDecl******************");
			f.genInstr("", "movl","%eax,-32(%ebp)", value+" :=");
		}
	}
	if( d instanceof EnumLiteral){
		EnumLiteral en = (EnumLiteral)d;
		d.genCode(f);
	}
}
}