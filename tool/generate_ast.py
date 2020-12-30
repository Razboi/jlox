#!/usr/bin/python

import sys

def main():
    if len(sys.argv[1:]) != 1:
        print "Usage: generate_ast <output directory>"
        return 64
    output_dir = sys.argv[1:][0]
    define_ast(output_dir, "Expr", [
            "Logical : Expr left, Token operator, Expr right",
            "Assign : Token name, Expr value",
            "Ternary: Expr condition, Expr left, Expr right",
            "Binary : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal : Object value",
            "Unary : Token operator, Expr right",
            "Variable: Token name"
        ])
    define_ast(output_dir, "Stmt", [
            "While : Expr condition, Stmt body",
            "If : Expr condition, Stmt thenBranch, Stmt elseBranch",
            "Block : List<Stmt> statements",
            "Expression : Expr expression",
            "Print : Expr expression",
            "Var : Token name, Expr initializer"
        ])

def define_ast(output_dir, base_name, types):
    f = open(output_dir + '/' + base_name + ".java", "w")
    f.write("package jlox.lang;\n\n")
    f.write("import java.util.List;\n\n")
    f.write("abstract class " + base_name + " {\n\n")
    define_visitor(f, base_name, types)
    for type in types:
        class_name = type.split(':')[0].strip()
        fields = type.split(':')[1].strip()
        define_type(f, base_name, class_name, fields)

    f.write("\n")
    f.write("   abstract <R> R accept(Visitor<R> visitor);\n")
    f.write("}\n")
    f.close()

def define_visitor(f, base_name, types):
    f.write("   interface Visitor<R> {\n")
    for type in types:
        type_name = type.split(':')[0].strip()
        f.write("       R visit" + type_name + base_name + '(' + type_name + ' ' + base_name.lower() + ");\n")
    f.write("   }\n\n")

def define_type(f, base_name, class_name, fields_list):
    f.write("   static class " + class_name + " extends " + base_name + " {\n")
    fields = fields_list.split(", ")

    #Class fields
    for field in fields:
        f.write("       final " + field + ";\n")
    f.write("\n")

    #Constructor
    f.write("       " + class_name + "(" + fields_list + ") {\n")
    for field in fields:
        name = field.split(" ")[1]
        f.write("           this." + name + " = " + name + ";\n")

    f.write("       }\n\n")
    f.write("    @Override\n")
    f.write("    <R> R accept(Visitor<R> visitor) {\n")
    f.write("       return visitor.visit" + class_name + base_name + "(this);\n")
    f.write("       }\n")
    f.write("   }\n\n")


if __name__ == "__main__":
    main()
