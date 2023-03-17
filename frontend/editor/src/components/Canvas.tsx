import React, { useEffect, useRef, useState } from "react";
import { Stage, Layer } from "react-konva";
import Konva from "konva";
import { useSetFocusedObject } from "../contexts/FocusedObjectContext";
import { SCALE_FACTOR } from "../constants";
import { Button, Menu, MenuItem, MenuList } from "@chakra-ui/react";
import { useAppDispatch, useAppSelector } from "../hooks";
import {
  addDefaultState,
  addState,
  generateFsmCode,
} from "../reducers/StateReducer";
import { TransitionArrow } from "../widgets/TransitionArrow";
import { StateComponent } from "../widgets/StateComponent";

export function Canvas({
  newElement,
  setNewElement,
}: {
  newElement: any;
  setNewElement: any;
}) {
  const layerRef = useRef<Konva.Layer>();
  const stageRef = useRef<Konva.Stage>();
  const setFocusedObject = useSetFocusedObject();
  const [connection, setConnection] = useState({ from: null, to: null });
  const [isContextMenuActive, setIsContextMenuActive] = useState(false);
  const [contextMenuCoordinates, setContextMenuCoordinates] = useState({
    top: "0",
    left: "0",
  });
  const states = useAppSelector((state) => state.states);
  const transitions = useAppSelector((state) => state.transitions);
  const dispatch = useAppDispatch();

  useEffect(() => {
    const stage: Konva.Stage = stageRef.current as Konva.Stage;

    // add zoom in and out effect to the editor
    const onWheel = (e: Konva.KonvaEventObject<WheelEvent>) => {
      // stop default scrolling
      e.evt.preventDefault();

      var oldScale = stage.scaleX();
      var pointer = stage.getPointerPosition() as Konva.Vector2d;

      var mousePointTo = {
        x: (pointer.x - stage.x()) / oldScale,
        y: (pointer.y - stage.y()) / oldScale,
      };

      // how to scale? Zoom in? Or zoom out?
      let direction = e.evt.deltaY > 0 ? 1 : -1;

      // when we zoom on trackpad, e.evt.ctrlKey is true
      // in that case lets revert direction
      if (e.evt.ctrlKey) {
        direction = -direction;
      }

      var newScale =
        direction > 0 ? oldScale * SCALE_FACTOR : oldScale / SCALE_FACTOR;

      stage.scale({ x: newScale, y: newScale });

      var newPos = {
        x: pointer.x - mousePointTo.x * newScale,
        y: pointer.y - mousePointTo.y * newScale,
      };
      stage.position(newPos);
    };

    const onWindowClick = () => {
      // hide menu
      setIsContextMenuActive(false);
    };

    stage.on("wheel", onWheel);
    window.addEventListener("click", onWindowClick);
    return () => {
      // remove unneeded event listners
      stage.removeEventListener("wheel");
      window.removeEventListener("click", onWindowClick);
    };
  }, [newElement]);

  return (
    <>
      <Button onClick={() => dispatch(generateFsmCode())}>Download</Button>
      <Menu isLazy isOpen={isContextMenuActive}>
        <MenuList
          position={"absolute"}
          top={contextMenuCoordinates.top}
          left={contextMenuCoordinates.left}
        >
          <MenuItem
            onClick={() => {
              const position =
                stageRef.current?.getPointerPosition() as Konva.Vector2d;
              dispatch(
                addState({
                  id: states.length,
                  x: position.x,
                  y: position.y,
                  name: "State",
                  entryCode: "",
                  exitCode: "",
                })
              );
            }}
          >
            Add State
          </MenuItem>
          <MenuItem
            disabled={states.find((state) => state.id === -1) !== undefined}
            onClick={() => {
              const position =
                stageRef.current?.getPointerPosition() as Konva.Vector2d;
              dispatch(
                addDefaultState({
                  id: -1,
                  x: position.x,
                  y: position.y,
                  name: "Default",
                  entryCode: "",
                  exitCode: "",
                })
              );
            }}
          >
            Add Default State
          </MenuItem>
        </MenuList>
      </Menu>
      <Stage
        onContextMenu={(e) => {
          let stage = stageRef.current;
          if (!stage) return;
          var containerRect = stage.container().getBoundingClientRect();
          setContextMenuCoordinates({
            top:
              containerRect.top +
              (stage?.getPointerPosition()?.y as number) +
              4 +
              "px",
            left:
              containerRect.left +
              (stage?.getPointerPosition()?.x as number) +
              4 +
              "px",
          });

          e.evt.preventDefault();
          if (e.target !== stageRef.current) {
            setIsContextMenuActive(false);
            return;
          }
          setIsContextMenuActive(true);
        }}
        ref={stageRef as any}
        width={window.innerWidth}
        height={window.innerHeight}
        onClick={(e) => {
          if (e.target === stageRef.current) {
            setFocusedObject(null);
          }
        }}
      >
        <Layer ref={layerRef as any}>
          {states.map((state) => (
            <StateComponent
              key={state.id}
              stateId={state.id}
              connection={connection}
              setConnection={setConnection}
            />
          ))}
          {transitions.map((transition) => (
            <TransitionArrow key={transition.id} transitionId={transition.id} />
          ))}
        </Layer>
      </Stage>
    </>
  );
}
