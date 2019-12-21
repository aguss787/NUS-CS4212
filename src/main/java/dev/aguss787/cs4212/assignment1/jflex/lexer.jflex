/* JFlex example: partial Java language lexer specification */
package dev.aguss787.cs4212.assignment1.jflex;

import dev.aguss787.cs4212.assignment1.cup.Symbols;
import java_cup.runtime.Symbol;
import java_cup.sym;

/**
 * This class is a simple example lexer.
 */
%%

%class Lexer
%public
%unicode
%cup
%line
%column

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

Identifier = [a-z] [a-zA-Z0-9_]*
Cname = [A-Z] [a-zA-Z0-9_]*

DecIntegerLiteral = 0 | [1-9][0-9]*

%state STRING
%state COMMENT

%%

/* keywords */
<YYINITIAL> "class"            { return symbol(Symbols.CLASS); }
//<YYINITIAL> "break"              { return symbol(sym.BREAK); }

<YYINITIAL> {
  /* reserved words */
  "return"                       { return symbol(Symbols.RETURN); }
  "this"                         { return symbol(Symbols.THIS); }
  "null"                         { return symbol(Symbols.NULL); }
  "new"                          { return symbol(Symbols.NEW); }
  "true"                         { return symbol(Symbols.TRUE); }
  "false"                        { return symbol(Symbols.FALSE); }
  "if"                           { return symbol(Symbols.IF); }
  "else"                         { return symbol(Symbols.ELSE); }
  "while"                        { return symbol(Symbols.WHILE); }
  "println"                      { return symbol(Symbols.PRINTLN); }
  "readln"                       { return symbol(Symbols.READLN); }


  /* data type */
  "Int"                          { return symbol(Symbols.TYPE_INT, yytext()); }
  "Bool"                         { return symbol(Symbols.TYPE_BOOL, yytext()); }
  "String"                       { return symbol(Symbols.TYPE_STRING, yytext()); }
  "Void"                         { return symbol(Symbols.TYPE_VOID, yytext()); }

  /* identifiers */
  {Identifier}                   { return symbol(Symbols.IDENTIFIER, new String(yytext())); }
  {Cname}                        { return symbol(Symbols.CNAME, new String(yytext())); }

  /* literals */
  {DecIntegerLiteral}            { return symbol(Symbols.INTEGER_LITERAL, new Integer(yytext())); }
  \+                             { return symbol(Symbols.PLUS); }
  \"                             { string.setLength(0); yybegin(STRING); }
  \{                             { return symbol(Symbols.LSQBR); }
  \}                             { return symbol(Symbols.RSQBR); }
  \(                             { return symbol(Symbols.LPAREN); }
  \)                             { return symbol(Symbols.RPAREN); }
  \.                             { return symbol(Symbols.DOT); }
  \,                             { return symbol(Symbols.COMMA); }
  \;                             { return symbol(Symbols.SEMI); }

  "/*"                           { yybegin(COMMENT); }

  /* operators */
  "&&"                           { return symbol(Symbols.AND); }
  "||"                           { return symbol(Symbols.OR); }
  "!"                            { return symbol(Symbols.NEG); }

  "="                            { return symbol(Symbols.EQ); }
  "+"                            { return symbol(Symbols.PLUS); }
  "-"                            { return symbol(Symbols.MINUS); }
  "*"                            { return symbol(Symbols.TIMES); }
  "/"                            { return symbol(Symbols.DIVIDE); }

  "=="                           { return symbol(Symbols.EQEQ); }
  "!="                           { return symbol(Symbols.NEQ); }
  "<="                           { return symbol(Symbols.LTQ); }
  "<"                            { return symbol(Symbols.LT); }
  ">="                           { return symbol(Symbols.GTQ); }
  ">"                            { return symbol(Symbols.GT); }


  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<COMMENT> {
    "*/" { yybegin(YYINITIAL); }
    [.]  {}
}

<STRING> {
  \"                             { yybegin(YYINITIAL);
                                   return symbol(Symbols.STRING_LITERAL,
                                   string.toString()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                        yytext()+">"); }