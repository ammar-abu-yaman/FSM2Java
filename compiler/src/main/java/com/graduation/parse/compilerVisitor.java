// Generated from java-escape by ANTLR 4.11.1
package com.graduation.parse;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link compilerParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface compilerVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link compilerParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(compilerParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOption(compilerParser.OptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#state_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState_block(compilerParser.State_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState(compilerParser.StateContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#enter_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnter_block(compilerParser.Enter_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#exit_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExit_block(compilerParser.Exit_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#transition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransition(compilerParser.TransitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigger(compilerParser.TriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#param_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_list(compilerParser.Param_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(compilerParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link compilerParser#type_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_id(compilerParser.Type_idContext ctx);
}