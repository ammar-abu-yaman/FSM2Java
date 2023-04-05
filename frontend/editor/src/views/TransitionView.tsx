import React from "react";
import { Circle, Arrow } from "react-konva";
import { ANCHOR_OFFSETS } from "../constants";
import { StateType, TransitionType } from "../types";
import { useAppDispatch, useAppSelector } from "../hooks";
import {
  addTransition,
  removeTransition,
  updateTransition,
} from "../reducers/TransitionsReducer";
import { paper, graph } from "../components/joint";
import * as joint from "jointjs";
import store from "../store";
import { setFocusedObject } from "../reducers/SettingsReducer";
import { OptionBarVariant } from "../util/optionbars";
import { CustomMenuItem } from "../components/CustomMenuItem";

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
        text: getTransitionLabel(transition),
      },
    },
  });
  view.source(graph.getCell(transition.source));
  view.target(graph.getCell(transition.target));

  graph.addCell(view);

  const linkTools = new joint.dia.ToolsView({
    tools: [
      new joint.linkTools.Vertices(),
      new joint.linkTools.TargetArrowhead(),
      new joint.linkTools.SourceArrowhead(),
    ],
  });

  paper.findViewByModel(view).addTools(linkTools);
}

export class CustomLinkView extends joint.dia.LinkView {
  protected pointerclick(evt: any, x: number, y: number) {
    super.pointerclick(evt, x, y);
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
      this.handleExisting(evt);
      return;
    }
    const currentPaper = this.paper as joint.dia.Paper;
    const model = (this as any).model;
    const targetView = currentPaper.findView(evt.target);

    if (this.isInvalidTarget(targetView)) {
      model.remove();
    } else {
      this.createNewLink(targetView);
    }
  }

  private isInvalidTarget(
    targetView: joint.dia.LinkView | joint.dia.ElementView
  ) {
    return (
      !targetView ||
      !(targetView instanceof joint.dia.ElementView) ||
      (targetView as any).model.id === "default"
    );
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

  handleExisting(evt: any) {
    const transition = store
      .getState()
      .transitions.find(
        (transition) => transition.id === (this as any).model.id
      ) as TransitionType;

    const source = this.getSource(
      transition.source,
      (this as any).model.source()?.id
    );

    const target = this.getTarget(
      transition.target,
      (this as any).model.target()?.id
    );

    const model = (this as any).model;

    if (source !== (this as any).model.source()?.id)
      model.source(graph.getCell(source));
    if (target !== (this as any).model.target()?.id)
      model.target(graph.getCell(target));

    this.showTools();
    store.dispatch(updateTransition({ ...transition, source, target }));
  }
  getTarget(target: string, viewTargetId: any) {
    if (!viewTargetId || viewTargetId === "default") return target;
    return viewTargetId;
  }
  getSource(source: string, viewSourceId: any) {
    if (!viewSourceId) return source;
    return viewSourceId;
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

export function getTransitionLabel(transition: TransitionType) {
  const name = transition.name;
  const action =
    transition.code?.length > 0
      ? "/" + transition.code.substring(0, Math.min(10, transition.code.length))
      : "";
  const condition =
    transition.condition?.length > 0 ? " IF " + transition.condition : "";
  return name + action + condition;
}
