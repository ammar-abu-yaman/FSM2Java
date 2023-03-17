import React from "react";
import { Circle, Arrow } from "react-konva";
import { ANCHOR_OFFSETS } from "../constants";
import { StateType, TransitionType } from "../types";
import { useAppDispatch, useAppSelector } from "../hooks";
import { addTransition } from "../reducers/TransitionsReducer";
import { useSetFocusedObject } from "../contexts/FocusedObjectContext";
import { TransitionOptionBarContent } from "../option/TransitionOptionBarContent";

export function TransitionArrow({ transitionId }: { transitionId: number }) {
  const setFocusedObject = useSetFocusedObject();
  const transition = useAppSelector(
    (state) =>
      state.transitions.find(
        (transition) => transition.id === transitionId
      ) as TransitionType
  );
  const { sourceId, destId } = transition;
  const source = useAppSelector(
    (state) => findState(state.states, sourceId.stateId) as StateType
  );
  const dest = useAppSelector(
    (state) => findState(state.states, destId.stateId) as StateType
  );
  const sourceCoordinates = [
    source.x + ANCHOR_OFFSETS[sourceId.anchor][0],
    source.y + ANCHOR_OFFSETS[sourceId.anchor][1],
  ];
  const destCoordinates = [
    dest.x + ANCHOR_OFFSETS[destId.anchor][0],
    dest.y + ANCHOR_OFFSETS[destId.anchor][1],
  ];
  return (
    <>
      <Arrow
        points={[...sourceCoordinates, ...destCoordinates]}
        stroke="black"
        fill="black"
        strokeWidth={4}
        pointerLength={10}
        pointerWidth={10}
        onClick={() =>
          setFocusedObject({
            id: transitionId,
            type: "transition",
            optionBarContent: () => (
              <TransitionOptionBarContent transitionId={transitionId} />
            ),
          })
        }
      />
    </>
  );
}

export function Anchors({
  stateId,
  connection,
  setConnection,
  isActive,
  points,
}: {
  stateId: number;
  connection: any;
  setConnection: any;
  isActive: boolean;
  points: { id: number; x: number; y: number }[];
}) {
  return (
    <>
      {points.map((point) => (
        <AnchorPoint
          key={point.id}
          stateId={stateId}
          point={point}
          isActive={isActive}
          connection={connection}
          setConnection={setConnection}
        />
      ))}
    </>
  );
}
function AnchorPoint({
  stateId,
  point,
  connection,
  setConnection,
  isActive,
}: {
  stateId: number;
  point: { id: number; x: number; y: number };
  connection: any;
  setConnection: any;
  isActive: boolean;
}) {
  const dispatch = useAppDispatch();
  if (!isActive && !isSourcePoint(connection, point, stateId)) return <></>;
  return (
    <>
      <Circle
        x={point.x}
        y={point.y}
        radius={5}
        onClick={() => {
          if (!connection?.from) {
            setConnection((connection: any) => ({
              ...connection,
              from: { stateId, anchor: point.id },
            }));
            return;
          }
          if (stateId == -1) return;
          dispatch(
            addTransition({
              id: -1,
              sourceId: connection.from,
              destId: { stateId, anchor: point.id },
              name: "-",
              parameters: [],
              code: "",
            })
          );

          setConnection(null);
        }}
        fill={isSourcePoint(connection, point, stateId) ? "blue" : "black"}
      />
    </>
  );
}
function isSourcePoint(
  connection: any,
  point: { id: number; x: number; y: number },
  stateId: number
) {
  return (
    connection?.from?.stateId === stateId &&
    connection?.from?.anchor === point.id
  );
}
function findState(states: StateType[], id: number) {
  return states.find((state) => state.id === id);
}
