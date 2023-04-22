import React from "react";
import { MetaData } from "../types";
import {
  Box,
  Button,
  FormControl,
  FormLabel,
  HStack,
  Input,
  Select,
  VStack,
  Text,
  Divider,
} from "@chakra-ui/react";
import { useAppDispatch, useAppSelector } from "../hooks";

import {
  addAction,
  removeAction,
  updateAction,
  updateMetaData,
} from "../reducers/MetaDataReducer";

export function DefaultOptionBarContent() {
  const dispatch = useAppDispatch();
  const states = useAppSelector((data) => data.states);
  const metaData = useAppSelector((data) => data.metaData);
  return (
    <VStack color={"whiteAlpha.800"} p="3">
      <Box>
        <Text fontSize={"lg"}>State Machine Settings</Text>
      </Box>
      <Divider orientation="horizontal" />
      <HStack mt="5" mb="1.5" alignItems="center">
        <FormLabel width={"30%"}>Package</FormLabel>
        <Input
          bg="blackAlpha.500"
          width={"80%"}
          value={metaData.package}
          onChange={(e) =>
            dispatch(updateMetaData({ package: e.target.value }))
          }
        />
      </HStack>
      <HStack mb="1.5" alignItems="center" justify={"space-between"}>
        <FormLabel width={"30%"}>Class</FormLabel>
        <Input
          bg="blackAlpha.500"
          width={"80%"}
          value={metaData.className}
          onChange={(e) =>
            dispatch(updateMetaData({ className: e.target.value }))
          }
        />
      </HStack>

      <HStack w="100%" mb="1.5" alignItems="center" justify={"space-between"}>
        <FormLabel width={"30%"}>Initial State</FormLabel>
        <Select
          bg="blackAlpha.500"
          display={"block"}
          width={"80%"}
          value={metaData.initialState}
          onChange={(e) =>
            dispatch(updateMetaData({ initialState: e.target.value }))
          }
        >
          <option value={""} hidden>
            Select State
          </option>
          {states.map((state) => (
            <option value={state.name}>{state.name}</option>
          ))}
        </Select>
      </HStack>

      <FormControl>
        <Button
          bg="gray.700"
          _hover={{ background: "gray.500" }}
          _active={{ background: "gray.600" }}
          onClick={() => dispatch(addAction())}
        >
          Add Action
        </Button>
      </FormControl>
      {metaData.actions.map((_action, actionId) => (
        <Parameter key={actionId} actionId={actionId} metaData={metaData} />
      ))}
    </VStack>
  );
}

function Parameter({
  actionId,
  metaData,
}: {
  actionId: number;
  metaData: MetaData;
}) {
  const dispatch = useAppDispatch();
  const value = metaData.actions[actionId];
  return (
    <HStack>
      <Input
        value={value}
        onChange={(e) =>
          dispatch(updateAction({ index: actionId, value: e.target.value }))
        }
      />
      <Button
        bg="red.700"
        _hover={{ background: "red.500" }}
        _active={{ background: "red.600" }}
        onClick={() => dispatch(removeAction(actionId))}
      >
        Remove
      </Button>
    </HStack>
  );
}
