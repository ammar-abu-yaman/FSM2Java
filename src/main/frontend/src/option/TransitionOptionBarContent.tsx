import React from "react";
import { TransitionType } from "../types";
import {
  Box,
  Button,
  Divider,
  FormControl,
  FormLabel,
  HStack,
  Input,
  VStack,
  Text,
} from "@chakra-ui/react";
import { useAppDispatch, useAppSelector } from "../hooks";
import {
  addTransitionParameter,
  removeTransitionParameter,
  updateTransition,
  updateTransitionParameter,
} from "../reducers/TransitionsReducer";
import { setCodeEdition } from "../reducers/SettingsReducer";

export function TransitionOptionBarContent({
  transitionId,
}: {
  transitionId: string;
}) {
  const dispatch = useAppDispatch();
  const transition = useAppSelector(
    (state) =>
      state.transitions.find(
        (item) => item.id === transitionId
      ) as TransitionType
  );

  return (
    <VStack color={"whiteAlpha.800"} p="3">
      <Box>
        <Text fontSize={"lg"}>Transition Settings</Text>
      </Box>
      <Divider orientation="horizontal" />
      <HStack mt="5" mb="1.5" alignItems="center">
        <FormLabel width={"30%"}>Name</FormLabel>
        <Input
          bg="blackAlpha.500"
          width={"80%"}
          value={transition.name}
          onChange={(e) =>
            dispatch(updateTransition({ ...transition, name: e.target.value }))
          }
        />
      </HStack>

      <FormControl>
        <Button
          mt={"1.5"}
          mb={"2"}
          bg="gray.700"
          _hover={{ background: "gray.500" }}
          _active={{ background: "gray.600" }}
          onClick={editCondition()}
        >
          Edit Condition
        </Button>
      </FormControl>
      <FormControl>
        <Button
          mb={"2"}
          bg="gray.700"
          _hover={{ background: "gray.500" }}
          _active={{ background: "gray.600" }}
          onClick={editCode()}
        >
          Edit Code
        </Button>
      </FormControl>
      <FormControl>
        <Button
          mb={"2"}
          bg="gray.700"
          _hover={{ background: "gray.500" }}
          _active={{ background: "gray.600" }}
          onClick={() => dispatch(addTransitionParameter(transitionId))}
        >
          Add Parameter
        </Button>
      </FormControl>
      {transition.parameters.map((_param, paramId) => (
        <Parameter key={paramId} paramId={paramId} transition={transition} />
      ))}
    </VStack>
  );

  function editCode() {
    return () => {
      dispatch(
        setCodeEdition({
          id: transitionId,
          type: "transition",
          label: "Edit Code",
          getValue: (data) =>
            data.transitions.find((item) => item.id === transition.id)?.code ??
            "",
          onChange: (newValue) => {
            dispatch(updateTransition({ ...transition, code: newValue }));
          },
        })
      );
    };
  }

  function editCondition() {
    return () => {
      dispatch(
        setCodeEdition({
          id: transitionId,
          type: "transition",
          label: "Edit Condition",
          getValue: (data) =>
            data.transitions.find((item) => item.id === transition.id)
              ?.condition ?? "",
          onChange: (newValue) => {
            dispatch(updateTransition({ ...transition, condition: newValue }));
          },
        })
      );
    };
  }
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
    <HStack alignItems={"flex-end"}>
      <VStack alignItems={"flex-start"}>
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
      <VStack alignItems={"flex-start"}>
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
          bg="red.700"
          _hover={{ background: "red.500" }}
          _active={{ background: "red.600" }}
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
