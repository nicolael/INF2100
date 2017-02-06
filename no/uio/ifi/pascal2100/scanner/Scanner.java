package no.uio.ifi.pascal2100.scanner;

import no.uio.ifi.pascal2100.main.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Scanner {
    public Token curToken = null, nextToken = null; 

    private LineNumberReader sourceFile = null;
    private String sourceFileName, sourceLine = "";
    private int sourcePos = 0;

    String test="";

    public Scanner(String fileName) {
	sourceFileName = fileName;
	System.out.println("Reading from file : "+sourceFileName);
	try {
	    sourceFile = new LineNumberReader(new FileReader(fileName));
	  
	} catch (FileNotFoundException e) {
	    Main.error("Cannot read " + fileName + "!");
	}
	readNextToken();  readNextToken();

    }

    public String identify() {
	return "Scanner reading " + sourceFileName;
    }


    public int curLineNum() {
	return curToken.lineNum;
    }

    
    private void error(String message) {
	Main.error("Scanner error on line " + curLineNum() + ": " + message);
    }

	/*
	Leser filen linje for linje, hver linje blir splittet om til en char array som analyseres
	tegn for tegn. Når vi treffer et mellomrom, operatør symbol, eller spesielle symboler så bestemmer 
	metoden at et ord eller en token er fullført.
	*/
    int pos;
    boolean finish=true;
    public void readNextToken() {
	// Del 1 her
    	curToken = nextToken; 
        nextToken=null;
    
        if(sourceFile==null){
            nextToken=new Token(eofToken,getFileLineNum());
            Main.log.noteToken(nextToken);
        }else{

            if(finish){
                readNextLine();
                pos=0;
            }
  
            String nameTokens=""; //plassholder for name tokens og number tokens
            String str=""; //plassholder for text string tokens
            String sign=""; //plassholder for symboler som + = [ ( / * @ > < ...osv
  
            char [] token = sourceLine.toLowerCase().toCharArray(); //Splitter linjen vi leser inn om til chars
            
            //System.out.println("\n* "+sourceLine+" *\n"); //kun til test formål
            
            for(int i=pos;i<token.length;i++){
                
                while(Character.toString(token[i]).matches("[a-zA-Z0-9]")) { 
                    nameTokens += token[i]; //legg til symbol i strengen nameTokens
                    if(i == (token.length-1)){ 
                        break;
                    }
                    i++;
                }
                 
                if(!nameTokens.equals("")){  

                    if(nameTokens.matches("(\\d+)")){
                        int number = Integer.parseInt(nameTokens);
                        nextToken = new Token(number,getFileLineNum()); //oppretter number tokens
                        Main.log.noteToken(nextToken);
                        //break;
                            
                    }else if(nameTokens.matches("(\\w+)")){
                        //System.out.println(nameTokens);
                        nextToken=new Token(nameTokens,getFileLineNum()); //oppretter name tokens
                        Main.log.noteToken(nextToken);
                    }
                    if(i<token.length){
                        pos=i;
                        finish=false;
                        break;
                    }
                    //nameTokens=""; //nullstiller det token vi har opprettet, slik at vi kan få inn nye

                }
                
                if(i+1<token.length&&token[i]=='/'&&token[i+1]=='*') {
                    i=i+2;
                    if(i==(token.length-1)){
                        readNextLine();
                        token = sourceLine.toCharArray();
                        i=0;
                    }
                    boolean comment = true;
                    while(comment){
                        if(token[i]=='*'&&token[i+1]=='/'){
                            break;
                        }
                        i++;
                        if(i==(token.length-1)&&token[i]!='/'){ 
                        //hvis kommentarer strekker seg over flere linjer, så hopper vi til neste linje
                        readNextLine();
                        token = sourceLine.toCharArray();
                        i=0;
                        }
                    }
                    i=i+2;
                }
        
                if(i>=token.length-1){
                    finish=true;
                    pos=0;
                    break;
                }
            
                if(i+1<token.length&&token[i]=='{') {
                    while(token[i]!='}'){
                        i++;
                        if(i==(token.length-1)&&token[i]!='}'){
                            readNextLine();
                            token = sourceLine.toCharArray();
                            i=0;
                        }
                    }
                    i=i+1;
                }

                if(i>=token.length-1){
                    finish=true;
                    pos=0;
                    break;
                }

                if(i<token.length){

                    if(token[i]=='\''){
                        i++;
                        if(i>=0&&i+1<token.length){
                            while(i<token.length&&token[i]!='\''){
                                str+=token[i];
                                i++;
                            }
                        }
                        nextToken = new Token("",str,getFileLineNum()); //oppretter tekst strenger
                        Main.log.noteToken(nextToken);
                        if(i<token.length){
                            pos=i;
                            pos++;
                            finish=false;
                            break;
                        }              
                    }
                    if(i<token.length&&isSign(token[i])) {

                        if(i+1<token.length){
                            if(token[i]=='<'&&token[i+1]=='='){
                                nextToken=new Token(lessEqualToken,getFileLineNum());
                                Main.log.noteToken(nextToken);
                                i++;
                            }else if(token[i]=='>'&&token[i+1]=='='){
                                nextToken=new Token(greaterEqualToken,getFileLineNum());
                                Main.log.noteToken(nextToken);
                                i++;
                            }else if(token[i]==':'&&token[i+1]=='='){
                                nextToken=new Token(assignToken,getFileLineNum());
                                Main.log.noteToken(nextToken);
                                i++;
                            }else if(token[i]=='.'&&token[i+1]=='.'){
                                nextToken=new Token(rangeToken,getFileLineNum());
                                Main.log.noteToken(nextToken);
                                i++;
                            }else if(token[i]=='<'&&token[i+1]=='>'){
                                nextToken=new Token(notEqualToken,getFileLineNum());
                                Main.log.noteToken(nextToken);
                                i++;
                            }else{
                                sign = Character.toString(token[i]);
                                nextToken=new Token(sign,getFileLineNum());
                                Main.log.noteToken(nextToken);
                            }
                            if(i<token.length){
                                pos=i;
                                pos++;
                                //curToken = nextToken;
                                finish=false;
                                break;
                            }
                        }else{//pa slutten av linja?
                            sign =  Character.toString(token[i]);
                            nextToken=new Token(sign,getFileLineNum());
                           
                            Main.log.noteToken(nextToken);
                        }         
                    }
                    pos++;
                }
            } //slutt for-loop som leser char for char
            if(pos==token.length){
                finish=true;
            }

        } //slutt else;

        if(nextToken==null){
            //curToken = nextToken;
            nextToken=curToken;
            readNextToken();
        }else{
            //curToken = nextToken;
            //nextToken=curToken;
            //System.out.println("kind : "+nextToken.kind);
        }
    } //slutt på metoden readNextToken

    private void readNextLine() {
    	if (sourceFile != null) {
    	    try {
    		sourceLine = sourceFile.readLine();
    		if (sourceLine == null) {
    		   	sourceFile.close();  sourceFile = null;
    		    sourceLine = "";  
    		} else {

    		    sourceLine += " ";

    		}
    		sourcePos = 0; //Når bruker vi denne?
    	    } catch (IOException e) {
    		Main.error("Scanner error: unspecified I/O error!");
    	    }
    	}
    	if (sourceFile != null) 
    	    Main.log.noteSourceLine(getFileLineNum(), sourceLine);
    }


    private int getFileLineNum() {
	return (sourceFile!=null ? sourceFile.getLineNumber() : 0);
    }


    // Character test utilities:
    private boolean isLetterAZ(char c) {
	return 'A'<=c && c<='Z' || 'a'<=c && c<='z';
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }
    private boolean isSign(char c){
    return Character.toString(c).matches("[^a-zA-Z0-9^\\s^\\']");
    }

    // Parser tests:
    public void test(TokenKind t) {
	if (curToken.kind != t)
	    testError(t.toString());
    }

    public void testError(String message) {
	Main.error(curLineNum(), 
		   "Expected a " + message +
		   " but found a " + curToken.kind + "!");
    }

    public void skip(TokenKind t) {
	test(t);  
	readNextToken();
    }
}
