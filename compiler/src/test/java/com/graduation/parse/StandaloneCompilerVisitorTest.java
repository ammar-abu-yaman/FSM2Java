package com.graduation.parse;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

import com.graduation.TestUtils;
import com.graduation.fsm.Fsm;
import com.graduation.fsm.Item.*;

public class StandaloneCompilerVisitorTest {
	@Test
	public void testDeclarations() throws Exception {
		Fsm fsm = TestUtils.generateFsmFromFile("samples/declarations.txt");
		assertEquals(new Fsm(
				TestUtils.options(
						new Option("package", TestUtils.list("com.sample.one23")),
						new Option("class", TestUtils.list("Sample")),
						new Option("initial-state", TestUtils.list("AnyState")),
						new Option("actions", TestUtils.list("action1", "action2", "action3"))),
				TestUtils.list()), fsm);
	}

	@Test
	public void testEmptyState() throws Exception {
		Fsm fsm = TestUtils.generateFsmFromFile("samples/empty-state.txt");
		assertEquals(new Fsm(
				TestUtils.options(),
				TestUtils.list(new State("Empty", TestUtils.list()))), fsm);
	}

	@Test
	public void testBasicTransitions() throws Exception {
		Fsm actual = TestUtils.generateFsmFromFile("samples/transitions.txt");
		Fsm expected = new Fsm(
				TestUtils.options(),
				TestUtils.list(
						new State(
								"State1",
								TestUtils.list(
										new Transition(
												new Trigger("Trigger1",
														TestUtils.list()),
												Optional.of("count == 5"),
												"State2",
												"""
														int x = 5;for(int i = 0; i < x; i++) {System.out.println("hello world");}
														"""
														.trim()),
										new Transition(
												new Trigger("Trigger2",
														TestUtils.list()),
												Optional.of("expr[5+3].whatever.equals(\"\")"),
												"State3",
												"""
														String s = "hello" + " world";
														"""
														.trim())

								))));
		assertEquals(expected, actual);
	}

	@Test
	public void testTransitionsWithParams() throws Exception {
		Fsm actual = TestUtils.generateFsmFromFile("samples/transitions-with-params.txt");
		Fsm expected = new Fsm(
				TestUtils.options(),
				TestUtils.list(
						new State(
								"State1",
								TestUtils.list(
										new Transition(
												new Trigger(
														"Trigger1",
														TestUtils.list(new Param(
																"x",
																"int"),
																new Param("y12", "String"))),
												Optional.of("count == 5"),
												"State2",
												"""
														int x = 5;for(int i = 0; i < x; i++) {System.out.println("hello world");}
														"""
														.trim()),
										new Transition(
												new Trigger("Trigger2",
														TestUtils.list(
																new Param("name",
																		"java.util.ArrayList<java.util.Map<String,String>>"))),
												Optional.of("expr[5+3].whatever.equals(\"\")"),
												"State3",
												"""
														String s = "hello" + " world";
														"""
														.trim())

								))));
		assertEquals(expected, actual);
	}

}
