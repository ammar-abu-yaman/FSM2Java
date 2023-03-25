import React from "react";
import { StateType } from "../types";
import {
  Button,
  FormControl,
  FormLabel,
  Input,
  VStack,
  Text,
  Box,
  Divider,
  HStack,
} from "@chakra-ui/react";
import { useAppDispatch, useAppSelector } from "../hooks";
import { updateState } from "../reducers/StateReducer";
import { setCodeEdition } from "../reducers/SettingsReducer";

export function StateOptionBarContent({ stateId }: { stateId: string }) {
  const dispatch = useAppDispatch();
  const state = useAppSelector(
    (state) => state.states.find((state) => state.id === stateId) as StateType
  );
  return (
    <VStack color={"whiteAlpha.800"} p="3">
      <Box>
        <Text fontSize={"lg"}>State Settings</Text>
      </Box>
      <Divider orientation="horizontal" />
      <HStack mt="5" mb="1.5" alignItems="center">
        <FormLabel width={"30%"}>Name</FormLabel>
        <Input
          bg="blackAlpha.500"
          width={"80%"}
          value={state.name}
          onChange={(e) =>
            dispatch(updateState({ ...state, name: e.target.value }))
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
          onClick={() =>
            dispatch(
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
            )
          }
        >
          Edit Entry Code
        </Button>
      </FormControl>
      <FormControl>
        <Button
          mb={"2"}
          bg="gray.700"
          _hover={{ background: "gray.500" }}
          _active={{ background: "gray.600" }}
          onClick={() =>
            dispatch(
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
            )
          }
        >
          Edit Exit Code
        </Button>
      </FormControl>
    </VStack>
  );
}
