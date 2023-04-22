grammar compiler;

WS: (' ' | '\t' | '\r' | '\n')+ -> skip;
OP_ID: '$' [a-zA-Z]+[A-Za-z0-9]*([-][a-zA-Z]+[A-Za-z0-9]*)*;
ID: [a-zA-Z]+[A-Za-z0-9]*([.][a-zA-Z]+[A-Za-z0-9]*)*;
GUARD: '[#' .*? '#]';
JAVA_CODE: '{#' .*? '#}';

program: (options+=option*) state_block EOF;

option: name=OP_ID values+=ID+;

state_block: '${{' (states+=state (',' states+=state)*)? '}}$';

state: name=ID '{' enter=enter_block? exit=exit_block? (transitions+=transition (',' transitions+=transition)*)?  '}';

enter_block: '__enter__' code=JAVA_CODE;

exit_block: '__exit__' code=JAVA_CODE;

transition: trig=trigger guard=GUARD? '=>' nextState=ID code=JAVA_CODE;

trigger: name=ID ('(' param_list ')')?;

param_list: (params+=param (',' params+=param)*)?;

param: name=ID ':' type=type_id;

type_id: ID ('<' type_id (',' type_id)* '>')?; 

