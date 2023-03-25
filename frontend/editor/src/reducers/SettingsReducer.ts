import { createAction, createReducer, createSlice } from "@reduxjs/toolkit";
import { CodeEdition, FocusedObject, MetaData, StateType } from "../types";
import type { PayloadAction } from "@reduxjs/toolkit";

export interface Settings {
  focusedObject?: FocusedObject;
  codeEdition?: CodeEdition;
}

const initialState: Settings = {};

const settingsSlice = createSlice({
  name: "settings",
  initialState,
  reducers: {
    setFocusedObject(state, action: PayloadAction<FocusedObject | null>) {
      state.focusedObject = action.payload as any;
    },
    setCodeEdition(state, action: PayloadAction<CodeEdition | null>) {
      state.codeEdition = action.payload as any;
    },
  },
});

export const { setFocusedObject, setCodeEdition } = settingsSlice.actions;
export default settingsSlice.reducer;
