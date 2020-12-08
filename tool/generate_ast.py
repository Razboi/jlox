#!/usr/bin/python

import sys

def main():
    if len(sys.argv[1:]) != 1:
        print "Usage: generate_ast <output directory>"
        return 64
    output_dir = sys.argv[1:][0]
    define_ast(output_dir, "Expr", [
            "Binary : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal : Object value",
            "Unary : Token operator, Expr right"
        ])

def define_ast(output_dir, base_name, types):
    f = open(output_dir + '/' + base_name + ".java", "w")
    f.write("package jlox.lang;\n\n")
    f.write("import java.util.List;\n\n")
    f.write("abstract class " + base_name + " {\n\n")
    for type in types:
        class_name = type.split(':')[0].strip()
        fields = type.split(':')[1].strip()
        define_type(f, base_name, class_name, fields)
    f.write("}\n")
    f.close()

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

    f.write("       }\n")
    f.write("   }\n\n")


if __name__ == "__main__":
    main()
