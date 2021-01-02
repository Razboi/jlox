package jlox.lang;

import java.util.List;

abstract class Stmt {

   interface Visitor<R> {
       R visitBreakStmt(Break stmt);
       R visitContinueStmt(Continue stmt);
       R visitWhileStmt(While stmt);
       R visitIfStmt(If stmt);
       R visitBlockStmt(Block stmt);
       R visitExpressionStmt(Expression stmt);
       R visitPrintStmt(Print stmt);
       R visitVarStmt(Var stmt);
   }

   static class Break extends Stmt {

       Break() {
       }

    @Override
    <R> R accept(Visitor<R> visitor) {
       return visitor.visitBreakStmt(this);
       }
   }

   static class Continue extends Stmt {

       Continue() {
       }

    @Override
    <R> R accept(Visitor<R> visitor) {
       return visitor.visitContinueStmt(this);
       }
   }

   static class While extends Stmt {
       final Expr condition;
       final Stmt body;
       final Boolean isForLoop;

       While(Expr condition, Stmt body, Boolean isForLoop) {
           this.condition = condition;
           this.body = body;
           this.isForLoop = isForLoop;
       }

    @Override
    <R> R accept(Visitor<R> visitor) {
       return visitor.visitWhileStmt(this);
       }
   }

   static class If extends Stmt {
       final Expr condition;
       final Stmt thenBranch;
       final Stmt elseBranch;

       If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
           this.condition = condition;
           this.thenBranch = thenBranch;
           this.elseBranch = elseBranch;
       }

    @Override
    <R> R accept(Visitor<R> visitor) {
       return visitor.visitIfStmt(this);
       }
   }

   static class Block extends Stmt {
       final List<Stmt> statements;

       Block(List<Stmt> statements) {
           this.statements = statements;
       }

    @Override
    <R> R accept(Visitor<R> visitor) {
       return visitor.visitBlockStmt(this);
       }
   }

   static class Expression extends Stmt {
       final Expr expression;

       Expression(Expr expression) {
           this.expression = expression;
       }

    @Override
    <R> R accept(Visitor<R> visitor) {
       return visitor.visitExpressionStmt(this);
       }
   }

   static class Print extends Stmt {
       final Expr expression;

       Print(Expr expression) {
           this.expression = expression;
       }

    @Override
    <R> R accept(Visitor<R> visitor) {
       return visitor.visitPrintStmt(this);
       }
   }

   static class Var extends Stmt {
       final Token name;
       final Expr initializer;

       Var(Token name, Expr initializer) {
           this.name = name;
           this.initializer = initializer;
       }

    @Override
    <R> R accept(Visitor<R> visitor) {
       return visitor.visitVarStmt(this);
       }
   }


   abstract <R> R accept(Visitor<R> visitor);
}
