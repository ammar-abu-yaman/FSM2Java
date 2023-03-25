import { configureStore } from "@reduxjs/toolkit";
import statesListener from "./listeners/statesListener";
import transitionsListener from "./listeners/transitionsListener";
import { defaultStateReducer } from "./reducers/DefaultState";
import { metaDataReducer } from "./reducers/MetaDataReducer";
import SettingsReducer from "./reducers/SettingsReducer";
import { statesReducer } from "./reducers/StateReducer";
import { transitionsReducer } from "./reducers/TransitionsReducer";
const store = configureStore({
  reducer: {
    states: statesReducer,
    transitions: transitionsReducer,
    metaData: metaDataReducer,
    defaultState: defaultStateReducer,
    settings: SettingsReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .prepend(statesListener.middleware)
      .prepend(transitionsListener.middleware),
});

export type RootType = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
