/**
 * Analisador léxico para expressões simples
 */
package compiler.scanner;

%%

%public
%class Scanner
%unicode
%type Token

%yylexthrow Exception

%eofval{
    return new Token(Tag.EOF, yyline, yycolumn);
%eofval}

%line
%column

%eof{
    System.out.println("Análise léxica terminada com sucesso!");
%eof}


delim   = [\ \t\n]
ws      = {delim}+
digit	= [0-9]
number	= {digit}+(\.{digit}+)?([Ee][+-]?{digit}+)?
letter  = [A-Za-z]
id      = {letter}({letter}|{digit})*
%%

{ws}		{ /* ignorar */ }
{number}	{ return new Number(Double.parseDouble(yytext()),
                                    yyline, yycolumn); }
{id}            {return new Id(yytext(), yyline, yycolumn); }
"+"             {return new Token(Tag.PLUS, yyline, yycolumn);}
"-"             {return new Token(Tag.MINUS, yyline, yycolumn);}
"*"             {return new Token(Tag.TIMES, yyline, yycolumn);}
"/"             {return new Token(Tag.DIV, yyline, yycolumn);}
"("             {return new Token(Tag.LPAREN, yyline, yycolumn);}
")"             {return new Token(Tag.RPAREN, yyline, yycolumn);}
"%"             {return new Token(Tag.MOD, yyline, yycolumn);}
";"             {return new Token(Tag.EOL, yyline, yycolumn);}
.		        { throw new Exception("Scanner: símbolo ilegal <" + yytext() +
                    "(" + (int)(yytext().charAt(0)) + ")" + ">"); }
