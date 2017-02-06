package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Collections;

public class ProcCallStatm extends Statement{
	Expression expr;
	ProcDecl procRef;
	LinkedList<Expression> exprList;
	String name;
	String label;
	
	ProcCallStatm(String name, int lNum){
		super(lNum);
		this.name = name;
		exprList = new LinkedList<Expression>();
	}
	@Override public String identify() {
		return "<ProcCallStatm> on line " + lineNum;
	}

	static ProcCallStatm parse(Scanner s){
		enterParser("proc call");
		s.test(nameToken);
		System.out.println("Proc call says 1: "+s.curToken.id);
		ProcCallStatm procStm = new ProcCallStatm(s.curToken.id, s.curLineNum());
		s.readNextToken();
		System.out.println("Proc call says 2: "+s.curToken.id);
		
		if(s.curToken.kind==leftParToken){
			s.skip(leftParToken);
			
			procStm.expr = Expression.parse(s);
			procStm.exprList.add(procStm.expr);
			System.out.println("Proc call says 3: "+s.curToken.kind);
		
			//sjekker om det kommer flere args
			while(s.curToken.kind==commaToken){
				s.skip(commaToken);
				procStm.expr = Expression.parse(s);
				procStm.exprList.add(procStm.expr);
			}

			s.skip(rightParToken);
		}

		System.out.println("Proc call says  6: "+s.curToken.kind);
		System.out.println("Proc call says  7: "+s.curToken.kind);
		leaveParser("proc call");
		return procStm;	
	}
	@Override void prettyPrint() {
		ListIterator<Expression> exprIt = exprList.listIterator();
		Main.log.prettyPrint(name);  
		if(expr!=null){
			Main.log.prettyPrint("("); 
			while(exprIt.hasNext()){
				Expression xpr = exprIt.next();
				xpr.prettyPrint();
				if(exprIt.hasNext()){
					Main.log.prettyPrint(", ");
				}
			}
        	Main.log.prettyPrint(")"); 
    	}
	}

@Override void check(Block curScope, Library lib) {
	System.out.println("ProcCallStatm check");
	PascalDecl d=null;

	if(name.equals("write")){
		d = lib.findDecl(name,this);
	}else{
		d = curScope.findDecl(name,this);
	}
	for(Expression xpr :exprList){
		xpr.check(curScope,lib);
	}
	procRef = (ProcDecl)d;
}
@Override void genCode(CodeFile f){
	System.out.println("proc call genCode");
	
	if(!name.equals("write")) Collections.reverse(exprList);
	
	ListIterator<Expression> xprIt = exprList.listIterator();
	int pushedParams = exprList.size();
	//antall bytes som skal fjernes fra stemmen
	int paramBytes = exprList.size() * 4;
	
	int paramTeller = 0;
	int popParam = 4;

	while(xprIt.hasNext()){
		paramTeller++;
		Expression xpr = xprIt.next();
		xpr.genCode(f);
		//paramBytes -=4;

		if(xpr.simpExpr.term.factor instanceof CharLiteral){
			if(name.equals("write")){
				f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
				f.genInstr("","call","write_char","");
				f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
			}
		}else if(xpr.simpExpr.term.factor instanceof NumberLiteral){
			if(name.equals("write")){
				f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
				f.genInstr("","call","write_int","");
				f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
			}
		}else if(xpr.simpExpr.term.factor instanceof Negation){
			if(name.equals("write")){	
				f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
				f.genInstr("","call","write_int","");
				f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
			}
		}else if(xpr.simpExpr.term.factor instanceof StringLiteral){
			StringLiteral str= (StringLiteral)xpr.simpExpr.term.factor;
			String testLabel = f.getLocalLabel();
			
			f.genInstr("",".data","","");
			f.genInstr(testLabel,".asciz","\""+str.value+"\"","");
			f.genInstr("",".align","2","");
			f.genInstr("",".text","","");
		
			f.genInstr("","leal",testLabel+",%eax","Addr(\""+str.value+"\")");

			if(name.equals("write")){
				f.genInstr("","pushl","%eax"," Push param #"+paramTeller+".");
				f.genInstr("","call","write_string","");
			}
			f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
		}else if(xpr.simpExpr.term.factor instanceof Variable){
			Variable v = (Variable)xpr.simpExpr.term.factor;
			System.out.println("Variable ------>"+v.value);

			if(v.d!=null){
				System.out.println("test : "+v.d.identify() +" Proccall");
				if(v.d instanceof ConstDecl){
					ConstDecl cDecl = (ConstDecl)v.d;
					System.out.println("ConstDecl");
					System.out.println("konst name --> "+cDecl.name);
					if(cDecl.konst instanceof NumberLiteral){
						if(name.equals("write")){
							f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
							f.genInstr("","call","write_int","");
							f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
						}	
					}else if(cDecl.konst instanceof StringLiteral){
						if(name.equals("write")){
							f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
							f.genInstr("","call","write_string","");
							f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
						}
					}else if(cDecl.konst instanceof CharLiteral){
						if(name.equals("write")){
							f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
							f.genInstr("","call","write_char","");
							f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
						}
					}else if(cDecl.name.equals("eol")){
						f.genInstr("","movl","$10,%eax","char 10");
						if(name.equals("write")){
							f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
							f.genInstr("","call","write_char","");
							f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
						}
					}

					}if(v.d instanceof VarDecl){
						System.out.println("VarDecl");
						VarDecl vDecl = (VarDecl)v.d;
						if(vDecl.type instanceof TypeName){
							TypeName typ = (TypeName)vDecl.type;
							System.out.println("---"+typ.type+"---");
							if(name.equals("write")){
								f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
								typ.genCode(f);
								f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");
							}
						}
					}if(v.d instanceof ParamDecl){
						System.out.println("ParamDecl");
						ParamDecl prm = (ParamDecl)v.d;
						f.genInstr("","pushl","%eax","Push param #"+paramTeller+".");
						prm.genCode(f);
						f.genInstr("","addl","$"+popParam+",%esp","Pop parameter.");

					}
				}
			}
		if(!name.equals("write")){
			f.genInstr("","pushl","%eax"," Push param #"+pushedParams+".");
			pushedParams--;

		}
	
		if(xprIt.hasNext()==false&&!name.equals("write")){ //ingen flere expressions
			f.genInstr("", "call","proc$"+procRef.label, "");
			f.genInstr("","addl","$"+paramBytes+",%esp","Pop parameters.");
		}
	
	}
	if(!name.equals("write")&& exprList.isEmpty()){
		f.genInstr("", "call","proc$"+procRef.label, "");
	}
}

}