import React from "react";
import { StateType } from "../types";
import {
  Button,
  FormControl,
  FormLabel,
  Input,
  VStack,
} from "@chakra-ui/react";
import { useAppDispatch, useAppSelector } from "../hooks";
import { updateState } from "../reducers/StateReducer";
import { useSetCodeEdition } from "../contexts/CodeEditionContext";

export function StateOptionBarContent({ stateId }: { stateId: number }) {
  const dispatch = useAppDispatch();
  const state = useAppSelector(
    (state) => state.states.find((state) => state.id === stateId) as StateType
  );
  const setCodeEdition = useSetCodeEdition();
  return (
    <VStack>
      <FormControl mb={3}>
        <FormLabel>Name</FormLabel>
        <Input
          value={state.name}
          onChange={(e) =>
            dispatch(updateState({ ...state, name: e.target.value }))
          }
        />
      </FormControl>
      <FormControl>
        <Button
          onClick={() =>
            setCodeEdition({
              id: state.id,
              type: "state",
              label: "On Enter Code",
              getValue: (data) =>
                data.states.find((item) => item.id === state.id)?.entryCode ??
                "",
              onChange: (value) => {
                dispatch(updateState({ ...state, entryCode: value }));
              },
            })
          }
        >
          Edit Entry Code
        </Button>
      </FormControl>
      <FormControl>
        <Button
          onClick={() =>
            setCodeEdition({
              id: state.id,
              type: "state",
              label: "On Exit Code",
              getValue: (data) =>
                data.states.find((item) => item.id === state.id)?.exitCode ??
                "",
              onChange: (value) => {
                dispatch(updateState({ ...state, exitCode: value }));
              },
            })
          }
        >
          Edit Exit Code
        </Button>
      </FormControl>
    </VStack>
  );
}
