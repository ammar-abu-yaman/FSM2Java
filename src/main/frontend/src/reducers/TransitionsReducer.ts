import { createAction, createReducer } from "@reduxjs/toolkit";
import { TransitionType } from "../types";

const addTransition = createAction<TransitionType, "addTransition">(
  "addTransition"
);
const removeTransition = createAction<string, "removeTransition">(
  "removeTransition"
);
const removeTransitions = createAction<string[], "removeTransitions">(
  "removeTransitions"
);
const updateTransition = createAction<TransitionType, "updateTransition">(
  "updateTransition"
);

const addTransitionParameter = createAction<string, "addTransitionParameter">(
  "addTransitionParameter"
);

const removeTransitionParameter = createAction<
  { transitionId: string; paramId: number },
  "removeTransitionParameter"
>("removeTransitionParameter");

const updateTransitionParameter = createAction<
  {
    transitionId: string;
    paramId: number;
    param: { name?: string; type?: string };
  },
  "updateTransitionParameter"
>("updateTransitionParameter");

const transitionsReducer = createReducer<TransitionType[]>([], (builder) =>
  builder
    .addCase(addTransition, (transitions, action) => {
      transitions.push({ ...action.payload, id: crypto.randomUUID() });
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
      const { parameters } = transitions.find(
        (item) => item.id === action.payload
      ) as TransitionType;
      parameters.push({
        name: `Param${parameters.length + 1}`,
        type: "String",
      });
    })
    .addCase(removeTransitionParameter, (transitions, action) => {
      const { transitionId, paramId } = action.payload;
      transitions
        .find((item) => item.id === transitionId)
        ?.parameters?.splice?.(paramId, 1);
    })
    .addCase(updateTransitionParameter, (transitions, action) => {
      const { parameters } = transitions.find(
        (item) => item.id === action.payload.transitionId
      ) as TransitionType;
      parameters[action.payload.paramId] = {
        ...parameters[action.payload.paramId],
        ...action.payload.param,
      };
    })
);

export {
  addTransition,
  removeTransition,
  removeTransitions,
  updateTransition,
  transitionsReducer,
  addTransitionParameter,
  removeTransitionParameter,
  updateTransitionParameter,
};
