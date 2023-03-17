import { configureStore } from "@reduxjs/toolkit";
import { defaultStateReducer } from "./reducers/DefaultState";
import { metaDataReducer } from "./reducers/MetaDataReducer";
import { statesReducer } from "./reducers/StateReducer";
import { transitionsReducer } from "./reducers/TransitionsReducer";
const store = configureStore({
  reducer: {
    states: statesReducer,
    transitions: transitionsReducer,
    metaData: metaDataReducer,
    defaultState: defaultStateReducer,
  },
});

export type RootType = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
