package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.ListIterator;

public class Block extends PascalSyntax {
	Program context;
	ConstDeclPart constant;
	VarDeclPart var;
	TypeDeclPart type;
	FuncDecl func;
	ProcDecl proc;
	StatmList statmList;
	HashMap<String,PascalDecl> decls = new HashMap<String,PascalDecl>();
	Block outerScope;
	LinkedList<PascalDecl> procList;
	LinkedList<PascalSyntax> pascSyntax;
	int level;
	int val=32;

	Block(int lNum){
		super(lNum);
		procList = new LinkedList<PascalDecl>();
		pascSyntax = new LinkedList<PascalSyntax>();
	}

@Override public String identify() {
	return "<block> on line " + lineNum;
}

static Block parse(Scanner s){
	enterParser("block");
	Block bl = new Block(s.curLineNum());

	if(s.curToken.kind == constToken){

		bl.constant = ConstDeclPart.parse(s);
		bl.pascSyntax.add(bl.constant);
	}
	if (s.curToken.kind == varToken){
		
		bl.var = VarDeclPart.parse(s);
		bl.pascSyntax.add(bl.var);
		
	}
	if(s.curToken.kind == typeToken){
		
		bl.type = TypeDeclPart.parse(s);
		bl.pascSyntax.add(bl.type);
		
	}

	while(s.curToken.kind!=beginToken){
		switch(s.curToken.kind){

			case functionToken:
				
				bl.func = FuncDecl.parse(s);
				bl.pascSyntax.add((PascalSyntax)bl.func);
				break;
			case procedureToken:
			
				bl.proc = ProcDecl.parse(s);
				bl.pascSyntax.add((PascalSyntax)bl.proc);
				break;
		}
	}
	s.skip(beginToken);
	bl.statmList = StatmList.parse(s);
	s.test(endToken);
	s.skip(endToken);
	leaveParser("block");
	return bl;
}

@Override void prettyPrint() {
     
    if(!pascSyntax.isEmpty()){
    	for(PascalSyntax pasc : pascSyntax){
    		pasc.prettyPrint();
    	}
    	Main.log.prettyPrintLn();
    }

    if(statmList!=null){

    	Main.log.prettyPrintLn("begin ");
    	Main.log.prettyIndent();
    	statmList.prettyPrint();
    	Main.log.prettyOutdent();
    }
    Main.log.prettyPrint("end"); 
}

void addDecl(String id, PascalDecl d) {
    if (decls.containsKey(id))
        d.error(id + " declared twice in same block!");
    decls.put(id, d);
}

PascalDecl findDecl(String id, PascalSyntax where) {

    PascalDecl d = decls.get(id);
    if (d != null) {
        Main.log.noteBinding(id, where, d);
		return d; 
	}
    if (outerScope != null)
            return outerScope.findDecl(id,where);
    
    where.error("Name " + id + " is unknown!");
        return null;  // Required by the Java compiler.
    
}
@Override void check(Block curScope, Library lib) {
	System.out.println("\nblock check");

	if (curScope == lib){
		level = 1;
	}else{
		level = curScope.level + 1;
	}

	for(PascalSyntax pasc : pascSyntax){
		if(pasc instanceof TypeDeclPart){
	    	TypeDeclPart typDeclPrt = (TypeDeclPart)pasc;
	    	for(TypeDecl typDcl : typDeclPrt.typeList ){
	    		addDecl(typDcl.name, typDcl);
	    	}
	    	typDeclPrt.check(this,lib);
	    }
	    if(pasc instanceof ConstDeclPart){
	    	ConstDeclPart constDeclPrt = (ConstDeclPart)pasc;
	    	for (ConstDecl cd: constDeclPrt.constList) {
	            addDecl(cd.name, cd);
	        }
	        constDeclPrt.check(this,lib);	
	    }
	    if(pasc instanceof VarDeclPart){
	    	VarDeclPart vrDeclPrt = (VarDeclPart)pasc;
	    	for(VarDecl v : vrDeclPrt.varList){
	    		addDecl(v.name, v);
	    		
	    	}
	    	vrDeclPrt.check(this,lib);
	    }
	    if(pasc instanceof ProcDecl){

	    	if(pasc instanceof FuncDecl){
	    		FuncDecl funcDecl = (FuncDecl)pasc;
	    		funcDecl.block.outerScope = this;
	    		addDecl(funcDecl.name, funcDecl);
	    		func.check(this,lib);	
	    	}else{
	    		ProcDecl procDcl = (ProcDecl)pasc;
	    		procDcl.block.outerScope = this;
	    		addDecl(procDcl.value, procDcl);
	    		procDcl.check(this,lib);
	    	}
	    }	
	}
	if(statmList!=null){
		statmList.check(this,lib);
	}

}

int declarations(){
	int cur_offsett=-36;

	if(var!=null){
	ListIterator<VarDecl> xprIt = var.varList.listIterator();

	while(xprIt.hasNext()){
		VarDecl v = xprIt.next();
		if(v.type instanceof ArrayType){
			ArrayType arr = (ArrayType)v.type;
			if(arr.type instanceof RangeType){
				RangeType rTyp = (RangeType)arr.type;
				cur_offsett += ((rTyp.limit)-(rTyp.start))*-4;
				v.declOffset = cur_offsett;
				val+=4;
				val+=((rTyp.limit)-(rTyp.start))*4;
				if(xprIt.hasNext()){
					xprIt.next().declOffset = cur_offsett-4;
					val+=4;
				}
			}
		}else{
			v.declOffset = cur_offsett;
			cur_offsett -=4;
			val +=4; 
		}
	}
}

	return val;
	
}
void func_proc_decl(CodeFile f){
	
	for(PascalSyntax pasc : pascSyntax){
		
		if(pasc instanceof ProcDecl){
			if(pasc instanceof FuncDecl){
				FuncDecl fDecl = (FuncDecl)pasc;
				fDecl.genCode(f);
			}else{
				System.out.println("PROSEDYRE_OPPDAGET");
				ProcDecl procDcl = (ProcDecl)pasc;
				procDcl.genCode(f);
			}
		}
	}
}

@Override void genCode(CodeFile f){
	
	if(statmList!=null){
	
		if(context!=null){
			f.genInstr("prog$"+context.label,"","","");
			f.genInstr("","enter","$" + declarations() + ",$"+level,"Start of "+context.name);

		}
		statmList.genCode(f);
	}
	//avslutter programmet
	if(context!=null){
		f.genInstr("","leave","","End of "+context.name);
		f.genInstr("","ret","","");
	}
}

}//end Block class