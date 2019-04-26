package com.prezi.homeassignment

import com.prezi.homeassignment.schemalang.{ComplexSchemaType, FieldDef}

class Generator(val packageName: Option[String] = None) {

    def generate(typeDef: ComplexSchemaType): GeneratorResult = {
        if (typeDef.isAbstract) {
            generateInterface(typeDef)
        } else {
            generateClass(typeDef)
        }
    }

    def generateVector(typeName: String): GeneratorResult = {
        val vectorVariableName = typeName.charAt(0).toLower + typeName.substring(1) + "List"
        val className = typeName + "Vector"
        GeneratorResult(
            className + ".java",
            packageDeclaration +
                s"""public class $className extends ArrayListVector<$typeName> {
                   #
               #    public $className($typeName... $vectorVariableName) {
                   #        super($vectorVariableName);
                   #    }
                   #
               #}
                   #""".stripMargin('#')
        )
    }

    private def generateClass(typeDef: ComplexSchemaType) = {
        val className = typeDef.name.toString()
        GeneratorResult(
            typeDef.name.toString() + ".java",
            packageDeclaration +
            s"""public class $className {
               #
               #    public $className(
               #    ) {
               #    }
               #
               #    public String toJson() {
               #        StringBuilder result = new StringBuilder();
               #
               #        result.append("{\\"def\\":\\"$className\\"");
               #
               #        result.append("}");
               #
               #        return result.toString();
               #    }
               #
               #}
               #""".stripMargin('#')
        )
    }

    private def generateInterface(typeDef: ComplexSchemaType) = {
        val name = typeDef.name.toString
        val interface = Interface(
            name = name,
            _extends = typeDef.inheritsFrom.map(t => t.toString),
            accessors = typeDef.fields.map(f => AbstractAccessorPair(f.name.toString, typeNameOfField(f)))
        )
        GeneratorResult(
            name + ".java",
            packageDeclaration + interface.toString
        )
    }

    def packageDeclaration: String = packageName.fold("") (name => PackageDeclaration(name).toString)

    def extendsDeclaration(typeDef: ComplexSchemaType): String = {
        if (typeDef.inheritsFrom.nonEmpty) {
            s" extends ${typeDef.inheritsFrom.mkString(", ")}"
        } else {
            ""
        }
    }

    def fields(typeDef: ComplexSchemaType): String = {
        clearIfOnlyWhitespace(
            "\n" +
            indent(
                typeDef.fields
                .map(f => AbstractAccessorPair(f.name.toString, typeNameOfField(f)))
                .mkString("\n\n")
            ) +
            "\n\n"
        )
    }

    def indent(s: String): String = {
        val indentation = " " * 4
        s.split('\n').map(line => if (line.trim.length > 0) indentation + line else line).mkString("\n")
    }

    def clearIfOnlyWhitespace(s: String): String = {
        if (s.trim.length > 0) {
            s
        } else {
            ""
        }
    }

    def typeNameOfField(f: FieldDef): String = {
        if (f.isList) {
            s"${f.typeName.toString()}Vector"
        } else {
            f.typeName.toString()
        }
    }

}

case class GeneratorResult(fileName: String, contents: String)

case class PackageDeclaration(name: String) {
    override def toString: String = s"package $name;\n\n"
}

case class Interface(name: String, _extends: List[String], accessors: List[AbstractAccessorPair]) {

    override def toString: String = {
        s"""public interface $name$extendsDeclaration {
           #$fields}
           #""".stripMargin('#')
    }

    def extendsDeclaration: String = {
        if (_extends.nonEmpty) {
            s" extends ${_extends.mkString(", ")}"
        } else {
            ""
        }
    }

    def fields: String = {
        clearIfOnlyWhitespace(
            "\n" +
            indent(accessors.mkString("\n\n")) +
            "\n\n"
        )
    }

    def indent(s: String): String = {
        val indentation = " " * 4
        s.split('\n').map(line => if (line.trim.length > 0) indentation + line else line).mkString("\n")
    }

    def clearIfOnlyWhitespace(s: String): String = {
        if (s.trim.length > 0) {
            s
        } else {
            ""
        }
    }
}

case class AbstractAccessorPair(name: String, _type: String) {
    override def toString: String = {
        List(
            AbstractGetter(name, _type),
            AbstractSetter(name, _type)
        ).mkString("\n")
    }
}

case class AbstractGetter(name: String, _type: String) {
    override def toString: String = s"${_type} get${name.capitalize}();"
}

case class AbstractSetter(name: String, _type: String) {
    override def toString: String = s"void set${name.capitalize}(${_type} value);"
}
