import { createAction, createReducer } from "@reduxjs/toolkit";
import { TransitionType } from "../types";

const addTransition = createAction<TransitionType, "addTransition">(
  "addTransition"
);
const removeTransition = createAction<number, "removeTransition">(
  "removeTransition"
);
const removeTransitions = createAction<number[], "removeTransitions">(
  "removeTransitions"
);
const updateTransition = createAction<TransitionType, "updateTransition">(
  "updateTransition"
);

const addTransitionParameter = createAction<number, "addTransitionParameter">(
  "addTransitionParameter"
);

const removeTransitionParameter = createAction<
  { transitionId: number; paramId: number },
  "removeTransitionParameter"
>("removeTransitionParameter");

const updateTransitionParameter = createAction<
  {
    transitionId: number;
    paramId: number;
    param: { name?: string; type?: string };
  },
  "updateTransitionParameter"
>("updateTransitionParameter");

let nextId = 0;

const transitionsReducer = createReducer<TransitionType[]>([], (builder) =>
  builder
    .addCase(addTransition, (transitions, action) => {
      transitions.push({ ...action.payload, id: nextId++ });
    })
    .addCase(removeTransition, (transitions, action) => {
      return transitions.filter(
        (transition) => transition.id === action.payload
      );
    })
    .addCase(updateTransition, (transitions, action) => {
      const index = transitions.findIndex(
        (transition) => transition.id === action.payload.id
      );
      transitions[index] = action.payload;
    })
    .addCase(removeTransitions, (state, action) => {
      const ids = action.payload;
      return state.filter((transition) => ids.includes(transition.id));
    })
    .addCase(addTransitionParameter, (transitions, action) => {
      const { parameters } = transitions[action.payload];
      parameters.push({
        name: `Param${parameters.length + 1}`,
        type: "String",
      });
    })
    .addCase(removeTransitionParameter, (transitions, action) => {
      const { transitionId, paramId } = action.payload;
      transitions[transitionId].parameters.splice(paramId, 1);
    })
    .addCase(updateTransitionParameter, (transitions, action) => {
      const { parameters } = transitions[action.payload.transitionId];
      parameters[action.payload.paramId] = {
        ...parameters[action.payload.paramId],
        ...action.payload.param,
      };
    })
);

export {
  addTransition,
  removeTransition,
  updateTransition,
  transitionsReducer,
  addTransitionParameter,
  removeTransitionParameter,
  updateTransitionParameter,
};
