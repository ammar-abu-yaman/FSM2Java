import { configureStore } from "@reduxjs/toolkit";
import { metaDataReducer } from "./reducers/MetaDataReducer";
import { statesReducer } from "./reducers/StateReducer";
import { transitionsReducer } from "./reducers/TransitionsReducer";

const store = configureStore({
  reducer: {
    states: statesReducer,
    transitions: transitionsReducer,
    metaData: metaDataReducer,
  },
});

export type RootType = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
