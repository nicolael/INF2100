package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class ParamDeclList extends PascalSyntax{

	ParamDecl paramDcl;
	LinkedList<ParamDecl> declList;

	ParamDeclList(int lNum){
		super(lNum);
		declList = new LinkedList<ParamDecl>();
	}

@Override public String identify() {
	return "<param decl list> on line " + lineNum;
}

static ParamDeclList parse(Scanner s){
	enterParser("param decl list");
	System.out.println("param decl list 1: "+s.curToken.id);
	s.skip(leftParToken);
	System.out.println("param decl list 2: "+s.curToken.id);
	ParamDeclList prmDecList = new ParamDeclList(s.curLineNum());
	prmDecList.paramDcl = ParamDecl.parse(s);

	//legger i listen
	prmDecList.declList.add(prmDecList.paramDcl);
	System.out.println("param decl list 3: "+s.curToken.id);
	while(s.curToken.kind==semicolonToken){
		s.skip(semicolonToken);
		prmDecList.paramDcl = ParamDecl.parse(s);
		//legger i listen
		prmDecList.declList.add(prmDecList.paramDcl);
	}
	s.skip(rightParToken);
	System.out.println("param decl list 4: "+s.curToken.id);
	leaveParser("param decl list");
	return prmDecList;
}
@Override public void prettyPrint() {
	ListIterator<ParamDecl> prmIt = declList.listIterator();
	Main.log.prettyPrint("(");
	while(prmIt.hasNext()){
		ParamDecl prm = prmIt.next();
		prm.prettyPrint();
		if(prmIt.hasNext()){
			Main.log.prettyPrint(";");
		}
	}
	Main.log.prettyPrint(")");

}
@Override void check(Block curScope, Library lib) {
	for(ParamDecl pr : declList){
		pr.check(curScope,lib);
	}

}
@Override void genCode(CodeFile f){
	
}
}


