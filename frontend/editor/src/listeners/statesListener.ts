import { createListenerMiddleware, isAnyOf } from "@reduxjs/toolkit";
import { graph, paper } from "../components/joint";
import { setFocusedObject } from "../reducers/SettingsReducer";

import {
  addDefaultState,
  addState,
  removeState,
  updateState,
} from "../reducers/StateReducer";
import {
  removeTransition,
  removeTransitions,
} from "../reducers/TransitionsReducer";
import { RootType } from "../store";
import { StateType } from "../types";
import {
  createDefaultStateView,
  createStateView,
  updateStateView,
} from "../views/StateView";

const statesListener = createListenerMiddleware();

statesListener.startListening({
  actionCreator: addState,
  effect: (action, api) => {
    const state = getNewState(
      (api.getOriginalState() as RootType).states,
      (api.getState() as RootType).states
    );
    createStateView(state, paper, graph);
  },
});

statesListener.startListening({
  actionCreator: addDefaultState,
  effect: (action, api) => {
    const state = getNewState(
      (api.getOriginalState() as RootType).states,
      (api.getState() as RootType).states
    );
    createDefaultStateView(state, paper, graph);
  },
});

statesListener.startListening({
  actionCreator: updateState,
  effect: (action, api) => {
    const state = action.payload;
    updateStateView(state, paper, graph);
  },
});

statesListener.startListening({
  actionCreator: removeState,
  effect: (action, api) => {
    const id = action.payload;
    const { transitions } = api.getState() as RootType;
    api.dispatch(setFocusedObject(null));
    graph.getCell(id).remove();
    api.dispatch(
      removeTransitions(
        transitions
          .filter(
            (transition) => transition.source === id || transition.target === id
          )
          .map((transition) => transition.id)
      )
    );
  },
});

function getNewState(oldStates: StateType[], newStates: StateType[]) {
  return newStates.filter(
    (state) => !oldStates.some((old) => old.id === state.id)
  )[0];
}

export default statesListener;

// export default listenerMiddleware;

// const store = configureStore({
//   reducer: {
//     todos: todosReducer,
//   },
//   middleware: (getDefaultMiddleware) =>
//     getDefaultMiddleware().prepend(listenerMiddleware.middleware),
// });
