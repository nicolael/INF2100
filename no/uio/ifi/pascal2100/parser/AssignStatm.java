package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

class AssignStatm extends Statement{
	Expression expr;
	Variable var;
	int level;
	Block b;
	AssignStatm(int lNum){
		super(lNum);
	}

	@Override public String identify() {
		return "<assign statm> on line " + lineNum;
	}

	static AssignStatm parse(Scanner s){
		enterParser("assign statm");
		AssignStatm as = new AssignStatm(s.curLineNum());
		System.out.println("AssignStatm says 1: "+s.curToken.id);
		as.var = Variable.parse(s);
		System.out.println("AssignStatm says 2: "+s.curToken.kind);
		s.skip(assignToken);
		System.out.println("AssignStatm says 3: "+s.curToken.kind);
		as.expr = Expression.parse(s);
		leaveParser("assign statm");
		return as;
	}

	@Override void prettyPrint() {
	
	    var.prettyPrint(); Main.log.prettyPrint(" := ");
	    expr.prettyPrint();
	    
	}
@Override void check(Block curScope, Library lib) {
	System.out.println("assignstatm check");
	level = curScope.level;
	var.check(curScope,lib);
	System.out.println(var.value + " var");
	expr.check(curScope,lib);
	b = curScope;
}
@Override void genCode(CodeFile f){
	System.out.println("AssignStatm genCode "+var.value);
	expr.genCode(f);

	if(var.d instanceof VarDecl){
		System.out.println("-----------> VARDECL");
		int result = -4*var.level;
		VarDecl vDecl = (VarDecl)var.d;

		if(vDecl.type instanceof ArrayType){
			Type tp = (Type)vDecl.type;
			int nivaa = -4*vDecl.level;
			f.genInstr("","pushl","%eax","");
			var.expr.genCode(f);
	
			ArrayType arr = (ArrayType)vDecl.type;
			if(arr.type instanceof RangeType){
				RangeType rTyp = (RangeType)arr.type;
				vDecl.declOffset = (rTyp.limit) * 4;
				vDecl.declOffset +=32;
			}
			int arr_offset = vDecl.declOffset+nivaa;			
			f.genInstr("","subl","$2,%eax","");
			f.genInstr("","movl",nivaa+"(%ebp),%edx","");
			f.genInstr("","leal","-"+arr_offset+"(%edx),%edx","");
			f.genInstr("","popl","%ecx","");
			f.genInstr("","movl","%ecx,0(%edx,%eax,4)",vDecl.name+"[x] :=");

		}else{
			f.genInstr("","movl",result+"(%ebp),%edx","");
			f.genInstr("","movl","%eax,"+vDecl.declOffset+"(%edx)",var.value+" :=");
		}
	}else if(var.d instanceof FuncDecl){
		f.genInstr("", "movl","%eax,-32(%ebp)", var.value+" :=");
	}
}
}