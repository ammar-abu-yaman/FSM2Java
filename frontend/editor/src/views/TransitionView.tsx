import React from "react";
import { Circle, Arrow } from "react-konva";
import { ANCHOR_OFFSETS } from "../constants";
import { StateType, TransitionType } from "../types";
import { useAppDispatch, useAppSelector } from "../hooks";
import {
  addTransition,
  removeTransition,
} from "../reducers/TransitionsReducer";
import { TransitionOptionBarContent } from "../option/TransitionOptionBarContent";
import { paper, graph } from "../components/joint";
import * as joint from "jointjs";
import store from "../store";
import { setFocusedObject } from "../reducers/SettingsReducer";
import { OptionBarVariant } from "../util/optionbars";
import { CustomMenuItem } from "../components/CustomMenuItem";
import { updateMetaData } from "../reducers/MetaDataReducer";

export function createTransitionView(transition: TransitionType) {
  const view = new joint.shapes.standard.Link({
    id: transition.id,
    router: joint.routers.orthogonal,
    connector: joint.connectors.rounded,
    attrs: {
      line: {
        class: "transition-line",
        targetMarker: { class: "transition-arrow" },
      },
    },
  });
  view.appendLabel({
    attrs: {
      text: {
        text: "Hello, World!",
      },
    },
  });
  view.source(graph.getCell(transition.source));
  view.target(graph.getCell(transition.target));

  graph.addCell(view);
}

export class CustomLinkView extends joint.dia.LinkView {
  protected pointerclick(evt: any, x: number, y: number) {
    evt.preventDefault();
    store.dispatch(
      setFocusedObject({
        id: (this as any).model.id,
        type: "transition",
        optionbar: OptionBarVariant.Transition,
      })
    );
  }

  pointerup(evt: any, x: number, y: number) {
    super.pointerup(evt, x, y);
    if (
      store
        .getState()
        .transitions.some(
          (transition) => transition.id === (this as any).model.id
        )
    ) {
      return;
    }
    const currentPaper = this.paper as joint.dia.Paper;
    const model = (this as any).model;
    const targetView = currentPaper.findView(evt.target);

    if (
      !targetView ||
      !(targetView instanceof joint.dia.ElementView) ||
      (targetView as any).model.id === "default"
    ) {
      model.remove();
    } else {
      this.createNewLink(targetView);
    }
  }

  createNewLink(targetView: any) {
    const source: string = (this as any).sourceView.model.id;
    const target: string = targetView.model.id;
    store.dispatch(
      addTransition({
        id: "",
        name: "Transition",
        parameters: [],
        source,
        target,
        code: "",
      })
    );
    (this as any).model.remove();
  }
}

export function TransitionContextMenu({ id }: { id: string }) {
  const dispatch = useAppDispatch();
  const transition = useAppSelector((data) =>
    data.transitions.find((t) => t.id === id)
  ) as TransitionType;
  return (
    <>
      <CustomMenuItem
        text="Delete"
        onClick={() => dispatch(removeTransition(id))}
      />
    </>
  );
}
