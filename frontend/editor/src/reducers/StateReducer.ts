import {
  createAction,
  createAsyncThunk,
  createReducer,
} from "@reduxjs/toolkit";
import { useSelector } from "react-redux";
import { AppDispatch, RootType } from "../store";
import { StateType } from "../types";
import { aggregateState } from "../util/json";
import { removeTransition } from "./TransitionsReducer";

const x = createAsyncThunk("uploadFsm", async (_, api) => {
  const data = api.getState() as RootType;
  const fsm = aggregateState(data);
  const result = await fetch("url", {
    headers: { contentType: "application/json" },
    body: JSON.stringify(fsm),
  });
});
const addState = createAction<StateType, "addState">("addState");
const addDefaultState = createAction<StateType, "addDefaultState">(
  "addDefaultState"
);
const removeState = createAction<number, "removeState">("removeState");
const updateState = createAction<StateType, "updateState">("updateState");

const generateFsmCode = createAsyncThunk(
  "states/generateFsmCode",
  async (_input, api) => {
    console.log("starting");
    const data = api.getState() as RootType;
    try {
      const fsm = aggregateState(data);
      const json = JSON.stringify(fsm);
      const resp = await fetch("https://google.com", {
        body: json,
        method: "post",
      });
      const text = await resp.text();
      return text;
    } catch (err) {
      console.log(err);
    }
  }
);

let nextId = 0;

const statesReducer = createReducer<StateType[]>([], (builder) =>
  builder
    .addCase(addState, (states, action) => {
      states.push({ ...action.payload, id: nextId++ });
    })
    .addCase(addDefaultState, (states, action) => {
      states.push({ ...action.payload, id: -1 });
    })
    .addCase(removeState, (states, action) => {
      return states.filter((state) => state.id === action.payload);
    })
    .addCase(updateState, (states, action) => {
      const index = states.findIndex((state) => state.id === action.payload.id);
      states[index] = action.payload;
    })
    .addCase(generateFsmCode.fulfilled, (state, action) => {
      alert(state);
    })
);

export {
  addState,
  addDefaultState,
  removeState,
  updateState,
  generateFsmCode,
  statesReducer,
};
