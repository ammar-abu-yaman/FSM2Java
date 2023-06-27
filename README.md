# FSM4J

## FSM4J is a tool that can generate working code from finite state machine specifications
## The tool provides two methods to provide FSM specifications

* Textual mode using a custom language
* Graphical web application to provide the specification visually

# How to run

Running the project locally requires 



### Running the compiler only

To run the compiler command line tool

* Clone the project into a folder on your local machine using ```git clone https://github.com/ammar-abu-yaman/FSM2Java.git```

* Run ``` mvnw clean install ``` to compile the project 

* The compiler can be found in src/main/compiler/target/compiler-jar-with-dependencies.jar 

The resulting Jar file contains all the necessary dependencies to run the project compiler 

### Running the application

The application contains the web UI and a server that runs the compiler on your behalf.
To compile and run the application follow these steps

* Clone the project into a folder on your local machine using ```git clone https://github.com/ammar-abu-yaman/FSM2Java.git```

* Run ``` mvnw clean install ``` to compile the project 

* The application can be found in target/server-0.0.1-SNAPSHOT.jar

Now you have two choices to run the application. You run it directly on your local machine using ``` java -jar target/server-0.0.1-SNAPSHOT.jar ``` and the web application will boot up on ```localhost:8080 ```

You can also run it using Docker after building an image from the compiled project using the provided Dockerfile.

# User Manual

There are two ways to use the project, one is to use the compiler directly after writing your FSM description using the fsm4j language or use the web ui to build a visual description of your FSM.

## Fsm4j language spec

### Metadata

Metadata provides information to the compiler about your application specifics to be used in code generation.

The syntax for a metadata directive is as follows

``` $property-name property-value [property-value]* ```

The supported properties are:

package: used to specify the application's package to be included in the generated code (only relevant for Java code generation)

class: specify the application  name that is used to name all the generated classes

initial-state: used to specify the starting state of the FSM

actions: multivalue property that is used to specify the actions that should be present in the application class code.

### States specification

The state specification comes after the metadata section. This section describes the states, state transitions, action code, and guards that composes the finite state machine


the section starts with ```${{``` and ends with ```}}$```


### State specification syntax

States are generally defined as follows

```
state-name { 

    [__enter__ {# .. #}]
    [__exit__ {# .. #}]

    transition-one,
    transition-two,
    transition-three
    ..
}
```

starting with the state name then optionally defining enter code and exit code that will be executed every time that state is entered or exited respectively, followed by the state's transitions' definitions.


### Transition specification syntax

Transitions are generally defined as follows

```
transition-name [[# guard-condition #]] => next-state {# .. #}
```

starting with the transition name followed by an optional guard condition then the next state followed by the code that will be executed upon hitting  the transition.

### Base State

You can also define a Base state that acts as a failsafe mechanism that contains the default implementation of the FSM transitions, it can be defined as any other State but the name of the state must be ```Base```.

## Example 
Putting all of this together we will build a basic classical Turnstile finite-state machine using the tool 
--some image here

The spec file for the turnstile
```javascript
$package com.turnstile
$class Turnstile
$initial-state Locked
$actions lock unlock alarm thankyou

${{
    Locked {
        coin => Unlocked {# ctx.unlock(); #},
        pass => null {# ctx.alarm(); #}
    },
    
    Unlocked {
        pass => Locked  {# ctx.lock(); #},
        coin => nil {# ctx.thankyou(); #}
    }
}}$
```
We save this in a file called turnstile.fsm4j
Next, we compile the file to generate code using 

