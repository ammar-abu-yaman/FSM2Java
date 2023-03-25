import React, { DOMElement, useEffect, useRef, useState } from "react";
import { Stage, Layer } from "react-konva";
import Konva from "konva";
import { SCALE_FACTOR } from "../constants";
import { Button, Flex, Menu, MenuItem, MenuList } from "@chakra-ui/react";
import { useAppDispatch, useAppSelector } from "../hooks";
import * as joint from "jointjs";
import {
  addDefaultState,
  addState,
  generateFsmCode,
} from "../reducers/StateReducer";
import { graph, initPaper, paper } from "./joint";
import { CustomMenuItem } from "./CustomMenuItem";
import { TransitionContextMenu } from "../views/TransitionView";
import { StateContextMenu } from "../views/StateView";

export function Canvas() {
  const [contextMenuObject, setContextMenuObject] = useState({
    id: "",
    type: "",
  });
  const [isContextMenuActive, setIsContextMenuActive] = useState(false);
  const [contextMenuCoordinates, setContextMenuCoordinates] = useState({
    top: "0",
    left: "0",
  });
  const states = useAppSelector((state) => state.states);
  const dispatch = useAppDispatch();
  const paperElRef = useRef(null);

  useEffect(() => {
    if (!paperElRef.current) return;
    if (!paper) initPaper(paperElRef.current);

    const onWindowClick = () => {
      // hide menu
      setContextMenuObject({ id: "", type: "" });
      setIsContextMenuActive(false);
    };

    const onContextMenuBlank = (evt: any, x: number, y: number) => {
      evt.preventDefault();
      setContextMenuCoordinates({
        left: `${evt.pageX}`,
        top: `${evt.pageY}`,
      });
      setIsContextMenuActive(true);
    };

    const onContextMenuLink = (view, evt, x: number, y: number) => {
      evt.preventDefault();
      setContextMenuCoordinates({
        left: `${evt.pageX}`,
        top: `${evt.pageY}`,
      });
      setIsContextMenuActive(true);
      setContextMenuObject({ id: view.model.id, type: "transition" });
    };

    const onContextMenuElement = (view, evt, x: number, y: number) => {
      evt.preventDefault();
      setContextMenuCoordinates({
        left: `${evt.pageX}`,
        top: `${evt.pageY}`,
      });
      setIsContextMenuActive(true);
      setContextMenuObject({ id: view.model.id, type: "state" });
    };

    const onWheel = (evt: any, x: number, y: number, delta: number) => {
      const MIN_SCALE = 0.4,
        MAX_SCALE = 2.5;

      evt.preventDefault();

      const oldScale = paper.scale().sx;
      const newScale = oldScale + delta * 0.1;

      if (newScale >= MIN_SCALE && newScale <= MAX_SCALE) {
        const currentScale = paper.scale().sx;

        const beta = currentScale / newScale;

        const ax = x - x * beta;
        const ay = y - y * beta;

        const translate = paper.translate();

        const nextTx = translate.tx - ax * newScale;
        const nextTy = translate.ty - ay * newScale;

        paper.translate(nextTx, nextTy);

        const ctm = paper.matrix();

        ctm.a = newScale;
        ctm.d = newScale;

        paper.matrix(ctm);
      }
    };
    const onWheelCell = (
      cell: any,
      evt: any,
      x: number,
      y: number,
      delta: number
    ) => onWheel(evt, x, y, delta);

    paper.on("blank:contextmenu", onContextMenuBlank);
    paper.on("link:contextmenu", onContextMenuLink);
    paper.on("element:contextmenu", onContextMenuElement);
    paper.on("cell:mousewheel", onWheelCell);
    paper.on("blank:mousewheel", onWheel);
    window.addEventListener("click", onWindowClick);
    return () => {
      // remove unneeded event listners
      (paper as any).off("cell:mousewheel", onWheelCell);
      (paper as any).off("blank:mousewheel", onWheel);
      (paper as any).off("blank:contextmenu", onContextMenuBlank);
      (paper as any).off("link:contextmenu", onContextMenuLink);
      (paper as any).off("element:contextmenu", onContextMenuElement);
      graph.clear();
      window.removeEventListener("click", onWindowClick);
    };
  }, []);

  return (
    <>
      <Menu isLazy isOpen={isContextMenuActive}>
        <MenuList
          borderColor={"gray.300"}
          position={"absolute"}
          top={contextMenuCoordinates.top}
          left={contextMenuCoordinates.left}
          color="white"
          bg="#212226"
        >
          <ContextMenuContent
            contextMenuCoordinates={contextMenuCoordinates}
            contextObject={contextMenuObject}
          />
        </MenuList>
      </Menu>
      <div id="holder" ref={paperElRef} />
    </>
  );
}

function ContextMenuContent({
  contextMenuCoordinates,
  contextObject,
}: {
  contextObject: { id: string; type: string };
  contextMenuCoordinates: { top: string; left: string };
}) {
  switch (contextObject.type) {
    case "state":
      return <StateContextMenu id={contextObject.id} />;
    case "transition":
      return <TransitionContextMenu id={contextObject.id} />;
    default:
      return (
        <DefaultContextMenu contextMenuCoordinates={contextMenuCoordinates} />
      );
  }
}

function DefaultContextMenu({
  contextMenuCoordinates,
}: {
  contextMenuCoordinates: { top: string; left: string };
}) {
  const dispatch = useAppDispatch();
  const states = useAppSelector((data) => data.states);
  const addDefaultStateAction = () => {
    const { x, y } = paper.clientToLocalPoint(
      parseFloat(contextMenuCoordinates.left),
      parseFloat(contextMenuCoordinates.top)
    );
    dispatch(
      addDefaultState({
        id: "default",
        x,
        y,
        name: "Default",
        entryCode: "",
        exitCode: "",
      })
    );
  };
  const addStateAction = () => {
    const { x, y } = paper.clientToLocalPoint(
      parseFloat(contextMenuCoordinates.left),
      parseFloat(contextMenuCoordinates.top)
    );

    dispatch(
      addState({
        id: "",
        x,
        y,
        name: "State",
        entryCode: "",
        exitCode: "",
      })
    );
  };

  return (
    <>
      <CustomMenuItem text="Add State" onClick={addStateAction} />
      <CustomMenuItem
        text="Add Default State"
        hidden={states.some((state) => state.id === "default")}
        onClick={addDefaultStateAction}
      />
    </>
  );
}
