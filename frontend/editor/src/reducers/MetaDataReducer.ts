import { createAction, createReducer } from "@reduxjs/toolkit";
import { MetaData, StateType } from "../types";

type UpdatableMetaData = {
  package?: string;
  className?: string;
  initialState?: string;
  contextMenuCoordinates?: { left: number; top: number };
  isContextMenuActive?: boolean;
};

const updateMetaData = createAction<UpdatableMetaData, "updateMetaData">(
  "updateMetaData"
);
const addAction = createAction<void, "addAction">("addAction");
const removeAction = createAction<number, "removeAction">("removeAction");
const updateAction = createAction<
  { index: number; value: string },
  "updateAction"
>("updateAction");

export const metaDataReducer = createReducer<MetaData>(
  {
    package: "",
    className: "",
    actions: [],
    contextMenuCoordinates: { left: 0, top: 0 },
    isContextMenuActive: false,
  },
  (builder) =>
    builder
      .addCase(updateMetaData, (state, action) => {
        return { ...state, ...(action.payload as any) };
      })
      .addCase(addAction, (state, action) => {
        state.actions.push("");
      })
      .addCase(removeAction, (state, action) => {
        state.actions.splice(action.payload, 1);
      })
      .addCase(updateAction, (state, action) => {
        const { index, value } = action.payload;
        state.actions[index] = value;
      })
);

export { updateMetaData, addAction, removeAction, updateAction };
