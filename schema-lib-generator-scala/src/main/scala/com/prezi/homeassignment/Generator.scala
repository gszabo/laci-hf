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
            members = typeDef.fields.map(f => AbstractAccessorPair(f.name.toString, typeNameOfField(f)))
        )
        val javaFile = JavaFile(name + ".java", packageName, interface)
        GeneratorResult(
            javaFile.name,
            javaFile.lines().mkString("\n")
        )
    }

    def packageDeclaration: String = packageName.fold("") (name => PackageDeclaration(name).toString)

    def typeNameOfField(f: FieldDef): String = {
        if (f.isList) {
            s"${f.typeName.toString()}Vector"
        } else {
            f.typeName.toString()
        }
    }

}

case class GeneratorResult(fileName: String, contents: String)

trait SourceCodeItem {

    def lines(): List[String]

}

case class JavaFile(name: String, packageName: Option[String], content: SourceCodeItem) extends SourceCodeItem {
    override def lines(): List[String] = {
        packageName.fold(Nil: List[String]) { PackageDeclaration(_).lines() } ++ content.lines()
    }
}

case class PackageDeclaration(name: String) extends SourceCodeItem {
    override def toString: String = s"package $name;\n\n"

    override def lines(): List[String] = List(s"package $name;", "")
}

case class Interface(name: String, _extends: List[String], members: List[SourceCodeItem]) extends SourceCodeItem {

    override def lines(): List[String] = {
        List(
            s"public interface $name$extendsDeclaration {"
        ) ++
            bodyLines().map(indent) ++
        List(
            "}",
            ""
        )
    }

    private def bodyLines(): List[String] = {
        if (members.nonEmpty) {
            "" :: members.flatMap(a => a.lines() :+ "")
        } else {
            Nil
        }
    }

    private def extendsDeclaration: String = {
        if (_extends.nonEmpty) {
            s" extends ${_extends.mkString(", ")}"
        } else {
            ""
        }
    }

    private def indent(s: String): String = {
        val indentation = " " * 4
        s.split('\n').map(line => if (line.trim.length > 0) indentation + line else line).mkString("\n")
    }

}

case class AbstractAccessorPair(name: String, _type: String) extends SourceCodeItem {
    override def lines(): List[String] = {
        AbstractGetter(name, _type).lines() ++ AbstractSetter(name, _type).lines()
    }
}

case class AbstractGetter(name: String, _type: String) extends SourceCodeItem {
    override def lines(): List[String] = {
        List(s"${_type} get${name.capitalize}();")
    }
}

case class AbstractSetter(name: String, _type: String) extends SourceCodeItem {
    override def lines(): List[String] = {
        List(s"void set${name.capitalize}(${_type} value);")
    }
}
