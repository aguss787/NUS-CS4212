Requirement:
	- Maven
	- Java 13

This project is build using intellij, so opening it using intellij should be easier.
There are commands prepared in intellij to generate cup, jflex, and to compile everything.

To compile from terminal:
	`make`

To run:
	`java -jar target\assignment-1-0.1-jar-with-dependencies.jar {input file}`

Other:
    The current version does not support overloading, however, it should not be that hard to implement. It can be
    implemented by extending the TypeMap to differentiate function and variables. In this implementation, function
    is defined as a data type (`FunctionSignature`). Two function is equal if and only if the function name is same
    and the arguments are exactly the same. However, this feature is not implemented due to lack of time (tbh, I
    procrastinate too much :'( )

    Both the type checking and IR3 generation is done by traversing the AST.

    Error message from type check is complete which means all errors will be reported. However, the line number is not
    shown due to my bad design at the parsing part which make the line number not accessible during these 2 processes.

Todo:
    Add line number in error message