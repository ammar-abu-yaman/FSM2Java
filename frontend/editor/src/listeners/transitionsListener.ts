import { createListenerMiddleware } from "@reduxjs/toolkit";
import { graph, paper } from "../components/joint";
import { setFocusedObject } from "../reducers/SettingsReducer";

import { addState, updateState } from "../reducers/StateReducer";
import {
  addTransition,
  removeTransition,
  updateTransition,
} from "../reducers/TransitionsReducer";
import { RootType } from "../store";
import { StateType, TransitionType } from "../types";
import {
  createTransitionView,
  getTransitionLabel,
} from "../views/TransitionView";

const transitionsListener = createListenerMiddleware();

transitionsListener.startListening({
  actionCreator: addTransition,
  effect: (action, api) => {
    const transition = getNewTransition(
      (api.getOriginalState() as RootType).transitions,
      (api.getState() as RootType).transitions
    );
    createTransitionView(transition);
  },
});

transitionsListener.startListening({
  actionCreator: removeTransition,
  effect: (action, api) => {
    api.dispatch(setFocusedObject(null));
    const id = action.payload;
    graph.getCell(id).remove();
  },
});

transitionsListener.startListening({
  actionCreator: updateTransition,
  effect: (action, api) => {
    const transition = action.payload;
    const view = graph.getCell(transition.id) as joint.shapes.standard.Link;
    view.removeLabel(0);
    view.appendLabel({
      attrs: {
        text: {
          text: getTransitionLabel(transition),
        },
      },
    });
  },
});

function getNewTransition(
  oldStates: TransitionType[],
  newStates: TransitionType[]
) {
  return newStates.filter(
    (state) => !oldStates.some((old) => old.id === state.id)
  )[0];
}

export default transitionsListener;
