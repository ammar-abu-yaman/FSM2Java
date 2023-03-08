import React from "react";
import { StateType, TransitionType } from "../types";
import {
  Button,
  FormControl,
  FormLabel,
  HStack,
  Input,
  VStack,
} from "@chakra-ui/react";
import { useAppDispatch, useAppSelector } from "../hooks";
import { useSetCodeEdition } from "../contexts/CodeEditionContext";
import {
  addTransitionParameter,
  removeTransitionParameter,
  updateTransition,
  updateTransitionParameter,
} from "../reducers/TransitionsReducer";

export function TransitionOptionBarContent({
  transitionId,
}: {
  transitionId: number;
}) {
  const dispatch = useAppDispatch();
  const transition = useAppSelector(
    (state) =>
      state.transitions.find(
        (item) => item.id === transitionId
      ) as TransitionType
  );

  const setCodeEdition = useSetCodeEdition();
  return (
    <VStack>
      <FormControl mb={3}>
        <FormLabel>Name</FormLabel>
        <Input
          value={transition.name}
          onChange={(e) =>
            dispatch(updateTransition({ ...transition, name: e.target.value }))
          }
        />
      </FormControl>
      <FormControl>
        <Button
          onClick={() => {
            setCodeEdition({
              id: transitionId,
              type: "transition",
              label: "Edit Condition",
              getValue: (data) =>
                data.transitions.find((item) => item.id === transition.id)
                  ?.condition ?? "",
              onChange: (newValue) => {
                dispatch(
                  updateTransition({ ...transition, condition: newValue })
                );
              },
            });
          }}
        >
          Edit Condition
        </Button>
      </FormControl>
      <FormControl>
        <Button onClick={() => dispatch(addTransitionParameter(transitionId))}>
          Add Parameter
        </Button>
      </FormControl>
      {transition.parameters.map((_param, paramId) => (
        <Parameter key={paramId} paramId={paramId} transition={transition} />
      ))}
    </VStack>
  );
}

function Parameter({
  transition,
  paramId,
}: {
  transition: TransitionType;
  paramId: number;
}) {
  const dispatch = useAppDispatch();
  const { name, type } = transition.parameters[paramId];
  return (
    <HStack>
      <VStack>
        <FormLabel>Name</FormLabel>
        <Input
          value={name}
          onChange={(e) =>
            dispatch(
              updateTransitionParameter({
                transitionId: transition.id,
                paramId,
                param: { name: e.target.value },
              })
            )
          }
        />
      </VStack>
      <VStack>
        <FormLabel>Type</FormLabel>
        <Input
          value={type}
          onChange={(e) =>
            dispatch(
              updateTransitionParameter({
                transitionId: transition.id,
                paramId,
                param: { type: e.target.value },
              })
            )
          }
        />
      </VStack>
      <VStack>
        <Button
          onClick={() =>
            dispatch(
              removeTransitionParameter({
                transitionId: transition.id,
                paramId,
              })
            )
          }
        >
          Remove
        </Button>
      </VStack>
    </HStack>
  );
}
