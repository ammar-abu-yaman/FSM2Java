import React, { useEffect, useRef, useState } from "react";
import { SCALE_FACTOR } from "../constants";
import { Menu, MenuList } from "@chakra-ui/react";
import { useAppDispatch, useAppSelector } from "../hooks";
import * as joint from "jointjs";
import { addDefaultState, addState } from "../reducers/StateReducer";
import { graph, initPaper, paper } from "./joint";
import { CustomMenuItem } from "./CustomMenuItem";
import { TransitionContextMenu } from "../views/TransitionView";
import { StateContextMenu } from "../views/StateView";
import { setFocusedObject } from "../reducers/SettingsReducer";

export function Canvas(): JSX.Element {
  const [contextMenuObject, setContextMenuObject] = useState({
    id: "",
    type: "",
  });
  const [isContextMenuActive, setIsContextMenuActive] = useState(false);
  const [contextMenuCoordinates, setContextMenuCoordinates] = useState({
    top: "0",
    left: "0",
  });

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
    const cellWheel = (
      cell: any,
      evt: any,
      x: number,
      y: number,
      delta: number
    ) => onWheel(evt, x, y, delta);

    const cellClick = (cellView) => {
      console.log(cellView);
      cellView.showTools();
    };

    const clickUnfocus = () => {
      dispatch(setFocusedObject(null));
      paper.hideTools();
    };

    paper.on("link:pointerclick element:pointerclick", cellClick);

    paper.on("blank:pointerclick cell:pointerclick", clickUnfocus);

    paper.on("blank:contextmenu", onContextMenuBlank);
    paper.on("link:contextmenu", onContextMenuLink);
    paper.on("element:contextmenu", onContextMenuElement);
    paper.on("cell:mousewheel", cellWheel);
    paper.on("blank:mousewheel", onWheel);
    window.addEventListener("click", onWindowClick);

    paper.hideTools();

    return () => {
      // bypass typescript typesystem
      let paperAny = paper as any;
      // remove unneeded event listners
      paperAny.off("cell:mousewheel", cellWheel);
      paperAny.off("blank:mousewheel", onWheel);
      paperAny.off("blank:contextmenu", onContextMenuBlank);
      paperAny.off("link:contextmenu", onContextMenuLink);
      paperAny.off("element:contextmenu", onContextMenuElement);

      paperAny.off("element:contextmenu", onContextMenuElement);

      paperAny.off("link:pointerclick element:pointerclick", cellClick);

      paperAny.off("blank:pointerclick cell:pointerclick", clickUnfocus);

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
        id: "base",
        x,
        y,
        name: "Base",
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
        text="Add Base State"
        hidden={states.some((state) => state.id === "base")}
        onClick={addDefaultStateAction}
      />
    </>
  );
}
