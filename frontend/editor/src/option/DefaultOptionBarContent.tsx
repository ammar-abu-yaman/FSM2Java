import React from "react";
import { MetaData } from "../types";
import {
  Button,
  FormControl,
  FormLabel,
  HStack,
  Input,
  Select,
  VStack,
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
    <VStack>
      <FormControl mb={3}>
        <FormLabel>Package</FormLabel>
        <Input
          value={metaData.package}
          onChange={(e) =>
            dispatch(updateMetaData({ package: e.target.value }))
          }
        />
      </FormControl>
      <FormControl mb={3}>
        <FormLabel>Class</FormLabel>
        <Input
          value={metaData.className}
          onChange={(e) =>
            dispatch(updateMetaData({ className: e.target.value }))
          }
        />
      </FormControl>

      <FormControl mb={3}>
        <FormLabel>Initial State</FormLabel>
        <Select value={metaData.initialState}>
          {states.map((state) => (
            <option value={state.name}>{state.name}</option>
          ))}
        </Select>
      </FormControl>

      <FormControl>
        <Button onClick={() => dispatch(addAction())}>Add Action</Button>
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
      <Button onClick={() => dispatch(removeAction(actionId))}>Remove</Button>
    </HStack>
  );
}
