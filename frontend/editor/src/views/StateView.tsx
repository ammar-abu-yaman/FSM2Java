import React from "react";
import { StateType } from "../types";
import { StateOptionBarContent } from "../option/StateOptionBarContent";
import * as joint from "jointjs";
import "../shapes.css";
import store from "../store";
import { setFocusedObject } from "../reducers/SettingsReducer";
import { OptionBarVariant } from "../util/optionbars";
import { useAppDispatch, useAppSelector } from "../hooks";
import { CustomMenuItem } from "../components/CustomMenuItem";
import { removeState } from "../reducers/StateReducer";

const STATE_WIDTH = 150;
const STATE_HEIGHT = 60;

export function createStateView(
  state: StateType,
  paper: joint.dia.Paper,
  graph: joint.dia.Graph
) {
  const view = createView(state, "state-body", "state-label");
  view.addTo(graph);
  const onClick = () => {
    const focusedObject = {
      id: state.id,
      type: "state",
      optionbar: OptionBarVariant.State,
    };
    store.dispatch(setFocusedObject(focusedObject));
  };
  const connect = new joint.elementTools.Connect();
  const tools = new joint.dia.ToolsView({ tools: [connect] });

  (view.findView(paper) as any).on("element:pointerclick", onClick);
  view.findView(paper).addTools(tools);
}

export function createDefaultStateView(
  state: StateType,
  paper: joint.dia.Paper,
  graph: joint.dia.Graph
) {
  const view = createView(state, "default-state-body", "default-state-label");
  view.addTo(graph);

  const onClick = () => {
    const focusedObject = {
      id: state.id,
      type: "state",
      optionbar: OptionBarVariant.DefaultState,
    };
    store.dispatch(setFocusedObject(focusedObject));
  };
  const connect = new joint.elementTools.Connect();
  const tools = new joint.dia.ToolsView({ tools: [connect] });

  (view.findView(paper) as any).on("element:pointerclick", onClick);
  view.findView(paper).addTools(tools);
}

function createView(state: StateType, bodyClass: string, labelClass: string) {
  const view = new joint.shapes.standard.Rectangle({ id: state.id });
  view.position(state.x, state.y);
  view.size(STATE_WIDTH, STATE_HEIGHT);
  view.attr({
    body: {
      class: bodyClass,
      rx: 4,
      ry: 4,
    },
    label: {
      class: labelClass,
      text: state.name,
    },
  });

  return view;
}

export function updateStateView(
  state: StateType,
  paper: joint.dia.Paper,
  graph: joint.dia.Graph
) {
  const id = state.id;
  const view = graph.getCell(id);
  view.attr("label/text", state.name);
}

export function StateContextMenu({ id }: { id: string }) {
  const dispatch = useAppDispatch();
  const state = useAppSelector((data) =>
    data.states.find((t) => t.id === id)
  ) as StateType;
  return (
    <>
      <CustomMenuItem text="Delete" onClick={() => dispatch(removeState(id))} />
    </>
  );
}
