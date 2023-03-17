import { createAction, createReducer } from "@reduxjs/toolkit";
import { TriggerMapping } from "../types";

const addTrigger = createAction<TriggerMapping, "addTrigger">("addTrigger");
const removeTrigger = createAction<number, "removeTrigger">("removeTrigger");
const removeTriggers = createAction<number[], "removeTriggers">(
  "removeTriggers"
);
const updateTrigger = createAction<TriggerMapping, "updateTrigger">(
  "updateTrigger"
);

const addTriggerParameter = createAction<number, "addTriggerParameter">(
  "addTriggerParameter"
);

const removeTriggerParameter = createAction<
  { triggerId: number; paramId: number },
  "removeTriggerParameter"
>("removeTriggerParameter");

const updateTriggerParameter = createAction<
  {
    triggerId: number;
    paramId: number;
    param: { name?: string; type?: string };
  },
  "updateTriggerParameter"
>("updateTriggerParameter");

let nextId = 0;

export const defaultStateReducer = createReducer<TriggerMapping[]>(
  [],
  (builder) =>
    builder
      .addCase(addTrigger, (triggers, action) => {
        triggers.push({ ...action.payload, id: nextId++ });
      })
      .addCase(removeTrigger, (triggers, action) => {
        return triggers.filter((trigger) => trigger.id === action.payload);
      })
      .addCase(updateTrigger, (triggers, action) => {
        const index = triggers.findIndex(
          (trigger) => trigger.id === action.payload.id
        );
        triggers[index] = action.payload;
      })
      .addCase(removeTriggers, (state, action) => {
        const ids = action.payload;
        return state.filter((trigger) => ids.includes(trigger.id));
      })
      .addCase(addTriggerParameter, (triggers, action) => {
        const { parameters } = triggers[action.payload];
        parameters.push({
          name: `Param${parameters.length + 1}`,
          type: "String",
        });
      })
      .addCase(removeTriggerParameter, (triggers, action) => {
        const { triggerId, paramId } = action.payload;
        triggers[triggerId].parameters.splice(paramId, 1);
      })
      .addCase(updateTriggerParameter, (triggers, action) => {
        const { parameters } = triggers[action.payload.triggerId];
        parameters[action.payload.paramId] = {
          ...parameters[action.payload.paramId],
          ...action.payload.param,
        };
      })
);
