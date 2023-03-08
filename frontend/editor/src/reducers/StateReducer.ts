import { createAction, createReducer } from "@reduxjs/toolkit";
import { StateType } from "../types";

const addState = createAction<StateType, "addState">("addState");
const removeState = createAction<number, "removeState">("removeState");
const updateState = createAction<StateType, "updateState">("updateState");

let nextId = 0;

const statesReducer = createReducer<StateType[]>([], (builder) =>
  builder
    .addCase(addState, (states, action) => {
      states.push({ ...action.payload, id: nextId++ });
    })
    .addCase(removeState, (states, action) => {
      return states.filter((state) => state.id === action.payload);
    })
    .addCase(updateState, (states, action) => {
      const index = states.findIndex((state) => state.id === action.payload.id);
      states[index] = action.payload;
    })
);

export { addState, removeState, updateState, statesReducer };
