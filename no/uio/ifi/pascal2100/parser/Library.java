package no.uio.ifi.pascal2100.parser;
import no.uio.ifi.pascal2100.main.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class Library extends Block{
	ProcDecl  write = new ProcDecl("write",0);
	TypeDecl  integer = new TypeDecl("integer",0);
	TypeDecl  chr = new TypeDecl("char",0);
	TypeDecl  bool = new TypeDecl("boolean",0);
	ConstDecl eol = new ConstDecl("eol",0);
	EnumLiteral enum_true = new EnumLiteral("true",-1);
	EnumLiteral enum_false = new EnumLiteral("false",-1);

	public Library(int lNum){
	super(lNum);
	addDecl("write",write);
	addDecl("integer",integer);
	addDecl("char",chr);
	addDecl("boolean",bool);
	addDecl("eol",eol);
	addDecl("true",enum_true);
	addDecl("false",enum_false);
	}

@Override public void genCode(CodeFile f){
	f.genInstr("", ".extern write_char", "", "");
	f.genInstr("", ".extern write_int", "", "");
	f.genInstr("", ".extern write_string", "", "");
	/*
	f.genInstr("", ".extern _write_char", "", "");
	f.genInstr("", ".extern _write_int", "", "");
	f.genInstr("", ".extern _write_string", "", "");
	*/
}

}