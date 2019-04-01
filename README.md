
# Home assignment

We'll define a simple schema definition language. This language can describe hierarchical document structures. At Prezi 
we have a similar one to describe prezi documents, but you can represent other structures with it as well. 

Your task is to write a code generator. The generator will be given some instance of the schema and create a library to access documents complying to it. The library will provide type safe functions for document creation, field access, modification and serialization.

You can implement the generator in either *Scala* or *Java*, but in both cases it should generate *Java source code*.

The assignment has multiple tasks, some of them is optional. These worth bonus points during the evaluation. 

- [Getting started](#getting started)
- [The schema language](#the-schema-language)
- [Task: Java API generation (required)](#task-java-api-generation-required)
- [Task: The Json format (required)](#task-the-json-format-required)
- [Task: Schema validation (optional)](#task-schema-validation-optional)
- [Task: Runtime checks (optional)](#task-runtime-checks-optional)

Have a good time! We hope to see you in person soon.

## Getting started
### What you will need

You need to have JDK 8 installed and available in your command line. You should be able to compile everything with your 
favourite OS (Windows, Max or Linux). We also suggest setting up an IDE (we use IntelliJ IDEA).

### The sample schema
We prepared the repository with a schema called `directory`. In some pseudo language it looks like:

```scala
    abstract def FileSystemObject
        String name
    
    def File extends FileSystemObject
    
    def Directory extends FileSystemObject
        FileSystemObject[] content
```

It's a simple abstraction of a file system consisting of directories and files. Both have a name, and a directory can 
contain other file system objects.

**Note:** although we describe everything in the context of the sample schema, we will test the code generator with 
various other schemas which are not provided here. We will invoke it with erroneous schemas, larger and 
smaller ones, so make sure you prepare for the corner cases.

### The repository structure
This is a multi project gradle repository with the following structure:
```
.
├── directory-test-app
│   ├── build.gradle
│   └── src
├── schema-lib-generator-java
│   ├── build.gradle
│   └── src
│       ├── main
│       └── provided
└── schema-lib-generator-scala
    ├── build.gradle
    └── src
        ├── main
        └── provided
```

Depending on your choice you will work either in [schema-lib-generator-scala](schema-lib-generator-scala) or [schema-lib-generator-java](schema-lib-generator-java). 

We set up the project so that it contains a client application [directory-test-app](directory-test-app) for the library you generate from the sample schema. Your main goal is to make the test app compile, and the unit tests green.

### Compiling

You should decide the path you choose (java or scala) and can even delete the other generator folder. If you choose java, please also open [directory-test-app/build.gradle](directory-test-app/build.gradle) and change the dependencies to point to the java project:

```groovy
    dependencies {
        // TODO: Change this if you are using java
        // compile project(path: ":schema-lib-generator-java", configuration: "directory-lib")
        compile project(path: ":schema-lib-generator-scala", configuration: "directory-lib")
```


Let's try to build:

    gradlew directory-test-app:build

It will fail with an exception, because the generator is not implemented yet. Your entry point is the `generate` 
function of the `LibraryGenerator` class [here](schema-lib-generator-java/src/main/java/com/prezi/homeassignment/LibraryGenerator.java) or [here](schema-lib-generator-scala/src/main/scala/com/prezi/homeassignment/LibraryGenerator.scala). It gets the schema as parameter and also an output folder for the generated files. 

Comment out the line that throws the `Not implemented exception` and run the command again. Now you will get a bunch of 
java compile errors from the `directory-test-app` project.  

Check [directory-test-app/src/.../ApiTest.java](directory-test-app/src/test/java/ApiTest.java), this file contains unit tests to check some aspects of the generated library. It gives you some clues about what kind of API is to be generated. (But we will describe everything below.)

Start working in the LibraryGenerator class. If you want to use some third party library, add it to the `build.gradle` of the generator project. To add helper classes to the generated code, just add them to the `src/provided` folder.

Once you are done with everything the compile errors will go away, tests will pass and the build will succeed.

## The schema language
The language has a JSON representation, which is not discussed here, because the code generator gets the parsed schema 
as input. We will use the pseudo syntax for discussion.

A schema consists of `primitive` types and `complex` types. 

Primitive types are built in, and we have just a single one called `String`. 

A `complex` type has a name and zero or more field definitions. 
Fields have:
- **name** (*for example* `field1`)
- **type** (*for example* `String` or `Foo`)
- **shape** defines whether the field contains a **single value** or **list**. 

```scala
    // a complex type with 4 fields:
    def Foo
        String field1
        String[] field3 // field with a list of primitives
        Bar field4
        Bar[] field2   // field with a list of complex values
```

Since the shape is not part of the field type, the schema cannot describe fields with lists of lists.

For the sake of simplicity, we suppose that there are no empty fields in valid documents. Empty lists are ok though.

A complex type can be abstract or concrete.  Abstract types are for organization and cannot be instantiated (modeled 
with interfaces in Java).

A complex type can extend zero or more *abstract* types (modeled with interface inheritance in Java).  

 ```scala
    abstract def FileSystemObject
        String name
    
    // File has name field 
    def File extends FileSystemObject
    
    // Directory has name field
    // the content field contains Files or Directories
    def Directory extends FileSystemObject
        FileSystemObject[] content
```    

## Task: Java API generation (required)
This section describes the library to be generated. For other clues and examples check 
[ApiTest.java](directory-test-app/src/test/java/ApiTest.java).

### Abstract types
Generate an interface, containing a getter and setter for each field. If the type extends other types, the interface 
should extend the interfaces generated from those.

```java
    // abstract def Foo extends Base1, Base2
    //    Type1 field1
    //    Type2[] field2
 
    public interface Foo extends Base1, Base2 {
        
        Type1 getField1();
        void setField1(Type1 value);
        
        Type2Vector getField2();
        void setField2(Type2Vector value);
    }
```
 
### Concrete types
Generate a class containing a getter and setter for each field. Add base interfaces based on the extension list. The 
class should have a public constructor that initializes all of the fields in the [natural field order (defined below)](#the-natural-field-order). It should also 
have a static `fromJson` method and a `toJson` method for [serialization](#task-json-serialization-required).

 ```java
    // def Foo extends Base1, Base2
    //     Type1 field1
    //     Type2Vector field2

    public class Foo implements Base1, Base2 {
        
        ...
        
        Type1 getField1();
        void setField1(Type1 value) { 
            ...
        }
        
        Type2Vector getField2();
        void setField2(Type2Vector value){
            ...
        }
        
        public Foo(
            TypeX baseFieldX, 
            TypeY baseFieldY,
            Type1 field1, 
            Type2Vector field2) {...}
    
        public static Foo fromJson(String jsonData) {
            ...
        }
         
        public String toJson() {
            ...
        }
    }
```

### Vector classes
To store list of values, you should generate a class which implements the [Vector interface (defined in the template 
project resources)](schema-lib-generator-java/src/provided/java/com/prezi/homeassignment/schemalib/Vector.java).

```java
    public class FooVector implements Vector<Foo> {
        ...
        public FooVector(Foo... fooList){
            ...
        }
    }
```

### The natural field order
Both the constructor parameters and the JSON serialization traverses the fields of the type in the so called `natural 
field order`.
It's a recursive process: first we take the natural field order of each extended type and then concatenate the fields 
defined in the type itself in a postorder manner. E.g:

```scala
    abstract def Base1
        Type1 field1
        Type2[] field2
    
    abstract def Base2
        Type3 field3
        Type4 field4
    
    def Foo extends Base1, Base2
        Type5 field5
        Type6 field6
```

The natural field order for Foo's fields is:
    field1, field2, field3, field4, field5, field6

## Task: Json serialization (required)
(Concrete) complex objects can be serialized to and from Json, as specified in the [API chapter](#concrete-types).  We describe the format here.

To store `strings` use Json string literals, `lists` are just Json arrays []. 

For `complex objects` create a Json object {}. First serialize the type of the object. Take the type name and add a 
`"def":"typename"` field to the object. Then go over the fields in the `natural field order` and serialize each with the 
field name as the label. E.g:

```java
    new Directory("/", 
        new FileVector(new File("file1.txt")))
```

becomes:

```javascript
    {
        "def": "Directory",
        "name": "/",
        "content": [{
            "def": "File",
            "name": "file1.txt"
        }]
    }
``` 

At the end remove all unnecessary whitespaces, and write out the Json as a single line. You can find more examples in 
the [test resources](directory-test-app/src/test/resources) folder.

You can suppose that the document is only manipulated with the API you provide hence you don't have to prepare for Json 
parse errors or semantically invalid documents.

## Task: Schema validation (optional)
There are a few rules to be checked before code generation. Some of these follow from the Java representation we 
introduce. A good implementation gives meaningful error messages instead of generating code that doesn't compile. So 
check for these:

- The type names cannot clash with each other (and with the primitive types)
- Every type mentioned in the schema has to be defined somewhere (or be a primitive type)
- The type names and field names cannot clash with java keywords and should be valid identifiers.
- The name `def` cannot be used as a field name (see Json serialization).
- Only abstract types can be extended.
- The type extension hierarchy cannot contain cycles.

You should throw the provided `SchemaValidationException` for errors. It's OK to stop at the first error.

## Task: Runtime checks (optional)
An object which can be found by traversing the fields of some other object is called attached. Otherwise it's free. 
The document root is free. 

Every setter and insert method should check that the inserted object is free. If an attached object is about to be 
inserted somewhere, an `UnsupportedOperationException` should be thrown. The tricky case is when a free object is about 
to be inserted under itself. Throw an `UnsupportedOperationException` in this case as well.

The same should happen if somebody wants to set a field to null.

**Note:** if you decide to skip this task, check the TODOs in [ApiTest.java](directory-test-app/src/test/java/ApiTest.java).
