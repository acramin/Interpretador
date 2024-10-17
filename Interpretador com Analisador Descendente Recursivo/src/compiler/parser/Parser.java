package compiler.parser;

import compiler.scanner.Scanner;
import compiler.scanner.Tag;
import compiler.scanner.Token;

import java.util.Stack;
import compiler.scanner.Number;

public class Parser {
    // referência a um scanner criado separadamente pelo JFlex
    private final Scanner scanner;
    // token atualmente obtido pelo Scanner
    private Token token;

    // Pilha
    private Stack<Double> stack = new Stack<>();

    // classe do analisador sintático
    public Parser(Scanner scanner) throws Exception {
        // se não tem um scanner, então não é possível continuar
        if (scanner == null) {
            throw new Exception("Scanner não definido! Não foi possível criar o Parser!");
        }
        // armazena a referência
        this.scanner = scanner;
        // e obtém o primeiro token
        token = scanner.yylex();
    }

    // função para tratar erros
    private void error(Tag tag) throws Exception {
        throw new Exception("Erro durante a analise sintatica: esperava marca com tag " + tag +
                " mas recebi marca com tag " + token.tag + "\n" +
                "Linha: " + token.line + " e Coluna: " + token.column);
    }

    private void error(Tag[] tags) throws Exception {
        String msg = "Erro durante a analise sintatica: esperava marcas com tags [ ";
        for (Tag t : tags) {
            msg += t;
            msg += ' ';
        }
        msg += ']';
        msg += " mas recebi marca com tag " + token.tag + "\n" +
                "Linha: " + token.line + " e Coluna: " + token.column;
        throw new Exception(msg);
    }

    // função que verifica a marca atual e avança na entrada
    private void accept(Tag tag) throws Exception {
        // Se o token atual é o que se está esperando
        if (token.tag == tag) {
            // avança um token na entrada
            token = scanner.yylex();
        } else {
            // gera uma exceção
            error(tag);
        }
    }

    // função que inicia a analise sintática descendente
    // recursiva
    public void parse() throws Exception {
        // REGRA: goal = expr;
        program();
        // se, depois de expr() o token for EOF, tudo deu certo!
        if (token.tag == Tag.EOF) {
            System.out.println("Analise sintatica terminada com sucesso!");
        } else {
            error(Tag.EOF);
        }
    }

    // REGRA: program = expr, ';', { expr, ';' };
    private void program() throws Exception {
        do{
            expr();
            System.out.println("valor: " + stack.pop());
            accept(Tag.EOL);
        } while (token.tag != Tag.EOF);
    }


    // REGRA: expr = term, eprime;
    private void expr() throws Exception {
        term();
        eprime();
    }

    // REGRA: eprime = { ('+' | '-'), term };
    private void eprime() throws Exception {
        while (token.tag == Tag.PLUS || token.tag == Tag.MINUS) {
            Tag operator = token.tag;  // Armazena o operador
            accept(token.tag);
            term();
            double valor2 = stack.pop();
            double valor1 = stack.pop();
            if(operator == Tag.PLUS){
                stack.push(valor1 + valor2);
                //System.out.println(valor1 + " + " + valor2 + " = " + (valor1 + valor2));
                //System.out.println("soma");
            }else if(operator == Tag.MINUS){
                stack.push(valor1 - valor2);
                //System.out.println(valor1 + " - " + valor2 + " = " + (valor1 - valor2));
                //System.out.println("menos");
            }          
        }
    }

    // REGRA: term = factor, tprime;
    private void term() throws Exception {
        factor();
        tprime();
    }

    // REGRA: tprime = { ( '*' | '/' ), factor };
    private void tprime() throws Exception {
    while   (   token.tag == Tag.TIMES || token.tag == Tag.DIV ) {
            Tag operator = token.tag;  // Armazena o operador
            accept(token.tag);
            factor();
            double valor2 = stack.pop();
            double valor1 = stack.pop();
            if(operator== Tag.TIMES){
                stack.push(valor1 * valor2);
                //System.out.println(valor1 + " * " + valor2 + " = " + (valor1 * valor2));
                //System.out.println("multiplicacao");
            } else if(operator == Tag.DIV){
                stack.push(valor1 / valor2);
                //System.out.println(valor1 + " / " + valor2 + " = " + (valor1 / valor2));
                //System.out.println("divisao");
            }
        }
    }

    // REGRA: factor = '(', expr, ')' | '-', factor | number;
private void factor() throws Exception {
    double valor;
    switch (token.tag) {
        case LPAREN:
            accept(Tag.LPAREN);
            expr();
            accept(Tag.RPAREN);
            break;
        case MINUS:
            accept(Tag.MINUS);
            factor();
            valor = stack.pop();
            stack.push(-valor);
            break;
        case NUMBER:
            Number tk = (Number) token;
            valor = tk.value;
            /*String[] tkn = token.toString().split(",");
            valor = Double.parseDouble(tkn[1].substring(0, tkn[1].length()-1));*/
            stack.push(valor);
            //System.out.println(stack);
            accept(Tag.NUMBER);
            break;
        default:
            error(new Tag[] { Tag.LPAREN, Tag.MINUS, Tag.NUMBER });
            break;
    }
}

}