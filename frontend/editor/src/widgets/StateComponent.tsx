import React, { useState } from "react";
import { Circle, Group } from "react-konva";
import { useSetFocusedObject } from "../contexts/FocusedObjectContext";
import { ANCHOR_OFFSETS } from "../constants";
import { DispatchType, StateType } from "../types";
import { useAppDispatch, useAppSelector } from "../hooks";
import { updateState } from "../reducers/StateReducer";
import { StateOptionBarContent } from "../util/StateOptionBarContent";
import { Anchors } from "./TransitionArrow";

export function StateComponent({
  stateId,
  connection,
  setConnection,
}: {
  stateId: number;
  connection: any;
  setConnection: DispatchType<any>;
}) {
  const state = useAppSelector(
    (state) => state.states.find((state) => state.id === stateId) as StateType
  );
  const dispatch = useAppDispatch();
  const setFocusedObject = useSetFocusedObject();
  const [isHover, setIsHover] = useState(false);
  return (
    <>
      <Group
        onClick={(e) =>
          setFocusedObject({
            id: state.id,
            type: "state",
            optionBarContent: () => <StateOptionBarContent stateId={stateId} />,
          })
        }
        x={state.x}
        y={state.y}
        draggable
        onDragMove={(e) => {
          dispatch(
            updateState({
              ...state,
              x: e.target.x(),
              y: e.target.y(),
            })
          );
        }}
        onMouseOver={(e) => setIsHover(true)}
        onMouseOut={(e: any) => {
          const xdiff = state.x - e.evt.layerX;
          const ydiff = state.y - e.evt.layerY;
          const distance = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
          if (distance >= 50.0) setIsHover(false);
        }}
      >
        <Circle radius={55} opacity={0} />
        <Circle
          radius={40}
          fill="white"
          stroke={"black"}
          strokeWidth={2}
          strokeEnabled
        />
      </Group>
      <Anchors
        stateId={state.id}
        points={ANCHOR_OFFSETS.map(([dx, dy], id) => ({
          id,
          x: state.x + dx,
          y: state.y + dy,
        }))}
        isActive={isHover}
        connection={connection}
        setConnection={setConnection}
      />
    </>
  );
}
